package com.example.smallhttp.download;

import android.support.annotation.Nullable;

import com.example.smallhttp.upload.ProgressCallBack;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import okio.Timeout;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/6/25
 * Desc  : 下载进度监听
 */

public class DownloadResponseBody extends ResponseBody {
    private ProgressCallBack mProgressCallBack;
    private ResponseBody originalResponseBody;
    private BufferedSource buffer;

    public DownloadResponseBody(ResponseBody responseBody, ProgressCallBack callBack) {
        this.originalResponseBody = responseBody;
        this.mProgressCallBack = callBack;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return originalResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return originalResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (buffer == null) {
            buffer = Okio.buffer(source(originalResponseBody.source()));
        }
        return buffer;
    }

    private Source source(BufferedSource s) {
        return new ForwardingSource(s) {
            long totalBytesRead = 0L;
            final long total = originalResponseBody.contentLength();

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += (bytesRead != -1 ? bytesRead : 0);
                mProgressCallBack.updateProgress(total, totalBytesRead, total == totalBytesRead);
                return bytesRead;
            }
        };
    }
}
