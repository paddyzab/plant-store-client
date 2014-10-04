package com.plantcare.app.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import com.google.common.collect.Lists;
import com.plantcare.app.R;
import com.plantcare.app.activities.PlantDetailsActivity;
import com.plantcare.app.adapters.PlantAdapter;
import com.plantcare.app.storage.greendao.model.DaoMaster;
import com.plantcare.app.storage.greendao.model.DaoSession;
import com.plantcare.app.storage.greendao.model.PlantObject;
import com.plantcare.app.storage.greendao.model.PlantObjectDao;
import com.plantcare.app.utils.IntentKeys;
import com.plantcare.app.views.EndlessGridView;
import java.util.List;

public class PlantsFeedFragment extends Fragment implements OnItemClickListener {

    private final static String TAG = PlantsFeedFragment.class.getSimpleName();

    private PlantAdapter plantsAdapter;

    public static PlantsFeedFragment newInstance(final String stringToken) {
        final PlantsFeedFragment fragment = new PlantsFeedFragment();
        final Bundle args = new Bundle();
        args.putString(IntentKeys.TOKEN_KEY, stringToken);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        final DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(container.getContext(), "PLANT_OBJECT", null);
        final SQLiteDatabase db = helper.getWritableDatabase();
        final DaoMaster daoMaster = new DaoMaster(db);
        final DaoSession daoSession = daoMaster.newSession();
        final PlantObjectDao plantDao = daoSession.getPlantObjectDao();

        final View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        final EndlessGridView endlessGridViewPlants = (EndlessGridView) rootView.findViewById(R.id.endlessListViewPlants);
        final ProgressBar progressBarLoading = (ProgressBar) rootView.findViewById(R.id.progressBarLoading);
        endlessGridViewPlants.setOnItemClickListener(this);

        plantsAdapter = new PlantAdapter(container.getContext(), Lists.<PlantObject>newArrayList());
        endlessGridViewPlants.setAdapter(plantsAdapter);
        final List<PlantObject> plantObjects = plantDao.loadAll();

        if (plantObjects.size() > 0) {
            plantsAdapter.addAll(plantObjects);
            plantsAdapter.notifyDataSetChanged();
            progressBarLoading.setVisibility(View.GONE);
        } else {
            //TODO notify user that amount of data is too damn low! Probably with some view to invite creation
            progressBarLoading.setVisibility(View.GONE);
        }

        return rootView;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        final Intent intent = new Intent(getActivity(), PlantDetailsActivity.class);
        intent.putExtra(IntentKeys.PLANT_KEY, plantsAdapter.getItem(position).getId());
        startActivity(intent);
    }
}
