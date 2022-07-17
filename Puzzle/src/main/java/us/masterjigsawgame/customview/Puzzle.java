package us.masterjigsawgame.customview;

import android.os.Parcel;
import android.os.Parcelable;

public class Puzzle implements Parcelable {
	public int mColor;
	public int mNumber;
	
    public static final Creator CREATOR = new Creator() {
        public Puzzle createFromParcel(Parcel in) {
            return new Puzzle(in);
        }
        
        public Puzzle[] newArray(int size) {
            return new Puzzle[size];
        }
    };

    public Puzzle(int number, int color) {
		mNumber = number;
		mColor = color;
	}
	
    private Puzzle(Parcel in) {
        mColor = in.readInt();
        mNumber = in.readInt();
    }
    
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mColor);
        out.writeInt(mNumber);
    }
    
    public int describeContents() {
        return 0;
    }
}
