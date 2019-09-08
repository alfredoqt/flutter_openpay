package com.alfredoqt.flutter_openpay;

import com.google.gson.annotations.SerializedName;

public class Card {

    @SerializedName("id")
    public String id;

    @SerializedName("holder_name")
    public String holderName;

    @SerializedName("expiration_month")
    public String expirationMonth;

    @SerializedName("expiration_year")
    public String expirationYear;

    @SerializedName("card_number")
    public String cardNumber;

    @SerializedName("cvv2")
    public String cvv2;

}
