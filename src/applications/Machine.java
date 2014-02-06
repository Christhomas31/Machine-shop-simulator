package applications;

import dataStructures.LinkedQueue;
import exceptions.MyInputException;

public class Machine {
	// data members
	private final int largeTime = Integer.MAX_VALUE;
	private LinkedQueue jobQ; // queue of waiting jobs for this machine
	private int changeTime; // machine change-over time
	private int totalWait; // total delay at this machine
	private int numTasks; // number of tasks processed on this machine
	private Job activeJob; // job currently active on this machine
	private int timeTaken; // replacing finish time

	// constructor
	public Machine(int tempChange) {
		timeTaken = largeTime;
		jobQ = new LinkedQueue();
		if (tempChange < 0)
			throw new MyInputException("change-over time must be >= 0");
		changeTime = tempChange;
	}

	public LinkedQueue getJobQ(){
		return jobQ;
	}

	public Job getActiveJob(){
		return activeJob;
	}

	public void setActiveJob(Job newJob){
		activeJob = newJob;
	}

	public void incrementNumTasks(){
		numTasks++;
	}


	public void increaseTotalWait(int increase){
		totalWait += increase;
	}

	public void printResults(int count){
		System.out.println("Machine " + count + " completed "
				+ numTasks + " tasks");
		System.out.println("The total wait time was "
				+ totalWait);
		System.out.println();
	}

	public int getChangeTime(){
		return changeTime;
	}

	public int getTimeTaken() {
		return timeTaken;
	}
	
	public void setFinishTime() {
        timeTaken = largeTime;
    }
    
    public void setFinishTime(int time) {
        timeTaken = time;
    }
    
    public boolean isIdle() {
		return timeTaken == largeTime;
	}
}
