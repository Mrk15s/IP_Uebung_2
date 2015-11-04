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
	private int stackMaxSize = 0;

	public int getMaxStackSize() {
		return stackMaxSize;
	}

	public int[][] execute() {
		super.execute();

		executeFloodFillingInScanLineOrder();

		return this.labeledPixels;
	}

	private void executeFloodFillingInScanLineOrder() {
		int label = START_LABEL;

		// walk through image pixels in scan-line order, execute depth first on
		// unlabeled foreground pixels
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (canBeLabeled(x, y)) {
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
		S.push(getSeed(u, v)); // push seed coordinate (u,v) onto S

		while (!S.isEmpty()) {
			Point p = S.pop(); // pop first coordinate off the stack
			int x = p.x;
			int y = p.y;

			if (isWithinImageBoundaries(x, y) && canBeLabeled(x, y)) {
				switch (mode) {
					case NEIGHBOURS4:
						push4Neighbour(S, x, y);
						break;
					case NEIGHBOURS8:
						push8Neighbour(S, x, y);
						break;
				}

				labelPixel(x, y, label);
			}
		}
	}

	protected void push4Neighbour(Deque<Point> S, int x, int y) {
		pushToStack(S, x + 1, y);
		pushToStack(S, x, y + 1);
		pushToStack(S, x, y - 1);
		pushToStack(S, x - 1, y);

		this.updateLargestStackInfo(S);
	}

	protected void pushToStack(Deque<Point> S, int x, int y) {
		S.push(new Point(x, y));
	}

	protected void push8Neighbour(Deque<Point> S, int x, int y) {
		pushToStack(S, x + 1, y);
		pushToStack(S, x + 1, y + 1);
		pushToStack(S, x, y + 1);
		pushToStack(S, x + 1, y - 1);
		pushToStack(S, x, y - 1);
		pushToStack(S, x - 1, y - 1);
		pushToStack(S, x - 1, y);
		pushToStack(S, x - 1, y + 1);

		this.updateLargestStackInfo(S);
	}

	protected void updateLargestStackInfo(Deque<Point> q) {
		if (q.size() > this.stackMaxSize) {
			this.stackMaxSize = q.size();
		}
	}
}
