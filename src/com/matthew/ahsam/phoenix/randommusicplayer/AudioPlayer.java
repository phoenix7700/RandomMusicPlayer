package com.matthew.ahsam.phoenix.randommusicplayer;

import android.app.Fragment;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

public class AudioPlayer extends Fragment{
	private MediaPlayer mPlayer;
	private Button mButtonPlayPause;
	private Button mButtonStop;
	private SeekBar mSeekBar;
	private boolean mIsPaused;
	private Integer mCurrentSection;
	private Integer mCurrentSong;
	
	//private boolean mIsPlaying;
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.music_player,parent,false);
		
		mPlayer = new MediaPlayer();
		
		mSeekBar = (SeekBar)v.findViewById(R.id.seekBar1);
		mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		mButtonPlayPause = (Button)v.findViewById(R.id.buttonPlayPause);
		mButtonPlayPause.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mPlayer.isPlaying()) {
					mButtonPlayPause.setText(R.string.button_play);
					mPlayer.pause();
					mIsPaused = true;
				} else if (mIsPaused) {
					mPlayer.start();
					mIsPaused = false;
					mButtonPlayPause.setText(R.string.button_pause);
				} else {
					MainActivity ma = ((MainActivity)getActivity());
					String name = ma.getSongAdapter().getNextSongPath(mCurrentSection, mCurrentSong);
					Log.e("songname",name);
					playSong(getActivity(), name);
					mButtonPlayPause.setText(R.string.button_pause);
				}
			}
		});
		
		mButtonStop =(Button)v.findViewById(R.id.buttonStop);
		mButtonStop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stopSong();
				mIsPaused = false;
				mButtonPlayPause.setText(R.string.button_play);
			}
		});
		
		return v;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mPlayer.stop();
	}
	
	public void stopSong() {
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}
	
	public void playSong(Context c, String path) {
		stopSong();
		Uri.Builder uribuild = new Uri.Builder();
		uribuild.path(path);
		Uri song = uribuild.build();
		mPlayer = new MediaPlayer();
		mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mPlayer.setDataSource(c, song);
			mPlayer.prepareAsync();
		} catch (Exception e) {
			Log.e("PLAY", e.getMessage());
		}
		
		mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				stopSong();
			}
		});
		
		mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
					mPlayer.start();
			}
		});
		
		mPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
			
			@Override
			public void onSeekComplete(MediaPlayer mp) {
				// TODO Auto-generated method stub
			}
		});
	}
}
