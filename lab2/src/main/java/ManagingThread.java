
public class ManagingThread implements Runnable {
    /*
     * Which task is currently being processed
     * */
    int taskBeingProcessed = -1;

    /*
     * Some number to differentiate, the same task can occur again while the last one is still being processed
     * */
    int jobBeingProcessed = 0;

    /*
     * For how many periods has the task been running for
     * 0 - no processing
     * 1 - processing, regularly in the 1st period
     * 2 - processing, in the second period in a row
     * */
    int period = -1;

    /*
     * Number of consecutive periods without going into overtime (letting a task run into the second period)
     * Second period is allowed only after 10 periods have passed without going into overtime
     * */
    int noOvertime = 0;

    // array of tasks
    private int[] typeA, typeB, typeC;

    // array of task types to start
    private int[] queue;

    // which interrupt in a millisecond (selecting an element from the sequence[])
    private int t = 0;

    // which task of that type is currently being processed
    private int[] index;

    // number of tasks
    private int[] numberOfTasksPerType;

    private final Input[] inputs;
    private final int[] lastStates;

    public ManagingThread(Input[] inputs, int[] typeA, int[] typeB, int[] typeC, int[] queue) {
        int numberOfInputs = inputs.length;
        this.inputs = inputs;
        this.lastStates = new int[numberOfInputs];

        this.typeA = typeA;
        this.typeB = typeB;
        this.typeC = typeC;
        this.queue = queue;
        this.index = new int[3];
        this.numberOfTasksPerType = new int[3];
        this.numberOfTasksPerType[0] = typeA.length;
        this.numberOfTasksPerType[1] = typeB.length;
        this.numberOfTasksPerType[2] = typeC.length;
    }

    @Override
    public void run() {
        // start 100 ms later
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (Main.running.get()) {
            long start = System.currentTimeMillis();
            processTask();
            long end = System.currentTimeMillis();
            long waitTime = 100 - (end - start);
            if (waitTime > 0) {
                sleepMs((int) waitTime);
            }
        }
    }

    public void processTask() {
        loop:
        {
            if (taskBeingProcessed != -1) {
                // Something is being processed already
                // are we interrupting the task or not?
                if (period == 1 && noOvertime >= 10) {
                    // Note that we're into overtime
                    inputs[taskBeingProcessed].increaseNumberOfOvertime();

                    System.out.println("[" + Main.getRelativeTime() +
                            "] - upravljač dozvoljava drugu periodu zadatku " + taskBeingProcessed);

                    period = 2; // we're in the second consecutive period
                    noOvertime = 0; // we let something in overtime

                    // return from this function - just continue processing the task
                    return; // ? is this okay
                }

                // interrupt current processing and start the next task
                // code below?
                inputs[taskBeingProcessed].increaseNumberOfInterruptions(); // note that we interrupted the task

                System.out.println("[" + Main.getRelativeTime() +
                        "] - upravljač prekida zadatak " + taskBeingProcessed);

            }

            boolean takeNext = true;
            while (takeNext) {
                takeNext = false;

                taskBeingProcessed = getTaskToProcess();
                int myTask = taskBeingProcessed;

                if (myTask == -1 || inputs[myTask].getState() == lastStates[myTask]) {
                    // no change in input for this task
                    taskBeingProcessed = -1;
                    jobBeingProcessed = 0;

                    return; // return from this function?
                }

                // some unique number, e.g., current time in ms
                jobBeingProcessed = (int) System.currentTimeMillis();
                int myJob = jobBeingProcessed;
                period = 1;

                // Note that we started processing the task
                // todo - input stats
                long start = Main.getRelativeTime();
                inputs[myTask].setTimeOfLastProcessingStart((int) start);

                System.out.println("[" + Main.getRelativeTime() +
                        "] - upravljač započinje obradu zadatka " + myTask);

                // simulate task processing
                int processingTime = getProcessingTime();
                sleepMs(processingTime);

                // enable thread interruptions - if needed
                while (myJob == jobBeingProcessed && processingTime > 0) {
                    try {

                        if (Main.getRelativeTime() - start > 100) {
                            break loop;
                        }
                        // break if it's more than 100 ms?
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        break;
                    }

                    processingTime -= 5;
                }
                // disable thread interruptions - if needed

                // determine if the task was finished or interrupted
                if (myJob == jobBeingProcessed && processingTime <= 0) {
                    lastStates[myTask] = inputs[myTask].getState(); // last response = state, task is up to date
                    inputs[myTask].setLastResponse(lastStates[myTask]);

                    // Note the end of processing
                    inputs[myTask].setTimeOfLastProcessingEnd(Main.getRelativeTime());

                    System.out.println("[" + Main.getRelativeTime() +
                            "] - upravljač gotova obrada zadatka " + myTask);

                    // reset the task
                    taskBeingProcessed = -1;
                    jobBeingProcessed = 0;

                    // reset the period
                    if (period == 1) {
                        noOvertime++;
                    } else {
                        // task spent a part of the second period, start the next task now
                        takeNext = true;
                    }
                } else {
                    // the task was interrupted, leave this function
                    return;
                }

                // enable interruptions - if needed
            }
        }
    }

    // scheduling task using a table of tasks
    public int getTaskToProcess() {
        int task = 0;
        int type = queue[t];

        t = (t + 1) % 10;
        if (type != -1) {
            int[] typeArray = getTaskType(type);
            task = typeArray[index[type]];
            index[type] = (index[type] + 1) % numberOfTasksPerType[type];
        }

        return task - 1;
    }

    public int[] getTaskType(int type) {
        if (type == 0) {
            return typeA;
        } else if (type == 1) {
            return typeB;
        } else if (type == 2) {
            return typeC;
        } else {
            return null;
        }
    }

    /*
     * Odredi trajanje obrade po vjerojatnostima
     * 30ms (20%), 50ms (50%), 80ms (20%), 120ms (10%)
     * */
    private int getProcessingTime() {
        int[] numbers = {60, 100, 130, 170};
        int[] probabilities = {20, 70, 90, 100}; // relative probabilities

        int random = (int) (Math.random() * 100);
        int sleepTime = 0;
        for (int i = 0; i < numbers.length; i++) {
            if (random < probabilities[i]) {
                sleepTime = numbers[i];
            } else {
                break;
            }
        }

        return sleepTime;
    }

    private void sleepMs(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
