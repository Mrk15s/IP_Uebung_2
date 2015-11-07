/**
 * Image Processing WiSe 2015/16
 *
 * Authors: Markus Föllmer, Sascha Feldmann
 */
package de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.ff;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

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
	private TreeSet<Integer> freeLabels;
	private HashMap<Integer, Integer> newLabelMap;

	public int[][] execute() {
		super.execute();

		assignInitialLabels();
		resolveLabelCollisions();
		relabelImage();

		return this.labeledPixels;
	}

	private void assignInitialLabels() {
		int label = START_LABEL;
		this.collisions = new HashMap<>();

		// walk through image pixels in scan-line order, execute
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
					if (this.isForegroundPixel(x, y)) {
						Set<LabeledPoint> neighbours = getNeighbours(x, y);
	
						if (allNeighboursAreBackgroundPixelsAndNotLabelledYet(neighbours)) {
							this.labelPixel(x, y, label);
							label++;
						} else if (exactlyOneNeighbourLable(neighbours)) {
							this.labelPixel(x, y, this.lastExactlyOneNeighboursLabel);
						} else if (moreThanOneNeighbourIsLabled(neighbours)) {
							this.labelPixel(x, y, this.lastMoreThanOneNeighbourSmallestPoint.getLabel());
	
							this.registerCollision(this.lastMoreThanOneNeighbourSmallestPoint, neighbours);
						}
					}
				}
		}		
	}

	private void resolveLabelCollisions() {
		this.freeLabels = new TreeSet<>();
		
		for (LabeledPoint collisionPoint : this.collisions.keySet()) {
			Set<LabeledPoint> collisionNeighbours = this.collisions.get(collisionPoint);
			
			for (LabeledPoint collisionNeighbour : collisionNeighbours) {
				//this.labeledPixels[(int) collisionNeighbour.getX()][(int) collisionNeighbour.getY()] = collisionPoint.getLabel();
				this.replaceAllLabels(collisionNeighbour.getLabel(), collisionPoint.getLabel());
			}
		}				
	}
	
	private void relabelImage() {
		int nextFreeLabelIndex = 0;
		this.newLabelMap = new HashMap<>();		
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int currentLabel = this.labeledPixels[x][y];
				if (NOT_LABELED != currentLabel
						&& !this.freeLabels.contains(currentLabel)) {
					
					if (!newLabelMap.containsKey(currentLabel)
							&& currentLabel > (int) this.freeLabels.toArray()[nextFreeLabelIndex]) {
						// assign the current label one of the free ones, starting from the smallest one
						newLabelMap.put(currentLabel, (int) this.freeLabels.toArray()[nextFreeLabelIndex]);
						nextFreeLabelIndex++;
					}
					
					if (newLabelMap.containsKey(currentLabel)) {
						this.labeledPixels[x][y] = newLabelMap.get(currentLabel);
					}
				}
			}
		}
	}
	
	private void replaceAllLabels(int label, int byLabel) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (label == this.labeledPixels[x][y]) {
					this.labeledPixels[x][y] = byLabel;
				}
			}
		}		
		
		this.freeLabels.add(label);
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

	private boolean allNeighboursAreBackgroundPixelsAndNotLabelledYet(Set<LabeledPoint> neighbours) {
		for (LabeledPoint neighbour : neighbours) {
			if (this.isForegroundPixel((int) neighbour.getX(), (int) neighbour.getY())
					&& this.isLabeled((int) neighbour.getX(), (int) neighbour.getY())) {
				return false;
			}
		}

		return true;
	}


	private boolean exactlyOneNeighbourLable(Set<LabeledPoint> neighbours) {
		Set<Integer> neighbourLabels = new HashSet<>();
	
		for (LabeledPoint neighbour : neighbours) {
			if (neighbour.getLabel() >= START_LABEL
					&& !neighbourLabels.contains(neighbour.getLabel())) {
				neighbourLabels.add(neighbour.getLabel());
			}
		}

		boolean hasExactlyOneLabelInNeighbourhood = 1 == neighbourLabels.size();

		if (hasExactlyOneLabelInNeighbourhood) {
			this.lastExactlyOneNeighboursLabel = (int) neighbourLabels.toArray()[0];
		}

		return hasExactlyOneLabelInNeighbourhood;
	}

	private boolean moreThanOneNeighbourIsLabled(Set<LabeledPoint> neighbours) {
		Set<LabeledPoint> labeledNeighbours = new TreeSet<>(new LabeledPointComparator());
		
		for (LabeledPoint neighbour : neighbours) {
			if (neighbour.getLabel() >= START_LABEL
					&& !labeledNeighbours.contains(neighbour)) {
				labeledNeighbours.add(neighbour);
			}
		}

		boolean moreThanOneLabelInNeighbourhood = 1 < labeledNeighbours.size();

		if (moreThanOneLabelInNeighbourhood) {
			this.lastMoreThanOneNeighbourSmallestPoint = (LabeledPoint) labeledNeighbours.toArray()[0];
		}

		return moreThanOneLabelInNeighbourhood;
	}

	private void registerCollision(LabeledPoint lastMoreThanOneNeighbourSmallestPoint, Set<LabeledPoint> neighbours) {
		for (LabeledPoint neighbour : neighbours) {
			if (lastMoreThanOneNeighbourSmallestPoint.getLabel() == neighbour.getLabel()) {
				continue;
			}

			if (neighbour.getLabel() >= START_LABEL) {
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
