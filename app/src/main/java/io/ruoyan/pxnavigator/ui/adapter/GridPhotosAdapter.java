package io.ruoyan.pxnavigator.ui.adapter;

import android.content.Context;
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
import io.ruoyan.pxnavigator.utils.BasicUtils;
import io.ruoyan.pxnavigator.utils.PhotoCacheUtils;

/**
 * Created by Miroslaw Stanek on 20.01.15.
 */
public class GridPhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int PHOTO_ANIMATION_DELAY = 600;
    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();

    private final Context mContext;
    private final int mCellSize;
    private final Category mCategory;

    private final List<String> mPhotos;

    private boolean mLockedAnimations = false;
    private int mLastAnimatedItem = -1;

    public GridPhotosAdapter(Context context, int photoPerRow, Category category, List<String>
            imagelUrls) {
        mContext = context;
        mCellSize = BasicUtils.getScreenWidth(context) / photoPerRow;
        mCategory = category;
        mPhotos = imagelUrls;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_photo, parent, false);
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

    private void bindPhoto(final PhotoViewHolder holder, final int position) {
        Glide.with(mContext)
                .load(mPhotos.get(position))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        PhotoCacheUtils.addDrawablePhoto(mCategory, position, resource);
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
            if (mLastAnimatedItem == viewHolder.getPosition()) {
                setLockedAnimations(true);
            }

            long animationDelay = PHOTO_ANIMATION_DELAY + viewHolder.getPosition() * 30;

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

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.flRoot)
        FrameLayout flRoot;
        @InjectView(R.id.ivPhoto)
        ImageView ivPhoto;

        public PhotoViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    public void setLockedAnimations(boolean lockedAnimations) {
        mLockedAnimations = lockedAnimations;
    }
}
