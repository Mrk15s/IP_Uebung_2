/**
 * Image Processing WiSe 2015/16
 *
 * Authors: Markus Föllmer, Sascha Feldmann
 */
package de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.ff;

import java.awt.Point;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Implementation of flood filling.
 *
 * @since 27.10.2015
 */
public class DepthFirst extends AbstractFloodFilling {
	
	public int[][] execute()
	{
		super.execute();
		
		executeFloodFillingInScanLineOrder();
		
		return this.labeledPixels;
	}

	private void executeFloodFillingInScanLineOrder() {
		int label = START_LABEL;
		
		// walk through image pixels in scan-line order, execute depth first on unlabeled foreground pixels
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

			if ((x >= 0) && (x < width) && (y >= 0) && (y < height) && this.originalBinaryPixels[x][y] == 1) {
				switch (mode) {
					case NEIGHBOURS4:
						push4Neighbour(S, x, y);
						break;
					case NEIGHBOURS8:
						push8Neighbour(S, x, y);
						break;
				}
				
				labelPixel(u, v, label);
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
		S.push(new Point(x + 1, y));
		S.push(new Point(x + 1, y + 1 ));
		S.push(new Point(x, y + 1));
		S.push(new Point(x + 1, y - 1));
		S.push(new Point(x, y - 1));
		S.push(new Point(x - 1, y - 1));
		S.push(new Point(x - 1, y));
		S.push(new Point(x - 1, y + 1));
	}
}
