//Main class that handles all overview of the game
package com.mygdx.game;

import java.util.LinkedList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class ScienceSorcerySiege extends ApplicationAdapter{
	SpriteBatch batch;
	float w; //The width of the screen in pixels
	float h; //The width of the screen in pixels
	Field map; //The game map that players traverse
	OrthographicCamera camera; //The camera that displays certain points of the screen
	TiledMapRenderer tiledMapRenderer; //Allows the tiled map to be rendered
	Player player; //The controlled player character
	
	//Upgrade paths
	LinkedList<Upgrade> naturalUpgrades;
	LinkedList<Upgrade> darkUpgrades;
	LinkedList<Upgrade> militaryUpgrades;
	LinkedList<Upgrade> alchemyUpgrades;
	
	

	@Override
	public void create () {
		batch = new SpriteBatch();
		//setting variables for easy access to screen dimensions
		w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.zoom -= 0.7; //Zooming in the camera to adjust for small tile sizes
        camera.setToOrtho(false,w,h);
        camera.update();
        
        
        
        map = new Field(40);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map);
		
		Texture playerTex = new Texture("playerStartSprite.png");
		player = new Player(new Sprite(playerTex));
		fillUpgrades(); //Fills the upgrade linkedlists with the appropriate upgrades for each path
		
	}

	@Override
	public void render () {
		player.update(Gdx.graphics.getRawDeltaTime(), camera, map);
		if(player.getX() - camera.position.x > w * camera.zoom - 250  && camera.position.x + w * camera.zoom / 2 < map.ground.getWidth() * map.ground.getTileWidth()) { //Scrolling when at right side of the screen
			camera.translate(60 * Gdx.graphics.getRawDeltaTime() * player.moveMod(), 0);
			
		} else if(player.getX() - camera.position.x < -1 * w * camera.zoom + 250 && camera.position.x > w * camera.zoom / 2) { //Scrolling when at left
			camera.translate(-60 * Gdx.graphics.getRawDeltaTime() * player.moveMod(), 0);
			
		}
		if(player.getY() - camera.position.y > h * camera.zoom - 250  && camera.position.y + h * camera.zoom / 2 < map.ground.getHeight() * map.ground.getTileHeight()) { //Scrolling when at right side of the screen
			camera.translate(0, 60 * Gdx.graphics.getRawDeltaTime() * player.moveMod());
		} else if(player.getY() - camera.position.y < -1 * h * camera.zoom + 250 && camera.position.y > h * camera.zoom / 2) { //Scrolling when at left
			camera.translate(0, -60 * Gdx.graphics.getRawDeltaTime() * player.moveMod());
		}
		//Drawing all objects
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//Drawing tiles
		camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        batch.setProjectionMatrix(camera.combined);
        
        batch.begin();
        player.draw(batch);
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
	
	public void fillUpgrades() {
		//Fills the appropriate linked lists full of the upgrade paths for future reference
		naturalUpgrades = new LinkedList<Upgrade>();
		darkUpgrades = new LinkedList<Upgrade>();
		militaryUpgrades = new LinkedList<Upgrade>();
		alchemyUpgrades = new LinkedList<Upgrade>();
		naturalUpgrades.add(new Upgrade("Arboreal Essence", "Grants higher attack power and movement speed while in forests"));
		naturalUpgrades.add(new Upgrade("Nature's Freedom", "Removes all field based movement limitations"));
		darkUpgrades.add(new Upgrade("Soul Thief", "Grants increased stats upon slaying enemies"));
		militaryUpgrades.add(new Upgrade("Bigger Artillery", "Grants higher attack power and increased splash radius"));
		alchemyUpgrades.add(new Upgrade("Fortified Genetics", "Increases overall health"));
		alchemyUpgrades.add(new Upgrade("Poisoner", "Attack potions now reduce enemy attack power and deal damage over time"));
	}
	
}