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

import com.example.mobile30_03.R;
import com.example.mobile30_03.database.AppDatabase;
import com.example.mobile30_03.database.RPlaylist;
import com.example.mobile30_03.database.RSong;
import com.example.mobile30_03.databinding.FragmentPlaylistBinding;
import com.example.mobile30_03.utils.HelperFunctions;
import com.example.mobile30_03.utils.PlaylistsAdapter;

import java.util.List;

public class PlaylistFragment extends Fragment {
    private String TAG = "PlaylistFragment";
    private FragmentPlaylistBinding binding;
    public PlaylistFragment() {}
    List<RPlaylist> playlists;
    AppDatabase db = AppDatabase.getInstance(getContext());
    PlaylistsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        //Action bar
        if (activity != null) {
            activity.setSupportActionBar(binding.tbPlaylists);
        }

        adapter = new PlaylistsAdapter(db.wpDao().getAllPlaylists());
        binding.rvPlaylists.setAdapter(adapter);
        binding.rvPlaylists.setHasFixedSize(true);
        binding.rvPlaylists.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(getContext()));

        return binding.getRoot();
    }

    public void updateData(){
        playlists = db.wpDao().getAllPlaylists();
        adapter.setPlaylists(playlists);
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
                View view3 = LayoutInflater.from(getContext()).inflate(R.layout.popup_new_playlist, null);
                popupWindow.setContentView(view3);
                popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(binding.getRoot(), Gravity.CENTER, 0, 0);

                EditText etNameInput = view3.findViewById(R.id.etNameInput);
                EditText etDescInput = view3.findViewById(R.id.etDescInput);
                view3.findViewById(R.id.btnCreate).setOnClickListener(v -> {
                    String name = etNameInput.getText().toString();
                    String desc = etDescInput.getText().toString();
                    if (name.isEmpty() || desc.isEmpty()) {
                        Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        AppDatabase db = AppDatabase.getInstance(requireContext());
                        RPlaylist rPlaylist = new RPlaylist(name, desc);
                        db.wpDao().insertPlaylist(rPlaylist);
                        Toast.makeText(getContext(), "Playlist created", Toast.LENGTH_SHORT).show();
                        updateData();
                        popupWindow.dismiss();
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}