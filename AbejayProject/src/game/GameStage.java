	/***********************************************************	*  
	* This class is responsible for the Graphical User Interface
	* of the application/game.
	* 
	* The class creates the different scenes in the application:
	* the welcome screen, the game scene, the instructions scene,
	* the about scene, and the game over scene.
	*
	* @author Rhys Allen Abejay
	* @created_date 2022-05-17 14:54
	***********************************************************/

package game;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import java.util.Random;

public class GameStage {
	private Stage stage;
	private Scene splashScene;						//welcome screen
	private Scene gameScene;						//game stage scene
	private Scene instructionsScene;				//instructions scene
	private Scene aboutScene;						//about scene
	private Scene gameOverScene;					//game over scene
	
	private Group root;
	private Canvas canvas;
	private GraphicsContext gc;
	private GameTimer gametimer;
	
	private Canvas howToCanvas;
	private Canvas aboutCanvas;
	private GraphicsContext howToGc;
	private GraphicsContext aboutGc;
	
	
	//Class constants
	final static int WINDOW_HEIGHT = 500;			
	final static int WINDOW_WIDTH = 800;
	
	//Scene backgrounds
	final static Image SPLASH_BG = new Image("images/splash-bg.png", GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT, true, true);
	final static Image GAME_BG = new Image("images/gamescene-bg.png", GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT, true, false);
	final static Image WIN_BG = new Image("images/gameover-win-bg.png", GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT, true, true);
	final static Image LOSE_BG = new Image("images/gameover-lose-bg.png", GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT, true, true);
	final static Image HOWTO_BG = new Image("images/howto-bg.png", GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT, true, true);
	final static Image ABOUT_BG = new Image("images/about-bg.png", GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT, true, true);
	
	//Title cards
	final static Image TITLE_CARD = new Image("images/title-card.png", GameStage.WINDOW_WIDTH*0.95, 200, true, true);
	final static Image WIN_CARD = new Image("images/win-card.png", GameStage.WINDOW_WIDTH*0.85, 200, true, false);
	final static Image LOSE_CARD = new Image("images/lose-card.png", GameStage.WINDOW_WIDTH*0.85, 200, true, false);
	final static Image GAMEOVER_WHITE_CARD = new Image("images/gameover-text-white.png", GameStage.WINDOW_WIDTH*0.95, 200, true, true);
	final static Image GAMEOVER_BLACK_CARD = new Image("images/gameover-text-black.png", GameStage.WINDOW_WIDTH*0.95, 200, true, true);
	
	//Icons
	final static Image TIMER_ICON = new Image("images/clock-image.png",25, 25, true, false);
	final static Image FROZEN_TIMER_ICON = new Image("images/frozen-clock-image.png",25, 25, true, true);
	final static Image SCORE_ICON = new Image("images/score-image.png",25, 25, true, true);
	final static Image STRENGTH_ICON = new Image("images/strength-image.png",25, 25, true, true);
	final static Image IMMORTAL_ICON = new Image("images/immortal-strength-image.png",25, 25, true, true);
	
	//Buttons
	final static Image NEW_GAME_BUTTON = new Image("images/new-game-btn.png", GameStage.WINDOW_WIDTH*0.25, 250, true, true);
	final static Image HOVERED_NEW_GAME_BUTTON = new Image("images/hovered-new-game-btn.png", GameStage.WINDOW_WIDTH*0.25, 250, true, true);
	final static Image HOW_TO_PLAY_BUTTON = new Image("images/how-to-play-btn.png", GameStage.WINDOW_WIDTH*0.25, 250, true, true);
	final static Image HOVERED_HOW_TO_PLAY_BUTTON = new Image("images/hovered-how-to-play-btn.png", GameStage.WINDOW_WIDTH*0.25, 250, true, true);
	final static Image ABOUT_BUTTON = new Image("images/about-btn.png", GameStage.WINDOW_WIDTH*0.25, 250, true, true);
	final static Image HOVERED_ABOUT_BUTTON = new Image("images/hovered-about-btn.png", GameStage.WINDOW_WIDTH*0.25, 250, true, true);
	final static Image EXIT_BUTTON = new Image("images/exit-btn.png", GameStage.WINDOW_WIDTH*0.25, 250, true, true);
	final static Image HOVERED_EXIT_BUTTON = new Image("images/hovered-exit-btn.png", GameStage.WINDOW_WIDTH*0.25, 250, true, true);
	final static Image MAIN_MENU_BUTTON = new Image("images/main-menu-btn.png", GameStage.WINDOW_WIDTH*0.25, 250, true, true);
	final static Image HOVERED_MAIN_MENU_BUTTON = new Image("images/hovered-main-menu-btn.png", GameStage.WINDOW_WIDTH*0.25, 250, true, true);
	final static Image PAUSE_BUTTON = new Image("images/pause-btn.png", 25, 25, true, false);
	final static Image HOVERED_PAUSE_BUTTON = new Image("images/hovered-pause-btn.png", 25, 25, true, false);
	final static Image PLAY_BUTTON = new Image("images/play-btn.png", 25, 25, true, false);
	final static Image HOVERED_PLAY_BUTTON = new Image("images/hovered-play-btn.png", 25, 25, true, false);
	final static Image HOME_BUTTON = new Image("images/home-btn.png", 25, 25, true, false);
	final static Image HOVERED_HOME_BUTTON = new Image("images/hovered-home-btn.png", 25, 25, true, false);
	final static Image CIRCLE_HOME_BUTTON = new Image("images/home-btn-2.png", 50, 50, true, true);
	final static Image CIRCLE_HOVERED_HOME_BUTTON = new Image("images/hovered-home-btn-2.png", 50, 50, true, true);
	
	//Fonts
	private final static Font TITLE_FONT = Font.font("Avenir", FontWeight.BLACK, 60);
	private final static Font HEADING_FONT = Font.font("Avenir", FontWeight.BOLD, 20);
	private final static Font SUBHEADING_FONT = Font.font("Avenir", FontWeight.SEMI_BOLD, 18);
	private final static Font NORMAL_FONT = Font.font("Avenir", FontWeight.NORMAL, 13);
	private final static Font STANDARD_FONT = Font.font("Avenir", FontWeight.NORMAL, 16);
	private final static Font SUBSTANDARD_FONT = Font.font("Avenir", FontWeight.NORMAL, 15);
	private final static Font BUTTON_FONT = Font.font("Avenir", FontWeight.NORMAL, 13);
	
	
	//the class constructor
	public GameStage() {
		this.root = new Group();
		this.gameScene = new Scene(root, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);	
		this.canvas = new Canvas(GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);	
		this.gc = canvas.getGraphicsContext2D();
		
		this.howToCanvas = new Canvas(GameStage.WINDOW_WIDTH-100,GameStage.WINDOW_HEIGHT);	
		this.howToGc = howToCanvas.getGraphicsContext2D();
		
		this.aboutCanvas = new Canvas(GameStage.WINDOW_WIDTH-80,GameStage.WINDOW_HEIGHT);	
		this.aboutGc = aboutCanvas.getGraphicsContext2D();
	}
	
	
	//Methods that modify splashScene
	
	
	//method that sets the current scene of the stage to splashScene
	public void setStage(Stage stage) {
		this.stage = stage;
		
		this.stage.setTitle("Stup!d Cup¡d");					//game title
		
		this.initSplash();										//initialize splashScene
		
		this.stage.setScene(this.splashScene);					//set the stage scene to welcome screen
		this.stage.setResizable(false);							//makes the window(stage) have fixed size
		this.stage.show();
	}
	
	
	//initializes splashScene
	private void initSplash() {
		StackPane root = new StackPane();
		root.getChildren().addAll(this.splashCanvas(), this.createButtonBox());
		this.splashScene = new Scene(root);
	}
	
	
	//initializes the canvas for the splashScene
	private Canvas splashCanvas() {
		Canvas canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		gc.drawImage(GameStage.SPLASH_BG, 0, 0);				//window background
		gc.drawImage(GameStage.TITLE_CARD, 20, 50);				//game title card
		return canvas;
	}
	
	
	//initializes the VBox for the splashScene
	private VBox createButtonBox() {
		VBox btnBox = new VBox();
		btnBox.setAlignment(Pos.BOTTOM_CENTER);
		btnBox.setPadding(new Insets(20));
		btnBox.setSpacing(5);		
		
		btnBox.getChildren().addAll(this.newGameBtn(), this.howToPlayBtn(), this.aboutBtn(), this.exitBtn());
		
		return btnBox;
	}
	
	
	//method that creates a new game button
	private ImageView newGameBtn() {
		ImageView newGame = new ImageView();
		newGame.setImage(GameStage.NEW_GAME_BUTTON);	
		
		newGame.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
                PauseTransition transition = new PauseTransition(Duration.seconds(0.5));
				transition.play();
				
				transition.setOnFinished(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent arg0) {
						System.out.println("Starting a new game...");	
						newGame(stage);											//changes the scene into the game scene
		                gametimer.setStartGame(System.nanoTime());				//sets the startGame attribute of gametimer to the current time when button is clicked
					}
				});
			}
		});
		
		newGame.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				newGame.setImage(GameStage.HOVERED_NEW_GAME_BUTTON);
			}
		});
		
		newGame.setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				newGame.setImage(GameStage.NEW_GAME_BUTTON);
			}
		});
		
		return newGame;
	}
	
	
	//method that creates a how to play button
	private ImageView howToPlayBtn() {
		ImageView howToPlay = new ImageView();
		howToPlay.setImage(GameStage.HOW_TO_PLAY_BUTTON);
		
		howToPlay.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				showHowTo(stage);										//changes the scene to the instructions scene
			}
		});
		
		howToPlay.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				howToPlay.setImage(GameStage.HOVERED_HOW_TO_PLAY_BUTTON);
			}
		});
		
		howToPlay.setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				howToPlay.setImage(GameStage.HOW_TO_PLAY_BUTTON);
			}
		});
		
		return howToPlay;
	}
	
	
	//method that creates an about button
	private ImageView aboutBtn() {
		ImageView about = new ImageView();
		about.setImage(GameStage.ABOUT_BUTTON);
		
		about.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				showAbout(stage);										//changes the scene to the about scene
			}
		});
		
		about.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				about.setImage(GameStage.HOVERED_ABOUT_BUTTON);
			}
		});
		
		about.setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				about.setImage(GameStage.ABOUT_BUTTON);
			}
		});
		
		return about;
	}
	
	
	//method that creates an exit button
	private ImageView exitBtn() {
		ImageView exit = new ImageView();
		exit.setImage(GameStage.EXIT_BUTTON);
		
		exit.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				System.out.println("Exiting game...");					
                System.exit(0);											//exit the application
			}
		});
		
		exit.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				exit.setImage(GameStage.HOVERED_EXIT_BUTTON);
			}
		});
		
		exit.setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				exit.setImage(GameStage.EXIT_BUTTON);
			}
		});
		
		return exit;
	}
	
	//Methods that modify gameOverScene
	
	
	//method that sets the current scene of the stage to gameOverScene
	void flashGameOver(int result, int score) {
		this.stage.setTitle("Game Over!");						//stage title
		
		this.initGameOver(result,score);					//initialize splashScene
		
		this.stage.setScene(this.gameOverScene);				//set the stage scene to game over scene
		this.stage.setResizable(false);							//makes the window(stage) have fixed size
		this.stage.show();
	}
	
	
	//initializes gameOverScene
	private void initGameOver(int result, int score) {
		StackPane root = new StackPane();
		root.getChildren().addAll(this.gameOverCanvas(result, score), this.createOptions());
		this.gameOverScene = new Scene(root);
	}
	
	
	//initializes canvas for gameOverScene
	private Canvas gameOverCanvas(int result, int score) {
		Canvas canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		Font scoreTextFont = Font.font("Avenir", FontWeight.BOLD, 30);
		Font scoreFont = Font.font("Avenir", FontWeight.BOLD, 60);
		
		//draw window background
		if(result == GameTimer.WIN_GAME) {
			gc.drawImage(GameStage.WIN_BG, 0, 0);							//window background
			gc.drawImage(GameStage.GAMEOVER_BLACK_CARD, 20, 50);
			gc.drawImage(GameStage.WIN_CARD, 60, 150);						//game result
			
			gc.setFont(scoreTextFont);		//score
			gc.setFill(Color.BLACK);
			gc.fillText("Score:", 315, 340);
			
			gc.setFont(scoreFont);
			gc.setFill(Color.BLACK);
			gc.fillText(score + "", 415, 350);
		}else {
			gc.drawImage(GameStage.LOSE_BG, 0, 0);							//window background
			gc.drawImage(GameStage.GAMEOVER_WHITE_CARD, 20, 50);
			gc.drawImage(GameStage.LOSE_CARD, 60, 150);						//game result
			
			gc.setFont(scoreTextFont);		//score
			gc.setFill(Color.WHITE);
			gc.fillText("Score:", 315, 340);
			
			gc.setFont(scoreFont);
			gc.setFill(Color.WHITE);
			gc.fillText(score + "", 415, 350);
		}		
		return canvas;
	}
	
	
	//initializes the HBox for gameOverScene
	private HBox createOptions() {
		HBox optnBox = new HBox();
		optnBox.setAlignment(Pos.BOTTOM_CENTER);
		optnBox.setPadding(new Insets(50));
		optnBox.setSpacing(30);
		
		ImageView mainMenu = new ImageView();
		mainMenu.setImage(GameStage.MAIN_MENU_BUTTON);	
		
		mainMenu.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				PauseTransition transition = new PauseTransition(Duration.seconds(0.5));
				transition.play();
				
				transition.setOnFinished(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent arg0) {
						setStage(stage);
						System.out.println("Going back home...");
					}
				});
			}
		});
		
		mainMenu.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				mainMenu.setImage(GameStage.HOVERED_MAIN_MENU_BUTTON);
			}
		});
		
		mainMenu.setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				mainMenu.setImage(GameStage.MAIN_MENU_BUTTON);
			}
		});
		
		optnBox.getChildren().addAll(this.newGameBtn(),mainMenu,this.exitBtn());
		
		return optnBox;
	}

	
	//Methods that modify gameScene
	
	
	//method that sets the current scene of the stage to gameScene
	private void newGame(Stage stage) {
		
		if(this.root.getChildren() != null) {						//checks if a game has been launched previously
			this.root.getChildren().removeAll(this.canvas);			//removes all nodes in root
			
			//Instantiate new root, scene, canvas, gc, and gametimer for the new game
			this.root = new Group();
			this.gameScene = new Scene(root, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);	
			this.canvas = new Canvas(GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);	
			this.gc = canvas.getGraphicsContext2D();
		}
		//instantiate an animation timer
		this.gametimer = new GameTimer(this.gc,this.gameScene, this);
		this.stage = stage;
		
		ImageView pause = new ImageView();
		pause.setImage(GameStage.PAUSE_BUTTON);	
		pause.setLayoutX(5);
		
		ImageView play = new ImageView();
		play.setImage(GameStage.PLAY_BUTTON);	
		play.setLayoutX(5);
		play.setVisible(false);
		
		ImageView home = new ImageView();
		home.setImage(GameStage.HOME_BUTTON);	
		home.setLayoutX(35);
		
		pause.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {	
				gametimer.pause();
				pause.setVisible(false);
				play.setVisible(true);
;			}
		});
		
		pause.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				pause.setImage(GameStage.HOVERED_PAUSE_BUTTON);
			}
		});
		
		pause.setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				pause.setImage(GameStage.PAUSE_BUTTON);
			}
		});
		
		
		play.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {	
				gametimer.play();
				play.setVisible(false);
				pause.setVisible(true);
			}
		});
		
		play.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				play.setImage(GameStage.HOVERED_PLAY_BUTTON);
			}
		});
		
		play.setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				play.setImage(GameStage.PLAY_BUTTON);
			}
		});
		
		home.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {	
				gametimer.stop();
				
				PauseTransition transition = new PauseTransition(Duration.seconds(0.5));
				transition.play();
				
				transition.setOnFinished(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent arg0) {
						setStage(stage);
						System.out.println("Going back home...");
					}
				});
				
			}
		});
		
		home.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				home.setImage(GameStage.HOVERED_HOME_BUTTON);
			}
		});
		
		home.setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				home.setImage(GameStage.HOME_BUTTON);
			}
		});
		
		this.root.getChildren().addAll(this.canvas, pause, play, home);
			
		this.stage.setTitle("Stup!d Cup¡d");						//game title
		this.stage.setScene(this.gameScene);						//set the stage scene to game scene
			
		this.gametimer.start();
		this.stage.setResizable(false);								//makes window(stage) have a fixed size
		this.stage.show();
	}

	
	//Methods that modify instructionsScene
	
	
	//method that sets the current scene of the stage to instructionsScene
	private void showHowTo(Stage stage) {
		
		this.stage = stage;
		
		this.stage.setTitle("How to Play Stup!d Cup¡d");			//window title
		
		this.initInstructions();									//initialize instructionsScene
		
		this.stage.setScene(this.instructionsScene);				//set the stage scene to instructions screen
		this.stage.setResizable(false);								//makes window(stage) have a fixed size
		this.stage.show();
	}
	
	
	//initializes instructionsScene
	private void initInstructions() {
		BorderPane root = new BorderPane();
		
		this.howToHome();
		
		root.setLeft(this.createNaviBar());
		root.setCenter(this.howToCanvas);
		
		this.instructionsScene = new Scene(root);
	}
	
	
	//method that redraws the background image of the canvas
	private void redrawBg() {
		GraphicsContext gc = this.howToGc;
		gc.clearRect(0, 0, GameStage.WINDOW_WIDTH-100,GameStage.WINDOW_HEIGHT);
		gc.drawImage(GameStage.HOWTO_BG, 0, 0);
		
	}
	
	
	//method that creates a VBox containing the buttons for navigation and home and new game buttons
	private VBox createNaviBar() {
		VBox root = new VBox();
		root.setMaxWidth(150);
		root.setBackground(new Background(new BackgroundFill(Color.HONEYDEW, null, null)));
		root.setSpacing(110);
		
		VBox vbox = new VBox();
		vbox.setSpacing(5);
		
		Button b1 = new Button("Stup!d Cup¡d");
		b1.setFont(GameStage.BUTTON_FONT);
		b1.setMaxWidth(Double.MAX_VALUE);
		
		b1.setOnAction(new EventHandler<ActionEvent>() {
			 @Override 
			 public void handle(ActionEvent e) {
	            howToHome();							//sets the canvas to the how to home page
	         }
		});
		
		Button b2 = new Button("Cupid");
		b2.setFont(GameStage.BUTTON_FONT);
		b2.setMaxWidth(Double.MAX_VALUE);
		
		b2.setOnAction(new EventHandler<ActionEvent>() {
			 @Override 
			 public void handle(ActionEvent e) {
	            howToCupid();							//sets the canvas to the cupid page
	         }
		});
		
		Button b3 = new Button("Humans");
		b3.setFont(GameStage.BUTTON_FONT);
		b3.setMaxWidth(Double.MAX_VALUE);
		
		b3.setOnAction(new EventHandler<ActionEvent>() {
			 @Override 
			 public void handle(ActionEvent e) {
	            howToHuman();							//sets the canvas to the human page
	         }
		});
		
		Button b4 = new Button("Power-Ups");
		b4.setFont(GameStage.BUTTON_FONT);
		b4.setMaxWidth(Double.MAX_VALUE);
		
		b4.setOnAction(new EventHandler<ActionEvent>() {
			 @Override 
			 public void handle(ActionEvent e) {
	            howToPowerUp();							//sets the canvas to the powerup page
	         }
		});
		
		Button b5 = new Button("Gameplay");
		b5.setFont(GameStage.BUTTON_FONT);
		b5.setMaxWidth(Double.MAX_VALUE);
		b5.setOnAction(new EventHandler<ActionEvent>() {
			 @Override 
			 public void handle(ActionEvent e) {
	            howToGameplay();						//sets the canvas to the gameplay page
	         }
		});
		
		Button b6 = new Button("Boss Human");
		b6.setFont(GameStage.BUTTON_FONT);
		b6.setMaxWidth(Double.MAX_VALUE);
		b6.setOnAction(new EventHandler<ActionEvent>() {
			 @Override 
			 public void handle(ActionEvent e) {
	            howToBoss();						//sets the canvas to the boss page
	         }
		});
	
		vbox.getChildren().addAll(b1,b5,b2,b3,b6,b4);
		root.getChildren().addAll(this.createHomeButton(), vbox, this.createNewGameBtn());
		return root;
	}
	
	
	//method that creates a home button in a stackpane
	private StackPane createHomeButton() {
		StackPane root = new StackPane();
		ImageView home = new ImageView();
		home.setImage(GameStage.CIRCLE_HOME_BUTTON);
		home.setLayoutX(50);
		home.setLayoutY(10);
		
		home.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {	
				PauseTransition transition = new PauseTransition(Duration.seconds(0.5));
				transition.play();
				
				transition.setOnFinished(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent arg0) {
						setStage(stage);
						
					}
				});
			}
		});
		
		home.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				home.setImage(GameStage.CIRCLE_HOVERED_HOME_BUTTON);
			}
		});
		
		home.setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				home.setImage(GameStage.CIRCLE_HOME_BUTTON);
			}
		});
		
		
		root.getChildren().add(home);
		
		return root;
	}
	
	
	//method that creates a new game button in a VBox
	private VBox createNewGameBtn() {
		VBox root = new VBox();
		root.setAlignment(Pos.BOTTOM_LEFT);
		
		ImageView newGame = new ImageView();
		Image buttonImage = new Image("images/new-game-btn.png", 100, 250, true, true);
		Image hoveredBtn = new Image("images/hovered-new-game-btn.png", 100, 250, true, true);
		
		newGame.setImage(buttonImage);	
		newGame.setLayoutX(0);
		newGame.setY(WINDOW_HEIGHT-buttonImage.getHeight()-10);
		
		newGame.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				PauseTransition transition = new PauseTransition(Duration.seconds(0.5));
				transition.play();
				
				transition.setOnFinished(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent arg0) {
						System.out.println("Starting a new game...");	
						newGame(stage);											//changes the scene into the game scene
		                gametimer.setStartGame(System.nanoTime());				//sets the startGame attribute of gametimer to the current time when button is clicked
					}
				});
			}
		});
		
		newGame.setOnMouseEntered(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				newGame.setImage(hoveredBtn);
			}
		});
		
		newGame.setOnMouseExited(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent e) {
				newGame.setImage(buttonImage);
			}
		});
		
		root.getChildren().add(newGame);
		return root;
	}
	
	
	//method that modifies the howToGc to show the home page of the how to play screen
	private void howToHome() {
		GraphicsContext gc = this.howToGc;
		
		Image cupid = Cupid.CUPID_IMAGE;
		Image arrow = Arrow.ARROW_IMAGE;
		Image fhuman = Human.FEMALE_HUMAN_IMAGE;
		Image mhuman = Human.MALE_HUMAN_IMAGE;
		
		Random r = new Random();
		Random s = new Random();
		Random t = new Random();
		
		int strength = r.nextInt(51)+100;
		int x1 = t.nextInt(321)+330;
		int x2 = t.nextInt(321)+330;
		
		String title = "Stup!d Cup¡d";
		String paragraph1 = "Cupid, the moment he existed, has been responsible for making humans fall in love with\nanother human being with his bow and arrow. He, however, found his job boring after a\nfew hundred years and decided to abandon his post, leaving many humans incapable of\nfalling in love.";
		String paragraph2 = "Zeus later discovers that Cupid has not been doing his job for a long time and calls on\nCupid. He incurs the wrath of Zeus and is to be sent to the world of the mortals until he\nfulfills his duty.";
		String paragraph3 = "Cupid arrives in the mortal world, shocked by the number of humans that are still single and\nunwed as a result of his foolish decisions.";
		String paragraph4 = "Help Cupid fulfill his duty by making the humans in the mortal world fall in love, one by one!";
		
		this.redrawBg();
		
		gc.drawImage(cupid, 20, 400);
		gc.drawImage(arrow, 90, 440);
		gc.drawImage(arrow, 300, 440);
		gc.drawImage(mhuman, x1, 400);
		gc.drawImage(fhuman, x2, 400);
		
		gc.setFont(GameStage.TITLE_FONT);
		gc.setFill(Color.BLACK);
		gc.fillText(title, 150, 70);
		
		gc.setFont(Cupid.DEFAULT_FONT);
		gc.setFill(Color.BLACK);
		gc.fillText(strength + "", 27, 406);
		gc.fillText((s.nextInt(11)+30) + "",(x1+20) , 400);
		gc.fillText((s.nextInt(11)+30) + "",(x2+20) , 400);
				
		gc.setFont(GameStage.STANDARD_FONT);
		gc.setFill(Color.BLACK);
		gc.fillText(paragraph1, 20, 130);
		gc.fillText(paragraph2, 20, 230);
		gc.fillText(paragraph3, 20, 310);
		gc.fillText(paragraph4, 20, 370);
			
	}
	
	
	//method that modifies the howToGc to show the gameplay page of the how to play screen
	private void howToGameplay() {
		GraphicsContext gc = this.howToGc;
		
		Image heart = new Image("images/heart.png", 30, 30, true, true);
		Image timer = new Image("images/clock-image.png",40, 40, true, true);
		Image score =  new Image("images/score-image.png",40, 40, true, true);;
		Image health =  new Image("images/strength-image.png",40, 40, true, true);;
		Image cupid = Cupid.CUPID_IMAGE;
		Image fhuman = Human.FEMALE_HUMAN_IMAGE;
		Image mhuman = Human.MALE_HUMAN_IMAGE;
		Image arrow = Arrow.ARROW_IMAGE;
		
		Random r = new Random();
		Random s = new Random();
		Random t = new Random();
		Random u = new Random();
		
		int strength = r.nextInt(51)+100;
		int runtime = u.nextInt(61);
		int x1 = t.nextInt(561)+90;
		int x2 = t.nextInt(561)+90;
		int x3 = t.nextInt(561)+90;
		int x4 = t.nextInt(561)+90;
		int x5 = t.nextInt(561)+90;
		
		String title = "Gameplay";
		String line1 = "The game revolves with Cupid and a swarm of single humans that are in need of finding love.";
		String line2 = "Cupid’s strength is randomized from 100-150 and may increase when Cupid collects a Heart\nor decrease when a human hits Cupid. Cupid’s score increases when the arrow shot by Cupid\nhits a human and the human’s strength goes down to zero.";
		String line3 = "The game runs for one minute or when Cupid’s strength has gone down to zero. Help Cupid\nmake many humans fall in love within the time limit and preserve Cupid’s strength! Good luck!";

		this.redrawBg();
		
		gc.drawImage(cupid, 20, 110);
		gc.drawImage(mhuman, x1, 110);
		gc.drawImage(fhuman, x2, 110);
		gc.drawImage(mhuman, x3, 110);
		gc.drawImage(mhuman, x4, 110);
		gc.drawImage(fhuman, x5, 110);
		gc.drawImage(heart, 70, 295);
		gc.drawImage(cupid, 20, 275);
		gc.drawImage(cupid, 250, 275);
		gc.drawImage(fhuman, 285, 275);
		gc.drawImage(fhuman, 530, 275);
		gc.drawImage(cupid, 400, 275);
		gc.drawImage(timer, 30, 420);			
		gc.drawImage(score, 250, 420);
		gc.drawImage(health, 500, 420);
		gc.drawImage(arrow, 500, 310);
		
		gc.setFont(GameStage.TITLE_FONT);
		gc.setFill(Color.BLACK);
		gc.fillText(title, 30, 60);
		
		gc.setFont(Cupid.DEFAULT_FONT);
		gc.setFill(Color.BLACK);
		gc.fillText(strength + "", 27, 116);
		gc.fillText((s.nextInt(11)+30) + "",(x1+20) , 110);
		gc.fillText((s.nextInt(11)+30) + "",(x2+20) , 110);
		gc.fillText((s.nextInt(11)+30) + "",(x3+20) , 110);
		gc.fillText((s.nextInt(11)+30) + "",(x4+20) , 110);
		gc.fillText((s.nextInt(11)+30) + "",(x5+20) , 110);
		gc.fillText(strength + "", 27, 281);
		gc.fillText((strength-38) + "", 268, 271);
		gc.fillText("0", 320, 275);
		gc.fillText(strength +"", 407, 281);
		gc.fillText("0", 565, 275);
		
		gc.setFill(Color.BLUEVIOLET);
		gc.fillText("Strength x2!", 70, 285);
		gc.fillText("Score+1", 445, 281);
		
		gc.setFill(Color.RED);
		gc.fillText(strength + "", 257, 281);
		gc.fillText("3̵8̵", 305, 275);
		gc.fillText("3̵8̵", 550, 275);
		
		gc.setFont(GameStage.STANDARD_FONT);
		gc.setFill(Color.BLACK);
		gc.fillText(line1, 20, 95);
		gc.fillText(line2, 20, 215);  
		gc.fillText(line3, 20, 380);
		
		gc.setFont(GameStage.HEADING_FONT);
		gc.setFill(Color.BLACK);
		
		if(runtime < 10) {
			gc.fillText("0:0"+ runtime, 75, 450);
		}else if (runtime < 60 && runtime >= 10){
			gc.fillText("0:"+ runtime, 75, 450);
		}else if (runtime == 60){
			gc.fillText("1:00", 75, 450);
		}
		
		gc.fillText("20", 295, 450);
		gc.fillText(strength + "", 545, 450);

	}
	
	
	//method that modifies the howToGc to show the cupid page of the how to play screen
	private void howToCupid() {
		GraphicsContext gc = this.howToGc;
		
		Image cupid = new Image("images/cupid.png",120,120,true,true);
		Image arrow = new Image("images/arrow.png", 70, 70, true, true);
		Image keys = new Image("images/keys.png", 256, 80, true, true);
		Image spacebar = new Image("images/spacebar.png", 256, 80, true, true);
		
		Random r = new Random();
		
		int strength = r.nextInt(51)+100;
		
		String title = "Cupid";
		String s1 = "Cupid is the playable character in the game. He has a randomized strength attribute, which\nalso serves as the damage of the arrows it shoots.";
		String s2 = "Cupid can move UP, DOWN, LEFT,\nand RIGHT when the same keyboard\nkeys are pressed.";
		String s3 = "Cupid can shoot ARROWS when\nthe SPACEBAR is pressed.";
		String s4 = "The goal is to shoot ARROWS to as many HUMANS as you can and make sure that Cupid’s\nstrength does not go below zero within the time limit.";
		String s5 = "You WIN if Cupid’s strength is not zero by the end of the time limit, and you LOSE the\nmoment Cupid’s strength drops to zero.";

		this.redrawBg();
		
		gc.drawImage(cupid, 20, 70);
		gc.drawImage(arrow, 130, 135);
		gc.drawImage(arrow, 300, 135);
		gc.drawImage(arrow, 450, 135);
		gc.drawImage(arrow, 600, 135);
		gc.drawImage(keys, 60, 235);
		gc.drawImage(spacebar, 380, 235);
		
		gc.setFont(GameStage.TITLE_FONT);
		gc.setFill(Color.BLACK);
		gc.fillText(title, 30, 60);
		
		gc.setFont(GameStage.HEADING_FONT);
		gc.setFill(Color.BLACK);
		gc.fillText(strength + "", 100, 90);
		
		gc.setFont(GameStage.STANDARD_FONT);
		gc.setFill(Color.BLACK);
		gc.fillText(s1, 20, 210);
		gc.fillText(s2, 60, 320);
		gc.fillText(s3, 390, 320);
		gc.fillText(s4, 20, 395);
		gc.fillText(s5, 20, 450);
	}
	
	
	//method that modifies the howToGc to show the human page of the how to play screen
	private void howToHuman() {
		GraphicsContext gc = this.howToGc;
		
		Image human1 = new Image("images/male-human.png",120,120,true,true);
		Image human2 = new Image("images/female-human.png",120,120,true,true);
		Image cupid = Cupid.CUPID_IMAGE;
		Image arrow = Arrow.ARROW_IMAGE;
		Image fhuman = Human.FEMALE_HUMAN_IMAGE;
		Image mhuman = Human.MALE_HUMAN_IMAGE;
		
		Random r = new Random();
		Random s = new Random();
		Random t = new Random();
		
		int strength = r.nextInt(51)+100;
		int x1 = t.nextInt(271)+380;
		int x2 = t.nextInt(271)+380;
		int x3 = t.nextInt(501)+150;
		int x4 = t.nextInt(501)+150;
		
		String title = "Humans";
		String s1 = "Humans are Cupid's targets. Cupid's goal is to shoot arrows\nto the humans to make them fall in love.";
		String s2 = "When an arrow hits a human, the human's strength is decreased. Cupid's score increases\nwhen the human's strength goes down to 0, and disappears.";
		String s3 = "When a human hits Cupid, Cupid's strength decreases by the human's life, and the human's\nlife decreases by cupid's strength. The position of Cupid will change depending on the\nposition of the human it hit. Avoid getting hit by a human as much as possible!";
		
		this.redrawBg();
		
		gc.drawImage(human1, 20, 65);
		gc.drawImage(human2, 600, 65);
		gc.drawImage(cupid, 20, 230);  //250
		gc.drawImage(arrow, 90, 260);
		gc.drawImage(arrow, 300, 260);		
		gc.drawImage(fhuman, 330, 230);
		gc.drawImage(mhuman, x1, 230);
		gc.drawImage(fhuman, x2, 230);
		gc.drawImage(cupid, 20, 400);
		gc.drawImage(fhuman, 55, 400);
		gc.drawImage(mhuman, x3, 400);
		gc.drawImage(fhuman, x4, 400);
		
		gc.setFont(GameStage.TITLE_FONT);
		gc.setFill(Color.BLACK);
		gc.fillText(title, 30, 60);

		gc.setFont(GameStage.STANDARD_FONT);
		gc.setFill(Color.BLACK);
		gc.fillText(s1, 140, 125);
		gc.fillText(s2, 20, 200);
		gc.fillText(s3, 20, 335);
		
		gc.setFont(Cupid.DEFAULT_FONT);
		gc.setFill(Color.BLACK);
		gc.fillText(strength +"", 27, 236);
		gc.fillText("0", 365, 230);
		gc.fillText((s.nextInt(11)+30) +"", (x1+20), 230);
		gc.fillText((s.nextInt(11)+30) +"", (x2+20), 230);
		gc.fillText((strength-38) + "", 38, 416);
		gc.fillText("0", 90, 400);
		gc.fillText((s.nextInt(11)+30) +"", (x3+20), 400);
		gc.fillText((s.nextInt(11)+30) +"", (x4+20), 400);
		
		gc.setFill(Color.BLUEVIOLET);
		gc.fillText("Score+1", 70, 236);
		
		gc.setFill(Color.RED);
		gc.fillText("3̵8̵", 350, 230);	
		gc.fillText(strength + "", 27, 406);
		gc.fillText("3̵8̵", 75, 400);
	}
	
	
	//method that modifies the howToGc to show the powerup page of the how to play screen
	private void howToPowerUp() {
		GraphicsContext gc = this.howToGc;
		Image pup1 = new Image("images/heart.png", 60, 60, true, true);
		Image pup2 = new Image("images/wings.png", 60, 60, true, true);
		Image pup3 = new Image("images/dolphin.png", 60, 60, true, true);
		Image pup4 = new Image("images/autopilot.png", 60, 60, true, true);
		Image pup5 = new Image("images/snowflake.png", 60, 60, true, true);
		
		String title = "Power-Ups";
		String s1 = "Power-ups are items Cupid can collect in the game that\nchanges Cupid's stats depending on the type. Power-ups\nare spawned every 10 seconds and disappear when\nuncollected for five seconds.";
		String heart = "Heart";
		String heartDes = "Heart doubles the strength of Cupid.";
		String wings = "Wings";
		String wingsDes = "Wings provides temporary immortality\nto Cupid. The effect lasts for 5 seconds.";
		String dolphin = "Dolphin";
		String dolphinDes = "Dolphin makes Cupid's movement\nspeed faster. The effect lasts for 8 seconds.";
		String autopilot = "Autopilot";
		String autopilotDes = "Autopilot makes Cupid automatically\nshoot arrows for 4 seconds.";
		String snowflake = "Snowflake";
		String snowflakeDes = "Snowflake freezes the time for\n3 seconds.";
		
		this.redrawBg();
		gc.drawImage(pup1, 30, 70);
		gc.drawImage(pup2, 100, 70);
		gc.drawImage(pup3, 170, 70);
		gc.drawImage(pup4, 60, 130);
		gc.drawImage(pup5, 130, 130);
		
		gc.drawImage(pup1, 20, 210);
		gc.drawImage(pup2, 20, 310);
		gc.drawImage(pup3, 20, 410);
		gc.drawImage(pup4, 360, 260);
		gc.drawImage(pup5, 360, 360);
		
		
		gc.setFont(GameStage.TITLE_FONT);
		gc.setFill(Color.BLACK);
		gc.fillText(title, 30, 60);
		
		gc.setFont(GameStage.STANDARD_FONT);
		gc.setFill(Color.BLACK);
		gc.fillText(s1, 240, 100);
		
		gc.setFont(GameStage.SUBSTANDARD_FONT);
		gc.setFill(Color.BLACK);
		gc.fillText(heartDes, 85, 255);
		gc.fillText(wingsDes, 85, 345);
		gc.fillText(dolphinDes, 85, 445);
		gc.fillText(autopilotDes, 425, 295);
		gc.fillText(snowflakeDes, 425,395);
		
		gc.setFont(GameStage.HEADING_FONT);
		gc.setFill(Color.BLACK);
		gc.fillText(heart, 85, 240);
		gc.fillText(wings, 85, 330);
		gc.fillText(dolphin, 85, 430);	
		gc.fillText(autopilot, 425, 280);	
		gc.fillText(snowflake, 425, 380);	
	}
	
	
	///method that modifies the howToGc to show the human boss page of the how to play screen
	private void howToBoss() {
		GraphicsContext gc = this.howToGc;
		
		Image human1 = new Image("images/boss-male.png",120,120,true,true);
		Image human2 = new Image("images/boss-female.png",120,120,true,true);
		Image human3 = new Image("images/boss-male.png",150,150,true,true);
		Image human4 = new Image("images/boss-female.png",150,150,true,true);
		Image human5 = Human.FEMALE_HUMAN_IMAGE;
		Image human6 = Human.MALE_HUMAN_IMAGE;
		
		Image cupid = Cupid.CUPID_IMAGE;
		
		Random a = new Random();
		boolean b = a.nextBoolean();
		
		String title = "Boss Human";
		String s1 = "When the elapsed time in the game has reached 30 seconds,\nthe Boss Human will appear at the rightmost side of the screen.\nUnlike normal humans, it has an initial health of 3000 and a\nfixed strength 50.";
		String s2 = "A Boss Human appear much bigger compared to normal humans. In addition, it spawns\nthree Lackey Humans, Humans with strength 50, in 5-second intervals while it is still alive.\nThe game also enters Frenzy mode while the Boss Human is alive. Be careful of hitting\nthe Boss Human or a huge drop in Cupid strength will happen!";
		
		this.redrawBg();
		
		gc.drawImage(human1, 20, 65);
		gc.drawImage(human2, 580, 65);
		
		if(b) {
			gc.drawImage(human3, 580, 320);
		}else {
			gc.drawImage(human4, 580, 320);
		}
		
		gc.drawImage(cupid, 20, 350);
		gc.drawImage(human5, 230, 300);
		gc.drawImage(human6, 350, 350);
		gc.drawImage(human5, 500, 400);
		
		gc.setFont(GameStage.TITLE_FONT);
		gc.setFill(Color.BLACK);
		gc.fillText(title, 30, 60);
		
		gc.setFont(GameStage.STANDARD_FONT);
		gc.setFill(Color.BLACK);
		gc.fillText(s1, 120, 110);
		gc.fillText(s2, 20, 220);
		
		
		gc.setFont(Cupid.DEFAULT_FONT);
		gc.setFill(Color.BLACK);
		gc.fillText("150", 27, 356);
		gc.fillText("50", 250, 300);
		gc.fillText("50", 370, 350);
		gc.fillText("50", 520, 400);
		gc.fillText("3000", 600, 320);
	}
	
	
	//Methods that modify aboutScene
	

	//method that sets the current scene of the stage to aboutScene
	private void showAbout(Stage stage) {
		this.stage = stage;
		
		this.stage.setTitle("About");							//window title
		
		this.initAbout();										//initialize aboutScene
		
		this.stage.setScene(this.aboutScene);					//set the stage scene to the about screen
		this.stage.setResizable(false);							//makes window(stage) have a fixed size
		this.stage.show();
	}
	
	
	//initializes aboutScene
	private void initAbout() {		
		BorderPane root = new BorderPane();
		
		this.aboutHome();
		
		root.setLeft(this.navigationBar());
		root.setCenter(this.aboutCanvas);
		
		this.aboutScene = new Scene(root);
	}
	
	
	//method that redraws the background image of the canvas
	private void redrawBackground() {
		GraphicsContext gc = this.aboutGc;
		gc.clearRect(0, 0, GameStage.WINDOW_WIDTH-100,GameStage.WINDOW_HEIGHT);
		
		//change bg later
		gc.drawImage(GameStage.ABOUT_BG, 0, 0);
	}
	
	
	//method that creates a VBox containing the navigation buttons and the home button
	private VBox navigationBar() {
		VBox root = new VBox();
		
		root.setMaxWidth(150);
		root.setBackground(new Background(new BackgroundFill(Color.MISTYROSE, null, null)));
		root.setSpacing(150);
		
		VBox vbox = new VBox();
		vbox.setSpacing(5);
		
		Button b1 = new Button("About the\nDeveloper");
		b1.setFont(GameStage.BUTTON_FONT);
		b1.setMaxWidth(Double.MAX_VALUE);
		
		b1.setOnAction(new EventHandler<ActionEvent>() {
			 @Override 
			 public void handle(ActionEvent e) {
	            aboutHome();							//sets the canvas to the about home page
	         }
		});
		
		Button b2 = new Button("Credits &\nReferences");
		b2.setFont(GameStage.BUTTON_FONT);
		b2.setMaxWidth(Double.MAX_VALUE);
		
		b2.setOnAction(new EventHandler<ActionEvent>() {
			 @Override 
			 public void handle(ActionEvent e) {
	            showCredits();							//sets the canvas to the credits page
	         }
		});
		
		
		vbox.getChildren().addAll(b1,b2);
		root.getChildren().addAll(this.createHomeButton(), vbox);
		return root;
	}
	
	
	//method that modifies the aboutGc to show the home page of the about screen
	private void aboutHome() {
		GraphicsContext gc = this.aboutGc;
		
		Image me1 = new Image("images/me1.png", 227, 170, true, true);
		Image me2 = new Image("images/me2.png", 227, 170, true, true);
		Image me3 = new Image("images/me3.png", 227, 170, true, true);
		
		String title = "About the Developer";
		String name = "Rhys Allen Abejay";
		String desc = "2nd Year, BS Statistics";
		
		String para1 = "I am the developer of this game “Stup!d Cup¡d,” which came to fruition as my mini-project and final requirement\nfor the course CMSC 22. I have always been curious on how do computer applications, like computer games are\nbuilt through code, so I took this opportunity to try and get my hands with crafting a game myself.";
		String para2 = "When I was thinking of what theme I should use, some of the themes I thought of were “Vaccine and COVID-19”\nand “Immune System at Work!”. I eventually went for the current theme Cupid and Humans as I found the\nconcept cute and adorable.";
		String para3 = "Looking back, I think this is the school requirement that I was really absorbed in doing that I found it very\nsurprising that an hour has passed since I began coding a part of the application. I spent the past few weeks\npulling all-nighters so that I can accomplish my goals for the game for the day, which I believe is a first for me.";
		String para4 = "I hope that you enjoy playing this game, like how I enjoyed creating it. :-)";
		
		this.redrawBackground();
		
		gc.drawImage(me1, 18, 75);
		gc.drawImage(me2, 248, 75);
		gc.drawImage(me3, 478, 75);
		
		gc.setFont(GameStage.TITLE_FONT);
		gc.setFill(Color.BLACK);
		gc.fillText(title, 30, 60);
		
		gc.setFont(GameStage.HEADING_FONT);
		gc.setFill(Color.BLACK);
		gc.fillText(name, 30, 270);
		
		gc.setFont(GameStage.SUBHEADING_FONT);
		gc.setFill(Color.BLACK);
		gc.fillText(desc, 210, 270);
		
		gc.setFont(GameStage.NORMAL_FONT);
		gc.setFill(Color.BLACK);
		gc.fillText(para1, 30, 290);
		gc.fillText(para2, 30, 350);
		gc.fillText(para3, 30, 410);
		gc.fillText(para4, 30, 470);
	}
	
	
	//method that modifies the aboutGc to show the credits page of the about screen
	private void showCredits() {
		GraphicsContext gc = this.aboutGc;
		
		String title = "Credits & References";
		String heading1 = "Base Code";
		String heading2 = "Images used in the Game";
		String subhead1 = "Scene Backgrounds";
		String subhead2 = "Game Characters and Items";
		String subhead3 = "Icons and Buttons";
		
		String text1 = "The base code used in building this game was provided by our CMSC 22 Laboratory CMSC 22 Laboratory\nProfessor, Ms. Mylah Ristie Anacleto.";
		String text2 = "About, How To Play, Game Over - Win backgrounds are created Gradienta, sourced from Canva.";
		String text3 = "Game Over - Lose background is created by Ling, sourced from Canva.";
		String text4 = "Main Menu background is created using works by Clker-Free-Vector Images, Sketchify, Digital Silk, sparklestroke,\nMiracle82, and Mositron, sourced from Canva.";
		String text5 = "Game background is created using works by OpenClipart-Vectors, Sketchify, a-arrow, GDJ, and Harry Cooke,\nsourced from Canva.";
		String text6 = "Cupid images are created using works by sweetpocket and Canva, sourced from Canva.";
		String text7 = "Male and female human & boss human images are created by Sketchify Korea, sourced from Canva.";
		String text8 = "Heart, Wings, Dolphin, Autopilot, and Snowflake power-up and Arrow images are created using design assets and\nimages in Adobe Creative Cloud Express";
		String text9 = "Pause and Play image button is created by roundicon; Home image button is created by hqrloveq, sourced from\nflaticon. Timer, Score, and Strength icons are created by freepik, sourced from flaticon.";
		String text10 = "Modified icons are used using works by freepik and justicon, sourced from flaticon. The remaining buttons in the\napplication are created using design assets in Adobe Creative Cloud Express.";
		
		this.redrawBackground();
		
		gc.setFont(GameStage.TITLE_FONT);
		gc.setFill(Color.BLACK);
		gc.fillText(title, 30, 60);
		
		gc.setFont(GameStage.HEADING_FONT);
		gc.setFill(Color.BLACK);
		gc.fillText(heading1, 30, 85);
		gc.fillText(heading2, 30, 150);
		
		gc.setFont(GameStage.NORMAL_FONT);
		gc.setFill(Color.BLACK);
		gc.fillText(text1, 30, 105);
		gc.fillText(text2, 30, 190);
		gc.fillText(text3, 30, 210);
		gc.fillText(text4, 30, 230);
		gc.fillText(text5, 30, 270);
		gc.fillText(text6, 30, 330);
		gc.fillText(text7, 30, 350);
		gc.fillText(text8, 30, 370);
		gc.fillText(text9, 30, 430);
		gc.fillText(text10, 30, 470);
		
		gc.setFont(GameStage.SUBHEADING_FONT);
		gc.setFill(Color.BLACK);
		gc.fillText(subhead1, 30, 170);
		gc.fillText(subhead2, 30, 310);
		gc.fillText(subhead3, 30, 410);
	}
			
}

