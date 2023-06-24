	/***********************************************************	*  
	* This class inherits from the class PowerUp and implements 
	* the inherited method affectCupid which automatically makes
	* the Cupid shoot arrows continuously for 3 seconds.
	* 
	* @author Rhys Allen Abejay
	* @created_date 2022-05-29 00:15
	***********************************************************/

package game;

import javafx.scene.image.Image;

class Autopilot extends PowerUp {
	//Class constants	
	final static int AUTOPILOT_DURATION = 3;					//autopilot makes cupid shoot arrows automatically for 4 seconds
	
	final static Image AUTOPILOT_IMAGE = new Image("images/autopilot.png", PowerUp.POWER_UP_IMAGE_WIDTH, PowerUp.POWER_UP_IMAGE_WIDTH, true, true);
	final static Image BW_AUTOPILOT = new Image("images/bw-autopilot.png", PowerUp.POWER_UP_IMAGE_WIDTH, PowerUp.POWER_UP_IMAGE_WIDTH, true, true);
	
	Autopilot(int x, int y){
		super(x,y, Autopilot.AUTOPILOT_IMAGE);
	}
	
	@Override
	void affectCupid(Cupid cupid, GameTimer gametimer) {
		System.out.println("CUPID is going AUTOPILOT!");
		gametimer.setHasAutopilot(true);								//set hasAutopilot attribute to true
		gametimer.setAutopilotTime(System.nanoTime());					//set the autopilotTime to the time it is collected
		gametimer.setCollectedAutopilots();								//increment collectedAutopilots attribute
		cupid.setAutopilot(true);										//set autopilot attribute to true
		this.setVisible(false);											//set visibility to false since it is collected
		
	}

}
