public class ManagingThread implements Runnable {
    private final int numberOfInputs;
    private final Input[] inputs;
    private final int[] lastStates;

    public ManagingThread(Input[] inputs) {
        this.numberOfInputs = inputs.length;
        this.inputs = inputs;
        this.lastStates = new int[numberOfInputs];
    }

    @Override
    public void run() {
        // Pričekaj početak rada ulaza "i"
        try {
            Thread.sleep(input.getFirstAppearance() + 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (Main.running.get()) {
            for (int i = 0; i < numberOfInputs; i++) {
                int state = inputs[i].getState(); // Dohvati stanje ulaza
                int lastState = lastStates[i];
                if (state != lastState) {
                    System.out.println("[" + Main.getRelativeTime() + "] upr: ulaz:" + (i + 1) + " promjena(" + lastState + "->" + state + "), obrađujem");

                    simulateProcessing(); // Simuliraj trajanje obrade
                    inputs[i].setLastResponse(state); // Postavi odgovor i trenutak zadnjeg odgovora
                    // (automatski u klasi Input)
                    lastStates[i] = state; // Ažuriraj zadnje stanje

                    // pricekaj pocetak sljedece periode
                    long timeToSleep = input.getPeriod() - input.getRelativeTime() % input.getPeriod();
                    // cekaj periodu
                    // long timeToSleep = input.getPeriod();
                    // System.err.println("Period = " + input.getPeriod() + "TTS = " + timeToSleep);
                    try {
                        Thread.sleep(timeToSleep);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println("[" + Main.getRelativeTime() + "] upr: ulaz:" + (i + 1) + " kraj obrade, postavljeno " + state);
                } else {
                    //System.out.println("[" + Main.getRelativeTime() + "] upr: ulaz:" + (i + 1) + " nema promjene (" + state + ")");
                }
            }

        }

        System.out.println("[" + Main.getRelativeTime() + "] upr: završeno");
    }


    /*
     * Simuliraj trajanje obrade po vjerojatnostima
     * 30ms (20%), 50ms (50%), 80ms (20%), 120ms (10%)
     * */
    private void simulateProcessing() {
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

        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}



