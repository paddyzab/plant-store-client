package com.platncare.app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import client.model.Plant;
import com.platncare.app.R;
import com.squareup.timessquare.CalendarPickerView;
import java.util.Calendar;
import java.util.Date;

public class CalendarFragment extends Fragment {

    private static final String PLANT_KEY = "plant";

    public static CalendarFragment newInstance(Plant plant) {

        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putSerializable(PLANT_KEY, plant);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        CalendarPickerView calendar = (CalendarPickerView) rootView.findViewById(R.id.calendar_view);
        Date today = new Date();
        calendar.init(today, nextYear.getTime())
                .withSelectedDate(today);

        return rootView;
    }
}
