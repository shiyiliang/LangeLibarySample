package shiyiliang.me.langelibarysample;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.LoaderViewManager;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import shiyiliang.me.baselibary.base.DefaultBaseActivity;
import shiyiliang.me.langelibarysample.adapter.TestAdapter;

public class SwipLayoutActivity extends DefaultBaseActivity {
    @BindView(R.id.swipe_target)
    RecyclerView rvTest;
    @BindView(R.id.stl_parent)
    SwipeToLoadLayout stllParent;

    List<String> mData = new ArrayList<>();
    private TestAdapter adapter;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_swip_layout;
    }

    @Override
    protected void init() {
        initRecycleView();
        initSwipeToLayout();

    }

    private void initRecycleView() {
        LinearLayoutManager lm = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        adapter = new TestAdapter(mContext,R.layout.test,mData);
        HeaderAndFooterWrapper wrapper=new HeaderAndFooterWrapper(adapter);
        View headerView = LoaderViewManager.createHeaderView(mContext, LoaderViewManager.Type.ClassicStyle, stllParent);
        View footerView = LoaderViewManager.createFooterView(mContext, LoaderViewManager.Type.GoogleStyle, stllParent);
        wrapper.addHeaderView(headerView);
        wrapper.addHeaderView(footerView);
        rvTest.setLayoutManager(lm);
        rvTest.setAdapter(wrapper);
    }

    private void initSwipeToLayout() {
        View headerView = LoaderViewManager.createHeaderView(mContext, LoaderViewManager.Type.ClassicStyle, stllParent);
        View footerView = LoaderViewManager.createFooterView(mContext, LoaderViewManager.Type.GoogleStyle, stllParent);
        stllParent.setRefreshHeaderView(headerView);
        stllParent.setLoadMoreFooterView(footerView);
        stllParent.setRefreshing(true);

        stllParent.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                stllParent.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stllParent.setLoadingMore(false);
                        mData.add(mData.size() + "more 数据");
                        adapter.notifyItemInserted(mData.size()-1);
                        Toast.makeText(mContext, "上拉加载完成", Toast.LENGTH_LONG).show();
                    }
                }, 3000);
            }
        });

        stllParent.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                stllParent.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stllParent.setRefreshing(false);
                        mData.clear();
                        for (int i = 0; i < 10; i++) {
                            mData.add(i + "个数据");
                        }
                        adapter.notifyDataSetChanged();
                        Toast.makeText(mContext, "刷新完成", Toast.LENGTH_LONG).show();
                    }
                }, 3000);
            }
        });
    }

}
