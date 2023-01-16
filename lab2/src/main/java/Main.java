import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    static AtomicBoolean running = new AtomicBoolean(true);
    static int startTime = (int) System.currentTimeMillis();

    public static void main(String[] args) throws InterruptedException {
        int[] typeA = new int[]{1, 2, 3}; // T = 1s, 3 zadatka
        int[] typeB = new int[]{4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18}; // T = 5s, 15 tasks
        int[] typeC = new int[]{18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37}; // T = 20s, 20 tasks

        int[] queue = new int[]{0, 1, -1, 0, 1, -1, 0, 1, 2, -1}; // 0 = A, 1 = B, 2 = C, -1 = nothing

        Input[] inputs = getInputs(typeA, typeB, typeC);

        ManagingThread manager = new ManagingThread(inputs, typeA, typeB, typeC, queue);
        Thread managingThread = new Thread(manager);
        managingThread.start();

        int K = 2;
        SimulationThread[] simulations = new SimulationThread[inputs.length];
        Thread[] simulationThreads = new Thread[inputs.length];
        for (int i = 0; i < inputs.length; i++) {
            simulations[i] = new SimulationThread(inputs[i], K);
            simulationThreads[i] = new Thread(simulations[i]);
            simulationThreads[i].start();
        }

        // Pusti da se izvršava 60 sekundi
        int time = 0;
        while (time < 60) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            time += 1;
        }
        running.set(false);

        Thread.sleep(10000); // wait for threads to finish
        // Print stats
        for (SimulationThread simulation : simulations) {
            simulation.printStats();
        }

        calculateCumulativeStats(simulations);
    }

    public static Input[] getInputs(int[] typeA, int[] typeB, int[] typeC) {
        int numAllTasks = typeA.length + typeB.length + typeC.length;
        Input[] inputs = new Input[numAllTasks];

        for (int i = 0; i < numAllTasks; i++) {
            if (i < typeA.length) {
                inputs[i] = new Input(i, 1000, 0, startTime);
            } else if (i < typeA.length + typeB.length) {
                inputs[i] = new Input(i, 5000, 0, startTime);
            } else {
                inputs[i] = new Input(i, 20000, 0, startTime);
            }
        }
        return inputs;
    }

    public static int getRelativeTime() {
        return (int) (System.currentTimeMillis() - startTime);
    }

    public static void calculateCumulativeStats(SimulationThread[] simulationThreads) {
        int totalStateChanges = 0;
        int sumOfResponseTimes = 0;
        double totalLateResponses = 0;
        int sumOfInterruptions = 0;
        int sumOfOvertime = 0;

        for (SimulationThread simulationThread : simulationThreads) {
            totalStateChanges += simulationThread.getStats().getNumberOfStateChanges();
            sumOfResponseTimes += simulationThread.getStats().getSumOfResponseTimes();
            totalLateResponses += simulationThread.getStats().getNumberOfLateResponses();
            sumOfInterruptions += simulationThread.getStats().getNumberOfInterruptions();
            sumOfOvertime += simulationThread.getStats().getNumberOfOvertime();
        }

        System.err.println("Total number of state changes: " + totalStateChanges);
        System.err.println("Total number of late responses: " + totalLateResponses);
        System.err.println("Average response time: " + (sumOfResponseTimes / totalStateChanges));
        System.err.println("Percentage of late responses: " + (totalLateResponses / totalStateChanges));
        System.err.println("Total number of interruptions: " + sumOfInterruptions);
        System.err.println("Total number of overtimes: " + sumOfOvertime);
    }
}
