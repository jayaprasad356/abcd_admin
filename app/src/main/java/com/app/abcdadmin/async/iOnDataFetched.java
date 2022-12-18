package com.app.abcdadmin.async;

public interface iOnDataFetched {
    void showProgressBar(int progress);

    void hideProgressBar();

    void setDataInPageWithResult(Object result);
}