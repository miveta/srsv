import java.util.Random;

public class SimulationThread implements Runnable {
    private Input input;
    private InputStats stats;

    private int K;


    private final Random random = new Random();

    public SimulationThread(Input input, int K) {
        this.input = input;
        this.stats = new InputStats(this.input);
        this.K = K;
    }

    public int generateState() {
        return (int) (Math.random() * 900 + 100);
    }
    public int generateDelay() {
        return (int) (Math.random() * K) * input.getPeriod();
    }

    public void printStats() {
        stats.printStats();
    }

    @Override
    public void run() {
        // Pričekaj početak rada ulaza "i"
        try {
            Thread.sleep(input.getFirstAppearance());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Dok nije označen kraj simulacije
        while (Main.running.get()) {
            int state = generateState(); // Generiraj novo stanje

            System.out.println("[" + input.getRelativeTime() + "] dretva-" + (input.getIdentifier()) + " promjena " + state);

            input.setState(state); // Postavi novo stanje
            // Ažuriraj trenutak promjene stanja - automatski s promjenom stanja
            stats.increaseStateChangesCounter(); // Povećaj broj promjena stanja

            // Odgodi izvođenje za jedan period
            try {
                Thread.sleep(input.getPeriod());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Ako nije pristigao odgovor na novo stanje
            if (input.getLastResponse() != state) {
                System.out.println("[" + input.getRelativeTime() + "] dretva-" + (input.getIdentifier()) + " problem, nije odgovoreno; čekam odgovor");

                stats.increaseLateResponseCounter(); // Povećaj brojač kasnih odgovora
                // Čekaj na odgovor
                while (input.getLastResponse() != state) {
                    try {
                        Thread.sleep(1); // Vrlo kratko spavaj
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Odgovor je stigao
            int response = input.getLastResponse();

            // Izračunaj vrijeme od promjene stanja do odgovora
            int time = input.getTimeOfLastResponse() - input.getTimeOfLastStateChange();

            stats.addTime(time); // Dodaj vrijeme u sumu
            stats.setBiggestTime(time); // Postavi najveće vrijeme - automatska provjera je li stvarno najveće

            int additionalDelay = generateDelay(); // Generiraj dodatnu odgodu
            int sleepUntil = input.getTimeOfLastResponse() + additionalDelay + input.getPeriod();
            int sleep = sleepUntil - input.getRelativeTime(); // Možda je vrijeme odgode prošlo ili nema odgode
            try {
                sleep = Math.max(sleep, 0);
                System.out.println("[" + input.getRelativeTime() + "] dretva-" + (input.getIdentifier()) + " odgovoreno (" + time + " ms), spavam " + sleep);
                // Odgodi do period + dodatna odgoda
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("[" + input.getRelativeTime() + "] dretva-" + (input.getIdentifier()) + " završava");
    }

    public InputStats getStats() {
        return stats;
    }

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
}
