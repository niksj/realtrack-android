package com.hackforchange.views.activities;

import java.util.ArrayList;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.hackforchange.R;
import com.hackforchange.backend.activities.ParticipationDAO;
import com.hackforchange.models.activities.Participation;

public class PendingParticipationActivity extends SherlockActivity {
    private ArrayList<Participation> unservicedParticipation_data;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_pendingparticipation);
    }

    @Override
    public void onResume() {
        super.onResume();
        ParticipationDAO pDao = new ParticipationDAO(getApplicationContext());
        unservicedParticipation_data = pDao.getAllUnservicedParticipations();

        PendingParticipationListAdapter listAdapter = new PendingParticipationListAdapter(this, R.layout.row_pendingparticipation, unservicedParticipation_data);
        ListView participationitemslist = (ListView) findViewById(R.id.pendingparticipationlistView);
        participationitemslist.setAdapter(listAdapter);
        participationitemslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Participation p = unservicedParticipation_data.get(position);
                // if the user is already on the pending participations screen when a notification pops up or comes to it
                // from the home screen's "Pending" button (and not by clicking the notifification), clear the corresponding
                // notification
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.cancel(p.getId());
                // clicking on the item must take the user to the record participation activity
                Intent newActivity = new Intent(PendingParticipationActivity.this, RecordParticipationActivity.class);
                newActivity.putExtra("participationid", p.getId());
                newActivity.putExtra("datetime", p.getDate());
                startActivity(newActivity);
            }
        });
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // provide a back button on the actionbar
                finish();
                break;
        }
        return true;
    }
}