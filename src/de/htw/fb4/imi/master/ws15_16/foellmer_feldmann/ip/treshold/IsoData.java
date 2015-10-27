/**
 * Image Processing WiSe 2015/16
 *
 * Authors: Markus Föllmer, Sascha Feldmann
 */
package de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.treshold;

import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.Histogram;

/**
 * This class capsulates the IsoData main algorithm. It solves the task of
 * finding an appropriate threshold.
 *
 * @author Sascha Feldmann <sascha.feldmann@gmx.de>
 * @since 20.10.2015
 */
public class IsoData implements ThresholdFindingAlgorithm {

	public static final int DEFAULT_START_VALUE = 128;

	/**
	 * Start value (tO). Default is 128.
	 */
	private int startValue = DEFAULT_START_VALUE;

	private Histogram histogram;
	private int oldThreshold;
	
	protected int counter = 0;

	public int getStartValue() {
		return startValue;
	}

	public void setStartValue(int startValue) {
		this.startValue = startValue;
	}

	@Override
	public int calculateThreshold(int[] pixels) {
		this.createHistogram(pixels);

		this.oldThreshold = Integer.MAX_VALUE;
		return this.runIsoData(this.getStartValue());
	}

	private int runIsoData(int t) {
		if (!this.thresholdIsGoodEnough(t)) {
			this.counter++; 
			
			int n0 = this.histogram.getFrequencyCountBetween(0, t - 1);
			int n1 = this.histogram.getFrequencyCountBetween(t, Histogram.MAX_VALUE);
			
			if (n0 == 0 || n1 == 0) {
				return -1;
			}
			
			int Ma = this.calculateMean(0, t - 1);
			int Mb = this.calculateMean(t, Histogram.MAX_VALUE);
		
			int newThreshold = (Ma + Mb) / 2;
			this.oldThreshold = t;

			return runIsoData(newThreshold);
		} else {
			System.out.println("# Iterations: " + this.counter);
			return t;
		}
	}

	private int calculateMean(int minValue, int maxValue) {
		int count = this.histogram.getFrequencyCountBetween(minValue, maxValue);
		
		int relativeCount = 0;
		for (int i = minValue; i <= maxValue; i++) {
			relativeCount += i * this.histogram.getAbsoluteFrequencies()[i];
		}
		
		return relativeCount / count;
	}

	private boolean thresholdIsGoodEnough(final int newThreshold) {
		if (newThreshold == this.oldThreshold) {
			return true;
		} else {
			return false;
		}
	}

	private void createHistogram(int[] pixels) {
		this.histogram = new Histogram();
		this.histogram.calculate(pixels);

		System.out.println("Histogram was created: " + this.histogram);
	}
}
