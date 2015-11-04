/**
 * Image Processing WiSe 2015/16
 *
 * Authors: Markus Föllmer, Sascha Feldmann
 */
package de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.ff;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Implementation of flood filling.
 *
 * @since 27.10.2015
 */
public class BreadthFirst extends AbstractFloodFilling {

	private int queueMaxSize = 0;

	
	public int getMaxQueueSize() {
		return queueMaxSize;
	}

	public int[][] execute() {
		super.execute();

		executeFloodFillingInScanLineOrder();
		
		return this.labeledPixels;
	}

	private void executeFloodFillingInScanLineOrder() {
		int label = START_LABEL;

		// walk through image pixels in scan-line order, execute on
		// unlabeled foreground pixels
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (canBeLabeled(x, y)) {
					this.breadthFirst(x, y, label);

					label++;
				}
			}
		}
	}
	
	/**
	 * mit queue
	 */
	public void breadthFirst(int u, int v, int label) {
		Queue<Point> Q = new LinkedList<Point>(); // queue Q
		Q.add(new Point(u, v));
		while (!Q.isEmpty()) {
			Point p = Q.remove(); // get the next point to process
			int x = p.x;
			int y = p.y;
			
			if (isWithinImageBoundaries(x, y)
				&& canBeLabeled(x, y)) {
				
				switch (this.mode) {
					case NEIGHBOURS4:
						queue4Neighbour(Q, x, y);
						break;
					case NEIGHBOURS8:
						queue8Neighbour(Q, x, y);
						break;
					}
				
				labelPixel(x, y, label);
			}
		}
	}
	
	private void queue4Neighbour(Queue<Point> q, int x, int y) {
		addToQueue(q, x + 1, y);
		addToQueue(q, x, y + 1);
		addToQueue(q, x, y - 1);
		addToQueue(q, x - 1, y);
	}
	
	private void queue8Neighbour(Queue<Point> q, int x, int y) {
		addToQueue(q, x + 1, y);
		addToQueue(q, x + 1, y + 1);
		addToQueue(q, x, y + 1);
		addToQueue(q, x + 1, y - 1);
		addToQueue(q, x, y - 1);
		addToQueue(q, x - 1, y - 1);
		addToQueue(q, x - 1, y);
		addToQueue(q, x - 1, y + 1);
	}
	
	protected void addToQueue(Queue<Point> q, int newX, int newY) {
		q.add(new Point(newX, newY));
		
		this.updateLargestQueueInfo(q);
	}	
	
	protected void updateLargestQueueInfo(Queue<Point> q) {
		if (q.size() > this.queueMaxSize) {
			this.queueMaxSize = q.size();
		}		
	}
}
