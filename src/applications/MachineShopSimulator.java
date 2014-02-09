package applications;

import utilities.MyInputStream;
import dataStructures.LinkedQueue;
import exceptions.MyInputException;

public class MachineShopSimulator {
    
    public static final String NUMBER_OF_MACHINES_MUST_BE_AT_LEAST_1 = "number of machines must be >= 1";
    public static final String NUMBER_OF_MACHINES_AND_JOBS_MUST_BE_AT_LEAST_1 = "number of machines and jobs must be >= 1";
    public static final String CHANGE_OVER_TIME_MUST_BE_AT_LEAST_0 = "change-over time must be >= 0";
    public static final String EACH_JOB_MUST_HAVE_AT_LEAST_1_TASK = "each job must have >= 1 task";
    public static final String BAD_MACHINE_NUMBER_OR_TASK_TIME = "bad machine number or task time";
    
    private static int timeNow; 
    private static int numMachines; 
    private static int numJobs;
    private static Machine[] machineArray;
    
    static void moveToNextMachine(Job job){
    	LinkedQueue taskQueue = job.getTaskQ();
    	int machineID = ((Task) taskQueue.getFrontElement()).getMachine();
        // put on machine p's wait queue
        machineArray[machineID].getJobQ().put(job);
        job.setArrivalTime(timeNow);
        // if idle, schedule immediately
        if (machineArray[machineID].isIdle()) {// machine is idle
            machineArray[machineID].processJobQ(timeNow);
        }
    }

    /**
     * change the state of theMachine
     * 
     * @return last job run on this machine
     */
    /** @return machine for next event */
    public static int nextEventMachine() {
        // find first machine to finish, this is the
        // machine with smallest finish time
        int machineID = 1;
        int lowestTime = machineArray[1].getTimeTaken();
        for (int i = 2; i < machineArray.length; i++)
            if (machineArray[i].getTimeTaken() < lowestTime) {// i finishes earlier
                machineID = i;
                lowestTime = machineArray[i].getTimeTaken();
            }
        return machineID;
    }

    /** input machine shop data */
    static void inputData() {
        // define the input stream to be the standard input stream
        MyInputStream keyboard = new MyInputStream();

        System.out.println("Enter number of machines and jobs");
        numMachines = keyboard.readInteger();
        numJobs = keyboard.readInteger();
        if (numMachines < 1 || numJobs < 1)
            throw new MyInputException(NUMBER_OF_MACHINES_AND_JOBS_MUST_BE_AT_LEAST_1);

        // create event and machine queues
        
        machineArray = new Machine[numMachines + 1];
        
        System.out.println("Enter change-over times for machines");
        for (int i = 1; i <= numMachines; i++){
            machineArray[i] = new Machine(i, keyboard.readInteger());
        }
       
        // input the jobs
        Job theJob;
        for (int i = 1; i <= numJobs; i++) {
            theJob = new Job(i, keyboard);
            System.out.println("Enter the tasks (machine, time)"
                    + " in process order");
            theJob.setTasks(keyboard, numMachines, machineArray);
        }
    }

    /** load first jobs onto each machine */
    static void startShop() {
        for (int p = 1; p <= numMachines; p++)
            machineArray[p].processJobQ(timeNow);
    }
    /** process all jobs to completion */
    static void simulate() {
        while (numJobs > 0) {// at least one job left
            int nextToFinish = nextEventMachine();
            timeNow = machineArray[nextToFinish].getTimeTaken();
            // change job on machine nextToFinish
            Job theJob = machineArray[nextToFinish].changeState(timeNow);
            // move theJob to its next machine
            // decrement numJobs if theJob has finished
            if (theJob != null){
            	if(theJob.getTaskQ().isEmpty()){
            	System.out.println("Job " + theJob.getId() + " has completed at "
                        + timeNow + " Total wait was " + (timeNow - theJob.getLength()));
                numJobs--;
            	} else {
                	moveToNextMachine(theJob);
                }
            } 
        }
    }

    /** output wait times at machines */
    static void outputStatistics() {
        System.out.println("Finish time = " + timeNow);
        for (int p = 1; p <= numMachines; p++) {
        	machineArray[p].printResults();
        }
    }

    /** entry point for machine shop simulator */
    public static void main(String[] args) {
        /*
         * It's vital that we (re)set this to 0 because if the simulator is called
         * multiple times (as happens in the acceptance tests), because timeNow
         * is static it ends up carrying over from the last time it was run. I'm
         * not convinced this is the best place for this to happen, though.
         */
        timeNow = 0;
        inputData(); // get machine and job data
        startShop(); // initial machine loading
        simulate(); // run all jobs through shop
        outputStatistics(); // output machine wait times
    }
}