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
    int homeDirection;
    int targetLocationX;
    int targetLocationY;

                //SW -1 1 / W -1 0 / NW -1 -1 / N 0 1 / NE 1 -1 / E 1 0 / SE 1 1 / S 0 1
    int[] directionX = {-1,-1,-1,0, 1,1,1,0};
    int[] directionY = {-1,0 ,-1,1,-1,0,1,1};

    int nearestKarSourceX;
    int nearestKarSourceY;

    public Action turn() {
        log(" ");
    	log("BEGINNING ROUND " + me.turn);
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

                if( checkDir(me, randomDirX, randomDirY) ){

                    if( (int)(Math.floor(Math.random()*4)) >= 1 ) {
                         log("about to build Crusader in " + randomDirX + ", " + randomDirY);
                         return buildUnit(SPECS.CRUSADER, randomDirX, randomDirY);
                    } else {
                        log("about to build Pilgrim in " + randomDirX + ", " + randomDirY);
                        return buildUnit(SPECS.PILGRIM, randomDirX, randomDirY);
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
                if( checkDir(me, randomDirX, randomDirY) ){
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
                if(getKarboniteMap()[me.y][me.x] == true){
                    log("Currently at karbonite mine, recording location...");
                    targetLocationX = me.x;
                    targetLocationY = me.y;
                } else {
                    log("Looking around for a mine...");
                    targetLocationX = findNearestKarbMine(me, karbMines, true);
                    targetLocationY = findNearestKarbMine(me, karbMines, false);
                }
            	
            	log("Target Mine Location recorded. X: " + targetLocationX + ". Y: " + targetLocationY + ".");
           	} else {
                log("Not first turn alive.");

                if(me.karbonite < 20){
                    // this runs if Pilgrim isn't full of karbonite
                    log("Karbonite inventory isn't full. My X: " + me.x + " Karb X: " + targetLocationX);
                    if(me.x != targetLocationX){
                        log("X coordinate not equal to target mine.");
                        // if mine is to the left
                        if(me.x > targetLocationX && checkDir(me, -1, 0)){
                            log("Mine is to the left, moving left.");
                            return move(-1,0);
                            //if mine is to the right
                        } else if(me.x < targetLocationX && checkDir(me, 1, 0)){
                            log("Mine is to the right, moving right.");
                            return move(1,0);
                        } else {
                            log("Horizontal movement is blocked.");

                            // move up if we can to get around obstacle
                            if(checkDir(me, 0, -1)) {
                                log("Moving up to avoid obstacle.");
                                return move(0,-1);
                            } else {
                                // otherwise, move down
                                log("Moving down to avoid obstacle.");
                                return move(0, 1);
                            }
                        }
                    } else if(me.y != targetLocationY){
                        log("Y coordinate not equal to target mine. My Y: " + me.y + " Karb Y: " + targetLocationY);
                        // if mine is upward
                        if(me.y > targetLocationY && checkDir(me, 0, -1)){
                            log("Mine is upward, moving upward.");
                            return move(0,-1);
                            //if mine is downward
                        } else if(me.y < targetLocationY && checkDir(me, 0, 1)){
                            log("Mine is downward, moving down.");
                            return move(0,1);
                        } else {
                            log("Vertical movement is blocked.");

                            // move left if we can to get around obstacle
                            if(checkDir(me, -1, 0)) {
                                log("Moving left to avoid obstacle.");
                                return move(-1, 0);
                            } else {
                                log("Moving right to avoid obstacle.");
                                return move(1, 0);
                            }
                        }
                    } else if( (me.x == targetLocationX) && (me.y == targetLocationY) ){
                        log("Mine coords are equal. Currently at mine. Mining now...");
                        return mine();
                    }

                } else {
                    // this runs if Pilgrim is full of karbonite
                    if(me.y != homeLocationY){
                        log("Y coordinate not equal to origin. My Y: " + me.y + " Origin Y: " + homeLocationY);
                        // if mine is upward
                        if(me.y > homeLocationY){
                            log("Origin is upward, moving upward.");
                            return move(0,-1);
                            //if mine is downward
                        } else if(me.y < homeLocationY){
                            log("Origin is downward, moving down.");
                            return move(0,1);
                        }
                    } else if(me.x != homeLocationX){
                        log("X coordinate not equal to origin. My X: " + me.x + " Origin X: " + homeLocationX);
                        // if mine is to the left
                        if(me.x > homeLocationX){
                            log("Origin is to the left, moving left.");
                            return move(-1,0);
                            //if mine is to the right
                        } else if(me.x < homeLocationX){
                            log("Origin is to the right, moving right.");
                            return move(1,0);
                        }
                    } else if( (me.x == homeLocationX) && (me.y == homeLocationY) ){



                        log("Home coords are equal. Currently at home. Locating castle...");
                        for(int x = me.x-1; x <= me.x+1; x++){
                            for(int y = me.y-1; y <= me.y+1; y++){
                                log("Starting tile (" + x + ", " + y + ")");
                               // if()


                            }
                        }



                        //return give(, , me.karbonite, 0);
                    }


                }



            }  
        }
    }





    private boolean checkDir(Robot me, int relativeX, int relativeY) {
    	log(" ");
        log("About to check if coords " + relativeX + ", " + relativeY + " are passible...");  
        int trueCoordX = me.x+relativeX;
        int trueCoordY = me.y+relativeY;
        log("Coords being checked: " + trueCoordX + ", " + trueCoordY + ". Current Coords: " + me.x + ", " + me.y);

        if(this.getPassableMap()[me.y+relativeY][me.x+relativeX] == true){
            log("Command returns " + getPassableMap()[me.y+relativeY][me.x+relativeX] + ". Coords are passible. Returning True.");
            return true;
        } else {
            log("Command returns " + getPassableMap()[me.y+relativeY][me.x+relativeX] + ". Coords are not passible. Returning False.");
            return false;
        }
         
    }
    private ArrayList<Integer[]> checkKarb(Robot me, int currentX, int currentY){
    	log(" ");
        ArrayList<Integer[]> karboniteLocations = new ArrayList<Integer[]>();
        log("Beginning mildly dangerous loop...");
        // start with coords for negative karbonite range from robot, then iterates to positive karb range from bot
        for(int x = -karbRange+currentX; x < karbRange+currentX; x++){
            // if the x coordinate is greater than or equal to 0 (meaning on the map)
            if(x >= 0){
                log("Beginning X Tile " + x);
                // search y coords now
                for(int y = -karbRange+currentY; y < karbRange+currentY; y++){
                    // if y coord is on the map
                    if(y >= 0){
                        log("Beginning Y Tile " + y);
                        log("Tile " + x + ", " + y);
                        //if the current coords being checked contain karb
                        if(getKarboniteMap()[y][x] == true){
                            log("Tile " + x + ", " + y + " contains a Karbonite deposit. Writing to memory...");
                            Integer[] tileCoords = new Integer[]{x, y};
                            karboniteLocations.add(tileCoords);
                            log("Tile " + x + ", " + y + " written to memory. Searching next tile.");
                        } else {
                            log("Tile " + x + ", " + y + " doesn't have Karbonite.");
                        }
                    } else {
                        log("Y Tile is off the map, moving to next one.");
                        continue;
                    }
                }
            } else {
                log("X Tile is off the map, moving to next one.");
                continue;
            }
        }
        log("Loop completed. Returning value...");
        log("");
        return karboniteLocations;
    }

    private int findNearestKarbMine(Robot me, ArrayList<Integer[]> karbLocations, boolean returnX){
        log("");
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
                log("Ideal Coordinates set is null, setting to current value to start with.");
                idealCoords = currentCoords;
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
            log("");
            return idealCoords[0];
        } else {
            log("Returning Y coord");
            log("");
            return idealCoords[1];
        }



    }

    private int findSignalingRobot(Robot me){
        for(int bot = 0; bot < getVisibleRobots().length; bot++){
            if( isRadioing(getVisibleRobots()[bot]) ){
                return getVisibleRobots()[bot].id;
            }
        }
        return 0;
    }





}
