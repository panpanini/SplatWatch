package nz.co.panpanini.splatwatch.network;

import com.google.gson.JsonObject;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by matthew <matthew@showgizmo.com> on 5/01/16.
 */
public interface SplatoonService {

    @GET("schedule.json")
    Call<JsonObject> getSchedule();


}
