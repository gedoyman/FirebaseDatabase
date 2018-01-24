package com.littleaozora.hendra.firebasedatabase;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Hendra on 1/21/2018.
 */

public class ArtistList extends ArrayAdapter<Artist>{
    private Activity context;
    private List<Artist> artistList;

    public ArtistList(Activity  context, List<Artist> artistList){
        super(context, R.layout.list_layout, artistList);
        this.context = context;
        this.artistList = artistList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        TextView txtArtisNama = (TextView) listViewItem.findViewById(R.id.txtArtisNama);
        TextView txtArtisGendre = (TextView) listViewItem.findViewById(R.id.txtArtisGendre);

        Artist artist = artistList.get(position);

        txtArtisNama.setText(artist.getArtistName());
        txtArtisGendre.setText(artist.getArtistGenre());

        return listViewItem;
    }
}
