/**
 * Image Processing WiSe 2015/16
 *
 * Authors: Markus F�llmer, Sascha Feldmann
 */
package de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.ff;

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
	protected Mode mode = Mode.NEIGHBOURS8;	

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
				
				this.labeledPixels[x][y] = NOT_LABELED; // not labeled
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
	
	protected void labelPixel(int u, int v, int label) {
		this.labeledPixels[u][v] = label;
		LabeledPoint labeledPixel = new LabeledPoint(u, v, label);
		
		this.setChanged();
		this.notifyObservers(labeledPixel);
	}
	
	/**
	 * Main method to start algorithm. Each subclass should implement this method.
	 * @return a 2d (x, y) array of pixel labels
	 */
	public int[][] execute()
	{
		this.ensureThatOriginalWasSet();
		
		return this.labeledPixels;
	}
	
	private void ensureThatOriginalWasSet() {
		if (null == this.originalBinaryPixels) {
			throw new IllegalStateException(
					"originalPixels wasn't set. Please set it before calling executeOutline().");
		}
	}
}
