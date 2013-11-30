package GameState;

import java.util.ArrayList;

public class GameStateManager 
{
	//create an array list of options (gameStates)
	private ArrayList<GameState> gameStates;
	
	//sets up to track the current state
	private int currentState;
	
	//uses constants to navigate
	public static final int MENUSTATE = 0;
	public static final int LEVEL1STATE = 1;
	
	
	//constructor
	public GameStateManager()
	{
		//initalize the current game state (when u start the game, u are at the menu)
		gameStates = new ArrayList<GameState>();
		
		//set the current state to menu
		currentState = MENUSTATE;
		
		//add a menu to the gameStates
		gameStates.add(new MenuState(this));
		gameStates.add(new Level1State(this));
		
		
	}
	
	public void setState(int state)
	{
		currentState = state;
		gameStates.get(currentState).init();
	}
	
	//STEP
	public void update()
	{
		gameStates.get(currentState).update();
	}
	
	public void draw(java.awt.Graphics2D g)
	{
		gameStates.get(currentState).draw(g);
	}
	
	public void keyPressed(int k)
	{
		gameStates.get(currentState).keyPressed(k);
	}
	
	public void keyReleased(int k)
	{
		gameStates.get(currentState).keyReleased(k);
	}
}
