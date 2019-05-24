package com.mygdx.game;

import java.util.HashSet;
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
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Player extends Sprite implements InputProcessor {

    /** the movement velocity */
    private Vector2 velocity = new Vector2();

    private float speed = 80;

    
    private LinkedList<String> upgrades = new LinkedList<String>();
    
    private HashSet<String> tilesOn = new HashSet<String>(); //The tiles that the player is currently standing on
    
    public boolean baseDown = false;

    public Player(Sprite sprite, OrthographicCamera camera) {
        super(sprite);
        setSize(16, 16);
        
    }
    public void draw(SpriteBatch spriteBatch, OrthographicCamera camera) {
        //update(Gdx.graphics.getDeltaTime());

        super.draw(spriteBatch);
        
    }

    public void update(float delta, OrthographicCamera camera) {
        float moveSpeedMod = 1;
    	tilesOn.clear();
    	//System.out.println(getX() + "/" + getY());
    	//System.out.println(camera.unproject(new Vector3(getX(), getY(), 0)).x + " " + camera.unproject(new Vector3(getX(), getY(), 0)).y);
    	
    	
        // save old position
        float oldX = getX(), oldY = getY(), tileWidth = Field.ground.getTileWidth(), tileHeight = Field.ground.getTileHeight();
        boolean collisionX = false, collisionY = false;
       
        tilesOn.add((String) Field.ground.getCell((int) ((getX()) / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().get("TerrainType"));
        tilesOn.add((String) Field.ground.getCell((int) ((getX()) / tileWidth), (int) ((getY() + getHeight() / 2) / tileHeight)).getTile().getProperties().get("TerrainType"));
        tilesOn.add((String) Field.ground.getCell((int) ((getX()) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().get("TerrainType"));
        tilesOn.add((String) Field.ground.getCell((int) (((getX()) + getWidth()) / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().get("TerrainType"));
        tilesOn.add((String) Field.ground.getCell((int) (((getX()) + getWidth()) / tileWidth), (int) ((getY() + getHeight() / 2) / tileHeight)).getTile().getProperties().get("TerrainType"));
        tilesOn.add((String) Field.ground.getCell((int) (((getX()) + getWidth()) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().get("TerrainType"));
        tilesOn.add((String) Field.ground.getCell((int) (((getX()) + getWidth() / 2) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().get("TerrainType"));
        tilesOn.add((String) Field.ground.getCell((int) (((getX()) + getWidth() / 2) / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().get("TerrainType"));
        
        
        // move on x
        
        moveSpeedMod = moveMod();
        
        if(Gdx.input.isKeyPressed(Keys.D)){
            velocity.x = speed;
        } 
        if(Gdx.input.isKeyPressed(Keys.A)){
            velocity.x = -speed;
        } 
        
        if(getX() + getWidth() <= Field.ground.getWidth() * Field.ground.getTileWidth() && getX() >= 0) {
        	translateX(velocity.x * delta * moveSpeedMod);
        }
        if(getX() < 0) {
        	setX(oldX);
        }
        if(getX() + getWidth() > Field.ground.getWidth() * Field.ground.getTileWidth()) {
        	setX(oldX);
        }
        
        if(Gdx.input.isKeyPressed(Keys.W)){
            velocity.y = speed;
        } 
        if(Gdx.input.isKeyPressed(Keys.S)){
            velocity.y = -speed;
        } 
        
        if(getY() + getHeight() <= Field.ground.getHeight() * Field.ground.getTileHeight() && getY() >= 0) {
        	translateY(velocity.y * delta * moveSpeedMod);
        }
        if(getY() < 0) {
        	setY(0);
        }
        if(getY() + getHeight() > Field.ground.getHeight() * Field.ground.getTileHeight()) {
        	setY(oldY);
        }
        
        
        
        collisionX = collisionY = false;
        for(MapLayer l : Field.layers) {
        	TiledMapTileLayer layer = (TiledMapTileLayer) l;
        	if(velocity.x < 0) { // going left
                // top left
                if(!collisionX) {
                	collisionX = (Boolean) layer.getCell((int) ((getX()) / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().get("blocked");
                }

                // middle left
                if(!collisionX)
                    collisionX = (Boolean) layer.getCell((int) ((getX()) / tileWidth), (int) ((getY() + getHeight() / 2) / tileHeight)).getTile().getProperties().get("blocked");

                // bottom left
                if(!collisionX)
                    collisionX = (Boolean) layer.getCell((int) ((getX()) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().get("blocked");
            } else if(velocity.x > 0) { // going right
                // top right
                if(!collisionX) {
                	collisionX = (Boolean) layer.getCell((int) (((getX()) + getWidth()) / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().get("blocked");
                }

                // middle right
                if(!collisionX)
                    collisionX = (Boolean) layer.getCell((int) (((getX()) + getWidth()) / tileWidth), (int) ((getY() + getHeight() / 2) / tileHeight)).getTile().getProperties().get("blocked");

                // bottom right
                if(!collisionX)
                    collisionX = (Boolean) layer.getCell((int) (((getX()) + getWidth()) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().get("blocked");
            }
            

            // react to x collision
            if(collisionX) {
                setX(oldX);
            } 

            if(velocity.y < 0) { // going down
                // bottom left
                if(!collisionY) {
                	collisionY = (Boolean) layer.getCell((int) ((getX()) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().get("blocked");
                }

                // bottom middle
                if(!collisionY)
                    collisionY = (Boolean) layer.getCell((int) (((getX()) + getWidth() / 2) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().get("blocked");

                // bottom right
                if(!collisionY)
                    collisionY = (Boolean) layer.getCell((int) (((getX()) + getWidth()) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().get("blocked");

            } else if(velocity.y > 0) { // going up
                // top left
                if(!collisionY) {
                	collisionY = (Boolean) layer.getCell((int) ((getX()) / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().get("blocked");
                }

                // top middle
                if(!collisionY)
                    collisionY = (Boolean) layer.getCell((int) (((getX()) + getWidth() / 2) / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().get("blocked");

                // top right
                if(!collisionY)
                    collisionY = (Boolean) layer.getCell((int) (((getX()) + getWidth()) / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().get("blocked");
            }
            

            // react to y collision
            if(collisionY) {
                setY(oldY);
            }
        }
        
        
        velocity.x = 0;
        velocity.y = 0;
        
    }
    
    public float moveMod() {
    	float moveSpeedMod = 1;
    	if(tilesOn.contains("forest")) {
        	if(upgrades.contains("Arboreal Essence")) {
        		moveSpeedMod *= 1.5;
        	} else {
        		moveSpeedMod /= 2;
        	}
        	
        }
    	return moveSpeedMod;
    }
    
    public int[] tilePos(OrthographicCamera camera) {
    	//Returns the tile that the bottom left corner of the player sprite is on 
    	int[] tilePos = new int[2];
    	tilePos[0] = (int) ((getX()) / ((TiledMapTileLayer) Field.ground).getTileWidth());
    	tilePos[1] = (int) ((getY()) / ((TiledMapTileLayer) Field.ground).getTileHeight());
    	return tilePos;
    }
    
    public void setBase(Field map, OrthographicCamera camera) {
    	if(Gdx.input.isKeyPressed(Keys.SPACE)) {
    		if(map.setBase(tilePos(camera)[0], tilePos(camera)[1])) {
        		baseDown = true;
        	}
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

    
    public Upgrade[] displayUpgrade(LinkedList<Upgrade> path) {
    	Upgrade[] choices = new Upgrade[2];
    	if(upgrades.isEmpty()) {
    		Upgrade option1 = new Upgrade("Science", "Gives a splash damage projectile attacks");
    		Upgrade option2 = new Upgrade("Magic", "Gives a close quarters combat attack");
    		choices[0] = option1;
    		choices[1] = option2;
    	} else if(upgrades.getLast().equals("Science")) {
    		Upgrade option1 = new Upgrade("Military", "Allows the purchase of placeable turrets");
    		Upgrade option2 = new Upgrade("Alchemy", "Allows the purchase of health potions");
    		choices[0] = option1;
    		choices[1] = option2;
    	} else if(upgrades.getLast().equals("Sorcery")) {
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
    	upgrades.add(u.name);
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