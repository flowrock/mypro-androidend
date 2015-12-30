package io.ruoyan.pxnavigator.ui.fragment;

import android.os.Bundle;
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
import io.ruoyan.pxnavigator.utils.PhotoInfoCacheUtils;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by ruoyan on 12/20/15.
 */
public class PhotoListFragment extends Fragment {
    public static final int PHOTO_PER_ROW = 3;
    public static final String EXTRA_CATEGORY = "extra_category";
    public static final String BASE_URL = "http://159.203.117.77";

    @InjectView(R.id.popPhotoRecyclerView)
    RecyclerView mPopPhotoRecyclerView;

    private GridPhotosAdapter mGridPhotosAdapter;
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
        if (PhotoInfoCacheUtils.getPhotoInfo(mCategory) != null)
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
        mPhotos = PhotoInfoCacheUtils.getPhotoInfo(mCategory);
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
        queryParameters.put("day",day);
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
                PhotoInfoCacheUtils.setPhotoInfo(cat, mPhotos);
                progressbar.setVisibility(View.GONE);
                setupPopPhotosGrid();
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
            mGridPhotosAdapter = new GridPhotosAdapter(getActivity(), PHOTO_PER_ROW, Arrays.asList
                    (getActivity()
                    .getResources().getStringArray(R.array.user_photos)));
        else
            mGridPhotosAdapter = new GridPhotosAdapter(getActivity(), PHOTO_PER_ROW, imageUrls);
        mPopPhotoRecyclerView.setAdapter(mGridPhotosAdapter);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(PHOTO_PER_ROW,
                StaggeredGridLayoutManager.VERTICAL);
        mPopPhotoRecyclerView.setLayoutManager(layoutManager);
        mPopPhotoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                mGridPhotosAdapter.setLockedAnimations(true);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    List<Integer> visiblePhotoPositions = getVisiblePhotoPositions(layoutManager);
                }
            }

        });
    }

    private List<String> getImageUrlList() {
        List<String> urlList = new ArrayList<>();
        if (mPhotos == null)
            return null;
        for (Photo photo : mPhotos)
            urlList.add(photo.getImageUrl());
        return urlList;
    }

    private List<Integer> getVisiblePhotoPositions(StaggeredGridLayoutManager layoutManager) {
        int[] firstVisiblePhotos = new int[PHOTO_PER_ROW];
        layoutManager.findFirstCompletelyVisibleItemPositions(firstVisiblePhotos);
        int[] lastVisiblePhotos = new int[PHOTO_PER_ROW];
        layoutManager.findLastCompletelyVisibleItemPositions(lastVisiblePhotos);
        List<Integer> result = new ArrayList<>();
        result.add(firstVisiblePhotos[0]);
        result.add(lastVisiblePhotos[PHOTO_PER_ROW-1] > lastVisiblePhotos[0] ?
                lastVisiblePhotos[PHOTO_PER_ROW-1] : lastVisiblePhotos[0]);
        return result;
    }

}
