//This class stores and randomly generates the game map

package com.mygdx.game;

public class Field {
	public static Tile[][] map;
	public int size;
	public Field(int size) {
		this.size = size;
		map = new Tile[size][size];
		generate(size);
	}
	
	public static void generate(int size) {
		for(int i = 0; i < size; i++) {
			//Setting all the border tiles to be plain tiles to ensure a consistent, navigable access point to each base
			map[0][i] = new Tile(Tile.PLAIN, i, 0);
			map[i][0] = new Tile(Tile.PLAIN, 0, i);
			map[size - 1][i] = new Tile(Tile.PLAIN, i, size - 1);
			map[i][size - 1] = new Tile(Tile.PLAIN, size - 1, i);
		}
		for(int y = 1; y < size - 1; y++) {
			for(int x = y; x < size - 1; x++) {
				int tileDeterminer = randint(1, 100); //Using a random integer to determine a random tile
				int tileType;
				int lakeMod = 0;
				if(map[x - 1][y].type() == Tile.WATER || map[x][y - 1].type() == Tile.WATER) { //Making it more likely to create water tiles next to other water tiles to simulate more realistic lake structures
					lakeMod += 3;
				}
				int forestMod = 0;
				if(map[x - 1][y].type() == Tile.FOREST || map[x][y - 1].type() == Tile.FOREST) { //Making it more likely to create forest tiles next to other forest tiles to simulate more realistic forest structures
					forestMod += 5;
				}
				if(tileDeterminer <= 5 + lakeMod) {
					tileType = Tile.WATER;
				} else if(tileDeterminer <= 40 + forestMod) {
					tileType = Tile.FOREST;
				} else {
					tileType = Tile.PLAIN;
				}
				map[x][y] = new Tile(tileType, x, y); //Setting tiles to be symmetric across a diagonal
				map[y][x] = new Tile(tileType, y, x);
			}
		}
	}
	
	public static int randint(int low, int high){
	    return (int)(Math.random()*(high-low+1) + low);
	}
}
