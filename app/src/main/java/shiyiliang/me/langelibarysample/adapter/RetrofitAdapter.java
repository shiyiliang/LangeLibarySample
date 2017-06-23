package shiyiliang.me.langelibarysample.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import shiyiliang.me.langelibarysample.R;
import shiyiliang.me.langelibarysample.bean.ClassInfo;
import shiyiliang.me.langelibarysample.bean.event.DownloadEvent;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017-06-22
 * Desc  :
 */

public class RetrofitAdapter extends CommonAdapter<ClassInfo> {
    public RetrofitAdapter(Context context, int layoutId, List<ClassInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ClassInfo classInfo, final int position) {
        Button action=holder.getView(R.id.action);
        action.setText(classInfo.name);
        holder.setOnClickListener(R.id.action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DownloadEvent(position));
            }
        });
    }
}
