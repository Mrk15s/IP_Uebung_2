/**
 * Image Processing WiSe 2015/16
 *
 * Authors: Markus F�llmer, Sascha Feldmann
 */
package de.htw.fb4.imi.master.ws15_16.feldmann_foellmer.ff;

import java.awt.Point;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Implementation of flood filling.
 *
 * @since 27.10.2015
 */
public class FloodFilling {
	
	private int[][] originalBinaryPixels;
	private int[][] fillPixels;
	
	protected int width;
	protected int height;
	
	
	public FloodFilling() {
		
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
	 * mit stack
	 */
	public void depthFirst(int u, int v, int label){		
		Deque<Point> S = new LinkedList<Point>(); 	// create an empty stack stack S
		S.push(new Point(u, v));					// push seed coordinate (u,v) onto S
		while (!S.isEmpty()) {
			Point p = S.pop();						// pop first coordinate off the stack
			int x = p.x;
			int y = p.y;
			int pos = calc1DPosition(width, x, y);
			
			if ((x >= 0) && (x < width) && (y >= 0) && (y < height) && pos == 1) {
//				ip.putPixel(x, y, label);
				// kernelmitte (x,y) mit label zeichnen
				
				//noch vierer element!!!!
//				S.push(new Point(x + 1, y));
//				S.push(new Point(x, y + 1));
//				S.push(new Point(x, y - 1));
//				S.push(new Point(x - 1, y));
				// kernel: achternachbarschaft
				S.push(new Point(x + 1, y - 1));
				S.push(new Point(x + 1, y));
				S.push(new Point(x + 1, y + 1));
				S.push(new Point(x, y + 1));
				S.push(new Point(x, y - 1));
				S.push(new Point(x - 1, y - 1));
				S.push(new Point(x - 1, y));
				S.push(new Point(x - 1, y + 1));
			}
		}	
	}
	
	/**
	 * mit queue
	 */
	public void breadthFirst(int u, int v, int label) {
//		Queue<Point> Q = new LinkedList<Point>(); // queue Q
//		Q.add(new Point(u, v));
//		while (!Q.isEmpty()) {
//			Point p = Q.remove(); // get the next point to process
//			int x = p.x;
//			int y = p.y;
//			if ((x >= 0) && (x < width) && (y >= 0) && (y < height) && ip.getPixel(x, y) == 1) {
//				ip.putPixel(x, y, label);
//				//TODO : noch vierer element !!!!
//				Q.add(new Point(x + 1, y));
//				Q.add(new Point(x, y + 1));
//				Q.add(new Point(x, y - 1));
//				Q.add(new Point(x - 1, y));
//			}
//		}	
	}
	
	/**
	 * Sequentielle Regionenmarkierung
	 */
	public void regionLabeling() {		
	}

}