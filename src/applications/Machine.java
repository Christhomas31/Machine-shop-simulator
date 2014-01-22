package applications;

import dataStructures.LinkedQueue;

public class Machine {
	  // data members
    private LinkedQueue jobQ; // queue of waiting jobs for this machine
    private int changeTime; // machine change-over time
    private int totalWait; // total delay at this machine
    private int numTasks; // number of tasks processed on this machine
    private Job activeJob; // job currently active on this machine

    // constructor
    public Machine() {
        jobQ = new LinkedQueue();
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
    
    public void setChangeTime(int newChangeTime){
    	changeTime = newChangeTime;
    }
}
