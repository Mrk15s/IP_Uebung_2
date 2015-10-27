/**
 * Image Processing WiSe 2015/16
 *
 * Authors: Markus Föllmer, Sascha Feldmann
 */
package de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip;

import javax.swing.JSlider;

import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.outline.Outline;
import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.treshold.IsoData;
import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.treshold.ThresholdFindingAlgorithm;
import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.treshold.ThresholdUserInput;

/**
 * Central factory.
 *
 * @author Sascha Feldmann <sascha.feldmann@gmx.de>
 * @since 20.10.2015
 */
public class Factory {
	
	public static ThresholdFindingAlgorithm newThresholdUserInput(JSlider slider)
	{
		ThresholdUserInput algorithm = new ThresholdUserInput();
		
		algorithm.setThresholdInput(slider);
		
		return algorithm;
	}
	
	public static ThresholdFindingAlgorithm newIsoDataAlgorithm(int startValue)
	{
		IsoData algorithm = new IsoData();
		
		algorithm.setStartValue(startValue);
		
		return algorithm;
	}
	
	public static Outline newOutlineAlgorithm()
	{
		return new Outline();
	}

}
