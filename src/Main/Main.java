package Main;

import java.awt.Color;
import java.util.*;

import Data.Vector2D;
import Data.spriteInfo;
import Data.BoundBox;
import FileIO.EZFileRead;
import logic.Control;
import timer.stopWatchX;

public class Main{
	// Fields (Static) below...

                                  /*Text Fields*/
	public static EZFileRead ezr = new EZFileRead("Dialogue.txt"); // File to retrieve Dialogue lines from
	public static HashMap<String,String> textMap = new HashMap<>(); //HashMap to hold text
    public static stopWatchX textTimer = new stopWatchX(4000); //Text Stop Watch Timer
    public static String currentText = ""; // Variable to hold current Dialogue

                                /*Sprite Fields*/
    public static stopWatchX spriteTimer = new stopWatchX(120); //Sprite Stop Watch Timer

    //Background Image of Grass
    public static spriteInfo background = new spriteInfo(new Vector2D(0,0),"Grass");
    // Boundary Image for Walls
    public static spriteInfo backgroundWalls = new spriteInfo(new Vector2D(0,0),"TreeWalls");
    // Boundary Image for Roof
    public static spriteInfo backgroundRoof = new spriteInfo(new Vector2D(0,0),"TreeRoof");
    //Player Sprite
    public static spriteInfo player = new spriteInfo(new Vector2D(480,352),"FR0",480,352);
    //Banana -- Interactable Object
    public static spriteInfo banana = new spriteInfo(new Vector2D(300,375),"Banana",
                                                     new BoundBox(300,330,375,405));
    //Tree -- Interactable Object
    public static spriteInfo tree = new spriteInfo(new Vector2D(600, -100),"Tree",
                                                   new BoundBox(800,910,80,400));
    //Queue to hold Sprites
    public static ArrayList<spriteInfo> sprites = new ArrayList<>();

                                 /*Movement Fields*/
    public static String currentDirection = ""; //String for storing current direction (ex: up, left, right, down, ""=not moving)
    public static int currentFrameIndex = 0; // Iterator for Sprite Frames

    //Arrays to hold tag values of walking frames
    //Order of Frames: Idle-Step-Idle-Step-Repeat
    public static String[] rightWalkingFrame = {"R0", "R1", "R0", "R2"};
    public static String[] leftWalkingFrame = {"L0", "L1", "L0", "L2"};
    public static String[] upWalkingFrame = {"B0", "B1", "B0", "B2"};
    public static String[] downWalkingFrame = {"FR0", "FR1", "FR0", "FR2"};

                             /*Collision Bound Fields*/
    //Bounds for edges of the screen
    public static BoundBox wallTop = new BoundBox(0,1280,0,96);
    public static BoundBox wallBottom = new BoundBox(0,1280,672,720);
    public static BoundBox wallLeft = new BoundBox(0, 96, 0,720);
    public static BoundBox wallRight = new BoundBox (1184, 1280,0,720);
    //Additional bound for top of interactable tree
    public static BoundBox treeTop = new BoundBox(600,1100,0,250);
    //Container to hold BoundBox variables
    public static ArrayList<BoundBox> bounds = new ArrayList<>();

                               /*Interaction Fields*/
    //BoundBox variable for banana interaction area
    public static BoundBox bananaInteract = new BoundBox(280,350,355,425);
    //BoundBox variable for tree interaction area
    public static BoundBox treeInteract = new BoundBox(780,930,390,420);
    //Boolean Flags for if objects have been interacted with
    public static boolean isBananaInteracted = false;
    public static boolean isTreeInteracted = false;
    // End Static fields...
    

	public static void main(String[] args) {
		Control ctrl = new Control();				// Do NOT remove!
		ctrl.gameLoop();							// Do NOT remove!
	}
	
	/* This is your access to things BEFORE the game loop starts */
	public static void start(){

        //Adding sprites to ArrayList in-order of back to front
        sprites.add(background); //Back
        sprites.add(backgroundRoof);
        sprites.add(banana);
        sprites.add(tree);
        sprites.add(player);
        sprites.add(backgroundWalls); //Front

        //Adding BoundBox values to bounds ArrayList for collision detection
        bounds.add(wallTop);
        bounds.add(wallBottom);
        bounds.add(wallLeft);
        bounds.add(wallRight);
        bounds.add(treeTop);
        bounds.add(banana.getPlayerBox());
        bounds.add(tree.getPlayerBox());

        //Retrieving comments from Dialogue.txt file for text when objects interacted with
        for (int i = 0; i < 2; i++){ //For loop to add all dialogue lines to HashMap
            String rawToken = ezr.getLine(i); //Retrieve raw dialogue from txt file
            StringTokenizer st = new StringTokenizer(rawToken, "*"); //Assign raw dialogue to StringTokenizer to be broken down
            String Key = st.nextToken(); // Assign Key value (Ex: string#)
            String Value = st.nextToken(); // Assign Value value (Ex: 'Dialogue')
            textMap.put(Key,Value); // Assign Key and corresponding Value to HashMap
        }
	}
	
	/* This is your access to the "game loop" (It is a "callback" method from the Control class (do NOT modify that class!))*/
	public static void update(Control ctrl) {
                                              /*Sprites in Game Loop*/
        //Sprites are printed in-order 

        //Grass
        ctrl.addSpriteToFrontBuffer(sprites.get(0).getCoords().getX(),sprites.get(0).getCoords().getY(),sprites.get(0).getTag());
        //Tree Roof/Background Roof
        ctrl.addSpriteToFrontBuffer(sprites.get(1).getCoords().getX(),sprites.get(1).getCoords().getY(),sprites.get(1).getTag());
        //Banana -- if statement to clear banana from screen when interacted
        if(!isBananaInteracted){
            ctrl.addSpriteToFrontBuffer(sprites.get(2).getCoords().getX(), sprites.get(2).getCoords().getY(), sprites.get(2).getTag());
        }
        //Tree
        ctrl.addSpriteToFrontBuffer(sprites.get(3).getCoords().getX(),sprites.get(3).getCoords().getY(),sprites.get(3).getTag());
        //Player
        ctrl.addSpriteToFrontBuffer(sprites.get(4).getCoords().getX(),sprites.get(4).getCoords().getY(),sprites.get(4).getTag());
        //Background Overlay
        ctrl.addSpriteToFrontBuffer(sprites.get(5).getCoords().getX(),sprites.get(5).getCoords().getY(),sprites.get(5).getTag());


        //Movement
        if (spriteTimer.isTimeUp()){ //If statement to allow for repeated movements
            movement();
            spriteTimer.resetWatch();
        }

        //Text for Interactions
        ctrl.drawString(150,600,currentText,Color.white);
		if (textTimer.isTimeUp()) {
             currentText = "";
        }



	}	// Additional Static methods below...(if needed)

    //Movement method reads direction from keyProcessor. then calls movementHelper with x or y adjustments
    public static void movement(){

        switch(currentDirection){
            case "":
                break;

            case "up":
                movementHelper(upWalkingFrame,0,-16);
                break;

            case "down":
                movementHelper(downWalkingFrame,0,16);
                break;

            case "left":
                movementHelper(leftWalkingFrame,-16,0);
                break;

            case "right":
                movementHelper(rightWalkingFrame,16,0);
                break;
        }
    }

    /*
    movementHelper method updates the player's coordinates, sprite tag and index, checks for an out-of-bounds or object
    collision with checkCollision method. If not collision detected, updates player's oldPosition and checks if
    frame index needs to be reset.
     */
    public static void movementHelper(String[] currentMovementFrames, int x, int y){

        player.setCoords(player.getCoords().getX() + x, player.getCoords().getY() + y); //Update Coordinates
        player.setTag(currentMovementFrames[currentFrameIndex]); //Update Sprite Tag
        currentFrameIndex++; //Increment Frame Index

        checkCollision(); //Check for out-of-bounds or object collision

        player.setOldPosition(player.getCoords().getX(),player.getCoords().getY()); //Update oldPosition

        if (currentFrameIndex > 3){ //If statement to check if frame index needs to be updated
            currentFrameIndex = 0;
        }
    }

    /*
    checkCollision method checks current player BoundBox with designated out-of-bounds and object bounds to check if
    collision has occurred. If collision occurs, resets player coordinates and BoundBox values to correspond with
    coordinates prior to collision.
    */
    public static void checkCollision() {

        BoundBox playerBox = player.getPlayerBox(); //Retrieve current player BoundBox

        //For loop and if statement check ArrayList of bounds values to check if any collisions have occurred.
        for (int i = 0; i < bounds.size(); i++) {
            if (playerBox.collisionDetect(bounds.get(i))) {
                //Sets players location to previous value if collision detected.
                player.setCoords(player.getOldX(), player.getOldY());

                //Update player BoundBox to correspond with old coordinates
                playerBox.setBox(player.getOldX()+32, player.getOldX()+96, player.getOldY() + 96,
                                 player.getOldY() + 128);
                return;
            }
        }
    }

    /*
    checkInteract checks if player's current BoundBox is within the bounds of the banana or tree interaction bounds.
    If either is interacted with, assign text for corresponding object to currentText and reset textTimer to display
    text to screen. For banana, method will remove banana BoundBox value from ArrayList of bounds and change value of
    isBananaInteracted to make banana disappear from screen.
     */

    public static void checkInteract(){
        if(sprites.get(4).getPlayerBox().collisionDetect(bananaInteract)){
            if(!isBananaInteracted){
                currentText = textMap.get("banana1");
                isBananaInteracted = true;
                bounds.remove(banana.getPlayerBox());
                textTimer.resetWatch();
            }
        }

        if(sprites.get(4).getPlayerBox().collisionDetect(treeInteract) && sprites.get(4).getTag().contains("B")){
            currentText = textMap.get("tree1");
            isTreeInteracted = true;
            textTimer.resetWatch();
        }
    }
}
