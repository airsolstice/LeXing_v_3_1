package com.hs.lxv3_1.navi;

import android.content.Context;

import com.hs.lxv3_1.R;
import com.hs.lxv3_1.utils.CommonAdapter;
import com.hs.lxv3_1.utils.ViewHolder;

import java.util.List;

/**
 * Created by Holy-Spirit on 2016/3/2.
 */
public class ResultsAdapter extends CommonAdapter<SuggestionBean> {


    public ResultsAdapter(Context context,
                          List<SuggestionBean> datas) {
        super(context, datas);
    }


    @Override
    public void convert(ViewHolder holder, SuggestionBean bean) {
        holder.setText(R.id.item_sim_addr, bean.getHeadStr())
                .setText(R.id.item_addr, bean.getDetailStr());
    }

    @Override
    public int getLayoutId() {
        return R.layout.lv_result_item;
    }
}
