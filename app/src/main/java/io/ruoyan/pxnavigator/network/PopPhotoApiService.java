package io.ruoyan.pxnavigator.network;

import java.util.HashMap;
import java.util.List;

import io.ruoyan.pxnavigator.model.Photo;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.QueryMap;

/**
 * Created by ruoyan on 12/27/15.
 */
public interface PopPhotoApiService {
    @GET("/api/photos")
    Call<List<Photo>> requestPhotos(@QueryMap HashMap<String, String> params);
}
