# FLO-49: Opzione Acquisizione fotografia fiori

## Test 1

### Descrizione

Selezionare la funzionalità relativa al riconoscimento automatico dei fiori. Verificare che
all’utente venga chiesto se vuole scattare una nuova fotografia oppure utilizzarne
una scattata in precedenza.

### Esecuzione

Test eseguito automaticamente dal metodo:
```kotlin
PhotoFragmentTest.testChoosePhotoPopUp()
```

## Test 2

### Descrizione

Selezionare la funzionalità relativa al riconoscimento automatico dei fiori. Selezionare la
funzionalità relativa alla possibilità di scattare una fotografia.
Scattare una fotografia. Verificare che la fotografia sia mostrata all'utente.

### Esecuzione

1. Aprire l'applicazione;
2. Cliccare sul testo sottolineato `Log In`;
3. Inserire nel box E-Mail la mail `floraleye.test@gmail.com`;
4. Inserire nel box Password la password `Floraleye2023!`;
5. Cliccare il bottone `LOG IN`;
6. Verificare che l'onboard avvenga con successo e si sia rimandati alle funzionalità dell'applicazione;
7. Navigare fino alla sezione `Photo`;
8. Cliccare sul bottone `CHOOSE PHOTO`;
9. Nel pop up che appare cliccare sul bottone `TAKE`;
10. Scattare una fotografia tramite fotocamera del dispositivo;
11. Verificare che l'immagine appena scattare sia mostrata nella sezione `Photo`.

Si noti che è bene eseguire questo test con un dispositivo fisico, non emulato.

## Test 3

### Descrizione

Selezionare la funzionalità relativa al riconoscimento automatico dei fiori. Selezionare la
funzionalità relativa alla possibilità di caricare una fotografia dalla galleria. Verificare che
la fotografia sia mostrata all'utente.

### Esecuzione

1. Aprire l'applicazione;
2. Cliccare sul testo sottolineato `Log In`;
3. Inserire nel box E-Mail la mail `floraleye.test@gmail.com`;
4. Inserire nel box Password la password `Floraleye2023!`;
5. Cliccare il bottone `LOG IN`;
6. Verificare che l'onboard avvenga con successo e si sia rimandati alle funzionalità dell'applicazione;
7. Navigare fino alla sezione `Photo`;
8. Cliccare sul bottone `CHOOSE PHOTO`;
9. Nel pop up che appare cliccare sul bottone `LOAD`;
10. Selezionare una immagine dalla galleria tramite la finestra che appare;
11. Verificare che l'immagine appena selezionata sia mostrata nella sezione `Photo`.

Si noti che è bene eseguire questo test con un dispositivo fisico, non emulato.
