package bc19;

import java.util.ArrayList;
import java.lang.*;

public class MyRobot extends BCAbstractRobot {

    // private static Map<Integer, Unit> units;
    // private UnitFactory unitFactory;
    
    // public MyRobot() {
    //     this.units = new HashMap<>();
    //     this.unitFactory = new UnitFactory(me);
    // }    

    Robot enemyBot;

    //CRUSADER VARS
    int enemyID;
    int enemyX;
    int enemyY;
    int randomDirX;
    int randomDirY;
                //      SW -1 1 / W -1 0 / NW -1 -1 / N 0 1 / NE 1 -1 / E 1 0 / SE 1 1 / S 0 1
    int[] directionX = {-1,-1,-1,0, 1,1,1,0};
    int[] directionY = {-1,0 ,-1,1,-1,0,1,1};

    public Action turn() {
        enemyBot = null;

        log("Attempting to search for enemy bots...");
        if(getVisibleRobots()[0].team != me.team){
            enemyBot = getVisibleRobots()[0];
            log("Enemy bot found.");
        } else {
            log("No enemies found.");
        }

        if(me.unit == SPECS.CASTLE){
            log("Generating direction to build in.");
            randomDirX = directionX[ ( (int)(Math.round(Math.random()*7) ) ) ];
            randomDirY = directionY[ ( (int)(Math.round(Math.random()*7) ) ) ];
            log("Direction " + randomDirX + ", " + randomDirY + " generated. Testing if open for building.");
            if( checkDir(me, randomDirX, randomDirY, me.x, me.y) ){
                log("about to build Crusader in " + randomDirX + ", " + randomDirY);
                return buildUnit(SPECS.CRUSADER, 1, 1);
            } else {
                return null;
            } 
        }
        if(me.unit == SPECS.CRUSADER){
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
                log("Generating direction.");
                randomDirX = directionX[ ( (int)(Math.round(Math.random()*7) ) ) ];
                randomDirY = directionY[ ( (int)(Math.round(Math.random()*7) ) ) ];
                log("Direction " + randomDirX + ", " + randomDirY + " generated. Testing if passible.");
                if( checkDir(me, randomDirX, randomDirY, me.x, me.y) ){
                    log("about to move in " + randomDirX + ", " + randomDirY);
                    return move(randomDirX, randomDirY);  
                } else {
                   return null;
                }
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
        log("About to check if coords " + relativeX + ", " + relativeY + " are passible...");  

        if(this.getPassableMap()[currentLocX+relativeX][currentLocY+relativeY]){
            log("Coords are passible.");
            return getPassableMap()[currentLocX+relativeX][currentLocY+relativeY];
        } else{
            log("Not passible, returning false.");
            return false;
        }
         
    }
    // public List<Unit> getUnits() {
    //     return units.values();    
    // }
    
    // private Action castleLogic() {
         
    // }
}

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