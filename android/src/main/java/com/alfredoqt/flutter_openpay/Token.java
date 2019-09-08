package com.alfredoqt.flutter_openpay;

import com.google.gson.annotations.SerializedName;

public class Token {
    @SerializedName("id")
    public String id;

    @SerializedName("card")
    public Card card;
}
