package com.example.mobile30_03.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.mobile30_03.R;
import com.example.mobile30_03.database.AppDatabase;
import com.example.mobile30_03.database.RPlaylist;
import com.example.mobile30_03.database.RSong;
import com.example.mobile30_03.databinding.FragmentPlaylistSongBinding;
import com.example.mobile30_03.utils.AddSongAdapter;
import com.example.mobile30_03.utils.PlaylistSongsAdapter;
import com.example.mobile30_03.utils.SongsAdapter;

import java.util.List;

public class PlaylistSongFragment extends Fragment {
    FragmentPlaylistSongBinding binding;
    PlaylistSongsAdapter adapter;
    AppDatabase db = AppDatabase.getInstance(getContext());
    int playlistId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentPlaylistSongBinding.inflate(inflater, container, false);
        playlistId = getArguments().getInt("playlistId");

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        //Action bar
        if (activity != null) {
            activity.setSupportActionBar(binding.tbPlaylistInside);
        }

        RPlaylist playlist = db.wpDao().getPlaylistById(playlistId);
        binding.tbPlaylistInside.setTitle(playlist.name);

        List<RSong> songs = db.wpDao().getSongsOfPlaylist(playlistId).get(0).rSongs;
        if (songs.size() > 0) {
            adapter = new PlaylistSongsAdapter(songs, playlistId);
            binding.rvPlaylistSongs.setAdapter(adapter);
            binding.rvPlaylistSongs.setHasFixedSize(true);
            binding.rvPlaylistSongs.setLayoutManager(new androidx.recyclerview.widget
                    .LinearLayoutManager(getContext()));
        }else{
            Toast.makeText(getContext(), "No songs in this playlist", Toast.LENGTH_SHORT).show();
        }
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.playlist_fragment_toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionAdd:
                PopupWindow popupWindow = new PopupWindow(getContext());
                View popupView = getLayoutInflater().inflate(R.layout.add_song_to_playlist, null);
                popupWindow.setContentView(popupView);
                popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(binding.getRoot(), Gravity.CENTER, 0, 0);
                Toolbar tbPopup = popupView.findViewById(R.id.toolbar);
                tbPopup.setTitle("Add to playlist");

                RecyclerView rv = popupView.findViewById(R.id.rvAddPlaylist);
                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                rv.setHasFixedSize(true);
                List<RSong> songs = db.wpDao().getAllSongs();
                AddSongAdapter adapter = new AddSongAdapter(songs, playlistId);
                rv.setAdapter(adapter);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}