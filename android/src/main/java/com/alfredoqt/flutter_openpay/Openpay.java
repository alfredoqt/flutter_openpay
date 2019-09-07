package com.alfredoqt.flutter_openpay;

import android.os.AsyncTask;

public class Openpay {
    private static final String URL_SANDBOX = "https://sandbox-api.openpay.mx";
    private static final String URL_PRODUCTION = "https://api.openpay.mx";
    private TokenService tokenService;
    private DeviceCollectorDefaultImpl deviceCollectorDefaultImpl;

    public Openpay(String merchantId, String apiKey, Boolean productionMode) {
        String baseUrl = "https://sandbox-api.openpay.mx";
        if (productionMode) {
            baseUrl = "https://api.openpay.mx";
        }

        ServicesFactory servicesFactory = ServicesFactory.getInstance(baseUrl, merchantId, apiKey);
        this.tokenService = (TokenService) servicesFactory.getService(TokenService.class);
        this.deviceCollectorDefaultImpl = new DeviceCollectorDefaultImpl(baseUrl, merchantId);
    }

    public void createToken(final Card card, final OperationCallBack<Token> operationCallBack) {
        (new AsyncTask<Void, Void, OpenPayResult<Token>>() {
            protected OpenPayResult<Token> doInBackground(Void... params) {
                OpenPayResult openPayResult = new OpenPayResult();

                try {
                    Token newToken = Openpay.this.tokenService.create(card);
                    openPayResult.setOperationResult(new OperationResult(newToken));
                } catch (OpenpayServiceException var4) {
                    openPayResult.setOpenpayServiceException(var4);
                } catch (ServiceUnavailableException var5) {
                    openPayResult.setServiceUnavailableException(var5);
                }

                return openPayResult;
            }

            protected void onPostExecute(OpenPayResult<Token> result) {
                if (result.getOperationResult() != null) {
                    operationCallBack.onSuccess(result.getOperationResult());
                } else if (result.getOpenpayServiceException() != null) {
                    operationCallBack.onError(result.getOpenpayServiceException());
                } else if (result.getServiceUnavailableException() != null) {
                    operationCallBack.onCommunicationError(result.getServiceUnavailableException());
                }

            }
        }).execute(new Void[0]);
    }

    public DeviceCollectorDefaultImpl getDeviceCollectorDefaultImpl() {
        return this.deviceCollectorDefaultImpl;
    }
}
