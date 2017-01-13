package ElevatorControlSystem;

import java.util.ArrayList;

import javax.naming.directory.DirContext;

enum SchedulerType {
	ALLWAYS_FIRST, 
	MIN_GOALS,
	MIN_FLOORS_TO_PICKUP,
	MIN_FLOORS_TO_DIRECTION
}

public class ElevatorControlSystem {
	public int numberOfElevators; 
	public int numberOfFloors;
	public ArrayList<Elevator> elevators;
	

	
	public SchedulerType schedulerType; 
	
	public ElevatorControlSystem (int numberOfFloors, int numberOfElevators, SchedulerType schedulerType){
		this.numberOfFloors = numberOfFloors;
		this.numberOfElevators = numberOfElevators;
		this.schedulerType = schedulerType;
		elevators = new ArrayList<Elevator>();
		for (int i=0 ; i < numberOfElevators; i++){
			Elevator e = new Elevator(i, 0, numberOfFloors);
			elevators.add(e);
		}
	}
	//Instead of only one update function, we have two separated update function one for 
	//setting floor, another for adding a new goal. 
	public int updateSetFloor (int id, int floor){
		return elevators.get(id).setFloor(floor);
	}
	
	public int updateAddGoal (int id, int newGoal){
		return elevators.get(id).addGoal(newGoal);
	}
	
	public ArrayList<Elevator> status (){
		return elevators;
	}
	
	public void pickup (int floor, int direction){
		int selectedId; 
		switch (schedulerType) {
		case ALLWAYS_FIRST :
			selectedId = schedulerAllwaysFirst(floor, direction);
			break;
		case MIN_GOALS:
			selectedId = schedulerMinGoal(floor, direction);
			break;
		case MIN_FLOORS_TO_PICKUP:
			selectedId = schedulerMinFloorsToPickup(floor, direction);
			break;
		case MIN_FLOORS_TO_DIRECTION:
			selectedId = schedulerMinFloorsToPickup(floor, direction);
			break;
		default:
			selectedId = schedulerMinGoal(floor, direction);
			break;
		}
		 
		if (selectedId != -1) elevators.get(selectedId).addGoal(floor);
	}
	
	public void step (){
		for (int i=0; i < numberOfElevators; i++){
			Elevator ce = elevators.get(i);
			ce.updateLocation();
		}
	}
	
	//Schedulers -----------------------------------------------
	/**
	 * Always selects the first elevator. 
	 * @param floor
	 * @param direction
	 * @return If system has any elevator, it return 0. Otherwise, return -1. 
	 */
	int schedulerAllwaysFirst (int floor, int direction) {
		if (elevators.size() < 1) return -1;
		return 0;
	}
	
	/**
	 * Selects the elevator with minimum number of goals. 
	 * @param floor
	 * @param direction
	 * @return If system has any elevator, returns the id of the elevator
	 * with minimum number of goals. Otherwise, return -1. 
	 */
	int schedulerMinGoal (int floor, int direction) {
		if (elevators.size() < 1) return -1;
		int minGoals = elevators.get(0).getGoals().size(); 
		int minId = 0; 
		for (int i = 1; i < numberOfElevators; i++){ 
			if (elevators.get(i).getGoals().size() < minGoals) {
				minGoals = elevators.get(i).getGoals().size();
				minId = i;
			}
		}
		return minId;
	}
	
	/**
	 * Selects the elevator with minimum number of floors that it should pass before
	 * picking up the requester.
	 * @param floor
	 * @param direction
	 * @return If system has any elevator, returns the id of the elevator
	 * with minimum numbers of floors that it should pass before
	 * picking up the requester. Otherwise, return -1. 
	 */
	int schedulerMinFloorsToPickup (int floor, int direction) {
		if (elevators.size() < 1) return -1;
		int minFloors = floorsToPickup(0, floor);
		int minId = 0;
		for (int i=1; i < numberOfElevators ; i++) {
			int newMinFloors = floorsToPickup(i, floor);
			if (newMinFloors < minFloors) {
				minFloors = newMinFloors;
				minId = i;
			}
		}
		return minId;
	}
	/**
	 * Computes number of floors that the elevator should pass before
	 * picking up the requester.
	 * @param id
	 * @param floor
	 * @return The number of floors that the elevator should pass before
	 * picking up the requester.
	 */
	int floorsToPickup (int id, int floor){
		Elevator e = elevators.get(id);
		
		if (e.getFloor() > floor || (e.getFloor() == floor && e.getDirection() > 0)) {
			if (e.getDirection() <= 0) return e.getFloor() -  floor; 
			else return (e.maxGoal() - e.getFloor()) + (e.maxGoal() - floor);
		}
		
		else {
			if (e.getDirection() >= 0) return  floor - e.getFloor();
			else return (e.getFloor() - e.minGoal()) + (floor - e.minGoal());
		}
	}
	
	/**
	 * Select the elevator that has the minimum number of floors that the elevator should pass before 
	 * going to the desired direction in the desired floor.
	 * @param floor
	 * @param direction
	 * @return If system has any elevator, returns the id of the elevator that has the minimum number of floors that the elevator should pass before 
	 * going to the desired direction in the desired floor. Otherwise, return -1.
	 */
	int schedulerMinFloorsToDirection (int floor, int direction) {
		if (elevators.size() < 1) return -1;
		int minFloors = floorsToDirection(0, floor, direction);
		int minId = 0;
		for (int i=1; i < numberOfElevators ; i++) {
			int newMinFloors = floorsToDirection(i, floor, direction);
			if (newMinFloors < minFloors) {
				minFloors = newMinFloors;
				minId = i;
			}
		}
		return minId;
	}
	
	/**
	 * Computer the number of floors that the elevator should pass before 
	 * going to the desired direction in the desired floor.
	 * @param id
	 * @param floor
	 * @param direction
	 * @return The number of floors that the elevator should pass before 
	 * going to the desired direction in the desired floor.
	 */
	int floorsToDirection (int id, int floor, int direction){
		Elevator e = elevators.get(id);
		
		if (e.getFloor() > floor || (e.getFloor() == floor && e.getDirection() > 0) ) {
			if (direction < 0 && e.getDirection() <= 0) return e.getFloor() - floor;
			else if (direction < 0 && e.getDirection() > 0) {
				int floors = 0;
				floors += e.maxGoal() - e.getFloor();
				floors += e.maxGoal() - floor;
				return floors;
			}
			else if (direction > 0 && e.getDirection() <= 0) {
				int floors = 0; 
				if (e.minGoal() < floor) {
					floors += e.getFloor() - e.minGoal();
					floors += floor - e.minGoal();
					return floors;
				}
				return e.getFloor() - floor;
			}
			else {
				int floors = 0; 
				floors += e.maxGoal() - e.getFloor();
				if (e.minGoal() < floor) {
					floors += e.maxGoal() - e.minGoal(); 
					floors += floor - e.minGoal();
					return floors;
				}
				else {
					floors += e.maxGoal() - floor;
					return floors;
				}
				
			}
		}
		
		else {
			if (direction > 0 && e.getDirection() >= 0) return floor - e.getFloor();
			else if (direction > 0 && e.getDirection() < 0) {
				int floors = 0; 
				floors += e.getFloor() - e.minGoal();
				floors += floor - e.minGoal();
				return floors;
			}
			else if (direction < 0 && e.getDirection() >= 0){
				int floors = 0; 
				if (e.maxGoal() > floor) {
					floors += e.maxGoal() - e.getFloor();
					floors += e.maxGoal() - floor;
					return floors;
				}
				else return floor - e.getFloor(); 
			}
			else {
				int floors = 0; 
				floors += e.getFloor() - e.minGoal(); 
				if (e.maxGoal() > floor){
					floors += e.maxGoal() - e.minGoal(); 
					floors += e.maxGoal() - floor;
					return floors;
				}
				else {
					floors += floor - e.minGoal();
					return floors;
				}
			}
		}
	}
	
	//---------------------------------------------------

}
