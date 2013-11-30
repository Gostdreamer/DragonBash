package GameState;
/****************************************************
 * Interface that will force our GameState's to     *
 * follow the rules                                 *
 ***************************************************/
public interface GameState 
{
	//Creates the GameState interface, so all of our GameStates follow the rules
	public void init();
	public void update();
	public void draw(java.awt.Graphics2D g);
	public void keyPressed(int k);
	public void keyReleased(int k);
}
