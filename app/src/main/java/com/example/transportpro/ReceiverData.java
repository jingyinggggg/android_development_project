package com.example.transportpro;

public class ReceiverData {
    private final String name;
    private final String mobile;
    private final String email;
    private final String postcode;
    private final String add1;
    private final String add2;
    private final String add3;

    public ReceiverData(String name, String mobile, String email, String postcode, String add1, String add2, String add3) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.postcode = postcode;
        this.add1 = add1;
        this.add2 = add2;
        this.add3 = add3;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getAdd1() {
        return add1;
    }

    public String getAdd2() {
        return add2;
    }

    public String getAdd3() {
        return add3;
    }
}
