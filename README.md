# MAS-WoT_productionCell

Questo progetto rappresenta una cella di produzione all'interno di un'azienda 4.0.
Ha tre modalità di esecuzione: manuale, automatica e MAS (Multi-Agent System). 
È implementato in Java e JavaFX e l'integrazione con i MAS viene eseguita tramite JaCaMo.
Inoltre, il progetto include un'interfaccia RESTful API e l'implementazione del Web of Things (WoT) tramite il concetto di Thing e Thing Description.

## Le principali caratteristiche del sistema includono:

- Multi-Agent System: Gestione di agenti autonomi che comunicano e collaborano per gestire il flusso di produzione.

- Thing Description: Le descrizioni degli oggetti, chiamate Thing Description, sono memorizzate in file .json. Gli agenti richiedono queste descrizioni inizialmente per acquisire le proprietà degli oggetti. Prima di eseguire qualsiasi azione su un oggetto, gli agenti verificano che l'azione sia presente nella Thing Description, garantendo così che tutte le interazioni siano conformi alle specifiche definite.

- Comunicazione HTTP: Implementazione di richieste HTTP per ottenere la Thing Description di un oggetto e per eseguire azioni basate sulle Thing Description ricevute. È implementata una gestione dei token di autorizzazione per garantire la sicurezza delle operazioni.

- Interfaccia JavaFX: Fornisce una visualizzazione in tempo reale della simulazione della cella di produzione, permettendo agli utenti di monitorare lo stato e le operazioni del sistema in modo intuitivo.

- Gestione dei Malfunzionamenti: Ogni componente della cella di produzione è dotato di una luce di segnalazione per indicare il suo stato: Rosso per componente spento, Verde per componente operativo. Esclusivamente per l'esecuzione MAS, occasionalmente alcune luci possono diventare gialle, indicando malfunzionamenti del componente specifico. Questo è utile per studiare la gestione dei guasti e l'interazione tra agenti in presenza di componenti con problemi.

## Requisiti per l'esecuzione:

- JaCaMo (solo per esecuzione MAS):
    Il framework per Multi-Agent Systems utilizzato nel progetto.
    Le istruzioni per l'installazione e la configurazione di JaCaMo sono disponibili al seguente URL: https://jacamo-lang.github.io/getting-started

- Java: 
    dalla versione 17 in su, disponibili al seguente URL : https://www.oracle.com/it/java/technologies/downloads/

- Gradle:
    La versione 8.7 di Gradle è inclusa nel repository GitHub del progetto.
    Non è necessaria un'installazione separata di Gradle, poiché è possibile utilizzare il Gradle Wrapper incluso (gradlew per Unix/Linux/macOS, gradlew.bat per Windows).

### Esecuzione:

Per avviare il sistema, l'esecuzione parte dalla classe Launcher, che avvia l'interfaccia utente. A seconda della modalità selezionata (automatica, manuale o MAS), il processo viene gestito                automaticamente.

Modalità Automatica o Manuale
L'interfaccia utente consente di scegliere tra la modalità di funzionamento automatica o manuale. Il sistema è configurato per gestire queste modalità senza ulteriori interventi manuali.

Per eseguire i MAS senza l'interfaccia, è possibile utilizzare i seguenti comandi nella Shell dalla directory  
" /productioncellSimulator/src/main/resources/mas/src " :

Windows: ````` cmd.exe /c gradlew run `````

Unix-like: ````` ./gradlew run `````

Modalità MAS (Multi-Agent System)
Quando si seleziona la modalità MAS, il sistema rileva il sistema operativo in uso e utilizza il comando Gradle appropriato per avviare i MAS. Inoltre, vengono inizializzati i server necessari per la comunicazione tra gli agenti e per la gestione delle Thing Description.
