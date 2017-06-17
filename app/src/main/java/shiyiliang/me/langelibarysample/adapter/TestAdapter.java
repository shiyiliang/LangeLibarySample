package shiyiliang.me.langelibarysample.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import shiyiliang.me.langelibarysample.R;

/**
 * Created by lange on 2017/6/17.
 */

public class TestAdapter extends CommonAdapter<String> {

    public TestAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, String s, int position) {
        TextView tvName = holder.getView(R.id.name);
        tvName.setText(s);
    }

    public void setData(List<String> data) {
        this.mDatas = data;
        notifyDataSetChanged();
    }
}
