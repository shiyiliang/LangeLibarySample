package com.example.smallhttp.subscriber;

import com.example.smallhttp.bean.HttpResult;
import com.example.smallhttp.exception.SmallHttpException;

import rx.Observable;
import rx.Subscriber;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017-06-26
 * Desc  :
 */

public abstract class HttpSubscriber<T> extends Subscriber<HttpResult<T>> {
    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        onFailure(e);
    }

    @Override
    public void onNext(HttpResult<T> tHttpResult) {
        if (isError(tHttpResult)) {
            onFailure(new SmallHttpException(tHttpResult.getCode(), tHttpResult.getMessage()));
        } else {
            onSuccess(tHttpResult.getData());
        }
    }

    public abstract boolean isError(HttpResult<T> result);

    public abstract void onSuccess(T t);

    //// TODO: 2017-06-26 这里的错误传入的是 Throwable吗？需要传SmallHttpException不？
    public abstract void onFailure(Throwable e);
}
