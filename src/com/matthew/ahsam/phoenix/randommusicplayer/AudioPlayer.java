package com.matthew.ahsam.phoenix.randommusicplayer;

import java.util.ArrayList;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class AudioPlayer extends Fragment{
	//Resources
	private Button mButtonPlayPause;
	private Button mButtonStop;
	private Button mButtonPreviousSong;
	private Button mButtonNextSong;
	private SeekBar mSeekBar;
	
	//Variables
	static private MediaPlayer mPlayer;
	private Handler mSeekHandler = new Handler();
	private boolean mIsPaused;
	private Integer mCurrentSection;
	private Integer mCurrentSong;
	private Integer mCurrentRandomCount;
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.music_player,parent,false);

		if (savedInstanceState != null) {
			mIsPaused = savedInstanceState.getBoolean("isPaused");
			mCurrentSection = savedInstanceState.getInt("CurrentSection");
			mCurrentSong = savedInstanceState.getInt("CurrentSong");
			mCurrentRandomCount = savedInstanceState.getInt("CurrentRandomCount");
		}
		
		if (mCurrentSection == null || mCurrentSong == null) {
			mCurrentSection = 0;
			mCurrentSong = 0;
		}
		
		if(mCurrentRandomCount == null) {
			mCurrentRandomCount = 0;
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
		//MUST BE AFTER SEEKBAR
		if (savedInstanceState != null) {
			mSeekBar.setMax(savedInstanceState.getInt("SeekMax"));
		}

		
		
		mButtonPlayPause = (Button)v.findViewById(R.id.buttonPlayPause);
		mButtonPlayPause.setOnClickListener(new OnClickListener() {
			
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
					if (findFirstSong(ma.getSongList())) {
						String name = ma.getSongAdapter().getNextSongPath(mCurrentSection, mCurrentSong);
						Log.e("songname",name);
						playSong(getActivity(), name);
						mButtonPlayPause.setText(R.string.button_pause);
					} else {
						Toast t = Toast.makeText(v.getContext(),"No Songs in List", Toast.LENGTH_SHORT);
						t.setGravity(Gravity.TOP, 0, 100);
						t.show();
					}
				}	
			}
		});
		if (savedInstanceState != null) {
			mButtonPlayPause.setText(savedInstanceState.getCharSequence("PlayButton"));
		}
		
		mButtonStop =(Button)v.findViewById(R.id.buttonStop);
		mButtonStop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onSongEnd();
			}
		});
		
		mButtonPreviousSong = (Button) v.findViewById(R.id.buttonPreviousSong);
		mButtonPreviousSong.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		mButtonNextSong = (Button) v.findViewById(R.id.buttonNextSong);
		mButtonNextSong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MainActivity ma = (MainActivity) getActivity();
				getAndPlayNextSong(ma.getSongList(),mCurrentSection,mCurrentSong);
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
	
	//Return true if List is empty otherwise return false after setting mCurrentSection and mCurrentSong to first song
	private boolean findFirstSong (ArrayList<SongListGroup> slgArray) {
		for (int i = 0; i < slgArray.size(); i++) {
			if (slgArray.get(i).getSongs().size() > 0) {
				mCurrentSection = i;
				mCurrentSong = 0;
				return true;
			} else {}
		}
		return false;
	}
	
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
		saveState.putInt("CurrentRandomCount", mCurrentRandomCount);
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
		mSeekBar.setProgress(0);
		stopSong();
	}
	
	public void getAndPlayNextSong (ArrayList<SongListGroup> slgArray, Integer section, Integer song) {
		stopSong();
		mPlayer = new MediaPlayer();
		MainActivity ma = ((MainActivity)getActivity());
		//Get Next Section
			Integer nextSection;
		if (slgArray.size()-1 == section){
			nextSection = 0;
		} else {
			nextSection = section + 1;
		}
		//Fetch song
		switch (ma.getSongList().get(nextSection).getType()){
			case RANDOM:	
				getNextSongRandom(slgArray, section, song);
				break;
			case ORDERED:
				getNextSongOrdered(slgArray,section,song);
				break;
		}
		String name = ma.getSongAdapter().getNextSongPath(section, song);
		Log.e("songname",name);
		playSong(getActivity(), name);
	}
	
	public void getNextSongRandom (ArrayList<SongListGroup> slgArray, Integer section, Integer song){
		if(mCurrentRandomCount < slgArray.get(section).getMaxRandomSongs()-1) {
			mCurrentRandomCount++;
		} else {
			while (true) {
				if (section < slgArray.size()-1){
					section++;
					if (slgArray.get(section).getSongs().size() > 0) {
						mCurrentRandomCount = 0;
						break;
					} else{}
				} else {
					section = 0;
					mCurrentRandomCount = 0;
					break;
				}
			}
		}
		song = getRandomNumberInRange(0,slgArray.get(section).getSongs().size()-1);
		mCurrentSection = section;
		mCurrentSong = song;
	}
	
	//Return random number in range [min, max]
	public int getRandomNumberInRange (int min, int max){
		return min + (int)(Math.random()*((max-min)+1));
	}
	
	public void getNextSongOrdered (ArrayList<SongListGroup> slgArray,Integer currSection, Integer currSong) {
		if (currSong < slgArray.get(currSection).getSongs().size()-1) {
			currSong++;
		} else {
			while (true) {
				if (currSection < slgArray.size()-1){
					currSection++;
					if (slgArray.get(currSection).getSongs().size() > 0) {
						currSong = 0;
						break;
					} else{}
				} else {
					currSection = 0;
					currSong = 0;
					break;
				}
			}
		}
		mCurrentSection = currSection;
		mCurrentSong = currSong;
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
				MainActivity ma = (MainActivity) getActivity();
				getAndPlayNextSong(ma.getSongList(),mCurrentSection,mCurrentSong);
			}
			
		});
		
		mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
					mp.start();
					mSeekBar.setMax(mp.getDuration());
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
