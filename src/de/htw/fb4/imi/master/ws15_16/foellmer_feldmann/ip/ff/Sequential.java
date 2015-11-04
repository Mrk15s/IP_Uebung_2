/**
 * Image Processing WiSe 2015/16
 *
 * Authors: Markus Föllmer, Sascha Feldmann
 */
package de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.ff;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.util.ImageUtil;
import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.util.LabeledPoint;

/**
 * Implementation of flood filling.
 *
 * @since 27.10.2015
 */
public class Sequential extends AbstractFloodFilling {

	private int lastExactlyOneNeighboursLabel;
	private HashMap<LabeledPoint, Set<LabeledPoint>> collisions;
	private LabeledPoint lastMoreThanOneNeighbourSmallestPoint;

	/**
	 * Set the original pixels by an 1-dimensional pixel array.
	 * 
	 * Internally, the pixels are stored as 2-dimensional array. For conversion,
	 * hand in widht and height.
	 * 
	 * @param width
	 * @param height
	 * @param originalPixels
	 */
	// public void setOriginalBinaryPixels(int width, int height, int[]
	// originalPixels) {
	// this.width = width;
	// this.height = height;
	// this.originalBinaryPixels = new int[width][height];
	// this.labeledPixels = new int[width][height];
	//
	// for (int x = 0; x < width; x++) {
	// for (int y = 0; y < height; y++) {
	// int pos = ImageUtil.calc1DPosition(width, x, y);
	//
	// if (ImageUtil.isForegoundPixel(originalPixels[pos])) {
	// this.originalBinaryPixels[x][y] = 1;
	// this.labeledPixels[x][y] = START_LABEL;
	// } else {
	// this.originalBinaryPixels[x][y] = 0;
	// this.labeledPixels[x][y] = NOT_LABELED; // in the beginning,
	// // all pixels are
	// // not labeled
	// }
	// }
	// }
	// }

	public int[][] execute() {
		super.execute();

		executeFloodFillingInScanLineOrder();

		return this.labeledPixels;
	}

	private void executeFloodFillingInScanLineOrder() {
		int label = START_LABEL;
		this.collisions = new HashMap<>();

		// walk through image pixels in scan-line order, execute
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (this.isForegroundPixel(x, y)) {
					Set<LabeledPoint> neighbours = getNeighbours(x, y);

					if (allNeighboursAreNotLabled(neighbours)) {
						this.labelPixel(x, y, label);
						label++;
					} else if (exactlyOneNeighbourIsLabled(neighbours)) {
						this.labelPixel(x, y, this.lastExactlyOneNeighboursLabel);
					} else if (moreThanOneNeighbourIsLabled(neighbours)) {
						this.labelPixel(x, y, this.lastMoreThanOneNeighbourSmallestPoint.getLabel());

						this.registerCollision(this.lastMoreThanOneNeighbourSmallestPoint, neighbours);
					}
				}
			}
		}

		System.out.println("Bla");
	}

	private Set<LabeledPoint> getNeighbours(int x, int y) {
		Set<LabeledPoint> neighbours = new HashSet<>();

		switch (mode) {
		case NEIGHBOURS4:
			get4Neighbours(neighbours, x, y);
			break;
		case NEIGHBOURS8:
			get8Neighbours(neighbours, x, y);
			break;
		}

		return neighbours;
	}

	private void get4Neighbours(Set<LabeledPoint> neighbours, int x, int y) {
		addToSet(neighbours, x + 1, y);
		addToSet(neighbours, x, y - 1);
		addToSet(neighbours, x - 1, y);
		addToSet(neighbours, x, y + 1);
	}

	private void get8Neighbours(Set<LabeledPoint> neighbours, int x, int y) {
		addToSet(neighbours, x + 1, y);
		addToSet(neighbours, x + 1, y - 1);
		addToSet(neighbours, x, y - 1);
		addToSet(neighbours, x - 1, y - 1);
		addToSet(neighbours, x - 1, y);
		addToSet(neighbours, x - 1, y + 1);
		addToSet(neighbours, x, y + 1);
		addToSet(neighbours, x + 1, y + 1);
	}

	private void addToSet(Set<LabeledPoint> neighbours, int x, int y) {
		if (isWithinImageBoundaries(x, y)) {
			neighbours.add(new LabeledPoint(x, y, this.labeledPixels[x][y]));
		}
	}

	private boolean allNeighboursAreNotLabled(Set<LabeledPoint> neighbours) {
		for (LabeledPoint neighbour : neighbours) {
			if (this.isForegroundPixel((int) neighbour.getX(), (int) neighbour.getY())) {
				return false;
			}
		}

		return true;
	}

	private boolean exactlyOneNeighbourIsLabled(Set<LabeledPoint> neighbours) {
		int labeledNeighbourCount = 0;
		int lastLabel = 0;

		for (LabeledPoint neighbour : neighbours) {
			if (neighbour.getLabel() >= START_LABEL) {
				labeledNeighbourCount++;
				lastLabel = neighbour.getLabel();
			}
		}

		boolean isExactlyOneNeighbour = 1 == labeledNeighbourCount;

		if (isExactlyOneNeighbour) {
			this.lastExactlyOneNeighboursLabel = lastLabel;
		}

		return isExactlyOneNeighbour;
	}

	private boolean moreThanOneNeighbourIsLabled(Set<LabeledPoint> neighbours) {
		int labeledNeighbourCount = 0;
		LabeledPoint smallestLabel = null;

		for (LabeledPoint neighbour : neighbours) {
			if (neighbour.getLabel() >= START_LABEL) {
				labeledNeighbourCount++;

				if (null == smallestLabel || neighbour.getLabel() < smallestLabel.getLabel()) {
					smallestLabel = neighbour;
				}
			}
		}

		boolean moreThanOneNeighbourCount = 1 < labeledNeighbourCount;

		if (moreThanOneNeighbourCount) {
			this.lastMoreThanOneNeighbourSmallestPoint = smallestLabel;
		}

		return moreThanOneNeighbourCount;
	}

	private void registerCollision(LabeledPoint lastMoreThanOneNeighbourSmallestPoint, Set<LabeledPoint> neighbours) {
		for (LabeledPoint neighbour : neighbours) {
			if (lastMoreThanOneNeighbourSmallestPoint.equals(neighbour)) {
				continue;
			}

			if (neighbour.getLabel() > START_LABEL) {
				this.addCollision(lastMoreThanOneNeighbourSmallestPoint, neighbour);
			}
		}
	}

	private void addCollision(LabeledPoint lastMoreThanOneNeighbourSmallestPoint, LabeledPoint neighbour) {
		if (!this.collisions.containsKey(lastMoreThanOneNeighbourSmallestPoint)) {
			this.collisions.put(lastMoreThanOneNeighbourSmallestPoint, new HashSet<LabeledPoint>());
		}

		this.collisions.get(lastMoreThanOneNeighbourSmallestPoint).add(neighbour);
	}
}
