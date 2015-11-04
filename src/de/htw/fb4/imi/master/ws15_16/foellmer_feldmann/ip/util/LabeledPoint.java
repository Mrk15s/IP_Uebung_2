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
	private int label = 0;

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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LabeledPoint [getLabel()=");
		builder.append(getLabel());
		builder.append(", getX()=");
		builder.append(getX());
		builder.append(", getY()=");
		builder.append(getY());
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + label;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		LabeledPoint other = (LabeledPoint) obj;
		if (label != other.label)
			return false;
		return true;
	}			
}
