package io.ruoyan.pxnavigator.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.ruoyan.pxnavigator.MyApp;
import io.ruoyan.pxnavigator.R;
import io.ruoyan.pxnavigator.model.Category;
import io.ruoyan.pxnavigator.model.Photo;
import io.ruoyan.pxnavigator.network.PopPhotoApiService;
import io.ruoyan.pxnavigator.network.PopPhotoRequest;
import io.ruoyan.pxnavigator.network.Request;
import io.ruoyan.pxnavigator.observe.Observer;
import io.ruoyan.pxnavigator.observe.Subject;
import io.ruoyan.pxnavigator.ui.adapter.GridPhotosAdapter;
import io.ruoyan.pxnavigator.ui.map.MapManager;
import io.ruoyan.pxnavigator.helper.DayHelper;
import io.ruoyan.pxnavigator.helper.PhotoCacheHelper;

/**
 * Created by ruoyan on 12/20/15.
 */
public class PhotoListFragment extends Fragment implements Observer, Request.Callback{
    private static final int PHOTO_PER_ROW = 3;
    private static final int PHOTO_PER_CATEGORY = 100;
    private static final int MAP_UPDATE_INTERVAL = 100;
    private static final int MAP_INITIAL_DELAY = 1000;
    private static final String EXTRA_CATEGORY = "extra_category";

    @InjectView(R.id.popPhotoRecyclerView)
    RecyclerView mPopPhotoRecyclerView;

    @InjectView(R.id.progressbar)
    View mProgressbar;

    private GridPhotosAdapter mGridPhotosAdapter;
    private StaggeredGridLayoutManager mLayoutManager;
    private Category mCategory;
    private String mDay;

    public static PhotoListFragment newInstance(Category category) {
        PhotoListFragment fragment = new PhotoListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_CATEGORY, category.toString());
        fragment.setArguments(bundle);
        DayHelper.instance().attach(fragment);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mCategory = Category.valueOf(bundle.getString(EXTRA_CATEGORY));
        mDay = DayHelper.instance().getDay();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_list, container,
                false);
        ButterKnife.inject(this, view);
        mProgressbar.setVisibility(View.VISIBLE);
        if (PhotoCacheHelper.getPhotoInfo(mCategory, mDay) != null)
            setupPopPhotosGrid();
        return view;
    }

    @Override
    //this method is called before onCreateView
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            loadPhotoInfo();
        }
    }

    private void loadPhotoInfo() {
        if (PhotoCacheHelper.getPhotoInfo(mCategory, mDay) == null) {//first time to request data
            requestPhotos();
        }
        else { //data already exists
            updateMap(getVisiblePhotoPositions());
        }

    }

    private void requestPhotos() {
        PopPhotoRequest request = new PopPhotoRequest(getResources().getString(R.string
                .my_backend_base_url),
                PopPhotoApiService.class);
        HashMap<String, String> queryParameters = new HashMap<>();
        queryParameters.put("day", mDay);
        queryParameters.put("category", mCategory.name());
        request.setQueryParameters(queryParameters);
        request.setCallback(this);
        request.executeRequest();
    }

    @Override
    public void onResult(Object result) {
        List<Photo> photos = new ArrayList<>((List<Photo>)result);
        PhotoCacheHelper.setPhotoInfo(mCategory, mDay, new ArrayList<>(photos));
        setupPopPhotosGrid();
        updateMap(getVisiblePhotoPositions());
    }

    private void setupPopPhotosGrid() {
        mProgressbar.setVisibility(View.GONE);
        mPopPhotoRecyclerView.setVisibility(View.VISIBLE);
        mGridPhotosAdapter = new GridPhotosAdapter(getActivity(), PHOTO_PER_ROW, mCategory, mDay);
        mPopPhotoRecyclerView.setAdapter(mGridPhotosAdapter);
        mLayoutManager = new StaggeredGridLayoutManager(PHOTO_PER_ROW,
                StaggeredGridLayoutManager.VERTICAL);
        mPopPhotoRecyclerView.setLayoutManager(mLayoutManager);
        mPopPhotoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                mGridPhotosAdapter.setLockedAnimations(true);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    updateMap(getVisiblePhotoPositions());
                }
            }

        });
    }

    private void updateMap(final int[] visiblePhotoPositions) {
        final MapManager manager = MapManager.getMapManager(getActivity());
        boolean initialized = MyApp.getAppStatus();
        int delay = MAP_UPDATE_INTERVAL;
        if (!initialized) {
            delay = MAP_INITIAL_DELAY;
            MyApp.setAppStatus(true);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (visiblePhotoPositions[0] != -1)
                    manager.generateCluster(mCategory, visiblePhotoPositions);
                else //try again
                    updateMap(getVisiblePhotoPositions());
            }
        }, delay);
    }

    private int[] getVisiblePhotoPositions() {
        int[] firstVisiblePhotos = new int[PHOTO_PER_ROW];
        try {
            //it may happen when the method below gets null pointer exception
            mLayoutManager.findFirstCompletelyVisibleItemPositions(firstVisiblePhotos);
        } catch (Exception e) {
            Arrays.fill(firstVisiblePhotos,0);
            mPopPhotoRecyclerView.smoothScrollToPosition(0);
        }
        int[] result = new int[2];
        result[0] = firstVisiblePhotos[0];
        result[1] = result[0]==PHOTO_PER_CATEGORY-PHOTO_PER_ROW-PHOTO_PER_CATEGORY%PHOTO_PER_ROW
                ?PHOTO_PER_CATEGORY-1:result[0]+PHOTO_PER_ROW-1;
        return result;
    }

    @Override
    //update the day variable when the day selection changes, controlled by the observable
    public void update(Subject subject) {
        mDay = DayHelper.instance().getDay();
    }

}
