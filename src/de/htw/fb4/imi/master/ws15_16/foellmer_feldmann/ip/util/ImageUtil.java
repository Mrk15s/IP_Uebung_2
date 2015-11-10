/**
 * Image Processing WiSe 2015/16
 *
 * Authors: Markus Föllmer, Sascha Feldmann
 */
package de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.util;

import java.util.HashMap;
import java.util.Map;

import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.ff.AbstractFloodFilling;

/**
 * Util class for image processing.
 *
 * @author Sascha Feldmann <sascha.feldmann@gmx.de>
 * @since 03.11.2015
 */
public class ImageUtil {
	
	protected static Map<Integer, Integer> labelColorMap;	
	
	public static final int FOREGROUND_VALUE = Colors.BLACK;
	
	protected static Map<Integer, Integer> getLabelColorMap() {
		if (null == labelColorMap) {
			labelColorMap = new HashMap<>();
			
			// define the mapping of labels and colors here
			labelColorMap.put(AbstractFloodFilling.NOT_LABELED, Colors.WHITE);
			labelColorMap.put(AbstractFloodFilling.START_LABEL, Colors.RED);
			labelColorMap.put(AbstractFloodFilling.START_LABEL + 1, Colors.BLUE);
			labelColorMap.put(AbstractFloodFilling.START_LABEL + 2, Colors.GREEN);
			labelColorMap.put(AbstractFloodFilling.START_LABEL + 3, Colors.YELLOW);
			labelColorMap.put(AbstractFloodFilling.START_LABEL + 4, Colors.ORANGE);
			labelColorMap.put(AbstractFloodFilling.START_LABEL + 5, Colors.LIGHT_BLUE); 
			labelColorMap.put(AbstractFloodFilling.START_LABEL + 6, Colors.LIGHT_GREEN);
			labelColorMap.put(AbstractFloodFilling.START_LABEL + 7, Colors.LIGHT_RED);
			labelColorMap.put(AbstractFloodFilling.START_LABEL + 8, Colors.GREY);
			labelColorMap.put(AbstractFloodFilling.START_LABEL + 9, Colors.VIOLETT);
		}
		
		return labelColorMap;
	}
	
	/**
	 * Map a label returned by the {@link AbstractFloodFilling} algorithm to a color to mark a detected image region.
	 * @param label
	 * @return
	 */
	public static int mapLabelToColor(int label) 
	{
		Map<Integer, Integer> colorMap = getLabelColorMap();
		
		if (colorMap.containsKey(label%9)) {
			return colorMap.get(label%9);
		} else {
			// no color for label number defined
			return Colors.BLACK;
		}
	}
	
	public static int calc1DPosition(int width, int x, int y) {
		int pos = y * width + x;
		return pos;
	}

	/**
	 * Use this method to get a 1-d array by a given 2d one.
	 * 
	 * @param width
	 * @param height
	 * @param pixels
	 * @return
	 */
	public static int[] getFlatArray(int width, int height, int[][] pixels) {
		int[] flat = new int[width * height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int pos = ImageUtil.calc1DPosition(width, i, j);
				flat[pos] = pixels[i][j];
			}
		}

		return flat;
	}

	/**
	 * 
	 * @param i
	 * @return
	 */
	public static boolean isForegoundPixel(int i) {
		return i == FOREGROUND_VALUE;
	}
}
