package applications;

public class EventList {
	// data members
	private int[] finishTime; // finish time array
	private final int largeTime = Integer.MAX_VALUE;
	
    // constructor
    public EventList(int theNumMachines) {
        if (theNumMachines < 1)
            throw new IllegalArgumentException("number of machines must be >= 1");
        finishTime = new int[theNumMachines + 1];

        // all machines are idle, initialize with
        // large finish time
        for (int i = 1; i <= theNumMachines; i++){
            finishTime[i] = largeTime;
        }
    }

    /** @return machine for next event */
    public int nextEventMachine() {
        // find first machine to finish, this is the
        // machine with smallest finish time
        int p = 1;
        int t = finishTime[1];
        for (int i = 2; i < finishTime.length; i++)
            if (finishTime[i] < t) {// i finishes earlier
                p = i;
                t = finishTime[i];
            }
        return p;
    }

    public int nextEventTime(int machineIndex) {
        return finishTime[machineIndex];
    }

    public void setFinishTime(int theMachine) {
        finishTime[theMachine] = largeTime;
    }
    
    public void setFinishTime(int theMachine, int time) {
        finishTime[theMachine] = time;
    }

	public boolean isIdle(int p) {
		return nextEventTime(p) == largeTime;
	}
}
