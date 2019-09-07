package com.alfredoqt.flutter_openpay;

public class OpenPayResult<T> {
    private OpenpayServiceException openpayServiceException;
    private OperationResult<T> operationResult;
    private ServiceUnavailableException serviceUnavailableException;

    public OpenPayResult() {
    }

    public OpenpayServiceException getOpenpayServiceException() {
        return this.openpayServiceException;
    }

    public void setOpenpayServiceException(OpenpayServiceException openpayServiceException) {
        this.openpayServiceException = openpayServiceException;
    }

    public OperationResult<T> getOperationResult() {
        return this.operationResult;
    }

    public void setOperationResult(OperationResult<T> operationResult) {
        this.operationResult = operationResult;
    }

    public ServiceUnavailableException getServiceUnavailableException() {
        return this.serviceUnavailableException;
    }

    public void setServiceUnavailableException(ServiceUnavailableException serviceUnavailableException) {
        this.serviceUnavailableException = serviceUnavailableException;
    }
}
