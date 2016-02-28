
package nz.co.panpanini.datalayer.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Schedule implements Parcelable {

    private boolean splatfest = false;

    private ArrayList<Block> blocks = new ArrayList<>();



    /**
     * No args constructor for use in serialization
     * 
     */
    public Schedule() {
    }

    public Schedule(Parcel in) {
        splatfest = in.readByte() != 0;
        blocks = in.createTypedArrayList(Block.CREATOR);
    }


    public static final Creator<Schedule> CREATOR = new Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel in) {
            return new Schedule(in);
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };

    public boolean isSplatfest(){
        return splatfest;
    }

    public List<Block> getBlocks(){
        return blocks;
    }


    public static Schedule fromJson(JsonObject json){
        Schedule s = new Schedule();

        s.splatfest = json.get("splatfest").getAsBoolean();

        JsonArray schedules = json.get("schedule").getAsJsonArray();

        for (int i = 0; i < schedules.size(); i++) {
            s.blocks.add(Block.fromJson(schedules.get(i).getAsJsonObject()));
        }

        return s;
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
        dest.writeByte((byte) (splatfest ? 1 : 0));
        dest.writeTypedList(blocks);
    }
}
