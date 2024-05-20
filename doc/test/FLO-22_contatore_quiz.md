# FLO-22: Contatore Quiz

## Test 1

### Descrizione

Verificare che la risposta corretta di un utente a un quiz comporti un incremento del numero di risposte totali e
del numero di risposte corrette mostrato nel Profilo dell'utente. Il numero di risposte errate deve rimanere invariato.

### Esecuzione

1. Aprire l'applicazione;
2. Cliccare sul testo sottolineato `Log In`;
3. Inserire nel box E-Mail la mail `floraleye.empty@gmail.com`;
4. Inserire nel box Password la password `Test123?`;
5. Cliccare il bottone `LOG IN`;
6. Verificare che l'onboard avvenga con successo e si sia rimandati alla sezione del profilo;
7. Verificare che nella sezione Quiz Score vengano mostrate le seguenti stringhe di testo:
- "Total: 0";
- "Correct: 0";
- "Wrong: 0";
7. Cliccare sul simbolo del quiz nella bottom navigation bar (il primo simbolo a sinistra nella barra);
8. Verificare che venga mostrato un quiz nel tab New della sezione Quiz;
9. Cliccare sul radio button rappresentante la soluzione del quiz;
10. Cliccare sul bottone `SUBMIT`;
11. Verificare che la CardView del radio button selezionato abbia il bordo verde;
12. Cliccare sul simbolo del profilo nella bottom navigation bar (l'ultimo simbolo a destra nella barra);
13. Verificare che nella sezione Quiz Score vengano mostrate le seguenti stringhe di testo:
- "Total: 1";
- "Correct: 1";
- "Wrong: 0";

## Test 2

### Descrizione

Verificare che la risposta errata di un utente a un quiz comporti un incremento del numero di risposte totali e
del numero di risposte errate mostrato nel Profilo dell'utente. Il numero di risposte corrette deve rimanere invariato.

### Esecuzione

1. Aprire l'applicazione;
2. Cliccare sul testo sottolineato `Log In`;
3. Inserire nel box E-Mail la mail `floraleye.empty@gmail.com`;
4. Inserire nel box Password la password `Test123?`;
5. Cliccare il bottone `LOG IN`;
6. Verificare che l'onboard avvenga con successo e si sia rimandati alla sezione del profilo;
7. Verificare che nella sezione Quiz Score vengano mostrate le seguenti stringhe di testo:
- "Total: 0";
- "Correct: 0";
- "Wrong: 0";
7. Cliccare sul simbolo del quiz nella bottom navigation bar (il primo simbolo a sinistra nella barra);
8. Verificare che venga mostrato un quiz nel tab New della sezione Quiz;
9. Cliccare sul radio button rappresentante una risposta errata al quiz;
10. Cliccare sul bottone `SUBMIT`;
11. Verificare che la CardView del radio button selezionato abbia il bordo rosso;
12. Cliccare sul simbolo del profilo nella bottom navigation bar (l'ultimo simbolo a destra nella barra);
13. Verificare che nella sezione Quiz Score vengano mostrate le seguenti stringhe di testo:
- "Total: 1";
- "Correct: 0";
- "Wrong: 1";

## Test 3

### Descrizione

Verificare che per un nuovo utente il numero di risposte totali, corrette ed errate mostrato nel Profilo dell'utente sia
pari a zero. 

### Esecuzione

Test eseguito automaticamente dal metodo:
```kotlin
ProfileFragmentTest.testScoreEmptyQuizHistory()
```
