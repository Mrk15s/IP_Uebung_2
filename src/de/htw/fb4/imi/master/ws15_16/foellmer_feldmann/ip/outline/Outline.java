/**
 * Image Processing WiSe 2015/16
 *
 * Authors: Markus Föllmer, Sascha Feldmann
 */
package de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.outline;

/**
 * Implementation of an outline algorithm including all necessary steps such as eroding, reverting and reflecting pixels.
 *
 * @since 20.10.2015
 */
public class Outline {

	private int[][] originalBinaryPixels;
	private int[][] erodedPixels;
	private int[][] outlinePixels;

	protected int width;
	protected int height;

	private int[][] structureElement;

	public Outline() {
		this.create4NeighboursStructureElement();
	}

	private void create4NeighboursStructureElement() {
		this.structureElement = new int[3][3];

		this.structureElement[0][0] = 0;
		this.structureElement[0][1] = 0xff000000;
		this.structureElement[0][2] = 0;
		this.structureElement[1][0] = 0xff000000;
		this.structureElement[1][1] = 0xff000000;
		this.structureElement[1][2] = 0xff000000;
		this.structureElement[2][0] = 0;
		this.structureElement[2][1] = 0xff000000;
		this.structureElement[2][2] = 0;
	}

	public int[][] getOriginalPixels() {
		return originalBinaryPixels;
	}

	/**
	 * Set the original pixels by an 1-dimensional pixel array. 
	 * 
	 * Internally, the pixels are stored as 2-dimensional array. For conversion, hand in widht and height.
	 * @param width
	 * @param height
	 * @param originalPixels
	 */
	public void setOriginalBinaryPixels(int width, int height, int[] originalPixels) {
		this.width = width;
		this.height = height;
		this.originalBinaryPixels = new int[width][height];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int pos = calc1DPosition(width, x, y);

				this.originalBinaryPixels[x][y] = originalPixels[pos];
			}
		}
	}
	
	private int calc1DPosition(int width, int x, int y) {
		int pos = y * width + x;
		return pos;
	}

	/**
	 * Set the original pixels as 2d-array as it's internally expected.
	 * 
	 * @see setOriginalBinaryPixels(int width, int height, int[] originalPixels) if you want to hand in a 1d array
	 * @param originalPixels
	 */
	public void setOriginalBinaryPixels(int[][] originalPixels) {
		this.originalBinaryPixels = originalPixels;
	}

	/**
	 * Main algorithm.
	 * 
	 * Make sure to have set the original/input pixels.
	 * 
	 * @return 2d array: the outline pixels.
	 */
	public int[][] executeOutline() {
		this.ensureThatOriginalWasSet();

		this.erodePixel();
		this.outlinePixel();

		return this.outlinePixels;
	}
	
	private void ensureThatOriginalWasSet() {
		if (null == this.originalBinaryPixels) {
			throw new IllegalStateException(
					"originalPixels wasn't set. Please set it before calling executeOutline().");
		}
	}
	
	/**
	 * Take original pixel, do erosion
	 */
	protected void erodePixel() {
		int[][] invertedOriginal = this.invertPixels(this.originalBinaryPixels);
		int[][] reflectedStructureElement = this.reflectPixels(this.structureElement);

		this.erodedPixels = this.invertPixels(this.dilatePixel(reflectedStructureElement, invertedOriginal));
	}
	
	protected int[][] invertPixels(int pixels[][]) {
		int[][] invertedPixels = new int[pixels.length][pixels[0].length];

		for (int i = 0; i < pixels.length; i++) {
			for (int j = 0; j < pixels[i].length; j++) {
				invertedPixels[i][j] = pixels[i][j] == 0xff000000 ? 0xffffffff : 0xff000000;
			}
		}

		return invertedPixels;
	}

	private int[][] reflectPixels(int[][] pixels) {
		int[][] reflectedPixels = new int[pixels.length][pixels[0].length];

		for (int i = 0; i < pixels.length; i++) {
			for (int j = 0; j < pixels[i].length; j++) {
				reflectedPixels[j][i] = pixels[i][j];
			}
		}

		return reflectedPixels;
	}	
	
	protected int[][] dilatePixel(int[][] structureElement, int binaryPixels[][]) {
		int[][] dilatedPixels = new int[this.width][this.height];
		
		for (int i = -1; i < structureElement.length - 1; i++) {
			for (int j = -1; j < structureElement[i + 1].length - 1; j++) {
				for (int x = 0; i + x < binaryPixels.length - 1; x++) {
					for (int y = 0; j + y < binaryPixels[x].length - 1; y++) {
						if (structureElement[i + 1][j + 1] == 0xff000000
								&& i + x >= 0
								&& j + y >= 0
								&& binaryPixels[x][y] == structureElement[i + 1][j + 1]
								) {
							dilatedPixels[i + x][j + y] = structureElement[i + 1][j + 1];
						}
					}
				}
			}
		}

		return dilatedPixels;
	}	

	protected void outlinePixel() {
		int[][] invertedEroded = this.invertPixels(this.erodedPixels);
		this.outlinePixels = new int[this.erodedPixels.length][this.erodedPixels[0].length];

		for (int i = 0; i < invertedEroded.length; i++) {
			for (int j = 0; j < invertedEroded[i].length; j++) {
				if (invertedEroded[i][j] == this.originalBinaryPixels[i][j]) {
					this.outlinePixels[i][j] = invertedEroded[i][j];
				} else {
					this.outlinePixels[i][j] = 0xffffffff; // "unmarked" pixels have to be white
				}
			}
		}
	}
	
	/**
	 * Use this method to get a 1-d array by a given 2d one.
	 * 
	 * @param width
	 * @param height
	 * @param pixels
	 * @return
	 */
	public int[] getFlatArray(int width, int height, int[][] pixels) {
		int[] flat = new int[width * height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int pos = calc1DPosition(width, i, j);
				flat[pos] = pixels[i][j];
			}
		}

		return flat;
	}
}
