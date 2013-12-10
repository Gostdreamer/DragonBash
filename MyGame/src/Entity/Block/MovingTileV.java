package Entity.Block;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.MapObject;
import TileMap.TileMap;

public class MovingTileV extends MapObject
{

	//normal sprite
	private BufferedImage[] sprites;
	
	private int distance;
	private long timeTraveled;
	
	public MovingTileV(TileMap tm, int dist, double speed) 
	{
		super(tm);
		
		moveSpeed = speed;
		
		width = 30;
		height = 30;
		
		cwidth = 20;
		cheight = 20;
		
		up = true;
		
		distance = dist;
		timeTraveled = 0;
		//load sprite
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
	
	public int getDir()
	{
		if(up)
			return 0;
		else
			return 1;
				
	}
	
	public double getSpeed()
	{
		return moveSpeed;
	}
	
	public void changeDir()
	{
		if(up)
		{
			up = false;
			down = true;
		}
		else
		{
			up = true;
			down = false;
		}
	}
	
	public void update()
	{
		if(timeTraveled == 0)
			timeTraveled = System.nanoTime();
		
		long elapsed = ((System.nanoTime() - timeTraveled)/1000000)/200;
		
		if(elapsed >= distance)
		{
			changeDir();
			timeTraveled = 0;
		}
		
		if(up)
			dy = moveSpeed;
		else
			dy = -moveSpeed;
		
		//check our collision and set our position
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		//update out animation
		animation.update();
		
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
	

}
