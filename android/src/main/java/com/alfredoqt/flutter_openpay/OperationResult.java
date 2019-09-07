package com.alfredoqt.flutter_openpay;

public class OperationResult<T> {
    private T result;

    public OperationResult(T result) {
        this.result = result;
    }

    public T getResult() {
        return this.result;
    }
}