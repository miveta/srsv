Program simulira upravljačku petlju koja provjerava sve komponente sustava.

Glavna dretva - ManagingThread provjerava sve komponente
Sekundarne dretve - SimulatedThread simuliraju svaka po jednu komponentu sustava koju treba provjeriti

U kodu je hardkodirano 40 komponenti koje dobivaju svoje dretve, i program se izvršava 60 sekundi.

Na kraju izvršavanja na error stream (da je lakše izdvojiti od pomoćnih poruka) se ispisuju statistike o izvršavanju.