package com.example.mobile30_03.utils;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile30_03.database.AppDatabase;
import com.example.mobile30_03.database.RPlaylist;
import com.example.mobile30_03.database.RSong;
import com.example.mobile30_03.database.relations.SongPlaylistCrossRef;
import com.example.mobile30_03.databinding.CAddItemBinding;

import java.util.List;

public class AddPlaylistAdapter extends RecyclerView.Adapter<AddPlaylistAdapter.ViewHolder> {
    private static final String TAG = "AddPlaylistAdapter";
    final List<RPlaylist> playlists;
    final RSong song;
    final PopupWindow popupWindow;
    AppDatabase db = AppDatabase.getInstance(null);

    public AddPlaylistAdapter(RSong song, PopupWindow popupWindow) {
        this.song = song;
        this.playlists = db.wpDao().getAllPlaylists();
        this.popupWindow = popupWindow;
        //TODO: add a listener to update the list when a playlist is added
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CAddItemBinding binding = CAddItemBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RPlaylist playlist = playlists.get(position);
        holder.bind(playlist);
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CAddItemBinding binding;

        public ViewHolder(CAddItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(RPlaylist playlist) {
            binding.tvAddPlaylist.setText(playlist.name);
            binding.cvAddPlaylist.setOnClickListener(v -> {
                AppDatabase db = AppDatabase.getInstance(v.getContext());
                db.wpDao().insertSongPlaylistCrossRef(new SongPlaylistCrossRef(song.songId, playlist.playlistId));
                popupWindow.dismiss();
            });
        }
    }
}
