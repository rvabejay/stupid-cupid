	/***********************************************************	* 
	* This class defines the attributes and methods of the
	* Arrow that Cupid shoots in the game.
	* 
	* The class also inherits from the Sprite class.
	* 
	* Defined in this class is a damage attribute, which determines
	* its effect on the Humans it collides.
	* 
	* The method move in this class changes the x position of the 
	* bullet when called.
	*
	* @author Rhys Allen Abejay
	* @created_date 2022-05-17 14:54
	***********************************************************/

package game;

import javafx.scene.image.Image;

class Arrow extends Sprite {
	private int damage;											//damage that arrow can inflict on colliding human
	
	//Class constants	
	private final static int ARROW_SPEED = 20;					//speed of the arrow
	private final static int ARROW_WIDTH = 50;					//width of the arrow image
	
	final static Image ARROW_IMAGE = new Image("images/arrow.png",Arrow.ARROW_WIDTH,Arrow.ARROW_WIDTH,true,true);
	
	
	Arrow(int x, int y, int damage){
		super(x,y);
		this.loadImage(Arrow.ARROW_IMAGE);
		this.damage = damage;
	}

	
	//method that will move/change the x position of the bullet 
	void move(){		
		this.x += Arrow.ARROW_SPEED;			//increment the x position by BULLET_SPEED
		
		//Check if the x position (including the width of the image) has reached the right boundary of the window
		boolean isBoundary = (this.x + Arrow.ARROW_WIDTH) >= GameStage.WINDOW_WIDTH;
		
		if(isBoundary) {
			this.setVisible(false);				//set visibility to false
		}
	}
	
	//getter
	int getDamage() {
		return this.damage;
	}
	
	//setter
	void setDamage(int num) {					//this method is called when the Arrow has hit a human, and sets the damage of the Arrow to 0
		this.damage = num;
	}
	
}