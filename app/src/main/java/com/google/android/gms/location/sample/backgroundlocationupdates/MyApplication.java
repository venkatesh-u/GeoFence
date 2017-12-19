package com.google.android.gms.location.sample.backgroundlocationupdates;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.provider.Settings;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by venkatesh on 30/10/17.
 */

@SuppressLint("NewApi")
public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks{

    public static MainNetworkService service;
    public static Retrofit mRetrofit;
    private static OkHttpClient httpClient = new OkHttpClient();
    private static MyApplication instance;

    MyApplication(){
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PreferencesData.initPrefs(this);
        instance = this;
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }


    public static synchronized MainNetworkService getSerivce()
    {
        if (service == null) {
            service = getRetrofit().create(MainNetworkService.class);
        }
        return service;
    }


    public static synchronized Retrofit getRetrofit()
    {
        if(mRetrofit==null)
        {
            httpClient.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("device-type", "ANDROID")
                            .header("authentication-token", PreferencesData.getToken(MyApplication.getInstance()))
                            .header("device-id", Settings.Secure.getString(MyApplication.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID))
                            //.header("refresh_token",PreferencesData.getString(MyApplication.getInstance(),PreferencesData.PREF_REFRESH_TOKEN))
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);
                }
            });

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.interceptors().add(interceptor);
            httpClient.setReadTimeout(1, TimeUnit.MINUTES);
            httpClient.setWriteTimeout(1, TimeUnit.MINUTES);

            mRetrofit = new Retrofit.Builder()
                    .baseUrl("https://qa-api.eteki.com/")
                    .addConverterFactory(new GsonStringConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();
        }
        return mRetrofit;
    }

    public static synchronized MyApplication getInstance() {
        return instance;
    }

}
