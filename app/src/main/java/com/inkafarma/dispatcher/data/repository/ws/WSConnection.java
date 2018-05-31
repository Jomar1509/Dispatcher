package com.inkafarma.dispatcher.data.repository.ws;

import android.content.Context;

import com.inkafarma.dispatcher.BuildConfig;
import com.inkafarma.dispatcher.data.repository.InkaFarmaApi;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/*
* Clase usada para la conecion con Retrofit
* {@link Retrofit com.squareup.retrofit2:retrofit:2.2.0}
* */
public final class WSConnection {
    static final int CONNECTION_TIMEOUT = 60;
    static final int READ_TIMEOUT = 60;
    static final int WRITE_TIMEOUT = 60;
    static final String HEADER_TOKEN = "x-access-token";
    static final String HEADER_APP = "app-name";

    private WSConnection() {
    }

    /*
    * Todo
    * */
    private static OkHttpClient okHttpBuild(Context context, final String token) {

        return new OkHttpClient.Builder()
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder()
                                .addHeader(HEADER_TOKEN, token)
                                .addHeader(HEADER_APP, BuildConfig.APPLICATION_ID);

                        Timber.v("%s: %s", HEADER_TOKEN, token);

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                })
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    private static Retrofit retrofitWithURL(Context context, String url, String token) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpBuild(context, token))
                .build();

        return retrofit;
    }

    public static InkaFarmaApi getAPI(Context context, String token) {

        return retrofitWithURL(context, BuildConfig.API_URL, token)
                .create(InkaFarmaApi.class);
    }

}
