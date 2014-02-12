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
	private Job lastJob;
	private int timeTaken; // replacing finish time
	private int machineID;

	// constructor
	public Machine(int ID, int tempChange) {
		timeTaken = largeTime;
		machineID = ID;
		jobQ = new LinkedQueue();
		if (tempChange < 0)
			throw new MyInputException("change-over time must be >= 0");
		changeTime = tempChange;
	}

	public void addJob(Job job){
		jobQ.put(job);
	}

	public void printResults(){
		System.out.println("Machine " + machineID + " completed "
				+ numTasks + " tasks");
		System.out.println("The total wait time was "
				+ totalWait);
		System.out.println();
	}

	public int getTimeTaken() {
		return timeTaken;
	}

	public boolean isIdle() {
		return timeTaken == largeTime;
	}
	
	public void processJobQ(int timeNow){
		if (jobQ.isEmpty()) // no waiting job
			timeTaken = largeTime;
		else {// take job off the queue and work on it
			activeJob = (Job) jobQ.remove();
			totalWait += timeNow - activeJob.getArrivalTime();
			numTasks++;
			int timeOfTask = activeJob.removeNextTask();
			timeTaken = timeNow + timeOfTask;
			
		}
	}
	public Job changeState(int timeNow) {// Task on theMachine has finished,
		// schedule next one.
		if (activeJob == null) {// in idle or change-over
			lastJob = null;
			processJobQ(timeNow);
		} else {// task has just finished on machine[theMachine]
			// schedule change-over time
			lastJob = activeJob;
			activeJob = null;
			timeTaken = timeNow + changeTime;
		}

		return lastJob;
	}
}
