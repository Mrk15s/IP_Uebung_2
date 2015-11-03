/**
 * Image Processing WiSe 2015/16
 *
 * Authors: Markus Föllmer, Sascha Feldmann
 */
package de.htw.fb4.imi.master.ws15_16.feldmann_foellmer.ff;

import java.awt.Point;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

import javax.jws.WebParam.Mode;

import de.htw.fb4.imi.master.ws15_16.feldmann_foellmer.util.ImageUtil;

/**
 * Implementation of flood filling.
 *
 * @since 27.10.2015
 */
public class FloodFilling {
	private static final int NOT_LABELED = 0;
	private static final int START_LABEL = 1;

	public enum Mode {
		NEIGHBOURS4, NEIGHBOURS8
	}

	private int[][] originalBinaryPixels;
	private int[][] labeledPixels;

	protected int width;
	protected int height;

	protected Mode mode = FloodFilling.Mode.NEIGHBOURS8;

	public FloodFilling() {

	}

	public int[][] getOriginalPixels() {
		return originalBinaryPixels;
	}

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

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int pos = calc1DPosition(width, x, y);

				if (ImageUtil.isForegoundPixel(originalPixels[pos])) {
					this.originalBinaryPixels[x][y] = 1; 
				} else {
					this.originalBinaryPixels[x][y] = 0;
				}
				
				this.labeledPixels[x][y] = NOT_LABELED; // not labeled
			}
		}
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	private int calc1DPosition(int width, int x, int y) {
		int pos = y * width + x;
		return pos;
	}
	
	public void execute()
	{
		int label = START_LABEL;
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (1 == this.originalBinaryPixels[x][y]
						&& NOT_LABELED == this.labeledPixels[x][y]) {
					this.depthFirst(x, y, label);
					
					label++;
				}
			}
		}
	}

	/**
	 * mit stack
	 */
	public void depthFirst(int u, int v, int label) {
		Deque<Point> S = new LinkedList<Point>(); // create an empty stack stack
													// S
		S.push(new Point(u, v)); // push seed coordinate (u,v) onto S
		while (!S.isEmpty()) {
			Point p = S.pop(); // pop first coordinate off the stack
			int x = p.x;
			int y = p.y;
			int pos = calc1DPosition(width, x, y);

			if ((x >= 0) && (x < width) && (y >= 0) && (y < height) && this.originalBinaryPixels[x][y] == 1) {
				// ip.putPixel(x, y, label);
				// kernelmitte (x,y) mit label zeichnen

				// noch vierer element!!!!
				switch (mode) {
					case NEIGHBOURS4:
						push4Neighbour(S, x, y);
						break;
					case NEIGHBOURS8:
						push8Neighbour(S, x, y);
						break;
				}
				
				this.labeledPixels[u][v] = label;
			}
		}
	}

	private void push4Neighbour(Deque<Point> S, int x, int y) {
		S.push(new Point(x + 1, y));
		S.push(new Point(x, y + 1));
		S.push(new Point(x, y - 1));
		S.push(new Point(x - 1, y));
	}

	private void push8Neighbour(Deque<Point> S, int x, int y) {
		S.push(new Point(x + 1, y - 1));
		S.push(new Point(x + 1, y));
		S.push(new Point(x + 1, y + 1));
		S.push(new Point(x, y + 1));
		S.push(new Point(x, y - 1));
		S.push(new Point(x - 1, y - 1));
		S.push(new Point(x - 1, y));
		S.push(new Point(x - 1, y + 1));
	}

	/**
	 * mit queue
	 */
	public void breadthFirst(int u, int v, int label) {
		// Queue<Point> Q = new LinkedList<Point>(); // queue Q
		// Q.add(new Point(u, v));
		// while (!Q.isEmpty()) {
		// Point p = Q.remove(); // get the next point to process
		// int x = p.x;
		// int y = p.y;
		// if ((x >= 0) && (x < width) && (y >= 0) && (y < height) &&
		// ip.getPixel(x, y) == 1) {
		// ip.putPixel(x, y, label);
		// //TODO : noch vierer element !!!!
		// Q.add(new Point(x + 1, y));
		// Q.add(new Point(x, y + 1));
		// Q.add(new Point(x, y - 1));
		// Q.add(new Point(x - 1, y));
		// }
		// }
	}

	/**
	 * Sequentielle Regionenmarkierung
	 */
	public void regionLabeling() {
	}

}
