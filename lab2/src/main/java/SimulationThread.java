public class SimulationThread implements Runnable {
    private Input input;
    private InputStats stats;
    private int K;

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

            System.out.println("[" + Main.getRelativeTime() + "] dretva-" + (input.getIdentifier()) + " promjena " + state);

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
                System.out.println("[" + Main.getRelativeTime() + "] dretva-" + (input.getIdentifier()) + " problem, nije odgovoreno; čekam odgovor");

                stats.increaseLateResponseCounter(); // Povećaj brojač kasnih odgovora
                // Čekaj na odgovor
                while (input.getLastResponse() != state && Main.running.get()) {
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
            // Izračunaj vrijeme od početka do kraja obrade
            int time = input.getTimeOfLastResponse() - input.getTimeOfLastStateChange();

            stats.addTime(time); // Dodaj vrijeme u sumu
            stats.setBiggestTime(time); // Postavi najveće vrijeme - automatska provjera je li stvarno najveće

            int additionalDelay = generateDelay(); // Generiraj dodatnu odgodu
            int sleepUntil = input.getTimeOfLastResponse() + additionalDelay + input.getPeriod();
            int sleep = sleepUntil - Main.getRelativeTime(); // Možda je vrijeme odgode prošlo ili nema odgode
            try {
                sleep = Math.max(sleep, 0);
                System.out.println("[" + Main.getRelativeTime() + "] dretva-" + (input.getIdentifier()) + " odgovoreno (" + time + " ms), spavam " + sleep);
                // Odgodi do period + dodatna odgoda
                if(Main.running.get()) Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("[" + Main.getRelativeTime() + "] dretva-" + (input.getIdentifier()) + " završava");
    }

    public InputStats getStats() {
        return stats;
    }


}
