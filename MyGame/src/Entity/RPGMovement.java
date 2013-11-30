package Entity;

import TileMap.Tile;
import TileMap.TileMap;
/****************************************************
 * Controls the PLAYER movement (NOT enemy)         *
 ***************************************************/

public class RPGMovement extends Player
{
	//Set up tiles relative to players position
	private static int bottomTileType;
	private static int currentTileType;
	//private static int rightTileType;
	//private static int leftTileType;
	//private static int topTileType;
	private static int lastKnownKey;
	
	//remember if we were sliding, and for how long
	private static boolean wasSlide;
	private static long slidingTime;
	
	//remember if we were falling, and for how long
	private static boolean wasFalling;
	private static long fallingTime;
	
	//how far can we slide?
	private static final int SLIDE_DISTANCE = 1000;
	
	public RPGMovement(TileMap tm, Player player) {
		super(tm);

	}
	
	//returns the tile type at the specified location
	public static int getTileType(Player player, int row, int col)
	{
		//If anything is greater than zero (meaning it exists), get the tile type 
		if(row > -1 && col > -1)
			return player.tileMap.getType(row, col);
		else //Otherwise it must not exist, so return -1
			return -1;
	}

	//determine where the player is going to be 
	public static void getNextPosition(Player player)
	{
		//figure out what tiles are relative to the player
		//rightTileType = getTileType(player, player.currRow, player.currCol+1);
		//leftTileType = getTileType(player, player.currRow, player.currCol-1);
		bottomTileType = getTileType(player, player.currRow+1, player.currCol);
		//topTileType = getTileType(player, player.currRow-1, player.currCol);
		currentTileType = getTileType(player, player.currRow, player.currCol);
		
		//RIGHT tile checking
		
//		//if the right tile is a sticky tile, make the player unable to fall
//		if(rightTileType == Tile.STICKY_RIGHT)
//		{
//			if(!player.up)
//			{
//				player.falling = false;
//				wasFalling = false;
//				player.dy = 0;
//				
//			}
//		}
		
		//CURRENT tile checking
		
		//if we are currently on a ladder
		if(currentTileType == Tile.LADDER)
		{
			//if we are pressing up arrow
			if(player.up)
			{
				//we are not going to fall, and we were not falling
				player.falling = false;
				wasFalling = false;
				
				//set my dy (projected movement on the y axis)
				player.dy -= player.ladderSpeed;
				if(player.dy < -player.ladderSpeed)
					player.dy = -player.ladderSpeed;
			}
			//if we are pressing down arrow
			else if(player.down)
			{
				//we are not going to fall, and we were not falling
				player.falling = false;
				wasFalling = false;
				
				//set my dy (projected movement on the y axis)
				player.dy += player.ladderSpeed;
				if(player.dy > player.ladderSpeed)
					player.dy = player.ladderSpeed;
			}
			//we are not pressing up or down arrow
			else
			{
				//we are not going to fall, and we were not falling
				player.falling = false;
				wasFalling = false;
				
				//slide down the ladder at 1/3 of ladder climbing speed
				player.dy = player.ladderSpeed / 3;
			}
		}
		
		//if we are at the bottom of the ladder
		if(currentTileType == Tile.BOTTOM_LADDER)
		{
			//and we are pressing the up arrow key
			if(player.up)
			{
				//we are not going to fall, and we were not falling
				player.falling = false;
				wasFalling = false;
				
				//set my dy (projected movement on the y axis)
				player.dy -= player.ladderSpeed;
				if(player.dy < -player.ladderSpeed)
					player.dy = -player.ladderSpeed;
			}
		}
		
		//BOTTOM tile types
		
		//if we run into a death block
		if(bottomTileType == Tile.DEATH)
		{
			player.setDeath(true);
		}
		
		//if we are on a one-way left block
		if(bottomTileType == Tile.ONEWAY_LEFT)
		{
			//remember that we were sliding
			wasSlide = true;
			
			//remember that we are going left
			lastKnownKey = 0;
			
			//set my dx (projected movement on the x axis)
			player.dx = -player.oneWaySpeed;
		}
		//if we are on a one-way right block
		if(bottomTileType == Tile.ONEWAY_RIGHT)
		{
			//remember that we were sliding
			wasSlide = true;
			
			//remember that we are going right
			lastKnownKey =1;
			
			//set my dx (projected movement on the x axis)
			player.dx = player.oneWaySpeed;
		}
		
		//if we are on a one-way left block
		if(bottomTileType == Tile.ONEWAY_LEFT)
		{
			//remember that we were sliding
			wasSlide = true;
			
			//remember that we are going left
			lastKnownKey = 0;
			
			//set my dx (projected movement on the x axis)
			player.dx = -player.oneWaySpeed;
		}
		
		//if we are on an icy block
		if(bottomTileType == Tile.ICY)
		{
			//rememer that we were sliding
			wasSlide = true;

			//capture what direction the player was going and remember it
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
				//set up a timer for determining the sliding distances
				if(player.startSlide == true)
					slidingTime = System.nanoTime();
				
				//only set the timer the first time
				player.startSlide = false;
					
				//determine if we are speeding up or slowing down
				if(player.canSlide == true)
					player.dx += player.slideSpeed;
				else
					player.dx -= player.slideSpeed;
				
				//cap our speed
				if(player.canSlide == true && player.dx > player.maxSlideSpeed)
					player.dx = player.maxSlideSpeed;
					
				//determine how long we have been sliding
				long elapsed = (System.nanoTime() - slidingTime) / 1000000;
				
				//switch our direction from speeding up to slowing down
				if(player.dx > player.maxSlideSpeed - 0.1 && elapsed > SLIDE_DISTANCE)
					player.canSlide = false;
				
			}
		}
		
		//if we were sliding, and we are not on an icy block
		else if(wasSlide == true)
		{
			//if we were going left
			if(lastKnownKey == 0)
			{
				//move to the left two steps (so we fall off the block)
				player.dx -= player.slideSpeed+2;
				
				//remember that we were not sliding anymore
				wasSlide = false;
			}
			//if we were going right
			else if(lastKnownKey == 1)
			{
				//move to the right two steps (so we fall off the block)
				player.dx += player.slideSpeed+2;
				
				//remember that we are not sliding anymore
				wasSlide = false;
			}
		}
		
		//if we are going left
		if(player.left)
		{	
			//subtract player move speed from its dx (where it is going to be) 
			player.dx -= player.moveSpeed;
			
			//if player dx is less than the max speed, cap it off
			if(player.dx < -player.maxSpeed)
				player.dx = -player.maxSpeed;
		}
		//if we are going right
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
		
		//jumping and double jump
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
			//we we were not falling before now, capture the time
			if(wasFalling == false)
				fallingTime = System.nanoTime();
			
			//we are falling now
			wasFalling = true;
			
			//if in the air and gliding
			if(player.dy > 0 && player.gliding) 
			{
				//fall slower
				player.dy += player.fallSpeed * 0.1;
				
				//set that we are NOT falling (we are gliding, which is different)
				wasFalling = false;
			}
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
			
			

		}
		
		//falling damage
		if(wasFalling)
		{
			//if I hit the ground, and I was not jumping
			if((player.bottomLeft || player.bottomRight) && !player.jumping)
			{
				//figure out how long we were falling for
				long elapsed = ((System.nanoTime() - fallingTime) / 1000000)/200;
				
				//if we were falling for longer than specified duration, take damage
				if(elapsed > player.fallDistanceBeforeDamage)
					player.hit((int)elapsed - player.fallDistanceBeforeDamage);
				
				//we were no longer falling
				wasFalling = false;
			}
		}
		
		
	}
}
