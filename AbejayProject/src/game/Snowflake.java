	/***********************************************************	*  
	* This class inherits from the class PowerUp and implements 
	* the inherited method affectCupid which freezes the time for
	* 3 seconds. Cupid can move but the humans cannot move.
	* 
	* @author Rhys Allen Abejay
	* @created_date 2022-05-29 11:36
	***********************************************************/

package game;

import javafx.scene.image.Image;

class Snowflake extends PowerUp {
	//Class constants
	final static int TIME_FREEZE_DURATION = 3;					//snowflake makes the time is frozen for 3 seconds
	
	final static Image SNOWFLAKE_IMAGE = new Image("images/snowflake.png", PowerUp.POWER_UP_IMAGE_WIDTH, PowerUp.POWER_UP_IMAGE_WIDTH, true, true);
	final static Image BW_SNOWFLAKE = new Image("images/bw-snowflake.png", PowerUp.POWER_UP_IMAGE_WIDTH, PowerUp.POWER_UP_IMAGE_WIDTH, true, true);
	
	Snowflake(int x, int y){
		super(x,y,Snowflake.SNOWFLAKE_IMAGE);
	}
	
	@Override
	void affectCupid(Cupid cupid, GameTimer gametimer) {
		System.out.println("CUPID FROZE the time!");
		gametimer.setHasSnowflake(true);							//set hasSnowflake attribute to true
		gametimer.setSnowflakeTime(System.nanoTime());				//sets the snowflakeTime attribute to the time it is collected
		gametimer.setCollectedSnowflakes();							//increments collectedSnowflakes attribute
		cupid.setFrozeTime(true);									//set frozeTime attribute to true
		this.setVisible(false);										//since it is collected, set visibility to false
		

	}

}
