/**
 * Image Processing WiSe 2015/16
 *
 * Authors: Markus Föllmer, Sascha Feldmann
 */
package de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.ff;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import de.htw.fb4.imi.master.ws15_16.foellmer_feldmann.ip.util.LabeledPoint;

/**
 * Implementation of flood filling.
 *
 * @since 27.10.2015
 */
public class Sequential extends AbstractFloodFilling {

	private int lastExactlyOneNeighboursLabel;
	private TreeMap<LabeledPoint, TreeSet<LabeledPoint>> collisions;
	private LabeledPoint lastMoreThanOneNeighbourSmallestPoint;
	private TreeSet<Integer> freeLabels;
	private TreeMap<Integer, Integer> newLabelMap;

	public int[][] execute() {
		super.execute();

		assignInitialLabels();
		resolveLabelCollisions();
		relabelImage();

		return this.labeledPixels;
	}

	private void assignInitialLabels() {
		int label = START_LABEL;
		this.collisions = new TreeMap<>(new LabeledPointComparator());

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
		
		// map to keep collision labels that need to be replaced later
		this.newLabelMap = new TreeMap<Integer, Integer>();
		for (LabeledPoint collisionPoint : this.collisions.keySet()) {
			Set<LabeledPoint> collisionNeighbours = this.collisions.get(collisionPoint);
			
			for (LabeledPoint collisionNeighbour : collisionNeighbours) {				
				if (!this.newLabelMap.containsKey(collisionNeighbour.getLabel())) {
					this.newLabelMap.put(collisionNeighbour.getLabel(), collisionPoint.getLabel());
					this.freeLabels.add(collisionNeighbour.getLabel());
				}
			}
		}		
		
		// replace labels in map
		this.replaceAllLabels();
	}
	
	private void replaceAllLabels() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int oldLabel = this.labeledPixels[x][y];
				while (this.newLabelMap.containsKey(oldLabel)) {
					// be transitive: walk through label replacement chain
					Integer newLabel = this.newLabelMap.get(oldLabel);
					this.labeledPixels[x][y] = newLabel;
					oldLabel = newLabel;
				}
			}
		}		
	}
	
	private void relabelImage() {
		int nextFreeLabelIndex = 0;
		this.newLabelMap = new TreeMap<>();		
		
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
			this.collisions.put(lastMoreThanOneNeighbourSmallestPoint, new TreeSet<LabeledPoint>(new LabeledPointComparator()));
		}

		this.collisions.get(lastMoreThanOneNeighbourSmallestPoint).add(neighbour);
	}
}
