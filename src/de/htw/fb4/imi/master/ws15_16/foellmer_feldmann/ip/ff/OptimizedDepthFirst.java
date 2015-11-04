/**
 * Image Processing WiSe 2015/16
 *
 * Authors: Markus Föllmer, Sascha Feldmann
 */
package de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.ff;

import java.awt.Point;
import java.util.Deque;

/**
 * Just like the {@link BreadthFirst}, the {@link DepthFirst} can also be optimzied by only adding FOREGROUND and pixels within the image boundaries to the stack.
 *
 * @author Sascha Feldmann <sascha.feldmann@gmx.de>
 * @since 04.11.2015
 */
public class OptimizedDepthFirst extends DepthFirst {
	
	protected void pushToStack(Deque<Point> S, int x, int y) {
		if ( isWithinImageBoundaries(x, y)
				&&	canBeLabeled(x, y)) {
			S.push(new Point(x, y));
		}
	}
}
