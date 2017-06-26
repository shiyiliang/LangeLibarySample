package com.example.smallhttp.upload;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/6/25
 * Desc  :
 */

public interface ProgressCallBack {
    void updateProgress(long total,long remain,boolean isComplete);
}
