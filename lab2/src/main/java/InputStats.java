public class InputStats {
    private Input input;
    private int numberOfStateChanges = 0;
    private int sumOfResponseTimes = 0;
    private int maxTimeBetweenStateChangeAndResponse = 0;
    private int numberOfLateResponses = 0;

    public InputStats(Input input) {
        this.input = input;
    }

    public void increaseStateChangesCounter() {
        numberOfStateChanges++;
    }

    public void addTime(int time) {
        sumOfResponseTimes += time;
    }

    public void setBiggestTime(int time) {
        if (time > 0) {
            if (time > maxTimeBetweenStateChangeAndResponse) {
                maxTimeBetweenStateChangeAndResponse = time;
            }
        }
    }

    public int getNumberOfInterruptions() {
        return input.getNumberOfInterruptions();
    }

    public int getNumberOfOvertime() {
        return input.getNumberOfOvertime();
    }

    public void increaseLateResponseCounter() {
        numberOfLateResponses++;
    }

    public int getNumberOfStateChanges() {
        return numberOfStateChanges;
    }

    public int getNumberOfLateResponses() {
        return numberOfLateResponses;
    }

    public int getSumOfResponseTimes() {
        return sumOfResponseTimes;
    }

    public void printStats() {
        System.err.println("Input " + input.getIdentifier() + " stats:");
        System.err.println("Number of state changes: " + numberOfStateChanges);
        System.err.println("Average response time: " + (numberOfStateChanges != 0 ? sumOfResponseTimes / numberOfStateChanges : 0));
        System.err.println("Max time between state change and response: " + maxTimeBetweenStateChangeAndResponse);
        System.err.println("Number of late responses: " + numberOfLateResponses);
    }
}