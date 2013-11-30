package Entity;

import java.awt.Graphics2D;

import TileMap.TileMap;

public class Enemy extends MapObject 
{
	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected int damage; //damage on contact
	
	protected boolean flinching;
	protected long flinchingTimer;
	
	public Enemy(TileMap tm)
	{
		super (tm);
	}
	
	public boolean isDead()
	{
		return dead;
	}
	
	public int getDamage()
	{
		return damage;
	}
	
	public void hit(int damage)
	{
		if(dead || flinching)
			return;
		
		health -= damage;
		
		if(health < 0)
			health = 0;
		
		if(health == 0)
			dead = true;
		
		flinching = true;
		flinchingTimer = System.nanoTime();
		
	}
	
	public void update()
	{
		
	}

	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

}
