/**
 * Image Processing WiSe 2015/16
 *
 * Authors: Markus Föllmer, Sascha Feldmann
 */
package de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip;

import java.util.Observer;

import javax.swing.JSlider;

import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.ff.AbstractFloodFilling;
import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.ff.BreadthFirst;
import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.ff.DepthFirst;
import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.ff.OptimizedBreadthFirst;
import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.ff.OptimizedDepthFirst;
import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.ff.Sequential;
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
	
	public static AbstractFloodFilling newDepthFirst(Observer observer) {
		AbstractFloodFilling algorithm = newDepthFirst();
		algorithm.addObserver(observer);		
		
		return algorithm;
	}
	
	public static AbstractFloodFilling newDepthFirst()
	{
		return new DepthFirst();
	}
	
	public static AbstractFloodFilling newOptimizedDepthFirst(Observer observer) {
		AbstractFloodFilling algorithm = newOptimizedDepthFirst();
		algorithm.addObserver(observer);		
		
		return algorithm;
	}
	
	public static AbstractFloodFilling newOptimizedDepthFirst()
	{
		return new OptimizedDepthFirst();
	}

	public static AbstractFloodFilling newBreadthFirst()
	{
		return new BreadthFirst();
	}	
	
	public static AbstractFloodFilling newBreadthFirst(Observer observer) {
		AbstractFloodFilling algorithm = newBreadthFirst();
		algorithm.addObserver(observer);		
		
		return algorithm;
	}
	
	public static AbstractFloodFilling newOptimizedBreadthFirst()
	{
		return new OptimizedBreadthFirst();
	}	
	
	public static AbstractFloodFilling newOptimizedBreadthFirst(Observer observer) {
		AbstractFloodFilling algorithm = newOptimizedBreadthFirst();
		algorithm.addObserver(observer);		
		
		return algorithm;
	}
	
	public static AbstractFloodFilling newSequential()
	{
		return new Sequential();
	}

	public static AbstractFloodFilling newSequential(Observer observer) {
		AbstractFloodFilling algorithm = newSequential();
		algorithm.addObserver(observer);		
		
		return algorithm;
	}	
}
