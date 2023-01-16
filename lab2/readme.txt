Program simulira upravljačku dretvu koja statički raspoređuje zadatke uz pomoć predefinirane tablice.

Glavna dretva - ManagingThread određuje koji se zadatak pokreće
Sekundarne dretve - SimulatedThread simuliraju svaka po jednu komponentu sustava koju treba provjeriti

U kodu je hardkodirano 38 komponenti koje dobivaju svoje dretve, i program se izvršava 60 sekundi.
3x komponenta s 1s izvođenja
15x komponenta s 5s izvođenja
20x komponenta s 20s izvođenja

Na kraju izvršavanja na error stream (da je lakše izdvojiti od pomoćnih poruka) se ispisuju statistike o izvršavanju.