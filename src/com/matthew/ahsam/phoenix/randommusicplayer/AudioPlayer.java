package com.matthew.ahsam.phoenix.randommusicplayer;

import android.app.Fragment;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class AudioPlayer extends Fragment{
	static private MediaPlayer mPlayer;
	private Button mButtonPlayPause;
	private Button mButtonStop;
	private SeekBar mSeekBar;
	private Handler mSeekHandler = new Handler();
	private boolean mIsPaused;
	private Integer mCurrentSection;
	private Integer mCurrentSong;
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.music_player,parent,false);
		if (mCurrentSection == null || mCurrentSong == null) {
			mCurrentSection = 0;
			mCurrentSong = 0;
		}
		
		if (mPlayer == null){
			mPlayer = new MediaPlayer();
		}
		
		mSeekBar = (SeekBar)v.findViewById(R.id.seekBar1);
		mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				 if (mPlayer !=  null) {
					 if (mPlayer.isPlaying()) {
					 	mPlayer.seekTo(seekBar.getProgress());
					 	updateSeek();
					 }
				 }
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				if (mPlayer != null) {
					mSeekHandler.removeCallbacks(mUpdateSeekBar);
				}
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				
			}
		});
		if (savedInstanceState != null) {
			mSeekBar.setMax(savedInstanceState.getInt("SeekMax"));
		}
		
		
		mButtonPlayPause = (Button)v.findViewById(R.id.buttonPlayPause);
		mButtonPlayPause.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mPlayer == null) {
					mPlayer = new MediaPlayer();
				}
				if (mPlayer.isPlaying()){
					mPlayer.pause();
					mSeekHandler.removeCallbacks(mUpdateSeekBar);
					mIsPaused = true;
					mButtonPlayPause.setText(R.string.button_play);
				} else if (mIsPaused) {
					mPlayer.start();
					updateSeek();
					mIsPaused = false;
					mButtonPlayPause.setText(R.string.button_pause);
				} else {
					MainActivity ma = ((MainActivity)getActivity());
					if (ma.getSongAdapter().getChildrenCount(mCurrentSection) <= 0) {
						Toast t = Toast.makeText(v.getContext(),"No Songs in List", Toast.LENGTH_SHORT);
						t.setGravity(Gravity.TOP, 0, 100);
						t.show();
					} else {
						String name = ma.getSongAdapter().getNextSongPath(mCurrentSection, mCurrentSong);
						Log.e("songname",name);
						playSong(getActivity(), name);
						mButtonPlayPause.setText(R.string.button_pause);
					}
				}	
			}
		});
		if (savedInstanceState != null) {
			mButtonPlayPause.setText(savedInstanceState.getCharSequence("PlayButton"));
		}
		
		mButtonStop =(Button)v.findViewById(R.id.buttonStop);
		mButtonStop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onSongEnd();
			}
		});
		
		return v;
	}
	
	private Runnable mUpdateSeekBar = new Runnable() {

		@Override
		public void run() {
			if (mPlayer != null) {
				updateSeek();
			}
		}
		
	};
	
	private void updateSeek () {
		mSeekBar.setProgress(mPlayer.getCurrentPosition());
		mSeekHandler.postDelayed(mUpdateSeekBar, 100);
	}
	
	@Override
	public void onSaveInstanceState (Bundle saveState) {
		super.onSaveInstanceState(saveState);
		saveState.putBoolean("isPaused", mIsPaused);
		saveState.putInt("CurrentSection", mCurrentSection);
		saveState.putInt("CurrentSong",	mCurrentSong);
		saveState.putString("PlayButton", mButtonPlayPause.getText().toString());
		saveState.putInt("SeekMax", mSeekBar.getMax());
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	public void stopSong() {
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}
	
	private void onSongEnd () {
		mButtonPlayPause.setText(R.string.button_play);
		mIsPaused = false;
		mSeekHandler.removeCallbacks(mUpdateSeekBar);
		stopSong();
	}
	
	public void playSong(Context c, String path) {
		Uri.Builder uribuild = new Uri.Builder();
		uribuild.path(path);
		Uri song = uribuild.build();
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
				onSongEnd();
			}
		});
		
		mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
					mPlayer.start();
					mSeekBar.setMax(mPlayer.getDuration());
					updateSeek();		
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
