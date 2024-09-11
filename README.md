# 🛠️ **SIW Movie**  
### Progetto per il corso di Sistemi Informativi su Web RomaTre Ingegneria Informatica - 3° anno

## 📋 **Casi d'Uso**

_In questa scaletta di casi d'uso, ogni successivo tipo di utente può effettuare tutte le operazioni del tipo precedente._

### 👤 Utente Occasionale
- Qualsiasi utente può visualizzare tutte le informazioni su film e artisti, nonché le recensioni dei film.
   - Può visualizzare di un film tutti gli artisti coinvolti (regista e attori) e accedere alla loro pagina descrittiva.
   - Per ogni artista, può visualizzare tutti i film diretti o in cui è apparso, insieme alla data di nascita e morte (opzionale).
- Qualsiasi utente può cercare film per titolo e artisti per nome e cognome.
- Qualsiasi utente può registrarsi e accedere al servizio con un account registrato.

### 📝 Utente Registrato
- Un utente registrato può accedere alla propria pagina del profilo.
- Un utente registrato può aggiungere o cambiare l'immagine del profilo.
- Un utente registrato può aggiungere o rimuovere un film dai "preferiti", che saranno visibili nel profilo.
- Un utente registrato può aggiungere una recensione di un film, specificando titolo, contenuto e voto, visibile a tutti.
- Un utente registrato può cancellare la propria recensione.

### 🛡️ Utente Amministratore
- Un admin può aggiungere un film, fornendo tutte le informazioni necessarie.
- Un admin può rimuovere un film e le relative recensioni.
- Un admin può aggiungere un artista con le informazioni necessarie e opzionali.
- Un admin può rimuovere un artista, cancellando anche i suoi collegamenti con i film.
- Un admin può rimuovere una, alcune o tutte le recensioni relative a un film.
- Un admin può aggiornare le informazioni sui film, cambiando il regista o aggiungendo/rimuovendo attori.

### 🌐 Responsività del Sito
Tutti gli utenti possono modificare la dimensione della scheda del browser, e il sito si adatterà automaticamente:  
**Il sito è completamente responsive.**

---

## 💭 **Note Finali e Considerazioni**

Ho trovato **Spring Boot** uno strumento molto potente, soprattutto accoppiato con **Intellij IDEA**. Ho cercato di usare il meno possibile funzioni o "trucchi" che non comprendevo a fondo, limitando l'uso di **JavaScript** solo dove strettamente necessario.  
Ho anche utilizzato **GitHub Copilot**, che è utile solo se si sa esattamente cosa si vuole fare, altrimenti può risultare poco efficace.  

Per la validazione dei dati, ho adottato un approccio semplice con annotazioni e controlli per evitare oggetti nulli. Invece di duplicare pagine HTML per admin, utenti registrati e occasionali, ho creato una classe di utilità per gestire i permessi e decidere cosa visualizzare nel template.  
Per quanto riguarda lo stile, ho mantenuto un approccio semplice, con un tema scuro e una **UI** costruita principalmente con **Flexbox** e alcune griglie.
