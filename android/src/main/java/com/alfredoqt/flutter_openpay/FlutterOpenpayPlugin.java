package com.alfredoqt.flutter_openpay;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.flutter.Log;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * FlutterOpenpayPlugin
 */
public class FlutterOpenpayPlugin implements MethodCallHandler {

    private static final String TAG = FlutterOpenpayPlugin.class.getSimpleName();
    private static Map<Integer, String> openpayErrorCodes = new HashMap<>();

    private Activity activity;
    private OpenpayApi openpayApi;
    private DeviceCollectorDefaultImpl deviceCollectorDefault;

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_openpay");
        channel.setMethodCallHandler(new FlutterOpenpayPlugin(registrar.activity()));
    }

    public FlutterOpenpayPlugin(Activity activity) {
        this.activity = activity;
        openpayApi = new OpenpayApi();
        deviceCollectorDefault = new DeviceCollectorDefaultImpl();
        openpayErrorCodes.put(1000, "ERROR_INTERNAL_SERVER_ERROR");
        openpayErrorCodes.put(1001, "ERROR_BAD_REQUEST");
        openpayErrorCodes.put(1002, "ERROR_UNAUTHORIZED");
        openpayErrorCodes.put(1003, "ERROR_UNPROCESSABLE_ENTITY");
        openpayErrorCodes.put(1004, "ERROR_SERVICE_UNAVAILABLE");
        openpayErrorCodes.put(1005, "ERROR_NOT_FOUND");
        openpayErrorCodes.put(1009, "ERROR_REQUEST_ENTITY_TOO_LARGE");
        openpayErrorCodes.put(1010, "ERROR_PUBLIC_KEY_NOT_ALLOWED");
        openpayErrorCodes.put(2004, "ERROR_INVALID_CARD_NUMBER");
        openpayErrorCodes.put(2005, "ERROR_INVALID_EXP_DATE");
        openpayErrorCodes.put(2006, "ERROR_CVV2_MISSING");
        openpayErrorCodes.put(2007, "ERROR_CARD_NUMBER_ONLY_SANDBOX");
        openpayErrorCodes.put(2009, "ERROR_INVALID_CVV2");
        openpayErrorCodes.put(2011, "ERROR_CARD_PRODUCT_TYPE_NOT_SUPPORTED");
        openpayErrorCodes.put(3001, "ERROR_CARD_DECLINED");
        openpayErrorCodes.put(3002, "ERROR_CARD_EXPIRED");
        openpayErrorCodes.put(3003, "ERROR_CARD_INSUFFICIENT_FUNDS");
        openpayErrorCodes.put(3004, "ERROR_CARD_STOLEN");
        openpayErrorCodes.put(3005, "ERROR_CARD_FRAUDULENT");
        openpayErrorCodes.put(3008, "ERROR_CARD_NOT_SUPPORTED_IN_ONLINE_TRANSACTIONS");
        openpayErrorCodes.put(3009, "ERROR_CARD_REPORTED_AS_LOST");
        openpayErrorCodes.put(3010, "ERROR_CARD_RESTRICTED_BY_BANK");
        openpayErrorCodes.put(3011, "ERROR_CARD_RETAINED_BY_BANK");
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (call.method.equals("tokenizeCard")) {
            handleTokenizeCard(call, result);
        } else if (call.method.equals("getDeviceSessionId")) {
            handleGetDeviceSessionId(call, result);
        } else {
            result.notImplemented();
        }
    }

    private void handleTokenizeCard(MethodCall call, final Result result) {
        Map<String, Object> arguments = call.arguments();
        String merchantId = (String) arguments.get("merchantId");
        String publicApiKey = (String) arguments.get("publicApiKey");
        boolean productionMode = (boolean) arguments.get("productionMode");
        String cardholderName = (String) arguments.get("cardholderName");
        String cardNumber = (String) arguments.get("cardNumber");
        String cvv = (String) arguments.get("cvv");
        String expiryMonth = (String) arguments.get("expiryMonth");
        String expiryYear = (String) arguments.get("expiryYear");

        Card card = new Card();
        card.holderName = cardholderName;
        card.cardNumber = cardNumber;
        card.expirationMonth = expiryMonth;
        card.expirationYear = expiryYear;
        card.cvv2 = cvv;

        openpayApi.postToken(merchantId, card, publicApiKey, productionMode, new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    result.success(response.body().id);
                    return;
                }
                try {
                    JSONObject errorObject = new JSONObject(new String(response.errorBody().bytes()));
                    int errorCode = errorObject.getInt("error_code");
                    String description = errorObject.getString("description");
                    result.error(openpayErrorCodes.get(errorCode), description, null);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                    result.error("ERROR_SERVICE_UNAVAILABLE", e.getMessage(), null);
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                    result.error("ERROR_SERVICE_UNAVAILABLE", e.getMessage(), null);
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                result.error("ERROR_SERVICE_UNAVAILABLE", t.getMessage(), null);
            }
        });
    }

    private void handleGetDeviceSessionId(MethodCall call, Result result) {
        Map<String, Object> arguments = call.arguments();
        String merchantId = (String) arguments.get("merchantId");
        boolean productionMode = (boolean) arguments.get("productionMode");

        deviceCollectorDefault.setProductionMode(productionMode);
        deviceCollectorDefault.setMerchantId(merchantId);

        String deviceSessionId = deviceCollectorDefault.setup(activity);

        if (deviceSessionId != null) {
            result.success(deviceSessionId);
            return;
        }
        result.error("ERROR_UNABLE_TO_GET_SESSION_ID", "The device session id could not be generated.", null);
    }
}
