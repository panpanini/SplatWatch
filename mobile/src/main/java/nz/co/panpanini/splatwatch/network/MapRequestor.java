package nz.co.panpanini.splatwatch.network;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;

import nz.co.panpanini.datalayer.models.Schedule;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import rx.Observable;
import rx.functions.Func0;

/**
 * Created by matthew <matthew@showgizmo.com> on 5/01/16.
 */
public class MapRequestor {

    private Retrofit retrofit;
    private SplatoonService service;

    public MapRequestor(){

        GsonBuilder builder = new GsonBuilder();

        builder.registerTypeAdapter(Schedule.class, new ScheduleTypeAdapter());

        retrofit = new Retrofit.Builder()
                .baseUrl("https://splatoon.ink")
                .addConverterFactory(GsonConverterFactory.create(builder.create()))
                .build();

        service = retrofit.create(SplatoonService.class);

    }

    public Observable<Schedule> getSchedule(){
        return Observable.defer(new Func0<Observable<Schedule>>() {
            @Override
            public Observable<Schedule> call() {
                return Observable.just(requestMaps());
            }
        });
    }


    private Schedule requestMaps() {
        try {
            JsonObject jObj = service.getSchedule().execute().body();

            return Schedule.fromJson(jObj);
        }catch(IOException ex){
            Log.e("MapRequestor", "requestMaps", ex);
            return null;
        }
    }

}
