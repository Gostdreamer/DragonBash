package GameState;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Entity.Enemy;
import Entity.Explosion;
import Entity.HUD;
import Entity.Player;
import Entity.Block.FallingBlock;
import Entity.Block.MovingTileH;
import Entity.Block.MovingTileV;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

public abstract class LevelStates implements GameState
{
	protected GameStateManager gsm;
	
	//Core level objects 
	protected TileMap tileMap;
	protected Background bg;
	protected HUD hud;
	
	//Entities
	protected Player player;
	protected ArrayList<Enemy> enemies;
	protected ArrayList<MovingTileH>movingH;
	protected ArrayList<MovingTileV>movingV;
	protected ArrayList<FallingBlock>fallingBlocks;
	protected Point[] enemyPoints;
	protected Point[] movingHPoints;
	protected Point[] movingVPoints;
	protected ArrayList<Point> fallingBPoints;
	protected boolean[] fallingBCanRespawn;
	protected int[] fallingBRespawnTimers;

	
	//Other Objects
	protected ArrayList<Explosion> explosions;
	
	/**
	 *  Creates Tile Map
	 */
	@Override
	public void init() 
	{
		tileMap = new TileMap(30);
		tileMap.setPosition(0,0);


	}
	
	/**
	 * Adds the player, enemies, moving blocks and initializes the explosions and HUD
	 */
	public void afterMap()
	{
		//Makes the player
		player = new Player(tileMap);
		player.setDeath(false);
		player.setPosition(50,150);
		
		//Creates Enemies
		populateEnemies();
		populateMovingH();
		populateMovingV();
		populateFallingBlocks();
		
		//Initializes the ArrayList of explosions
		explosions = new ArrayList<Explosion>();
		
		//Initializes the HUD
		hud = new HUD(player);
	}
	
	/**
	 * Resets position of tilemap and player, sets player death to false, and removes enemies
	 * Also sets the state to GameOver
	 */
	public void reset()
	{
		tileMap.setPosition(0,0);
		player.setPosition(50,250);
		player.setDeath(false);
		
		for(int i = 0; i < enemies.size(); i++)
		{
			Enemy e = enemies.get(i);
			e.hit(100);
			if(e.isDead())
			{
				enemies.remove(i);
				i--;
			}
		}
		
		gsm.setState(GameStateManager.GAMEOVER);
	}
	
	/** 
	 * Populates Horizontal Moving blocks on screen
	 */
	protected abstract void populateMovingH();
	
	protected abstract void populateFallingBlocks();
	
	/**
	 * Populates Vertical Moving blocks on screen
	 */
	protected abstract void populateMovingV();
	
	/**
	 * Populates any and all enemies on screen
	 */
	protected abstract void populateEnemies();
	
	/**
	 * Updates all objects on screen
	 */
	@Override
	public void update() 
	{
		if(player.isDead())
		{
			reset();
		}
		else
			player.update();
		
		//scroll the tile map depending on where we are 
		tileMap.setPosition(GamePanel.WIDTH / 2 - player.getX(),GamePanel.HEIGHT/2 - player.getY());
		
		//set background
		bg.setPosition(tileMap.getX(), tileMap.getY());
		
		//check if player is attacking
		player.checkAttack(enemies);
		player.checkMovingH(movingH);
		player.checkMovingV(movingV);
		player.checkFallingB(fallingBlocks);

		//update all enemies
		for(int i = 0; i < enemies.size(); i++)
		{
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead())
			{
				enemies.remove(i);
				i--;
				explosions.add(new Explosion(e.getX(), e.getY()));
			}
		}
		
		//update all the explosions
		for(int i = 0; i < explosions.size(); i++)
		{
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove())
			{
				explosions.remove(i);
				i--;
			}
		}
		
		for(int i = 0; i < fallingBlocks.size(); i++)
		{
			fallingBlocks.get(i).update();
			
			if(fallingBlocks.get(i).getHasFallen() && !fallingBlocks.get(i).getCanRespawn())
			{
				fallingBlocks.remove(i);
				fallingBPoints.remove(i);
				i--;
			}
		}
		
		for(int i = 0; i < movingH.size(); i++)
		{
			movingH.get(i).update();
		}
		
		for(int i = 0; i < movingV.size(); i++)
		{
			movingV.get(i).update();
		}	
	}

	/**
	 * Draws all objects on screen
	 */
	@Override
	public void draw(Graphics2D g) 
	{
		//draw background
		bg.draw(g);
		
		//draw tile map
		tileMap.draw(g);
		
		//draw enemies
		for(int i = 0; i < enemies.size();i++)
		{
			enemies.get(i).draw(g);
		}
		
		//draw explosions
		for(int i = 0; i < explosions.size(); i++)
		{
			explosions.get(i).setMapPosition(tileMap.getX(), tileMap.getY());
			explosions.get(i).draw(g);
		}
		
		for(int i = 0; i < movingH.size(); i++)
		{
			movingH.get(i).draw(g);
		}
		
		for(int i = 0; i < movingV.size(); i++)
		{
			movingV.get(i).draw(g);
		}
		
		for(int i = 0; i < fallingBlocks.size(); i++)
		{
			if(!fallingBlocks.get(i).getCanRespawn() || !fallingBlocks.get(i).getHasFallen())
				fallingBlocks.get(i).draw(g);
			else if(fallingBlocks.get(i).getRedraw())
			{
				fallingBlocks.get(i).resetAll(fallingBPoints.get(i).x, fallingBPoints.get(i).y);
			}
		}
		
		//draw player
		player.draw(g);
		
		//draw hud
		hud.draw(g);
	}

	/**
	 * Deals with key presses
	 */
	@Override
	public void keyPressed(int k) 
	{
		if(k == KeyEvent.VK_LEFT)
		{
			player.setCanSlide(true);
			player.setStartSlide(true);
			player.setLeft(true);
			player.setUpdated(true);
		}
		if(k == KeyEvent.VK_RIGHT)
		{
			player.setCanSlide(true);
			player.setStartSlide(true);
			player.setRight(true);
			player.setUpdated(true);
		}
		if(k==KeyEvent.VK_UP)
			player.setUp(true);
		if(k==KeyEvent.VK_DOWN)
			player.setDown(true);
		if(k==KeyEvent.VK_W)
			player.setJumping(true);
		if(k==KeyEvent.VK_E)
			player.setGliding(true);
		if(k==KeyEvent.VK_R)
			player.setScratching();
		if(k==KeyEvent.VK_S)
			player.setCrawling(true);
		if(k==KeyEvent.VK_F)
		{
			player.setFiring();
		}
	}

	@Override
	public void keyReleased(int k) 
	{
		if(k == KeyEvent.VK_LEFT)
		{
			player.setLeft(false);
			player.setUpdated(false);
			player.checkSprint();
		}
		if(k == KeyEvent.VK_RIGHT)
		{
			player.setRight(false);
			player.setUpdated(false);
			player.checkSprint();
		}
		if(k==KeyEvent.VK_UP)
			player.setUp(false);
		if(k==KeyEvent.VK_DOWN)
			player.setDown(false);
		if(k==KeyEvent.VK_S)
			player.setCrawling(false);
		if(k==KeyEvent.VK_W)
		{
			player.setJumping(false);
			if(player.getCurrentAction() == 2)
				player.setUpReleasedInAir(true);
		}
		if(k==KeyEvent.VK_E)
			player.setGliding(false);

	}

}
