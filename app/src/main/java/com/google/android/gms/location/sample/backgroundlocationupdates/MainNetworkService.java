package com.google.android.gms.location.sample.backgroundlocationupdates;

import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Url;

/**
 * Created by RameshK on 24-11-2015.
 */

public interface MainNetworkService {
    @POST
    Call<ResponseBody> userSignin(@Url String url, @Header("DEVICE-TOKEN") String token, @Body UserRegBean user);
    //@FieldMap Map<String,Object> params);


}
