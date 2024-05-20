# FLO-38: Opzione Verifica E-Mail

## Test 1

### Descrizione

Verificare che la lista di fiori non presenti risultati selezionati incorrettamente.

### Esecuzione

Test eseguito automaticamente dai metodi:
```kotlin
FilterBottomSheetTest.testNotExistingFiltering
```
```kotlin
FilterBottomSheetTest.testExistingFiltering
```

Per verifica manuale:
1. Aprire l'applicazione;
2. Cliccare sul testo sottolineato `Log In`;
3. Inserire nel box E-Mail la mail `floraleye.test@gmail.com`;
4. Inserire nel box Password la password `Floraleye2023!`;
5. Cliccare il bottone `LOG IN`;
6. Verificare che l'onboard avvenga con successo e si sia rimandati alle funzionalità dell'applicazione;
7. Navigare fino alla sezione `Dictionary`;
8. Cliccare sull'icona relativa al filtro nell'angolo in alto a destra;
9. Inserire nel text box `Genus` la stringa `iris`;
10. Cliccare sul bottone `APPLY`;
11. Verificare se tutti i fiori mostrati hanno campo `Genus` uguale ad `iris`.