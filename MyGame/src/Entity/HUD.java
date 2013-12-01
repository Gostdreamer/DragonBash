package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
/****************************************************
 * Heads up Display (graphical display of health    *
 * ammo, that sort of thing							*
 ***************************************************/
public class HUD
{
	private Player player;
	private BufferedImage image;
	private Font font;
	
	//constructor
	public HUD(Player p)
	{
		player = p;
		try{
			//get the image for the HUD
			image = ImageIO.read(getClass().getResourceAsStream("/HUD/hud.gif"));

			
			font = new Font("Arial", Font.PLAIN, 14);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	//draw the HUD
	public void draw(Graphics2D g)
	{
		//draw the image we got earlier
		g.drawImage(image, 0, 10, null);
		g.setFont(font);
		g.setColor(Color.WHITE);
		//draw out information on the HUD
		g.drawString(player.getHealth() + "/" + player.getMaxHealth(), 30,25);
		g.drawString(player.getFire() / 100 + "/" + player.getMaxFire() / 100, 30,45);
	}
}
