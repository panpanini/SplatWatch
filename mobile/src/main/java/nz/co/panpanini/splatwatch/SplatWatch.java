package nz.co.panpanini.splatwatch;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;

import me.denley.courier.Courier;
import me.denley.courier.ReceiveMessages;
import nz.co.panpanini.datalayer.models.Map;
import nz.co.panpanini.datalayer.models.Schedule;
import nz.co.panpanini.splatwatch.network.MapRequestor;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by matthew <matthew@showgizmo.com> on 24/01/16.
 */
public class SplatWatch extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        Courier.startReceiving(this, this);
        Log.e("SplatWatch", "started listening");

    }

    @ReceiveMessages("/request_update")
    public void requestUpdate(String message, String nodeId){
        Log.e("SplatWatch", "update requested");
        new MapRequestor().getSchedule()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Schedule>() {
                    @Override
                    public void call(Schedule schedule) {
                        emitScheduleResult(schedule);
                    }
                });
    }

    private void emitScheduleResult(Schedule schedule){

        ArrayList strings = new ArrayList();

        strings.add(getString(R.string.regular_title));

        for (Map map : schedule.getBlocks().get(0).getRegularMaps().getMaps()){
            strings.add(map.getName(this));
        }

        Courier.deliverData(this, "/regular_maps", strings);

        strings = new ArrayList();

        if (schedule.getBlocks().get(0).getRankedMaps() != null){
            strings.add(getString(R.string.ranked_title) + ": " + schedule.getBlocks().get(0).getRankedMaps().getRules(this));

            for (Map map : schedule.getBlocks().get(0).getRankedMaps().getMaps()){
                strings.add(map.getName(this));
            }
        }

        Courier.deliverData(this, "/ranked_maps", strings);

    }
}
