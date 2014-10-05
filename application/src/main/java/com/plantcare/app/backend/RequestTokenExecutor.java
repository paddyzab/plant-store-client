package com.plantcare.app.backend;

import client.model.Token;

public interface RequestTokenExecutor {

    public void onSuccess(Token token);

    public void onFailure(Exception e);
}
