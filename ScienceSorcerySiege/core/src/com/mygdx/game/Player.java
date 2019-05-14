package com.mygdx.game;

import java.util.LinkedList;
import java.security.Key;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.Input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite implements InputProcessor {

    /** the movement velocity */
    private Vector2 velocity = new Vector2();

    private float speed = 300;

    private TiledMapTileLayer collisionLayer;
    
    private LinkedList<Upgrade> upgrades;
    
    public boolean baseDown = false;

    public Player(Sprite sprite, TiledMapTileLayer c) {
        super(sprite);
        setSize(64, 64);
        collisionLayer = c;
    }
    public void draw(SpriteBatch spriteBatch) {
        //update(Gdx.graphics.getDeltaTime());
        super.draw(spriteBatch);
    }

    public void update(float delta, OrthographicCamera camera) {
        
    	
        // save old position
        float oldX = getX(), oldY = getY(), tileWidth = collisionLayer.getTileWidth() / camera.zoom, tileHeight = collisionLayer.getTileHeight() / camera.zoom;
        boolean collisionX = false, collisionY = false;
        // move on x
        
        if(Gdx.input.isKeyPressed(Keys.D)){
            velocity.x = speed;
        } 
        if(Gdx.input.isKeyPressed(Keys.A)){
            velocity.x = -speed;
        } 
        translateX(velocity.x * delta);
        if(Gdx.input.isKeyPressed(Keys.W)){
            velocity.y = speed;
        } 
        if(Gdx.input.isKeyPressed(Keys.S)){
            velocity.y = -speed;
        } 
        translateY(velocity.y * delta);
        if(velocity.x < 0) { // going left
            // top left
            collisionX = (Boolean) collisionLayer.getCell((int) (getX() / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().get("blocked");

            // middle left
            if(!collisionX)
                collisionX = (Boolean) collisionLayer.getCell((int) (getX() / tileWidth), (int) ((getY() + getHeight() / 2) / tileHeight)).getTile().getProperties().get("blocked");

            // bottom left
            if(!collisionX)
                collisionX = (Boolean) collisionLayer.getCell((int) (getX() / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().get("blocked");
        } else if(velocity.x > 0) { // going right
            // top right
            collisionX = (Boolean) collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().get("blocked");

            // middle right
            if(!collisionX)
                collisionX = (Boolean) collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) ((getY() + getHeight() / 2) / tileHeight)).getTile().getProperties().get("blocked");

            // bottom right
            if(!collisionX)
                collisionX = (Boolean) collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().get("blocked");
        }

        // react to x collision
        if(collisionX) {
            setX(oldX);
            //velocity.x = 0;
        } 

        if(velocity.y < 0) { // going down
            // bottom left
            collisionY = (Boolean) collisionLayer.getCell((int) (getX() / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().get("blocked");

            // bottom middle
            if(!collisionY)
                collisionY = (Boolean) collisionLayer.getCell((int) ((getX() + getWidth() / 2) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().get("blocked");

            // bottom right
            if(!collisionY)
                collisionY = (Boolean) collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().get("blocked");

        } else if(velocity.y > 0) { // going up
            // top left
            collisionY = (Boolean) collisionLayer.getCell((int) (getX() / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().get("blocked");

            // top middle
            if(!collisionY)
                collisionY = (Boolean) collisionLayer.getCell((int) ((getX() + getWidth() / 2) / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().get("blocked");

            // top right
            if(!collisionY)
                collisionY = (Boolean) collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().get("blocked");
        }

        // react to y collision
        if(collisionY) {
            setY(oldY);
            //velocity.y = 0;
        }
        velocity.x = 0;
        velocity.y = 0;
        
    }
    
    public int[] tilePos() {
    	//Returns the tile that the bottom left corner of the player sprite is on 
    	int[] tilePos = new int[2];
    	tilePos[0] = (int) (getX() / collisionLayer.getTileWidth() * 0.3);
    	tilePos[1] = (int) (getY() / collisionLayer.getTileHeight() * 0.3);
    	return tilePos;
    }
    
    public void setBase(Field map) {
    	if(map.setBase(tilePos()[0], tilePos()[1])) {
    		baseDown = true;
    	}
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }


    public TiledMapTileLayer getCollisionLayer() {
        return collisionLayer;
    }

    public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
        this.collisionLayer = collisionLayer;
    }
    
    public Upgrade[] displayUpgrade(LinkedList<Upgrade> path) {
    	Upgrade[] choices = new Upgrade[2];
    	if(upgrades.isEmpty()) {
    		Upgrade option1 = new Upgrade("Science", "Gives a splash damage projectile attacks");
    		Upgrade option2 = new Upgrade("Magic", "Gives a close quarters combat attack");
    		choices[0] = option1;
    		choices[1] = option2;
    	} else if(upgrades.getLast().name.equals("Science")) {
    		Upgrade option1 = new Upgrade("Military", "Allows the purchase of placeable turrets");
    		Upgrade option2 = new Upgrade("Alchemy", "Allows the purchase of health potions");
    		choices[0] = option1;
    		choices[1] = option2;
    	} else if(upgrades.getLast().name.equals("Sorcery")) {
    		Upgrade option1 = new Upgrade("Natural Magic", "Allows the purchase of natural draining totems");
    		Upgrade option2 = new Upgrade("Dark Arts", "Allows the purchase of stat upgrades");
    		choices[0] = option1;
    		choices[1] = option2;
    	} else {
    		Upgrade[] onlyChoice = new Upgrade[1];
    		onlyChoice[0] = path.get(path.indexOf(upgrades.getLast()) + 1);
    		return onlyChoice;
    	}
    	return choices;
    }
    
    
    
    public void upgrade(Upgrade u) {
    	upgrades.add(u);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode) {
            case Keys.A:
                velocity.x = -speed;
                break;
            case Keys.D:
                velocity.x = speed;
            case Keys.S:
                velocity.y = -speed;
                break;
            case Keys.W:
                velocity.y = speed;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch(keycode) {
            case Keys.A:
            case Keys.D:
                velocity.x = 0;
            case Keys.S:
            case Keys.W:
                velocity.y = 0;
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