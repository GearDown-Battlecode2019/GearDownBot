package GearDownBot;

import java.util.ArrayList;
import java.lang.Object;
import java.util.*;
import ImportTest.*;

public class MyRobot extends BCAbstractRobot {

    // private static Map<Integer, Unit> units;
    // private UnitFactory unitFactory;
    
    // public MyRobot() {
    //     this.units = new HashMap<>();
    //     this.unitFactory = new UnitFactory(me);
    // }  

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

    ArrayList<Integer[]> karboniteMines = new ArrayList<Integer[]>();

    public Action turn() {
    	log("BEGINNING ROUND " + me.turn);
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
            	// log("Finding and recording closest Karbonite Mine...");
            	// targetLocationX = findNearestKarbMine(me, karbX, karbY, true);
            	// targetLocationY = findNearestKarbMine(me, karbX, karbY, false);
            	// log("Karbonite Mine Location recorded.");


           	}
            if(getKarboniteMap()[me.y][me.x] = true && me.karbonite < 20){
                log("Karbonite mine is here, and inventory has space. Mining now...");
                return mine();
            }
			

            
        }

        // switch (me.unit) {
        //     case SPECS.CASTLE:
                
        //     case SPECS.PILGRIM:
        //         // if(!units.containsKey(me.id)) {  // if the current unit has not been registered
        //         //     units.add(me.id, unitFactory.createUnit(me.id));
        //         // }
                
        //         //return units.get(me.id).turn();
        //         return move(1,1);
        // }
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
                    	log("Tile " + checkX + ", " + (checkY) + " contains a Karbonite deposit. Writing to memory...");
                    	karbX.add(checkX);
                    	karbY.add(checkY);
                    	log("Tile " + (checkX) + ", " + (checkY) + " written to memory. Restarting loop for next tile.");
                	} else {
                		log("Tile " + (checkX) + ", " + (checkY) + " doesn't contain any Karbonite.");
                	}
                }
            }
        }
        log("Loop completed. Returning value...");
        return 
    }
  //   private int findNearestKarbMine(Robot me, ArrayList<Integer> karbX, ArrayList<Integer> karbY, boolean returnX){
  //   	ArrayList<Integer> karbSortX = karbX; 
		// ArrayList<Integer> karbSortY = karbY;

  //   	if(karbX == null){
  //   		log("X array list is null.");
  //   		return 0;
  //   	} else if(karbY == null) {
		// 	log("Y array list is null.");
  //   		return 0;
  //   	} else if(karbX == null && karbY == null){
  //   		log("Both array lists are null.");
  //   		return 0;
  //   	} else {
  //   		log("X Raw: " + this.karbX + " Y Raw: " + this.karbY);
  //   		Collections.sort(karbSortX);
	 //    	Collections.sort(karbSortY);
  //   		log("X Sorted: " + karbSortX + " Y Sorted: " + karbSortY);
  //   	}

}
    // public List<Unit> getUnits() {
    //     return units.values();    
    // }
    
    // private Action castleLogic() {
         
    // }

// public class UnitFactory {
//     private MyRobot r;
    
//     public UnitFactory(MyRobot r) {
//         this.r = r;
//     }
    
//     public Unit createUnit(int type) {
//         switch(type) {
//             case SPECS.PILGRIM:
//                 return new Pilgrim(r);
//         }
//     }
// }

// public class Pilgrim extends Unit {
//     public Pilgrim(MyRobot robot) {
//         super(robot);
//     }
    
//     @Override
//     public Action turn() {
//         // Logic goes here
//     }
// }
// public abstract class AbstractUnit extends BCAbstractRobot {
//     private MyRobot r;
    
//     public Unit(MyRobot r) {
//         this.r = r;
//     }
    
//     public abstract Action turn();
// }
// // package bc19;
// // import java.awt.Point;

// // public class MyRobot extends BCAbstractRobot {
// // 	public int turn;
// // 	public Point destination;

// //     public Action turn() {
// //     	turn++;

// //     	if (me.unit == SPECS.CASTLE) {
// //     		if (turn == 1) {
// //     			log("Building a pilgrim.");
// //     			return buildUnit(SPECS.PILGRIM,1,0);
// //     		}

// // 			Robot[] visibleRobots = getVisibleRobots();
// // 			for(Robot r: visibleRobots) {
// // 				if (r.team != me.team) {
// // 					int diffX = r.x - me.x;
// // 					int diffY = r.y - me.y;
// // 					return attack(diffX, diffY);
// // 				}
// // 			}

// // 			Point myLocation = new Point(me.x, me.y);

// // 			if (destination == null) {
// // 				destination = Navigation.reflect(myLocation, getPassableMap(), me.id % 2 == 0);
// // 			}

// // 			Point movementDirection = Navigation.goTo(myLocation, destination, getPassableMap(), getVisibleRobotMap());
// // 			return move(movementDirection.x, movementDirection.y);
// //     	}

// //     	if (me.unit == SPECS.PILGRIM) {
// //     		if (turn == 1) {
// //     			log("I am a pilgrim.");
                 
// //                 //log(Integer.toString([0][getVisibleRobots()[0].castle_talk]));
// //     		}
// //     	}

// //     	return null;

// // 	}
// // }