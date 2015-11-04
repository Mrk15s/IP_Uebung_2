/**
 * Image Processing WiSe 2015/16
 *
 * Authors: Markus Föllmer, Sascha Feldmann
 */
package de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.util;

import java.awt.Point;

/**
 * A special {@link Point} with an additional label int value.
 *
 * @author Sascha Feldmann <sascha.feldmann@gmx.de>
 * @since 04.11.2015
 */
public class LabeledPoint extends Point {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int label;

	public LabeledPoint(int x, int y, int label) {
		super(x, y);
		
		this.label = label;
	}

	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		this.label = label;
	}		
}
