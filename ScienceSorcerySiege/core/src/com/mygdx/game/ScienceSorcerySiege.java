package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScienceSorcerySiege extends ApplicationAdapter {
	SpriteBatch batch;
	int screenX;
	int screenY;
	Field map;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		//setting variables for easy access to screen dimensions
		screenX = Gdx.graphics.getWidth();
		screenY = Gdx.graphics.getHeight();
		
		map = new Field(30);
	}

	@Override
	public void render () {
		
		
		//Drawing all objects
		batch.begin();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//Drawing tiles
		for(int i = 0; i < map.size; i++) {
			for(int j = 0; j < map.size; j++) {
				Field.map[i][j].sprite().draw(batch); 
			}
		}
		
		
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
