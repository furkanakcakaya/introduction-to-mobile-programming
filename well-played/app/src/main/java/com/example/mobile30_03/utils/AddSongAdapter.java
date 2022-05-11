package com.example.mobile30_03.utils;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile30_03.database.AppDatabase;
import com.example.mobile30_03.database.RSong;
import com.example.mobile30_03.database.relations.SongPlaylistCrossRef;
import com.example.mobile30_03.databinding.CAddSongItemBinding;
import com.example.mobile30_03.databinding.CSongItemBinding;

import java.util.List;

public class AddSongAdapter extends RecyclerView.Adapter<AddSongAdapter.SongViewHolder> {
    private List<RSong> mSongs;
    private final int playlistId;

    public AddSongAdapter(List<RSong> mSongs, int playlistId) {
        this.mSongs = mSongs;
        this.playlistId = playlistId;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CAddSongItemBinding binding = CAddSongItemBinding.inflate(inflater, parent, false);
        return new SongViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        holder.bind(mSongs.get(position));
    }

    @Override
    public int getItemCount() {
        return mSongs.size();
    }

    class SongViewHolder extends RecyclerView.ViewHolder{
        CAddSongItemBinding binding;
        public SongViewHolder(CAddSongItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(RSong rSong) {
            binding.tvAddSongName.setText(rSong.name);
            binding.tvAddArtist.setText(rSong.artist);
            binding.ivAddArt.setImageBitmap(HelperFunctions
                    .getBitmapFromContentURI(binding.getRoot().getContext(), Uri.parse(rSong.uri)));

            binding.cvAddSong.setOnClickListener(v -> {
                try {
                    AppDatabase.getInstance(v.getContext()).wpDao()
                            .insertSongPlaylistCrossRef(new SongPlaylistCrossRef(rSong.songId, playlistId));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
