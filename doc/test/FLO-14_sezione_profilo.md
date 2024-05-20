# FLO-14: Sezione Profilo

## Test 1

### Descrizione

Verificare se nella sezione Profilo è mostrata la e-mail specificata in fase di registrazione.

### Esecuzione

Test eseguito automaticamente dal metodo:
```kotlin
ProfileTest.isMailShowed()
```

## Test 2

### Descrizione

Provare a modificare la password, e verificare che la modifica sia possibile solo se la nuova
password rispetta tutti i criteri di sicurezza.

### Esecuzione

Test eseguito automaticamente dai metodi:
```kotlin
ProfileTest.changePasswordWithWeaK()
```
```kotlin
ProfileTest.changePassword()
```

## Test 3

### Descrizione

Eseguire il logout, e verificare se si viene rimandati alla pagina di login/registrazione.
Dopo il logout, chiudere e riaprire l’app, e verificare che non si venga reindirizzati subito
alle funzionalità dell'applicazione, ma venga prima chiesto il login/registrazione.

### Esecuzione

1. Aprire l'applicazione;
2. Cliccare sul testo sottolineato `Log In`;
3. Inserire nel box E-Mail la mail `floraleye.test@gmail.com`;
4. Inserire nel box Password la password `Floraleye2023!`;
5. Cliccare il bottone `LOG IN`;
6. Verificare che l'onboard avvenga con successo e si sia rimandati alle funzionalità dell'applicazione;
7. Navigare fino alla sezione Profile;
8. Cliccare i tre puntini in alto a destra;
9. Cliccare `Log Out`;
10. Verificare che si venga rimandati alla pagina di onboard;
11. Chiudere l'applicazione da task manager;
12. Riaprire l'applicazione;
13. Verificare che questa volta non si venga mandati direttamente alle funzionalità dell'applicazione,
ma venga richiesto di eseguire l'onboard.

## Test 4

### Descrizione

Provare ad eseguire il login con le vecchie credenziali (quelle precedenti alla modifica della password),
e verificare che con esse non sia possibile accedere alle funzionalità dell’applicazione. Provare ad
eseguire il login con le nuove credenziali, e verificare che con queste sia possibile accedere alle
funzionalità dell’applicazione.

### Esecuzione

Test eseguito automaticamente dal metodo:
```kotlin
ProfileTest.changePassword()
```

## Test 5

### Descrizione

Eliminare il proprio Profilo, e verificare se si viene rimandati alla pagina di login/registrazione.
Tentare di eseguire il login con le credenziali associate al profilo appena eliminato,
e verificare che non sia possibile autenticarsi con queste.

### Esecuzione

1. Aprire l'applicazione;
2. Inserire nel box E-Mail una propria e-mail. In particolare, usare una mail associata ad una
casella di posta a cui si ha accesso;
3. Inserire nel box Password la password `Test123?`;
4. Inserire nel box Repeat Password la password `Test123?`;
5. Cliccare sul bottone `SIGN UP`;
6. Verificare che appaia un messaggio dove si specifica che la registrazione è terminata ma occorre ancora verificare l’e-mail;
7. Cliccare sul pulsante `OK` presente nel popup mostrato;
8. Aprire la casella di posta relativa alla e-mail usata per la registrazione e verificare che si
sia ricevuta una mail per la verifica dell'account;
9. Verificare che la mail ricevuta contenga un link su cui sia possibile effettuare un click;
10. Effettuare un click sul link contenuto nella mail ricevuta;
11. Verificare che venga mostrata una pagina che segnali l’avvenuta verifica dell’e-mail;
12. Tornare sull’applicazione che ora mostra la pagina di Login;
13. Inserire nel box E-Mail la mail specificata prima in fase di registrazione;
14. Inserire nel box Password la password `Test123?`;
15. Cliccare sul bottone `LOG IN`;
16. Verificare che l'onboard avvenga con successo e si sia rimandati alle funzionalità dell'applicazione.
17. Navigare fino alla sezione Profile;
18. Cliccare sul bottone `DELETE USER`;
19. Cliccare `OK` sul messaggio di conferma eliminazione che appare;
20. Verificare che si venga rimandati alla pagina di onboard;
21. Cliccare sul testo `Log In`;
22. Inserire nel box E-Mail la mail specificata prima in fase di registrazione;
23. Inserire nel box Password la password `Test123?`;
24. Cliccare sul bottone `LOG IN`;
25. Verificare che appaia un messaggio di errore dove si specifica che la e-mail utilizzata non
è associata ad alcun account esistente.
