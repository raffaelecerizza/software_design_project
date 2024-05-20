# FLO-21: Storico Quiz

## Test 1

### Descrizione

Verificare che dalla sezione Profile sia possibile visualizzare i quiz svolti precedentemente dall'utente.

### Esecuzione

Test eseguito automaticamente dal metodo:
```kotlin
OldQuizzesFragmentTest.testQuizHistoryFromProfile()
```

## Test 2

### Descrizione

Verificare che per ogni quiz vengano mostrate le seguenti informazioni:
- la domanda del quiz;
- l'immagine del quiz svolto;
- la risposta data dall'utente;
- la soluzione del quiz;
- un riferimento temporale della risposta data dall'utente.

### Esecuzione

1. Aprire l'applicazione;
2. Cliccare sul testo sottolineato `Log In`;
3. Inserire nel box E-Mail la mail `floraleye.test@gmail.com`;
4. Inserire nel box Password la password `Floraleye2023!`;
5. Cliccare il bottone `LOG IN`;
6. Verificare che l'onboard avvenga con successo e si sia rimandati alla sezione del profilo;
7. Cliccare sul simbolo del quiz nella bottom navigation bar (il primo simbolo a sinistra nella barra);
8. Verificare che venga mostrato un quiz nel tab New della sezione Quiz;
9. Cliccare sul primo radio button rappresentante una possibile risposta del quiz;
10. Cliccare sul bottone `SUBMIT`;
11. Tenere a mente la risposta selezionata e la risposta mostrata come corretta (corrispondente al radio button la cui CardView ha il bordo verde);
12. Eseguire uno swipe verso sinistra;
13. Verificare che venga aperto il tab Old della sezione Quiz;
14. Verificare che venga mostrato il quiz appena svolto contenente le seguenti informazioni:
- la domanda del quiz;
- l'immagine del quiz svolto;
- la risposta selezionata;
- la soluzione del quiz;
- un riferimento temporale della risposta data dall'utente.

Test eseguito automaticamente dal metodo:
```kotlin
OldQuizzesFragmentTest.testOldQuizDisplayed()
```

## Test 3

### Descrizione

Verificare che i quiz dello storico siano ordinati dal più recente al più risalente.

### Esecuzione

1. Aprire l'applicazione;
2. Cliccare sul testo sottolineato `Log In`;
3. Inserire nel box E-Mail la mail `floraleye.test@gmail.com`;
4. Inserire nel box Password la password `Floraleye2023!`;
5. Cliccare il bottone `LOG IN`;
6. Verificare che l'onboard avvenga con successo e si sia rimandati alla sezione del profilo;
7. Cliccare sul simbolo del quiz nella bottom navigation bar (il primo simbolo a sinistra nella barra);
8. Verificare che venga mostrato un quiz nel tab New della sezione Quiz;
9. Cliccare sul primo radio button rappresentante una possibile risposta del quiz;
10. Cliccare sul bottone `SUBMIT`;
11. Cliccare sul bottone `NEXT`;
12. Verificare che venga mostrato un nuovo quiz nel tab New della sezione Quiz;
13. Cliccare sul primo radio button rappresentante una possibile risposta del quiz;
14. Cliccare sul bottone `SUBMIT`;
15. Cliccare sul bottone `NEXT`;
16. Verificare che venga mostrato un nuovo quiz nel tab New della sezione Quiz;
17. Cliccare sul primo radio button rappresentante una possibile risposta del quiz;
18. Cliccare sul bottone `SUBMIT`;
19. Eseguire uno swipe verso sinistra;
20. Verificare che venga aperto il tab Old della sezione Quiz;
21. Verificare che vengano mostrate le informazioni relative ai tre quiz sottomessi e in particolare il riferimento temporale della risposta;
22. Verificare che i tre quiz sottomessi siano ordinati dal più recente al più risalente e più precisamente:
- l'ultimo quiz sottomesso è in cima alla lista dei quiz;
- il secondo quiz sottomesso è il secondo quiz della lista;
- il primo quiz sottomesso è il terzo quiz della lista.

