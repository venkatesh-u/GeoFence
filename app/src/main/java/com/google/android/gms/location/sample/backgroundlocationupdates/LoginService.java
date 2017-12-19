package com.google.android.gms.location.sample.backgroundlocationupdates;

import android.app.IntentService;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by venkatesh on 30/10/17.
 */

public class LoginService extends IntentService {

    public LoginService() {
        super("QuoteService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
       final Stock p = intent.getParcelableExtra("stock");
        performSubmit(p);



    }


    private void performSubmit(Stock p) {

            UserRegBean bean = new UserRegBean();
            bean.email = p.getSymbol();
            bean.password = "12345678";
            bean.device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            bean.device_token = PreferencesData.getRegistrationId(this);

            Call<ResponseBody> cl;
            MainNetworkService service = MyApplication.getSerivce();
            cl = getCallInstance(service, bean);
            cl.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                    Log.d("Data: success, ", response.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("Data: failure, ", t.toString());

                }


            });
            ///new task().execute();
    }

    public Call<ResponseBody> getCallInstance(MainNetworkService service, UserRegBean bean) {
        return  service.userSignin("users/sign_in", bean.device_token, bean);
    }

}
