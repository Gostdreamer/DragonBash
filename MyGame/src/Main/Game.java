package Main;
import javax.swing.JFrame;

/****************************************************
 * Main Window that the game resides on             *
 ****************************************************/
public class Game 
{
	public static final boolean JARFILE = false;
	public static void main(String[] args)
	{
		
		//create the game window
		JFrame window = new JFrame("Dragon Tale");
		
		//add a GamePanel to the window
		window.setContentPane(new GamePanel());
		
		//set all the default window stuff
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
	}
}
