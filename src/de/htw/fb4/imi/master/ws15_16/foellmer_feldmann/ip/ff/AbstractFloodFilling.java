/**
 * Image Processing WiSe 2015/16
 *
 * Authors: Markus Föllmer, Sascha Feldmann
 */
package de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.ff;

import java.awt.Point;
import java.util.Observable;

import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.util.ImageUtil;
import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.util.LabeledPoint;

/**
 * [SHORT_DESCRIPTION]
 *
 * @author Sascha Feldmann <sascha.feldmann@gmx.de>
 * @since 04.11.2015
 */
public abstract class AbstractFloodFilling extends Observable {

	public static final int NOT_LABELED = 0;
	public static final int START_LABEL = 1;

	public enum Mode {
		NEIGHBOURS4, NEIGHBOURS8
	}

	protected int[][] originalBinaryPixels;
	protected int[][] labeledPixels;
	protected int width;
	protected int height;
	protected Mode mode = Mode.NEIGHBOURS4;

	/**
	 * Set the original pixels by an 1-dimensional pixel array.
	 * 
	 * Internally, the pixels are stored as 2-dimensional array. For conversion,
	 * hand in widht and height.
	 * 
	 * @param width
	 * @param height
	 * @param originalPixels
	 */
	public void setOriginalBinaryPixels(int width, int height, int[] originalPixels) {
		this.width = width;
		this.height = height;
		this.originalBinaryPixels = new int[width][height];
		this.labeledPixels = new int[width][height];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int pos = ImageUtil.calc1DPosition(width, x, y);

				if (ImageUtil.isForegoundPixel(originalPixels[pos])) {
					this.originalBinaryPixels[x][y] = 1;
				} else {
					this.originalBinaryPixels[x][y] = 0;
				}

				this.labeledPixels[x][y] = NOT_LABELED; // in the beginning, all pixels are not labeled
			}
		}
	}

	public int[][] getOriginalPixels() {
		return originalBinaryPixels;
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	protected boolean canBeLabeled(int x, int y) {
		return this.isForegroundPixel(x, y) && NOT_LABELED == this.labeledPixels[x][y];
	}
	
	protected boolean isForegroundPixel(int x, int y) {
		return 1 == this.originalBinaryPixels[x][y];
	}
	protected boolean isWithinImageBoundaries(int x, int y) {
		return (x >= 0) && (x < width) && (y >= 0) && (y < height); 
	}
	
	protected boolean isNotLabeled(int x, int y) {
		return NOT_LABELED == this.labeledPixels[x][y];
	}
	
	protected boolean isLabeled(int x, int y) {
		return !this.isNotLabeled(x, y);
	}

	protected void labelPixel(int x, int y, int label) {
		this.labeledPixels[x][y] = label;
		LabeledPoint labeledPixel = new LabeledPoint(x, y, label);

		// notify GUI on label change
		this.setChanged();
		this.notifyObservers(labeledPixel);
	}

	/**
	 * Main method to start algorithm. Each subclass should implement this
	 * method.
	 * 
	 * @return a 2d (x, y) array of pixel labels
	 */
	public int[][] execute() {
		this.ensureThatOriginalWasSet();

		return this.labeledPixels;
	}

	private void ensureThatOriginalWasSet() {
		if (null == this.originalBinaryPixels) {
			throw new IllegalStateException(
					"originalPixels wasn't set. Please set it before calling executeOutline().");
		}
	}

	protected Point getSeed(int x, int y) {
		return new Point(x, y);
	}

}
