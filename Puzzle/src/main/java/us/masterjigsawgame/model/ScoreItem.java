
package us.masterjigsawgame.model;

import java.io.Serializable;


public class ScoreItem implements Serializable {

	private String time;
	private String seconds;
	private String level;
	private int position;
	private String moves;
	private String hinttaken;

	public String getMoves() {
		return moves;
	}


	public void setMoves(String moves) {
		this.moves = moves;
	}

	public String getHinttaken() {
		return hinttaken;
	}

	public void setHinttaken(String hinttaken) {
		this.hinttaken = hinttaken;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSeconds() {
		return seconds;
	}

	public void setSeconds(String seconds) {
		this.seconds = seconds;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

}
