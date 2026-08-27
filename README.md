This is a command-line program that requires Java to run. The software searches for feature correlations and can either output ranked lists with statistics or generate visualizations of predefined features such as major chords, tone density, and texture density.
It is recommended to create shell scripts in order to analyze multiple works recursively.

The software was developed by Daniel Hensel and Ingo Jache for the Habilitation Project in Systematic Musicology at Martin Luther-University Halle-Wittenberg, Germany. Official Website:
http://palestrinizer.danielhensel.de

Results of the analysis have been published in:

Daniel Hensel, Beiträge zur Musikinformatik, Modus, Klang- und Zeitgestaltung in Lassus- und Palestrina-Motetten, Springer 2017. https://link.springer.com/book/10.1007/978-3-658-18273-1 <br>
and <br>
Daniel Hensel, Modale Klangstrukturen in Madrigalen Giovanni Pierluigi da Palestrinas und ihre Analyse durch die Software PALESTRiNIZER, AfMw 2022,(https://biblioscout.net/article/10.25162/afmw-2022-0008)

## GUI

Since this version, running the jar without any arguments opens a graphical
user interface instead of printing the usage instructions:

```bash
java -jar palestrinizer.jar
```

The GUI provides one tab for each of the nine subcommands — process,
batchprocess, difference, aggregate, soundchart, linechart, sequences,
compositesoundchart, and negativesoundchart — with file/folder pickers for
inputs and outputs, and a log panel showing the program's output.

Calling the jar with arguments still works exactly as before and runs the
command-line interface, e.g.:

```bash
java -jar palestrinizer.jar process -g4 -fplaintext myfile.midi
```

## GUI

Since this version, running the jar without any arguments opens a graphical
user interface instead of printing the usage instructions:

```bash
java -jar palestrinizer.jar
```

The GUI provides one tab for each of the nine subcommands — process,
batchprocess, difference, aggregate, soundchart, linechart, sequences,
compositesoundchart, and negativesoundchart — with file/folder pickers for
inputs and outputs, and a log panel showing the program's output.

Calling the jar with arguments still works exactly as before and runs the
command-line interface, e.g.:

```bash
java -jar palestrinizer.jar process -g4 -fplaintext myfile.midi
```
