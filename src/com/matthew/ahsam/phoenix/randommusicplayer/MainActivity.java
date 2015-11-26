package com.matthew.ahsam.phoenix.randommusicplayer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	
	//Resources
	private Button mButtonAddSection;
	private Button mButtonRemoveSelected;
	private Button mButtonMoveSongUp;
	private Button mButtonMoveSongDown;
	private ExpandableListView mExpandableListViewSongList;
	private ListView mListViewAddSongs;
	private TabHost mTabHost;
	@SuppressWarnings("unused")
	private TabWidget mTabWidget;
	private TextView mTextViewSongName;
	private TextView mTextViewSongArtist;
	private TextView mTextViewSongAlbum;
	private TextView mTextViewSectionType;
	private TextView mTextViewNumberToPlay;
	private Button mButtonSectionType;
	private Button mButtonNumberRandomSongs;
	

	//Variables
	private File[] fileList;
	private SongListAdapter mSongAdapter;
	private InputListAdapter mInputAdapter;
	private ArrayList<SongListGroup> mSongList;
	private ArrayList<SongListChild> mInputList;
	private Integer	mPreviousSelectedInputList;
	private Integer mPreviousSelectedChildSongList;	
	private Integer mPreviousSelectedGroupSongList;
	private boolean mLastSongSelectedGroup;
	private boolean mMovingChildSongList;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (savedInstanceState != null) {
			mPreviousSelectedInputList = savedInstanceState.getInt("PreviousSelectedInput");
			mPreviousSelectedChildSongList = savedInstanceState.getInt("PreviousSelectedChild");
			mPreviousSelectedGroupSongList = savedInstanceState.getInt("PreviousSelectedGroup");
		} else {
			mPreviousSelectedInputList = -1;
			mPreviousSelectedChildSongList = -1;
			mPreviousSelectedGroupSongList = -1;
		}
		
		
		//ExpandableList/SongList
		mExpandableListViewSongList = (ExpandableListView) findViewById(R.id.expandableListViewSongList);
		if (savedInstanceState != null) {
			mSongList = savedInstanceState.getParcelableArrayList("SongList");
		} else {
			mSongList = SetStandardGroups();
		}
		mSongAdapter = new SongListAdapter (MainActivity.this, mSongList);
		mExpandableListViewSongList.setAdapter(mSongAdapter);
		mExpandableListViewSongList.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {
				SongListChild slc = mSongList.get(groupPosition).getSongs().get(childPosition);
				if (slc.isSelected()) {
					slc.setSelected(false);
					view.setBackgroundColor(view.getResources().getColor(android.R.color.background_light));
				} else {
					slc.setSelected(true);
					view.setBackgroundColor(view.getResources().getColor(R.color.BlueTintBackground));
				}
				mLastSongSelectedGroup = false;
				mPreviousSelectedChildSongList = childPosition;
				mPreviousSelectedGroupSongList = groupPosition;
				SongListChild prevslc = mSongList.get(mPreviousSelectedGroupSongList).getSongs().get(mPreviousSelectedChildSongList);
				mTextViewSongName.setText(prevslc.getName());
				mTextViewSongArtist.setText(prevslc.getArtist());
				mTextViewSongAlbum.setText(prevslc.getAlbum());
				mTextViewSongName.setVisibility(View.VISIBLE);
				mTextViewSongArtist.setVisibility(View.VISIBLE);
				mTextViewSongAlbum.setVisibility(View.VISIBLE);
				mTextViewSectionType.setVisibility(View.INVISIBLE);
				mButtonSectionType.setVisibility(View.INVISIBLE);
				mTextViewNumberToPlay.setVisibility(View.INVISIBLE);
				mButtonNumberRandomSongs.setVisibility(View.INVISIBLE);
				return false;
			}
		
		});
		mExpandableListViewSongList.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View view, int groupPosition, long id) {
				mLastSongSelectedGroup = true;
				SongListGroup slg = mSongList.get(groupPosition);
				mPreviousSelectedGroupSongList = groupPosition;
				mButtonSectionType.setText(slg.getTypeAsString());
				mTextViewSongName.setVisibility(View.INVISIBLE);
				mTextViewSongArtist.setVisibility(View.INVISIBLE);
				mTextViewSongAlbum.setVisibility(View.INVISIBLE);
				mTextViewSectionType.setVisibility(View.VISIBLE);
				mButtonSectionType.setVisibility(View.VISIBLE);
				mButtonNumberRandomSongs.setText(Integer.toString(mSongList.get(mPreviousSelectedGroupSongList).getMaxRandomSongs()));
				if (mButtonSectionType.getText() == "Random"){
					mTextViewNumberToPlay.setVisibility(View.VISIBLE);
					mButtonNumberRandomSongs.setVisibility(View.VISIBLE);
				}
				return false;
			}
			
		});
		mExpandableListViewSongList.setOnItemLongClickListener(new OnItemLongClickListener () {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
		            int groupPosition = ExpandableListView.getPackedPositionGroup(id);
		            int childPosition = ExpandableListView.getPackedPositionChild(id);
		            
		            ClipData data = ClipData.newPlainText("", "");
					DragShadowBuilder shadow = new View.DragShadowBuilder(view);
					setMovingChildSongList(true);
					setPreviousSelectedChildSongList(childPosition);
					setPreviousSelectedGroupSongList(groupPosition);
					view.startDrag(data, shadow, mSongList.get(groupPosition).getSongs().get(childPosition), 0);
		            return true;
				} else {
					return false;
				}
			}
			
		});
		
		//ListView/InputList
		mListViewAddSongs = (ListView) findViewById(R.id.listViewAddSongs);
		if (savedInstanceState != null) {
			mInputList = savedInstanceState.getParcelableArrayList("InputList");
		} else {
			File f = new File (Environment.DIRECTORY_MUSIC);
			Log.e ("TESTING",f.getAbsolutePath());
			Log.e("Testing2", Environment.getExternalStorageDirectory().toString() + "/" + Environment.DIRECTORY_MUSIC);
			mInputList = populateInputList(Environment.getExternalStorageDirectory().getPath() + "/" + Environment.DIRECTORY_MUSIC);
		}
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
					mPreviousSelectedInputList = -1;
				} else {
					if (mPreviousSelectedInputList >= 0) {
						mInputList.get(mPreviousSelectedInputList).setSelected(false);
						ViewGroup vg = (ViewGroup) view.getParent();
						View mPreviousView = vg.getChildAt(Math.abs(((SongListChild)vg.getChildAt(0).getTag()).getPosition() - mPreviousSelectedInputList));
						mPreviousView.setBackgroundColor(view.getResources().getColor(android.R.color.background_light));
						mPreviousView.invalidate();
					}
					mInputList.get(position).setSelected(true);
					mPreviousSelectedInputList = position;
					view.setBackgroundColor(view.getResources().getColor(R.color.BlueTintBackground));
					view.invalidate();
				}
				
			}
		
		});
		
		mListViewAddSongs.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				if (mInputList.get(position).isDirectory()){
					return true;
				} else {
					ClipData data = ClipData.newPlainText("", "");
					DragShadowBuilder shadow = new View.DragShadowBuilder(view);
					view.startDrag(data, shadow, mInputList.get(position), 0);
				}
				return true;
			}
		
		});
		
		
		//Tab Controls
		mButtonAddSection = (Button) findViewById(R.id.buttonAddSection);
		mButtonAddSection.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SongListGroup g = new SongListGroup();
				ArrayList<SongListChild> arrayslc = new ArrayList<SongListChild>();
				String [] strarray = getResources().getStringArray(R.array.SectionTypes);
				g.setName(strarray[0]);
				g.setPosition(mSongList.size());
				g.setSongs(arrayslc);
				g.setType(0);
				mSongList.add(g);
				mSongAdapter.notifyDataSetChanged();
			}
		});
		
		mButtonRemoveSelected = (Button) findViewById(R.id.buttonRemoveSong);
		mButtonRemoveSelected.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				for (int i = 0; i < mSongList.size(); i++) {
					ArrayList<SongListChild> slcArray = mSongList.get(i).getSongs();
					for (int j = 0; j < slcArray.size(); j++ ) {
						if (slcArray.get(j).isSelected()){
							slcArray.remove(j);
							j--;
						} else {}
					}
				}
				mSongAdapter.notifyDataSetChanged();
				mExpandableListViewSongList.invalidateViews();
			}
		});

		mButtonMoveSongDown = (Button) findViewById(R.id.buttonMoveSongDown);
		mButtonMoveSongDown.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity ma = (MainActivity) v.getContext();
				int child = ma.getPreviousSelectedChildSongList();
				int group = ma.getPreviousSelectedGroupSongList();
				int groupmax = ma.getSongList().size()-1;
				int childmax = ma.getSongList().get(group).getSongs().size()-2;
 				if ((child <= childmax && child >= 0) && (group >= 0 && group <= groupmax)) {
 					SongListChild slc = ma.getSongList().get(group).getSongs().get(child);
					SongListChild slc2 = ma.getSongList().get(group).getSongs().get(child+1);
					getSongList().get(group).getSongs().set(child, slc2);
					getSongList().get(group).getSongs().set(child+1, slc);
					ma.getSongAdapter().notifyDataSetChanged();
					ma.setPreviousSelectedChildSongList(child+1);
					mExpandableListViewSongList.invalidateViews();
				}
				
			}
		});
		
		mButtonMoveSongUp = (Button) findViewById(R.id.buttonMoveSongUp);
		mButtonMoveSongUp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity ma = (MainActivity) v.getContext();
				int child = ma.getPreviousSelectedChildSongList();
				int group = ma.getPreviousSelectedGroupSongList();
				int groupmax = ma.getSongList().size()-1;
				int childmax = ma.getSongList().get(group).getSongs().size()-1;
 				if ((child <= childmax && child >= 1) && (group >= 0 && group <= groupmax)) {
 					SongListChild slc = ma.getSongList().get(group).getSongs().get(child);
					SongListChild slc2 = ma.getSongList().get(group).getSongs().get(child-1);
					getSongList().get(group).getSongs().set(child, slc2);
					getSongList().get(group).getSongs().set(child-1, slc);
					ma.getSongAdapter().notifyDataSetChanged();
					ma.setPreviousSelectedChildSongList(child-1);
					mExpandableListViewSongList.invalidateViews();
				}
				
			}
		});
		
		//Tab Settings
		mTextViewSongName = (TextView) findViewById(R.id.textViewSongName);
		mTextViewSongArtist = (TextView) findViewById(R.id.textViewArtist);
		mTextViewSongAlbum = (TextView) findViewById(R.id.textViewAlbum);
		mTextViewSectionType = (TextView) findViewById(R.id.textViewSectionType);
		mTextViewNumberToPlay = (TextView) findViewById(R.id.textNumberRandomSongs);
		
		mButtonNumberRandomSongs = (Button) findViewById(R.id.buttonNumberRandomSongs);
		mButtonNumberRandomSongs.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final View numPickerView = v.inflate(v.getContext(), R.layout.number_picker_dialog, null);
				AlertDialog.Builder numPickerDialogBuilder = new AlertDialog.Builder(v.getContext())
					.setTitle("Number of Songs")
					.setView(numPickerView)
					.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
				AlertDialog numPickerDialog = numPickerDialogBuilder.create();
				numPickerDialog.show();
				//Setup Number of random songs to play for each group.
				//Code for this is in NumberPickerFragment
				//Need to write code for OK button and figure out how to get info from fragment back to here.
			}
		});
		
		mButtonSectionType = (Button) findViewById(R.id.buttonSectionType);
		mButtonSectionType.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
				builder.setTitle(R.string.dialog_type_picker_title);
				builder.setItems(R.array.SectionTypes,new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SongListGroup slg = mSongList.get(mPreviousSelectedGroupSongList);
						slg.setType(which);
						mButtonSectionType.setText(slg.getTypeAsString());
						slg.setName(slg.getTypeAsString());
						if (slg.getType() == SectionType.RANDOM) {
							mTextViewNumberToPlay.setVisibility(View.VISIBLE);
							mButtonNumberRandomSongs.setVisibility(View.VISIBLE);	
						} else {
							mTextViewNumberToPlay.setVisibility(View.INVISIBLE);
							mButtonNumberRandomSongs.setVisibility(View.INVISIBLE);	
						}
						mExpandableListViewSongList.invalidateViews();
					}
				});
				builder.show();							
			}
		});
		mTextViewSongName.setVisibility(View.VISIBLE);
		mTextViewSongArtist.setVisibility(View.VISIBLE);
		mTextViewSongAlbum.setVisibility(View.VISIBLE);
		mTextViewSectionType.setVisibility(View.INVISIBLE);
		mButtonSectionType.setVisibility(View.INVISIBLE);
		mTextViewNumberToPlay.setVisibility(View.INVISIBLE);
		mButtonNumberRandomSongs.setVisibility(View.INVISIBLE);		
		
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		
		mTabWidget = mTabHost.getTabWidget();
		TabSpec tabSpec = mTabHost.newTabSpec("Tab1"); 
		tabSpec.setIndicator("",getResources().getDrawable(R.drawable.tabmusic));
		tabSpec.setContent(R.id.fragmentTabSongList);
		mTabHost.addTab(tabSpec);
		tabSpec = mTabHost.newTabSpec("Tab2");
		tabSpec.setIndicator("",getResources().getDrawable(R.drawable.tabsettings));
		tabSpec.setContent(R.id.fragmentTabSettings);
		mTabHost.addTab(tabSpec);
		tabSpec = mTabHost.newTabSpec("Tab3");
		tabSpec.setIndicator("",getResources().getDrawable(R.drawable.tabcontrol));
		tabSpec.setContent(R.id.fragmentTabControls);
		mTabHost.addTab(tabSpec);
		mTabHost.getTabWidget().getChildAt(1).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mTabHost.setCurrentTab(1);
			}
		});
		

	

	}
	
	//Overrides of android state functions
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState != null){
		savedInstanceState.putInt("PreviousSelectedInput", mPreviousSelectedInputList);
		savedInstanceState.putInt("PreviousSelectedChild", mPreviousSelectedChildSongList);
		savedInstanceState.putInt("PreviousSelectedGroup", mPreviousSelectedGroupSongList);
		savedInstanceState.putParcelableArrayList("SongList", mSongList);
		savedInstanceState.putParcelableArrayList("InputList", mInputList);
		} else {
			
		}
		super.onSaveInstanceState(savedInstanceState);
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		/*mPreviousSelected = savedInstanceState.getInt("PreviousSelected");
		mSongList = savedInstanceState.getParcelableArrayList("SongList");
		mInputList = savedInstanceState.getParcelableArrayList("InputList");*/
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	//Private Functions	
	private ArrayList<SongListChild> populateInputList (String directory) {
		ArrayList<SongListChild> list = new ArrayList<SongListChild>();
		File[] tempFolderList = loadFileList(directory, true);
		File[] tempFileList = loadFileList(directory, false);
		MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
		
		tempFolderList = sortByName(tempFolderList);
		tempFileList = sortByName(tempFileList);
		
		if (!directory.equals(Environment.getExternalStorageDirectory().getPath() + "/" + Environment.DIRECTORY_MUSIC)) {
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
        	metaRetriever.setDataSource(tempFileList[i].getAbsolutePath());
        	try {
        		slc.setName(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
                slc.setArtist(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
                slc.setAlbum(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
        	} catch (Exception e) {
    	        slc.setName(tempFileList[i].getName());
        		slc.setArtist("Unknown Artist");
        		slc.setAlbum("Unknown Album");
        	}
        	if (slc.getName() == null) {
        		slc.setName(tempFileList[i].getName());
        	}
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
	
	private File[] loadFileList(String directory, boolean onlyDirectories) {
	    File path = new File(directory);
	    Log.e("PATH",path.getAbsolutePath());
	    if(path.isDirectory()){
	    	Log.e("Dir","true");
	    } else {
	    	Log.e("dir","false");
	    }
	    if(path.exists()) {
	    	FilenameFilter filter;
	    	if (onlyDirectories) {
		        filter = new FilenameFilter() {
		            public boolean accept(File dir, String filename) {
		                File file = new File(dir, filename);
		                Log.e("FILEFILTER", ">" + filename);
		                return file.isDirectory();
		            }	
		        };
	    	} else {
	    		filter = new FilenameFilter() {
		            public boolean accept(File dir, String filename) {
		                //File file = new File(dir, filename);
		                Log.e("FILEFILTER", ">" + filename);
		                return filename.contains(".mp3");
		            }	
		        };
	    	}

	        //if null return an empty array instead
	    	File [] list = path.listFiles();
	         list = path.listFiles(filter);
	        return list == null? new File[0] : list;
	    } else {
	        return new File[0];
	    }
	}
	
	
	private ArrayList<SongListGroup> SetStandardGroups() {
		 
		ArrayList<SongListGroup> list = new ArrayList<SongListGroup>();
	    ArrayList<SongListChild> list2 = new ArrayList<SongListChild>();
	 
	    SongListGroup gru1 = new SongListGroup();
	    gru1.setName("Random");
	    gru1.setPosition(0);	 
		gru1.setSongs(list2);	 
		
	    list.add(gru1);
		 
	         return list;
	 }
	
	//Getters and Setters
	public String upOneDirectory(String directory){
	String[] dirs = directory.split("/");
	    StringBuilder stringBuilder = new StringBuilder("");

	    for(int i = 0; i < dirs.length-2; i++)
	        stringBuilder.append(dirs[i]).append("/");
	    
	    	stringBuilder.append(dirs[dirs.length-2]);

	    return stringBuilder.toString();
	}
	
	public ExpandableListView getExpandableListViewSongList() {
		return mExpandableListViewSongList;
	}

	public void setExpandableListViewSongList(ExpandableListView expandableListViewSongList) {
		mExpandableListViewSongList = expandableListViewSongList;
	}
	
	public SongListAdapter getSongAdapter() {
		return mSongAdapter;
	}

	public boolean isLastSongSelectedGroup() {
		return mLastSongSelectedGroup;
	}

	public boolean isMovingChildSongList() {
		return mMovingChildSongList;
	}

	public void setMovingChildSongList(boolean movingChildSongList) {
		mMovingChildSongList = movingChildSongList;
	}
	
	public Integer getPreviousSelectedChildSongList() {
		return mPreviousSelectedChildSongList;
	}

	public void setPreviousSelectedChildSongList(
			Integer previousSelectedChildSongList) {
		mPreviousSelectedChildSongList = previousSelectedChildSongList;
	}

	public Integer getPreviousSelectedGroupSongList() {
		return mPreviousSelectedGroupSongList;
	}

	public void setPreviousSelectedGroupSongList(
			Integer previousSelectedGroupSongList) {
		mPreviousSelectedGroupSongList = previousSelectedGroupSongList;
	}
	
	public ArrayList<SongListGroup> getSongList() {
		return mSongList;
	}

	public void setSongList(ArrayList<SongListGroup> songList) {
		mSongList = songList;
	}



}
