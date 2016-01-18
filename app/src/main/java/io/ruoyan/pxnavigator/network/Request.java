package io.ruoyan.pxnavigator.network;

import java.util.HashMap;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by ruoyan on 1/16/16.
 */
public abstract class Request<T> {
    public Retrofit mRetrofit;
    public T mService;
    public HashMap<String,String> mQueryParameters;
    public Callback mCallback;

    public interface Callback {
        void onResult(Object result);
    }

    protected Request(String baseUrl, Class<T> service) {
        setupRetrofit(baseUrl);
        setupService(service);
    }

    private void setupRetrofit(String baseUrl) {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void setupService(Class<T> service) {
        mService = mRetrofit.create(service);
    }

    public void setQueryParameters(HashMap<String,String> queryParameters) {
        mQueryParameters = queryParameters;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public abstract void executeRequest();

}
