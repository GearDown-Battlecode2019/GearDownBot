package bc19;

public class MyRobot extends BCAbstractRobot {

    private static Map<Integer, Unit> units;
    private UnitFactory unitFactory;
    
    public MyRobot() {
        this.units = new HashMap<>();
        this.unitFactory = new UnitFactory(me);
    }
    
    public Action turn() {
        switch (me.unit) {
            case SPECS.CASTLE:
                return castleLogic();
            case SPECS.PILGRIM:
                if(!units.containsKey(me.id)) {  // if the current unit has not been registered
                    units.add(me.id, unitFactory.createUnit(me.id));
                }
                
                return units.get(me.id).turn();
        }
    }
    
    public List<Unit> getUnits() {
        return units.values();    
    }
    
    private Action castleLogic() {
         // insert logic here
         log("Building a pilgrim.");
         log("Position: X(" + me.x + ") Y(" + me.y + ")");
         return buildUnit(SPECS.PILGRIM, 1, 0);
    }
    
}

public class UnitFactory {
    private MyRobot r;
    
    public UnitFactory(MyRobot r) {
        this.r = r;
    }
    
    public Unit createUnit(int type) {
        switch(type) {
            case SPECS.PILGRIM:
                return new Pilgrim(r);
        }
    }
}

public class Pilgrim extends Unit {
    public Pilgrim(MyRobot robot) {
        super(robot);
    }
    
    @Override
    public Action turn() {
        // Logic goes here
    }
}
public abstract class AbstractUnit extends BCAbstractRobot {
    private MyRobot r;
    
    public Unit(MyRobot r) {
        this.r = r;
    }
    
    public abstract Action turn();
}
// package bc19;
// import java.awt.Point;

// public class MyRobot extends BCAbstractRobot {
// 	public int turn;
// 	public Point destination;

//     public Action turn() {
//     	turn++;

//     	if (me.unit == SPECS.CASTLE) {
//     		if (turn == 1) {
//     			log("Building a pilgrim.");
//     			return buildUnit(SPECS.PILGRIM,1,0);
//     		}

// 			Robot[] visibleRobots = getVisibleRobots();
// 			for(Robot r: visibleRobots) {
// 				if (r.team != me.team) {
// 					int diffX = r.x - me.x;
// 					int diffY = r.y - me.y;
// 					return attack(diffX, diffY);
// 				}
// 			}

// 			Point myLocation = new Point(me.x, me.y);

// 			if (destination == null) {
// 				destination = Navigation.reflect(myLocation, getPassableMap(), me.id % 2 == 0);
// 			}

// 			Point movementDirection = Navigation.goTo(myLocation, destination, getPassableMap(), getVisibleRobotMap());
// 			return move(movementDirection.x, movementDirection.y);
//     	}

//     	if (me.unit == SPECS.PILGRIM) {
//     		if (turn == 1) {
//     			log("I am a pilgrim.");
                 
//                 //log(Integer.toString([0][getVisibleRobots()[0].castle_talk]));
//     		}
//     	}

//     	return null;

// 	}
// }