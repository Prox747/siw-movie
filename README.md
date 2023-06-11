# siw-movie - Progetto per corso di SIW RomaTre Ingegneria Informatica 3o anno

**CASI D'USO:**
     _In questa scaletta di casi d'uso, ogni successivo tipo di utente può effettuare tutte le operazioni del tipo precedente_
     
     
     Utente occassionale: 
          1. Qualsiasi utente può visualizzare tutte le informazioni su film e artisti, nonchè le recensioni dei film
               1.1 Può visualizzare di un film tutti gli artisti coinvolti, direttore e attori e accedere alla loro pagina descrizione
               1.2 Per ogni artista, può visualizzare tutti i film diretti o in cui è apparso, e la data di nascita e morte (opzionale)
          2. Qualsiasi utente può effettuare la ricerca di film per titolo e artisti per nome e cognome
          3. (Lo metto anche se scontato) Qualsiasi utente si può registrare e accedere al servizio con l'account registrato
     ---------------------------------------------------------------------------------------------------------------------------------------------------------------
     Utente registrato:
          1. Un utente registrato può accedere alla propria pagina del profilo
          2. Un utente registrato può aggiungere o cambiare la propria immagine del profilo
          3. Un utente registrato può aggiungere o rimuovere un film dai suoi "preferiti". I film preferiti saranno visualizzabili sulla propria pagina del profilo
          4. Un utente registrato può aggiungere una recensione di un film con titolo, contenuto e voto. Tutti potranno visualizzarla
          5. Un utente registrato può cancellare la SUA recensione
     ---------------------------------------------------------------------------------------------------------------------------------------------------------------
     Utente amministratore:
          1. Un utente admin può aggiungere un film, con le informazioni necessarie
          2. Un utente admin può rimuovere un film e conseguentemente tutte le sue recensioni
          3. Un utente admin può aggiungere un artista, con le informazioni necessarie e opzionali
          4. Un utente admin può rimuovere un artista e conseguentemente i suoi collegamenti con i film diretti/recitati
          5. Un utente admin può rimuovere una, qualcuna o tutte le recensioni relative ad un film
          6. Un utente admin può aggiornare le informazioni sui film, cambiando direttore e aggiungendo o rimuovendo attori
     ---------------------------------------------------------------------------------------------------------------------------------------------------------------
     non so se definibile caso d'uso:
          *******************************************************************************************************************
          Ogni utente può decidere di modificare la grandezza della scheda del browser e il sito si comporterà di conseguenza
          IN PRATICA: IL SITO E' RESPONSIVE
          *******************************************************************************************************************
          

**NOTE FINALI E CONSIDERAZIONI:**

     Ho trovato Spring Boot uno strumento molto potente, ancora di più accoppiato con Intellij IDEA (un IDE). Ho cercato di usare il meno possibile
     funzioni/trick che non comprendevo fino in fondo. Ho cercato infatti di usare JavaScript solo in caso di estrema necessità, capendo ogni riga
     che scrivevo. Ho anche usato GitHub Copilot, che se tu in primis sai cosa vuoi fare, ti aiuta molto, altrimenti è un disastro.
     Ho cercato di validare il più possibile i dati in entrata, anche se, non avendo politiche di validazioni complesse, ho usato per lo più
     le annotazioni e ho verificato che non ci potessero essere oggetti "non passati" (null). Ho preferito, invece di creare molti doppioni di pagine
     html per admin/registrato e non, di creare una classe di utilità che mi permette di controllare se l'utente corrente è admin o registrato o 
     occasionale, per poi decidere che cosa aggiungere al modello e conseguentemente cosa mostrare nel template. Per gli stili, tranne alcune cose
     (la card del film, che alla fine ho capito come funziona), mi sono mantenuto sul semplice con un tema scuro e UI principalmente costruito con
     flexbox e qualche griglia. 
     
     NOTA: 
     questo progetto agli albori del corso era un github condiviso con un mio amico di corso, per seguire in modo coordinato il corso stesso.
     E' quindi presente come collaboratore di questa repository, ma se si controllano i commit pubblicati, ha solo aggiornato il sito durante le prime
     settimane del corso con i tutorial pubblicati dal docente (se ricordo bene fino all'aggiunta/rimozione di attori dai film)
     
Per concludere, il corso mi è piaciuto tanto, sopratutto il focus sulla pratica senza però diventare delle scimmiette che scrivono codice a caso 🐵
     
               
