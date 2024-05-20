# FLO-9: Opzione Registrazione

## Test 1

### Descrizione

Verifica Registrazione con mail o password che non rispettano i criteri di sicurezza su di esse imposti.
In questo caso deve apparire un messaggio di errore dove si comunica all’utente che mail o password
non rispettano i criteri di sicurezza.

### Esecuzione

Test eseguito automaticamente dai metodi:
```kotlin
SignUpTest.signUpWithNoData()
```
```kotlin
SignUpTest.signUpWithMalformedEmail()
```
```kotlin
SignUpTest.signUpWithWeakPassword()
```

## Test 2

### Descrizione

Verifica Registrazione con mail già in uso da un altro utente. In questo caso deve apparire un
messaggio di errore dove si comunica all’utente che la mail inserita è già in uso.

### Esecuzione

Test eseguito automaticamente dal metodo:
```kotlin
SignUpTest.signUpWithExistingUser()
```

## Test 3

### Descrizione

Verificare la Registrazione con mail e password valide. In questo caso deve apparire un messaggio di
conferma avvenuta registrazione, e si deve ricevere all'indirizzo e-mail specificato una mail per
la verifica dell'account appena creato.

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

## Test 4

### Descrizione

Verificare su Firebase Authenticator se l’utente appena registrato è stato effettivamente salvato.

### Esecuzione

1. Aprire l'applicazione;
2. Inserire nel box E-Mail la mail `test@test.it`;
3. Inserire nel box Password la password `Test123?`;
4. Inserire nel box Repeat Password la password `Test123?`;
5. Cliccare sul bottone `SIGN UP`;
6. Verificare che appaia un messaggio dove si specifica che la registrazione è terminata ma occorre ancora verificare l’e-mail;
8. Connettersi alla board di Firebase Authenticator, disponibile a questo [link](https://console.firebase.google.com/u/0/project/floraleye/authentication/users);
9. Verificare che sia presente l'utente `test@test.it`.

Una volta concluso il test, eliminare l'utente appena creato tramite Firebase Authenticator console.

## Test 5

### Descrizione

Verificare il Login con le credenziali appena registrate.

### Esecuzione

1. Aprire l'applicazione;
2. Inserire nel box E-Mail una propria e-mail. In particolare, usare una mail associata ad una
casella di posta a cui si ha accesso;
3. Inserire nel box Password la password `Test123?`;
4. Inserire nel box Repeat Password la password `Test123?`;
5. Cliccare sul bottone `SIGN UP`;
6. Verificare che appaia un messaggio dove si specifica che la registrazione è terminata ma occorre ancora verificare l’e-mail;
7. Aprire la casella di posta relativa alla e-mail usata per la registrazione e verificare che si
sia ricevuta una mail per la verifica dell'account;
8. Verificare l'account cliccando sul link ricevuto via e-mail;
9. Tornare all'applicazione;
10. Cliccare sul testo `Log In`;
11. Inserire nel box E-Mail la mail specificata nel passaggio 2;
12. Inserire nel box Password la password `Test123?`;
13. Cliccare sul bottone `LOG IN`;
14. Verificare che si venga rimandati alle funzionalità dell'applicazione;

Una volta terminato il test, è opportuno eliminare l'utente appena creato o direttamente da applicazione,
tramite funzionalità `Delete User`, oppure tramite Firebase Authenticator console.

## Test 6

### Descrizione

Verificare la Registrazione con ripetizione della password mancante o non coincidente con la password specificata.

### Esecuzione

Test eseguito automaticamente dai metodi:
```kotlin
SignUpTest.signUpWithNoRepeatedPassword()
SignUpTest.signUpWithNoMatchingPassword()
```