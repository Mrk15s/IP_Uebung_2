// Copyright (C) 2014 by Klaus Jung
// All rights reserved.
// Date: 2014-10-02
package de.htw.fb4.imi.master.ws15_16.jungk;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.Factory;
import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.ff.AbstractFloodFilling;
import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.outline.Outline;
import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.treshold.IsoData;
import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.treshold.ThresholdFindingAlgorithm;
import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.util.ImageUtil;
import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.util.LabeledPoint;

import java.awt.event.*;
import java.awt.*;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class Binarize extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;
	private static final int border = 10;
	private static final int maxWidth = 400;
	private static final int maxHeight = 400;
	private static final File openPath = new File(".");
	private static final String title = "Binarisierung";
	private static final String author = "Markus Föllmer & Sascha Feldmann";
	private static final String initalOpen = "tools.png";

	private static JFrame frame;

	private ImageView srcView; // source image view
	private ImageView dstView; // binarized image view

	private JComboBox<String> methodList; // the selected binarization method
	private JLabel statusLine; // to print some status text
	private JSlider thresholdSlider;

	/**
	 * Algorithm to find the appropriate/desired threshold.
	 */
	private ThresholdFindingAlgorithm thresholdAlgorithm;

	private AbstractFloodFilling floodFillingAlgorithm;

	private String message;

	public Binarize() {
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

		// load image button
		// JButton outlineBtn = new JButton("Outline");
		// outlineBtn.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// createOutline();
		// }
		// });
		// load outline checkbox
		// JCheckBox outlineBox = new JCheckBox("Outline");
		// outlineBox.addItemListener(new ItemListener() {
		// @Override
		// public void itemStateChanged(ItemEvent e) {
		// if (e.getStateChange() == ItemEvent.SELECTED){
		// createOutline();
		// } else {
		// binarizeImage();
		// }
		// // System.out.println(e.getStateChange() == ItemEvent.SELECTED ?
		// "selected" : "unasdted");
		// }
		// });

		// selector for the binarization method
		JLabel methodText = new JLabel("Methode:");
		String[] methodNames = { "---", "Verfahren mit Stack", "Verfahren mit Queue",
				"sequentiellen Regionenmarkierung" };

		methodList = new JComboBox<String>(methodNames);
		methodList.setSelectedIndex(0); // set initial method
		methodList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runFloodFilling();
			}
		});

		// some status text
		statusLine = new JLabel(" ");

		// arrange all controls
		JPanel controls = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, border, 0, 0);
		controls.add(load, c);
		controls.add(methodText, c);
		controls.add(methodList, c);

		JPanel images = new JPanel(new FlowLayout());
		images.add(srcView);
		images.add(dstView);

		add(controls, BorderLayout.NORTH);
		add(images, BorderLayout.CENTER);
		add(statusLine, BorderLayout.SOUTH);

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
		frame = new JFrame(title + " - " + author + " - " + initalOpen);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JComponent newContentPane = new Binarize();
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

		// image dimensions
		int width = srcView.getImgWidth();
		int height = srcView.getImgHeight();

		// get pixels arrays
		int srcPixels[] = srcView.getPixels();
		int dstPixels[] = java.util.Arrays.copyOf(srcPixels, srcPixels.length);

		// this.message = "Binarisieren mit \"" + methodName + "\"";
		this.message = "Auffinden von Bildregionen mit \"" + methodName + "\"";

		statusLine.setText(message);

		long startTime = System.currentTimeMillis();

		switch (methodList.getSelectedIndex()) {
			case 1: // depth first
				message += "iterativem Verfahren mit Stack; ";
				System.out.println("depth first");
	
				this.floodFillingAlgorithm = Factory.newDepthFirst(this);
				showImageRegions(dstPixels);
				break;
			case 2: // breadth first
				message += "iterativem Verfahren mit Queue; ";
				System.out.println("breadth first");
	
				this.floodFillingAlgorithm = Factory.newBreadthFirst(this);
				showImageRegions(dstPixels);
				break;
			case 3: // sequentiellen Regionenmarkierung
				message += "sequentieller Regionenmarkierung; ";
				System.out.println(" sequentiellen Regionenmarkierung");
	
				this.floodFillingAlgorithm = Factory.newSequential(this);
				showImageRegions(dstPixels);
				break;
				// other cases: do not change dstPixels
		}

		long time = System.currentTimeMillis() - startTime;

		dstView.setPixels(dstPixels, width, height);

		// dstView.saveImage("out.png");

		frame.pack();

		statusLine.setText(message + " in " + time + " ms");
	}

	private void showImageRegions(int[] dstPixels) {
		this.floodFillingAlgorithm.setOriginalBinaryPixels(this.srcView.getImgWidth(), this.srcView.getImgHeight(),
				this.srcView.getPixels());
		
		this.floodFillingAlgorithm.execute();
		
		/*int[] labeledPixels = ImageUtil.getFlatArray(this.srcView.getImgWidth(), this.srcView.getImgHeight(),
				this.floodFillingAlgorithm.execute());

		this.colorRegionsByLabel(dstPixels, labeledPixels);*/
	}

	/**
	 * @deprecated since the GUI is an observer now and the pixels are colored immediatly when the label changed
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
	public void update(Observable o, Object arg) {
		// color a pixel in the destination view when its label changed
		if (arg instanceof LabeledPoint) {
			LabeledPoint point = (LabeledPoint) arg;
			
			int[] labelPixels = dstView.getPixels();
			
			int labelPos = ImageUtil.calc1DPosition(
					this.srcView.getImgWidth(),
					(int) point.getX(),
					(int) point.getY());
			
			labelPixels[labelPos] = ImageUtil.mapLabelToColor(point.getLabel());
			
			dstView.setPixels(labelPixels);
		}		
	}
}
