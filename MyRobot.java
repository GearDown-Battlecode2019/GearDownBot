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
    int karbRange = 20;


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
                    if(me.turn >= 5){
                         log("about to build Crusader in " + randomDirX + ", " + randomDirY);
                         return buildUnit(SPECS.CRUSADER, 1, 1);
                    } else if(me.turn < 5){
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
        int checkX;
        int checkY;
        for(int x = -karbRange; x < karbRange; x++){
        	log("Beginning X Tile " + checkX);
            for(int y = -karbRange; y < karbRange; y++){
            	log("Beginning Y Tile " + checkY);
            	checkX = (currentX+x);
            	checkY = (currentY+y);
                log("Tile " + checkX + ", " + checkY);
                if((checkX >= 0) && (checkY >= 0)){
                	if(getKarboniteMap()[checkY][checkX] == true){
                    	log("Tile " + checkX + ", " + checkY + " contains a Karbonite deposit. Writing to memory...");
                    	Integer[] tileCoords = new Integer[]{checkX, checkY};
                        karboniteLocations.add(tileCoords);
                    	log("Tile " + checkX + ", " + checkY + " written to memory. Searching next tile.");
                	} else {
                		log("Tile " + checkX + ", " + checkY + " doesn't contain any Karbonite.");
                	}
                }
            }
        }
        log("Loop completed. Returning value...");
        return karboniteLocations;
    }
    private int findNearestKarbMine(Robot me, ArrayList<Integer[]> karbUnsorted, boolean returnX){
    	ArrayList<Integer[]> karbSorted = karbUnsorted; 

    	if(karbUnsorted == null){
    		log("Array list is null.");
    		return 0;
    	} else {
    		log("Raw: " + karbUnsorted);
    		Collections.sort(karbSorted);
    		log("Sorted: " + karbSorted);
    	}

        int shortestDistanceInstance = 0;
        int[] idealCoords;
        int[] currentCoords;

        for(int i = 0; i < karbSorted().length; i++){
            currentCoords = karbSorted.get(i);
                
            // GOAL: Check if the absolute value (ABS) of the currentCoords is less than the current idealCoords ABS. if so, then overwrite it.




        }


    }





}