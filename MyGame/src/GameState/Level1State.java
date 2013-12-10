package GameState;

import java.awt.Point;
import java.util.ArrayList;

import Entity.Enemy;
import Entity.Block.FallingBlock;
import Entity.Block.MovingTileH;
import Entity.Block.MovingTileV;
import Entity.Enemies.Slugger;
import TileMap.Background;
/****************************************************
 * All Level 1 Information is held here             *
 ****************************************************/
public class Level1State extends LevelStates
{
	
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
		super.init();
		
		tileMap.loadMapType("/Maps/level1-1Type.map");
		tileMap.loadTiles("/Tilesets/grasstileset5.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		
		//Adds a background
		bg = new Background("/Backgrounds/grassbg1.gif",0.1);
		
		enemyPoints = new Point[]{
				new Point(860,200),
				new Point(1525,200),
				new Point(1680,200),
				new Point(1800,200)
		};
		
		movingHPoints = new Point[]{
				//new Point(150,150)
		};
		
		movingVPoints = new Point[]{
				//new Point(150,150)
		};

		fallingBPoints = new ArrayList<Point>();
		
		fallingBPoints.add(new Point(150,150));
		fallingBPoints.add(new Point(200,150));
		fallingBPoints.add(new Point(250, 150));
		fallingBPoints.add(new Point(300,150));
		
		super.afterMap();
		
	}
	
	//Create Moving tiles in the game world
	protected void populateMovingH()
	{
		movingH = new ArrayList<MovingTileH>();
		
		MovingTileH newTile;
		
		for(int i = 0; i < movingHPoints.length; i++)
		{
			newTile = new MovingTileH(tileMap,15,1.0);
			newTile.setPosition(movingVPoints[i].x, movingVPoints[i].y);
			movingH.add(newTile);
		}
	}
	
	protected void populateMovingV()
	{
		movingV = new ArrayList<MovingTileV>();
		MovingTileV newTile;
		
		for(int i = 0; i < movingVPoints.length; i++)
		{
			newTile = new MovingTileV(tileMap,10,1.0);
			newTile.setPosition(movingVPoints[i].x, movingVPoints[i].y);
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
		
		//loops through each point we specified, and adds them onto the screen as well as into our array list of enemies
		for(int i = 0; i < enemyPoints.length; i++)
		{
			s = new Slugger(tileMap);
			s.setPosition(enemyPoints[i].x, enemyPoints[i].y);
			enemies.add(s);
		}
		
	}
	
	protected void populateFallingBlocks()
	{
		fallingBlocks = new ArrayList<FallingBlock>();
		
		FallingBlock tempBlock;
		
		int[] life = {4,4,4,4};
		double[] fallSpeed = {0.5,0.5,2,2};
		boolean [] alwaysFall = {true, true, false, false};
		boolean[] canRespawn = {true, false, true, false};
		int[] respawnTimers = {5,-1,5,-1};
		
		
		for(int i = 0; i < 4; i++)
		{
			tempBlock = new FallingBlock(tileMap, life[i],fallSpeed[i], alwaysFall[i], canRespawn[i], respawnTimers[i]);
			tempBlock.setPosition(fallingBPoints.get(i).x, fallingBPoints.get(i).y);
			fallingBlocks.add(tempBlock);
		}
	}
}
