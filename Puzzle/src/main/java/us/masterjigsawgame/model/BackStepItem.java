
package us.masterjigsawgame.model;

import java.io.Serializable;

import us.masterjigsawgame.customview.Puzzle;


public class BackStepItem implements Serializable {

	private int misplaced;
	private int emptyIndex;
	private Puzzle[] puzzles;

	public int getMisplaced() {
		return misplaced;
	}

	public void setMisplaced(int misplaced) {
		this.misplaced = misplaced;
	}

	public int getEmptyIndex() {
		return emptyIndex;
	}

	public void setEmptyIndex(int emptyIndex) {
		this.emptyIndex = emptyIndex;
	}

	public Puzzle[] getPuzzles() {
		return puzzles;
	}

	public void setPuzzles(Puzzle[] puzzles) {
		this.puzzles = puzzles;
	}

}
