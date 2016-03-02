package nz.co.panpanini.splatwatch.views;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import nz.co.panpanini.datalayer.models.Block;
import nz.co.panpanini.splatwatch.R;

/**
 * Created by matthew <matthew@showgizmo.com> on 12/01/16.
 */
public class BlockListView extends LinearLayout {

    @Bind(R.id.block_title)
    protected TextView blockTitle;

    @Bind(R.id.regular_map_section)
    protected MapSectionView regularSection;
    @Bind(R.id.ranked_map_section)
    protected MapSectionView rankedSection;

    public BlockListView(Context context) {
        super(context);

        inflate(context, R.layout.block_list_view, this);

        ButterKnife.bind(this);

    }


    public void setBlock(Block block){



        blockTitle.setText(getFormattedDate(new Date(block.getStartTime())));

        regularSection.setTitle("Regular - " + block.getRegularMaps().getRulesEN());
        regularSection.setMap1Text(block.getRegularMaps().getMaps().get(0).getNameEN());
        regularSection.setMap2Text(block.getRegularMaps().getMaps().get(1).getNameEN());
        if (block.getRegularMaps().getMaps().size() > 2) {
            regularSection.setMap3Text(block.getRegularMaps().getMaps().get(2).getNameEN());
        }else {
            regularSection.setMap3Text("");
        }

        if (block.getRankedMaps() != null) {
            rankedSection.setTitle("Ranked - " + block.getRankedMaps().getRulesEN());
            rankedSection.setMap1Text(block.getRankedMaps().getMaps().get(0).getNameEN());
            rankedSection.setMap2Text(block.getRankedMaps().getMaps().get(1).getNameEN());
            rankedSection.setMap3Text("");
            rankedSection.setVisibility(View.VISIBLE);
        }else{
            rankedSection.setVisibility(View.GONE);
        }
    }

    String getFormattedDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //2nd of march 2015
        int day = cal.get(Calendar.DATE);

        switch (day % 10) {
            case 1:
                return new SimpleDateFormat("EEE d'st' - HH:MM").format(date);
            case 2:
                return new SimpleDateFormat("EEE d'nd' - HH:MM").format(date);
            case 3:
                return new SimpleDateFormat("EEE d'rd' - HH:MM").format(date);
            default:
                return new SimpleDateFormat("EEE d'th' - HH:MM").format(date);
        }
    }
}
