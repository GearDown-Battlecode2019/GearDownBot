package bc19;

import java.util.ArrayList;
import java.util.Collections;

public class MyRobot extends BCAbstractRobot {
    /** 

    PYTHON REFLECT CODE

    def reflect(full_map, loc, horizontal=True):
    v_reflec = (len(full_map[0]) - loc[0], loc[1])
    h_reflec = (loc[0], len(full_map) - loc[1])
    if horizontal:
        return h_reflec if full_map[h_reflec[1]][h_reflec[0]] else v_reflec
    else:
        return v_reflec if full_map[v_reflec[1]][v_reflec[0]] else h_reflec
        
        */

    Robot enemyBot;

    int enemyID;
    int enemyX;
    int enemyY;
    int randomDirX;
    int randomDirY;
    int randNum;
    boolean firstTurnAlive = true;
    int karbRange = 10;


    int homeLocationX;
    int homeLocationY;
    int targetLocationX;
    int targetLocationY;

                //SW -1 1 / W -1 0 / NW -1 -1 / N 0 1 / NE 1 -1 / E 1 0 / SE 1 1 / S 0 1
    int[] directionX = {-1,-1,-1,0, 1,1,1,0};
    int[] directionY = {-1,0 ,-1,1,-1,0,1,1};

    int nearestKarSourceX;
    int nearestKarSourceY;

    public Action turn() {
    	this.log("BEGINNING ROUND " + me.turn);
        ImportTest.importTestMethod(this);

        enemyBot = null;

        log("Attempting to search for enemy bots...");
        if(getVisibleRobots()[0].team != me.team){
            enemyBot = getVisibleRobots()[0];
            log("Enemy bot found.");
        } else {
            log("No enemies found.");
        }
        

        if(me.unit == SPECS.CASTLE){
        	log(" ");
        	log("I am a CASTLE.");
            if(enemyBot != null){
                log("Recording Enemy coords...");
                enemyX = enemyBot.x-me.x;
                enemyY = enemyBot.y-me.y;
                log("Enemy coords recorded, attacking robot");
                return attack(enemyX, enemyY);
            } else {
                log("Generating direction number.");
                randNum = ( (int)(Math.floor(Math.random()*8) ) );
                log("Number " + randNum + " generated. Applying to direction.");
                randomDirX = directionX[ randNum ];
                randomDirY = directionY[ randNum ];
                log("Direction " + randomDirX + ", " + randomDirY + " generated. Testing if open for building.");
                if( checkDir(me, randomDirX, randomDirY, me.x, me.y) ){
                    if( (int)(Math.floor(Math.random()*4)) >= 2 ) {
                         log("about to build Crusader in " + randomDirX + ", " + randomDirY);
                         return buildUnit(SPECS.CRUSADER, 1, 1);
                    } else {
                        log("about to build Pilgrim in " + randomDirX + ", " + randomDirY);
                        return buildUnit(SPECS.PILGRIM, 1, 1);
                    }
                  } else {
                    return null;
                } 
            }
        }


        if(me.unit == SPECS.CRUSADER){
        	log(" ");
        	log("I am a CRUSADER.");
            log("Position: X(" + me.x + ") Y(" + me.y + ")");
            log("Setting random directional variables back to 0...");
            randomDirX = 0;
            randomDirY = 0;
            log("Directional variables set.");
            // attacking code
            if(enemyBot != null){
                log("Recording Enemy coords...");
                enemyX = enemyBot.x-me.x;
                enemyY = enemyBot.y-me.y;
                log("Enemy coords recorded, attacking robot");
                return attack(enemyX, enemyY);
            } else {
                // log("generating random directons...");
                // randomDirX = ((int)Math.floor(Math.random()*2)-1);
                // log("X coord generated");
                // randomDirY = ((int)Math.floor(Math.random()*2)-1);
                // log("random directions set. X: " + randomDirX + " Y: " + randomDirY);
                log("Generating number for direciton...");
                randNum = ( (int)(Math.floor(Math.random()*8) ) );
                log("Number " + randNum + " generated. Applying to direction.");
                randomDirX = directionX[ randNum ];
                randomDirY = directionY[ randNum ];
                log("Direction " + randomDirX + ", " + randomDirY + " generated. Testing if passible.");
                if( checkDir(me, randomDirX, randomDirY, me.x, me.y) ){
                    log("about to move in " + randomDirX + ", " + randomDirY);
                    return move(randomDirX, randomDirY);  
                } else {
                   return null;
                }
            }
        }


        if(me.unit == SPECS.PILGRIM){
        	log(" ");
        	log("I am a PILGRIM.");
           	if(firstTurnAlive){
           		log("First turn alive, looking for Karbonite mines...");
           		firstTurnAlive = false;
           		ArrayList<Integer[]> karbMines = checkKarb(me, me.x, me.y);
            	log("Coords returned, " + karbMines);
            	log("Self X: " + me.x + " Y: " + me.y);
            	homeLocationX = me.x;
            	homeLocationY = me.y;
            	log("Finding and recording closest Karbonite Mine...");
            	targetLocationX = findNearestKarbMine(me, karbMines, true);
            	targetLocationY = findNearestKarbMine(me, karbMines, false);
            	log("Karbonite Mine Location recorded. X: " );


           	}
            if(getKarboniteMap()[me.y][me.x] = true && me.karbonite < 20){
                log("Karbonite mine is here, and inventory has space. Mining now...");
                return mine();
            }
            if(me.karbonite == 20 && )
			

            
        }
    }
    private boolean checkDir(Robot me, int relativeX, int relativeY, int currentLocX, int currentLocY) {
    	log(" ");
        log("About to check if coords " + relativeX + ", " + relativeY + " are passible...");  

        if(this.getPassableMap()[currentLocY+relativeY][currentLocX+relativeX]){
            log("Coords are passible.");
            return getPassableMap()[currentLocY+relativeY][currentLocX+relativeX];
        } else{
            log("Not passible, returning false.");
            return false;
        }
         
    }
    private ArrayList<Integer[]> checkKarb(Robot me, int currentX, int currentY){
    	log(" ");
        ArrayList<Integer[]> karboniteLocations = new ArrayList<Integer[]>();
        log("Beginning mildly dangerous loop...");

        for(int x = -karbRange+currentX; x < karbRange+currentX; x++){
            // if the x coordinate is greater than or equal to 0
            if(x >= 0){
                log("Beginning X Tile " + x);
                for(int y = -karbRange+currentY; y < karbRange+currentY; y++){
                    if(y >= 0){
                        log("Beginning Y Tile " + y);

                        log("Tile " + x + ", " + y);

                        if(getKarboniteMap()[y][x] == true){

                            log("Tile " + x + ", " + y + " contains a Karbonite deposit. Writing to memory...");

                            Integer[] tileCoords = new Integer[]{x, y};

                            karboniteLocations.add(tileCoords);

                            log("Tile " + x + ", " + y + " written to memory. Searching next tile.");

                        } else {

                            log("Tile " + x + ", " + y + " doesn't contain any Karbonite.");

                        }
                    } else {
                        break;
                    }
                }
            } else {
                break;
            }
        }
        log("Loop completed. Returning value...");
        return karboniteLocations;
    }

    private int findNearestKarbMine(Robot me, ArrayList<Integer[]> karbLocations, boolean returnX){

    	if(karbLocations == null){
    		log("Array list is null.");
    		return 0;
    	 }
        int shortestDistanceInstance = 0;
        Integer[] idealCoords;
        Integer[] currentCoords;

        for(int i = 0; i < karbLocations.size(); i++){
            log("Beginning loop with instance " + i + " of karbonite coords, which is " + karbLocations.get(i));
            currentCoords = karbLocations.get(i);
                
            if(idealCoords == null){
                log("Ideal Coordinate variable is null, setting to current value to start with. Restarting loop.");
                currentCoords = idealCoords;
                break;
            }
            log("Checking if absolute values are less than the ideal values");
            if((Math.abs(currentCoords[0]) < Math.abs(idealCoords[0])) && (Math.abs(currentCoords[1]) < Math.abs(idealCoords[1]))){
                log("Values are less than ideal values. Overwriting...");
                idealCoords = currentCoords;
                shortestDistanceInstance = i;
            } else{
                log("Values are not less than ideal values. Moving on.");
            }
        }
        log("Current Ideal Coords: " + idealCoords);
        if(returnX){
            log("Returning X coord");
            return idealCoords[0];
        } else {
            log("Returning Y coord");
            return idealCoords[1];
        }



    }





}