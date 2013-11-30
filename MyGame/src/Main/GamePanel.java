package Main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import GameState.GameStateManager;

@SuppressWarnings("serial")
/****************************************************
 * Game Screen                                      *
 ****************************************************/
public class GamePanel extends JPanel implements Runnable, KeyListener
{
	//dimensions of the screen
	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 2;
	
	//game thread set up
	private Thread thread;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;
	
	//setting up the background image
	private BufferedImage image;
	private Graphics2D g;
	
	//game state manager
	private GameStateManager gsm;
	
	//constructor - setting up the size of the window
	public GamePanel()
	{
		super();
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT*SCALE));
		setFocusable(true);
		requestFocus();
		
	}
	
	@Override
	public void addNotify()
	{
		super.addNotify();
		if(thread == null)
		{
			thread = new Thread(this);
			addKeyListener(this);
			thread.start(); //runs public void run
			
		}
	}
	
	//initiates the game - called by run (called by thread)
	public void init()
	{
		
		//sets the image
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		//sets the graphics
		g = (Graphics2D) image.getGraphics();
		running = true;
		
		//creates the game state manager
		gsm = new GameStateManager();
	}
	
	//is called by the thread
	@Override
	public void run()
	{
		//initiates the game
		init();
		
		
		//sets up for thread management
		long start;
		long elapsed;
		long wait;
		
		//game loop
		while(running)
		{
			start = System.nanoTime();
			
			update();
			draw();
			drawToScreen();
			
			
			//managing the thread
			elapsed = System.nanoTime() - start;
			wait = targetTime - elapsed / 1000000L;
			
			if(wait < 0)
				wait = 5;
			
			try{
				Thread.sleep(wait);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	//update the game (STEPS)
	private void update()
	{
		gsm.update();
	}
	
	//sends it to buffer
	private void draw()
	{
		gsm.draw(g);
	}
	
	//draws it on the screen
	private void drawToScreen()
	{
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0,0,WIDTH *SCALE, HEIGHT * SCALE,null);
		g2.dispose();
	}
	
	//handles all the key presses
	@Override
	public void keyTyped(KeyEvent key)
	{
		
	}
	
	@Override
	public void keyPressed(KeyEvent key)
	{
		gsm.keyPressed(key.getKeyCode());
	}
	
	@Override
	public void keyReleased(KeyEvent key)
	{
		gsm.keyReleased(key.getKeyCode());
	}
}
