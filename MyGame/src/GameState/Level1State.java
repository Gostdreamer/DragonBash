package GameState;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Entity.Enemy;
import Entity.Explosion;
import Entity.HUD;
import Entity.Player;
import Entity.Enemies.Slugger;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

public class Level1State extends GameState
{
	private TileMap tileMap;
	private Background bg;
	private Player player;
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	private HUD hud;
	
	public Level1State(GameStateManager gsm)
	{
		this.gsm = gsm;
		init();
	}

	@Override
	public void init() 
	{
		tileMap = new TileMap(30);
		tileMap.loadMapType("/Maps/level1-1Type.map");
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0,0);
		
		bg = new Background("/Backgrounds/grassbg1.gif",0.1);
		
		player = new Player(tileMap);
		player.setPosition(50,50);
		
		populateEnemies();
		
		explosions = new ArrayList<Explosion>();
		
		
		hud = new HUD(player);
		
	}
	
	private void populateEnemies()
	{
		enemies = new ArrayList<Enemy>();
//		Slugger s;
//		Point[] points = new Point[]{
//			new Point(860,200),
//			new Point(1525,200),
//			new Point(1680,200),
//			new Point(1800,200)
//		};
//		
//		for(int i = 0; i < points.length; i++)
//		{
//			s = new Slugger(tileMap);
//			s.setPosition(points[i].x, points[i].y);
//			enemies.add(s);
//		}
		
		
	}

	@Override
	public void update() 
	{
		player.update();
		
		tileMap.setPosition(GamePanel.WIDTH / 2 - player.getX(),GamePanel.HEIGHT/2 - player.getY());
		
		//set background
		bg.setPosition(tileMap.getX(), tileMap.getY());
		
		//check if player is attacking
		player.checkAttack(enemies);
		
		
		
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
		
		for(int i = 0; i < explosions.size(); i++)
		{
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove())
			{
				explosions.remove(i);
				i--;
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
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
		
		//draw player
		player.draw(g);
		
		//draw hud
		hud.draw(g);
		
	}

	@Override
	public void keyPressed(int k) 
	{
		if(k == KeyEvent.VK_LEFT)
		{
			player.setCanSlide(true);
			player.setStartSlide(true);
			player.setLeft(true);
		}
		if(k == KeyEvent.VK_RIGHT)
		{
			player.setCanSlide(true);
			player.setStartSlide(true);
			player.setRight(true);
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
		if(k==KeyEvent.VK_F)
		{
			player.setFiring();
		}
		
		
		
	}

	@Override
	public void keyReleased(int k) 
	{
		if(k == KeyEvent.VK_LEFT)
			player.setLeft(false);
		if(k == KeyEvent.VK_RIGHT)
			player.setRight(false);
		if(k==KeyEvent.VK_UP)
			player.setUp(false);
		if(k==KeyEvent.VK_DOWN)
			player.setDown(false);
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
