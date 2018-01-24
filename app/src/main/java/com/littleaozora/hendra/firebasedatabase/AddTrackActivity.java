package com.littleaozora.hendra.firebasedatabase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddTrackActivity extends AppCompatActivity {
    private TextView artistName;
    private EditText trackName;
    private SeekBar rating;
    private Button btnSave;
    private ListView listView;

    DatabaseReference databaseTracks;

    List<Track> tracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);

        artistName = (TextView) findViewById(R.id.artistName);
        trackName = (EditText) findViewById(R.id.trackName);
        rating = (SeekBar) findViewById(R.id.rating);
        btnSave = (Button) findViewById(R.id.btnSave);
        listView = (ListView) findViewById(R.id.listViewTracks);

        Intent intent = getIntent();

        String id = intent.getStringExtra(MainActivity.ARTIST_ID);
        String name = intent.getStringExtra(MainActivity.ARTIST_NAME);

        artistName.setText(name);

        databaseTracks = FirebaseDatabase.getInstance().getReference("tracks").child(id);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTrack();
            }
        });

        tracks = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseTracks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tracks.clear();
                for(DataSnapshot trackSnapshot : dataSnapshot.getChildren()){
                    Track track = trackSnapshot.getValue(Track.class);
                    tracks.add(track);
                }

                TrackList trackListAdapter = new TrackList(AddTrackActivity.this, tracks);
                listView.setAdapter(trackListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addTrack() {
        String name = trackName.getText().toString().trim();
        int trackRating = rating.getProgress();

        if(!TextUtils.isEmpty(name)){
            String id = databaseTracks.push().getKey();

            Track track = new Track(id, name, trackRating);
            databaseTracks.child(id).setValue(track);
            Toast.makeText(this, "Add New Track Succesfully", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Name is empty!!!!", Toast.LENGTH_SHORT).show();
        }
    }
}
