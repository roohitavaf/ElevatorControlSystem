# ElevatorControlSystem

Please check out the test file to see an example of how you can use the ElevatorControlSystem. 

## Elevator Class
This class represents a single elevator. It has different fields: 
* id: id of the elevator.
* currentFloor: current floor of the elevator. 
* goals: set of destination floors. 
* direction: direction (1: up, 0:no direction, -1:down)
* stop (true: stopped, flase: no stop). Note that an elevator may have stop = true but direction != 0 which means it will continue its movement after 1 step delay. 

Note: In my simulation, the elevator spends 1 step at its goal floors. 

## ElevatorControlSystem Class
You can initialized on instance of ElevatorControlSystem, by specifying the number of floors, the number of elevators, and the type of scheduler that you want, respectively. There are different types of scheduler that you can use, and you easily expand the set of scheduler by writting your own scheduler. 

Right now, it has four different types of schedulers: 
* ALLWAYS_FIRST: Alwasy selects the first elevator. 
* MIN_GOALS: Selects the elevator with minimum number of goals. 
* MIN_FLOORS_TO_PICKUP: Selects elevator with minimum number of floors that it should pass untill reach the desired floor. 
* MIN_FLOORS_TO_DIRECTION: Selects elevator with minimum number of floors that it should pass untill reach the desired floor and direction. 

### Update
In my interface, we have two separted function for update. 
* updateSetFloor: for setting the floor for an elevator, and 
* updateAddGoal: for adding a new goald to for an elevator. This function should use for example whenever a person select a floor to go. 

### Status
status() function returns an ArrayList of Elevator objects. 

### pickup 
It select an elevator based on the scheduler type selected, and add the pickup floor to the selected elevator goals list. 

## Test file
It creates an instance of ElevatorControlSystem with 10 floors, 2 elevators, and MIN_FLOORS_TO_DIRECTION scheduler. 
It sets one elevator at floor 0, and the other at floor 9. 
There are two pickup requests: one for up at floor 2, and one for down in floor 7. 
The scheduelr assing elevaor 2 to elevator 0, and floor 7 to elevator 1. 

## Future work
A good future work is adding an scheduler that considers the number of stops before reaching desired floor and direction. It will be a more accurate estimation of time than number of floors remaining, beacuse in my simulation stops cost time, and they are not immediate. 
