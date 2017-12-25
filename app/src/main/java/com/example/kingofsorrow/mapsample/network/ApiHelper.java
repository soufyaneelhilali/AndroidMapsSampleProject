package com.example.kingofsorrow.mapsample.network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kingofsorrow on 11/04/2017.
 */

public class ApiHelper {
    private static final ApiHelper ourInstance = new ApiHelper();

    public static ApiHelper getInstance() {
        return ourInstance;
    }

    private ApiHelper() {
    }

    private MClient generateNewClient(String apiBaseUrl) {
        if (!apiBaseUrl.endsWith("/")) {
            apiBaseUrl += "/";
        }

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(apiBaseUrl)
                .addConverterFactory(buildGsonConverter())
                .client(httpClient.build());

        Retrofit retrofit = builder.build();
        return retrofit.create(MClient.class);
    }

    private static GsonConverterFactory buildGsonConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        // Adding custom deserializers
//        gsonBuilder.registerTypeAdapter(List.class, new Converter("reponse", "Commandes", ....));
        Gson myGson = gsonBuilder.create();

        return GsonConverterFactory.create(myGson);
    }

    public void getJson(@NonNull Context context, @NonNull String url, @NonNull String service, @NonNull String version, @NonNull String request, @NonNull String typeName, @NonNull String maxF, @NonNull String outPut, final RequestCallBack callBack) {
        if (TextUtils.isEmpty(url)) return;

        Call<ResponseBody> call = generateNewClient(url).getJson(url, service, version, request, typeName, maxF, outPut);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (callBack != null) {
                    callBack.onResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (callBack != null) {
                    callBack.onResponse(null);
                }
            }
        });
    }

    public void getLayer(@NonNull Context context, @NonNull String url, final RequestCallBack callBack) {
        if (TextUtils.isEmpty(url)) return;

        Call<ResponseBody> call = generateNewClient(url).getLayer(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (callBack != null) {
                    callBack.onResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (callBack != null) {
                    callBack.onResponse(null);
                }
            }
        });
    }
}
