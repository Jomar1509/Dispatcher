package com.inkafarma.dispatcher.data.repository.ws;


import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * <p>
 * CallbackCustom personalizado para manejar las respuestas de coneccion
 * los webservices.
 * </p>
 */
public abstract class CallbackCustom<T> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {

        if (response.isSuccessful()) {
            onResponseHttpOK(call, response.body(), response.code());
        } else {
            try {
                onResponseHttpError(response.code(), call, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public abstract void onResponseHttpOK(Call<T> call, T object, int code);

    public abstract void onResponseHttpError(int statuCode, Call<T> call, Response<T> response) throws IOException;

    public abstract void onFailure(Call<T> call, Throwable t);
}
