/**
 * Image Processing WiSe 2015/16
 *
 * Authors: Markus Föllmer, Sascha Feldmann
 */
package de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.ff;

/**
 * Implementation of flood filling.
 *
 * @since 27.10.2015
 */
public class Sequential extends AbstractFloodFilling {
	
	public int[][] execute()
	{
		super.execute();
		
		executeFloodFillingInScanLineOrder();
		
		return this.labeledPixels; 
	}

	private void executeFloodFillingInScanLineOrder() {
		int label = START_LABEL;
		
		// walk through image pixels in scan-line order, execute depth first on unlabeled foreground pixels
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (1 == this.originalBinaryPixels[x][y]
						&& NOT_LABELED == this.labeledPixels[x][y]) {
					this.regionLabeling();
					
					label++;
				}
			}
		}
	}
	
	/**
	 * Sequentielle Regionenmarkierung
	 */
	public void regionLabeling() {
	}
}
