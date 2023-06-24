	/***********************************************************	* 
	* This class serves as the superclass for all kinds of 
	* Power-ups that Cupid can collect in the game.
	* 
	* Defined in this class is an abstract method affectCupid
	* which changes the stats of Cupid and the game when the 
	* power-up is collected.
	*
	* @author Rhys Allen Abejay
	* @created_date 2022-05-18 18:58
	***********************************************************/

package game;

import javafx.scene.image.Image;

abstract class PowerUp extends Sprite {	
	
	//Class constants
	final static int SPAWN_TIME = 10;						//power-ups are spawn every 10 seconds
	final static int IDLE_TIME = 5;							//power-ups can stay uncollected for 5 seconds
	final static int POWER_UP_IMAGE_WIDTH = 45;				//fixed image size for power-ups
																	
	
	PowerUp(int x, int y, Image image){
		super(x,y);
		this.loadImage(image);
	}
	
	abstract void affectCupid(Cupid cupid, GameTimer gametimer);	//method to change the stats of the cupid and the game (once collected)
	
}
