
package nz.co.panpanini.datalayer.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;

public class Map implements Parcelable{


    private String nameJP;

    private String nameEN;


    /**
     * No args constructor for use in serialization
     * 
     */
    private Map() {
    }


    protected Map(Parcel in) {
        nameJP = in.readString();
        nameEN = in.readString();
    }

    public static final Creator<Map> CREATOR = new Creator<Map>() {
        @Override
        public Map createFromParcel(Parcel in) {
            return new Map(in);
        }

        @Override
        public Map[] newArray(int size) {
            return new Map[size];
        }
    };

    /**
     * 
     * @return
     *     The nameJP
     */
    public String getNameJP() {
        return nameJP;
    }


    /**
     * 
     * @return
     *     The nameEN
     */
    public String getNameEN() {
        return nameEN;
    }



    public static Map fromJson(JsonObject json){
        Map map = new Map();

        map.nameEN = json.get("nameEN").getAsString();
        map.nameJP = json.get("nameJP").getAsString();


        return map;
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
        dest.writeString(nameJP);
        dest.writeString(nameEN);
    }
}
