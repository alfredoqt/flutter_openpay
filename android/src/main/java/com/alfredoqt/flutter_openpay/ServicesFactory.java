package com.alfredoqt.flutter_openpay;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ServicesFactory {
    private static volatile ServicesFactory INSTANCE = null;
    private String baseUrl;
    private String merchantId;
    private String apiKey;

    private ServicesFactory(String baseUrl, String merchantId, String apiKey) {
        this.baseUrl = baseUrl;
        this.merchantId = merchantId;
        this.apiKey = apiKey;
    }

    public static ServicesFactory getInstance(String baseUrl, String merchantId, String apiKey) {
        if (INSTANCE == null) {
            Class var3 = ServicesFactory.class;
            synchronized(ServicesFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServicesFactory(baseUrl, merchantId, apiKey);
                }
            }
        }

        return INSTANCE;
    }

    public <V extends BaseService<?, ?>> V getService(Class<V> type) {
        try {
            Constructor<V> ctor = type.getDeclaredConstructor(String.class, String.class, String.class);
            ctor.setAccessible(true);
            return (V) ctor.newInstance(this.baseUrl, this.merchantId, this.apiKey);
        } catch (NoSuchMethodException var3) {
            return null;
        } catch (InstantiationException var4) {
            return null;
        } catch (IllegalAccessException var5) {
            return null;
        } catch (IllegalArgumentException var6) {
            return null;
        } catch (InvocationTargetException var7) {
            return null;
        }
    }
}
