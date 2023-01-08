Program simulira višedretveno upravljanje sustavom

Glavna dretva - Main program, pokreće sve ostale dretve i daje im signal da je program gotov
Upravljačke dretve - ManagingThread provjerava svoju komponentu i simulira obrađivanje zadatka radnim čekanjem
Simulacijske dretve - SimulatedThread simuliraju svaka po jednu komponentu sustava koju treba provjeriti

U kodu je hardkodirano 10 komponenti koje dobivaju svoje dretve, i program se izvršava 20 sekundi.

Na kraju izvršavanja na error stream (da je lakše izdvojiti od pomoćnih poruka) se ispisuju statistike o izvršavanju.