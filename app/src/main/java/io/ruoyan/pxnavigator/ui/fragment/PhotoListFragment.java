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
import io.ruoyan.pxnavigator.utils.PhotoCacheUtils;
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
        if (PhotoCacheUtils.getPhotoInfo(mCategory) != null)
            setupPopPhotosGrid();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
            loadPhotoInfo();
    }

    private void loadPhotoInfo() {
        mPhotos = PhotoCacheUtils.getPhotoInfo(mCategory);
        if (mPhotos == null) {
            requestPhotos("1", mCategory.name());
        }

    }

    private void requestPhotos(String day, String category) {
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
                PhotoCacheUtils.setPhotoInfo(cat, mPhotos);
                progressbar.setVisibility(View.GONE);
                setupPopPhotosGrid();
                int[] visiblePhotoPositions = {0, 2*PHOTO_PER_ROW-1};
                updateMap(visiblePhotoPositions, 1000);
            }

            @Override
            public void onFailure(Throwable t) {}
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
        mLayoutManager = new StaggeredGridLayoutManager(PHOTO_PER_ROW,
                StaggeredGridLayoutManager.VERTICAL);
        mPopPhotoRecyclerView.setLayoutManager(mLayoutManager);
        mPopPhotoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                mGridPhotosAdapter.setLockedAnimations(true);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    final int[] visiblePhotoPositions = getVisiblePhotoPositions();
                    updateMap(visiblePhotoPositions, 200);
                }
            }

        });
    }

    private void updateMap(final int[] visiblePhotoPositions, final int delay) {
        final MapManager manager = MapManager.getMapManager(getActivity());
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                manager.generateCluster(mCategory, visiblePhotoPositions);
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
        mLayoutManager.findFirstCompletelyVisibleItemPositions(firstVisiblePhotos);
        int[] result = new int[2];
        result[0] = firstVisiblePhotos[0];
        result[1] = result[0]==PHOTO_PER_CATEGORY-PHOTO_PER_ROW-PHOTO_PER_CATEGORY%PHOTO_PER_ROW
                ?PHOTO_PER_CATEGORY-1:result[0]+PHOTO_PER_ROW-1;
        return result;
    }

}
