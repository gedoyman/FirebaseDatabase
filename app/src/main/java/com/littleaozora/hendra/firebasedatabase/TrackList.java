package com.littleaozora.hendra.firebasedatabase;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Hendra on 1/23/2018.
 */

public class TrackList extends ArrayAdapter<Track> {
    private Activity context;
    private List<Track> tracks;

    public TrackList(Activity  context, List<Track> tracks){
        super(context, R.layout.list_track, tracks);
        this.context = context;
        this.tracks = tracks;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_track, null, true);

        TextView txtTrackName = (TextView) listViewItem.findViewById(R.id.txtTrackName);
        TextView txtTrackRating = (TextView) listViewItem.findViewById(R.id.txtTrackRating);

        Track track = tracks.get(position);

        txtTrackName.setText(track.getTrackName());
        txtTrackRating.setText(String.valueOf(track.getTrackRating()));

        return listViewItem;
    }
}
