package nz.co.panpanini.splatwatch.network;

import android.util.Log;

import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import nz.co.panpanini.datalayer.models.Schedule;

/**
 * Created by matthew <matthew@showgizmo.com> on 24/02/16.
 */
public class ScheduleTypeAdapter extends TypeAdapter<Schedule> {
    /**
     * Writes one JSON value (an array, object, string, number, boolean or null)
     * for {@code value}.
     *
     * @param out
     * @param value the Java object to write. May be null.
     */
    @Override
    public void write(JsonWriter out, Schedule value) throws IOException {
        // No-Op
    }

    /**
     * Reads one JSON value (an array, object, string, number, boolean or null)
     * and converts it to a Java object. Returns the converted object.
     *
     * @param in
     * @return the converted Java object. May be null.
     */
    @Override
    public Schedule read(JsonReader in) throws IOException {
        Log.d("ScheduleTypeAdapter", "in: " + in.toString());
        JsonParser parser = new JsonParser();
        return Schedule.fromJson(parser.parse(in.toString()).getAsJsonObject());
    }
}
