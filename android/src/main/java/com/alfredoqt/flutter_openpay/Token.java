package com.alfredoqt.flutter_openpay;

import com.google.api.client.util.Key;

public class Token {
    @Key
    private String id;
    @Key
    private Card card;

    public Token() {
    }

    public String toString() {
        return String.format("Token [id=%s, card=%s]", this.id, this.card);
    }

    public Token id(String id) {
        this.id = id;
        return this;
    }

    public Token card(Card card) {
        this.card = card;
        return this;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Card getCard() {
        return this.card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
