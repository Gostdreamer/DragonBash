package TileMap;

import java.awt.image.BufferedImage;

/****************************************************
 * Tiles that are pulled from the tileset           *
 * Stores what type of tile it is, as well as       *
 * image for level creation                         *
 ****************************************************/
public class Tile 
{
	private BufferedImage image;
	private int type;

	
	//tile types
	public static final int BLOCKEDTILES[] = {1,4,5,6,7,8};
	public static final int NORMAL = 0;
	public static final int BLOCKED = 1;
	public static final int LADDER = 2;
	public static final int BOTTOM_LADDER = 3;
	public static final int ICY = 4;
	public static final int ONEWAY_LEFT = 5;
	public static final int ONEWAY_RIGHT = 6;
	public static final int STICKY_LEFT = 7;
	public static final int STICKY_RIGHT = 8;
	public static final int DEATH = 9;
	
	
	public Tile(BufferedImage image, int type)
	{
		this.image = image;
		this.type = type;
	}
	
	public BufferedImage getImage()
	{
		return image;
	}
	
	public int getType()
	{
		return type;
	}
	
	public static int getBlockedNumber()
	{
		return BLOCKEDTILES.length;
	}

}

