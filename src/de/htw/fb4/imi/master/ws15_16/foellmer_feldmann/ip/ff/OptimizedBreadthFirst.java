/**
 * Image Processing WiSe 2015/16
 *
 * Authors: Markus Föllmer, Sascha Feldmann
 */
package de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.ff;

import java.awt.Point;
import java.util.Queue;

/**
 * The created queue can be optimized to not contain background pixels and pixels that are outside the image.
 * 
 * This implementation offers a better implementation for the queue push methods.
 *
 * @author Sascha Feldmann <sascha.feldmann@gmx.de>
 * @since 04.11.2015
 */
public class OptimizedBreadthFirst extends BreadthFirst {

	protected void addToQueue(Queue<Point> q, int x, int y) {
		if ( isWithinImageBoundaries(x, y)
			&&	canBeLabeled(x, y)) {
			q.add(new Point(x, y));
			
			this.updateLargestQueueInfo(q);			
		}		
	}	
}
