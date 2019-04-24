//This class will handle the different types of tiles as well as collisions and other interactions with players

package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Tile {
	private int type;
	private Texture tileImg;
	private Sprite area;
	
	public static final int PLAIN = 0;
	public static final int FOREST = 1;
	public static final int WATER = 2;
	
	public Tile(int t) {
		type = t;
		if(type == PLAIN) {
			
		} else if(type == FOREST) {
			
		} else if(type == WATER) {
			
		}
	}
	
	public int type() {
		return type;
	}
	
}
