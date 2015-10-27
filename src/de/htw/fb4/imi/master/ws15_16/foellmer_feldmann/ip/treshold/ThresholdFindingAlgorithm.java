package de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.treshold;

/**
 * 
 * @author Sascha Feldmann <sascha.feldmann@gmx.de>
 * @since 20.10.2015
 */
public interface ThresholdFindingAlgorithm {
	
	/**
	 * Solve the task of finding an appropriate threshold, e.g. for binarization tasks.
	 * @param pixels int[] array of pixel values
	 * @return
	 * @throws IllegalStateException if certain tasks were not done before triggering
	 */
	int calculateThreshold(int[] pixels) throws IllegalStateException;
}
