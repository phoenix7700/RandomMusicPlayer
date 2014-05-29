package com.matthew.ahsam.phoenix.randommusicplayer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class MainActivity extends FragmentActivity {
	
	//Resources
	private Button mButtonAddSection;
	private EditText mEditTextSongName;
	private ExpandableListView mExpandableListViewSongList;
	private ListView mListViewAddSongs;
	
	
	//Variables
	private File[] fileList;
	private SongListAdapter mSongAdapter;
	private InputListAdapter mInputAdapter;
	private ArrayList<SongListGroup> mSongList;
	private ArrayList<SongListChild> mInputList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mExpandableListViewSongList = (ExpandableListView) findViewById(R.id.expandableListViewSongList);
		mSongList = SetStandardGroups();
		mSongAdapter = new SongListAdapter (MainActivity.this, mSongList);
		mExpandableListViewSongList.setAdapter(mSongAdapter);
			
		mListViewAddSongs = (ListView) findViewById(R.id.listViewAddSongs);
		mInputList = populateInputList(Environment.getExternalStorageDirectory().toString() + "/" + Environment.DIRECTORY_MUSIC);
		mInputAdapter = new InputListAdapter (MainActivity.this, mInputList);
		mListViewAddSongs.setAdapter(mInputAdapter);
		mListViewAddSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
				SongListChild slc = mInputList.get(position);
				if (slc.isDirectory()) {
					mInputList.clear();
					mInputList.addAll(populateInputList(slc.getFullPath()));
					mInputAdapter.notifyDataSetChanged();
				}
				
			}
		
		});
		
		mButtonAddSection = (Button) findViewById(R.id.buttonAddSection);
		mButtonAddSection.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SongListGroup g = new SongListGroup();
				g.setName("Ordered");
				mSongList.add(g);
				mSongAdapter.notifyDataSetChanged();
			}
		});
	}

	private ArrayList<SongListChild> populateInputList (String directory) {
		ArrayList<SongListChild> list = new ArrayList<SongListChild>();
		File[] tempFolderList = loadFileList(directory, true);
		File[] tempFileList = loadFileList(directory, false);
		
		tempFolderList = sortByName(tempFolderList);
		tempFileList = sortByName(tempFileList);
		
		if (!directory.equals(Environment.getExternalStorageDirectory().toString() + "/" + Environment.DIRECTORY_MUSIC)) {
			File[] temp = new File[tempFolderList.length + 1];
			temp[0] = new File(upOneDirectory(directory));
			System.arraycopy(tempFolderList, 0, temp, 1, tempFolderList.length);
			tempFolderList = new File[temp.length];
			System.arraycopy(temp, 0, tempFolderList, 0, temp.length);
		}
		
		fileList = new File[tempFileList.length + tempFolderList.length];
		int folderlen = tempFolderList.length;
		for(int i = 0; i < folderlen; i++){
			
            fileList[i] = tempFolderList[i];   
        	SongListChild slc = new SongListChild();
            slc.setName(tempFolderList[i].getName());
            slc.setFullPath(tempFolderList[i].getAbsolutePath());
            slc.setDirectory(true);
            if (i == 0 && tempFolderList[i].getAbsolutePath().equals(upOneDirectory(directory))) {
				slc.setName("..");
			}
            list.add(slc);
        }
		
		for(int i = 0 ; i < tempFileList.length; i++){
	        fileList[i + folderlen] = tempFileList[i];   
	        SongListChild slc = new SongListChild();
	        slc.setName(tempFileList[i].getName());
	        slc.setFullPath(tempFileList[i].getAbsolutePath());
	        slc.setDirectory(false);
	        list.add(slc);
	    }

		return list;
	}
	
	private File[] sortByName (File[] files) {
		FileSort fSort = new FileSort (files);
		return fSort.Sort();
	}
	
	public ArrayList<SongListGroup> SetStandardGroups() {
		 
		ArrayList<SongListGroup> list = new ArrayList<SongListGroup>();
	    ArrayList<SongListChild> list2 = new ArrayList<SongListChild>();
	 
	    SongListGroup gru1 = new SongListGroup();
	    gru1.setName("Random");
	 
	    SongListChild ch1_1 = new SongListChild();
	    ch1_1.setName("A Song");
	    list2.add(ch1_1);
	 
	    SongListChild ch1_2 = new SongListChild(); 
        ch1_2.setName("Song");
	    list2.add(ch1_2);
		 
	    SongListChild ch1_3 = new SongListChild();
		ch1_3.setName("Other song");
		list2.add(ch1_3);
		 
		gru1.setSongs(list2);	 
		
	    list.add(gru1);
		 
	         return list;
	 }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	
	
	
	private File[] loadFileList(String directory, boolean onlyDirectories) {
	    File path = new File(directory);
	    
	    if(path.exists()) {
	    	FilenameFilter filter;
	    	if (onlyDirectories) {
		        filter = new FilenameFilter() {
		            public boolean accept(File dir, String filename) {
		                File file = new File(dir, filename);
		                //Log.e("FILEFILTER", ">" + filename);
		                return file.isDirectory();
		            }	
		        };
	    	} else {
	    		filter = new FilenameFilter() {
		            public boolean accept(File dir, String filename) {
		                //File file = new File(dir, filename);
		                //Log.e("FILEFILTER", ">" + filename);
		                return filename.contains(".mp3");
		            }	
		        };
	    	}

	        //if null return an empty array instead
	        File[] list = path.listFiles(filter);
	        return list == null? new File[0] : list;
	    } else {
	        return new File[0];
	    }
	}
	

/*	public void showFileListDialog(final String directory, final Context context){
	    Dialog dialog = null;
	    AlertDialog.Builder builder = new AlertDialog.Builder(context);

	    File[] tempFileList = loadFileList(directory);

	    //if directory is root, no need to up one directory
	    if(directory.equals("/")){
	        fileList = new File[tempFileList.length];
	        filenameList = new String[tempFileList.length];

	        //iterate over tempFileList
	        for(int i = 0; i < tempFileList.length; i++){
	            fileList[i] = tempFileList[i];
	            filenameList[i] = tempFileList[i].getName();
	        }
	    } else {
	        fileList = new File[tempFileList.length+1];
	        filenameList = new String[tempFileList.length+1];

	        //add an "up" option as first item
	        fileList[0] = new File(upOneDirectory(directory));
	        filenameList[0] = "..";

	        //iterate over tempFileList
	        for(int i = 0; i < tempFileList.length; i++){
	            fileList[i+1] = tempFileList[i];
	            filenameList[i+1] = tempFileList[i].getName();
	        }
	    }

	    builder.setTitle("Choose your file: " );

	    builder.setItems(filenameList, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	            File chosenFile = fileList[which];
	            mEditTextSongName.setText(chosenFile.getAbsolutePath());
	            
	            if(chosenFile.isDirectory())
	                showFileListDialog(chosenFile.getAbsolutePath(), context);
	        }
	    });

	    builder.setNegativeButton("Cancel", new OnClickListener() {

	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	mEditTextSongName.setText("");
	            dialog.dismiss();
	        }
	    });
	        dialog = builder.create();
	    dialog.show();
	}*/

	public String upOneDirectory(String directory){
	String[] dirs = directory.split("/");
	    StringBuilder stringBuilder = new StringBuilder("");

	    for(int i = 0; i < dirs.length-2; i++)
	        stringBuilder.append(dirs[i]).append("/");
	    
	    	stringBuilder.append(dirs[dirs.length-2]);

	    return stringBuilder.toString();
	}

	public EditText getmEditTextSongName() {
		return mEditTextSongName;
	}

	public void setmEditTextSongName(EditText mEditTextSongName) {
		this.mEditTextSongName = mEditTextSongName;
	}
	public void addSongToList () {
		
	}
}
