package com.example.projet_7.model.matrix_api;

@SuppressWarnings("unused")
public class Distance {
    private final String text;
    private final int value;

    public Distance(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }
}
