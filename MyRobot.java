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
    double randomDirX;
    double randomDirY;

    public Action turn() {
        
        log("Attempting to search for enemy bots...");
        if(getVisibleRobots()[0].team != me.team){
            enemyBot = getVisibleRobots()[0];
            log("Enemy bot found.");
        } 

        if(me.unit == SPECS.CASTLE){
            // insert logic here
            log("Building a crusader.");
            return buildUnit(SPECS.CRUSADER, 1, 1);
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
                enemyX = enemyBot.x;
                enemyY = enemyBot.y;
                log("Enemy coords recorded, resetting enemy bot target");
                enemyBot = null;
                log("enemy bot reset, attacking robot");
                return attack(enemyY, enemyX);
            } else {
                log("generating random directons...");
                log(" " + ((int)Math.round(2.0*Math.random()-1)));
                randomDirX = ((int)Math.round(2.0*Math.random()-1));
                randomDirY = ((int)Math.round(2.0*Math.random()-1));
                log("random directions set. X: " + randomDirX + " Y: " + randomDirY);
                if(checkDir(me, randomDirX, randomDirY)){
                    log("about to move in " + randomDirX + ", " + randomDirY);
                    return move((int) randomDirX,(int) randomDirY);  
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
    private boolean checkDir(Robot me, double vertDir, double horizDir) {
        
         if( (vertDir >= -1 && vertDir <= 1) && (horizDir >= -1 && horizDir <= 1) ){
            if(this.getPassableMap()[(int)vertDir][(int)horizDir]){
                return getPassableMap()[(int)vertDir][(int)horizDir];
            }
         } else{
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