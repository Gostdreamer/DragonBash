package Entity.Block;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import TileMap.TileMap;
import Entity.Animation;
import Entity.MapObject;

public class FallingBlock extends MapObject 
{
	private BufferedImage[] sprites;
	private boolean shouldFall;
	private boolean hasFallen;
	
	private int lifeTime;
	private long playerStandTimer;
	private boolean timerSet;
	
	private boolean canRespawn;
	private int respawnDelay;
	
	private boolean isFalling;
	private boolean isVisible;
	
	private boolean alwaysFall;

	private boolean redrawTimerSet;
	private boolean shouldRedraw;
	private long redrawTimer;

	public FallingBlock(TileMap tm, int life, double myFallSpeed, boolean shouldAlwaysFall, boolean ableRespawn, int respawn) 
	{
		super(tm);
		// TODO Auto-generated constructor stub
		width = 30;
		height = 30;
		
		cwidth = 20;
		cheight = 20;
		
		right = true;
		shouldFall = false;
		hasFallen = false;
		isFalling = false;
		isVisible = true;
		alwaysFall = shouldAlwaysFall;
		
		respawnDelay = respawn;
		canRespawn = ableRespawn;
		redrawTimer = 0;
		playerStandTimer = -1;
		
		lifeTime = life;
		
		fallSpeed = myFallSpeed;
		
		dy = 0;
		
		try
		{
			BufferedImage spritesheet;
			spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/fireball.gif"));

			//4 is the number of frames in the basic animation
			sprites = new BufferedImage[4];
			for(int i = 0; i < sprites.length; i++)
			{
				sprites[i] = spritesheet.getSubimage(i*width, 0, width, height);
			}
			
			//create our animation
			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(70);
			
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void reset()
	{
		shouldFall = hasFallen = timerSet = isFalling = false;
		isVisible = true;
		playerStandTimer = -1;
	}
	
	public void resetAll(int x, int y)
	{
		shouldFall = hasFallen = timerSet = redrawTimerSet = shouldRedraw = isFalling = false;
		isVisible = true;
		redrawTimer = 0; 
		playerStandTimer = -1;
		setPosition(x, y);
	}
	
	public void checkFall()
	{
		if(playerStandTimer > 0)
		{
			System.out.println((System.nanoTime() - playerStandTimer)/1000000/200);
			if((System.nanoTime() - playerStandTimer)/1000000/200 > lifeTime)
			{
				shouldFall = true;
			}
		}
	}
	
	public void update()
	{	
		checkFall();
		
		dy = 0;
		
		if(shouldFall == true)
		{
			isFalling = true;
			dy += fallSpeed;
		}
		//check our collision and set our position
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		//update out animation
		animation.update();

		if(bottomLeft && bottomRight)
		{
			hasFallen = true;
			isVisible = false;
			
			if(!redrawTimerSet)
			{
				redrawTimer = System.nanoTime();
				redrawTimerSet = true;
			}
		}
		
		if(hasFallen && canRespawn)
		{
			if((System.nanoTime() - redrawTimer)/1000000/200 > respawnDelay)
				shouldRedraw = true;
		}
		
	}
	
	public void draw(Graphics2D g)
	{
		setMapPosition(); //all MapObjects must do this!
		
		//if we are facing right, draw us as is
		if(facingRight)
			g.drawImage(animation.getImage(), (int)(x+xmap-width/2), (int)(y+ymap-height/2), null);
		//otherwise, draw us reversed
		else
			g.drawImage(animation.getImage(), (int)(x+xmap-width/2 + width), (int)(y+ymap-height/2),-width, height, null);	
	}
	
	public void setShouldRedraw(boolean b)
	{
		shouldRedraw = b;
	}
	
	public void setTimer()
	{
		if(!timerSet)
		{
			playerStandTimer = System.nanoTime();
			timerSet = true;
		}
	}
	
	public boolean getAlwaysFall()
	{
		return alwaysFall;
	}
	
	public boolean getTimer()
	{
		return timerSet;
	}
	
	public boolean getRedraw()
	{
		return shouldRedraw;
	}
	
	public boolean getCanRespawn()
	{
		return canRespawn;
	}
	
	public boolean getHasFallen()
	{
		return hasFallen;
	}
	
	public boolean getIsVisible()
	{
		return isVisible;
	}
	
	public boolean getIsFalling()
	{
		return isFalling;
	}
}
