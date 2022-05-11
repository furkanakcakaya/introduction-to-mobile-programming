package com.example.mobile30_03.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile30_03.database.AppDatabase;
import com.example.mobile30_03.database.RPlaylist;
import com.example.mobile30_03.database.RSong;
import com.example.mobile30_03.database.relations.PlaylistsWithSongs;
import com.example.mobile30_03.databinding.CPlaylistItemBinding;
import com.example.mobile30_03.fragment.PlaylistFragmentDirections;
import com.example.mobile30_03.fragment.SongsFragmentDirections;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class PlaylistsAdapter extends RecyclerView.Adapter<PlaylistsAdapter.PlaylistViewHolder> {
    final List<RPlaylist> playlists;

    public PlaylistsAdapter(List<RPlaylist> playlists) {
        this.playlists = playlists;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CPlaylistItemBinding binding = CPlaylistItemBinding.inflate(inflater, parent, false);
        return new PlaylistViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        RPlaylist playlist = playlists.get(position);
        holder.bind(playlist);
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public void setPlaylists(List<RPlaylist> playlists) {
        this.playlists.clear();
        this.playlists.addAll(playlists);
        notifyDataSetChanged();
    }


    class PlaylistViewHolder extends RecyclerView.ViewHolder {
        CPlaylistItemBinding binding;
        public PlaylistViewHolder(@NonNull CPlaylistItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(RPlaylist playlist) {
            binding.ivPlaylist.setImageResource(playlist.artworkUrl);
            binding.tvPlaylistName.setText(playlist.name);
            binding.tvPlaylistDescription.setText(playlist.description);
            binding.tvPlaylistCount.setText(String.valueOf(playlist.playlistId));
            binding.ivDelete.setOnClickListener(v -> {
                Snackbar.make(v, "Are you sure you want to delete playlist?", Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.RED)
                        .setAction("DELETE", v1 -> {
                            Context context = v.getContext();
                            AppDatabase db = AppDatabase.getInstance(context);
                            db.wpDao().deletePlaylist(playlist);
                            setPlaylists(db.wpDao().getAllPlaylists());
                        })
                        .show();
            });

            binding.cvAddPlaylist.setOnClickListener(v -> {
                NavDirections action = PlaylistFragmentDirections.playlistToInside(playlist.playlistId);
                Navigation.findNavController(v).navigate(action);

                MediaPlayerManager.getInstance().setPlaylist(playlist.playlistId, v.getContext());
//                NavDirections action = SongsFragmentDirections.songsToPlayer().setSongIndex(0);
//                Navigation.findNavController(v.getRootView()).navigate(action);
            });
        }
    }
}
