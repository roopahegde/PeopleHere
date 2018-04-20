package com.foursquare.takehome;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvRecyclerView;
    private PersonAdapter personAdapter;
    private TextView venueName;
    private TextView venueTime;
    private SimpleDateFormat formatter = new SimpleDateFormat("MM/dd h:mm a");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        venueName = findViewById(R.id.venueNameTxt);
        venueTime = findViewById(R.id.venueTimeTxt);
        rvRecyclerView = findViewById(R.id.rvRecyclerView);

        //Setup RecyclerView look and feel and data adapter.
        personAdapter = new PersonAdapter(new ArrayList<Person>(0), this);
        DividerItemDecoration itemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rvRecyclerView.setAdapter(personAdapter);
        rvRecyclerView.addItemDecoration(itemDecoration);

        //Fetch data
        new VenueFetcher(this).execute();
    }

    /*
     * Given the venue data with the list of visitors setup display the data to the user
     */
    private void setupData(Venue venue) {

        Util util = new Util(venue.getVisitors());
        //Get visitor list along with venue idle time slots and set it to adapter
        personAdapter.updateVisitorList(
                util.getVisitorsAndIdleTime(venue.getOpenTime(), venue.getCloseTime()));
        //Set venue name
        venueName.setText(venue.getName());

        //Set venue timing
        String openTime = formatter.format(venue.getOpenTime());
        String closeTime = formatter.format(venue.getCloseTime());
        venueTime.setText(getString(R.string.timestamp, openTime, closeTime));
    }

    /**
     * Fakes a data fetch and parses json from assets/people.json
     */
    private static class VenueFetcher extends AsyncTask<Void, Void, Venue> {
        private final WeakReference<MainActivity> activityWeakReference;

        public VenueFetcher(MainActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        protected Venue doInBackground(Void... params) {
            return activityWeakReference != null ? VenueStore.get().getVenue(activityWeakReference.get()) : null;
        }

        @Override
        protected void onPostExecute(Venue venue) {
            if (activityWeakReference == null || venue == null) {
                return;
            }
            MainActivity mainActivity = activityWeakReference.get();
            //Setup and display data to user.
            mainActivity.setupData(venue);
        }
    }

}
