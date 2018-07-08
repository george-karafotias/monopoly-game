package core;

import java.awt.Color;
import java.util.ArrayList;

public class Player {
	
	private String name;
	private Color color;
	private int position;
	private double amount;
	private boolean isAuto;
	private boolean isInJail;
	private int inJailRounds;
	private int wins;
	private ArrayList<Property> properties;
	
	public Player(String name, Color color, boolean isAuto) {
		this.name = name;
		this.color = color;
		this.isAuto = isAuto;
		this.wins = 0;
		reset();
	}
	
	public void reset() {
		this.isInJail = false;
		this.inJailRounds = 0;
		this.amount = 400;
		this.position = 0;
		this.properties = new ArrayList<Property>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public ArrayList<Property> getProperties() {
		return properties;
	}
	
	public void addProperty(Property property) {
		properties.add(property);
	}

	public Color getColor() {
		return color;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public boolean isAuto() {
		return isAuto;
	}
	
	public boolean isInJail() {
		return this.isInJail;
	}
	
	public void stillInJail() {
		this.inJailRounds++;
	}
	
	public int howLongInJail() {
		return this.inJailRounds;
	}
	
	public void goToJail() {
		this.inJailRounds = 0;
		this.isInJail = true;
	}
	
	public void outOfJail() {
		this.inJailRounds = 0;
		this.isInJail = false;
	}
	
	public void youWon() {
		this.wins++;
	}

	public int getWins() {
		return wins;
	}
}
