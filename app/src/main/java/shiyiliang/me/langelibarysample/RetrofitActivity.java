package shiyiliang.me.langelibarysample;

import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.zhy.adapter.recyclerview.CommonAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.internal.http.RetryAndFollowUpInterceptor;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Source;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;
import shiyiliang.me.baselibary.base.DefaultBaseActivity;
import shiyiliang.me.langelibarysample.adapter.RetrofitAdapter;
import shiyiliang.me.langelibarysample.bean.ClassInfo;
import shiyiliang.me.langelibarysample.bean.event.DownloadEvent;
import shiyiliang.me.smallhttp.Interceptor.RetryIntercepter;

public class RetrofitActivity extends DefaultBaseActivity {
    @BindView(R.id.rv_funciton)
    RecyclerView rvFunction;
    private List<ClassInfo> mData = new ArrayList<>();

    @Override
    protected int getLayoutID() {
        return R.layout.activity_retrofit;
    }

    @Override
    protected void init() {
        //初始化数据
        mData.add(new ClassInfo("文件下载"));
        mData.add(new ClassInfo("okhttp拦截器分析"));
        mData.add(new ClassInfo("retrofit文件上传"));

        RetrofitAdapter adapter = new RetrofitAdapter(mContext, R.layout.retrofit_item, mData);
        LinearLayoutManager lm = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rvFunction.setLayoutManager(lm);
        rvFunction.setAdapter(adapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void startDownload(DownloadEvent downloadEvent) {
        if (downloadEvent.type == 0)
            startDownload();
        else if(downloadEvent.type==1)
            testOkHttp();
        else if(downloadEvent.type==2){
            testUpload();
        }
    }

    //测试文件的下载
    private void testUpload() {
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(2,TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor())
                .build();

        Retrofit retrofit =new Retrofit.Builder()
                .client(client)
                .baseUrl("http://192.168.1.180:8080")
                .build();
        DownloadService downloadService = retrofit.create(DownloadService.class);

        String des="hello,this is file upload test";
        RequestBody desQB=RequestBody.create(MediaType.parse("multipart/form-data"),des);
        String path= Environment.getExternalStorageDirectory()+File.separator+"d.txt";
        File file=new File(path);
        RequestBody fileRQ=RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part part= MultipartBody.Part.createFormData("picture",file.getName(),fileRQ);

        Call<ResponseBody> uploadCall = downloadService.uploadFile(desQB, part);
        uploadCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i("upload",response.isSuccessful()+"");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private void testOkHttp() {
        Toast.makeText(mContext, "点击了", Toast.LENGTH_LONG).show();
        Request request=new Request.Builder()
                .url("")
                .build();
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(2,TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor())
                .addNetworkInterceptor(new HttpLoggingInterceptor())
                .build();

        try {
            okhttp3.Response execute = client.newCall(request).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startDownload() {
        Toast.makeText(mContext, "开始下载", Toast.LENGTH_LONG).show();
        String baseUrl = "https://github.com/shiyiliang/AndroidSourceCodeAnalyse/archive/";

        HttpLoggingInterceptor logger = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i("demo", message);
            }
        });

        logger.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
//                .addNetworkInterceptor(new RetryIntercepter(2))
                .addInterceptor(logger)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .build();
        DownloadService downloadService = retrofit.create(DownloadService.class);
        final String url = "master.zip";
        Call<ResponseBody> download = downloadService.download(url);
        download.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    System.out.println("下载了");
                    downlaodFile(response, url);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("失败了");
                t.printStackTrace();
            }
        });
    }

    private void downlaodFile(Response<ResponseBody> response, String fileName) {
        File filesDir = mContext.getFilesDir();
        FileOutputStream out = null;
        InputStream inputStream = null;
        try {
            out = new FileOutputStream(new File(filesDir, fileName));
            inputStream = response.body().byteStream();
            long total = response.body().contentLength();

            byte[] buffer = new byte[1024];
            int len = 0;
            long downloadCount = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                downloadCount += len;
                out.write(buffer, 0, len);

                System.out.println("已经下载了---》" + downloadCount);
                if (downloadCount == total) {
                    Log.i("demo", "下载完了");
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public interface DownloadService {
        @Headers({"User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36"})
        @GET
        Call<ResponseBody> download(@Url String url);

        @Multipart
        @POST("/upload")
        Call<ResponseBody> uploadFile(@Part("des")RequestBody des, @Part MultipartBody.Part file);
    }
}
