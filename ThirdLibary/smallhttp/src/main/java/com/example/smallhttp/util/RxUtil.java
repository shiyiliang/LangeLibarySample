package com.example.smallhttp.util;

import com.example.smallhttp.bean.HttpResult;
import com.example.smallhttp.exception.SmallHttpException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017-06-26
 * Desc  :
 */

public class RxUtil {

    /**
     * 转换数据
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<HttpResult<T>, T> applyTransform() {
        return new Observable.Transformer<HttpResult<T>, T>() {
            @Override
            public Observable<T> call(Observable<HttpResult<T>> httpResultObservable) {
                return httpResultObservable.flatMap(new Func1<HttpResult<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(HttpResult<T> tHttpResult) {
                        if (tHttpResult.getCode() != HttpResult.RESULT_OK) {
                            return Observable.error(new SmallHttpException(tHttpResult.getCode(),tHttpResult.getMessage()));
                        }
                        return Observable.just(tHttpResult.getData());
                    }
                });
            }
        };
    }

    /**
     * 切换线程
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T,T> applyMianScheduler(){
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 全部切换至io线程
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T,T> applyIOScheduler(){
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.subscribeOn(Schedulers.io()).observeOn(Schedulers.io());
            }
        };
    }
}
