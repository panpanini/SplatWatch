package nz.co.panpanini.datalayer.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by matthew <matthew@showgizmo.com> on 17/02/16.
 */
public class ColourSet implements Parcelable {

    private int primaryColour;
    private int secondaryColour;
    private int tertiaryColour;


    public ColourSet(int primary, int secondary, int tertiary){
        this.primaryColour = primary;
        this.secondaryColour = secondary;
        this.tertiaryColour = tertiary;
    }

    protected ColourSet(Parcel in) {
        primaryColour = in.readInt();
        secondaryColour = in.readInt();
        tertiaryColour = in.readInt();
    }

    public static final Creator<ColourSet> CREATOR = new Creator<ColourSet>() {
        @Override
        public ColourSet createFromParcel(Parcel in) {
            return new ColourSet(in);
        }

        @Override
        public ColourSet[] newArray(int size) {
            return new ColourSet[size];
        }
    };

    public int getPrimaryColour() {
        return primaryColour;
    }

    public void setPrimaryColour(int primaryColour) {
        this.primaryColour = primaryColour;
    }

    public int getSecondaryColour() {
        return secondaryColour;
    }

    public void setSecondaryColour(int secondaryColour) {
        this.secondaryColour = secondaryColour;
    }

    public int getTertiaryColour() {
        return tertiaryColour;
    }

    public void setTertiaryColour(int tertiaryColour) {
        this.tertiaryColour = tertiaryColour;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(primaryColour);
        dest.writeInt(secondaryColour);
        dest.writeInt(tertiaryColour);
    }
}
