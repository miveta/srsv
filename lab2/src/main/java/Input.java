/*
 * static:
 *   identifier (1,2,...)
 *   period (time)
 *   first appearance - when the event first appears (0-period)
 * dynamic:
 *   state -- written by simulator
 *   time of last state change -- written by simulator
 *   last response -- written by manager
 *   time of last response -- written by manager
 *   stats -- written by simulator
 *       - number of state changes
 *       - average response time
 *       - max time between state change and response
 *       - number of late responses
 * */
public class Input {
    private final int identifier;
    /**
     * Period when the input must be checked minimally once - 1, 2, 5, 10, 20.
     */
    private final int period;
    /**
     * Time when the input first appears relative to the program start.
     */
    private final int firstAppearance;
    private int state = 0;
    private int timeOfLastStateChange = -1;
    private int lastResponse = 0;
    private int timeOfLastResponse = -1;
    private int numberOfInterruptions = 0;
    private int numberOfOvertime = 0;

    private int timeOfLastProcessingStart = -1;

    private int timeOfLastProcessingEnd = -1;
    private final int startTime;

    public Input(int identifier, int period, int firstAppearance, int startTime) {
        this.identifier = identifier;
        this.period = period;
        this.firstAppearance = firstAppearance;
        this.startTime = startTime;
    }

    public int getRelativeTime() {
        return (int) (System.currentTimeMillis() - startTime);
    }

    public int getIdentifier() {
        return identifier;
    }

    public int getPeriod() {
        return period;
    }

    public int getFirstAppearance() {
        return firstAppearance;
    }

    public int getNumberOfInterruptions() {
        return numberOfInterruptions;
    }

    public void increaseNumberOfInterruptions() {
        numberOfInterruptions++;
    }

    public int getNumberOfOvertime() {
        return numberOfOvertime;
    }

    public void increaseNumberOfOvertime() {
        numberOfOvertime++;
    }

    // region state
    public void setState(int state) {
        this.state = state;
        this.timeOfLastStateChange = getRelativeTime();
    }

    public int getState() {
        return state;
    }

    public int getTimeOfLastStateChange() {
        return timeOfLastStateChange;
    }
    // endregion

    // region lastResponse
    public void setLastResponse(int lastResponse) {
        this.lastResponse = lastResponse;
        this.timeOfLastResponse = getRelativeTime();
    }

    public int getLastResponse() {
        return lastResponse;
    }

    public int getTimeOfLastResponse() {
        return timeOfLastResponse;
    }
    // endregion

    // region proccesing times
    public int getTimeOfLastProcessingStart() {
        return timeOfLastProcessingStart;
    }

    public void setTimeOfLastProcessingStart(int timeOfLastProcessingStart) {
        this.timeOfLastProcessingStart = timeOfLastProcessingStart;
    }

    public int getTimeOfLastProcessingEnd() {
        return timeOfLastProcessingEnd;
    }

    public void setTimeOfLastProcessingEnd(int timeOfLastProcessingEnd) {
        this.timeOfLastProcessingEnd = timeOfLastProcessingEnd;
    }
    // endregion
}
