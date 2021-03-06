package GameState;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Entity.Enemy;
import Entity.Explosion;
import Entity.HUD;
import Entity.MovingTileH;
import Entity.MovingTileV;
import Entity.Player;
import Entity.Enemies.Slugger;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;
/****************************************************
 * All Level 1 Information is held here             *
 ****************************************************/
public class Level1State extends LevelStates
{
//	private GameStateManager gsm;
//	
//	//Core level objects 
//	private TileMap tileMap;
//	private Background bg;
//	private HUD hud;
//	
//	//Entities
//	private Player player;
//	private ArrayList<Enemy> enemies;
//	private ArrayList<MovingTileH>movingH;
//	private ArrayList<MovingTileV>movingV;
//	//private ArrayList<MovingTileV> horizontalTile;
//
//	//Other Objects
//	private ArrayList<Explosion> explosions;
	
	
	//Constructor
	public Level1State(GameStateManager gsm)
	{
		this.gsm = gsm;
		init();
	}

	@Override
	//Initializes the game
	public void init() 
	{
		//Sets up the map and level creation
		//tileMap = new TileMap(30);
		super.init();
		
		tileMap.loadMapType("/Maps/level1-1Type.map");
		tileMap.loadTiles("/Tilesets/grasstileset5.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		//tileMap.setPosition(0,0);
		
		//Adds a background
		bg = new Background("/Backgrounds/grassbg1.gif",0.1);
		
		super.afterMap();
		
		//Makes the player
		//player = new Player(tileMap);
		//player.setDeath(false);
		//player.setPosition(50,150);
		
		//Creates Enemies
		//populateEnemies();
		//populateMovingH();
		//populateMovingV();
		
		//Initializes the ArrayList of explosions
		//explosions = new ArrayList<Explosion>();
		
		//Initizlizes the HUD
		//hud = new HUD(player);
		
	}
	
//	public void reset()
//	{
//		tileMap.setPosition(0,0);
//		player.setPosition(50,250);
//		player.setDeath(false);
//		
//		for(int i = 0; i < enemies.size(); i++)
//		{
//			Enemy e = enemies.get(i);
//			e.hit(100);
//			if(e.isDead())
//			{
//				enemies.remove(i);
//				i--;
//			}
//		}
//		
//		gsm.setState(GameStateManager.GAMEOVER);
//	}
	
	//Create Moving tiles in the game world
	protected void populateMovingH()
	{
		movingH = new ArrayList<MovingTileH>();
		
		MovingTileH newTile;
		
		Point[] points = new Point[]{
				new Point(150,150)
		};
		
		for(int i = 0; i < points.length; i++)
		{
			newTile = new MovingTileH(tileMap,15,1.0);
			newTile.setPosition(points[i].x, points[i].y);
			movingH.add(newTile);
		}
	}
	
	protected void populateMovingV()
	{
		movingV = new ArrayList<MovingTileV>();
		MovingTileV newTile;
		Point[] points = new Point[]{
				new Point(150,150)
		};
		
		for(int i = 0; i < points.length; i++)
		{
			newTile = new MovingTileV(tileMap,10,1.0);
			newTile.setPosition(points[i].x, points[i].y);
			movingV.add(newTile);
		}
		
	}
	
	//Create Enemies in the game world
	protected void populateEnemies()
	{
		//Initialized the enemy array list
		enemies = new ArrayList<Enemy>();
		
		//creates a new enemy
		Slugger s;
		
		//sets up where we want the enemies to be
		Point[] points = new Point[]{
			new Point(860,200),
			new Point(1525,200),
			new Point(1680,200),
			new Point(1800,200)
		};
		
		//loops through each point we specified, and adds them onto the screen as well as into our array list of enemies
		for(int i = 0; i < points.length; i++)
		{
			s = new Slugger(tileMap);
			s.setPosition(points[i].x, points[i].y);
			enemies.add(s);
		}
		
	}

	//Update all objects on the screen
	//public void update() 
	//{
		
//		if(player.isDead())
//		{
//			reset();
//			//gsm.setState(GameStateManager.GAMEOVER);
//		}
//		else
//			player.update();
//		
//		//scroll the tile map depending on where we are 
//		tileMap.setPosition(GamePanel.WIDTH / 2 - player.getX(),GamePanel.HEIGHT/2 - player.getY());
//		
//		//set background
//		bg.setPosition(tileMap.getX(), tileMap.getY());
//		
//		//check if player is attacking
//		player.checkAttack(enemies);
//		player.checkMovingH(movingH);
//		player.checkMovingV(movingV);
//
//		//update all enemies
//		for(int i = 0; i < enemies.size(); i++)
//		{
//			Enemy e = enemies.get(i);
//			e.update();
//			if(e.isDead())
//			{
//				enemies.remove(i);
//				i--;
//				explosions.add(new Explosion(e.getX(), e.getY()));
//			}
//		}
//		
//		//update all the explosions
//		for(int i = 0; i < explosions.size(); i++)
//		{
//			explosions.get(i).update();
//			if(explosions.get(i).shouldRemove())
//			{
//				explosions.remove(i);
//				i--;
//			}
//		}
//		
//		for(int i = 0; i < movingH.size(); i++)
//		{
//			movingH.get(i).update();
//		}
//		
//		for(int i = 0; i < movingV.size(); i++)
//		{
//			movingV.get(i).update();
//		}
	//}

	//draw our objects on the screen
//	public void draw(Graphics2D g) {
//		//draw background
//		bg.draw(g);
//		
//		//draw tile map
//		tileMap.draw(g);
//		
//		//draw enemies
//		for(int i = 0; i < enemies.size();i++)
//		{
//			enemies.get(i).draw(g);
//		}
//		
//		//draw explosions
//		for(int i = 0; i < explosions.size(); i++)
//		{
//			explosions.get(i).setMapPosition(tileMap.getX(), tileMap.getY());
//			explosions.get(i).draw(g);
//		}
//		
//		for(int i = 0; i < movingH.size(); i++)
//		{
//			movingH.get(i).draw(g);
//		}
//		
//		for(int i = 0; i < movingV.size(); i++)
//		{
//			movingV.get(i).draw(g);
//		}
//		
//		//draw player
//		player.draw(g);
//		
//		//draw hud
//		hud.draw(g);
//		
//	}

	//Handle key presses
//	public void keyPressed(int k) 
//	{
//		if(k == KeyEvent.VK_LEFT)
//		{
//			player.setCanSlide(true);
//			player.setStartSlide(true);
//			player.setLeft(true);
//			player.setUpdated(true);
//		}
//		if(k == KeyEvent.VK_RIGHT)
//		{
//			player.setCanSlide(true);
//			player.setStartSlide(true);
//			player.setRight(true);
//			player.setUpdated(true);
//		}
//		if(k==KeyEvent.VK_UP)
//			player.setUp(true);
//		if(k==KeyEvent.VK_DOWN)
//			player.setDown(true);
//		if(k==KeyEvent.VK_W)
//			player.setJumping(true);
//		if(k==KeyEvent.VK_E)
//			player.setGliding(true);
//		if(k==KeyEvent.VK_R)
//			player.setScratching();
//		if(k==KeyEvent.VK_S)
//			player.setCrawling(true);
//		if(k==KeyEvent.VK_F)
//		{
//			player.setFiring();
//		}
//		
//		
//		
//	}

	//Handle key releases
//	public void keyReleased(int k) 
//	{
//		if(k == KeyEvent.VK_LEFT)
//		{
//			player.setLeft(false);
//			player.setUpdated(false);
//			player.checkSprint();
//		}
//		if(k == KeyEvent.VK_RIGHT)
//		{
//			player.setRight(false);
//			player.setUpdated(false);
//			player.checkSprint();
//		}
//		if(k==KeyEvent.VK_UP)
//			player.setUp(false);
//		if(k==KeyEvent.VK_DOWN)
//			player.setDown(false);
//		if(k==KeyEvent.VK_S)
//			player.setCrawling(false);
//		if(k==KeyEvent.VK_W)
//		{
//			player.setJumping(false);
//			if(player.getCurrentAction() == 2)
//				player.setUpReleasedInAir(true);
//		}
//		if(k==KeyEvent.VK_E)
//			player.setGliding(false);
//
//	}
//	
}
