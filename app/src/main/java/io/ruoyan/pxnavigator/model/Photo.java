package io.ruoyan.pxnavigator.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruoyan on 12/25/15.
 */
public class Photo implements Serializable{
    private static final long serialVersionUID = 1L;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("shutter_speed")
    @Expose
    private String shutterSpeed;
    @SerializedName("tags")
    @Expose
    private List<String> tags = new ArrayList<String>();
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("lens")
    @Expose
    private String lens;
    @SerializedName("aperture")
    @Expose
    private String aperture;
    @SerializedName("camera")
    @Expose
    private String camera;
    @SerializedName("iso")
    @Expose
    private String iso;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("focal_length")
    @Expose
    private String focalLength;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("full_image_url")
    @Expose
    private String fullImageUrl;
    @SerializedName("size3_image_url")
    @Expose
    private String size3ImageUrl;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;

    /**
     *
     * @return
     * The username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     * The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     * The rating
     */
    public Double getRating() {
        return rating;
    }

    /**
     *
     * @param rating
     * The rating
     */
    public void setRating(Double rating) {
        this.rating = rating;
    }

    /**
     *
     * @return
     * The shutterSpeed
     */
    public String getShutterSpeed() {
        return shutterSpeed;
    }

    /**
     *
     * @param shutterSpeed
     * The shutter_speed
     */
    public void setShutterSpeed(String shutterSpeed) {
        this.shutterSpeed = shutterSpeed;
    }

    /**
     *
     * @return
     * The tags
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     *
     * @param tags
     * The tags
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     *
     * @return
     * The longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude
     * The longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     *
     * @return
     * The lens
     */
    public String getLens() {
        return lens;
    }

    /**
     *
     * @param lens
     * The lens
     */
    public void setLens(String lens) {
        this.lens = lens;
    }

    /**
     *
     * @return
     * The aperture
     */
    public String getAperture() {
        return aperture;
    }

    /**
     *
     * @param aperture
     * The aperture
     */
    public void setAperture(String aperture) {
        this.aperture = aperture;
    }

    /**
     *
     * @return
     * The camera
     */
    public String getCamera() {
        return camera;
    }

    /**
     *
     * @param camera
     * The camera
     */
    public void setCamera(String camera) {
        this.camera = camera;
    }

    /**
     *
     * @return
     * The iso
     */
    public String getIso() {
        return iso;
    }

    /**
     *
     * @param iso
     * The iso
     */
    public void setIso(String iso) {
        this.iso = iso;
    }

    /**
     *
     * @return
     * The avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     *
     * @param avatar
     * The avatar
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     *
     * @return
     * The focalLength
     */
    public String getFocalLength() {
        return focalLength;
    }

    /**
     *
     * @param focalLength
     * The focal_length
     */
    public void setFocalLength(String focalLength) {
        this.focalLength = focalLength;
    }

    /**
     *
     * @return
     * The latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude
     * The latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return
     * The imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     *
     * @param imageUrl
     * The image_url
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     *
     * @return
     * The fullImageUrl
     */
    public String getFullImageUrl() {
        return fullImageUrl;
    }

    /**
     *
     * @param fullImageUrl
     * The full_image_url
     */
    public void setFullImageUrl(String fullImageUrl) {
        this.fullImageUrl = fullImageUrl;
    }

    /**
     *
     * @return
     * The id
     */
    public String getSize3ImageUrl() {
        return size3ImageUrl;
    }

    /**
     *
     * @param size3ImageUrl
     * The full_image_url
     */

    public void setSize3ImageUrl(String size3ImageUrl) {
        this.size3ImageUrl = size3ImageUrl;
    }

    /**
     *
     * @return
     * The size3_image_url
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
