import acm.program.*;
import acm.graphics.*;
import acm.util.*;

import java.awt.Color;
import java.awt.event.*;
import java.util.ArrayList;

public class SpaceInvaders1 extends GraphicsProgram {

	//constants from break out prepared for bounce away reaction

	private GLabel score;
	private int scoreCount = 0;
	private int DELAY = 100;
	

	private ArrayList<GOval> shotsFiredList;

	private ArrayList<GImage> invaderList;
	private GImage paddle ;
	public static final int APPLICATION_HEIGHT = 600;
	private static final double BALL_SIZE = 5;
	private static final int PADDLE_WIDTH = 100;
	private static final int PADDLE_HEIGHT = 80;
	private static final int INV_WIDTH = 80;
	private static final int INV_HEIGHT = 60;
	RandomGenerator rgen = new RandomGenerator();

	/**
	 * Number of invaders in each row
	 * 7--> 5
	 * 8--> 7
	 */
	public static final int NUM_INV_PER_ROW = 7;

	/** 
	 * Number of rows of invaders
	 * Number of invader rows
	 * in reality it shows this number + 1 invader rows
	 */
	public static final int NUM_INV_ROWS = 2;	

	/**
	 * Separation between neighboring invaders, in pixels
	 */
	public static final int INV_SEP = 6;
	private GImage background = new GImage("res/Background.gif");

	public void run() {
		
		background.setSize(getWidth(), getHeight());
		
		add(background);


		shotsFiredList = new ArrayList<GOval>();
		createPaddle();
		addMouseListeners();
		createLineOfInvaders();
		while(true) {
			animateShots();
			pause(DELAY);
		}
	}

	/*
	 * reacts when mouse moves and shifts the paddle aka our ship along with it
	 */
	public void mouseMoved(MouseEvent e){
		double x = e.getX() - paddle.getWidth()/2 + BALL_SIZE/2;
		paddle.setLocation(x, getHeight() - paddle.getHeight());
	}

	/*
	 * reacts when mouse pressed and fires a shot
	 */
	public void mousePressed(MouseEvent e) {
		double x = e.getX();
		double y = APPLICATION_HEIGHT - paddle.getHeight() -25;
		GOval s = new GOval(x, y, BALL_SIZE, BALL_SIZE);
		s.setFilled(true);
		s.setColor(Color.ORANGE);

		// add the shot to the screen
		add(s);

		// add the shot to the list
		shotsFiredList.add(s);
	}

	/*
	 * makes the shots keep moving after being fired
	 */
	private void animateShots() {
		// loop over list backwards so that we can safely remove
		// from the list.
		for(int i = shotsFiredList.size() - 1; i >= 0; i--) {
			GOval shot = shotsFiredList.get(i);

			// move the rocket
			shot.move(0, -5);
			//check if the shot it something
			checkCollision(shot);
			// remove the rocket?
			if(shot.getY() < 0) {
				remove(shotsFiredList.get(i));
				shotsFiredList.remove(i);
			}
		}
	}

	/*
	 * when called creates a paddle
	 */
	private void createPaddle() {
		paddle = new GImage("res/SpaceShip.png");
		paddle.setSize(PADDLE_WIDTH, PADDLE_HEIGHT);
		add(paddle, getWidth()/2 - paddle.getWidth()/2, getHeight() - paddle.getHeight());
	}


	/*
	 * creates one single invader
	 * needs more information for which style it should make said invader and where to put it
	 */
	private void createInvader(double x, double y, String numberOfInva) {
		GImage inva = new GImage("invader-" + numberOfInva + ".png");
		inva.setSize(INV_WIDTH, INV_HEIGHT);
		add(inva, x, y);
		//invaderList.add(inva);
		
	}

	/*
	 * creates a table of invaders using the createInvared method
	 */
	private void createLineOfInvaders() {
		double paddingOnSides = getWidth() - (INV_WIDTH*NUM_INV_PER_ROW + INV_SEP*NUM_INV_PER_ROW);
		for (double i = 0; i <= (NUM_INV_ROWS*(INV_HEIGHT + INV_SEP)); i = i + INV_HEIGHT + INV_SEP) {		//y
			String str = " ";
			if(rgen.nextInt(0, 2) == 0) {
				str = "0";
			}else if(rgen.nextInt(0, 2) == 1) {
				str = "1";
			}else if(rgen.nextInt(0, 2) == 2) {
				str = "2";
			}
			for (double j = paddingOnSides; j < getWidth() - paddingOnSides - INV_WIDTH; j = j + INV_WIDTH + INV_SEP) {	//x
				createInvader(j, i, str);
			}
		}
	}


	/*
	 * should react when collision occurs
	 * RIGHT NOW WHEN ACTIVE GAME STOPS OR SLOWS DOWN SO MUCH IT LOOK LIKE IT STOPPED
	 */
	private void checkCollision(GOval shot) {
		if(getElementAt(	shot.getX(), shot.getY()	) != null || getElementAt(	shot.getX(), shot.getY()+ BALL_SIZE	) != null || getElementAt(	shot.getX()+ BALL_SIZE, shot.getY()+ BALL_SIZE	) != null  || getElementAt(	shot.getX()+ BALL_SIZE, shot.getY()	) != null ) {
			if(getElementAt(	shot.getX(), shot.getY()	) == paddle || getElementAt(	shot.getX(), shot.getY()+ BALL_SIZE	) == paddle || getElementAt(	shot.getX()+ BALL_SIZE, shot.getY()+ BALL_SIZE	) == paddle  || getElementAt(	shot.getX()+ BALL_SIZE, shot.getY()	) == paddle ) {
				//numOfLives--;
				//i will have to check the collision when the invaders start shooting and if they hit i need to lower the lives
			
			}else if(getElementAt(	shot.getX(), shot.getY()	) == background || getElementAt(	shot.getX(), shot.getY()+ BALL_SIZE	) == background || getElementAt(	shot.getX()+ BALL_SIZE, shot.getY()+ BALL_SIZE	) == background  || getElementAt(	shot.getX()+ BALL_SIZE, shot.getY()	) == background ) {
			
			
			
			} else {
				//if the top left corner hits something
				if(getElementAt(	shot.getX(), shot.getY()	) != null) {
					remove(getElementAt(	shot.getX(), shot.getY()	) );
					shotsFiredList.remove(shot);
					
				//if the lower left corner hits something
				}else if(getElementAt(	shot.getX(), shot.getY()+ BALL_SIZE	) != null ) {
					remove(getElementAt(	shot.getX(), shot.getY()+ BALL_SIZE	) );
					shotsFiredList.remove(shot);
					
					
				//if the top right corner hits something
				}else if(getElementAt(	shot.getX()+ BALL_SIZE, shot.getY()	) != null ) {
					remove(getElementAt(	shot.getX()+ BALL_SIZE, shot.getY()	));
					shotsFiredList.remove(shot);
					
					
				//if the lower right corner hits something
				}else if(getElementAt(	shot.getX()+ BALL_SIZE, shot.getY()	+ BALL_SIZE ) != null ) {
					remove(getElementAt(	shot.getX()+ BALL_SIZE, shot.getY()	+ BALL_SIZE ) );
					shotsFiredList.remove(shot);
					
				//if the top side of the square hits something
				}else if(getElementAt(	shot.getX(), shot.getY()	) != null || getElementAt(shot.getX()+ BALL_SIZE, shot.getY()	) != null) {				 
					remove(getElementAt(	shot.getX(), shot.getY()));
					//SOULD REMOVE OBJECT AT BOTH COORDINATES OR IT COULD BE  MAYBE FAULTY
					remove(getElementAt(shot.getX()+ BALL_SIZE, shot.getY()	));
					shotsFiredList.remove(shot);
					
					
					
				//if the left side of the square hits something
				}else if(getElementAt(	shot.getX(), shot.getY()+ BALL_SIZE	) != null ||  getElementAt(	shot.getX(), shot.getY()	) != null) {
					remove(getElementAt(	shot.getX(), shot.getY()+ BALL_SIZE	));
					remove(getElementAt(	shot.getX(), shot.getY()	) );
					shotsFiredList.remove(shot);
					
				//if the right side of the square hits something
				}else if(getElementAt(	shot.getX()+ BALL_SIZE, shot.getY()	+ BALL_SIZE ) != null || getElementAt(shot.getX()+ BALL_SIZE, shot.getY()	) != null) {
					remove(getElementAt(	shot.getX()+ BALL_SIZE, shot.getY()	+ BALL_SIZE ) );
					remove(getElementAt(shot.getX()+ BALL_SIZE, shot.getY()	));
					shotsFiredList.remove(shot);
					
					
					
					
				//if the under side of the square hits something
				}else if(getElementAt(	shot.getX()+ BALL_SIZE, shot.getY()	+ BALL_SIZE ) != null || getElementAt(	shot.getX(), shot.getY()+ BALL_SIZE	) != null) {
					remove(getElementAt(	shot.getX()+ BALL_SIZE, shot.getY()	+ BALL_SIZE ) );
					remove(getElementAt(	shot.getX(), shot.getY()+ BALL_SIZE	));
					shotsFiredList.remove(shot);
				
				}
				
				
				remove(shot);
				scoreCount++;
			}
			
		}


	}

}
