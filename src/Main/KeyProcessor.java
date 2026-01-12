/* This will handle the "Hot Key" system. */

package Main;

import logic.Control;
import timer.stopWatchX;

public class KeyProcessor{
	// Static Fields
	private static char last = ' ';			// For debouncing purposes
	private static stopWatchX sw = new stopWatchX(250); //Set to 120 from OG value of 250 to match frame rate
	
	// Static Method(s)
    public static stopWatchX getTimer(){return sw;}

	public static void processKey(char key){
		if(key == ' '){
            Main.currentDirection = "";
            return;
        }
		// Debounce routine below...
		if(key == last)
			if(!sw.isTimeUp())			return;
		last = key;
		sw.resetWatch();

		//Actions for corresponding keystrokes can be altered below
		switch(key) {

            case '%':                                // ESC key
                System.exit(0);
                break;

            case 'm':
                // For mouse coordinates
                Control.isMouseCoordsDisplayed = !Control.isMouseCoordsDisplayed;
                break;

            case 'w':
                Main.currentDirection = "up";
                break;

            case 'a':
                Main.currentDirection = "left";
                break;

            case 's':
                Main.currentDirection = "down";
                break;

            case 'd':
                Main.currentDirection = "right";
                break;

            case '$':
                Main.currentDirection = "";
                Main.checkInteract();
                break;
        }
	}
}