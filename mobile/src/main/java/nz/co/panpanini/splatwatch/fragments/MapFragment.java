package nz.co.panpanini.splatwatch.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;

import nz.co.panpanini.datalayer.models.Schedule;
import nz.co.panpanini.splatwatch.R;
import nz.co.panpanini.splatwatch.network.MapRequestor;
import nz.co.panpanini.splatwatch.views.ScheduleAdapter;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by matthew <matthew@showgizmo.com> on 5/01/16.
 */
public class MapFragment extends ListFragment {

    private ScheduleAdapter adapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new ScheduleAdapter();

        this.setListAdapter(adapter);

        getListView().setDivider(null);
        getListView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));


    }


    @Override
    public void onResume() {
        super.onResume();

        requestObservableMap();

    }

    private void requestObservableMap(){
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
        adapter.setSchedule(schedule);
    }


}
