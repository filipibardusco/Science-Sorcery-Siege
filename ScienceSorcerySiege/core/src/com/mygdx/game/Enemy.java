//This class handles enemy creation, movement, and management
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Enemy extends Sprite{
	private int health; 
	private int attackPower; //Determines how much damage an enemy deals to a player
	private int level;
	
	private static final int DEFMOVESPEED = 3; //The default movement speed of an enemy assuming no terrain
	public static final int RIGHT = 1;
	public static final int LEFT = -1;
	
    public void draw(SpriteBatch spriteBatch) {
        //update(Gdx.graphics.getDeltaTime());
        super.draw(spriteBatch);
    }
	
	public Enemy(int level, int x, int y) {
		//Creates the enemy sprite and sets it's position
		super(new Sprite(new Texture("badlogic.jpg")));
		this.level = level;
		health = 5 + level * level;
		attackPower = 1 + level * 2;
		setPosition(x, y);
	}
	
	
	
	public void moveH(int dir) {
		//Moves the enemy horizontally in the direction indicated
		
	}
	
	public void moveV() {
		//Moves the enemy one tile vertically in the direction indicated
	}
	
	public int takeDamage(int dmg) {
		//Makes the enemy take damage from an attack and kills it if it's health reaches 0 and subsequently rewards the player with an appropriate amount of gold as compensation
		int gold = 0;
		health -= dmg;
		if(health <= 0) {
			gold += 20 * (level + 5);
		}
		return gold;
	}
	
	public static int randint(int low, int high){
	    return (int)(Math.random()*(high-low+1) + low);
	}
}
