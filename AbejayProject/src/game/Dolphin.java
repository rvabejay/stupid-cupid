	/***********************************************************	*  
	* This class inherits from the class PowerUp and implements
	* the inherited method affectCupid which makes the Cupid move
	* faster for 8 seconds, from the moment it is collected.
	*
	* @author Rhys Allen Abejay
	* @created_date 2022-05-18 22:35
	***********************************************************/

package game;

import javafx.scene.image.Image;

class Dolphin extends PowerUp {	
	//Class constants
	final static int SPEED_UP_DURATION = 8;				//dolphin increases cupid's movement for 8 seconds
	
	final static Image DOLPHIN_IMAGE = new Image("images/dolphin.png", PowerUp.POWER_UP_IMAGE_WIDTH, PowerUp.POWER_UP_IMAGE_WIDTH, true, true);
	final static Image BW_DOLPHIN = new Image("images/bw-dolphin.png", PowerUp.POWER_UP_IMAGE_WIDTH, PowerUp.POWER_UP_IMAGE_WIDTH, true, true);
	
	Dolphin(int x, int y){
		super(x,y, Dolphin.DOLPHIN_IMAGE);
	}

	@Override
	void affectCupid(Cupid cupid, GameTimer gametimer) {		
		System.out.println("CUPID has found a DOLPHIN!");
		gametimer.setHasDolphin(true);							//sets the hasDolphin attribute to true
		gametimer.setDolphinTime(System.nanoTime());			//sets the time when dolphin is collected
		gametimer.setCollectedDolphins();						//increments the game's collected dolphins
		cupid.setFast(true);									//sets upSpeed to true
		this.setVisible(false);									//since dolphin has been collected, dolphin should not be visible anymore
		
	}

}
