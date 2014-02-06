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
    
    // top-level nested classes
    
    // data members of MachineShopSimulator
    private static int timeNow; // current time
    private static int numMachines; // number of machines
    private static int numJobs; // number of jobs
    private static Machine[] machineArray; // array of machines

    // methods
    /**
     * move theJob to machine for its next task
     * 
     * @return false iff no next task
     */
    static boolean moveToNextMachine(Job theJob) {
        LinkedQueue taskQueue = theJob.getTaskQ();
    	if (taskQueue.isEmpty()) {// no next task
            System.out.println("Job " + theJob.getId() + " has completed at "
                    + timeNow + " Total wait was " + (timeNow - theJob.getLength()));
            return false;
        } else {// theJob has a next task
                // get machine for next task
            int machineID = ((Task) taskQueue.getFrontElement()).getMachine();
            // put on machine p's wait queue
            machineArray[machineID].getJobQ().put(theJob);
            theJob.setArrivalTime(timeNow);
            // if p idle, schedule immediately
            if (machineArray[machineID].isIdle()) {// machine is idle
                changeState(machineID);
            }
            return true;
        }
    }

    /**
     * change the state of theMachine
     * 
     * @return last job run on this machine
     */
    static Job changeState(int machineID) {// Task on theMachine has finished,
                                     // schedule next one.
        Job lastJob;
        if (machineArray[machineID].getActiveJob() == null) {// in idle or change-over
                                                    // state
            lastJob = null;
            // wait over, ready for new job
            if (machineArray[machineID].getJobQ().isEmpty()) // no waiting job
                machineArray[machineID].setFinishTime();
            else {// take job off the queue and work on it
                machineArray[machineID].setActiveJob( (Job) machineArray[machineID].getJobQ()
                        .remove());
                machineArray[machineID].increaseTotalWait(timeNow
                        - machineArray[machineID].getActiveJob().getArrivalTime());
                machineArray[machineID].incrementNumTasks();
                int t = machineArray[machineID].getActiveJob().removeNextTask();
                machineArray[machineID].setFinishTime(timeNow + t);
            }
        } else {// task has just finished on machine[theMachine]
                // schedule change-over time
            lastJob = machineArray[machineID].getActiveJob();
            machineArray[machineID].setActiveJob(null);
            machineArray[machineID].setFinishTime(timeNow
                    + machineArray[machineID].getChangeTime());
        }

        return lastJob;
    }
    
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
            machineArray[i] = new Machine(keyboard.readInteger());
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
            changeState(p);
    }
    /** process all jobs to completion */
    static void simulate() {
        while (numJobs > 0) {// at least one job left
            int nextToFinish = nextEventMachine();
            timeNow = machineArray[nextToFinish].getTimeTaken();
            // change job on machine nextToFinish
            Job theJob = changeState(nextToFinish);
            // move theJob to its next machine
            // decrement numJobs if theJob has finished
            if (theJob != null && !moveToNextMachine(theJob))
                numJobs--;
        }
    }

    /** output wait times at machines */
    static void outputStatistics() {
        System.out.println("Finish time = " + timeNow);
        for (int p = 1; p <= numMachines; p++) {
        	machineArray[p].printResults(p);
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