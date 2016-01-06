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
import io.ruoyan.pxnavigator.R;
import io.ruoyan.pxnavigator.data.ApiService;
import io.ruoyan.pxnavigator.model.Category;
import io.ruoyan.pxnavigator.model.Photo;
import io.ruoyan.pxnavigator.ui.adapter.GridPhotosAdapter;
import io.ruoyan.pxnavigator.ui.map.MapManager;
import io.ruoyan.pxnavigator.utils.DayUtils;
import io.ruoyan.pxnavigator.utils.MapUtils;
import io.ruoyan.pxnavigator.utils.PhotoCacheUtils;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by ruoyan on 12/20/15.
 */
public class PhotoListFragment extends Fragment {
    private static final int PHOTO_PER_ROW = 3;
    private static final int PHOTO_PER_CATEGORY = 100;
    private static final int MAP_UPDATE_INTERVAL = 100;
    private static final int MAP_INITIAL_DELAY = 1000;
    private static final String EXTRA_CATEGORY = "extra_category";
    private static final String BASE_URL = "http://159.203.117.77";

    @InjectView(R.id.popPhotoRecyclerView)
    RecyclerView mPopPhotoRecyclerView;

    private GridPhotosAdapter mGridPhotosAdapter;
    private StaggeredGridLayoutManager mLayoutManager;
    private Category mCategory;
    private List<Photo> mPhotos;

    public static PhotoListFragment newInstance(Category category) {
        PhotoListFragment fragment = new PhotoListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_CATEGORY, category.toString());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mCategory = Category.valueOf(bundle.getString(EXTRA_CATEGORY));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_list, container,
                false);
        ButterKnife.inject(this, view);
        if (PhotoCacheUtils.getPhotoInfo(mCategory, DayUtils.getDay()) != null) //make sure view
        // pager preloading won't create any adapter for the recyclerview
            setupPopPhotosGrid();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            loadPhotoInfo();
        }
    }

    private void loadPhotoInfo() {
        mPhotos = PhotoCacheUtils.getPhotoInfo(mCategory, DayUtils.getDay());
        if (mPhotos == null) { //first time to request data
            requestPhotos(mCategory.name(), DayUtils.getDay());
        }

        else { //data already exists, check if the photos shown are desired, if not, need refresh
            if (DayUtils.needRefresh(mCategory)) {
                mGridPhotosAdapter.resetListData(getImageUrlList());
            }
            updateMap(getVisiblePhotoPositions());
        }
        DayUtils.setRefreshMap(mCategory);
    }

    private void requestPhotos(String category, String day) {
        final View progressbar = getActivity().findViewById(R.id.progressbar);
        progressbar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);

        HashMap<String, String> queryParameters = new HashMap<>();
        queryParameters.put("day", day);
        queryParameters.put("category", category);

        final Category cat = Category.valueOf(category);
        Call<List<Photo>> call = service.requestPhotos(queryParameters);
        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Response<List<Photo>> response, Retrofit retrofit) {
                try {
                    mPhotos = response.body();
                } catch (Exception e) {
                    mPhotos = null;
                }
                PhotoCacheUtils.setPhotoInfo(cat, DayUtils.getDay(), mPhotos);
                progressbar.setVisibility(View.GONE);
                setupPopPhotosGrid();
                updateMap(getVisiblePhotoPositions());
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private void setupPopPhotosGrid() {
        mPopPhotoRecyclerView.setVisibility(View.VISIBLE);
        List<String> imageUrls = getImageUrlList();
        if (imageUrls == null)
            mGridPhotosAdapter = new GridPhotosAdapter(getActivity(), PHOTO_PER_ROW, mCategory,
                    Arrays.asList
                    (getActivity()
                    .getResources().getStringArray(R.array.user_photos)));
        else
            mGridPhotosAdapter = new GridPhotosAdapter(getActivity(), PHOTO_PER_ROW, mCategory,
                    imageUrls);
        mPopPhotoRecyclerView.setAdapter(mGridPhotosAdapter);
        mPopPhotoRecyclerView.setItemAnimator(new SlideInDownAnimator());//set recyclerview animator
        mPopPhotoRecyclerView.getItemAnimator().setRemoveDuration(200);

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
        Handler handler = new Handler();
        boolean initialized = MapUtils.getAppStatus();
        int delay = MAP_UPDATE_INTERVAL;
        if (!initialized) {
            delay = MAP_INITIAL_DELAY;
            MapUtils.setAppStatus(true);
        }
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

    private List<String> getImageUrlList() {
        List<String> urlList = new ArrayList<>();
        if (mPhotos == null)
            return null;
        for (Photo photo : mPhotos)
            urlList.add(photo.getImageUrl());
        return urlList;
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

}
