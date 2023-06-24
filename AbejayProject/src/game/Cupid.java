	/***********************************************************	* 
	* The class defines the attributes and methods of Cupid, the 
	* playable character in the game.
	* 
	* The class inherits from the Sprite class.
	* 
	* The Cupid has a randomized initial strength attribute and can
	* move when the up, down, left, or right keys are pressed. It
	* also has a randomized initial y value. 
	* 
	* Defined in this class are attributes that determines if the 
	* Cupid is immortal or has an upgraded movement speed.
	* 
	* Defined in this class is the move attribute which changes the 
	* x and y attribute of Cupid, depending on when the left and right, 
	* or up and down keys are pressed. The method also checks if
	* the Cupid's x position is at the leftmost/rightmost side of the screen
	* and if its y position is at the topmost or bottom-most side of the screen.
	*
	* @author Rhys Allen Abejay
	* @created_date 2022-05-17 14:54
	***********************************************************/

package game;

import java.util.ArrayList;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.canvas.GraphicsContext;


class Cupid extends Sprite{
	private String name;										
	private int strength;									//cupid's health and damage it can inflict on colliding human
	private int score = 0;									//cupid's score when its arrow hits a human and falls in love
	private ArrayList<Arrow> arrows;
	
	private boolean alive;							
	private boolean immortal;								//cupid's health will not decrease if it is immortal
	private boolean fast;									//whether cupid's movement speed is upgraded or not
	private boolean autopilot;								//whether cupid is automatically shooting arrows
	private boolean frozeTime;								//whether cupid froze the time
	
	//Class constants
	final static int NORMAL_SPEED = 10;					//normal cupid's movement speed
	final static int UPGRADED_SPEED = 13;				//upgraded cupid's movement speed
	
	final static int CUPID_WIDTH = 70;					//width of the cupid image
	
	final static Font DEFAULT_FONT = Font.font("Microsoft Sans Serif", FontWeight.BOLD, 12);
	final static Image CUPID_IMAGE = new Image("images/cupid.png",Cupid.CUPID_WIDTH,Cupid.CUPID_WIDTH,true,true);
	private final static Image GLOWING_CUPID_IMAGE = new Image("images/glowing-cupid.png",Cupid.CUPID_WIDTH,Cupid.CUPID_WIDTH,true,true);
	
	
	Cupid(String name, int x, int y){
		super(x,y);
		this.name = name;
		
		Random r = new Random();
		this.strength = r.nextInt(51)+100;
		
		this.alive = true;
		this.fast  = false;
		this.immortal = false;
		this.autopilot = false;
		this.frozeTime = false;
	
		this.arrows = new ArrayList<Arrow>();
		this.loadImage(Cupid.CUPID_IMAGE);
		
		System.out.println("CUPID's initial strength/health: " + this.getStrength());
	}

	//getters
	boolean isAlive(){
		if(this.alive) return true;
		return false;
	} 
	
	String getName(){
		return this.name;
	}
	
	//method that will get the arrows 'shot' by the cupid
	ArrayList<Arrow> getArrows(){
		return this.arrows;
	}
	
	boolean isFast() {
		return this.fast;
	}
	
	boolean isImmortal() {
		return this.immortal;
	}
	
	int getStrength() {
		return this.strength;
	}
		
	int getScore() {
		return this.score;
	}
	
	boolean isAutopilot() {
		return this.autopilot;
	}
	
	boolean	didFreezeTime() {
		return this.frozeTime;
	}
	
	
	//setters
	private void die(){							//method called when health is 0
    	this.alive = false;					
    }
	
	void doubleStrength() {						//method called when Heart is collected
		this.strength = this.strength*2;
	}
	
	void setImmortal(boolean value) {			//method called to change the immortal attribute of Cupid
		this.immortal = value;
		
		if(value == true) {
			this.setImage(Cupid.GLOWING_CUPID_IMAGE);		//change the cupid's image
		}else {
			this.setImage(Cupid.CUPID_IMAGE);				//revert to normal
		}
	}
	
	void setFast(boolean value) {
		this.fast = value;
	}
	
	void updateScore() {									//method to increment the cupid's score
		this.score++;
		
		//Print prompt
		System.out.println("CUPID's ARROW successfully makes a HUMAN fall in LOVE! CUPID's score: " + this.getScore());
	}
	
	void setAutopilot(boolean value) {
		this.autopilot = true;
	}
	
	void setFrozeTime(boolean value) {
		this.frozeTime = value;
	}
	
	
	//method that changes the cupid's image
	private void setImage(Image image) {
		this.img = image;
	}
	
	
	//method called if spacebar is pressed
	void shoot(){
		//compute for the x and y initial position of the arrow
		int x = (int) (this.x + this.width+20);
		int y = (int) (this.y + this.height/2);
		
		Arrow a = new Arrow(x,y, this.getStrength());
		this.arrows.add(a);
    }
	
	
	//method called if up/down/left/right arrow key is pressed.
	void move() {		
		int tempX = this.x + this.dx;						
		int tempY = this.y + this.dy;
		
		boolean rBounds = this.isRightBounds(tempX);
		boolean bBounds = this.isBottomBounds(tempY);
		
	
		if((tempX > 0) && (rBounds)) {					//check if the incremented x attribute of cupid is greater than 0 and less than the window width
			this.x += this.dx;							//increment x 
		}
		
		if((tempY > GameTimer.GAME_STATUS_BAR_HEIGHT) && (bBounds)) {					//check if the incremented y attribute of cupid is greater than 0 and less than the window height
			this.y += this.dy;							//increment y
		}
	
	}
	
	
	//method that checks if the cupid is at the rightmost side of the window
	private boolean isRightBounds(int num) {
		int temp = num + Cupid.CUPID_WIDTH;
		
		if(temp < GameStage.WINDOW_WIDTH) {				//checks if the cupid, when x is incremented, is less than the window width
			return true;								//cupid is not yet at the rightmost side of the window
		}else {
			return false;								//cupid is at the rightmost side of the window
		}
	
	}
	
	
	//method that checks if the cupid is at the bottom-most side of the window
	private boolean isBottomBounds(int num) {
		int temp = num + Cupid.CUPID_WIDTH;
		
		if(temp < GameStage.WINDOW_HEIGHT) {			//checks if the cupid, when y is incremented, is less than the window height
			return true;								//cupid is not yet at the bottom-most part of the window
		}else {
			return false;								//cupid is at the bottom-most part of the window
		}
	
	}
	
	
	//method to update the cupid's strength/life
	void updateStrength(int value) {
		this.strength -= value;							//increment/decrement strength attribute by value
		
		if(strength <= 0) {								//check if strength is less than or equal 0
			this.strength = 0;							//set strength to 0
			this.die();									//call the die method
		}
		
	}

	
	//overrides the render method of sprite, includes text showing the cupids's strength
	@Override
	void render(GraphicsContext gc) {
		super.render(gc);
		gc.setFont(Cupid.DEFAULT_FONT);		
		gc.setFill(Color.WHITE);
		
		if(this.getStrength() >= 100) {
			gc.fillText(this.getStrength() + "", this.getX()+7, this.getY()+6);
		}else if(this.getStrength() >= 10 && this.getStrength() < 100) {
			gc.fillText(this.getStrength() + "", this.getX()+12, this.getY()+6);
		}else {
			gc.fillText(this.getStrength() + "", this.getX()+18, this.getY()+6);
		}
		
	}
	
	
	//method called when the cupid has hit a human
	//changes the x position of cupid
	//if the cupid's x position is less than the human's x position, the x attribute is decreased
	//if the cupid's x position is greater than the human's x position, the x attribute is increased
	void bounce(int xPos) {
		if(this.getX() < xPos) {
			if(this.getX() - 50 > 0) {
				this.x -= 50;
			}else {
				this.x = 1;
			}
		}else {
			if(this.getX() + Cupid.CUPID_WIDTH + 50 < GameStage.WINDOW_WIDTH) {
				this.x += 50;
			}else {
				this.x = (GameStage.WINDOW_WIDTH - Cupid.CUPID_WIDTH) + 1;
			}
		}
	}
	
}
