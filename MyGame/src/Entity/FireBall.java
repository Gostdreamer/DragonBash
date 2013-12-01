package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import TileMap.TileMap;
/****************************************************
 * Fireball Entity                                  *
 ***************************************************/
public class FireBall extends MapObject
{
	
	private boolean hit; //has it hit something?
	private boolean remove; //does it need to be removed?
	
	//normal sprite
	private BufferedImage[] sprites;
	
	//sprite when it hits something
	private BufferedImage[] hitSprites;
	
	//constructor
	public FireBall(TileMap tm, boolean right)
	{
		super(tm);
		
		//by default, we are facing right
		facingRight = true;
		
		//init our move speed
		moveSpeed = 3.8;
		
		//determine our projected movement based on our facing
		if(right)
			dx = moveSpeed;
		else
			dx = -moveSpeed;
		
		//width/height of the sprite
		width = 30;
		height = 30;
		
		//width/height in the game (collision w/h)
		cwidth = 14;
		cheight = 14;
		
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
			
			//3 is the number of frames in the hit animation
			hitSprites = new BufferedImage[3];
			for(int i = 0; i <hitSprites.length; i++)
			{
				hitSprites[i] = spritesheet.getSubimage(i*width, height, width, height);
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
	
	//set that we hit something
	public void setHit()
	{
		//if we are already set to hit, get out
		if(hit)
			return;
		
		//set hit to true
		hit = true;
		
		//set up the hit animation
		animation.setFrames(hitSprites);
		animation.setDelay(70);
		dx = 0;
	}
	
	//know if we should be removed
	public boolean shouldRemove()
	{
		return remove;
	}
	
	//update us!
	public void update()
	{
		//check our collision and set our position
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		//if we are not moving, and we are not hit, say we are hit
		if(dx == 0 && !hit)
		{
			setHit();
		}
		
		//update out animation
		animation.update();
		
		//if we are hit, and our animation has played once, remove us
		if(hit && animation.hasPlayedOnce() )
			remove = true;

		
		
	}
	
	//draw us
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
