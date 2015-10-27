/**
 * Image Processing WiSe 2015/16
 *
 * Authors: Markus Föllmer, Sascha Feldmann
 */
package de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.treshold;

import javax.swing.JSlider;

/**
 * [SHORT_DESCRIPTION] 
 *
 * @author Sascha Feldmann <sascha.feldmann@gmx.de>
 * @since 20.10.2015
 */
public class ThresholdUserInput implements ThresholdFindingAlgorithm {
	private JSlider thresholdInput;
	
	public JSlider getThresholdInput() {
		return thresholdInput;
	}

	public void setThresholdInput(JSlider thresholdInput) {
		this.thresholdInput = thresholdInput;
	}	

	/* (non-Javadoc)
	 * @see de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.ThresholdFindingAlgorithm#calculateThreshold()
	 */
	@Override
	public int calculateThreshold(int[] pixels) {
		this.ensureThatInputComponentWasSet();
		
		return this.thresholdInput.getValue();
	}

	private void ensureThatInputComponentWasSet() {
		if (null == this.thresholdInput) {
			throw new IllegalStateException("ThresholdInput wasn't set. Please set it before calling calculateThreshold().");
		}		
	}

}
