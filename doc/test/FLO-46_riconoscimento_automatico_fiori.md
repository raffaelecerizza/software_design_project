# FLO-46: Opzione Riconoscimento automatico fiori

## Test 1

### Descrizione

Selezionare la funzionalità relativa al riconoscimento automatico dei fiori. Selezionare un'immagine di un fiore.
Verificare che la funzionalità di riconoscimento di fiori riconosca correttamente la tipologia di fiore fotografata. 
Verificare che venga riportata anche la confidenza del riconoscimento.

### Esecuzione

Test eseguito automaticamente dal metodo:
```kotlin
PhotoFragmentTest.testFlowerIdentification()
```

## Test 2

### Descrizione

Selezionare la funzionalità relativa al riconoscimento automatico dei fiori. Selezionare un'immagine che non contiene fiori.
Verificare che la funzionalità di riconoscimento di fiori non riconosca l'immagine come contenente un fiore.

### Esecuzione

Test eseguito automaticamente dal metodo:
```kotlin
PhotoFragmentTest.testNotFlowerIdentification()
```
