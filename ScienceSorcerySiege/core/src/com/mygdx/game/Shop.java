package com.mygdx.game;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Shop extends Sprite{
	
	public static final float STARTINGHEIGHT = 700;
	public static final float YSPACING = 120;
	public static final float ITEMSX = 20;
	public static final float UPGRADESX = 500;
	
	private Product selected;
	public LinkedList<Product> sales;
	public LinkedList<Product> upgrades;
	public LinkedList<Product> items;
	
	public Shop() {
		super(new Sprite(new Texture("BaseShop.png")));
		setSize(960 * 0.3f, 960 * 0.3f);
		setAlpha(0.9f); //Allows player to still see what is going on in their screen while in the shop
		
		sales = new LinkedList<Product>();
		upgrades = new LinkedList<Product>();
		items = new LinkedList<Product>();
	}
	
	public void determineUpgrades(Player p) {
		sales.clear();
		upgrades.clear();
		items.clear();
		
		if(p.upgrades.isEmpty()) {
			//Adding potential upgrades
			upgrades.add(new Product("Magic", "Harness the power of the unknown and perform close ranged attacks!", UPGRADESX * 0.3f, STARTINGHEIGHT * 0.3f, 0));
			upgrades.add(new Product("Science", "Unleash the power of knowledge upon the world and gain a ranged projectile attack!", UPGRADESX * 0.3f, (STARTINGHEIGHT - YSPACING) * 0.3f, 0));
		} else if(p.lastUpgrade.name.equals("Magic")) {
			upgrades.add(ScienceSorcerySiege.upgradePaths.getFirst().getFirst());
			upgrades.add(ScienceSorcerySiege.upgradePaths.get(1).getFirst());
		} else if(p.lastUpgrade.name.equals("Science")) {
			upgrades.add(ScienceSorcerySiege.upgradePaths.get(2).getFirst());
			upgrades.add(ScienceSorcerySiege.upgradePaths.get(3).getFirst());
		} else {
			for(LinkedList<Product> path : ScienceSorcerySiege.upgradePaths) {
				if(path.contains(p.lastUpgrade)) {
					if(path.indexOf(p.lastUpgrade) + 1 < path.size()) {
						upgrades.add(path.get(path.indexOf(p.lastUpgrade) + 1));
					}
				}
			}
		}
		int numRepairs = p.getInventory().get("Repair Base") == null? 0 : p.getInventory().get("Repair Base");
		items.add(new Product("Repair Base", "Heals your base for 100 health!", ITEMSX * 0.3f, STARTINGHEIGHT * 0.3f, 50 * numRepairs));
		int numHeals = p.getInventory().get("Heal") == null? 0 : p.getInventory().get("Heal");
		items.add(new Product("Heal", "Heals you base for 50 health!", ITEMSX * 0.3f, (STARTINGHEIGHT - YSPACING) * 0.3f, 50 * numHeals));
		if(p.upgrades.contains("Natural Magic")) {
			items.add(new Product("Totem", "Place these on your current tile to deal AoE damage to anyone near! You can even hide them in forests.", ITEMSX * 0.3f, (STARTINGHEIGHT - YSPACING * 2) * 0.3f, 50));
		} else if(p.upgrades.contains("Dark Arts")) {
			items.add(new Product("Rune", "Place these on your current tile to lay a demonic trap for your opponent equal to your attack power.", ITEMSX * 0.3f, (STARTINGHEIGHT - YSPACING * 2) * 0.3f, 75));
		} else if(p.upgrades.contains("Military")) {
			items.add(new Product("Wall", "Place these in front of you to block a pathway!", ITEMSX * 0.3f, (STARTINGHEIGHT - YSPACING * 2) * 0.3f, 25));
		} else if(p.upgrades.contains("Alchemy")) {
			items.add(new Product("Alchemy", "Place these on your current tile to deal AoE damage to anyone near! You can even hide them in forests.", ITEMSX * 0.3f, (STARTINGHEIGHT - YSPACING * 2) * 0.3f, 50));
		}
		if(!items.isEmpty()) {
			selected = items.getFirst();
			selected.select();
		} else if(!upgrades.isEmpty()) {
			selected = upgrades.getFirst();
			selected.select();
		} 
		sales.addAll(upgrades);
		sales.addAll(items);
		for(Product product : sales) {
    		product.translate(getX(), getY());
		}
	}
	
	public LinkedList<Product> displayProducts() {
		return sales;
	}
	
	public void selectSide() {
		//Moves the selected Upgrade to the side
		if(!items.isEmpty() && !upgrades.isEmpty()) {
			if(upgrades.contains(selected)) {
				if(upgrades.indexOf(selected) >= items.size()) {
					selected.deselect();
					selected = items.getLast();
				} else {
					selected.deselect();
					selected = items.get(upgrades.indexOf(selected));
				}
			} else if(items.contains(selected)) {
				if(items.indexOf(selected) >= upgrades.size()) {
					selected.deselect();
					selected = upgrades.getLast();
				} else {
					selected.deselect();
					selected = upgrades.get(items.indexOf(selected));
				}
			}
			selected.select();
		}
	}
	
	public void selectDown() {
		if(upgrades.contains(selected)) {
			if(upgrades.indexOf(selected) + 1 < upgrades.size()) {
				selected.deselect();
				selected = upgrades.get(upgrades.indexOf(selected) + 1);
			} else {
				selected.deselect();
				selected = upgrades.getFirst();
			}
		} else if(items.contains(selected)) {
			if(items.indexOf(selected) + 1 < items.size()) {
				selected.deselect();
				selected = items.get(items.indexOf(selected) + 1);
			} else {
				selected.deselect();
				selected = items.getFirst();
			}
		}
		selected.select();
	}
	
	public void selectUp() {
		if(upgrades.contains(selected)) {
			if(upgrades.indexOf(selected) > 0) {
				selected.deselect();
				selected = upgrades.get(upgrades.indexOf(selected) - 1);
			} else {
				selected.deselect();
				selected = upgrades.getLast();
			}
		} else if(items.contains(selected)) {
			if(items.indexOf(selected) > 0) {
				selected.deselect();
				selected = items.get(items.indexOf(selected) - 1);
			} else {
				selected.deselect();
				selected = items.getLast();
			}
		}
		selected.select();
	}
	
	public Product getSelected() {
		return selected;
	}

}
