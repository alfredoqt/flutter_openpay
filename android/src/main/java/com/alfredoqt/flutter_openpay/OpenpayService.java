package com.alfredoqt.flutter_openpay;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OpenpayService {

    static final String BASE_URL_PRODUCTION_NO_SLASH = "https://api.openpay.mx/v1";
    static final String BASE_URL_DEVELOPMENT_NO_SLASH = "https://sandbox-api.openpay.mx/v1";
    static final String BASE_URL_PRODUCTION = BASE_URL_PRODUCTION_NO_SLASH + "/";
    static final String BASE_URL_DEVELOPMENT = BASE_URL_DEVELOPMENT_NO_SLASH + "/";

    @POST("{merchantId}/tokens")
    Call<Token> createToken(
            @Path("merchantId") String merchantId,
            @Header("Authorization") String authorization,
            @Header("User-Agent") String userAgent,
            @Body Card card
    );

}
