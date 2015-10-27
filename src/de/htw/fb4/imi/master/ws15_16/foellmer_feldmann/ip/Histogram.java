/**
 * Image Processing WiSe 2015/16
 *
 * Authors: Markus Föllmer, Sascha Feldmann
 */
package de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip;

import java.util.Arrays;

import de.htw.fb4.imi.master.ws15_16.jungk.Binarize;

/**
 * A histogram that currently allows to calculate and frequency of pixel values within an image
 * given as pixel array. 
 *
 * @author Sascha Feldmann <sascha.feldmann@gmx.de>
 * @since 20.10.2015
 */
public class Histogram {
	/**
	 * Maximum pixel value. The normal range is 0-255. So max value is 255.
	 */
	public static final int MAX_VALUE = 255;
	private int numberOfPixels;
	
	/**
	 * 
	 */
	private int[] absoluteFrequencies;
	private double[] relativeFrequencies;
	
	
	public int getNumberOfPixels() {
		return numberOfPixels;
	}
	
	/**
	 * Get number of pixels that have a frequency between min and maxValue.
	 * @param minValue
	 * @param maxValue
	 * @return
	 */
	public int getFrequencyCountBetween(int minValue, int maxValue) {
		int number = 0;
		for (int i = minValue; i <= maxValue; i++) {
			number += this.getAbsoluteFrequencies()[i];
		}
		
		return number;
	}	

	public int[] getAbsoluteFrequencies() {
		return absoluteFrequencies;
	}

	public double[] getRelativeFrequencies() {
		return relativeFrequencies;
	}

	/**
	 * Main method to start calculation.
	 * 
	 * Both the absolute and relative frequencies will be calculated:
	 * - relative: frequencies of pixel value divided by the number of all pixels (probability)
	 * 
	 * @param pixels
	 * @return this
	 */
	public Histogram calculate(int[] pixels)
	{
		reset(pixels);
		
		for (int i = 0; i < pixels.length; i++) {
			int greyValue = Binarize.calculateGrayValue(pixels[i]);
			this.absoluteFrequencies[greyValue]++;
			double relativeFrequency = (this.absoluteFrequencies[greyValue] * 100 / this.numberOfPixels);
			this.relativeFrequencies[greyValue] = relativeFrequency;
		}
		
		return this;
	}

	private void reset(int[] pixels) {
		this.numberOfPixels = pixels.length;
		// by spec, initial values will be set to 0
		int max = MAX_VALUE + 1;
		this.absoluteFrequencies = new int[max];
		this.relativeFrequencies = new double[max];
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(absoluteFrequencies);
		result = prime * result + numberOfPixels;
		result = prime * result + Arrays.hashCode(relativeFrequencies);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Histogram other = (Histogram) obj;
		if (!Arrays.equals(absoluteFrequencies, other.absoluteFrequencies))
			return false;
		if (numberOfPixels != other.numberOfPixels)
			return false;
		if (!Arrays.equals(relativeFrequencies, other.relativeFrequencies))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Histogram [getNumberOfPixels()=");
		builder.append(getNumberOfPixels());
		builder.append(", getAbsoluteFrequencies()=");
		builder.append(Arrays.toString(getAbsoluteFrequencies()));
		builder.append(", getRelativeFrequencies()=");
		builder.append(Arrays.toString(getRelativeFrequencies()));
		builder.append("]");
		return builder.toString();
	}	
}
