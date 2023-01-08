import java.util.Random;

public class ManagingThread implements Runnable {
    private final Input input;
    private int lastState;

    private int[] numIterations; // Number of iterations for each of the different task processing times

    private final Random random = new Random();

    private int number;

    public ManagingThread(Input input, int lastState) {
        this.input = input;
        this.lastState = lastState;

        determineTaskProcessingTimes(); // Determine the number of iterations for each of the different task processing times
    }

    @Override
    public void run() {
        while (Main.running.get()) {
            int state = input.getState(); // Dohvati stanje ulaza

            if (state != lastState) {
                System.out.println("[" + input.getRelativeTime() + "] upr: ulaz:" + (input.getIdentifier() + 1) + " promjena(" + lastState + "->" + state + "), obrađujem");

                simulateProcessing(); // Simuliraj trajanje obrade
                input.setLastResponse(state); // Postavi odgovor i trenutak zadnjeg odgovora
                // (automatski u klasi Input)
                lastState = state; // Ažuriraj zadnje stanje

                System.out.println("[" + input.getRelativeTime() + "] upr: ulaz:" + (input.getIdentifier()+ 1) + " kraj obrade, postavljeno " + state);
            } else {
                //System.out.println("[" + Main.getRelativeTime() + "] upr: ulaz:" + (i + 1) + " nema promjene (" + state + ")");
            }

        }

        System.out.println("[" + input.getRelativeTime() + "] upr: završeno");
    }



    private void simulateProcessing() {
        int numIterations = getNumIterations();
        simulateXms(numIterations);
    }

    /*
     * Waste 10 ms of time
     * */
    public void waste10ms(int number) {
        for (int i = 0; i < number; i++) {
            int time = (int) System.currentTimeMillis(); // do nothing, just waste time
        }
    }

    public void waste10ms() {
        waste10ms(this.number);
    }

    /*
     * Simulate task processing time
     * */
    public void simulateXms(int X) {
        for (int i = 0; i < X / 10; i++) {
            waste10ms();
        }
    }


    /*
     * Get the number of iterations based on random probability
     * */
    private int getNumIterations() {
        /*
         * 50% prob - 10% period
         * 30% prob - 20% period
         * 15% prob - 40% period
         * 5% prob - 70% period
         * */
        int randomInt = random.nextInt(100);
        if (randomInt < 50) {
            return numIterations[0];
        } else if (randomInt < 80) {
            return numIterations[1];
        } else if (randomInt < 95) {
            return numIterations[2];
        } else {
            return numIterations[3];
        }
    }

    /*
     * For each of the possible task processing times - 10%, 20%, 40%, 70% of period
     * calculate the number of iterations that should be done in that time
     * */
    private void determineTaskProcessingTimes(){
        int period = input.getPeriod();

        int numIterations10 = determineNumIterations(10);
        this.number = numIterations10;
        numIterations = new int[4];
        numIterations[0] = (int) (period * 0.1);
        numIterations[1] = (int) (period * 0.2);
        numIterations[2] = (int) (period * 0.4);
        numIterations[3] = (int) (period * 0.7);
    }

    /*
     * Get the number of iterations needed to waste a certain number of milliseconds
     * */
    public int determineNumIterations(int ms) {
        int number = 1000000;
        boolean run = true;

        int t0 = 0, t1 = 1;
        while (run) {
            t0 = (int) System.currentTimeMillis();
            waste10ms(number);
            t1 = (int) System.currentTimeMillis();

            if (t1 - t0 >= ms) {
                run = false;
            } else {
                number *= 10;
            }
        }

        number = number * 10 / (t1 - t0);

        return number;
    }


}



