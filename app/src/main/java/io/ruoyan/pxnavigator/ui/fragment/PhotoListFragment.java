package io.ruoyan.pxnavigator.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.ruoyan.pxnavigator.R;
import io.ruoyan.pxnavigator.ui.adapter.GridPhotosAdapter;

/**
 * Created by ruoyan on 12/20/15.
 */
public class PhotoListFragment extends Fragment{
    private GridPhotosAdapter mGridPhotosAdapter;

    @InjectView(R.id.popPhotoRecyclerView)
    RecyclerView mPopPhotoRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_list, container,
                false);
        ButterKnife.inject(this,view);
        setupPopPhotosGrid();
        return view;
    }

    private void setupPopPhotosGrid() {
        mPopPhotoRecyclerView.setVisibility(View.VISIBLE);
        mGridPhotosAdapter = new GridPhotosAdapter(getActivity());
        mPopPhotoRecyclerView.setAdapter(mGridPhotosAdapter);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mPopPhotoRecyclerView.setLayoutManager(layoutManager);
        mPopPhotoRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                mGridPhotosAdapter.setLockedAnimations(true);
            }
        });
    }
}
