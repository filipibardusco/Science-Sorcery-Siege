//This class stores and randomly generates the game map

package com.mygdx.game;

public class Field {
	public static char[][] map;
	public Field(int size) {
		map = new char[size][size];
		generate(size);
	}
	
	public static void generate(int size) {
		for(int i = 0; i < size; i++) {
			//Setting all the border tiles to be plain tiles to ensure a consistent, navigable access point to each base
			map[0][i] = 'p';
			map[i][0] = 'p';
			map[size - 1][i] = 'p';
			map[i][size - 1] = 'p';
		}
		for(int y = 1; y < size - 1; y++) {
			for(int x = y; x < size - 1; x++) {
				int tileDeterminer = randint(1, 100); //Using a random integer to determine a random tile
				char tile;
				int lakeMod = 0;
				if(map[x - 1][y] == 'w') { //Making it more likely to create water tiles next to other water tiles to simulate more realistic lake structures
					lakeMod += 3;
				}
				if(map[x][y - 1] == 'w') {
					lakeMod += 3;
				}
				int forestMod = 0;
				if(map[x - 1][y] == 'w') { //Making it more likely to create forest tiles next to other forest tiles to simulate more realistic forest structures
					forestMod += 5;
				}
				if(map[x][y - 1] == 'w') {
					forestMod += 5;
				}
				if(tileDeterminer <= 5 + lakeMod) {
					tile = 'w';
				} else if(tileDeterminer <= 40 + forestMod) {
					tile = 'f';
				} else {
					tile = 'p';
				}
				map[x][y] = tile; //Setting tiles to be symmetric across a diagonal
				map[y][x] = tile;
			}
		}
	}
	
	public static int randint(int low, int high){
	    return (int)(Math.random()*(high-low+1) + low);
	}
}
