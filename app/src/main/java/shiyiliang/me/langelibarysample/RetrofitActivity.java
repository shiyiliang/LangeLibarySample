package shiyiliang.me.langelibarysample;

import android.os.Environment;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;


import com.example.smallhttp.download.DownloadResponseBody;
import com.example.smallhttp.upload.UploadProgressCallBack;
import com.example.smallhttp.upload.ProgressRequestBody;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;
import shiyiliang.me.baselibary.base.DefaultBaseActivity;
import shiyiliang.me.langelibarysample.adapter.RetrofitAdapter;
import shiyiliang.me.langelibarysample.bean.ClassInfo;
import shiyiliang.me.langelibarysample.bean.event.DownloadEvent;

public class RetrofitActivity extends DefaultBaseActivity {
    @BindView(R.id.rv_funciton)
    RecyclerView rvFunction;
    @BindView(R.id.file_name)
    EditText etFileName;

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
        else if (downloadEvent.type == 1)
            testOkHttp();
        else if (downloadEvent.type == 2) {
//            okhttpUpload();
            testUpload();
        }

    }

    private void okhttpUpload() {
        Log.i("upload", "upload start");
        OkHttpClient client = getClient();
        String name = etFileName.getText().toString().trim();
        name = TextUtils.isEmpty(name) ? "1.png" : name;
        String path = Environment.getExternalStorageDirectory() + File.separator + name;
        File file = new File(path);
        System.out.println(file.getAbsolutePath());

        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);
        RequestBody muliBody = new MultipartBody.Builder()
                .addFormDataPart("image", file.getName(), fileBody)
                .addFormDataPart("username", "lange")
                .build();


        Request request = new Request.Builder()
                .url("http://192.168.3.6:8080/blog/upload")
                .post(muliBody)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                Log.i("upload", response.isSuccessful() + "");
            }
        });

    }


    //测试文件的下载
    private void testUpload() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .addInterceptor(getHttpLoggingInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://192.168.3.6:8080/blog/")
                .build();
        DownloadService downloadService = retrofit.create(DownloadService.class);

        String name = etFileName.getText().toString().trim();
        name = TextUtils.isEmpty(name) ? "1.png" : name;
        String path = Environment.getExternalStorageDirectory() + File.separator + name;
        File file = new File(path);

        ProgressRequestBody rq=new ProgressRequestBody(file, MediaType.parse("image/*"), new UploadProgressCallBack() {
            @Override
            public void updateProgress(long total, long remain,boolean isCompelte) {
                Log.i("upload", (Looper.getMainLooper()==Looper.myLooper())+"");
                etFileName.setText(Thread.currentThread().getName());
                Log.i("upload",Thread.currentThread().getName()+"-->"+total+"--->"+remain);
            }
        });
        MultipartBody.Part part2=MultipartBody.Part.createFormData("test",file.getName(),rq);
        Call<ResponseBody> uploadCall = downloadService.uploadOneFile(part2);
        uploadCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i("upload", response.isSuccessful() + "");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private void testOkHttp() {
        Toast.makeText(mContext, "点击了", Toast.LENGTH_LONG).show();
        Request request = new Request.Builder()
                .url("")
                .build();
        OkHttpClient client = getClient();

        try {
            okhttp3.Response execute = client.newCall(request).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private OkHttpClient getClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .addInterceptor(getHttpLoggingInterceptor())
                .addNetworkInterceptor(new HttpLoggingInterceptor())
                .build();
    }

    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    private void startDownload() {
        Toast.makeText(mContext, "开始下载", Toast.LENGTH_LONG).show();
        String baseUrl = "https://github.com/shiyiliang/AndroidSourceCodeAnalyse/archive/";

        HttpLoggingInterceptor logger = getHttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        okhttp3.Response response = chain.proceed(chain.request());
                        return response.newBuilder().body(new DownloadResponseBody(response.body(), new UploadProgressCallBack() {
                            @Override
                            public void updateProgress(long total, long remain, boolean isComplete) {
                                Log.i("upload",Thread.currentThread().getName()+"--->"+total+"--->"+remain+"-->"+isComplete);
                            }
                        })).build();
                    }
                })
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
        @POST("upload")
        Call<ResponseBody> uploadFile(@Part("body") RequestBody body, @Part MultipartBody.Part file);


        @POST("upload")
        Call<ResponseBody> uploadFile(@Body RequestBody body);

        @FormUrlEncoded
        @POST("upload")
        Call<ResponseBody> uploadParams(@Field("username") String username, @Field("token") String token);

        @FormUrlEncoded
        @POST("upload")
        Call<ResponseBody> uploadParams(@FieldMap Map<String, String> map);

        @POST("upload")
        Call<ResponseBody> uploadParams(@Body RequestBody body);

        //如果@Part("name") RequestBody body在上传文件时，可以用，但是无法上传成功，当做参数处理
        @Multipart
        @POST("upload")
        Call<ResponseBody> uploadOneFile(@Part MultipartBody.Part file);

        @Multipart
        @POST("upload")
        Call<ResponseBody> uploadFiles(@PartMap Map<String, RequestBody> map);

        @Multipart
        @POST("upload")
        Call<ResponseBody> uploadFiles(@Part List<MultipartBody.Part> parts);


    }
}
