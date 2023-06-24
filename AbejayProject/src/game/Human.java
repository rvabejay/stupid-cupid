	/***********************************************************	* 
	* This class defines the attributes and methods of the Human,
	* which is Cupid's target in the game.
	* 
	* The class inherits from the Sprite class.
	* 
	* The Human has a random strength and speed value, and is initially
	* alive. The Human also has a random initial movement direction.
	* Seven Humans are initially spawned in the game, and three more
	* Humans are spawned every 5 seconds of the game.
	* 
	* Defined in this class is the move attribute which changes
	* the x value of the Human, depending on whether it moves to the
	* right or left, and if it has reached the leftmost/rightmost side
	* of the screen, when called.
	*
	* @author Rhys Allen Abejay
	* @created_date 2022-05-17 14:54
	***********************************************************/

package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Random;

class Human extends Sprite {
	private String type;											//type of human
	private int strength;											//damage of the human to cupid if human hits cupid
	private int health;												//health of human (may or may not be equal to strength)
	private int speed;												//movement speed of human
	private boolean alive;
	private boolean moveRight;										//attribute that will determine if a human will initially move to the right
		
	//Class constants
	private final static int MIN_HUMAN_DAMAGE = 30;					//minimum damage of a human
	private final static int MAX_HUMAN_DAMAGE = 40;					//maximum damage of a human
	private static final int MAX_HUMAN_SPEED = 5;					//maximum speed of a human
	
	final static int BOSS_STRENGTH = 50;							//damage of boss when it hits cupid
	final static int BOSS_INIT_HEALTH  = 3000;						//initial health of boss when spawned
	final static int BOSS_HUMAN_HEIGHT = 150;						//width of boss human image
	final static int LACKEY_STRENGTH = 50;
	
	final static String NORMAL = "Normal";	
	final static String LACKEY = "Lackey";
	final static String BOSS = "Boss";
	
	final static int HUMAN_WIDTH = 80;								//width of human image
		
	final static Image MALE_HUMAN_IMAGE = new Image("images/male-human.png",Human.HUMAN_WIDTH,Human.HUMAN_WIDTH,true,true);
	final static Image FEMALE_HUMAN_IMAGE = new Image("images/female-human.png",Human.HUMAN_WIDTH,Human.HUMAN_WIDTH,true,true);
	final static Image FEMALE_BOSS = new Image("images/boss-female.png",119,Human.BOSS_HUMAN_HEIGHT,true,true);
	final static Image MALE_BOSS = new Image("images/boss-male.png",102,Human.BOSS_HUMAN_HEIGHT,true,true);
	
	final static int HUMAN_HEIGHT = (int) Human.MALE_HUMAN_IMAGE.getHeight();					//real height of the human image
	final static int MALE_BOSS_WIDTH = (int) Human.MALE_BOSS.getWidth();						//real width of the male boss image
	private final static int FEMALE_BOSS_WIDTH = (int) Human.FEMALE_BOSS.getWidth();			//real width of the female boss image
	
	
	Human(int x, int y, String type){
		super(x,y);
		this.alive = true;
		this.type = type;
		
		Random r = new Random();
		Random s = new Random();
		
		int randomSpeed = r.nextInt(Human.MAX_HUMAN_SPEED)+1;			//randomize human speed (1-5)
		this.speed = randomSpeed;
		
		this.moveRight = s.nextBoolean();								//randomize moveRight attribute
		
		if(type == Human.NORMAL) {
			this.randomizeImage();
			this.randomizeStrength();
		}else if (type == Human.LACKEY){
			this.initLackeyAttributes();
		}else {
			this.initBossAttributes();
		}
		
	}
	
	
	//method that randomizes the human image
	private void randomizeImage() {
		Random x = new Random();
		int y = x.nextInt(2);
		
		if(y == 0) {
			this.loadImage(Human.MALE_HUMAN_IMAGE);
		}else {
			this.loadImage(Human.FEMALE_HUMAN_IMAGE);
		}
	}
	
	
	//method that randomizes the human strength
	private void randomizeStrength() {
		Random x = new Random();
		int range = Human.MAX_HUMAN_DAMAGE-Human.MIN_HUMAN_DAMAGE;
		
		int randomStrength = x.nextInt(range+1)+Human.MIN_HUMAN_DAMAGE;		//randomize strength (from 30 to 40)
		
		this.strength = randomStrength;
		this.health = this.strength;
	}
	
	
	//method that initializes the boss human attributes
	private void initBossAttributes() {
		Random x = new Random();
		int y = x.nextInt(2);
		
		if(y == 0) {
			this.loadImage(Human.MALE_BOSS);
			this.x = GameStage.WINDOW_WIDTH - Human.MALE_BOSS_WIDTH;
		}else {
			this.loadImage(Human.FEMALE_BOSS);
			this.x = GameStage.WINDOW_WIDTH - Human.FEMALE_BOSS_WIDTH;
		}
		
		this.strength = Human.BOSS_STRENGTH;
		this.health = Human.BOSS_INIT_HEALTH;
	}
	
	
	//method that initializes the lackey human attributes
	private void initLackeyAttributes() {
		this.randomizeImage();
		this.strength = Human.LACKEY_STRENGTH;
		this.health = Human.LACKEY_STRENGTH;
	}
	
	
	//overrides the render method of sprite, includes text showing the cupids's strength
	@Override
	void render(GraphicsContext gc) {
		super.render(gc);
			
		gc.setFont(Cupid.DEFAULT_FONT);		
		gc.setFill(Color.WHITE);
			
		if(this.getHealth() >= 100) {
			gc.fillText(this.getHealth() + "", this.getX()+20, this.getY()+8);
		}else if(this.getHealth() >= 10 && this.getHealth() < 100) {
			gc.fillText(this.getHealth() + "", this.getX()+25, this.getY()+8);
		}else {
			gc.fillText(this.getHealth() + "", this.getX()+30, this.getY()+8);
		}
			
	}
	

	//method that changes the x position of the human
	void move(){		
		if(this.moveRight) {						//human is moving to the right
			
			if(!this.isRightBounds()) {				//human is not yet at the rightmost side of the window
				this.x += this.speed;				//increment x by speed
			}else {									//human is at the rightmost side of the window
				this.moveRight = false;				//make human move to the left
			}
			
		}else {										//human is moving to the left
			
			if(!this.isLeftBounds()) {				//human is not yet at the leftmost side of the window
				this.x -= this.speed;				//decrement x by speed
			}else {									//human is at the leftmost side of the window
				this.moveRight = true;				//make human move to the right
			}
			
		}	
	}
	
	
	//method that checks if the human has reached the right boundary
	private boolean isRightBounds() {
		int temp;
		
		if(this.type == Human.NORMAL) {
			temp = this.x + this.speed + Human.HUMAN_WIDTH;
		}else {
			if(this.img == Human.FEMALE_BOSS) {
				temp = this.x + this.speed + Human.FEMALE_BOSS_WIDTH;
			}else {
				temp = this.x + this.speed + Human.MALE_BOSS_WIDTH;
			}
			
		}
		
		
		if(temp < GameStage.WINDOW_WIDTH) {			//check if temp is less than window width (rightmost side of window)
			return false;							//human is not at the right boundary yet
		}else {
			return true;							//human is at the right boundary
		}
	}
	
	
	//method that checks if the human has reached the left boundary
	private boolean isLeftBounds() {
		int temp;
		
		temp = this.x + this.speed;
		
		if(temp > 0) {								//check if temp is greater than 0 (leftmost side of the window)
			return false;							//human is not at the left boundary yet
		}else {
			return true;							//human is at the left boundary
		}
	}
	
	
	//getters
	boolean isAlive() {
		return this.alive;
	}
		
	int getStrength() {
		return this.strength;
	}
	
	int getHealth() {
		return this.health;
	}
	
	String getType() {
		return this.type;
	}
	
	
	//setters
	
	//method that sets the alive attribute to false
	private void die() {
		this.alive = false;
	}
	
	//method that updates the human's health
	void updateHealth(int damage, GameTimer game) {
		this.health -= damage;								//decrease human health by the damage
		this.updateStrength(damage);						//call the updateStrength method
		
		if(this.health <= 0) {								//check if health is 0 or less than 0
			this.health = 0;								//set health to 0		
			this.die();
			
			if(this.type == Human.BOSS) {					//check if the human is a boss human
				game.setHasBoss(false);						//set the hasBoss attribute to false
			}
		}
	}
	
	//method that updates the human's strength
	//does not implement on a BOSS type human, as the BOSS's strength is consistently 50
	private void updateStrength(int damage) {
		if(this.type != BOSS) {								//checks if the human is not a boss
			this.strength -= damage;						//decrease human's strength by the damage
			
			if(this.strength <= 0) {						//check if the strength is 0 or less than 0
				this.strength = 0;							//set strength to 0
			}
		}
		
	}

}

