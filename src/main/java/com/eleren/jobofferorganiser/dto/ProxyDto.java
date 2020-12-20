package com.eleren.jobofferorganiser.dto;

public class ProxyDto {
    private String address;
    private int port;

    public ProxyDto() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
