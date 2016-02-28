package nz.co.panpanini.datalayer.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;

/**
 * Created by matthew <matthew@showgizmo.com> on 7/01/16.
 */
public class Block implements Parcelable{

    /**
     * A Block consists of:
     * Start time - when the block of maps is in rotation
     * End time - when they're not
     * Regular - maps available in regular mode
     * Ranked - maps available in ranked mode
     */


    private long startTime;
    private long endTime;

    private Mode regular;
    private Mode ranked;



    private Block(){

    }

    protected Block(Parcel in) {
        startTime = in.readLong();
        endTime = in.readLong();
        regular = in.readParcelable(Mode.class.getClassLoader());
        ranked = in.readParcelable(Mode.class.getClassLoader());
    }

    public static final Creator<Block> CREATOR = new Creator<Block>() {
        @Override
        public Block createFromParcel(Parcel in) {
            return new Block(in);
        }

        @Override
        public Block[] newArray(int size) {
            return new Block[size];
        }
    };

    public long getStartTime(){
        return startTime;
    }

    public long getEndTime(){
        return endTime;
    }

    public Mode getRegularMaps(){
        return regular;
    }

    public Mode getRankedMaps(){
        return ranked;
    }

    public static Block fromJson(JsonObject json){
        Block block = new Block();

        block.startTime = json.get("startTime").getAsLong();
        block.endTime = json.get("endTime").getAsLong();

        block.regular = Mode.fromJson(json.get("regular").getAsJsonObject());
        if (json.get("ranked") != null) {
            block.ranked = Mode.fromJson(json.get("ranked").getAsJsonObject());
        }

        return block;
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
        dest.writeLong(getStartTime());
        dest.writeLong(getEndTime());
        dest.writeParcelable(getRegularMaps(), flags);
        dest.writeParcelable(getRankedMaps(), flags);
    }
}
