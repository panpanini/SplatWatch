
package nz.co.panpanini.datalayer.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Mode implements Parcelable{


    private List<Map> maps = new ArrayList<Map>();

    private String rulesJP;

    private String rulesEN;

    private Mode() {
    }


    protected Mode(Parcel in) {
        maps = in.createTypedArrayList(Map.CREATOR);
        rulesJP = in.readString();
        rulesEN = in.readString();
    }

    public static final Creator<Mode> CREATOR = new Creator<Mode>() {
        @Override
        public Mode createFromParcel(Parcel in) {
            return new Mode(in);
        }

        @Override
        public Mode[] newArray(int size) {
            return new Mode[size];
        }
    };

    /**
     * 
     * @return
     *     The maps
     */
    public List<Map> getMaps() {
        return maps;
    }

    /**
     * 
     * @return
     *     The rulesJP
     */
    private String getRulesJP() {
        return rulesJP;
    }

    /**
     * 
     * @return
     *     The rulesEN
     */
    private String getRulesEN() {
        return rulesEN;
    }


    public String getRules(Context context){
        if (context.getResources().getConfiguration().locale.equals(Locale.JAPAN)){
            return getRulesJP();
        }

        return getRulesEN();
    }

    public static Mode fromJson(JsonObject json){
        Mode mode = new Mode();

        JsonArray maps = json.get("maps").getAsJsonArray();

        for (int i = 0; i < maps.size(); i++){
            mode.maps.add(Map.fromJson(maps.get(i).getAsJsonObject()));
        }

        mode.rulesEN = json.get("rules").getAsJsonObject().get("en").getAsString();
        mode.rulesJP = json.get("rules").getAsJsonObject().get("jp").getAsString();

        return mode;
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
        dest.writeTypedList(maps);
        dest.writeString(rulesJP);
        dest.writeString(rulesEN);
    }
}
