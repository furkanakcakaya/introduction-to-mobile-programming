package com.example.mobile30_03.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile30_03.R;
import com.example.mobile30_03.database.AppDatabase;
import com.example.mobile30_03.database.RPlaylist;
import com.example.mobile30_03.database.RSong;
import com.example.mobile30_03.databinding.FragmentSongsBinding;
import com.example.mobile30_03.utils.MediaPlayerManager;
import com.example.mobile30_03.utils.SongsAdapter;

import java.util.List;

public class SongsFragment extends Fragment {
    private FragmentSongsBinding binding;

    private static final String TAG = "SongsFragment";

    private RSong[] listSongs;
    private SongsAdapter mAdapter;
    private RecyclerView mSongList;
    public SongsFragment() {}

    //Create MediaPlayerManager
    private MediaPlayerManager mediaPlayerManager = MediaPlayerManager.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentSongsBinding.inflate(inflater,container,false);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        //Action bar
        if (activity != null) {
            activity.setSupportActionBar(binding.tbSongs);
        }
        binding.tbSongs.setTitle("Songs");


        mSongList = binding.rvSongs;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mSongList.setLayoutManager(layoutManager);
        mSongList.setHasFixedSize(true);
        List<RSong> songs = AppDatabase.getInstance(requireContext()).wpDao().getAllSongs();
        mAdapter = new SongsAdapter(songs);
        mSongList.setAdapter(mAdapter);

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
        inflater.inflate(R.menu.songs_fragment_toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionUpdate:
                MediaPlayerManager.getInstance().updateSonglist(requireContext());
                mAdapter.setSongs(AppDatabase.getInstance(requireContext()).wpDao().getAllSongs());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}