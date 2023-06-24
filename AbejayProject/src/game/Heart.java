	/***********************************************************	* 
	* This class inherits from the class PowerUp and implements
	* the inherited method affectCupid which doubles the current
	* strength of Cupid once collected.
	*
	* @author Rhys Allen Abejay
	* @created_date 2022-05-18 19:12
	***********************************************************/

package game;

import javafx.scene.image.Image;

class Heart extends PowerUp {
	//Class constant
	final static Image HEART_IMAGE = new Image("images/heart.png", PowerUp.POWER_UP_IMAGE_WIDTH, PowerUp.POWER_UP_IMAGE_WIDTH, true, true);
	
	Heart(int x, int y){
		super(x,y,Heart.HEART_IMAGE);
	}

	@Override
	void affectCupid(Cupid cupid, GameTimer gametimer) {
		System.out.println("CUPID has collected a HEART!");
		cupid.doubleStrength();							//double the strength of cupid
		gametimer.setCollectedHearts();					//increments the game's collected hearts
		this.setVisible(false);							//since heart is collected, heart should not be visible anymore
	}

}
