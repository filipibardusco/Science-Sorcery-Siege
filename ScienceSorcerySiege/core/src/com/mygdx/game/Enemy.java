//This class handles enemy creation, movement, and management
package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Enemy {
	private int health; 
	private int attackPower; //Determines how much damage an enemy deals to a player
	private int level;
	private Texture enemyImg = new Texture("badlogic.jpg");
	private Sprite enemySprite;
	
	private static final int DEFMOVESPEED = 3; //The default movement speed of an enemy assuming no terrain
	
	public Enemy(int level, int x, int y) {
		//Creates the enemy sprite and sets it's position
		this.level = level;
		health = 5 + level * level;
		attackPower = 1 + level * 2;
		enemySprite = new Sprite(enemyImg);
		enemySprite.setPosition(x, y);
	}
	
	public void Move() {
		//Moves the enemy randomly if no player is nearby
		if(true) { 
			enemySprite.translate(randint(0, 1) * DEFMOVESPEED, randint(0, 1) * DEFMOVESPEED);
		}
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
	
	public Sprite sprite() {
		return enemySprite;
	}
	
	public static int randint(int low, int high){
	    return (int)(Math.random()*(high-low+1) + low);
	}
}
