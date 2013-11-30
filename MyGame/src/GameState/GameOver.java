package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import TileMap.Background;

public class GameOver implements GameState 
{
	public GameStateManager gsm;
	//the actual menu (which is a state) - States aka Screens!
	private Background bg;
	
	private int currentChoice = 0;
	private String[] options ={"Restart", "Quit"};
	
	private Color titleColor;
	private Font titleFont;
	private Font font;
	
	public GameOver(GameStateManager gsm)
	{
		this.gsm = gsm;
		
		try{
			bg = new Background("/Backgrounds/menubg.gif",1);
			
			//Make the Background scroll left .1 pixel
			bg.setVector(-0.1,0);
			
			titleColor = new Color (128,0,0);
			titleFont = new Font("Centry Gothic", Font.PLAIN,28);
			
			font = new Font("Arial", Font.PLAIN, 12);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void init() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() 
	{
		bg.update();
		
	}

	@Override
	public void draw(Graphics2D g) 
	{
		bg.draw(g);
		
		//draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("GAME OVER",80,70);
		
		//draw menu options
		g.setFont(font);
		for(int i = 0; i < options.length; i++)
		{
			if(i == currentChoice)
			{
				g.setColor(Color.black);
			}
			else
			{
				g.setColor(Color.red);
			}
			
			g.drawString(options[i], 145, 140+ i*15);
		}
		
	}
	
	//What happens when you select an option
	private void select()
	{
		if(currentChoice == 0)
		{
			//start
			gsm.setState(GameStateManager.LEVEL1STATE);
		}
		if(currentChoice == 1)
		{
			System.exit(0);
		}
	}

	@Override
	public void keyPressed(int k) 
	{
		if(k == KeyEvent.VK_ENTER)
		{
			select();
		}
		if(k == KeyEvent.VK_UP)
		{
			currentChoice --;
			//If you are at the top, put you at the bottom
			if(currentChoice == -1)
				currentChoice = options.length-1;
		}
		if(k == KeyEvent.VK_DOWN)
		{
			currentChoice ++;
			//If you are at the bottom, put you at the top
			if(currentChoice == options.length)
				currentChoice = 0;
		}
		
	}

	@Override
	public void keyReleased(int k) 
	{
		// TODO Auto-generated method stub
		
	}

}
