# FLO-8: Opzione Login

## Test 1

### Descrizione

Verifica Login con credenziali corrette e utente verificato.

### Esecuzione

Test eseguito automaticamente dal metodo:
```kotlin
LoginTest.loginAndLogout()
```

## Test 2

### Descrizione

Verifica Login con credenziali errate.

### Esecuzione

Test eseguito automaticamente dai metodi:
```kotlin
LoginTest.loginWithIncorrectPassword()
```
```kotlin
LoginTest.loginWithNotExistingUser()
```
```kotlin
LoginTest.loginWithNotVerifiedAccount()
```

## Test 3

### Descrizione

Verificare che non sia possibile eseguire il login con mail o password che non rispettano i
criteri di correttezza su di esse definiti.

### Esecuzione

Test eseguito automaticamente dai metodi:
```kotlin
LoginTest.loginWithWeakPassword()
```
```kotlin
LoginTest.loginWithMalformedMail()
```

## Test 4

### Descrizione

Verificare che, una volta fatto il login, questo non venga più richiesto.

### Esecuzione

1. Aprire l'applicazione;
2. Cliccare sul testo sottolineato `Log In`;
3. Inserire nel box E-Mail la mail `floraleye.test@gmail.com`;
4. Inserire nel box Password la password `Floraleye2023!`;
5. Cliccare il bottone `LOG IN`;
6. Verificare che l'onboard avvenga con successo e si sia rimandati alle funzionalità dell'applicazione;
7. Chiudere l'applicazione da task manager;
8. Riaprire l'applicazione;
9. Verificare che questa volta non venga più richiesto l'onboard, ma si venga mandati direttamente alle funzionalità dell'applicazione.