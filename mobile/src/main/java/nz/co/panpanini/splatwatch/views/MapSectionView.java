package nz.co.panpanini.splatwatch.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.Bind;
import nz.co.panpanini.splatwatch.R;

/**
 * Created by matthew <matthew@showgizmo.com> on 18/01/16.
 */
public class MapSectionView extends LinearLayout {

    @Bind(R.id.title)
    protected TextView title;

    @Bind(R.id.map_1)
    protected TextView map1;
    @Bind(R.id.map_2)
    protected TextView map2;
    @Bind(R.id.map_3)
    protected TextView map3;

    public MapSectionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.map_section, this);

        ButterKnife.bind(this);
    }

    public MapSectionView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }


    public MapSectionView(Context context) {
        this(context, null);

    }



    public void setTitle(String title){
        this.title.setText(title);
    }


    public void setMap1Text(String text){
        this.map1.setText(text);
        if (text == null || text.equals("")){
            this.map1.setVisibility(View.GONE);
        }else{
            this.map1.setVisibility(View.VISIBLE);
        }
    }

    public void setMap2Text(String text){
        this.map2.setText(text);
        if (text == null || text.equals("")){
            this.map2.setVisibility(View.GONE);
        }else{
            this.map2.setVisibility(View.VISIBLE);
        }
    }

    public void setMap3Text(String text){
        this.map3.setText(text);
        if (text == null || text.equals("")){
            this.map3.setVisibility(View.GONE);
        }else{
            this.map3.setVisibility(View.VISIBLE);
        }
    }

}
