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
	private static int rightTileType;
	private static int leftTileType;
	//private static int topTileType;
	private static int lastKnownKey;
	
	//remember if we were sliding, and for how long
	private static boolean wasSlide;
	private static long slidingTime;
	
	//remember if we were falling, and for how long
	private static boolean wasFalling;
	private static long fallingTime;
	
	private static boolean wasHolding;
	private static long holdingTime;
	
	private static boolean holdingOnCooldown;
	private static long holdingTimeReset;
	
	//sprinting
	private static long firstLeftTimer;
	private static long firstRightTimer;
	private static long sprintingTimer;
	
	//how far can we slide?
	private static final int SLIDE_DISTANCE = 1000;
	private static final int HOLDING_TIME = 8;
	private static final int HOLDING_COOLDOWN = 4;
	private static final int SPRINTING_TIME = 8;
	
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
	public static void handleRightTile(Player player)
	{
		rightTileType = getTileType(player, player.currRow, player.currCol+1);
		if(rightTileType == Tile.STICKY_RIGHT)
		{
			if(wasHolding == false)
				holdingTime = System.nanoTime();
			
			wasHolding = true;
			
			long elapsed = ((System.nanoTime() - holdingTime) / 1000000)/200;
			
			if(elapsed < HOLDING_TIME)
			{
				if(player.up && player.right)
				{
					player.falling = false;
					wasFalling = false;
					player.dy = -player.ladderSpeed;
				}				
				else if(player.right)
				{
					player.falling = false;
					wasFalling = false;
					player.dy = 0;
				}
			}
		}

	}
	
	public static void handleLeftTile(Player player)
	{
		leftTileType = getTileType(player, player.currRow, player.currCol-1);
		if(leftTileType == Tile.STICKY_LEFT)
		{
			if(wasHolding == false)
				holdingTime = System.nanoTime();
			
			wasHolding = true;
			
			long elapsed = ((System.nanoTime() - holdingTime) / 1000000)/200;
			
			if(elapsed < HOLDING_TIME)
			{
				if(player.up && player.left)
				{
					player.falling = false;
					wasFalling = false;
					player.dy = -player.ladderSpeed;
				}				
				else if(player.left)
				{
					player.falling = false;
					wasFalling = false;
					player.dy = 0;
				}
			}
		}
		
		if(wasHolding)
		{
			if(holdingOnCooldown == false)
				holdingTimeReset = System.nanoTime();
			
			holdingOnCooldown = true;
			
			long elapsed = ((System.nanoTime() - holdingTimeReset) / 1000000)/200;
			//System.out.println(elapsed);
			if(elapsed > HOLDING_COOLDOWN + HOLDING_TIME)
			{
				wasHolding = false;
				holdingOnCooldown = false;
			}
		}
	}
	
	public static void handleBottomTile(Player player)
	{
		bottomTileType = getTileType(player, player.currRow+1, player.currCol);
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
		
		//if we are on an icy block
		if(bottomTileType == Tile.ICY)
		{
			//remember that we were sliding
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
	}
	
	public static void handleCurrentTile(Player player)
	{
		currentTileType = getTileType(player, player.currRow, player.currCol);
		if(currentTileType == Tile.LADDER)
		{
			//if we are pressing up arrow
			if(player.up)
			{
				//we are not going to fall, and we were not falling
				player.falling = false;
				wasFalling = false;
				
				//set my dy (projected movement on the y axis)
				//player.dy -= player.ladderSpeed
				player.dy = -player.ladderSpeed;
				//if(player.dy < -player.ladderSpeed)
				//	player.dy = -player.ladderSpeed;
			}
			//if we are pressing down arrow
			else if(player.down)
			{
				//we are not going to fall, and we were not falling
				player.falling = false;
				wasFalling = false;
				
				//set my dy (projected movement on the y axis)
				//player.dy += player.ladderSpeed
				player.dy = player.ladderSpeed;
				//if(player.dy > player.ladderSpeed)
				//	player.dy = player.ladderSpeed;
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
		if(currentTileType == Tile.DEATH)
		{
			player.setDeath(true);
		}
	

	}
	
	//determine where the player is going to be 
	public static void getNextPosition(Player player)
	{
		//figure out what tiles are relative to the player
		handleRightTile(player);
		handleLeftTile(player);
		handleBottomTile(player);
		handleCurrentTile(player);
		
		//if we are going left
		if(player.left)
		{	
			long sprint = ((System.nanoTime() - sprintingTimer) / 1000000)/200;
			if(player.lastKey == Player.KEY_LEFT)
			{
				if(player.sprinting && sprint < SPRINTING_TIME)
				{
					player.dx -= player.sprintSpeed;
					
					if(player.dx < -player.maxSprintSpeed)
						player.dx = -player.maxSprintSpeed;
				}
				else
				{
					if(player.capturedLeftPress)
					{
						long elapsed = (System.nanoTime() - firstLeftTimer) / 1000000;

						if(elapsed < 1000)
						{
							player.sprinting = true;
							sprintingTimer = System.nanoTime();
						}
						else
						{
							player.sprinting = false;
							player.capturedLeftPress = false;
							//subtract player move speed from its dx (where it is going to be) 
							player.dx -= player.moveSpeed;
							
							//if player dx is less than the max speed, cap it off
							if(player.dx < -player.maxSpeed)
								player.dx = -player.maxSpeed;
						}
					}
					else
					{
						player.lastKey = -1;
					}
				}
				
			}
			else
			{
				//subtract player move speed from its dx (where it is going to be) 
				player.dx -= player.moveSpeed;
				
				//if player dx is less than the max speed, cap it off
				if(player.dx < -player.maxSpeed)
					player.dx = -player.maxSpeed;
				
				if(!player.capturedLeftPress)
				{
					player.setCapturedLeft(true);
					firstLeftTimer = System.nanoTime();
				}
			}

		}
		//if we are going right
		else if(player.right)
		{
			long sprint = (System.nanoTime() - sprintingTimer) / 1000000/200;
			
			if(player.lastKey == Player.KEY_RIGHT)
			{
				if(player.sprinting && sprint < SPRINTING_TIME)
				{
					System.out.println("SPRINTING");
					player.dx += player.sprintSpeed;
					
					if(player.dx > player.maxSprintSpeed)
						player.dx = player.maxSprintSpeed;
				}
				else
				{
					if(player.capturedRightPress)
					{
						long elapsed = (System.nanoTime() - firstRightTimer) / 1000000;

						if(elapsed < 1000)
						{
							sprintingTimer = System.nanoTime();
							player.sprinting = true;
						}
						else
						{
							player.sprinting = false;
							player.capturedRightPress = false;
							//subtract player move speed from its dx (where it is going to be) 
							player.dx = player.moveSpeed;
							
							//if player dx is less than the max speed, cap it off
							if(player.dx > player.maxSpeed)
								player.dx = player.maxSpeed;
						}
					}
					else
					{
						player.lastKey = -1;
						sprintingTimer = 0;
					}
				}
				
			}
			else
			{
				//add player move speed to its dx (where it is going to be)
				player.dx += player.moveSpeed;
				//if player dx is greater than the max speed, cap it off
				if(player.dx > player.maxSpeed)
					player.dx = player.maxSpeed;
				
				if(!player.capturedRightPress)
				{
					player.setCapturedRight(true);
					firstRightTimer = System.nanoTime();
				}
			}
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
		if(player.jumping)
		{
			if (player.dJumpNum < 2)
			{
				if (player.dJumpNum == 0)
				{
					player.dJumpNum += 1;
					player.dy = player.jumpStart;
					player.falling = true;
				}
				else if (player.upReleasedInAir)
				{
					player.dJumpNum += 1;
					player.dy = player.jumpStart;
					player.falling = true;
				}
			}
		}
		
		//falling
		if(player.falling)
		{		
			//we we were not falling before now, capture the time
			if(wasFalling == false)
				fallingTime = System.nanoTime();
			
			//we are falling now, but only if we are not dead
			if(!player.isDead())
				wasFalling = true;
			else
				wasFalling = false;
			
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
			
			player.startSlide = true;
			player.canSlide = true;

		}
		
		//falling damage
		if(wasFalling)
		{
			if(player.wasOnMoving)
			{
				player.wasOnMoving = false;
				fallingTime = System.nanoTime();
			}
			
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
