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

public class ScienceSorcerySiege extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	float w;
	float h;
	Field map;
	OrthographicCamera camera;
	TiledMapRenderer tiledMapRenderer;
	Player player;
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
        camera.zoom -= 0.7;
        camera.setToOrtho(false,w,h);
        camera.update();
        
        
        
        map = new Field(40);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map.map);
		
		Texture playerTex = new Texture("badlogic.jpg");
		player = new Player(new Sprite(playerTex), camera);
		fillUpgrades();
		
	}

	@Override
	public void render () {
		if(!player.baseDown) {
			player.setBase(map, camera);
		}
		//System.out.println(camera.position.x + " " + camera.position.y);
		player.update(Gdx.graphics.getRawDeltaTime(), camera);
		if(player.getX() - camera.position.x > w * camera.zoom - 170  && camera.position.x + w * camera.zoom / 2 < Field.ground.getWidth() * Field.ground.getTileWidth()) { //Scrolling when at right side of the screen
			camera.translate(80 * Gdx.graphics.getRawDeltaTime() * player.moveMod(), 0);
			
		} else if(player.getX() - camera.position.x < -1 * w * camera.zoom + 170 && camera.position.x > w * camera.zoom / 2) { //Scrolling when at left
			camera.translate(-80 * Gdx.graphics.getRawDeltaTime() * player.moveMod(), 0);
			
		}
		if(player.getY() - camera.position.y > h * camera.zoom - 130  && camera.position.y + h * camera.zoom / 2 < Field.ground.getHeight() * Field.ground.getTileHeight()) { //Scrolling when at right side of the screen
			camera.translate(0, 80 * Gdx.graphics.getRawDeltaTime() * player.moveMod());
		} else if(player.getY() - camera.position.y < -1 * h * camera.zoom + 130 && camera.position.y > h * camera.zoom / 2) { //Scrolling when at left
			camera.translate(0, -80 * Gdx.graphics.getRawDeltaTime() * player.moveMod());
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
        player.draw(batch, camera);
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
	
	

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode) {
            case Keys.A:
                break;
            case Keys.D:
            case Keys.S:
                break;
            case Keys.W:
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch(keycode) {
            case Keys.A:
            case Keys.D:
            case Keys.S:
            case Keys.W:
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}