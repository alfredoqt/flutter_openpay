package com.alfredoqt.flutter_openpay;

public interface OperationCallBack<T> {
    void onError(OpenpayServiceException var1);

    void onCommunicationError(ServiceUnavailableException var1);

    void onSuccess(OperationResult<T> var1);
}
