// Copyright (C) 2014 by Klaus Jung
// All rights reserved.
// Date: 2014-10-02
package de.htw.fb4.imi.master.ws15_16.jungk;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.Factory;
import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.ff.AbstractFloodFilling;
import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.ff.BreadthFirst;
import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.ff.AbstractFloodFilling.Mode;
import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.ff.DepthFirst;
import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.treshold.ThresholdFindingAlgorithm;
import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.util.ImageUtil;
import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.util.LabeledPoint;

public class FloodFillingGui extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;
	private static final int border = 10;
	private static final int maxWidth = 400;
	private static final int maxHeight = 400;
	private static final File openPath = new File(".");
	private static String title = "Flood Filling ";
	private static final String author = "Markus Föllmer & Sascha Feldmann";
	private static final String initalOpen = "tools.png";
	private static final int TEXTAREA_COLS = 30;
	private static final int TEXTAREA_ROWS = 5;

	private static JFrame frame;

	private ImageView srcView; // source image view
	private ImageView dstView; // binarized image view

	private JComboBox<String> methodList; // the selected binarization method
	private JTextArea statusArea; // to print some status text

	/**
	 * Algorithm to find the appropriate/desired threshold.
	 */
	private ThresholdFindingAlgorithm thresholdAlgorithm;

	private AbstractFloodFilling floodFillingAlgorithm;

	private String message;
	protected Mode mode;
	private JComboBox<String> neighbourModeBox;
	private JCheckBox debugModeCheckbox;
	private JPanel imagesPanel;

	public FloodFillingGui() {
		super(new BorderLayout(border, border));

		// load the default image
		File input = new File(initalOpen);

		if (!input.canRead())
			input = openFile(); // file not found, choose another image

		srcView = new ImageView(input);
		srcView.setMaxSize(new Dimension(maxWidth, maxHeight));

		// create an empty destination image
		dstView = new ImageView(srcView.getImgWidth(), srcView.getImgHeight());
		dstView.setMaxSize(new Dimension(maxWidth, maxHeight));

		// load image button
		JButton load = new JButton("Bild öffnen");
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File input = openFile();
				if (input != null) {
					srcView.loadImage(input);
					srcView.setMaxSize(new Dimension(maxWidth, maxHeight));
					runFloodFilling();
				}
			}
		});

		// selector for the binarization method
		JLabel methodText = new JLabel("Methode:");
		String[] methodNames = { "---", "Verfahren mit Stack", "Verfahren mit Queue",
				"sequentiellen Regionenmarkierung",
				"Optimiertes Verfahren mit Stack",
				"Optimiertes Verfahren mit Queue"};

		methodList = new JComboBox<String>(methodNames);
		methodList.setSelectedIndex(0); // set initial method
		methodList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runFloodFilling();
			}
		});

		String[] neighbourModes = { "8 neighbours", "4 neighbours" };

		neighbourModeBox = new JComboBox<String>(neighbourModes);
		neighbourModeBox.setSelectedIndex(0); // set initial method
		neighbourModeBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runFloodFilling();
			}
		});

		// Debug mode checkbox
		this.debugModeCheckbox = new JCheckBox("Debug mode");

		// some status text
		statusArea = new JTextArea(TEXTAREA_ROWS, TEXTAREA_COLS);
		statusArea.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane(statusArea);

		// arrange all controls
		JPanel controls = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, border, 0, 0);
		controls.add(load, c);
		controls.add(methodText, c);
		controls.add(methodList, c);
		controls.add(neighbourModeBox, c);
		controls.add(debugModeCheckbox, c);

		this.imagesPanel = new JPanel(new FlowLayout());
		imagesPanel.add(srcView);
		imagesPanel.add(dstView);

		add(controls, BorderLayout.NORTH);
		add(imagesPanel, BorderLayout.CENTER);
		add(scrollPane, BorderLayout.SOUTH);

		setBorder(BorderFactory.createEmptyBorder(border, border, border, border));
	}

	private File openFile() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Images (*.jpg, *.png, *.gif)", "jpg", "png",
				"gif");
		chooser.setFileFilter(filter);
		chooser.setCurrentDirectory(openPath);
		int ret = chooser.showOpenDialog(this);
		if (ret == JFileChooser.APPROVE_OPTION) {
			frame.setTitle(title + chooser.getSelectedFile().getName());
			return chooser.getSelectedFile();
		}
		return null;
	}

	private static void createAndShowGUI() {
		// create and setup the window
		title = title + " - " + author + " - ";

		frame = new JFrame(title + initalOpen);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JComponent newContentPane = new FloodFillingGui();
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// display the window.
		frame.pack();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		frame.setLocation((screenSize.width - frame.getWidth()) / 2, (screenSize.height - frame.getHeight()) / 2);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	protected void runFloodFilling() {

		String methodName = (String) methodList.getSelectedItem();
		String neighbourMode = (String) neighbourModeBox.getSelectedItem();

		// image dimensions
		int width = srcView.getImgWidth();
		int height = srcView.getImgHeight();

		// get pixels arrays
		int srcPixels[] = srcView.getPixels();
		int dstPixels[] = java.util.Arrays.copyOf(srcPixels, srcPixels.length);
		dstView.setPixels(dstPixels, width, height);
		frame.pack();

		// this.message = "Binarisieren mit \"" + methodName + "\"";
		this.message = "Auffinden von Bildregionen mit \"" + methodName + "\" und \"" + neighbourMode + "\" ";

		statusArea.setText(message);

		long time = 0;
		
		switch (methodList.getSelectedIndex()) {
		case 1: // depth first
			this.floodFillingAlgorithm = Factory.newDepthFirst(this);
			time = showImageRegions(dstPixels);
			message += "\nMax stack size: " + ((DepthFirst) floodFillingAlgorithm).getMaxStackSize();
			break;
		case 2: // breadth first
			this.floodFillingAlgorithm = Factory.newBreadthFirst(this);
			time = showImageRegions(dstPixels);
			message += "\nMax queue size: " + ((BreadthFirst) floodFillingAlgorithm).getMaxQueueSize();
			break;
		case 3: // sequentiellen Regionenmarkierung
			this.floodFillingAlgorithm = Factory.newSequential(this);
			time = showImageRegions(dstPixels);
			break;
		case 4: // optimized depth first
			this.floodFillingAlgorithm = Factory.newOptimizedDepthFirst(this);
			time = showImageRegions(dstPixels);
			message += "\nMax stack size: " + ((DepthFirst) floodFillingAlgorithm).getMaxStackSize();
			break;
		case 5: // optimized breadth first
			this.floodFillingAlgorithm = Factory.newOptimizedBreadthFirst(this);
			time = showImageRegions(dstPixels);
			message += "\nMax queue size: " + ((BreadthFirst) floodFillingAlgorithm).getMaxQueueSize();
			break;
		// other cases: do not change dstPixels
		}

		if (!this.isDebug()) {
			dstView.setPixels(dstPixels, width, height);
			frame.pack();
		}

		// dstView.saveImage("out.png");

		statusArea.setText(message + "\nRequired time: " + time + " ms");
	}

	private Mode getMode() {
		if (this.neighbourModeBox.getSelectedIndex() == 0) {
			return Mode.NEIGHBOURS8;
		} else {
			return Mode.NEIGHBOURS4;
		}
	}

	private boolean isDebug() {
		return this.debugModeCheckbox.isSelected();
	}

	private long showImageRegions(int[] dstPixels) {
		this.floodFillingAlgorithm.setMode(this.getMode());
		this.floodFillingAlgorithm.setOriginalBinaryPixels(this.srcView.getImgWidth(), this.srcView.getImgHeight(),
				this.srcView.getPixels());

		if (this.isDebug()) {
			// in debug mode, the regions will be drawn on single label change
			long startTime = System.currentTimeMillis();
			
			this.floodFillingAlgorithm.execute();
			
			return System.currentTimeMillis() - startTime;
			
		} else {
			// in non-debug mode, the regions will be drawn when all labels were
			// set
			this.floodFillingAlgorithm.deleteObservers();

			// only measure labeling algorithm runtime, not the time to color pixels
			long startTime = System.currentTimeMillis();
			int[][] labeled2dPixels = this.floodFillingAlgorithm.execute();
			long time = System.currentTimeMillis() - startTime;
			
			int[] labeledPixels = ImageUtil.getFlatArray(this.srcView.getImgWidth(), this.srcView.getImgHeight(),
					labeled2dPixels);

			this.colorRegionsByLabel(dstPixels, labeledPixels);
			
			return time;
		}
	}

	/**
	 * @param dstPixels
	 * @param labeledPixels
	 */
	private void colorRegionsByLabel(int[] dstPixels, int[] labeledPixels) {
		for (int i = 0; i < labeledPixels.length; i++) {
			dstPixels[i] = ImageUtil.mapLabelToColor(labeledPixels[i]);
		}
	}

	void binarize(int pixels[]) {
		int threshold = this.thresholdAlgorithm.calculateThreshold(pixels);
		this.message += "; Schwellwert: " + threshold;

		for (int i = 0; i < pixels.length; i++) {
			int pixelValue = pixels[i];
			int gray = calculateGrayValue(pixelValue);
			pixels[i] = gray < threshold ? 0xff000000 : 0xffffffff;
		}
	}

	public static int calculateGrayValue(int pixelValue) {
		// greyValue = R + G + B / 3
		return ((pixelValue & 0xff) + ((pixelValue & 0xff00) >> 8) + ((pixelValue & 0xff0000) >> 16)) / 3;
	}

	@Override
	public void update(Observable o, final Object arg) {
		// color a pixel in the destination view when its label changed (should
		// only be called in debug mode since it slows down the process)
		if (arg instanceof LabeledPoint) {
			LabeledPoint point = (LabeledPoint) arg;

			int[] labelPixels = dstView.getPixels();

			int labelPos = ImageUtil.calc1DPosition(srcView.getImgWidth(), (int) point.getX(), (int) point.getY());

			labelPixels[labelPos] = ImageUtil.mapLabelToColor(point.getLabel());

			dstView.setPixels(labelPixels, srcView.getImgWidth(), srcView.getImgHeight());
			
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
			    public void run() {
					// TODO check why GUI isn't updated
			    	dstView.invalidate();
			        dstView.repaint();  // repaint(), etc. according to changed states
			        
			        imagesPanel.invalidate();
			        imagesPanel.repaint();
			        
			        frame.repaint();
			    }
			});

			System.out.println("Updated " + point);
		}
	}
}
