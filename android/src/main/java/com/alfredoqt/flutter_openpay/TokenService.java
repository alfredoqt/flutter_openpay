package com.alfredoqt.flutter_openpay;

public class TokenService extends BaseService<Card, Token> {
    private static final String MERCHANT_TOKENS = "tokens";

    public TokenService(String baseUrl, String merchantId, String apiKey) {
        super(baseUrl, merchantId, apiKey, Token.class);
    }

    public Token create(Card card) throws OpenpayServiceException, ServiceUnavailableException {
        return (Token)this.post("tokens", card);
    }
}
