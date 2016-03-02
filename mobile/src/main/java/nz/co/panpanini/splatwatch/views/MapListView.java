package nz.co.panpanini.splatwatch.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.Bind;
import nz.co.panpanini.splatwatch.R;

/**
 * Created by matthew <matthew@showgizmo.com> on 5/01/16.
 */
public class MapListView extends LinearLayout {

    @Bind(R.id.name)
    protected TextView nameTextView;

    public MapListView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.map_list_view, this);

        ButterKnife.bind(this);
    }

    public void setName(String name){
        this.nameTextView.setText(name);
    }


}
