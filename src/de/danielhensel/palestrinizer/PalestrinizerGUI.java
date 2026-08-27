package de.danielhensel.palestrinizer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Grafische Oberflaeche fuer PALESTRiNIZER.
 *
 * Bildet alle neun Kommandozeilenbefehle (process, batchprocess, difference,
 * aggregate, soundchart, linechart, sequences, compositesoundchart,
 * negativesoundchart) als Tabs ab und ruft dafuer dieselbe Logik auf, die
 * auch die Kommandozeile benutzt (Palestrinizer.execute...-Methoden).
 *
 * Aufgerufen wird sie automatisch, wenn "java -jar palestrinizer.jar" ohne
 * Argumente gestartet wird. Mit Argumenten verhaelt sich das Programm
 * weiterhin wie gewohnt als Kommandozeilenwerkzeug.
 */
public class PalestrinizerGUI
{
	private static File lastDirectory = new File(System.getProperty("user.home"));

	private JFrame frame;
	private JTextArea logArea;

	public static void launch()
	{
		SwingUtilities.invokeLater(() -> new PalestrinizerGUI().createAndShow());
	}

	private void createAndShow()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) { /* ignore, fall back to default L&F */ }

		frame = new JFrame("PALESTRiNIZER");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("Process", buildProcessTab());
		tabs.addTab("Batchprocess", buildBatchProcessTab());
		tabs.addTab("Difference", buildDifferenceTab());
		tabs.addTab("Aggregate", buildAggregateTab());
		tabs.addTab("Soundchart", buildBarChartTab("soundchart", "Erzeugt ein Balkendiagramm (PNG) aus mehreren MIDI-Dateien."));
		tabs.addTab("Linechart", buildLineChartTab());
		tabs.addTab("Sequences", buildSequencesTab());
		tabs.addTab("Composite Soundchart", buildCompositeTab());
		tabs.addTab("Negative Soundchart", buildBarChartTab("negativesoundchart", "Wie Soundchart, aber mit invertierter Darstellung (PNG)."));

		logArea = new JTextArea(10, 80);
		logArea.setEditable(false);
		logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		JScrollPane logScroll = new JScrollPane(logArea);
		logScroll.setBorder(BorderFactory.createTitledBorder("Protokoll"));

		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabs, logScroll);
		split.setResizeWeight(0.72);

		frame.add(split, BorderLayout.CENTER);
		frame.setSize(880, 720);
		frame.setLocationRelativeTo(null);

		redirectSystemOut();

		frame.setVisible(true);
		appendLog("PALESTRiNIZER GUI bereit.\n");
	}

	// ------------------------------------------------------------------
	// Ausgabe-Umleitung: System.out/System.err landen im Protokollfenster
	// ------------------------------------------------------------------
	private void redirectSystemOut()
	{
		OutputStream out = new LineBufferedOutputStream(this::appendLog);
		PrintStream ps = new PrintStream(out, true);
		System.setOut(ps);
		System.setErr(ps);
	}

	private void appendLog(String line)
	{
		SwingUtilities.invokeLater(() -> {
			logArea.append(line);
			logArea.setCaretPosition(logArea.getDocument().getLength());
		});
	}

	/** Sammelt Bytes zeilenweise und reicht fertige Zeilen an einen Consumer weiter. */
	private static class LineBufferedOutputStream extends OutputStream
	{
		private final StringBuilder buffer = new StringBuilder();
		private final java.util.function.Consumer<String> sink;

		LineBufferedOutputStream(java.util.function.Consumer<String> sink) { this.sink = sink; }

		@Override
		public void write(int b)
		{
			char c = (char) b;
			buffer.append(c);
			if (c == '\n')
			{
				sink.accept(buffer.toString());
				buffer.setLength(0);
			}
		}
	}

	// ------------------------------------------------------------------
	// Ausfuehrung im Hintergrund, damit die Oberflaeche nicht einfriert
	// ------------------------------------------------------------------
	private interface Command { void run() throws Exception; }

	private void runInBackground(JButton triggerButton, Command command)
	{
		triggerButton.setEnabled(false);
		appendLog("\n--- Start (" + new java.util.Date() + ") ---\n");

		new SwingWorker<Void, Void>()
		{
			private Exception error;

			@Override
			protected Void doInBackground()
			{
				try
				{
					command.run();
				} catch (Exception e)
				{
					error = e;
				}
				return null;
			}

			@Override
			protected void done()
			{
				triggerButton.setEnabled(true);
				if (error != null)
				{
					appendLog("FEHLER: " + error + "\n");
					JOptionPane.showMessageDialog(frame,
							error.getMessage() != null ? error.getMessage() : error.toString(),
							"Fehler bei der Ausfuehrung", JOptionPane.ERROR_MESSAGE);
				} else
				{
					appendLog("--- Fertig ---\n");
				}
			}
		}.execute();
	}

	// ------------------------------------------------------------------
	// Hilfsfunktionen fuer Formulare
	// ------------------------------------------------------------------
	private JPanel formPanel()
	{
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(new EmptyBorder(12, 12, 12, 12));
		return panel;
	}

	private GridBagConstraints gbc(int x, int y, int width)
	{
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x; c.gridy = y; c.gridwidth = width;
		c.insets = new Insets(4, 4, 4, 4);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.weightx = (x == 1) ? 1.0 : 0.0;
		return c;
	}

	private JLabel headline(String text)
	{
		JLabel l = new JLabel(text);
		l.setFont(l.getFont().deriveFont(Font.BOLD, 13f));
		return l;
	}

	private JComboBox<Integer> granularityCombo()
	{
		JComboBox<Integer> combo = new JComboBox<>(new Integer[]{1, 2, 4, 8, 16, 32});
		combo.setSelectedItem(4);
		return combo;
	}

	private JComboBox<String> formatCombo(String defaultFormat)
	{
		JComboBox<String> combo = new JComboBox<>(new String[]{"plaintext", "xml"});
		combo.setSelectedItem(defaultFormat);
		return combo;
	}

	private File chooseFile(boolean forSaving, String... extensions)
	{
		JFileChooser chooser = new JFileChooser(lastDirectory);
		if (extensions.length > 0)
		{
			chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
					String.join(", ", extensions) + "-Dateien", extensions));
		}
		int result = forSaving ? chooser.showSaveDialog(frame) : chooser.showOpenDialog(frame);
		if (result != JFileChooser.APPROVE_OPTION) return null;
		lastDirectory = chooser.getCurrentDirectory();
		return chooser.getSelectedFile();
	}

	private File chooseDirectory()
	{
		JFileChooser chooser = new JFileChooser(lastDirectory);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = chooser.showOpenDialog(frame);
		if (result != JFileChooser.APPROVE_OPTION) return null;
		lastDirectory = chooser.getCurrentDirectory();
		return chooser.getSelectedFile();
	}

	/** Textfeld + "Durchsuchen..."-Knopf fuer eine einzelne Datei. */
	private JPanel fileField(JTextField target, boolean forSaving, String... extensions)
	{
		JPanel p = new JPanel(new BorderLayout(4, 0));
		p.add(target, BorderLayout.CENTER);
		JButton browse = new JButton("Durchsuchen...");
		browse.addActionListener(e -> {
			File f = chooseFile(forSaving, extensions);
			if (f != null) target.setText(f.getAbsolutePath());
		});
		p.add(browse, BorderLayout.EAST);
		return p;
	}

	// ------------------------------------------------------------------
	// Datei-/Muster-Liste, wiederverwendet fuer alle Batch-artigen Befehle
	// ------------------------------------------------------------------
	private static class PatternListPanel
	{
		final JPanel panel = new JPanel(new BorderLayout(4, 4));
		final DefaultListModel<String> model = new DefaultListModel<>();
		final JList<String> list = new JList<>(model);
		final JCheckBox recursive = new JCheckBox("Unterordner einbeziehen (-r)");

		PatternListPanel(JFrame owner)
		{
			list.setVisibleRowCount(5);
			panel.add(new JScrollPane(list), BorderLayout.CENTER);

			JPanel buttons = new JPanel();
			JButton addFiles = new JButton("Dateien hinzufuegen...");
			addFiles.addActionListener(e -> {
				JFileChooser chooser = new JFileChooser(lastDirectory);
				chooser.setMultiSelectionEnabled(true);
				chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
						"MIDI-Dateien", "mid", "midi"));
				if (chooser.showOpenDialog(owner) == JFileChooser.APPROVE_OPTION)
				{
					lastDirectory = chooser.getCurrentDirectory();
					for (File f : chooser.getSelectedFiles()) model.addElement(f.getAbsolutePath());
				}
			});

			JButton addFolder = new JButton("Ordner + Muster hinzufuegen...");
			addFolder.addActionListener(e -> {
				JFileChooser chooser = new JFileChooser(lastDirectory);
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (chooser.showOpenDialog(owner) == JFileChooser.APPROVE_OPTION)
				{
					lastDirectory = chooser.getCurrentDirectory();
					String pattern = JOptionPane.showInputDialog(owner,
							"Dateimuster (z.B. *.midi):", "*.midi");
					if (pattern != null && !pattern.isBlank())
					{
						model.addElement(chooser.getSelectedFile().getAbsolutePath() + File.separator + pattern.trim());
					}
				}
			});

			JButton remove = new JButton("Entfernen");
			remove.addActionListener(e -> {
				List<String> selected = list.getSelectedValuesList();
				for (String s : selected) model.removeElement(s);
			});

			buttons.add(addFiles);
			buttons.add(addFolder);
			buttons.add(remove);
			panel.add(buttons, BorderLayout.NORTH);

			JPanel south = new JPanel(new BorderLayout());
			south.add(recursive, BorderLayout.WEST);
			panel.add(south, BorderLayout.SOUTH);
		}

		String joined()
		{
			List<String> items = new ArrayList<>();
			for (int i = 0; i < model.size(); i++) items.add(model.get(i));
			return String.join(File.pathSeparator, items);
		}

		boolean isEmpty() { return model.isEmpty(); }
	}

	// ------------------------------------------------------------------
	// Tab: process
	// ------------------------------------------------------------------
	private JPanel buildProcessTab()
	{
		JPanel panel = formPanel();
		int row = 0;

		panel.add(headline("Eine einzelne MIDI-Datei analysieren (process)"), gbc(0, row, 2));
		row++;

		JTextField inputField = new JTextField();
		panel.add(new JLabel("MIDI-Datei:"), gbc(0, row, 1));
		panel.add(fileField(inputField, false, "mid", "midi"), gbc(1, row, 1));
		row++;

		JComboBox<Integer> granularity = granularityCombo();
		panel.add(new JLabel("Granularitaet (-g):"), gbc(0, row, 1));
		panel.add(granularity, gbc(1, row, 1));
		row++;

		JComboBox<String> format = formatCombo("plaintext");
		panel.add(new JLabel("Format (-f):"), gbc(0, row, 1));
		panel.add(format, gbc(1, row, 1));
		row++;

		JCheckBox saveToFile = new JCheckBox("In Datei speichern statt im Protokoll anzeigen");
		panel.add(saveToFile, gbc(0, row, 2));
		row++;

		JTextField outputField = new JTextField();
		outputField.setEnabled(false);
		JPanel outputChooser = fileField(outputField, true);
		outputChooser.setEnabled(false);
		saveToFile.addActionListener(e -> {
			outputField.setEnabled(saveToFile.isSelected());
			for (Component c : outputChooser.getComponents()) c.setEnabled(saveToFile.isSelected());
		});
		panel.add(new JLabel("Ausgabedatei (-o):"), gbc(0, row, 1));
		panel.add(outputChooser, gbc(1, row, 1));
		row++;

		JButton run = new JButton("Analysieren");
		panel.add(run, gbc(1, row, 1));
		row++;

		panel.add(Box.createVerticalGlue(), gbc(0, row, 2));

		run.addActionListener(e -> {
			if (inputField.getText().isBlank())
			{
				JOptionPane.showMessageDialog(frame, "Bitte eine MIDI-Datei auswaehlen.");
				return;
			}
			List<String> args = new ArrayList<>();
			args.add("process");
			args.add("-g" + granularity.getSelectedItem());
			args.add("-f" + format.getSelectedItem());
			if (saveToFile.isSelected() && !outputField.getText().isBlank())
			{
				args.add("-o" + outputField.getText().trim());
			}
			args.add(inputField.getText().trim());
			runInBackground(run, () -> Palestrinizer.executeProcess(args.toArray(new String[0])));
		});

		return panel;
	}

	// ------------------------------------------------------------------
	// Tab: batchprocess
	// ------------------------------------------------------------------
	private JPanel buildBatchProcessTab()
	{
		JPanel panel = formPanel();
		int row = 0;

		panel.add(headline("Mehrere MIDI-Dateien im Batch verarbeiten (batchprocess)"), gbc(0, row, 2));
		row++;

		panel.add(new JLabel("Ergebnis wird pro Datei als <datei>.palestrinizer bzw. <datei>.txt abgelegt."), gbc(0, row, 2));
		row++;

		PatternListPanel patterns = new PatternListPanel(frame);
		panel.add(patterns.panel, gbc(0, row, 2));
		row++;

		JComboBox<Integer> granularity = granularityCombo();
		panel.add(new JLabel("Granularitaet (-g):"), gbc(0, row, 1));
		panel.add(granularity, gbc(1, row, 1));
		row++;

		JComboBox<String> format = formatCombo("xml");
		panel.add(new JLabel("Format (-f):"), gbc(0, row, 1));
		panel.add(format, gbc(1, row, 1));
		row++;

		JButton run = new JButton("Batch starten");
		panel.add(run, gbc(1, row, 1));
		row++;
		panel.add(Box.createVerticalGlue(), gbc(0, row, 2));

		run.addActionListener(e -> {
			if (patterns.isEmpty())
			{
				JOptionPane.showMessageDialog(frame, "Bitte mindestens eine Datei oder ein Ordnermuster hinzufuegen.");
				return;
			}
			List<String> args = new ArrayList<>();
			args.add("batchprocess");
			args.add("-g" + granularity.getSelectedItem());
			args.add("-f" + format.getSelectedItem());
			if (patterns.recursive.isSelected()) args.add("-r");
			args.add(patterns.joined());
			runInBackground(run, () -> Palestrinizer.executeBatchProcess(args.toArray(new String[0])));
		});

		return panel;
	}

	// ------------------------------------------------------------------
	// Tab: difference
	// ------------------------------------------------------------------
	private JPanel buildDifferenceTab()
	{
		JPanel panel = formPanel();
		int row = 0;

		panel.add(headline("Zwei Analyse-Ergebnisse vergleichen (difference)"), gbc(0, row, 2));
		row++;

		JTextField fileA = new JTextField();
		panel.add(new JLabel("Datei A (.palestrinizer):"), gbc(0, row, 1));
		panel.add(fileField(fileA, false, "palestrinizer"), gbc(1, row, 1));
		row++;

		JTextField fileB = new JTextField();
		panel.add(new JLabel("Datei B (.palestrinizer):"), gbc(0, row, 1));
		panel.add(fileField(fileB, false, "palestrinizer"), gbc(1, row, 1));
		row++;

		JCheckBox absolute = new JCheckBox("Absoluter Unterschied (-abs)");
		panel.add(absolute, gbc(0, row, 2));
		row++;

		JComboBox<String> format = formatCombo("plaintext");
		panel.add(new JLabel("Format (-f):"), gbc(0, row, 1));
		panel.add(format, gbc(1, row, 1));
		row++;

		JTextField outputField = new JTextField();
		panel.add(new JLabel("Ausgabedatei (-o, optional):"), gbc(0, row, 1));
		panel.add(fileField(outputField, true), gbc(1, row, 1));
		row++;

		JButton run = new JButton("Vergleichen");
		panel.add(run, gbc(1, row, 1));
		row++;
		panel.add(Box.createVerticalGlue(), gbc(0, row, 2));

		run.addActionListener(e -> {
			if (fileA.getText().isBlank() || fileB.getText().isBlank())
			{
				JOptionPane.showMessageDialog(frame, "Bitte beide Dateien auswaehlen.");
				return;
			}
			List<String> args = new ArrayList<>();
			args.add("difference");
			if (absolute.isSelected()) args.add("-abs");
			args.add("-f" + format.getSelectedItem());
			if (!outputField.getText().isBlank()) args.add("-o" + outputField.getText().trim());
			args.add(fileA.getText().trim());
			args.add(fileB.getText().trim());
			runInBackground(run, () -> Palestrinizer.executeDifference(args.toArray(new String[0])));
		});

		return panel;
	}

	// ------------------------------------------------------------------
	// Tab: aggregate
	// ------------------------------------------------------------------
	private JPanel buildAggregateTab()
	{
		JPanel panel = formPanel();
		int row = 0;

		panel.add(headline("Analyse-Ergebnisse aggregieren (aggregate)"), gbc(0, row, 2));
		row++;

		panel.add(new JLabel("Erwartet .palestrinizer-Dateien (Ergebnis von batchprocess -fxml)."), gbc(0, row, 2));
		row++;

		PatternListPanel patterns = new PatternListPanel(frame);
		panel.add(patterns.panel, gbc(0, row, 2));
		row++;

		JCheckBox average = new JCheckBox("Durchschnitt statt Summe (-avg)");
		panel.add(average, gbc(0, row, 2));
		row++;

		JComboBox<String> format = formatCombo("plaintext");
		panel.add(new JLabel("Format (-f):"), gbc(0, row, 1));
		panel.add(format, gbc(1, row, 1));
		row++;

		JTextField outputField = new JTextField();
		panel.add(new JLabel("Ausgabedatei (-o, optional):"), gbc(0, row, 1));
		panel.add(fileField(outputField, true), gbc(1, row, 1));
		row++;

		JButton run = new JButton("Aggregieren");
		panel.add(run, gbc(1, row, 1));
		row++;
		panel.add(Box.createVerticalGlue(), gbc(0, row, 2));

		run.addActionListener(e -> {
			if (patterns.isEmpty())
			{
				JOptionPane.showMessageDialog(frame, "Bitte mindestens eine Datei oder ein Ordnermuster hinzufuegen.");
				return;
			}
			List<String> args = new ArrayList<>();
			args.add("aggregate");
			if (patterns.recursive.isSelected()) args.add("-r");
			if (average.isSelected()) args.add("-avg");
			args.add("-f" + format.getSelectedItem());
			if (!outputField.getText().isBlank()) args.add("-o" + outputField.getText().trim());
			args.add(patterns.joined());
			runInBackground(run, () -> Palestrinizer.executeAggregate(args.toArray(new String[0])));
		});

		return panel;
	}

	// ------------------------------------------------------------------
	// Tab: soundchart / negativesoundchart (identischer Aufbau)
	// ------------------------------------------------------------------
	private JPanel buildBarChartTab(String subcommand, String description)
	{
		JPanel panel = formPanel();
		int row = 0;

		panel.add(headline(description), gbc(0, row, 2));
		row++;

		PatternListPanel patterns = new PatternListPanel(frame);
		panel.add(patterns.panel, gbc(0, row, 2));
		row++;

		JComboBox<Integer> granularity = granularityCombo();
		panel.add(new JLabel("Granularitaet (-g):"), gbc(0, row, 1));
		panel.add(granularity, gbc(1, row, 1));
		row++;

		JComboBox<String> mode = new JComboBox<>(new String[]{
				"pattern", "dissonancegrade", "crossings", "density", "sentencedensity"});
		panel.add(new JLabel("Modus (-m):"), gbc(0, row, 1));
		panel.add(mode, gbc(1, row, 1));
		row++;

		JTextField pattern = new JTextField();
		panel.add(new JLabel("Klangmuster (-p, nur bei Modus \"pattern\"):"), gbc(0, row, 1));
		panel.add(pattern, gbc(1, row, 1));
		row++;
		mode.addActionListener(e -> pattern.setEnabled("pattern".equals(mode.getSelectedItem())));

		JSpinner width = new JSpinner(new SpinnerNumberModel(800, 1, 20000, 10));
		panel.add(new JLabel("Breite in Pixel (-w):"), gbc(0, row, 1));
		panel.add(width, gbc(1, row, 1));
		row++;

		JSpinner height = new JSpinner(new SpinnerNumberModel(400, 1, 20000, 10));
		panel.add(new JLabel("Hoehe in Pixel (-h):"), gbc(0, row, 1));
		panel.add(height, gbc(1, row, 1));
		row++;

		JSpinner length = new JSpinner(new SpinnerNumberModel(50, 1, 5000, 1));
		panel.add(new JLabel("Anzahl Balken/Dateien (-l):"), gbc(0, row, 1));
		panel.add(length, gbc(1, row, 1));
		row++;

		JTextField outputField = new JTextField();
		panel.add(new JLabel("Ausgabedatei (.png):"), gbc(0, row, 1));
		panel.add(fileField(outputField, true, "png"), gbc(1, row, 1));
		row++;

		JButton run = new JButton("Diagramm erzeugen");
		panel.add(run, gbc(1, row, 1));
		row++;
		panel.add(Box.createVerticalGlue(), gbc(0, row, 2));

		run.addActionListener(e -> {
			if (patterns.isEmpty())
			{
				JOptionPane.showMessageDialog(frame, "Bitte mindestens eine Datei oder ein Ordnermuster hinzufuegen.");
				return;
			}
			if (outputField.getText().isBlank())
			{
				JOptionPane.showMessageDialog(frame, "Bitte eine Ausgabedatei angeben.");
				return;
			}
			if ("pattern".equals(mode.getSelectedItem()) && pattern.getText().isBlank())
			{
				JOptionPane.showMessageDialog(frame,
						"Beim Modus \"pattern\" muss ein Klangmuster angegeben werden (z.B. \"major\" oder \"minor\"),\n"
						+ "oder waehle stattdessen einen der vorgefertigten Modi (dissonancegrade, crossings, density, sentencedensity).",
						"Klangmuster fehlt", JOptionPane.WARNING_MESSAGE);
				return;
			}
			List<String> args = new ArrayList<>();
			args.add(subcommand);
			args.add("-g" + granularity.getSelectedItem());
			args.add("-m" + mode.getSelectedItem());
			if ("pattern".equals(mode.getSelectedItem()))
			{
				args.add("-p" + pattern.getText().trim());
			}
			args.add("-w" + width.getValue());
			args.add("-h" + height.getValue());
			args.add("-l" + length.getValue());
			args.add("-o" + outputField.getText().trim());
			if (patterns.recursive.isSelected()) args.add("-r");
			args.add(patterns.joined());

			String[] argsArray = args.toArray(new String[0]);
			runInBackground(run, () -> {
				if (subcommand.equals("soundchart")) Palestrinizer.executeBarchart(argsArray);
				else Palestrinizer.executeNegativeBarChart(argsArray);
			});
		});

		return panel;
	}

	// ------------------------------------------------------------------
	// Tab: linechart
	// ------------------------------------------------------------------
	private JPanel buildLineChartTab()
	{
		JPanel panel = formPanel();
		int row = 0;

		panel.add(headline("Erzeugt ein Liniendiagramm (PNG) aus mehreren MIDI-Dateien."), gbc(0, row, 2));
		row++;

		PatternListPanel patterns = new PatternListPanel(frame);
		panel.add(patterns.panel, gbc(0, row, 2));
		row++;

		JComboBox<Integer> granularity = granularityCombo();
		panel.add(new JLabel("Granularitaet (-g):"), gbc(0, row, 1));
		panel.add(granularity, gbc(1, row, 1));
		row++;

		JSpinner width = new JSpinner(new SpinnerNumberModel(800, 1, 20000, 10));
		panel.add(new JLabel("Breite in Pixel (-w):"), gbc(0, row, 1));
		panel.add(width, gbc(1, row, 1));
		row++;

		JSpinner height = new JSpinner(new SpinnerNumberModel(400, 1, 20000, 10));
		panel.add(new JLabel("Hoehe in Pixel (-h):"), gbc(0, row, 1));
		panel.add(height, gbc(1, row, 1));
		row++;

		JSpinner length = new JSpinner(new SpinnerNumberModel(50, 1, 5000, 1));
		panel.add(new JLabel("Anzahl Samples (-l):"), gbc(0, row, 1));
		panel.add(length, gbc(1, row, 1));
		row++;

		JTextField pattern = new JTextField();
		panel.add(new JLabel("Klangmuster (-p, Pflichtfeld):"), gbc(0, row, 1));
		panel.add(pattern, gbc(1, row, 1));
		row++;

		JTextField outputField = new JTextField();
		panel.add(new JLabel("Ausgabedatei (.png):"), gbc(0, row, 1));
		panel.add(fileField(outputField, true, "png"), gbc(1, row, 1));
		row++;

		JButton run = new JButton("Diagramm erzeugen");
		panel.add(run, gbc(1, row, 1));
		row++;
		panel.add(Box.createVerticalGlue(), gbc(0, row, 2));

		run.addActionListener(e -> {
			if (patterns.isEmpty())
			{
				JOptionPane.showMessageDialog(frame, "Bitte mindestens eine Datei oder ein Ordnermuster hinzufuegen.");
				return;
			}
			if (outputField.getText().isBlank())
			{
				JOptionPane.showMessageDialog(frame, "Bitte eine Ausgabedatei angeben.");
				return;
			}
			if (pattern.getText().isBlank())
			{
				JOptionPane.showMessageDialog(frame, "Bitte ein Klangmuster angeben (Pflichtfeld, z.B. \"major\").");
				return;
			}
			List<String> args = new ArrayList<>();
			args.add("linechart");
			args.add("-g" + granularity.getSelectedItem());
			args.add("-w" + width.getValue());
			args.add("-h" + height.getValue());
			args.add("-l" + length.getValue());
			args.add("-p" + pattern.getText().trim());
			args.add("-o" + outputField.getText().trim());
			if (patterns.recursive.isSelected()) args.add("-r");
			args.add(patterns.joined());
			runInBackground(run, () -> Palestrinizer.executeLinechart(args.toArray(new String[0])));
		});

		return panel;
	}

	// ------------------------------------------------------------------
	// Tab: sequences
	// ------------------------------------------------------------------
	private JPanel buildSequencesTab()
	{
		JPanel panel = formPanel();
		int row = 0;

		panel.add(headline("Wiederkehrende Tonfolgen suchen (sequences)"), gbc(0, row, 2));
		row++;

		PatternListPanel patterns = new PatternListPanel(frame);
		panel.add(patterns.panel, gbc(0, row, 2));
		row++;

		JComboBox<Integer> granularity = granularityCombo();
		panel.add(new JLabel("Granularitaet (-g):"), gbc(0, row, 1));
		panel.add(granularity, gbc(1, row, 1));
		row++;

		JComboBox<String> mode = new JComboBox<>(new String[]{"Basstoene", "Grundtonfortschreitung"});
		panel.add(new JLabel("Modus (-m):"), gbc(0, row, 1));
		panel.add(mode, gbc(1, row, 1));
		row++;

		JSpinner window = new JSpinner(new SpinnerNumberModel(2, 1, 100, 1));
		panel.add(new JLabel("Fensterbreite (-w):"), gbc(0, row, 1));
		panel.add(window, gbc(1, row, 1));
		row++;

		JSpinner limit = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 1));
		panel.add(new JLabel("Max. Ergebnisse (-l):"), gbc(0, row, 1));
		panel.add(limit, gbc(1, row, 1));
		row++;

		JTextField outputField = new JTextField();
		panel.add(new JLabel("Ausgabedatei (-o, optional):"), gbc(0, row, 1));
		panel.add(fileField(outputField, true), gbc(1, row, 1));
		row++;

		JButton run = new JButton("Suchen");
		panel.add(run, gbc(1, row, 1));
		row++;
		panel.add(Box.createVerticalGlue(), gbc(0, row, 2));

		run.addActionListener(e -> {
			if (patterns.isEmpty())
			{
				JOptionPane.showMessageDialog(frame, "Bitte mindestens eine Datei oder ein Ordnermuster hinzufuegen.");
				return;
			}
			List<String> args = new ArrayList<>();
			args.add("sequences");
			args.add("-g" + granularity.getSelectedItem());
			if ("Grundtonfortschreitung".equals(mode.getSelectedItem())) args.add("-mkeytoneprogression");
			args.add("-w" + window.getValue());
			args.add("-l" + limit.getValue());
			if (!outputField.getText().isBlank()) args.add("-o" + outputField.getText().trim());
			if (patterns.recursive.isSelected()) args.add("-r");
			args.add(patterns.joined());
			runInBackground(run, () -> Palestrinizer.executeSequences(args.toArray(new String[0])));
		});

		return panel;
	}

	// ------------------------------------------------------------------
	// Tab: compositesoundchart
	// ------------------------------------------------------------------
	private JPanel buildCompositeTab()
	{
		JPanel panel = formPanel();
		int row = 0;

		panel.add(headline("Zwei Balkendiagramme (PNG) uebereinanderlegen (compositesoundchart)"), gbc(0, row, 2));
		row++;

		JTextField imageA = new JTextField();
		panel.add(new JLabel("Bild A (.png):"), gbc(0, row, 1));
		panel.add(fileField(imageA, false, "png"), gbc(1, row, 1));
		row++;

		JTextField imageB = new JTextField();
		panel.add(new JLabel("Bild B (.png):"), gbc(0, row, 1));
		panel.add(fileField(imageB, false, "png"), gbc(1, row, 1));
		row++;

		JTextField outputField = new JTextField();
		panel.add(new JLabel("Ausgabedatei (.png):"), gbc(0, row, 1));
		panel.add(fileField(outputField, true, "png"), gbc(1, row, 1));
		row++;

		JButton run = new JButton("Kombinieren");
		panel.add(run, gbc(1, row, 1));
		row++;
		panel.add(Box.createVerticalGlue(), gbc(0, row, 2));

		run.addActionListener(e -> {
			if (imageA.getText().isBlank() || imageB.getText().isBlank() || outputField.getText().isBlank())
			{
				JOptionPane.showMessageDialog(frame, "Bitte beide Bilder und eine Ausgabedatei angeben.");
				return;
			}
			String[] args = new String[]{
					"compositesoundchart",
					imageA.getText().trim(),
					imageB.getText().trim(),
					outputField.getText().trim()
			};
			runInBackground(run, () -> Palestrinizer.executeCompositeBarchart(args));
		});

		return panel;
	}
}
