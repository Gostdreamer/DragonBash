package TileMap;

import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.ImageIO;

import Main.GamePanel;

public class TileMap 
{
	//position
	private double x,y;
	
	//bounds
	private int xmin, ymin, xmax, ymax;
	
	//smooth scrolling
	//private double tween;
	
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
	
	public TileMap(int tileSize)
	{
		this.tileSize = tileSize;
		numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
		numColsToDraw = GamePanel.WIDTH / tileSize + 2;
		//tween = 0.07;
		
	}
	
	//reads what each tile type is (blocked, normal, icy, one-way, ect)
	public void loadMapType(String s)
	{
		try{
			InputStream in = getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			numCols2 = Integer.parseInt(br.readLine()); //numTilesAccross
			numRows2 = Integer.parseInt(br.readLine()); //numTilesDown
			
			mapType = new int[numRows2][numCols2];
			
			String delims = "\\s+";
			
			for(int r = 0; r < numRows2; r++)
			{
				String line = br.readLine();
				String[] tokens = line.split(delims);
				
				for(int c = 0; c < numCols2; c++)
				{
					mapType[r][c] = Integer.parseInt(tokens[c]);
				}
			}
			
			for(int i = 0; i < mapType.length; i++)
			{
				for(int j = 0; j < mapType[0].length; j++)
				{
					System.out.print(mapType[i][j] + " ");
				}
				System.out.println("");
			}
			
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//loads tiels into memory
	public void loadTiles(String s)
	{
		try{
			
			tileset = ImageIO.read(getClass().getResourceAsStream(s));
			
			numTilesAcross = tileset.getWidth() / tileSize;
			numTilesDown = tileset.getHeight() / tileSize;
							
			tiles = new Tile[numTilesDown][numTilesAcross];
			
			BufferedImage subimage;
			for(int row = 0; row < numTilesDown; row++)
			{
				for(int col = 0; col < numTilesAcross; col++)
				{
					subimage = tileset.getSubimage(col * tileSize, row*tileSize, tileSize, tileSize);
					tiles[row][col] = new Tile(subimage, mapType[row][col]);	
					System.out.print(tiles[row][col].getType() + " " );
				}	
			}
		
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void drawTiles(Graphics2D g)
	{

	}
	
	
	public void loadMap(String s)
	{
		try{
			InputStream in = getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());

			map = new int[numRows][numCols];
			
			width = numCols * tileSize;
			height = numRows * tileSize;
			
			xmin = GamePanel.WIDTH - width;
			xmax = 0;
			ymin = GamePanel.HEIGHT - height;
			ymax = 0;
			
			String delims = "\\s+";
			
			for (int row = 0; row < numRows;row++)
			{
				String line = br.readLine();
				String[] tokens = line.split(delims);
				
				for (int col = 0; col < numCols; col++)
				{
					map[row][col] = Integer.parseInt(tokens[col]);
				}
			}
			//System.out.println("OUT!");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
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
				
				//System.out.println("R C " + r + " " + c);
				g.drawImage(tiles[r][c].getImage(), (int) x + col * tileSize, (int) y + row * tileSize, null);
				g.draw3DRect((int) x + col * tileSize, (int) y + row * tileSize, 30, 30, false);
				g.drawString(Integer.toString(tiles[r][c].getType()),(int) x + col * tileSize +10, (int) y + row * tileSize+10);
				//g.drawString(r + "." + c,(int) x + col * tileSize +0, (int) y + row * tileSize+20);
				//tiles only hold the tile type! we need to test tile map stuff
			}
		}
	}
	
	public int getMap(int r, int c)
	{
		return map[r][c];
	}
}
