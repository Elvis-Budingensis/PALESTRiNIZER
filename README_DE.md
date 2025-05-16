
# PALESTRiNIZER

**PALESTRiNIZER** ist ein Kommandozeilenprogramm, entwickelt von **Daniel Hensel** und **Ingo Jache**, zur **Analyse vokalpolyphoner Musik** auf Basis symbolischer MIDI-Daten.

## ⚙️ Voraussetzungen

Zur Ausführung von **PALESTRiNIZER** wird benötigt:

- Ein installiertes und konfiguriertes **Java SDK** (ab Version 8)
- Gültige **MIDI-Dateien** als Eingabe

## 🚀 Schnellstart

### Eine einzelne MIDI-Datei analysieren

```bash
java -jar palestrinizer.jar process -g<granularität> -f<format> [-o<ausgabedatei>] <datei.midi>
```

- `-g<granularität>`: Zeitraster (z.B. `-g1` = ganze Noten, `-g16` = Sechzehntelnoten)
- `-f<format>`: Ausgabeformat (`-fplaintext` oder `-fxml`)
- `-o<ausgabedatei>` *(optional)*: Datei für die Ausgabe. Wenn weggelassen, erscheint die Ausgabe im Terminal.

### Mehrere MIDI-Dateien im Batch verarbeiten

```bash
java -jar palestrinizer.jar batchprocess -g<granularität> -f<format> [-r] <pfad1> [<pfad2>] [...]
```

- `-r`: Rekursive Suche in Unterverzeichnissen

Beispiel:

```bash
java -jar palestrinizer.jar batchprocess -g16 -fxml -r /home/lasso/*.midi
```

### Vergleich zweier Analyse-Ergebnisse

```bash
java -jar palestrinizer.jar difference [-abs] [-f<format>] [-o<ausgabedatei>] <datei1.palestrinizer> <datei2.palestrinizer>
```

- `-abs`: Absoluter Unterschied

### Analyse-Ergebnisse aggregieren

```bash
java -jar palestrinizer.jar aggregate [-avg] [-f<format>] [-o<ausgabedatei>] [-r] <pfad1> [<pfad2>] [...]
```

- `-avg`: Durchschnitt statt Summe
- `-r`: Rekursive Dateisuche

## 🎼 Sequenzanalyse

```bash
java -jar palestrinizer.jar sequences -g<granularität> -w<fensterbreite> -l<limit> [-o<ausgabedatei>] <suchpfad>
```

- `-g<granularität>`: Zeitraster
- `-w<fensterbreite>`: Länge der gesuchten Sequenz
- `-l<limit>`: Maximale Anzahl an Ergebnissen
- `-o<ausgabedatei>` *(optional)*: Ausgabe-Dateiname

Beispiel:

```bash
java -jar palestrinizer.jar sequences -g16 -w2 -l20 "D:\Scores\Palestrina\*.midi"
```

## ❓ Fehlerbehebung

- Prüfen ob Java installiert ist:
```bash
java -version
```
- Sicherstellen, dass die MIDI-Dateien gültig und erreichbar sind

## 📁 Dateibenennung für Batch-Verarbeitung

Für die **Batch-Verarbeitung** müssen MIDI-Dateien eine bestimmte **Namenskonvention** einhalten, die den **Modus** und die **vokale Besetzung** abbildet. Jede Komposition wird in einem Ordner gespeichert, der dem jeweiligen Modus entspricht:

- 1. Modus = `M1`
- 1. transponierter Modus = `M1T`
- 2. Modus = `M2` usw.

### Beispiel:

Für ein vierstimmiges Werk mit der vokalen Besetzung **Bass, Tenor, Alt, Sopran** von Lassus im **1. transponierten Modus**, sollte der Dateiname lauten:

```text
Lassus_accipe_M1T_BTAS.midi
```

Diese Datei gehört in ein Verzeichnis mit dem Namen des Modus, z.B. `M1T/`.
