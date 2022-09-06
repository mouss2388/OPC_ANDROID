package com.example.projet_7.utils;

import com.example.projet_7.model.matrix_api.RowsItem;

import java.util.List;

public interface OnMatrixApiListReceivedCallback {
    void onMatrixApiListReceivedCallback(List<RowsItem> list, String id);

}
