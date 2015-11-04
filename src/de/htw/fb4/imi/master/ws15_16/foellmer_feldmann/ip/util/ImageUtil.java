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
			
			labelColorMap.put(AbstractFloodFilling.NOT_LABELED, Colors.WHITE); // red
			labelColorMap.put(1, Colors.RED); // red
			labelColorMap.put(2, Colors.BLUE); // blue
			labelColorMap.put(3, Colors.GREEN); // green
			labelColorMap.put(4, Colors.YELLOW); // yellow
			labelColorMap.put(5, Colors.ORANGE); // orange
			labelColorMap.put(5, Colors.BLACK); // black
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
		
		if (colorMap.containsKey(label)) {
			return colorMap.get(label);
		} else {
			return 0xff000000;
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
