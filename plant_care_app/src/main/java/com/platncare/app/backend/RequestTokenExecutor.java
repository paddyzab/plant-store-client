package com.platncare.app.backend;

import model.Token;

public interface RequestTokenExecutor {

    public void onSuccess(Token token);

    public void onFailure(Exception e);
}
