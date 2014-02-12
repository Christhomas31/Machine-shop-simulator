package applications;

import utilities.MyInputStream;
import dataStructures.LinkedQueue;
import exceptions.MyInputException;

public class Job {
	// data members
    private LinkedQueue taskQ; // this job's tasks
    private int length; // sum of scheduled task times
    private int arrivalTime; // arrival time at current queue
    private int id; // job identifier
    private int numTasks;

    // constructor
    public Job(int IdValue) {
        id = IdValue;
        taskQ = new LinkedQueue();
        // length and arrivalTime have default value 0
    }

    public Job(int IdValue, MyInputStream keyboard){
    	taskQ = new LinkedQueue();
    	id = IdValue;
    	System.out.println("Enter number of tasks for job " + id);
        numTasks = keyboard.readInteger(); // number of tasks
        if (numTasks < 1)
            throw new MyInputException(MachineShopSimulator.EACH_JOB_MUST_HAVE_AT_LEAST_1_TASK);
    }
    // other methods
    /**
     * remove next task of job and return its time also update length
     */
    public int removeNextTask() {
        int theTime = ((Task) taskQ.remove()).getTime();
        length += theTime;
        return theTime;
    }
    
    public boolean isTaskQEmpty(){
    	return taskQ.isEmpty();     
    }
    
    public int getNextMachine(int timeNow){
		arrivalTime = timeNow;
    	return ((Task) taskQ.getFrontElement()).getMachine();
    }
    
    public void printJobStats(int timeNow){
    	System.out.println("Job " + id + " has completed at "
				+ timeNow + " Total wait was " + (timeNow - length));
    }
    
    public int getArrivalTime(){
    	return arrivalTime;
    }

	public void setTasks(MyInputStream keyboard, int numMachines, Machine[] machine) {
		for (int j = 1; j <= numTasks; j++) {// get tasks for job i
            int machineID = keyboard.readInteger();
            int theTaskTime = keyboard.readInteger();
            if (machineID < 1 || machineID > numMachines
                    || theTaskTime < 1)
                throw new MyInputException("bad machine number or task time");
            if (j == 1)
            	machine[machineID].addJob(this);
            taskQ.put(new Task(machineID, theTaskTime)); // add to
        }
	}
}