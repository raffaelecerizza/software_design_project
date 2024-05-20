# FLO-38: Opzione Verifica E-Mail

## Test 1

### Descrizione

Verificare se, una volta fatta la registrazione, non si viene direttamente rimandati alle
funzionalità dell’applicazione, ma alla pagina di Login.

### Esecuzione

1. Aprire l'applicazione;
2. Inserire nel box E-Mail la mail `test@test.it`;
3. Inserire nel box Password la password `Test123?`;
4. Inserire nel box Repeat Password la password `Test123?`;
5. Cliccare sul bottone `SIGN UP`;
6. Verificare che appaia un messaggio dove si specifica che la registrazione è terminata ma occorre ancora verificare l’e-mail;
7. Cliccare sul pulsante `OK` presente nel popup mostrato;
8. Verificare che si venga rimandati alla pagina di Login.

Una volta concluso il test, eliminare l'utente appena creato tramite Firebase Authenticator console.

## Test 2

### Descrizione

Verificare se, una volta fatta la registrazione, si riceve all’indirizzo e-mail specificato la
mail di verifica dell'account (controllare anche nella casella Spam).

### Esecuzione

1. Aprire l'applicazione;
2. Inserire nel box E-Mail una propria e-mail. In particolare, usare una mail associata ad una
casella di posta a cui si ha accesso;
3. Inserire nel box Password la password `Test123?`;
4. Inserire nel box Repeat Password la password `Test123?`;
5. Cliccare sul bottone `SIGN UP`;
6. Verificare che appaia un messaggio dove si specifica che la registrazione è terminata ma occorre ancora verificare l’e-mail;
7. Aprire la casella di posta relativa alla e-mail usata per la registrazione e verificare che si
sia ricevuta una mail per la verifica dell'account.

Una volta concluso il test, eliminare l'utente appena creato tramite Firebase Authenticator console.

## Test 3

### Descrizione

Verificare se, una volta richiesto il reinvio della mail di verifica, tramite apposita
funzionalità, questa viene effettivamente ricevuta.

### Esecuzione

1. Aprire l'applicazione;
2. Inserire nel box E-Mail una propria e-mail. In particolare, usare una mail associata ad una
casella di posta a cui si ha accesso;
3. Inserire nel box Password la password `Test123?`;
4. Inserire nel box Repeat Password la password `Test123?`;
5. Cliccare sul bottone `SIGN UP`;
6. Verificare che appaia un messaggio dove si specifica che la registrazione è terminata ma occorre ancora verificare l’e-mail;
7. Cliccare sul bottone `Log In`;
8. Inserire nel box E-Mail la mail specificata prima in fase di registrazione;
9. Inserire nel box Password la password `Test123?`;
10. Cliccare sul bottone `LOG IN`;
11. Verificare che appaia un pop up dove si specifica che l'account non è stato ancora verificato, quindi
non è possibile eseguire il login;
12. Cliccare sul bottone `OK` presente nel pop up;
13. Verificare che appaia un pop up dove si comunica che la mail di verifica dell'account è stata re-inviata
con successo;
14. Aprire la casella di posta relativa alla e-mail usata per la registrazione e verificare che si
sia ricevuta un'altra mail per la verifica dell'account.

## Test 4

### Descrizione

Verificare se, una volta verificata la e-mail tramite il link inviato,
sia possibile eseguire il Login con successo.

### Esecuzione

Test eseguito automaticamente dal metodo:
```kotlin
LoginTest.loginAndLogout()
```

## Test 5

### Descrizione

Verificare che il messaggio di verifica dell’e-mail contenga un link per la verifica e che il click
su questo link consenta effettivamente il successivo login con l’e-mail verificata.

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