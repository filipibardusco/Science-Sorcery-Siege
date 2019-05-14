//This class stores and randomly generates the game map

package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

public class Field{
	public TiledMap map;
	public int size;
	public TiledMapTileLayer ground;
	public TiledMapTileLayer objects;
	//Don't ask me why these IDs are all one higher than they are in the Field.tsx file, I really don't know but they are
	private static final int FOREST = 2;
	private static final int PLAIN = 3;
	private static final int BASETL = 12;
	private static final int BASETM = 7;
	private static final int BASETR = 8;
	private static final int BASEBL = 9;
	private static final int BASEBM = 10;
	private static final int BASEBR = 11;
	private static final int WATER1 = 13;
	private static final int WATER2 = 14;
	private static final int WATER3 = 15;
	
	TiledMap importTiles = new TmxMapLoader().load("MapTemplate.tmx");
	TiledMapTileSet tiles = importTiles.getTileSets().getTileSet(0);
	
	public Field(int size) {
		this.size = size;
		map = new TiledMap();
		MapLayers layers = map.getLayers();
		
		
		
		
		
		ground = new TiledMapTileLayer(size, size, 32, 32);
		objects = new TiledMapTileLayer(size, size, 32, 32);
		
		for(int i = 0; i < size; i++) {
			//Setting all the border tiles to be plain tiles to ensure a consistent, navigable access point to each base
			Cell cellTop = new Cell();
			Cell cellBottom = new Cell();
			Cell cellLeft = new Cell();
			Cell cellRight = new Cell();
			cellTop.setTile(tiles.getTile(PLAIN));
			cellBottom.setTile(tiles.getTile(PLAIN));
			cellLeft.setTile(tiles.getTile(PLAIN));
			cellRight.setTile(tiles.getTile(PLAIN));
			ground.setCell(i, 0, cellTop);
			ground.setCell(i, size - 1, cellBottom);
			ground.setCell(0, i, cellLeft);
			ground.setCell(size - 1, i, cellRight);
		}
		for (int x = 1; x < size - 1; x++) {
			for (int y = x; y < size - 1; y++) {
				int tileDeterminer = randint(1, 100); //Using a random integer to determine a random tile
				int lakeMod = 0;
				if(ground.getCell(x - 1, y).getTile().getId() == WATER1 || ground.getCell(x, y - 1).getTile().getId() == WATER1) { //Making it more likely to create water tiles next to other water tiles to simulate more realistic lake structures
					lakeMod += 15;
				}
				int forestMod = 0;
				if(ground.getCell(x - 1, y).getTile().getId() == FOREST || ground.getCell(x, y - 1).getTile().getId() == FOREST) { //Making it more likely to create forest tiles next to other forest tiles to simulate more realistic forest structures
					forestMod += 8;
				}
				Cell cell = new Cell();
				Cell cellReflect = new Cell();
				if(tileDeterminer <= 6 + lakeMod) {
					cell.setTile(tiles.getTile(WATER1));
					cellReflect.setTile(tiles.getTile(WATER1));
				} else if(tileDeterminer <= 30 + forestMod) {
					cell.setTile(tiles.getTile(FOREST));
					cellReflect.setTile(tiles.getTile(FOREST));
				} else {
					cell.setTile(tiles.getTile(PLAIN));
					cellReflect.setTile(tiles.getTile(PLAIN));
				}
				
				ground.setCell(x, y, cell);
				ground.setCell(y, x, cellReflect);
			}
		}
		layers.add(ground);
		layers.add(objects);
	
		
	}
	
	public boolean setBase(int x, int y) {
		//Sets the player's base at Tile position (x, y) and returns whether it was successful or not
		if(x > 0 && x < size && y > 1) {
			Cell TR = new Cell();
			Cell TM = new Cell();
			Cell TL = new Cell();
			Cell BR = new Cell();
			Cell BM = new Cell();
			Cell BL = new Cell();
			TR.setTile(tiles.getTile(BASETR));
			TM.setTile(tiles.getTile(BASETM));
			TL.setTile(tiles.getTile(BASETL));
			BR.setTile(tiles.getTile(BASEBR));
			BM.setTile(tiles.getTile(BASEBM));
			BL.setTile(tiles.getTile(BASEBL));
			objects.setCell(x - 1, y - 1, TL);
			objects.setCell(x, y - 1, TM);
			objects.setCell(x + 1, y - 1, TR);
			objects.setCell(x - 1, y - 2, BL);
			objects.setCell(x, y - 2, BM);
			objects.setCell(x + 1, y - 2, BR);
			ground.getCell(x - 1, y - 1).setTile(tiles.getTile(PLAIN));
			ground.getCell(x, y - 1).setTile(tiles.getTile(PLAIN));
			ground.getCell(x + 1, y - 1).setTile(tiles.getTile(PLAIN));
			ground.getCell(x - 1, y - 2).setTile(tiles.getTile(PLAIN));
			ground.getCell(x, y - 2).setTile(tiles.getTile(PLAIN));
			ground.getCell(x + 1, y - 2).setTile(tiles.getTile(PLAIN));
			return true;
		} else {
			return false;
		}
	}
	
	public int columnAt(int x) {
		//Returns the tile column position at pixel value x
		int column = (int) (x / ground.getTileWidth());
		return column;
	}
	
	public int rowAt(int y) {
		int row = (int) (y / ground.getTileWidth());
		return row;
	}
	
	
	
	public static int randint(int low, int high){
	    return (int)(Math.random()*(high-low+1) + low);
	}
}
