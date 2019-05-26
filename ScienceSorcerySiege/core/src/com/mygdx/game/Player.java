//This class is inherits from the sprite class and is specifically designed for player controlled characters
//It takes input from the user and handles all interactions the player has with the rest of the game and stores the player's upgrades and items that they've collected
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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Player extends Sprite{

    /** the movement velocity */
    private Vector2 velocity = new Vector2(); //The current velocity of the player

    private float defSpeed = 60; //The default movement speed value for a player provided no effects are on them, but can change with upgrades

    
    private LinkedList<Upgrade> upgrades = new LinkedList<Upgrade>(); //Stores the player's current upgrades
    
    private HashSet<String> tilesOn = new HashSet<String>(); //The tiles that the player is currently standing on
    
    public boolean baseDown = false; //Flag storing whether or not this player has placed a base
    private int[] basePos;

    public Player(Sprite sprite) {
    	//Constructor is identical to the Sprite constructor, but sets it to the appropriate size for a player and some initiation
        super(sprite);
        setSize(16, 16);
        basePos = new int[2];
    }

    public void update(float delta, OrthographicCamera camera, Field map) {
    	//Updates the player's position and handles interactions with the map
        float moveSpeedMod = 1; //Modifier for movement speed based on tile positioning
    	tilesOn.clear();

        // save old position
        float oldX = getX(), oldY = getY(), tileWidth = map.ground.getTileWidth(), tileHeight = map.ground.getTileHeight();
        boolean collisionX = false, collisionY = false; //flags for determining collisions
        
        //Determining all tile types the player is currently standing on
        tilesOn.add((String) map.ground.getCell((int) ((getX()) / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().get("TerrainType"));
        tilesOn.add((String) map.ground.getCell((int) ((getX()) / tileWidth), (int) ((getY() + getHeight() / 2) / tileHeight)).getTile().getProperties().get("TerrainType"));
        tilesOn.add((String) map.ground.getCell((int) ((getX()) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().get("TerrainType"));
        tilesOn.add((String) map.ground.getCell((int) (((getX()) + getWidth()) / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().get("TerrainType"));
        tilesOn.add((String) map.ground.getCell((int) (((getX()) + getWidth()) / tileWidth), (int) ((getY() + getHeight() / 2) / tileHeight)).getTile().getProperties().get("TerrainType"));
        tilesOn.add((String) map.ground.getCell((int) (((getX()) + getWidth()) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().get("TerrainType"));
        tilesOn.add((String) map.ground.getCell((int) (((getX()) + getWidth() / 2) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().get("TerrainType"));
        tilesOn.add((String) map.ground.getCell((int) (((getX()) + getWidth() / 2) / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().get("TerrainType"));
        
        
        // move on x
        
        moveSpeedMod = moveMod(); //Determining what move speed modifiers should be applied based on terrain
        
        kbInput(map, camera); //Gets input to determine which direction to head and other things
        
        if(getX() + getWidth() <= map.ground.getWidth() * map.ground.getTileWidth() && getX() >= 0) {
        	//Moves the player horizontally provided they are not at the edges of the map
        	translateX(velocity.x * delta * moveSpeedMod);
        }
        if(getX() < 0 || getX() + getWidth() > map.ground.getWidth() * map.ground.getTileWidth()) { 
        	//Applies collisions at edges of the screen
        	collisionX = true;
        }
        if(getY() + getHeight() <= map.ground.getHeight() * map.ground.getTileHeight() && getY() >= 0) {
        	//Moves the player vertically provided they are not at the edges of the map
        	translateY(velocity.y * delta * moveSpeedMod);
        }
        if(getY() < 0 || getY() + getHeight() > map.ground.getHeight() * map.ground.getTileHeight()) {
        	//Applies collisions at edges of the screen
        	collisionY = true;
        }
        
        for(MapLayer l : map.layers) {
        	//Checking and reacting to collisions across all map layers
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
        
        //Stopping the player for when there is no input
        velocity.x = 0;
        velocity.y = 0;
        
    }
    
    public float moveMod() {
    	//Adjusts the movement speed of the player based on the tiles it is on
    	float movedefSpeedMod = 1;
    	if(tilesOn.contains("forest")) { //Reduces move speed if in a forest, unless they have the appropriate upgrade
        	if(upgrades.contains("Arboreal Essence")) {
        		movedefSpeedMod *= 1.5;
        	} else {
        		movedefSpeedMod /= 2;
        	}
        	
        }
    	return movedefSpeedMod;
    }
    
    public int[] tilePos(Field map) {
    	//Returns the tile that the bottom left corner of the player sprite is on 
    	int[] tilePos = new int[2];
    	tilePos[0] = (int) ((getX()) / map.ground.getTileWidth());
    	tilePos[1] = (int) ((getY()) / map.ground.getTileHeight());
    	return tilePos;
    }
    
    public void setBase(Field map, OrthographicCamera camera) {
    	//Sets the player's base directly above them
    	if(map.setBase(tilePos(map)[0], tilePos(map)[1])) {
        	baseDown = true; //Confirms that the base was successfully placed
        	basePos[0] = tilePos(map)[0];
        	basePos[1] = tilePos(map)[1] + 1;
        }
    }
    
    public void setTexture() {
    	//Sets the texture of the player character to the appropriate one based on the most recent upgrade path
    	if(upgrades.size() <= 2) {
    		setTexture(new Texture(upgrades.getLast().name + "Avatar.png"));
    	}
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public float getdefSpeed() {
        return defSpeed;
    }

    public void setdefSpeed(float defSpeed) {
        this.defSpeed = defSpeed;
    }
    
    public void kbInput(Field map, OrthographicCamera camera) {
    	//Gets player related keyboard input
    	
    	//Movement speed calculations
    	if(Gdx.input.isKeyPressed(Keys.D)){
            velocity.x = defSpeed;
        } 
        if(Gdx.input.isKeyPressed(Keys.A)){
            velocity.x = -defSpeed;
        } 
        if(Gdx.input.isKeyPressed(Keys.W)){
            velocity.y = defSpeed;
        } 
        if(Gdx.input.isKeyPressed(Keys.S)){
            velocity.y = -defSpeed;
        } 
        if(Gdx.input.isKeyPressed(Keys.SPACE)) {
        	//Places down base if not done, otherwise attempts to enter the upgrade/shop menu, provided the player is in their base
        	if(baseDown) {
        		if(tilePos(map)[0] == basePos[0] && tilePos(map)[1] == basePos[1]) {
        			System.out.println("Yay safe!");
        		}
        	} else {
        		setBase(map, camera);
        	}
        }
    }
    
    public void upgrade(Upgrade u) {
    	upgrades.add(u);
    }
}