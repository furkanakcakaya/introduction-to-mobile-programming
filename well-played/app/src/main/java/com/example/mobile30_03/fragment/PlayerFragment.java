package com.example.mobile30_03.fragment;

import static com.example.mobile30_03.utils.HelperFunctions.milliSecondsToTimer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.fragment.app.Fragment;

import com.example.mobile30_03.R;
import com.example.mobile30_03.database.RSong;
import com.example.mobile30_03.databinding.FragmentPlayerBinding;
import com.example.mobile30_03.utils.HelperFunctions;
import com.example.mobile30_03.utils.MediaPlayerManager;

public class PlayerFragment extends Fragment {
    public PlayerFragment() {}
    private static final String TAG = "PlayerFragment";
    private FragmentPlayerBinding binding;

    //Create MediaPlayerManager
    private MediaPlayerManager mediaPlayerManager = MediaPlayerManager.getInstance();
    RSong currentSong;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPlayerBinding.inflate(inflater, container, false);

        //get navigation args
        int songIndex = getArguments() != null ? getArguments().getInt("songIndex") : -1;

        if (songIndex > 0){
            mediaPlayerManager.setPlaylist(-1, getContext());
        }
        if(mediaPlayerManager.isPlaying() && mediaPlayerManager.getCurrentSongIndex() == songIndex){
            currentSong = mediaPlayerManager.getCurrentlyPlaying().get(mediaPlayerManager.getCurrentSongIndex());
            mediaUIInit();
        }else if (songIndex == -1) {
            currentSong = mediaPlayerManager.getCurrentlyPlaying().get(mediaPlayerManager.getCurrentSongIndex());
            mediaUIInit();
        } else {
            mediaPlayerInit(songIndex);
        }

        binding.btnPlay.setOnClickListener(v -> {
            if (mediaPlayerManager.getMediaPlayer().isPlaying()){
                mediaPlayerManager.pause();
                //Remove handler callbacks when music stops
                binding.btnPlay.setColorFilter(R.color.blue);
                handler.removeCallbacks(myUpdater);
                binding.btnPlay.setImageResource(R.drawable.ic_baseline_play_circle_filled);
            }else if(!mediaPlayerManager.getMediaPlayer().isPlaying()){
                if (mediaPlayerManager.getMediaPlayer() == null){
                }else {
                mediaPlayerManager.play();
                binding.btnPlay.clearColorFilter();
                binding.btnPlay.setImageResource(R.drawable.ic_baseline_pause_circle_filled);
                updateSeekBar();
                }
            }
        });

        binding.btnPrev.setOnClickListener(view -> {
            if (mediaPlayerManager.getMediaPlayer().getCurrentPosition() / 1000 > 10){
                mediaPlayerManager.getMediaPlayer().seekTo(0);
            }else {
                mediaPlayerInit(mediaPlayerManager.getCurrentSongIndex() - 1);
            }
        });

        binding.btnNext.setOnClickListener(view -> {
            if (mediaPlayerManager.isShuffle()){
                int randomIndex = HelperFunctions.getRandomIndex(mediaPlayerManager.getCurrentlyPlaying().size());
                mediaPlayerInit(randomIndex);
            }else{
                mediaPlayerInit(mediaPlayerManager.getCurrentSongIndex()+1);
            }
        });

        binding.btnShuf.setOnClickListener(view -> {
            if (mediaPlayerManager.isShuffle()){
                mediaPlayerManager.setShuffle(false);
                binding.btnShuf.setColorFilter(R.color.blue);
                Log.i(TAG, "onCreate: btn_shuf - false");
            }else{
                mediaPlayerManager.setShuffle(true);
                binding.btnShuf.clearColorFilter();
                Log.i(TAG, "onCreate: btn_shuf - true");
            }
        });

        binding.btnLoop.setOnClickListener(view -> {
            if (mediaPlayerManager.isLooping()) {
                mediaPlayerManager.setLooping(false);
                binding.btnLoop.setColorFilter(R.color.blue);
                Log.i(TAG, "onCreate: btn_loop - false");
            } else {
                mediaPlayerManager.setLooping(true);
                binding.btnLoop.clearColorFilter();
                Log.i(TAG, "onCreate: btn_loop - true");
            }
        });

        return binding.getRoot();
    }

    private void mediaPlayerInit(int position) {
        //mediaPlayer init
        mediaPlayerManager.setMediaPlayer(getContext(), position);
        mediaPlayerManager.play();
        currentSong = mediaPlayerManager.getCurrentlyPlaying().get(position);
        mediaUIInit();

        mediaPlayerManager.getMediaPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.i(TAG, "onCompletion: mediaPlayer islooping: " + mediaPlayerManager.isLooping());
                if (mediaPlayerManager.isLooping()){
                    mediaPlayerManager.getMediaPlayer().seekTo(0);
                    mediaPlayerManager.getMediaPlayer().start();
                }
                else if (mediaPlayerManager.isShuffle()){
                    int randomIndex = HelperFunctions.getRandomIndex(mediaPlayerManager.getCurrentlyPlaying().size());
                    mediaPlayerInit(randomIndex);
                }
                else{
                    mediaPlayerInit(mediaPlayerManager.getCurrentSongIndex()+1);
                }
            }
        });

    }


    private void mediaUIInit() {
        binding.btnPlay.setImageResource(R.drawable.ic_baseline_pause_circle_filled);
        binding.tvArtistName.setText(currentSong.artist);
        binding.tvDurationTotal.setText(milliSecondsToTimer(currentSong.duration));
        binding.tvDurationCurrent.setText(milliSecondsToTimer(0));
        binding.tvSongName.setText(currentSong.name);
        binding.ivAlbumArt.setImageBitmap(HelperFunctions.getBitmapFromContentURI(getContext(), Uri.parse(currentSong.uri)));

        if (mediaPlayerManager.isLooping()) binding.btnLoop.clearColorFilter();
        else binding.btnLoop.setColorFilter(R.color.blue);

        if (mediaPlayerManager.isShuffle()) binding.btnShuf.clearColorFilter();
        else binding.btnShuf.setColorFilter(R.color.blue);

        if (mediaPlayerManager.isPlaying()) {
            binding.btnPlay.clearColorFilter();
            binding.btnPlay.setImageResource(R.drawable.ic_baseline_pause_circle_filled);
        } else {
            binding.btnPlay.setColorFilter(R.color.blue);
            binding.btnPlay.setImageResource(R.drawable.ic_baseline_play_circle_filled);
        }

        binding.sbMedia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int seekTo;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekTo = i;
                binding.tvDurationCurrent.setText(milliSecondsToTimer(seekTo * mediaPlayerManager.getMediaPlayer().getDuration() / 100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeCallbacks(myUpdater);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayerManager.getMediaPlayer().seekTo(seekTo * mediaPlayerManager.getMediaPlayer().getDuration() / 100 );
                updateSeekBar();
            }
        });
        updateSeekBar();
    }

    private Handler handler =new Handler();
    private class Updater implements Runnable{
        @Override
        public void run() {
            updateSeekBar();
            long currentDuration = mediaPlayerManager.getMediaPlayer().getCurrentPosition();
            binding.tvDurationCurrent.setText(milliSecondsToTimer(currentDuration));
        }
    }
    private final Runnable myUpdater = new PlayerFragment.Updater();
    private void updateSeekBar(){
        if(mediaPlayerManager.getMediaPlayer().isPlaying()){
            binding.sbMedia.setProgress((int)(((float) mediaPlayerManager.getMediaPlayer().getCurrentPosition()/mediaPlayerManager.getMediaPlayer().getDuration())*100));
            handler.postDelayed(myUpdater,1000);
        }
    }
}