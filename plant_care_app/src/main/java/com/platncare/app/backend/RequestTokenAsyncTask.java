package com.platncare.app.backend;

import android.os.AsyncTask;
import client.endpoint.TokenEndpoint;
import model.Token;

public class RequestTokenAsyncTask extends AsyncTask<Object, Void, Token> {

    private RequestTokenExecutor executor;
    private Exception exception;

    public RequestTokenAsyncTask(RequestTokenExecutor executor) {
        this.executor = executor;
    }

    @Override
    protected Token doInBackground(Object... params) {
        try {
            String email = (String) params[0];
            String password = (String) params[1];

            return new TokenEndpoint().getToken(email, password);
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(final Token token) {
        if(exception != null) {
            executor.onFailure(exception);
        } else {
            executor.onSuccess(token);
        }
    }
}
