package io.ruoyan.pxnavigator.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ruoyan on 1/8/16.
 */
public class PhotoWrapper implements Serializable{
    private static final long serialVersionUID = 1L;
    private ArrayList<Photo> photos;

    public PhotoWrapper(ArrayList<Photo> photos) {
        this.photos = photos;
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }
}
