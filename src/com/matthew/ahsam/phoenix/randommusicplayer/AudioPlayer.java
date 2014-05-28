package com.matthew.ahsam.phoenix.randommusicplayer;

import android.app.Fragment;
import android.content.Context;
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
	private MediaPlayer mPlayer = new MediaPlayer();
	private Button mButtonPlayPause;
	private Button mButtonStop;
	private SeekBar mSeekBar;
	
	//private boolean mIsPlaying;
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.music_player,parent,false);
		
		mSeekBar = (SeekBar)v.findViewById(R.id.seekBar1);
		
		
		mButtonPlayPause = (Button)v.findViewById(R.id.buttonPlayPause);
		mButtonPlayPause.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//String name = ((MainActivity)getActivity()).getmEditTextSongName().getText().toString();
				//Log.e("songname",name);
				//playSong(getActivity(), name);
			}
		});
		
		mButtonStop =(Button)v.findViewById(R.id.buttonStop);
		mButtonStop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stopSong();
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
		mPlayer = MediaPlayer.create(c, song);
		
		mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				stopSong();
			}
		});
		
		mPlayer.start();
	}
}
