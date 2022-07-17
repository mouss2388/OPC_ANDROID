package com.example.projet_7.model.matrix_api;

import java.util.List;

@SuppressWarnings("unused")
public class Response {
    private List<String> destinationAddresses;
    private List<RowsItem> rows;
    private List<String> originAddresses;
    private String status;

    public List<String> getDestinationAddresses() {
        return destinationAddresses;
    }

    public List<RowsItem> getRows() {
        return rows;
    }

    public List<String> getOriginAddresses() {
        return originAddresses;
    }

    public String getStatus() {
        return status;
    }
}