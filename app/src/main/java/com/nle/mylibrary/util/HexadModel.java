package com.nle.mylibrary.util;

/**
 * Created by gfdej on 2015/11/27.
 */
public class HexadModel {
    private String hex;
    private String info;

    public HexadModel() {
        super();
    }

    public HexadModel(String hex, String info) {
//        this.hex = "0x"+hex;
        this.hex = hex;
        this.info = info;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getHex() {
        return hex;
    }

    public String getInfo() {
        return info;
    }

}
