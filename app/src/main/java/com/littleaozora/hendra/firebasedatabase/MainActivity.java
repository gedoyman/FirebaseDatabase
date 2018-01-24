package com.littleaozora.hendra.firebasedatabase;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String ARTIST_NAME = "artistName";
    public static final String ARTIST_ID = "artistId";

    private static final String TAG = "Main Activity";
    private EditText artistName;
    private Spinner artistGenre;
    private Button btnSave;
    private ListView listView;
    List<Artist> artists;

    DatabaseReference databaseArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        artistName = (EditText) findViewById(R.id.artistName);
        artistGenre = (Spinner) findViewById(R.id.artistGenre);

        btnSave = (Button) findViewById(R.id.btnSave);
        listView = (ListView) findViewById(R.id.listViewArtist);
        artists = new ArrayList<>();

        databaseArtist = FirebaseDatabase.getInstance().getReference("artist");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, ": onClick btnSave");
                addArtist();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = artists.get(position);

                Intent intent =  new Intent(getApplicationContext(), AddTrackActivity.class);

                intent.putExtra(ARTIST_NAME, artist.getArtistName());
                intent.putExtra(ARTIST_ID, artist.getArtistID());

                startActivity(intent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = artists.get(position);

                String artistId = artist.getArtistID();
                String artistName = artist.getArtistName();
                String artistGenre = artist.getArtistGenre();

                showUpdateDialog(artistId, artistName, artistGenre);
                return false;
            }
        });

    }

    private void showUpdateDialog(final String artistId, String artistName, String artistGenre){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog, null);

        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.txtNewName);
        final Spinner editGenre = (Spinner) dialogView.findViewById(R.id.newArtistGenre);
        final Button btnUpdate = (Button) dialogView.findViewById(R.id.btnUpdate);
        final Button btnDelete = (Button) dialogView.findViewById(R.id.btnDelete);

        dialogBuilder.setTitle("Update Artist : "+artistName);
        editTextName.setText(artistName);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String genre = editGenre.getSelectedItem().toString();

                if(TextUtils.isEmpty(name)){
                    editTextName.setError("Name required !");
                    return;
                }

                updateArtist(artistId, name, genre);
                alertDialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteArtist(artistId);
                alertDialog.dismiss();
            }
        });
    }

    private boolean updateArtist(String artistId, String artistName, String artistGenre){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("artist").child(artistId);
        Artist artist = new Artist(artistId, artistName, artistGenre);
        databaseReference.setValue(artist);
        Toast.makeText(this, "Update succcescfully", Toast.LENGTH_SHORT).show();
        return true;
    }

    private void deleteArtist(String artistId){
        DatabaseReference drArtist = FirebaseDatabase.getInstance().getReference("artist").child(artistId);
        DatabaseReference drTracks = FirebaseDatabase.getInstance().getReference("tracks").child(artistId);

        drArtist.removeValue();
        drTracks.removeValue();

        Toast.makeText(this, "Delete succcescfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseArtist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                artists.clear();

                for(DataSnapshot artisSnapshot : dataSnapshot.getChildren()){
                    Artist artist = artisSnapshot.getValue(Artist.class);

                    artists.add(artist);
                }

                ArtistList adapter = new ArtistList(MainActivity.this, artists);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addArtist(){
        String name = artistName.getText().toString().trim();
        String genre = artistGenre.getSelectedItem().toString();

        if(!TextUtils.isEmpty(name)){
            String id_system = databaseArtist.push().getKey();
            Artist artist = new Artist(id_system, name, genre);
            databaseArtist.child(id_system).setValue(artist);
            Toast.makeText(this, "Add succcescfully", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Name is empty!!!!", Toast.LENGTH_SHORT).show();
        }
    }
}
