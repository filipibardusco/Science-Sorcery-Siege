//Main class that handles all overview of the game
package com.mygdx.game;


import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class ScienceSorcerySiege extends ApplicationAdapter{
	SpriteBatch batch;
	float w; //The width of the screen in pixels
	float h; //The width of the screen in pixels
	Field map; //The game map that players traverse
	OrthographicCamera camera; //The camera that displays certain points of the screen
	TiledMapRenderer tiledMapRenderer; //Allows the tiled map to be rendered
	//Player player; //The controlled player character
	Player[] players;
	ArrayList<Enemy> enemies;
	float spawnTimer;
	private float totalTime;
	BitmapFont itemFont;
	BitmapFont descFont;
	BitmapFont itemFontS;
	BitmapFont descFontS;
	
	BitmapFont generalFont;
	
	//Upgrade paths 
	LinkedList<Product> naturalUpgrades;
	LinkedList<Product> darkUpgrades;
	LinkedList<Product> militaryUpgrades;
	LinkedList<Product> alchemyUpgrades;
	static LinkedList<LinkedList<Product>> upgradePaths;
	
	
	

	@Override
	public void create () {
		batch = new SpriteBatch();
		//setting variables for easy access to screen dimensions
		w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.zoom -= 0.7; //Zooming in the camera to adjust for small tile sizes
        camera.setToOrtho(false, w, h);
        camera.update();
        itemFont = new BitmapFont();
        descFont = new BitmapFont();
        itemFont.getData().setScale(0.4f);
        itemFont.setColor(0, 0, 0, 0.7f);
        descFont.getData().setScale(0.3f);
        descFont.setColor(0, 0, 0, 0.7f);
        
        itemFontS = new BitmapFont();
        descFontS = new BitmapFont();
        generalFont = new BitmapFont();
        itemFontS.getData().setScale(0.4f);
        itemFontS.setColor(0, 0, 0, 1);
        descFontS.getData().setScale(0.3f);
        descFontS.setColor(0, 0, 0, 1);
        generalFont.getData().setScale(0.65f);
        itemFontS.setColor(0, 0, 0, 1);
        
        Texture playerTex = new Texture("playerStartSprite.png");
		
		players = new Player[2];
		players[0] = new Player(new Sprite(playerTex), 5, 5);
		players[1] = new Player(new Sprite(playerTex), 100, 100);
		
		enemies = new ArrayList<Enemy>();
        
        
        map = new Field(40);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map);
        players[0].setGameState("Field");
		
        for(Player p : players) {
        	fillProducts(p.getShop()); //Fills the Product linkedlists with the appropriate Products for each path
        }
	}

	@Override
	public void render () {
		String signal = "";
		if(players[0].isAlive()) {
			signal = players[0].kbInput(map, camera); //Getting all player input and determining the game state based off of that
		}
		if(players[1].isAlive()) {
			players[1].networkInput(map, camera, signal);
		}
		for(Player player : players) {
			player.update(Gdx.graphics.getRawDeltaTime(), camera, map); //Delta time used for smooth movements
			if(player.baseDown) {
				for(Product p : player.getShop().displayProducts()) {
					if(player.getShop().upgrades.contains(p)) {
						p.passTime(Gdx.graphics.getRawDeltaTime());
					}
				}
			}
			player.countRespawn(Gdx.graphics.getRawDeltaTime(), map);
		}
		spawnEnemies();
		if(players[0].getGameState().equals("Field")) {
			if(players[0].getX() - camera.position.x > w * camera.zoom - 250  && camera.position.x + w * camera.zoom / 2 < map.ground.getWidth() * map.ground.getTileWidth()) { //Scrolling when at right side of the screen
				camera.translate(players[0].getdefSpeed() * Gdx.graphics.getRawDeltaTime() * players[0].moveMod(map), 0);
				
			} else if(players[0].getX() - camera.position.x < -1 * w * camera.zoom + 250 && camera.position.x > w * camera.zoom / 2) { //Scrolling when at left
				camera.translate(-players[0].getdefSpeed() * Gdx.graphics.getRawDeltaTime() * players[0].moveMod(map), 0);
				
			}
			if(players[0].getY() - camera.position.y > h * camera.zoom - 250  && camera.position.y + h * camera.zoom / 2 < map.ground.getHeight() * map.ground.getTileHeight()) { //Scrolling when at right side of the screen
				camera.translate(0, players[0].getdefSpeed() * Gdx.graphics.getRawDeltaTime() * players[0].moveMod(map));
			} else if(players[0].getY() - camera.position.y < -1 * h * camera.zoom + 250 && camera.position.y > h * camera.zoom / 2) { //Scrolling when at left
				camera.translate(0, -players[0].getdefSpeed() * Gdx.graphics.getRawDeltaTime() * players[0].moveMod(map));
			}
		} else {
			for(Player p : players) {
				if(p.getGameState().equals("Upgraded")) {
					p.setGameState("Shop");
					p.getShop().determineUpgrades(players[0]);
				}
			}
		} if(players[0].getGameState().equals("Shop")) {
			players[0].getShop().setPosition(camera.position.x - w / 2 * camera.zoom, camera.position.y - h / 2 * camera.zoom);
			for(Product p : players[0].getShop().displayProducts()) {
				p.setPosition(players[0].getShop().getX() + p.relativeX, players[0].getShop().getY() + p.relativeY);
			}

		}
		ArrayList<Enemy> outdatedEnemies = new ArrayList<Enemy>();
		for(Enemy e : enemies) {
			e.moveDir(totalTime);
			for(Player p : players) {
				if(e.isNear(p) && p.isVisible(map)) {
					e.moveTo(p, map);
				}
			}
			e.move(Gdx.graphics.getRawDeltaTime(), map);
			if(e.getLevel() < (int) totalTime / 35 - 2) {
				//Removing enemies that are far weaker, mostly as a balancing mechanic for Soul Thief to minimize farming of early low level enemies that were never defeated
				outdatedEnemies.add(e);
			}
		}
		enemies.removeAll(outdatedEnemies);
		for(Player p : players) {
			if(p.isAttacking()) {
				p.getAttack().animate();
				ArrayList<Enemy> deadEnemies = new ArrayList<Enemy>();
				for(Enemy e : enemies) {
					if(p.isAttacking()) {
						if(p.getAttack().collide(e)) {
		        			if(e.getHealth() <= 0) {
		        				deadEnemies.add(e);
		        				if(p.upgrades.contains("Soul Thief")) {
		        					p.setAttackPower(p.getAttackPower() + 1);
		        					p.setHealth(Math.min(p.getHealth() + 1, p.maxHealth));
		        					p.setdefSpeed(p.getdefSpeed() + 1);
		        				}
		        			}
		        		}
					}
	        	}
				enemies.removeAll(deadEnemies);
			}
			for(Player opponent : players) {
				if(!opponent.equals(p)) {
					ArrayList<Placeable> brokenItems = new ArrayList<Placeable>();
					for(Placeable item : p.placeables) {
						if(item.getType().equals("Dark Arts") && item.isVisible()) {
							brokenItems.add(item);
							item.takeDamage(item.getHP(), map);
						}
						if(opponent.getBoundingRectangle().overlaps(item.getBoundingRectangle()) && item.getType().equals("Dark Arts")) {
							opponent.takeDamage(item.attack, 1.5f);
							item.setVisibility(true);
						} else if(item.getType().equals("Natural Magic") || item.getType().equals("Alchemy")) {
							item.dealEffect(opponent, map, totalTime);
						}
					}
					p.placeables.removeAll(brokenItems);
					if(p.isAttacking()) {
						p.getAttack().collide(opponent, map);
					}
				}
			}
		}
		countTime();
		//Drawing all objects
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//Drawing tiles
		camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        batch.setProjectionMatrix(camera.combined);
        
        batch.begin();
        for(Player p : players) {
        	if(p.isVisible(map)) {
        		p.draw(batch);
        	}
            for(Enemy e : enemies) {
            	e.collide(p);
            	e.draw(batch);
            }
            if(p.isAttacking()) {
            	p.getAttack().draw(batch);
            }
            for(Placeable item : p.placeables) {
            	if(item.isVisible()) {
            		item.draw(batch);
            	}
            }
        }
        
        if(players[0].getGameState().equals("Shop")) {
        	players[0].getShop().draw(batch);
        	for(Product p : players[0].getShop().displayProducts()) {
        		p.draw(batch);
        		GlyphLayout itemGlyth = new GlyphLayout();
        		String reqName = players[0].getShop().upgrades.contains(p)? " seconds" : " gold";
        		if(p.selected) {
            		itemGlyth.setText(itemFontS, p.name);
            		itemFontS.draw(batch, itemGlyth, p.getX() + (p.getWidth() - itemGlyth.width) / 2, p.getY() + p.getHeight() - 3);
            		
            		GlyphLayout descGlyth = new GlyphLayout();
            		descGlyth.setText(descFontS, p.description, descFontS.getColor(), p.getWidth() - 10, (int) (p.getX() + p.getWidth() - 10), true);
            		descFontS.draw(batch, descGlyth, p.getX() + (p.getWidth() - descGlyth.width) / 2, p.getY() + p.getHeight() - 10);
            		
            		if(p.requirement > 0) {
            			GlyphLayout timeGlyth = new GlyphLayout();
                		timeGlyth.setText(descFontS, (int) p.requirement + reqName, descFontS.getColor(), p.getWidth() - 10, (int) (p.getX() + p.getWidth() - 10), true);
                		descFontS.draw(batch, timeGlyth, p.getX() + 5, p.getY() + 7);
            		}
            		
        		} else {
            		itemGlyth.setText(itemFont, p.name);
            		itemFont.draw(batch, itemGlyth, p.getX() + (p.getWidth() - itemGlyth.width) / 2, p.getY() + p.getHeight() - 3);
            		
            		GlyphLayout descGlyth = new GlyphLayout();
            		descGlyth.setText(descFont, p.description, descFont.getColor(), p.getWidth() - 10, (int) (p.getX() + p.getWidth() - 10), true);
            		descFont.draw(batch, descGlyth, p.getX() + (p.getWidth() - descGlyth.width) / 2, p.getY() + p.getHeight() - 10);
            		
            		if(p.requirement > 0) {
            			GlyphLayout timeGlyth = new GlyphLayout();
                		timeGlyth.setText(descFont, (int) p.requirement + reqName, descFont.getColor(), p.getWidth() - 10, (int) (p.getX() + p.getWidth() - 10), true);
                		descFont.draw(batch, timeGlyth, p.getX() + 5, p.getY() + 7);
            		}
        		}
        		
        	}
        }
        generalFont.draw(batch, "Gold: " + players[0].money, (w - 150) * camera.zoom + camera.position.x - w / 2 * camera.zoom, (h - 20) * camera.zoom + camera.position.y - w / 2 * camera.zoom);
        generalFont.draw(batch, "Health: " + players[0].getHealth(), (w - 950) * camera.zoom + camera.position.x - w / 2 * camera.zoom, (h - 20) * camera.zoom + camera.position.y - w / 2 * camera.zoom);
		batch.end();
	}

	private void spawnEnemies() {
		//
		int numEnemiesSpawn = (int) totalTime / 35 + 1;
		if(totalTime >= 10 && spawnTimer <= 0) {
			for(int i = 0; i < numEnemiesSpawn; i++) {
				Enemy potentialEnemy;
				float enemyX = randint(5, map.size - 5) * 32;
				float enemyY = randint(5, map.size - 5) * 32;
				boolean invalidPos = false;
				for(MapLayer layer : map.layers) {
					TiledMapTileLayer l = (TiledMapTileLayer) layer;
					if(!invalidPos) {
						invalidPos = (Boolean) l.getCell((int) ((enemyX) / l.getTileWidth()), (int) ((enemyY + 16 - 1) / l.getTileHeight())).getTile().getProperties().get("blocked");
					} 
					if(!invalidPos) {
						invalidPos = (Boolean) l.getCell((int) ((enemyX) / l.getTileWidth()), (int) ((enemyY) / l.getTileHeight())).getTile().getProperties().get("blocked");
					} 
					if(!invalidPos) {
						invalidPos = (Boolean) l.getCell((int) ((enemyX + 16 - 1) / l.getTileWidth()), (int) ((enemyY + 16 - 1) / l.getTileHeight())).getTile().getProperties().get("blocked");
					} 
					if(!invalidPos) {
						invalidPos = (Boolean) l.getCell((int) ((enemyX + 16 - 1) / l.getTileWidth()), (int) ((enemyY) / l.getTileHeight())).getTile().getProperties().get("blocked");
					} 
				}
				if(!invalidPos) {
					potentialEnemy = new Enemy((int) totalTime, enemyX, enemyY);
					for(Player p : players) {
						if(!potentialEnemy.isNear(p)) {
							enemies.add(potentialEnemy);
							spawnTimer = randint(5, 20);
						}
					}
				}
			}
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
	
	public void fillProducts(Shop shop) {
		//Fills the appropriate linked lists full of the Product paths for future reference
		naturalUpgrades = new LinkedList<Product>();
		darkUpgrades = new LinkedList<Product>();
		militaryUpgrades = new LinkedList<Product>();
		alchemyUpgrades = new LinkedList<Product>();
		
		upgradePaths = new LinkedList<LinkedList<Product>>();
		
		naturalUpgrades.add(new Product("Natural Magic", "Harness the power of the world around you and build health draining totems (They're invisible in forests)!", Shop.UPGRADESX * 0.3f, Shop.STARTINGHEIGHT * 0.3f, 5));
		naturalUpgrades.add(new Product("Arboreal Essence", "Grants higher attack power and movement speed while in forests", Shop.UPGRADESX * 0.3f, Shop.STARTINGHEIGHT * 0.3f, 5));
		naturalUpgrades.add(new Product("Nature's Wrath", "Removes all field based movement limitations", Shop.UPGRADESX * 0.3f, Shop.STARTINGHEIGHT * 0.3f, 5));
		
		darkUpgrades.add(new Product("Dark Arts", "Learn the forbidden techniques and use the currency of the dead to upgrade your stats!", Shop.UPGRADESX * 0.3f, (Shop.STARTINGHEIGHT - Shop.YSPACING) * 0.3f, 5));
		darkUpgrades.add(new Product("Soul Thief", "Grants increased stats upon slaying enemies", Shop.UPGRADESX * 0.3f, Shop.STARTINGHEIGHT * 0.3f, 5));

		militaryUpgrades.add(new Product("Military", "Research the lethal technologies surrounding turrets!", Shop.UPGRADESX * 0.3f, Shop.STARTINGHEIGHT * 0.3f, 5));
		militaryUpgrades.add(new Product("Fortify", "Surround yourself in armour and double your max health!", Shop.UPGRADESX * 0.3f, Shop.STARTINGHEIGHT * 0.3f, 5));		
		militaryUpgrades.add(new Product("Bigger Artillery", "Grants higher attack power and increased splash radius", Shop.UPGRADESX * 0.3f, Shop.STARTINGHEIGHT * 0.3f, 5));
		
		alchemyUpgrades.add(new Product("Alchemy", "Research mystical combinations of substances allwoing you to buy health potions!", Shop.UPGRADESX * 0.3f, (Shop.STARTINGHEIGHT - Shop.YSPACING) * 0.3f, 5));
		alchemyUpgrades.add(new Product("Lingering Acid Pools", "Makes attacking acid pools last longer when expanded", Shop.UPGRADESX * 0.3f, Shop.STARTINGHEIGHT * 0.3f, 5));
		alchemyUpgrades.add(new Product("Poisoner", "Attack potions now reduce enemy attack power and deal damage over time", Shop.UPGRADESX * 0.3f, Shop.STARTINGHEIGHT * 0.3f, 5));
		upgradePaths.add(naturalUpgrades);
		upgradePaths.add(darkUpgrades);
		upgradePaths.add(militaryUpgrades);
		upgradePaths.add(alchemyUpgrades);
	}
	
	public void countTime() {
		totalTime += Gdx.graphics.getRawDeltaTime();
		if(spawnTimer > 0) {
			spawnTimer -= Gdx.graphics.getRawDeltaTime();
		}
	}
	
	public static int randint(int low, int high){
	    return (int)(Math.random()*(high-low+1) + low);
	}
}