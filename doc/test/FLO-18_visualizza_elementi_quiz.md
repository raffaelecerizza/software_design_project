# FLO-18: Visualizza elementi Quiz

## Test 1

### Descrizione

Verificare che nella pagina del quiz venga mostrata una domanda, un'immagine di un fiore e 4 risposte alla domanda.

### Esecuzione

La presenza della domanda, dell'immagine e delle risposte è testata automaticamente dai metodi:
```kotlin
QuizFragmentTest.testQuizFragmentDisplayed()
```
```kotlin
QuizFragmentTest.testQuizDisplayed()
```
Inoltre per verificare che le immagini proposte siano effettivamente immagini di fiori, eseguire il seguente test manuale:
1. Aprire l'applicazione;
2. Cliccare sul testo sottolineato `Log In`;
3. Inserire nel box E-Mail la mail `floraleye.test@gmail.com`;
4. Inserire nel box Password la password `Floraleye2023!`;
5. Cliccare il bottone `LOG IN`;
6. Verificare che la pagina mostrata dopo il login contenga un'immagine rappresentante un fiore.

## Test 2

### Descrizione

Verificare che le risposte alla domanda siano alternative tra loro.

### Esecuzione

Test eseguito automaticamente dai metodi:
```kotlin
QuizFragmentTest.testOnlyOneCheckedRadioButton()
```
```kotlin
QuizFragmentTest.testUncheckRadioButton()
```


