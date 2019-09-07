package com.alfredoqt.flutter_openpay;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import mx.openpay.android.Openpay;
import mx.openpay.android.OperationCallBack;
import mx.openpay.android.OperationResult;
import mx.openpay.android.exceptions.OpenpayServiceException;
import mx.openpay.android.exceptions.ServiceUnavailableException;
import mx.openpay.android.model.Card;
import mx.openpay.android.model.Token;

/**
 * FlutterOpenpayPlugin
 */
public class FlutterOpenpayPlugin implements MethodCallHandler {
    public static final String TAG = FlutterOpenpayPlugin.class.getSimpleName();

    private static Map<Integer, String> mOpenpayErrorCodes = new HashMap<>();

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_openpay");
        channel.setMethodCallHandler(new FlutterOpenpayPlugin());
        mOpenpayErrorCodes.put(1000, "ERROR_INTERNAL_SERVER_ERROR");
        mOpenpayErrorCodes.put(1001, "ERROR_BAD_REQUEST");
        mOpenpayErrorCodes.put(1002, "ERROR_UNAUTHORIZED");
        mOpenpayErrorCodes.put(1003, "ERROR_UNPROCESSABLE_ENTITY");
        mOpenpayErrorCodes.put(1004, "ERROR_SERVICE_UNAVAILABLE");
        mOpenpayErrorCodes.put(1005, "ERROR_NOT_FOUND");
        mOpenpayErrorCodes.put(1009, "ERROR_REQUEST_ENTITY_TOO_LARGE");
        mOpenpayErrorCodes.put(1010, "ERROR_PUBLIC_KEY_NOT_ALLOWED");
        mOpenpayErrorCodes.put(2004, "ERROR_INVALID_CARD_NUMBER");
        mOpenpayErrorCodes.put(2005, "ERROR_INVALID_EXP_DATE");
        mOpenpayErrorCodes.put(2006, "ERROR_CVV2_MISSING");
        mOpenpayErrorCodes.put(2007, "ERROR_CARD_NUMBER_ONLY_SANDBOX");
        mOpenpayErrorCodes.put(2009, "ERROR_INVALID_CVV2");
        mOpenpayErrorCodes.put(3001, "ERROR_CARD_DECLINED");
        mOpenpayErrorCodes.put(3002, "ERROR_CARD_EXPIRED");
        mOpenpayErrorCodes.put(3003, "ERROR_CARD_INSUFFICIENT_FUNDS");
        mOpenpayErrorCodes.put(3004, "ERROR_CARD_STOLEN");
        mOpenpayErrorCodes.put(3005, "ERROR_CARD_FRAUDULENT");
        mOpenpayErrorCodes.put(3008, "ERROR_CARD_NOT_SUPPORTED_IN_ONLINE_TRANSACTIONS");
        mOpenpayErrorCodes.put(3009, "ERROR_CARD_REPORTED_AS_LOST");
        mOpenpayErrorCodes.put(3010, "ERROR_CARD_RESTRICTED_BY_BANK");
        mOpenpayErrorCodes.put(3011, "ERROR_CARD_RETAINED_BY_BANK");
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (call.method.equals("tokenizeCard")) {
            handleTokenizeCard(call, result);
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

        Openpay openpay = new Openpay(merchantId, publicApiKey, productionMode);

        Card card = new Card();
        card.holderName(cardholderName);
        card.cardNumber(cardNumber);
        card.expirationMonth(Integer.parseInt(expiryMonth));
        card.expirationYear(Integer.parseInt(expiryYear));
        card.cvv2(cvv);

        openpay.createToken(card, new OperationCallBack<Token>() {
            @Override
            public void onError(OpenpayServiceException e) {
                result.error(mOpenpayErrorCodes.get(e.errorCode), e.description, null);
            }

            @Override
            public void onCommunicationError(ServiceUnavailableException e) {
                result.error("ERROR_SERVICE_UNAVAILABLE", e.getLocalizedMessage(), null);
            }

            @Override
            public void onSuccess(OperationResult<Token> operationResult) {
                result.success(operationResult.getResult().getId());
            }
        });
    }
}
