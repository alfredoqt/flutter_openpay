package com.alfredoqt.flutter_openpay;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenpayApi {

    private OpenpayService mProductionService;
    private OpenpayService mDevelopmentService;

    public OpenpayApi() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofitProduction = new Retrofit.Builder()
                .baseUrl(OpenpayService.BASE_URL_PRODUCTION)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Retrofit retrofitDevelopment = new Retrofit.Builder()
                .baseUrl(OpenpayService.BASE_URL_DEVELOPMENT)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mProductionService = retrofitProduction.create(OpenpayService.class);
        mDevelopmentService = retrofitDevelopment.create(OpenpayService.class);
    }

    public void postToken(String merchantId, Card card, String publicApiKey, boolean productionMode, Callback<Token> callback) {
        String userPass =
                publicApiKey + ":" + "";
        String encoded = Base64.encodeBase64String(StringUtils.getBytesUtf8(userPass));
        String authorization = "Basic " + encoded;
        String version = getClass().getPackage().getImplementationVersion();
        if (version == null) {
            version = "1.0.1-UNKNOWN";
        }
        if (productionMode) {
            mProductionService.createToken(merchantId, authorization, "openpay-android/" + version, card).enqueue(callback);
            return;
        }
        mDevelopmentService.createToken(merchantId, authorization, "openpay-android/" + version, card).enqueue(callback);
    }

}
