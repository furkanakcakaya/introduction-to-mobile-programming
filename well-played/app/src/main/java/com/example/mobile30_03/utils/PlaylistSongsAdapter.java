package com.example.mobile30_03.utils;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile30_03.R;
import com.example.mobile30_03.database.AppDatabase;
import com.example.mobile30_03.database.RSong;
import com.example.mobile30_03.databinding.CSongItemBinding;
import com.example.mobile30_03.fragment.PlaylistSongFragmentDirections;
import com.example.mobile30_03.fragment.SongsFragmentDirections;

import java.util.List;

public class PlaylistSongsAdapter extends RecyclerView.Adapter<PlaylistSongsAdapter.SongViewHolder> {
    private List<RSong> mSongs;
    private int playlistId;

    public PlaylistSongsAdapter(List<RSong> mSongs, int playlistId) {
        this.mSongs = mSongs;
        this.playlistId = playlistId;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CSongItemBinding binding = CSongItemBinding.inflate(inflater, parent, false);
        return new PlaylistSongsAdapter.SongViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        RSong song = mSongs.get(position);
        holder.bind(song);
    }

    @Override
    public int getItemCount() {
        return mSongs.size();
    }

    public void setmSongs(List<RSong> songs) {
        this.mSongs.clear();
        this.mSongs.addAll(songs);
        notifyDataSetChanged();
    }

    class SongViewHolder extends RecyclerView.ViewHolder{
        CSongItemBinding binding;
        public SongViewHolder(CSongItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(RSong song) {
            binding.ivAlbumArt.setImageBitmap(HelperFunctions
                    .getBitmapFromContentURI(binding.getRoot().getContext(), Uri.parse(song.uri)));
            binding.tvSongName.setText(song.name);
            binding.tvDuration.setText(HelperFunctions.milliSecondsToTimer(song.duration));
            binding.tvArtistName.setText(song.artist);

            binding.ivSongMore.setImageResource(R.drawable.delete_clip);

            binding.ivSongMore.setOnClickListener(v -> {
                AppDatabase.getInstance(binding.getRoot().getContext()).wpDao().deleteSongFromPlaylist(playlistId, song.songId);
                setmSongs(AppDatabase.getInstance(binding.getRoot().getContext()).wpDao().getSongsOfPlaylist(playlistId).get(0).rSongs);
            });

            binding.cvAddSong.setOnClickListener(v -> {
                MediaPlayerManager.getInstance().setPlaylist(playlistId, v.getContext());
                NavDirections action = PlaylistSongFragmentDirections.actionPlaylistSongToPlayer().setSongIndex(getAdapterPosition());
                Navigation.findNavController(v).navigate(action);
            });

        }
    }

}
