import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    static AtomicBoolean running = new AtomicBoolean(true);
    static int startTime = 0;

    public static int getRelativeTime() {
        return (int) (System.currentTimeMillis() - startTime);
    }

    public static void main(String[] args) {
        startTime = (int) System.currentTimeMillis();

        Input[] inputs = get40inputs();
        int K = 5;

        // Start the managing thread
        ManagingThread managing = new ManagingThread(inputs);
        Thread managingThread = new Thread(managing);
        managingThread.start();


        SimulationThread[] simulations = new SimulationThread[inputs.length];
        Thread[] simulationThreads = new Thread[inputs.length];
        for (int i = 0; i < inputs.length; i++) {
            simulations[i] = new SimulationThread(inputs[i], K);
            simulationThreads[i] = new Thread(simulations[i]);
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
        while(time < 60) {
            try {
                Thread.sleep(1000);
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

    public static void calculateCumulativeStats(SimulationThread[] simulationThreads){
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


    public static Input[] get40inputs(){
        Input[] inputs = new Input[40];
        inputs[0] = new Input(1, 1000, 0);
        inputs[1] = new Input(2, 1000, 10);
        inputs[2] = new Input(3, 1000, 40);
        inputs[3] = new Input(4, 1000, 80);
        inputs[4] = new Input(5, 1000, 120);
        inputs[5] = new Input(6, 1000, 160);
        inputs[6] = new Input(7, 1000, 200);
        inputs[7] = new Input(8, 1000, 240);
        inputs[8] = new Input(9, 1000, 280);
        inputs[9] = new Input(10, 1000, 320);
        inputs[10] = new Input(11, 2000, 300);
        inputs[11] = new Input(12, 2000, 400);
        inputs[12] = new Input(13, 2000, 350);
        inputs[13] = new Input(14, 2000, 450);
        inputs[14] = new Input(15, 2000, 500);
        inputs[15] = new Input(16, 5000, 0);
        inputs[16] = new Input(17, 5000, 100);
        inputs[17] = new Input(18, 5000, 200);
        inputs[18] = new Input(19, 10000, 300);
        inputs[19] = new Input(20, 20000, 400);

        inputs[20] = new Input(21, 1000, 100);
        inputs[21] = new Input(22, 1000, 10);
        inputs[22] = new Input(23, 1000, 400);
        inputs[23] = new Input(24, 1000, 880);
        inputs[24] = new Input(25, 1000, 50);
        inputs[25] = new Input(26, 1000, 60);
        inputs[26] = new Input(27, 1000, 250);
        inputs[27] = new Input(28, 1000, 240);
        inputs[28] = new Input(29, 1000, 280);
        inputs[29] = new Input(30, 1000, 320);
        inputs[30] = new Input(31, 1000, 100);
        inputs[31] = new Input(32, 1000, 10);
        inputs[32] = new Input(33, 1000, 400);
        inputs[33] = new Input(34, 1000, 880);
        inputs[34] = new Input(35, 1000, 50);
        inputs[35] = new Input(36, 1000, 60);
        inputs[36] = new Input(37, 1000, 250);
        inputs[37] = new Input(38, 1000, 240);
        inputs[38] = new Input(39, 1000, 280);
        inputs[39] = new Input(40, 1000, 320);

        return inputs;
    }
}
