package Entity;

import java.awt.Rectangle;

import Main.GamePanel;
import TileMap.Tile;
import TileMap.TileMap;

public abstract class MapObject 
{
	//tile stuff
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap;
	protected double ymap;
	
	//position and vector
	protected double x, y, dx, dy;
	
	//dimensions
	protected int width, height;
	
	//collision box
	protected int cwidth, cheight;
	
	//collision helpers
	protected int currRow;
	protected int currCol;
	
	//destinations, where we will be
	protected double xdest;
	protected double ydest;
	
	protected double xtemp;
	protected double ytemp;
	
	//4 point method, for collision
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	
	protected int topLeftType;
	protected int topRightType;
	protected int bottomLeftType;
	protected int bottomRightType;
	protected int bottomTileType;
	
	//animation
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean facingRight;
	
	//movement
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;
	protected boolean sliding;
	protected boolean swimming;
	
	//movement attributes
	protected double moveSpeed;
	protected double maxSpeed;
	protected double stopSpeed;
	protected double fallSpeed;
	
	protected double maxFallSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;
	
	//constructor
	public MapObject(TileMap tm)
	{
		tileMap = tm;
		tileSize = tm.getTileSize();
	}
	
	public boolean intersects(MapObject other)
	{
		Rectangle r1 = getCollisionBox();
		Rectangle r2 = other.getCollisionBox();
		return r1.intersects(r2);
	}
	
	public Rectangle getCollisionBox()
	{
		return new Rectangle((int)x - cwidth, (int) y - cheight, cwidth, cheight);
	}
	
	public void checkTileMapCollision()
	{
		currCol = (int)x / tileSize;
		currRow = (int)y / tileSize;
		
		xdest = x + dx;
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;
		
		calculateCorners(x,ydest);
		
		//going up
		if(dy < 0)
		{
			if(topLeft || topRight)
			{
				dy = 0;
				ytemp = currRow * tileSize + cheight / 2;
			}
			else
				ytemp += dy;
		}
		
		//going down
		if(dy > 0)
		{
			if(bottomLeft || bottomRight)
			{
				dy = 0;
				falling = false;
				ytemp = (currRow + 1) * tileSize - cheight / 2;
			}
			else
				ytemp += dy;

		}
		
		calculateCorners(xdest, y);
		
		//going left
		if(dx < 0)
		{
			//System.out.println("inside if dx < 0");
			if(topLeft || bottomLeft)
			{
				dx = 0;
				xtemp = currCol * tileSize + cwidth/2;
			}
			else
				xtemp += dx;

		}
		
		//going right
		if(dx > 0)
		{
			if(topRight || bottomRight)
			{
				dx = 0;
				xtemp = (currCol+1) * tileSize - cwidth / 2;
			}
			else
				xtemp += dx;
		}
		
		if(!falling)
		{
			calculateCorners(x, ydest+1);
			if(!bottomLeft && !bottomRight)
			{
				falling = true;
			}
		}
		
	}
	
	public void calculateCorners(double x, double y)
	{
		int leftTile = ((int) x - cwidth / 2) / tileSize;
		int rightTile = ((int) x + cwidth / 2 -1) / tileSize;
		int topTile = ((int) y - cheight / 2) / tileSize;
		int bottomTile = ((int) y + cheight / 2 - 1) / tileSize;

		
		int tl = tileMap.getType(topTile,leftTile);
		int tr = tileMap.getType(topTile, rightTile);
		int bl = tileMap.getType(bottomTile, leftTile);
		int br = tileMap.getType(bottomTile, rightTile);
		
		topLeft = (tl == Tile.BLOCKED);
		topRight = (tr == Tile.BLOCKED);
		bottomLeft = (bl == Tile.BLOCKED);
		bottomRight = (br == Tile.BLOCKED);
		//System.out.println(bottomLeft + " " + bottomRight);

	}
	
	public boolean notOnScreen()
	{
		return x + xmap + width < 0 || x + xmap - width > GamePanel.WIDTH || y + ymap + height < 0 || y + ymap - height > GamePanel.HEIGHT;
	}
	
	public void setLeft(boolean b)
	{
		left = b;
	}
	
	public void setRight(boolean b)
	{
		right = b;
	}
	public void setUp(boolean b)
	{
		up = b;
	}
	public void setDown(boolean b)
	{
		down = b;
	}
	public void setJumping(boolean b)
	{
		jumping = b;
	}
	
	public void setPosition(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void setVector(double dx, double dy)
	{
		this.dx = dx;
		this.dy = dy;
	}
	
	public void setMapPosition()
	{
		xmap = tileMap.getX();
		ymap = tileMap.getY();
	}
	
	public int getX()
	{
		return (int) x;
	}
	public int getY()
	{
		return (int) y;
	}
	public int getWidth()
	{
		return width;
	}
	public int getHeight()
	{
		return height;
	}
	public int getCWidth()
	{
		return cwidth;
	}
	public int getCHeight()
	{
		return cheight;
	}
}
