package com.alfredoqt.flutter_openpay;

import android.annotation.TargetApi;
import java.io.IOException;

public class ServiceUnavailableException extends IOException {
    private static final long serialVersionUID = -7388627000694002585L;

    public ServiceUnavailableException(String message) {
        super(message);
    }

    @TargetApi(26)
    public ServiceUnavailableException(Throwable cause) {
        super(cause);
    }

    @TargetApi(26)
    public ServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
