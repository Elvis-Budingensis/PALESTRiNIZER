
# PALESTRiNIZER

**PALESTRiNIZER** is a command-line software developed by **Daniel Hensel** and **Ingo Jache** for the **analysis of vocal-polyphonic music**, based on symbolic representations in MIDI format.

## ⚙️ Requirements

To run **PALESTRiNIZER**, ensure the following:

- A **Java SDK** is installed and configured (Java 8 or higher).
- You have valid **MIDI files** ready for analysis.

## 🚀 Quick Start

### Process a Single MIDI File

```bash
java -jar palestrinizer.jar process -g<granularity> -f<format> [-o<outputfile>] <file.midi>
```

- `-g<granularity>`: Grid resolution (e.g., `-g1` = whole notes, `-g16` = sixteenth notes)
- `-f<format>`: Output format (`-fplaintext` or `-fxml`)
- `-o<outputfile>` *(optional)*: Output file. If omitted, results are printed to the terminal.

### Batch Process Multiple MIDI Files

```bash
java -jar palestrinizer.jar batchprocess -g<granularity> -f<format> [-r] <path1> [<path2>] [...]
```

- `-r`: Enables recursive search through subdirectories

Examples:

```bash
java -jar palestrinizer.jar batchprocess -g16 -fxml -r /home/lasso/*.midi
```

### Compare Two Analysis Results

```bash
java -jar palestrinizer.jar difference [-abs] [-f<format>] [-o<outputfile>] <file1.palestrinizer> <file2.palestrinizer>
```

- `-abs`: Show absolute difference

### Aggregate Multiple Analysis Results

```bash
java -jar palestrinizer.jar aggregate [-avg] [-f<format>] [-o<outputfile>] [-r] <path1> [<path2>] [...]
```

- `-avg`: Compute average instead of total sum
- `-r`: Enable recursive folder scanning

## 🎼 Sequence Analysis

```bash
java -jar palestrinizer.jar sequences -g<granularity> -w<window> -l<limit> [-o<outputfile>] <searchpath>
```

- `-g<granularity>`: Grid resolution
- `-w<window>`: Window size for sequence length
- `-l<limit>`: Max number of results
- `-o<outputfile>` *(optional)*: Output file name

Example:

```bash
java -jar palestrinizer.jar sequences -g16 -w2 -l20 "D:\Scores\Palestrina\*.midi"
```

## ❓ Troubleshooting

- Make sure Java is installed:
```bash
java -version
```
- Ensure MIDI files are valid and accessible

## 📁 File Naming Convention for Batch Processing

To enable **batch processing**, MIDI file names must follow a specific naming convention that reflects their **mode** and **vocal scoring**. Each work must be stored in a folder corresponding to its mode:

- Mode 1 = `M1`
- Transposed Mode 1 = `M1T`
- Mode 2 = `M2`, etc.

### Example:

For a four-voice composition with the vocal order **Bass, Tenor, Alto, Soprano**, composed by Lassus and in **transposed Mode 1**, the file name should be:

```text
Lassus_accipe_M1T_BTAS.midi
```

This file should be located in a directory labeled according to the mode (e.g., `M1T/`).
