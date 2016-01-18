package io.ruoyan.pxnavigator.network;

import java.util.List;

import io.ruoyan.pxnavigator.model.Photo;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by ruoyan on 1/16/16.
 */
public class PopPhotoRequest extends Request{

    public PopPhotoRequest(String baseUrl, Class service) {
        super(baseUrl, service);
    }

    @Override
    public void executeRequest() {
        Call<List<Photo>> call = ((PopPhotoApiService)mService).requestPhotos(mQueryParameters);
        call.enqueue(new retrofit.Callback<List<Photo>>() {
            @Override
            public void onResponse(Response<List<Photo>> response, Retrofit retrofit) {
                List<Photo> photos;
                try {
                    photos = response.body();
                } catch (Exception e) {
                    photos = null;
                }
                if (mCallback == null)
                    throw new NullPointerException("The callback of photo request has not defined");
                mCallback.onResult(photos);
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

}
