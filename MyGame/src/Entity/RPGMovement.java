package Entity;

import java.awt.Color;
import java.awt.Graphics2D;

import TileMap.TileMap;


public class RPGMovement extends Player
{
	//private static TileMap myTM;
	private static int bottomTile;
	private static int bottomTileType;
	private static int currentTile;
	private static int currentTileType;
	private static int rightTile;
	private static int rightTileType;
	private static int leftTile;
	private static int leftTileType;
	private static int topTile;
	private static int topTileType;
	private static int lastKnownKey;
	
	private static boolean wasSlide;
	private static long slidingTime;
	
	private static boolean wasFalling;
	private static long fallingTime;
	
	private static final int SLIDE_DISTANCE = 1000;
	
	private static final int SLIDING = 0;
	private static final int ONEWAY_RIGHT = 1;
	private static final int ONEWAY_LEFT = 2;
	private static final int LADDER = 3;
	private static final int BOTTOM_LADDER = 4;
	private static final int STICKY_RIGHT = 5;
	private static final int STICKY_LEFT = 6;
	private static final int STICKY_BOTTOM = 7;
	private static final int STICKY_TOP = 8;
	
	public RPGMovement(TileMap tm, Player player) {
		super(tm);

	}
	
	public static void draw(Graphics2D g, Player player)
	{
		//x,y,30,30,false
		g.setColor(Color.GREEN);

	}
	
	public static int getTileType(Player player, int row, int col)
	{
		return player.tileMap.getType(row, col);
	}
	
	public static void getTileType(Player player)
	{
		//note that this goes off the map location, so you need to check with the TILE NUMBER, not tileType
		bottomTile = player.tileMap.getMap(player.currRow+1, player.currCol);
		//topTile = player.tileMap.getMap(player.currRow-1, player.currCol);
		
		rightTile = player.tileMap.getMap(player.currRow, player.currCol+1);
		//leftTile = player.tileMap.getMap(player.currRow, player.currCol-1);
		
		currentTile = player.tileMap.getMap(player.currRow, player.currCol);
		
		switch (currentTile)
		{
			case 5:
				currentTileType = LADDER;
				break;
			case 6:
				currentTileType = BOTTOM_LADDER;
				break;
			default:
				currentTileType = -1;
		}
		
		switch(bottomTile)
		{
			case 23:
			case 25:
				bottomTileType = SLIDING;
				break;
			case 24:
				bottomTileType = ONEWAY_LEFT;
				break;
			case 5:
				bottomTileType = LADDER;
				break;
			default:
				bottomTileType = -1;
		}
		switch(rightTile)
		{
			case 8:
				rightTileType = STICKY_RIGHT;
				break;
			default:
				rightTileType = -1;
		}
	}

	public static void getNextPosition(Player player)
	{
		getTileType(player);
		if(rightTileType == STICKY_RIGHT)
		{
			if(!player.up)
			{
				player.falling = false;
				wasFalling = false;
				player.dy = 0;
				
			}
		}
		if(currentTileType == LADDER)
		{
			if(player.up)
			{
				player.falling = false;
				wasFalling = false;
				player.dy -= player.ladderSpeed;
				if(player.dy < -player.ladderSpeed)
					player.dy = -player.ladderSpeed;
			}
			else if(player.down)
			{
				player.falling = false;
				wasFalling = false;
				player.dy += player.ladderSpeed;
				if(player.dy > player.ladderSpeed)
					player.dy = player.ladderSpeed;
			}
			else
			{
				player.falling = false;
				wasFalling = false;
				player.dy = player.ladderSpeed / 3;
			}
		}
		if(currentTileType == BOTTOM_LADDER)
		{
			if(player.up)
			{
				player.falling = false;
				wasFalling = false;
				
				player.dy -= player.ladderSpeed;
				if(player.dy < -player.ladderSpeed)
					player.dy = -player.ladderSpeed;
			}
			else if(player.down)
			{
				player.falling = false;
				wasFalling = false;
				
				player.dy += player.ladderSpeed;
				if(player.dy > player.ladderSpeed)
					player.dy = player.ladderSpeed;
			}
		}
		
		if(bottomTileType == ONEWAY_RIGHT)
		{
			wasSlide = true;
			lastKnownKey =1;

			player.dx = player.oneWaySpeed;
		}
		if(bottomTileType == ONEWAY_LEFT)
		{
			wasSlide = true;
			lastKnownKey = 0;
			
			player.dx = -player.oneWaySpeed;
		}
		
		//player sliding
		//determine if the bottom tile type is sliding
		if(bottomTileType == SLIDING)
		{
			//tell the engine we were sliding
			wasSlide = true;

			//capture what direction the player was going
			if(player.left)
				lastKnownKey = 0;
			if(player.right)
				lastKnownKey = 1;
			
			//if going left
			if(lastKnownKey == 0)
			{
				//set up a timer for determining the sliding distances
				if(player.startSlide == true)
					slidingTime = System.nanoTime();
				
				//only set the timer the first time
				player.startSlide = false;
					
				//determine if we are speeding up or slowing down
				if(player.canSlide == true)
					player.dx -= player.slideSpeed;
				else
					player.dx += player.slideSpeed;
				
				//cap our speed
				if(player.canSlide == true && player.dx < -player.maxSlideSpeed)
					player.dx = -player.maxSlideSpeed;
					
				//determine how long we have been sliding
				long elapsed = (System.nanoTime() - slidingTime) / 1000000;
	
				//switch our direction from speeding up to slowing down
				if(player.dx < -(player.maxSlideSpeed - 0.1) && elapsed > SLIDE_DISTANCE)
					player.canSlide = false;

			}
			else if(lastKnownKey == 1)
			{
				if(player.startSlide == true)
					slidingTime = System.nanoTime();
				
				player.startSlide = false;
					
				if(player.canSlide == true)
					player.dx += player.slideSpeed;
				else
					player.dx -= player.slideSpeed;
				
				if(player.canSlide == true && player.dx > player.maxSlideSpeed)
					player.dx = player.maxSlideSpeed;
					

				long elapsed = (System.nanoTime() - slidingTime) / 1000000;
				
				if(player.dx > player.maxSlideSpeed - 0.1 && elapsed > SLIDE_DISTANCE)
					player.canSlide = false;
				
			}
			
			


		}
		//player slides off the last block moved
		else if(wasSlide == true)
		{
			if(lastKnownKey == 0)
			{
				player.dx -= player.slideSpeed+2;
				wasSlide = false;
			}
			else if(lastKnownKey == 1)
			{
				player.dx += player.slideSpeed+2;
				wasSlide = false;
			}
		}
		
		if(player.left)
		{	
			//subtract player move speed from its dx (where it is going to be) 
			player.dx -= player.moveSpeed;
			
			//if player dx is less than the max speed, cap it off
			if(player.dx < -player.maxSpeed)
				player.dx = -player.maxSpeed;
		}
		else if(player.right)
		{
			//add player move speed to its dx (where it is going to be)
			player.dx += player.moveSpeed;
			//if player dx is greater than the max speed, cap it off
			if(player.dx > player.maxSpeed)
				player.dx = player.maxSpeed;
		}
		//if player is not pressing any keys, it needs to slow down to a stop
		else
		{
			//if it is greater than zero (it was going right)
			if(player.dx > 0)
			{	
				//decrease player dx (stop Speed)
				player.dx -= player.stopSpeed;
				
				//if it is now less than zero
				if(player.dx < 0)
					//set it to zero (we have stopped)
					player.dx = 0;
			}
			//if it is less than zero (we were going left)
			else if(player.dx < 0)
			{
				//increase player dx (stopSpeed)
				player.dx += player.stopSpeed;
				
				//if it is now greater than zero
				if(player.dx > 0)
					//set it to zero (we have stopped)
					player.dx = 0;
			}
		}
		//cannot attack and move at same time, unless in air
		if( (player.currentAction == Player.SCRATCHING || player.currentAction == Player.FIREBALL) && !(player.jumping || player.falling))
		{
			player.dx = 0;
		}
		
		//jumping
		//if(player.jumping && !player.falling )
		if(player.jumping && player.dJumpNum < 2)
		{
			if(player.dJumpNum == 0)
			{
				player.dJumpNum += 1;
				player.dy = player.jumpStart;
				player.falling = true;
			}
			else if(player.upReleasedInAir)
			{
				player.dJumpNum += 1;
				player.dy = player.jumpStart;
				player.falling = true;
			}

		}
		
		//falling
		if(player.falling)
		{		
			if(wasFalling == false)
				fallingTime = System.nanoTime();
			//if in the air and gliding
			if(player.dy > 0 && player.gliding) 
				player.dy += player.fallSpeed * 0.1;
			//else if just in the air
			else
				player.dy += player.fallSpeed;
			
			//if moving down
			if(player.dy > 0)
				player.jumping = false;
			
			//if moving up and not jumping
			if(player.dy < 0 && !player.jumping)
				player.dy += player.stopJumpSpeed;
			
			//cap fall speed
			if(player.dy > player.maxFallSpeed)
				player.dy = player.maxFallSpeed;
			
			wasFalling = true;
			

		}
		
		//falling damage
		if(wasFalling)
		{
			if((player.bottomLeft || player.bottomRight) && !player.jumping)
			{
				long elapsed = ((System.nanoTime() - fallingTime) / 1000000)/200;
				if(elapsed > 8)
					player.hit((int)elapsed - 8);
				wasFalling = false;
			}
		}
		
		
	}
}
