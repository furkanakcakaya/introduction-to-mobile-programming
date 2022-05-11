package com.example.mobile30_03.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile30_03.R;
import com.example.mobile30_03.database.RPlaylist;
import com.example.mobile30_03.database.RSong;
import com.example.mobile30_03.fragment.SongsFragmentDirections;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.MusicViewHolder> {
    private static final String TAG = "SongsAdapter";
    private final List<RSong> songs;
    private final int mNumberItems;

    public SongsAdapter(List<RSong> songs) {
        this.songs = songs;
        mNumberItems = songs.size();
    }

    public void setSongs(List<RSong> songs) {
        this.songs.clear();
        this.songs.addAll(songs);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.c_song_item, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        RSong music = songs.get(position);
        holder.ivAlbumArt.setImageBitmap(HelperFunctions.getBitmapFromContentURI(holder.itemView.getContext(), Uri.parse(music.uri)));
        holder.tvSongName.setText(music.name);
        holder.tvDuration.setText(HelperFunctions.milliSecondsToTimer(music.duration));
        holder.tvArtistName.setText(music.artist);

        holder.ivSongMore.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.ivSongMore);
            popupMenu.getMenuInflater().inflate(R.menu.song_more_popup, popupMenu.getMenu());
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.action_add_to_playlist:
                        PopupWindow popupWindow = new PopupWindow(holder.itemView.getContext());
                        View view2 = LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.add_to_playlist, null);
                        popupWindow.setContentView(view2);
                        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                        popupWindow.setFocusable(true);
                        popupWindow.showAsDropDown(holder.ivSongMore);

                        RecyclerView rv = view2.findViewById(R.id.rvAddPlaylist);
                        rv.setAdapter(new AddPlaylistAdapter(music, popupWindow));
                        rv.setHasFixedSize(true);
                        rv.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(holder.itemView.getContext()));

                        break;
                    case R.id.action_share:
                        shareSong(holder.itemView.getContext(), Uri.parse(music.uri));
                        break;
                    case R.id.action_delete:
                        Snackbar.make(holder.itemView, "Are you sure you want to delete " + music.name+" ?", Snackbar.LENGTH_LONG)
                                .setActionTextColor(Color.RED)
                                .setAction("Yes, delete it", view1 -> {
                                    deleteSong(holder.itemView.getContext(), Uri.parse(music.uri));
                                }).show();

                        break;
                    default:
                        break;
                }
                return false;
            });
            popupMenu.show();
        });

        holder.itemView.setOnClickListener(view -> {
            NavDirections action = SongsFragmentDirections.songsToPlayer().setSongIndex(position);
            Navigation.findNavController(view).navigate(action);
        });

    }

    private void deleteSong(Context context, Uri uri) {
        //delete song from device if it is below android 11
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            try {
                ContentResolver contentResolver = context.getContentResolver();
                contentResolver.delete(uri, null, null);
                Toast.makeText(context, "Song deleted", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    //A function that sends an intent to share a song file
    public void shareSong(Context context, Uri uri) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("audio/*");
        context.startActivity(Intent.createChooser(shareIntent, "Share the song"));
    }

    class MusicViewHolder extends RecyclerView.ViewHolder {
        TextView tvSongName;
        TextView tvDuration;
        TextView tvArtistName;
        ImageView ivAlbumArt;
        ImageView ivSongMore;
        public MusicViewHolder(View itemView) {
            super(itemView);
            tvSongName = (TextView) itemView.findViewById(R.id.tvSongName);
            tvDuration = (TextView) itemView.findViewById(R.id.tvDuration);
            tvArtistName = (TextView) itemView.findViewById(R.id.tvArtistName);
            ivAlbumArt = (ImageView) itemView.findViewById(R.id.ivAlbumArt);
            ivSongMore = (ImageView) itemView.findViewById(R.id.ivSongMore);
        }
    }
}

