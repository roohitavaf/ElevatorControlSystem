package ElevatorControlSystem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.xml.bind.annotation.XmlElementDecl.GLOBAL;

public class Elevator {
	private int id;
	private int currentFloor;
	private ArrayList<Integer> goals;

	private int direction = 0;
	private boolean stop = false;

	private int numberOfFloors;

	public Elevator(int id, int currentFloor, int numberOfFloors) {
		this.id = id;
		this.currentFloor = currentFloor;
		this.numberOfFloors = numberOfFloors;
		goals = new ArrayList<Integer>();
	}
	
	public ArrayList<Integer>  getGoals (){
		return goals;
	}
	public int getId(){
		return id;
	}
	public int getDirection () {
		return direction;
	}
	
	public int getFloor (){
		return currentFloor;
	}
	/**
	 * Sets floor for the elevator. 
	 * @param floor
	 * @return 1 if goal is valid, -1 if goal is not valid.
	 */

	public int setFloor(int floor) {
		if (floor >= 0 && floor < numberOfFloors) {
			currentFloor = floor;
			return 1;
		}
		return -1;
	}

	/** 
	 * Add a goal to the goals set. Automatically calls updateDirection. 
	 * @param newGoal
	 * @return 1 if goal is valid, -1 if goal is not valid.
	 */
	public int addGoal(int newGoal) {
		if (newGoal < numberOfFloors && newGoal >= 0) {
			goals.add(newGoal);
			updateDirection();
			return 1;
		}
		return -1;
	}
	
	/**
	 * Removes a goal from goals set. It automatically calls updateDirection.
	 * @param goal
	 * @return
	 */
	private int removeGoal(int goal) {
		if (goals.contains(goal)) {
			goals.remove(goals.indexOf(goal));
			updateDirection();
			return 1;
		}
		return -1;
	}

	/**
	 * This function should be used to stop the elevator at a desired destination. 
	 * It sets stop flag to true, causing elevator stops at the current floor for one step.
	 * @param floor
	 */
	public void stop(int floor) {
		removeGoal(floor);
		stop = true;
	}

	/**
	 * Updates the direction of the elevator according to its set of goals.
	 */
	private void updateDirection() {
		if (goals.isEmpty())
		{
			direction = 0;
			return;
		}
		int upperGoals = 0;
		int lowerGoals = 0; 
		for (int g=0; g < goals.size() ; g++){
			if (goals.get(g) < currentFloor) lowerGoals++;
			else if (goals.get(g) > currentFloor) upperGoals++;
			else goals.remove(goals.indexOf(g));
		}
		if (direction == 0) {
			if (upperGoals >= lowerGoals) direction = 1; 
			else direction = -1;
		} else if (direction == 1 && (currentFloor >= numberOfFloors - 1) || upperGoals == 0) {
				direction = -1;
		} else if (direction == -1 && (currentFloor <= 0) || lowerGoals == 0) {
				direction = 1;
		}
	}
	
	/** 
	 * Updates the location of the elevator according to the direction and stop status.
	 * It automatically calls UpdateDirction to avoid going out of valid range. 
	 */
	public void updateLocation() {
		if (!stop) {
			setFloor(currentFloor + direction);
			if (goals.contains(currentFloor)) {
				stop(currentFloor);
			}
		} else if (direction != 0)
			stop = false;
		updateDirection();
	}
	
	/**
	 * 
	 * @return Highest floors in the goals set. 
	 */
	public int maxGoal ()
	{
		if (goals.size() == 0) return -1;
		Collections.sort(goals);
		return goals.get(goals.size()-1);
	}
	
	/**
	 * 
	 * @return lowest floor in the goals set. 
	 */
	public int minGoal ()
	{
		if (goals.size() == 0) return -1;
		Collections.sort(goals);
		return goals.get(0);
	}
}
