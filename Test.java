package ElevatorControlSystem;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Test {
	public static void main (String[] args){
		ElevatorControlSystem ecs = new ElevatorControlSystem(10, 2, SchedulerType.MIN_FLOORS_TO_DIRECTION);
		ecs.updateSetFloor(0, 0);
		ecs.updateSetFloor(1, 9);
		ecs.pickup(2, 1);
		ecs.pickup(7, -1);
		System.out.println("Inital status:");
		int numberOfSteps = 20;
		pirntStatus(ecs.status());
		for (int i=1; i <= numberOfSteps ; i++) {
			ecs.step();
			System.out.println("Step " + i +":");
			pirntStatus(ecs.status());
		}
	}
	
	public static void pirntStatus (ArrayList<Elevator> elevators){
		for (int i=0; i < elevators.size(); i++){ 
			Elevator e = elevators.get(i);
			System.out.print("Elevator " + e.getId() + " is in floor " + e.getFloor());
			ArrayList<Integer> goals = e.getGoals();
			if (!goals.isEmpty()) { 
				System.out.print(" with goals: ");
				for (int g = 0; g < goals.size() ; g++){
					System.out.print(goals.get(g) + " ");
				}
			}
			else System.out.print(" with no goal");
			System.out.println("");
		}
	}
}
