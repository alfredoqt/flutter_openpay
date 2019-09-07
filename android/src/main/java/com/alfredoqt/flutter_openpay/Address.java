package com.alfredoqt.flutter_openpay;

import com.google.api.client.util.Key;

public class Address {
    @Key("postal_code")
    private String postalCode;
    @Key
    private String line1;
    @Key
    private String line2;
    @Key
    private String line3;
    @Key
    private String city;
    @Key
    private String state;
    @Key("country_code")
    private String countryCode;

    public Address() {
    }

    public String toString() {
        return String.format("Address [postalCode=%s, line1=%s, line2=%s, line3=%s, city=%s, state=%s, countryCode=%s]", this.postalCode, this.line1, this.line2, this.line3, this.city, this.state, this.countryCode);
    }

    public Address postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public Address line1(String line1) {
        this.line1 = line1;
        return this;
    }

    public Address line2(String line2) {
        this.line2 = line2;
        return this;
    }

    public Address line3(String line3) {
        this.line3 = line3;
        return this;
    }

    public Address city(String city) {
        this.city = city;
        return this;
    }

    public Address state(String state) {
        this.state = state;
        return this;
    }

    public Address countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getLine1() {
        return this.line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return this.line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getLine3() {
        return this.line3;
    }

    public void setLine3(String line3) {
        this.line3 = line3;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
