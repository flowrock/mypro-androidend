package io.ruoyan.pxnavigator.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.ruoyan.pxnavigator.R;
import io.ruoyan.pxnavigator.model.Category;
import io.ruoyan.pxnavigator.model.Photo;
import io.ruoyan.pxnavigator.ui.activity.PhotoGalleryActivity;
import io.ruoyan.pxnavigator.utils.BasicUtils;
import io.ruoyan.pxnavigator.helper.DayHelper;
import io.ruoyan.pxnavigator.helper.PhotoCacheHelper;

/**
 * Created by Miroslaw Stanek on 20.01.15.
 */
public class GridPhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int PHOTO_ANIMATION_DELAY = 600;
    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();
    private static final String INTENT_EXTRA_PHOTO_CATEGORY = "PHOTO_CATEGORY";
    private static final String INTENT_EXTRA_PHOTO_DAY = "PHOTO_DAY";
    private static final String INTENT_EXTRA_PHOTO_POSITION = "PHOTO_POSITION";

    private static Context sContext;
    private final int mCellSize;
    private Category mCategory;
    private String mDay;
    private List<Photo> mPhotos;

    private boolean mLockedAnimations = false;
    private int mLastAnimatedItem = -1;

    public GridPhotosAdapter(Context context, int photoPerRow, Category category, String day) {
        sContext = context;
        mCellSize = BasicUtils.getScreenWidth(context) / photoPerRow;
        mCategory = category;
        mDay = day;
        mPhotos = PhotoCacheHelper.getPhotoInfo(category, day);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(sContext).inflate(R.layout.item_photo, parent, false);
        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
        layoutParams.height = mCellSize;
        layoutParams.width = mCellSize;
        layoutParams.setFullSpan(false);
        view.setLayoutParams(layoutParams);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindPhoto((PhotoViewHolder) holder, position);
    }

    private void bindPhoto(PhotoViewHolder holder, final int position) {
        holder.category = mCategory;
        holder.day = mDay;
        Glide.with(sContext)
                .load(mPhotos.get(position).getImageUrl())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        PhotoCacheHelper.addDrawablePhoto(mCategory, DayHelper.instance().getDay(),
                                position,
                                resource);
                        return false;
                    }
                })
                .override(mCellSize, mCellSize)
                .centerCrop()
                .into(holder.ivPhoto);
        animatePhoto(holder);
        if (mLastAnimatedItem < position) mLastAnimatedItem = position;
    }

    private void animatePhoto(PhotoViewHolder viewHolder) {
        if (!mLockedAnimations) {
            if (mLastAnimatedItem == viewHolder.getLayoutPosition()) {
                setLockedAnimations(true);
            }

            long animationDelay = PHOTO_ANIMATION_DELAY + viewHolder.getLayoutPosition() * 30;

            viewHolder.flRoot.setScaleY(0);
            viewHolder.flRoot.setScaleX(0);

            viewHolder.flRoot.animate()
                    .scaleY(1)
                    .scaleX(1)
                    .setDuration(200)
                    .setInterpolator(INTERPOLATOR)
                    .setStartDelay(animationDelay)
                    .start();
        }
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @InjectView(R.id.flRoot)
        FrameLayout flRoot;
        @InjectView(R.id.ivPhoto)
        ImageView ivPhoto;

        Category category;
        String day;

        public PhotoViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(sContext, PhotoGalleryActivity.class);
            intent.putExtra(INTENT_EXTRA_PHOTO_CATEGORY, category.name());
            intent.putExtra(INTENT_EXTRA_PHOTO_DAY, day);
            intent.putExtra(INTENT_EXTRA_PHOTO_POSITION, getLayoutPosition());
            sContext.startActivity(intent);
        }
    }

    public void setLockedAnimations(boolean lockedAnimations) {
        mLockedAnimations = lockedAnimations;
    }
}
