	/***********************************************************	* 
	* This class inherits from the AnimationTimer class, and
	* is responsible for the game itself.
	* 
	* This class is responsible for the following:
	* 	- Tracking the Cupid's current strength and score
	* 	- Tracking the current time elapsed in the game
	* 	- Tracking the power-ups collected by Cupid
	* 	- Tracking if the game is over (cupid is strength is 0 or 60 seconds has passed)
	* 	- Spawning the Humans (initially and in 5 second intervals)
	* 	- Spawning power-ups (in 10 second itnervals)
	* 	- Moving the sprites (Humans, Cupid, and Arrows)
	* 	= Rendering the sprite images (Humans, Cupid, Arrows)
	* 	- Monitor the effectivity of collected power-ups
	* 	- Check the collision of sprites (Cupid-Human, Arrow-Human, Cupid-Power-up)
	*
	* @author Rhys Allen Abejay
	* @created_date 2022-05-17 14:54
	***********************************************************/

package game;

import java.util.ArrayList;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import java.util.concurrent.TimeUnit;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;

/*
 * The GameTimer is a subclass of the AnimationTimer class. It must override the handle method. 
 */
 
class GameTimer extends AnimationTimer{
	private GameStage theGame;
	private GraphicsContext gc;
	private Scene theScene;
	private Cupid myCupid;
	private ArrayList<Human> humans;
	private ArrayList <PowerUp> powerUps;
	
	private long startGame;							//attribute that stores the time when the game starts
	private long realStart;							//attribute that stores the time when the game starts, includes the time it is frozen (effect of snowflake powerup)
	
	private int spawnedHumans = 0;					//attribute for the number of humans spawned every 5 seconds
	private int spawnedBoss = 0;					//attribute for the number of boss spawned when 30-second mark is reached
	private int spawnedPowerUps = 0;				//attribute for the number of power-ups spawned every 10 seconds
	private int spawnedLackeys = 0;					//attribute for the number of lackeys spawned every 5 seconds
	
	//attributes for collected power-ups
	private int collectedHearts = 0;
	private int collectedWings = 0;
	private int collectedDolphins = 0;
	private int collectedAutopilots = 0;
	private int collectedSnowflakes = 0;
	
	private long wingsTime;
	private long dolphinTime;
	private long autopilotTime;
	private long snowflakeTime;
	private long pauseTimestamp;
	private long bossTimestamp;
	
	private boolean hasWings;
	private boolean hasDolphin;
	private boolean hasBoss;
	private boolean hasAutopilot;
	private boolean hasSnowflake;
	
	private double backgroundX;						
	
	//Class constants
	private final static int INIT_NUM_HUMANS = 7;		//constant for the initial number of humans spawned
	private final static int NEW_NUM_HUMANS = 3;		//constant for the maximum number of humans spawned every 5 seconds
	private final static int NUM_LACKEYS = 3;			//constant for the maximum number of lackeys spawned every 5 seconds (while boss is alive)
	
	private final static int MAX_POWER_UPS = 1;			//constant for the maximum number of power-ups spawned every 10 seconds
	private final static int MAX_NUM_BOSS = 1;			//constant for the maximum number of boss spawned when 30-second mark is reached
	
	private final static int GAME_DURATION = 60;		//constant for the time the game runs
	private final static int SPAWN_NEW_HUMANS = 5;		//constant for the time interval where new humans are spawned
	private final static int SPAWN_BOSS = 30;			//constant for the time elapsed when a boss should be spawned
	private final static int SPAWN_LACKEYS = 5;			//constant for the time interval when lackeys should be spawned
	
	final static int WIN_GAME = 1;						//constant for a winning game
	final static int LOSE_GAME = 0;						//constant for a losing game
	final static int GAME_STATUS_BAR_HEIGHT = 25;
	
	private final static double BACKGROUND_SPEED = 0.8;
	
	private final static Font DEFAULT_FONT = Font.font("Microsoft Sans Serif", FontWeight.NORMAL, 16);
	
	
	GameTimer(GraphicsContext gc, Scene theScene, GameStage game){
		this.gc = gc;
		this.theScene = theScene;
		this.theGame = game;
		
		this.hasWings = false;
		this.hasDolphin = false;
		
		Random r = new Random();
		int rangeY = (GameStage.WINDOW_HEIGHT-Cupid.CUPID_WIDTH)-GameTimer.GAME_STATUS_BAR_HEIGHT;
		int y = r.nextInt((rangeY)+1) + GameTimer.GAME_STATUS_BAR_HEIGHT;
		this.myCupid = new Cupid("Eros",10,y);
		
		//instantiate the ArrayList of Human
		this.humans = new ArrayList<Human>();
		
		//instantiate the ArrayList of PowerUp
		this.powerUps = new ArrayList<PowerUp>();
		
		//call the spawnHumans method (spawns 7 humans)
		this.spawnHumans();
		
		//call method to handle mouse click event
		this.handleKeyPressEvent();	
	}

	
	@Override
	public void handle(long currentNanoTime) {
		this.initScreen(currentNanoTime);
		this.monitorPowerUps(currentNanoTime);
		this.renderSprites();
		this.moveSprites();
		this.collidingSprites();
	}
	
	
	//method that initializes the game scene
	private void initScreen(long currentNanoTime) {
		this.redrawBg();
		this.drawStatusBar(currentNanoTime);
		this.isCupidDead();
		this.isGameOver(currentNanoTime);
		this.newHumans(currentNanoTime);
		this.spawnBoss(currentNanoTime);
		this.spawnPowerUps(currentNanoTime);
		this.spawnLackeys(currentNanoTime);
	}
	
	
	//method that monitors the power-ups in the game, both collected and not
	synchronized private void monitorPowerUps(long currentNanoTime) {
		this.monitorIdlePowerUps(currentNanoTime);
		this.monitorWingsEffect(currentNanoTime);
		this.monitorDolphinEffect(currentNanoTime);	
		this.monitorAutopilotEffect(currentNanoTime);
		this.monitorSnowflakeEffect(currentNanoTime);
	}
	
	
	//method that renders all the sprites in the game
	private void renderSprites() {
		this.myCupid.render(this.gc);
		this.renderArrows();
		this.renderHumans();
		this.renderPowerUps();
	}
	
	
	//method that moves all the sprites in the game
	private void moveSprites() {
		this.myCupid.move();
		this.moveArrows();
		this.moveHumans();
	}
	
	
	//method that checks if the sprites collide in the game
	synchronized private void collidingSprites() {
		this.arrowHit();
		this.humanHit();
		this.collectPowerUp();
	}
	
	
	//ALL METHODS IN initScreen:

	
	//method that redraws the background image
	//method is adapted from the Everwing sample
	private void redrawBg() {
		this.gc.clearRect(0, 0, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		this.gc.drawImage(GameStage.GAME_BG, 0, 0);
		
		this.backgroundX += GameTimer.BACKGROUND_SPEED;
		this.gc.drawImage(GameStage.GAME_BG, this.backgroundX-GameStage.GAME_BG.getWidth(), 0);
		this.gc.drawImage(GameStage.GAME_BG, this.backgroundX, 0);
		
		if(this.backgroundX >= GameStage.WINDOW_WIDTH) {
			this.backgroundX = GameStage.WINDOW_WIDTH-GameStage.GAME_BG.getWidth();
		}
	}
	

	//method that draws/displays the game status/data on the scene
	synchronized private void drawStatusBar(long currentNanoTime) {
		this.gc.setFill(Color.STEELBLUE);
		this.gc.fillRect(0, 0, GameStage.WINDOW_WIDTH, GameTimer.GAME_STATUS_BAR_HEIGHT);
		
		this.drawSpecialStats();
		this.drawElapsedTime(currentNanoTime);
		this.drawCurrentScore();
		this.drawCurrentStrength();
		this.drawCollectedPowerUps();		
	}
	
	
	//method to draw the elapsed time in the game
	synchronized private void drawElapsedTime(long currentNanoTime) {
		int runtime;
		
		if(this.hasSnowflake) {
			this.gc.drawImage(GameStage.FROZEN_TIMER_ICON, 305, 0);
			runtime = this.elapsedTime(this.snowflakeTime, this.realStart);			//the time shown will be the time when the game started up to the time the Cupid collected a snowflake
		}else {
			this.gc.drawImage(GameStage.TIMER_ICON, 305, 0);
			runtime = this.elapsedTime(currentNanoTime, this.startGame);
		}

		this.gc.setFont(GameTimer.DEFAULT_FONT);
		this.gc.setFill(Color.BLACK);
		this.gc.fillText("Time: ", 335, 18);
		
		if(runtime < 10) {
			this.gc.fillText("0:0"+ runtime, 380, 18);
		}else if (runtime < 60 && runtime >= 10){
			this.gc.fillText("0:"+ runtime, 380, 18);
		}else if (runtime == 60){
			this.gc.fillText("1:00", 380, 18);
		}
	}
	
	
	//method to draw the current score of cupid
	synchronized private void drawCurrentScore() {
		this.gc.drawImage(GameStage.SCORE_ICON, 445, 0);
		
		this.gc.setFont(GameTimer.DEFAULT_FONT);
		this.gc.setFill(Color.BLACK);
		this.gc.fillText("Score: ", 475, 18);
		this.gc.fillText(this.myCupid.getScore() + "", 525, 18);
	}
	
	
	//method to draw cupid's current strength
	synchronized private void drawCurrentStrength() {
		if(this.hasWings) {
			this.gc.drawImage(GameStage.IMMORTAL_ICON, 570, 0);						//strength icon will change when the Cupid is immortal
		}else {
			this.gc.drawImage(GameStage.STRENGTH_ICON, 570, 0);
		}
		
		this.gc.setFont(GameTimer.DEFAULT_FONT);
		this.gc.setFill(Color.BLACK);
		this.gc.fillText("Strength: ", 600, 18);
		this.gc.fillText(this.myCupid.getStrength() + "", 665, 18);
	}
	
	
	//method to draw the running power-up cupid has
	synchronized public void drawSpecialStats() {
		
		if(this.hasBoss) {															//text will be shown when there is a boss in the game
			this.gc.setFont(GameTimer.DEFAULT_FONT);
			this.gc.setFill(Color.BLACK);
			this.gc.fillText("Frenzy", 125, 18);
		}
		
		
		//Images of power-ups will be drawn in the game
		//if the power-up is active, the power-up image will be colored
		//otherwise it will draw a black and white image
		if(!this.hasWings) {
			this.gc.drawImage(Wings.BW_WINGS, 190, 0, 25, 25);
		}else {
			this.gc.drawImage(Wings.WINGS_IMAGE, 190, 0, 25, 25);
		}
		
		if(!this.hasDolphin) {
			this.gc.drawImage(Dolphin.BW_DOLPHIN, 215, 0, 25, 25);
		}else {
			this.gc.drawImage(Dolphin.DOLPHIN_IMAGE, 215, 0, 25, 25);
		}
		
		if(!this.hasAutopilot) {
			this.gc.drawImage(Autopilot.BW_AUTOPILOT, 240, 0, 25, 25);
		}else {
			this.gc.drawImage(Autopilot.AUTOPILOT_IMAGE, 240, 0, 25, 25);
		}
		
		if(!this.hasSnowflake) {
			this.gc.drawImage(Snowflake.BW_SNOWFLAKE, 265, 0, 25, 25);
		}else {
			this.gc.drawImage(Snowflake.SNOWFLAKE_IMAGE, 265, 0, 25, 25);
		}
		
	}
	
	
	//method to draw all the power-ups cupid has collected in the game
	synchronized public void drawCollectedPowerUps() {
		int x = 720;
		
		for(int i = 0; i < this.collectedHearts; i++) {
			this.gc.drawImage(Heart.HEART_IMAGE, x, 0, 25, 25);
			x += 10;
		}
		
		for(int i = 0; i < this.collectedWings; i++) {
			this.gc.drawImage(Wings.WINGS_IMAGE, x, 0, 25, 25);
			x += 10;
		}
		
		for(int i = 0; i < this.collectedDolphins; i++) {
			this.gc.drawImage(Dolphin.DOLPHIN_IMAGE, x, 0, 25, 25);
			x += 10;
		}
		
		for(int i = 0; i < this.collectedAutopilots; i++) {
			this.gc.drawImage(Autopilot.AUTOPILOT_IMAGE, x, 0, 25, 25);
			x += 10;
		}
		
		for(int i = 0; i < this.collectedSnowflakes; i++) {
			this.gc.drawImage(Snowflake.SNOWFLAKE_IMAGE, x, 0, 25, 25);
			x += 10;
		}
		
	}
	
	
	//method that checks if the cupid is dead
	synchronized private void isCupidDead() {
		if(!this.myCupid.isAlive()) {
			System.out.println("CUPID has lost all of his strength!");
			System.out.println("GAME OVER!");
			
			this.stop();			//stops the game
			
			int score = this.myCupid.getScore();
			
			PauseTransition transition = new PauseTransition(Duration.seconds(1));
			transition.play();
			
			transition.setOnFinished(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent arg0) {
					theGame.flashGameOver(GameTimer.LOSE_GAME, score);
				}
			});
		}
	}
	
	
	//method that checks if the elapsed time has reached a minute (60 seconds)
	synchronized private void isGameOver(long currentNanoTime) {		
		int runtime = this.elapsedTime(currentNanoTime, this.startGame);
		
		if(runtime == GameTimer.GAME_DURATION || runtime > GameTimer.GAME_DURATION) {	
			
			System.out.println("60 seconds is up!");
			System.out.println("GAME OVER!");
			
			this.stop();						//stops the game
			
			int score = this.myCupid.getScore();
			
			PauseTransition transition = new PauseTransition(Duration.seconds(1));
			transition.play();
			
			transition.setOnFinished(new EventHandler<ActionEvent>() {
				
				public void handle(ActionEvent arg0) {
					if(myCupid.isAlive()) {
						theGame.flashGameOver(GameTimer.WIN_GAME, score);
					}else {
						theGame.flashGameOver(GameTimer.LOSE_GAME, score);
					}
				}
			});
		}
	}
	
	
	//method that spawns 3 more humans every 5 seconds
	synchronized private void newHumans(long currentNanoTime) {		
		int timeElapsed = this.elapsedTime(currentNanoTime, this.startGame);
		
		//check if timeElapsed is divisible by 5, and is not equal to 0 nor 60
		if((timeElapsed % GameTimer.SPAWN_NEW_HUMANS == 0) && (timeElapsed != 0) && (timeElapsed != 60)) {
			if(!this.hasSnowflake) {		//checks if the game has a snowflake (so that there will be no repetition of adding humans)
				this.addHumans();
			}
		}else {
			this.spawnedHumans = 0;			//set the spawnedHumans attribute to 0 
		}
	}
	
	
	//method that spawns the boss when the elapsed time has reached 30 seconds
	synchronized private void spawnBoss(long currentNanoTime) {		
		int timeElapsed = this.elapsedTime(currentNanoTime, this.startGame);
		
		if((timeElapsed % GameTimer.SPAWN_BOSS == 0) && (timeElapsed != 0) && (timeElapsed != 60)) {
			if(!this.hasSnowflake && !this.hasBoss) {	//checks if the game has a snowflake or has a boss human already (to prevent spawning a new boss when there is already one present)
				this.addBoss();
			}
		}
	}
	
	
	//method that spawns a power-up every 10 seconds
	synchronized private void spawnPowerUps(long currentNanoTime) {		
		int timeElapsed = this.elapsedTime(currentNanoTime, this.startGame);
		
		if((timeElapsed % PowerUp.SPAWN_TIME == 0) && (timeElapsed != 0) && (timeElapsed != 60)) {
			if(!this.hasSnowflake) {					//checks if there is a snowflake so that spawning the powerup will not repeat in the same timestamp
				this.addPowerUp();
			}
		}else {
			this.spawnedPowerUps = 0;
		}
	}
	
	
	//method that spawns human lackeys when the boss is still alive 
	synchronized private void spawnLackeys(long currentNanoTime) {
		int timeElapsed = this.elapsedTime(currentNanoTime, this.bossTimestamp);
		
		if((timeElapsed % GameTimer.SPAWN_LACKEYS == 0) && (this.hasBoss == true) && (timeElapsed != 0)){
			if(!this.hasSnowflake) {				//checks if there is a snowflake so that spawning the alckeys will not repeat
				this.addLackeys();
			}
		}else {
			this.spawnedLackeys = 0;
		}
		
	}
	

	//method that returns the elapsed time between the starting time and the current time
	private int elapsedTime(long currentNanoTime,long origin) {
		long currentTime = TimeUnit.NANOSECONDS.toSeconds(currentNanoTime);
		long startTime = TimeUnit.NANOSECONDS.toSeconds(origin);
			
		int timeElapsed = (int)(currentTime-startTime);
		return timeElapsed;
	}
	
	
	//method that spawns humans and adds them to the humans ArrayList
	synchronized private void addHumans() {
		Random r = new Random();
		
		while(this.spawnedHumans < GameTimer.NEW_NUM_HUMANS) {				//while spawnedHumans is less than the maximum number of humans to be spawned every 5 seconds
			
			int rangeX = ((GameStage.WINDOW_WIDTH-Human.HUMAN_WIDTH)-(GameStage.WINDOW_WIDTH/2))+1;
			int rangeY = (GameStage.WINDOW_HEIGHT-Human.HUMAN_HEIGHT)-GameTimer.GAME_STATUS_BAR_HEIGHT;
			
			int x = r.nextInt(rangeX) + (GameStage.WINDOW_WIDTH/2);
			int y = r.nextInt((rangeY)+1) + GameTimer.GAME_STATUS_BAR_HEIGHT;
			
			Human h = new Human(x,y, Human.NORMAL);
			this.humans.add(h);
			this.spawnedHumans++;						//increment spawnedHumans
		}
	}
	

	//method that spawns a boss human and adds it to the humans ArrayList
	synchronized private void addBoss() {		
		while(this.spawnedBoss < GameTimer.MAX_NUM_BOSS) {				
			
			int x = GameStage.WINDOW_WIDTH-Human.BOSS_HUMAN_HEIGHT;			//x position of boss will be changed when initialized to make it centered
			int y = ((GameStage.WINDOW_HEIGHT-GameTimer.GAME_STATUS_BAR_HEIGHT)-Human.BOSS_HUMAN_HEIGHT)/2;
			
			Human h = new Human(x,y, Human.BOSS);
			this.humans.add(h);
			this.spawnedBoss++;							//increment spawnedBoss	
			
			this.setHasBoss(true);
			this.bossTimestamp = System.nanoTime();
		}
	}
	
	
	//method that spawns a random power-up and adds it the PowerUps ArrayList
	synchronized private void addPowerUp() {
		Random r = new Random();
		
		while(this.spawnedPowerUps < GameTimer.MAX_POWER_UPS) {
						
			int rangeX = GameStage.WINDOW_WIDTH/2;
			int rangeY = (GameStage.WINDOW_HEIGHT-PowerUp.POWER_UP_IMAGE_WIDTH)-GameTimer.GAME_STATUS_BAR_HEIGHT;
			
			int x = r.nextInt(rangeX+1);
			int y = r.nextInt(rangeY+1)+GameTimer.GAME_STATUS_BAR_HEIGHT;
			
			Random t = new Random();
			int v = t.nextInt(100)+1;
			
			if(v % 10 == 0 && v <= 50) {
				Snowflake s = new Snowflake(x,y);
				this.powerUps.add(s);
				this.spawnedPowerUps++;
			}else if(v % 3 == 0) {
				Wings w = new Wings(x,y);
				this.powerUps.add(w);
				this.spawnedPowerUps++;
			}else if(v % 5 == 0) {
				Autopilot u = new Autopilot(x,y);
				this.powerUps.add(u);
				this.spawnedPowerUps++;
			}else if(v % 8 == 0 && v <= 50) {
				Heart h = new Heart (x,y);
				this.powerUps.add(h);
				this.spawnedPowerUps++;
			}else {
				Dolphin d = new Dolphin(x,y);
				this.powerUps.add(d);
				this.spawnedPowerUps++;
			}
		}
	}

	
	//method that spawns humans and adds them to the humans ArrayList
	synchronized private void addLackeys() {
		Random r = new Random();
		
		while(this.spawnedLackeys < GameTimer.NUM_LACKEYS) {
			
			int rangeX = ((GameStage.WINDOW_WIDTH-Human.HUMAN_WIDTH)-110)+1;
			int rangeY = (GameStage.WINDOW_HEIGHT-Human.HUMAN_HEIGHT)-GameTimer.GAME_STATUS_BAR_HEIGHT;
			
			int x = rangeX;
			int y = r.nextInt((rangeY)+1) + GameTimer.GAME_STATUS_BAR_HEIGHT;
			
			Human h = new Human(x,y, Human.LACKEY);
			this.humans.add(h);
			this.spawnedLackeys++;		
		}
	}
	
	
	//ALL METHODS IN monitorPowerUps:
	

	//method to monitor uncollected power-ups
	synchronized private void monitorIdlePowerUps(long currentNanoTime) {
		for(int i = 0; i < this.powerUps.size(); i++) {
			PowerUp p = this.powerUps.get(i);
			
			if(!p.isVisible()) {
				this.powerUps.remove(p);
			}else {				
				int timeElapsed = this.elapsedTime(currentNanoTime, this.startGame);
				
				if((timeElapsed % PowerUp.IDLE_TIME == 0) && (timeElapsed != 0) && (timeElapsed % 10 != 0)) {
					p.setVisible(false);				//since the power-up is not collected within 5 seconds, remove the power-up
				}
			}	
		}
	}
	
	
	//method that monitors the duration of the wings effect on cupid
	synchronized private void monitorWingsEffect(long currentNanoTime) {
		if(this.hasWings) {
			int timeElapsed = this.elapsedTime(currentNanoTime, this.wingsTime);
			
			if((timeElapsed % Wings.IMMORTALITY_DURATION == 0) && (timeElapsed != 0)){
				//the effect of the collected wings has worn off
				this.myCupid.setImmortal(false);					//sets the immortal attribute to false		
				this.setHasWings(false);							//sets the hasWings attribute of gametimer to false
				this.setWingsTime(0);								//resets wingsTime
				
				System.out.println("WINGS effect has worn off.");
			}
		}
	}

	
	//method that monitors the duration of the dolphin's effect on cupid
	synchronized private void monitorDolphinEffect(long currentNanoTime) {
		if(this.hasDolphin) {			
			int timeElapsed = this.elapsedTime(currentNanoTime, this.dolphinTime);
			
			if((timeElapsed % Dolphin.SPEED_UP_DURATION == 0) && (timeElapsed != 0)){
				//the effect of the collected dolphin has worn off
				this.myCupid.setFast(false);					//sets the fast attribute to false
				this.setHasDolphin(false);						//sets the hasDolphin attribute of gametimer to false
				this.setDolphinTime(0);							//resets dolphinTime
				
				System.out.println("DOLPHIN effect has worn off");
			}
		}
	}
	
	
	//method that implements and monitors the duration of the autopilot's effect on cupid
	synchronized private void monitorAutopilotEffect(long currentNanoTime) {
		if(this.hasAutopilot) {
			this.myCupid.shoot();
			
			int timeElapsed = this.elapsedTime(currentNanoTime, this.autopilotTime);
			
			if((timeElapsed % Autopilot.AUTOPILOT_DURATION == 0) && (timeElapsed != 0)) {
				//the effect of the collected autopilot has worn off
				this.myCupid.setAutopilot(false);					//sets the autopilot attribute to false
				this.setHasAutopilot(false);						//sets the hasAutopilot attribute of gametimer to false
				this.setAutopilotTime(0);							//resets autopilotTime
				
				System.out.println("AUTOPILOT effect has worn off");
			}
		}
	}
	
	
	//method that monitors the duration of the snowflake's effect on the game
	synchronized private void monitorSnowflakeEffect(long currentNanoTime) {
		if(this.hasSnowflake) {
			int timeElapsed =  this.elapsedTime(currentNanoTime, this.snowflakeTime);
			
			if((timeElapsed % Snowflake.TIME_FREEZE_DURATION == 0) && (timeElapsed != 0)) {
				//the effect of the collected snowflake ahs worn off
				long freezetime = System.nanoTime() - this.snowflakeTime;		
				this.startGame += freezetime;							//adjusts the startGame by the amount of time the game is "frozen"
				
				this.myCupid.setFrozeTime(false);						//sets the frozeTime attribute to false
				this.setHasSnowflake(false);							//sets the hasSnowflake attribute of gametimer to false
				this.setSnowflakeTime(0);								//resets snowflakeTime
				
				System.out.println("SNOWFLAKE effect has worn off");
			}
			
		}
	}

	
	//ALL METHODS IN renderSprites:
	
	
	//method that will render/draw the arrows to the canvas
	private void renderArrows() {
		for (Arrow a: this.myCupid.getArrows()) {
			a.render(this.gc);
		}
	}
	
	
	//method that will render/draw the humans to the canvas
	private void renderHumans() {
		for (Human h : this.humans){
			h.render(this.gc);
		}
	}
	
	
	//method that will render/draw power-ups in the canvas
	private void renderPowerUps() {
		for (PowerUp p: this.powerUps) {
			p.render(this.gc);
		}
	}

	
	//ALL METHODS IN moveSprites:
	
	
	//method that will move the arrows shot by cupid
	private void moveArrows(){
		//create a local arraylist of Arrows for the arrows 'shot' by cupid
		ArrayList<Arrow> aList = this.myCupid.getArrows();
			
		//Loop through the arrow list and check whether a arrow is still visible.
		for(int i = 0; i < aList.size(); i++){
			Arrow a = aList.get(i);
				
			if(a.getVisible()) {
				a.move();
			}else {
				aList.remove(a);
			}
				
		}
	}
	
	
	//method that will move the humans 
	synchronized private void moveHumans(){
		//Loop through the humans arraylist
		for(int i = 0; i < this.humans.size(); i++){
			Human h = this.humans.get(i);
				
			if(h.isAlive()) {
				if(!this.hasSnowflake) {					//humans will move when the game has not collected a snowflake
					h.move();
				}
			}else {
				this.humans.remove(h);
			}
		}
	}

	
	//ALL METHODS IN collidingSprites:
	
	
	//method that checks if the arrow hits a human
	synchronized private void arrowHit() {
		ArrayList <Arrow> cupidArrows = this.myCupid.getArrows();
		
		for(Arrow a: cupidArrows) {
			for(Human h: this.humans) {
				if(a.collidesWith(h)) {									//check if arrow has hit a human
					
					h.updateHealth(a.getDamage(), this);				//decrease the human's health by the arrow's damage
					
					a.setDamage(0);										//sets the damage of the arrow to 0 (to ensure that only one human has been hit)
					a.setVisible(false);								//makes the arrow not visible when it has hit a human 
					
					if(!h.isAlive()) {
						this.myCupid.updateScore();
					}
				}
			}
		}
	}
	
	
	//method that checks if the human hits cupid
	synchronized private void humanHit() {
		for(Human h: this.humans) {
			if(this.myCupid.collidesWith(h) && this.myCupid.isAlive()) {				
				if(this.myCupid.isImmortal()) {
					h.updateHealth(this.myCupid.getStrength(), this);					//if immortal, only update the health of human
				}else {
					int initialStrength = this.myCupid.getStrength();
					
					this.myCupid.updateStrength(h.getStrength());						//decrease cupid strength by strength of human
					this.myCupid.bounce(h.getX());										//make cupid change x position
					
					System.out.println("CUPID got hit by a HUMAN with strength " + h.getStrength());
					System.out.println("Current CUPID's strength is " + this.myCupid.getStrength());
					
					h.updateHealth(initialStrength, this);								//decrease human's health by the intialStrength of cupid (before cupid hit human)
				}
			}
		}
	}
	
	
	//method that checks if cupid has collided/collected with/a power-up
	synchronized private void collectPowerUp() {
		for(PowerUp p: this.powerUps) {
			if(this.myCupid.collidesWith(p)) {
				p.affectCupid(this.myCupid, this);				
			}
		}
	}
	
	
	//method that will spawn 7 humans at a random x,y location (initial)
	private void spawnHumans(){		
		Random r = new Random();
		for (int i = 0; i < GameTimer.INIT_NUM_HUMANS; i++) {
			
			int rangeX = ((GameStage.WINDOW_WIDTH-Human.HUMAN_WIDTH)-(GameStage.WINDOW_WIDTH/2))+1;
			int rangeY = (GameStage.WINDOW_HEIGHT-Human.HUMAN_HEIGHT)-GameTimer.GAME_STATUS_BAR_HEIGHT;
			
			int x = r.nextInt(rangeX) + (GameStage.WINDOW_WIDTH/2);
			int y = r.nextInt((rangeY)+1) + GameTimer.GAME_STATUS_BAR_HEIGHT;
			
			Human h = new Human (x,y, Human.NORMAL);
			this.humans.add(h);
		}
	}
	
	
	//method that will listen and handle the key press events
	private void handleKeyPressEvent() {
		this.theScene.setOnKeyPressed(new EventHandler<KeyEvent>(){
			public void handle(KeyEvent e){
            	KeyCode code = e.getCode();
                movemyCupid(code);
			}
		});
		
		this.theScene.setOnKeyReleased(new EventHandler<KeyEvent>(){
		            public void handle(KeyEvent e){
		            	KeyCode code = e.getCode();
		                stopmyCupid(code);
		            }
		        });
    }
	
	
	//method that will move the ship depending on the key pressed
	private void movemyCupid(KeyCode ke) {
		if(this.hasDolphin) {
			if(ke==KeyCode.UP) this.myCupid.setDY(-Cupid.UPGRADED_SPEED);                 

			if(ke==KeyCode.LEFT) this.myCupid.setDX(-Cupid.UPGRADED_SPEED);

			if(ke==KeyCode.DOWN) this.myCupid.setDY(Cupid.UPGRADED_SPEED);
			
			if(ke==KeyCode.RIGHT) this.myCupid.setDX(Cupid.UPGRADED_SPEED);
		}else {
			if(ke==KeyCode.UP) this.myCupid.setDY(-Cupid.NORMAL_SPEED);                 

			if(ke==KeyCode.LEFT) this.myCupid.setDX(-Cupid.NORMAL_SPEED);

			if(ke==KeyCode.DOWN) this.myCupid.setDY(Cupid.NORMAL_SPEED);
			
			if(ke==KeyCode.RIGHT) this.myCupid.setDX(Cupid.NORMAL_SPEED);
		}
		
		if(ke==KeyCode.SPACE) this.myCupid.shoot();			
		
		System.out.println(ke+" key pressed.");
   	}
	
	
	//method that will stop the ship's movement; set the ship's DX and DY to 0
	private void stopmyCupid(KeyCode ke){
		this.myCupid.setDX(0);
		this.myCupid.setDY(0);
	}
	
	
	//method that pauses the game/stops the game temporarily
	void pause() {
		this.gc.setFont(GameTimer.DEFAULT_FONT);
		this.gc.setFill(Color.BLACK);
		this.gc.fillText("Paused", 65, 18);
		
		this.pauseTimestamp = System.nanoTime();
		this.stop();										//stops the GameTimer
		System.out.println("Game is paused.");
	}
	
	
	//method that continues the game from where it left off
	synchronized void play() {
		long pauseTime = System.nanoTime() - this.pauseTimestamp;
		this.startGame += pauseTime;						//adjusts the startGame by the time it is paused
		
		
		//Adjusts other timestamps for time-related powerups
		if(this.dolphinTime != 0) {
			this.dolphinTime += pauseTime;
		}
		
		if(this.wingsTime != 0) {
			this.wingsTime +=  pauseTime;
		}
		
		if(this.autopilotTime != 0) {
			this.autopilotTime += pauseTime;
		}
		
		if(this.snowflakeTime != 0) {
			this.snowflakeTime += pauseTime;
			this.realStart += pauseTime;
		}
		
		
		this.start();										//starts the GameTimer
		System.out.println("Game is resumed.");
	}
	
	
	//setters
	void setStartGame(long time) {
		this.startGame = time;
	}
	
	void setHasWings(boolean value) {
		this.hasWings = value;
	}
	
	void setHasDolphin(boolean value) {
		this.hasDolphin = value;
	}
	
	void setWingsTime(long value) {
		this.wingsTime = value;
	}
	
	void setDolphinTime(long value) {
		this.dolphinTime = value;
	}
	
	void setCollectedHearts() {
		this.collectedHearts++;
	}
	
	void setCollectedWings() {
		this.collectedWings++;
	}
	
	void setCollectedDolphins() {
		this.collectedDolphins++;
	}
	
	void setHasAutopilot(boolean value) {
		this.hasAutopilot = value;
	}
	
	void setAutopilotTime(long value) {
		this.autopilotTime = value;
	}
	
	void setCollectedAutopilots() {
		this.collectedAutopilots++;
	}
	
	void setHasSnowflake(boolean value) {
		this.hasSnowflake = value;
	}
	
	synchronized void setSnowflakeTime(long value) {
		this.snowflakeTime = value;
		this.realStart = this.startGame;
	}
	
	void setCollectedSnowflakes() {
		this.collectedSnowflakes++;
	}
	
	void setHasBoss(boolean value) {
		this.hasBoss = value;
	}
}
