	/***********************************************************	* 
	* This class inherits from the class PowerUp and implements
	* the inherited method affectCupid which makes the Cupid
	* immortal for 5 seconds, from the moment it is collected.
	*
	* @author Rhys Allen Abejay
	* @created_date 2022-05-18 19:18
	***********************************************************/

package game;

import javafx.scene.image.Image;

class Wings extends PowerUp {
	//Class constants
	final static int IMMORTALITY_DURATION = 5;				//wings provide immortality to cupid for 5 seconds

	final static Image WINGS_IMAGE = new Image("images/wings.png", PowerUp.POWER_UP_IMAGE_WIDTH, PowerUp.POWER_UP_IMAGE_WIDTH, true, true);
	final static Image BW_WINGS = new Image("images/bw-wings.png", PowerUp.POWER_UP_IMAGE_WIDTH, PowerUp.POWER_UP_IMAGE_WIDTH, true, true);
	
	
	Wings(int x, int y){
		super(x,y, Wings.WINGS_IMAGE);
	}

	@Override
	void affectCupid(Cupid cupid, GameTimer gametimer) {		
		System.out.println("CUPID has collected a new set of WINGS!");
		gametimer.setHasWings(true);						//sets the hasWings attribute of gametimer to true
		gametimer.setWingsTime(System.nanoTime());			//sets the time when wings is collected
		gametimer.setCollectedWings();						//increments the game's collected wings
		cupid.setImmortal(true);						//makes cupid immortal
		this.setVisible(false);								//since wings has been collected, wings should not be visible anymore
	}

}
