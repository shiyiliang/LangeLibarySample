package shiyiliang.me.baselibary.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import shiyiliang.me.baselibary.R;
import shiyiliang.me.baselibary.R2;
import shiyiliang.me.baselibary.base.DefaultBaseActivity;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017-07-06
 * Desc  :
 */

public class ImageShowActivity extends DefaultBaseActivity {
    @BindView(R2.id.show_image)
    ImageView ivShowImage;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_image_show;
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("ImageUrl");

        Glide.with(this).load(url).into(ivShowImage);
    }

}
