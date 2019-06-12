package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Product extends Sprite{
	public String name;
	public String description;
	public boolean selected;
	public float requirement;
	public float relativeX;
	public float relativeY;
	
	public Product(String name, String d, float x, float y, float t) {
		super(new Sprite(new Texture("ProductTemplate.png")));
		setSize(430 * 0.3f, 100 * 0.3f);
		setAlpha(0.7f);
		setPosition(x, y);
		relativeX = x;
		relativeY = y;
		this.name = name;
		description = d;
		requirement = t;
	}
	
	public void setTime(float t) {
		requirement = t;
	}
	
	public void passTime(float delta) {
		requirement -= delta;
	}
	
	public boolean isAvailable() {
		return requirement <= 0;
	}
	
	public void select() {
		selected = true;
		setAlpha(0.9f);
	}
	
	public void deselect() {
		selected = false;
		setAlpha(0.7f);
	}
}
