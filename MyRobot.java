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
    ArrayList<Robot> enemyBots = new ArrayList<>();


    public Action turn() {
        if(me.unit == SPECS.CASTLE){
            // insert logic here
            log("Building a crusader.");
            return buildUnit(SPECS.CRUSADER, 1, 1);
        }
        if(me.unit == SPECS.CRUSADER){
            log("Position: X(" + me.x + ") Y(" + me.y + ")");
            if(getVisibleRobots() != null && me.turn >= 75){
                log("Enemy has been detected, attempting to attack.");
                int enemyID = getVisibleRobots()[0].id;
                int enemyX = getRobot(enemyID).x;
                int enemyY = getRobot(enemyID).y;
                return attack(enemyY, enemyX);
            }  
            log("about to move");
            return move(2,-1);            
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