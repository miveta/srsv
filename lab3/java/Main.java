import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    static boolean LAB3RT = true;
    static AtomicBoolean running = new AtomicBoolean(true);
    static int startTime = 0;

    public static int getRelativeTime() {
        return (int) (System.currentTimeMillis() - startTime);
    }

    public static void main(String[] args) {
        startTime = (int) System.currentTimeMillis();

        Input[] inputs = get40inputs();
        int K = 2;

        // Start the managing thread

        ManagingThread[] managing = new ManagingThread[inputs.length];
        SimulationThread[] simulations = new SimulationThread[inputs.length];
        Thread[] managingThreads = new Thread[inputs.length];
        Thread[] simulationThreads = new Thread[inputs.length];

        for (int i = 0; i < inputs.length; i++) {
            managing[i] = new ManagingThread(inputs[i], 0);
            managingThreads[i] = new Thread(managing[i]);
            if (LAB3RT) {
                switch (inputs[i].getPeriod()) {
                    case 1000 -> managingThreads[i].setPriority(4);
                    case 2000 -> managingThreads[i].setPriority(3);
                    case 5000 -> managingThreads[i].setPriority(2);
                    case 10000 -> managingThreads[i].setPriority(1);
                }
            }

            managingThreads[i].start();

            simulations[i] = new SimulationThread(inputs[i], K);
            simulationThreads[i] = new Thread(simulations[i]);

            if (LAB3RT) {
                simulationThreads[i].setPriority(10); // worker threads get max priority
            }
            simulationThreads[i].start();
        }

        /*
        // End simulation when "exit" is entered
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String input = scanner.nextLine();
            if (input.equals("e")) {
                running.set(false);
                break;
            }
        }
        scanner.close();
        */

        // Pusti da se izvršava 60 sekundi
        int time = 0;
        while (time < 60) {
            try {
                Thread.sleep(1000);
                System.out.println("TIME: " + time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            time += 1;
        }
        running.set(false);

        // Print stats
        for (SimulationThread simulation : simulations) {
            simulation.printStats();
        }

        calculateCumulativeStats(simulations);
    }

    public static void calculateCumulativeStats(SimulationThread[] simulationThreads) {
        int totalStateChanges = 0;
        int sumOfResponseTimes = 0;
        double totalLateResponses = 0;

        for (SimulationThread simulationThread : simulationThreads) {
            totalStateChanges += simulationThread.getStats().getNumberOfStateChanges();
            sumOfResponseTimes += simulationThread.getStats().getSumOfResponseTimes();
            totalLateResponses += simulationThread.getStats().getNumberOfLateResponses();
        }


        System.err.println("Total number of state changes: " + totalStateChanges);
        System.err.println("Total number of late responses: " + totalLateResponses);
        System.err.println("Average response time: " + (sumOfResponseTimes / totalStateChanges));
        System.err.println("Percentage of late responses: " + (totalLateResponses / totalStateChanges));
    }


    public static Input[] get40inputs() {
        int startTime = (int) System.currentTimeMillis();
        Input[] inputs = new Input[10];
        inputs[0] = new Input(1, 1000, 0, startTime);
        inputs[1] = new Input(2, 1000, 10, startTime);
        inputs[2] = new Input(3, 1000, 40, startTime);
        inputs[3] = new Input(4, 1000, 80, startTime);
        inputs[4] = new Input(5, 1000, 120, startTime);
        inputs[5] = new Input(6, 2000, 160, startTime);
        inputs[6] = new Input(7, 2000, 200, startTime);
        inputs[7] = new Input(8, 5000, 240, startTime);
        inputs[8] = new Input(9, 5000, 280, startTime);
        inputs[9] = new Input(10, 10000, 320, startTime);

        /*

        inputs[10] = new Input(11, 2000, 300, startTime);
        inputs[11] = new Input(12, 2000, 400, startTime);
        inputs[12] = new Input(13, 2000, 350, startTime);
        inputs[13] = new Input(14, 2000, 450, startTime);
        inputs[14] = new Input(15, 2000, 500, startTime);
        inputs[15] = new Input(16, 5000, 0, startTime);
        inputs[16] = new Input(17, 5000, 100, startTime);
        inputs[17] = new Input(18, 5000, 200, startTime);
        inputs[18] = new Input(19, 10000, 300, startTime);
        inputs[19] = new Input(20, 20000, 400, startTime);


        inputs[20] = new Input(21, 1000, 100, startTime);
        inputs[21] = new Input(22, 1000, 10, startTime);
        inputs[22] = new Input(23, 1000, 400, startTime);
        inputs[23] = new Input(24, 1000, 880, startTime);
        inputs[24] = new Input(25, 1000, 50, startTime);
        inputs[25] = new Input(26, 1000, 60, startTime);
        inputs[26] = new Input(27, 1000, 250, startTime);
        inputs[27] = new Input(28, 1000, 240, startTime);
        inputs[28] = new Input(29, 1000, 280, startTime);
        inputs[29] = new Input(30, 1000, 320, startTime);
        inputs[30] = new Input(31, 1000, 100, startTime);
        inputs[31] = new Input(32, 1000, 10, startTime);
        inputs[32] = new Input(33, 1000, 400, startTime);
        inputs[33] = new Input(34, 1000, 880, startTime);
        inputs[34] = new Input(35, 1000, 50, startTime);
        inputs[35] = new Input(36, 1000, 60, startTime);
        inputs[36] = new Input(37, 1000, 250, startTime);
        inputs[37] = new Input(38, 1000, 240, startTime);
        inputs[38] = new Input(39, 1000, 280, startTime);
        inputs[39] = new Input(40, 1000, 320, startTime);

         */

        return inputs;
    }
}
