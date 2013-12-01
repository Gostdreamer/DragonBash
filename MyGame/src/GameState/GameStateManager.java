package GameState;

//import java.util.ArrayList;

/****************************************************
 * Controls what state of the game we are in        *
 * AKA what Screen we are on                        *
 ***************************************************/
public class GameStateManager 
{
	//create an array list of options (gameStates)
	//private ArrayList<GameState> gameStates;
	private GameState[] gameStates;
	
	//sets up to track the current state
	private int currentState;
	private int previousState;
	
	//uses constants to navigate
	public static final int NUMSTATE = 3;
	public static final int MENUSTATE = 0;
	public static final int LEVEL1STATE = 1;
	public static final int GAMEOVER = 2;
	
	
	//constructor
	public GameStateManager()
	{
		//Initialize the current game state (when u start the game, u are at the menu)
		//gameStates = new ArrayList<GameState>();
		gameStates = new GameState[NUMSTATE];
		setState(MENUSTATE);
		
		//set the current state to menu
		//currentState = MENUSTATE;
		
		//add a menu to the gameStates
		//gameStates.add(new MenuState(this));
		//gameStates.add(new Level1State(this));
		//gameStates.add(new GameOver(this));
		
	}
	
	public void setState(int i)
	{
		previousState = currentState;
		unloadState(previousState);
		currentState = i;
		if(i == MENUSTATE) {
			gameStates[i] = new MenuState(this);
			gameStates[i].init();
		}
		else if(i == LEVEL1STATE) {
			gameStates[i] = new Level1State(this);
			gameStates[i].init();
		}
		else if(i == GAMEOVER) {
			gameStates[i] = new GameOver(this);
			gameStates[i].init();
		}
		//currentState = state;
		//gameStates.get(currentState).init();
	}
	
	public void unloadState(int i) 
	{
		gameStates[i] = null;
	}
	
	//STEP
	public void update()
	{
		//gameStates.get(currentState).update();
		if(gameStates[currentState] != null)
		{
			gameStates[currentState].update();
		}
	}
	
	public void draw(java.awt.Graphics2D g)
	{
		//gameStates.get(currentState).draw(g);
		if(gameStates[currentState] != null) 
		{
			gameStates[currentState].draw(g);
		}
	}
	
	public void keyPressed(int k)
	{
		//gameStates.get(currentState).keyPressed(k);
		if(gameStates[currentState] != null) 
		{
			gameStates[currentState].keyPressed(k);
		}
	}
	
	public void keyReleased(int k)
	{
		//gameStates.get(currentState).keyReleased(k);
		if(gameStates[currentState] != null) 
		{
			gameStates[currentState].keyReleased(k);
		}
	}
}
