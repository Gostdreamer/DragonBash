package TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Main.GamePanel;

/****************************************************
 * Background Images, able to layer and scroll them *
 * to create parallax backgrounds                   *
 ****************************************************/
public class Background 
{
	private BufferedImage image;
	
	private int width, height;
	
	private double x, y, dx, dy;
	
	private double moveScale;
	
	public Background(String s, double ms)
	{
		//set the image to the resource
		//set what scale to set the image
		try{
			image = ImageIO.read(getClass().getResourceAsStream(s));
			
			width = image.getWidth();
			height = image.getHeight();
			
			moveScale = ms;
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//set the position of the background, using the moveScale to stretch it
	public void setPosition(double x, double y)
	{
		this.x = (x * moveScale);
		this.y = (y * moveScale);
		fixPosition();
	}
	
	//if you want the image to continuously scroll, set the Vector
	public void setVector(double dx, double dy)
	{
		this.dx = dx;
		this.dy = dy;
	}
	
	//the Background "STEP"
	public void update()
	{
		x += dx;
		y += dy;
		fixPosition();
	}
	
	public void fixPosition()
	{
		while(x <= -width) x += width;
        while(x >= width) x -= width;
        while(y <= -height) y += height;
        while(y >= height) y -= height;
	}
	
	//how to draw the background
	public void draw(Graphics2D g)
	{
		//draw the image
		g.drawImage(image, (int)x, (int)y, null);
        
        if(x < 0)
        	g.drawImage(image,(int)x + GamePanel.WIDTH,(int)y,null);
        if(x > 0)
        	g.drawImage(image,(int)x - GamePanel.WIDTH,(int)y,null);
	}
}
