/**
 * Image Processing WiSe 2015/16
 *
 * Authors: Markus Föllmer, Sascha Feldmann
 */
package de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.ff;

import java.util.Comparator;

import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.util.LabeledPoint;

/**
 * [SHORT_DESCRIPTION] 
 *
 * @author Sascha Feldmann <sascha.feldmann@gmx.de>
 * @since 07.11.2015
 */
public class LabeledPointComparator implements Comparator<LabeledPoint> {

	@Override
	public int compare(LabeledPoint o1, LabeledPoint o2) {
		
		if (o1.getLabel() < o2.getLabel()) {
			return -1;
		}
		
		if (o1.getLabel() == o2.getLabel()) {
			return 0;
		}
		
		return 1;
	}

}
