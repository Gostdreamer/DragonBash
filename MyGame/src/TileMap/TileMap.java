package TileMap;

import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.ImageIO;

import Main.GamePanel;

/****************************************************
 * Holds the entire spritesheet for level creation  *
 * As well as the map layout for level creation     *
 ****************************************************/
public class TileMap 
{
	//position
	private double x,y;
	
	//bounds
	private int xmin, ymin, xmax, ymax;
	
	//map
	private int[][] map;
	private int[][] mapType;
	private int tileSize;
	private int numRows, numCols, width, height, numCols2, numRows2;
	
	//tileset
	private BufferedImage tileset;
	private int numTilesAcross;
	private int numTilesDown;
	public Tile [][] tiles;
	
	//drawing the tiles that are on the screen
	//which row to start drawing
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;
	
	//initialize basic information
	public TileMap(int tileSize)
	{
		this.tileSize = tileSize;
		numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
		numColsToDraw = GamePanel.WIDTH / tileSize + 2;
		
	}
	
	//reads what each tile type is (blocked, normal, icy, one-way, ect)
	public void loadMapType(String s)
	{
		try{
			//Get the file directory
			InputStream in = getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			//determine how many rows and columns are in this sheet
			numCols2 = Integer.parseInt(br.readLine()); 
			numRows2 = Integer.parseInt(br.readLine());
			
			//initialize mapType to an empty 2D array with our new variables
			mapType = new int[numRows2][numCols2];
			
			String delims = "\\s+";
			
			//Run through each number in the file, assigning it to the 2D array for later use
			for(int r = 0; r < numRows2; r++)
			{
				String line = br.readLine();
				String[] tokens = line.split(delims);
				
				for(int c = 0; c < numCols2; c++)
				{
					mapType[r][c] = Integer.parseInt(tokens[c]);
				}
			}			
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//loads tiles into memory
	public void loadTiles(String s)
	{
		try{
			//Get the file
			tileset = ImageIO.read(getClass().getResourceAsStream(s));
			
			//Determine how many tiles there are
			numTilesAcross = tileset.getWidth() / tileSize;
			numTilesDown = tileset.getHeight() / tileSize;
			
			//Initialize 2D Tile array with our new found dimensions
			tiles = new Tile[numTilesDown][numTilesAcross];
			
			//Set up variable for holding the images
			BufferedImage subimage;
			
			//Go through the spritesheet pulling out tiles and storing them in the array
			for(int row = 0; row < numTilesDown; row++)
			{
				for(int col = 0; col < numTilesAcross; col++)
				{
					subimage = tileset.getSubimage(col * tileSize, row*tileSize, tileSize, tileSize);
					tiles[row][col] = new Tile(subimage, mapType[row][col]);	
				}	
			}
		
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void drawTiles(Graphics2D g)
	{
		//does nothing
	}
	
	//Loads in the map layout (also known as the level layout)
	public void loadMap(String s)
	{
		try{
			//Get the file
			InputStream in = getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			//determine how many rows/columns are in the file
			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());
			
			//initialize the map 2D int array with out new found dimensions
			map = new int[numRows][numCols];
			
			//set up how large this entire map is
			width = numCols * tileSize;
			height = numRows * tileSize;
			
			//get our boundries
			xmin = GamePanel.WIDTH - width;
			xmax = 0;
			ymin = GamePanel.HEIGHT - height;
			ymax = 0;
			
			String delims = "\\s+";
			
			//cycle through each number in the file and place them in the 2d array
			for (int row = 0; row < numRows;row++)
			{
				String line = br.readLine();
				String[] tokens = line.split(delims);
				
				for (int col = 0; col < numCols; col++)
				{
					map[row][col] = Integer.parseInt(tokens[col]);
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//Makes it so that it will not pan off screen
	private void fixBounds()
	{
		if(x < xmin)
			x = xmin;
		if(x > xmax)
			x = xmax;
		if(y < ymin)
			y = ymin;
		if(y > ymax)
			y = ymax;
	}
	
	//draw the tile map
	public void draw(Graphics2D g)
	{
		for(int row = rowOffset; row < rowOffset + numRowsToDraw; row++)
		{
			if(row >= numRows)
				break;
			
			for(int col = colOffset; col < colOffset + numColsToDraw; col++)
			{
				if(col >= numCols)
					break;
				
				//if(map[row][col] == 0)
				//	continue;
				
				int rc = map[row][col];
				int r = rc / numTilesAcross;
				int c = rc % numTilesAcross;
				
				g.drawImage(tiles[r][c].getImage(), (int) x + col * tileSize, (int) y + row * tileSize, null);

				//Debugging Lines:
				//g.draw3DRect((int) x + col * tileSize, (int) y + row * tileSize, 30, 30, false);
				//g.drawString(Integer.toString(tiles[r][c].getType()),(int) x + col * tileSize +10, (int) y + row * tileSize+10);
				//g.drawString(r + "." + c,(int) x + col * tileSize +0, (int) y + row * tileSize+20);
			}
		}
	}
	
	/****************************************************
	 * Getters and Setters								*
	 ****************************************************/
	public int getTileSize()
	{
		return tileSize;
	}
	
	public int getX()
	{
		return (int)x;
	}
	
	public int getY()
	{
		return (int)y;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public int getType(int row, int col)
	{
		int rc = map[row][col];
		int r = rc / numTilesAcross;
		int c = rc % numTilesAcross;
		
		return tiles[r][c].getType();
	}
	
	public void setPosition(double x, double y)
	{
		this.x = x;
		this.y = y;
		
		fixBounds();
		
		colOffset = (int) -this.x / tileSize;
		rowOffset = (int) -this.y / tileSize;
	}
	
	public int getMap(int r, int c)
	{
		return map[r][c];
	}
}
