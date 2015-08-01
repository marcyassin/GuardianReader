package yassin.marc.guardianreader;

import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.daimajia.swipe.util.Attributes;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // Navigation Drawer setup

    private DrawerLayout mDrawerLayout;
    String TITLES[] = {"Home","Profile","Feed 1","Feed 2","Feed 3"};
    int ICONS[] = {R.drawable.ic_home_black_24dp,
            R.drawable.ic_android_black_24dp,
            R.drawable.ic_home_black_24dp,
            R.drawable.ic_home_black_24dp,
            R.drawable.ic_home_black_24dp};
    private String NAME = "Marcisbrown";
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mNavAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;



    // Toolbar setup
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    // Post Data
    private ArrayList<PostData> postListData= new ArrayList<PostData>();
    //private ArrayList<NavigationDrawerItem> navigationDrawerItems = new ArrayList<NavigationDrawerItem>();
    private List postLinks;
    private ListView mListView;
    private PostListViewAdapter mAdapter;
    private Context mContext = this;



    // Swipe Refresh Layout



    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String mainUrl;


    //----------------Activity LifeCycle-----------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts_listview);

        mainUrl = "http://www.pcworld.com/index.rss";
        mToolbar = (Toolbar) findViewById(R.id.toolbar);






        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mNavAdapter = new NavigationDrawerAdapter(TITLES,ICONS,NAME, this);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mNavAdapter);                              // Setting the adapter to RecyclerView

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);





        mActivityTitle = getTitle().toString();
        //mDrawerList = (ListView)findViewById(R.id.navList);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);

        if (mToolbar != null) {
            mToolbar.setTitle(mActivityTitle);
            setSupportActionBar(mToolbar);
        }
        setupToggleDrawer();
        setRefreshSwipe();
        addDrawerItems();

        //populate postListData
        RssReaderTask task = new RssReaderTask(); // new task
        task.execute(mainUrl);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //------------------- Class Definitions-------------------------------------

    private class RssReaderTask extends AsyncTask<String, Void, ArrayList<PostData> >{
        @Override
        protected ArrayList<PostData> doInBackground(String... params) {

            // Debug the task thread name
            Log.d("GuardianRssReader", Thread.currentThread().getName());
            try {
                RssReaderProcess rssReader = new RssReaderProcess(params[0]);

                // begin parsing the rss
                return rssReader.getItems();
            }
            catch (Exception e){
                Log.e("GuardianRssReader",e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<PostData> postDatas) {
            super.onPostExecute(postDatas);

            postListData = postDatas;
            setPostListView();
        }
    }

    //-----------------------------Helper functions-------------------------------------

    private void addDrawerItems(){













    }

    private void setupToggleDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void setRefreshSwipe() {
        mSwipeRefreshLayout.setColorScheme(R.color.red,
                android.R.color.holo_green_dark,
                android.R.color.holo_purple,
                android.R.color.holo_orange_dark);
        mSwipeRefreshLayout.setDistanceToTriggerSync(250);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        RssReaderTask task = new RssReaderTask();


//                        ////////animation for refresh on listview/////////
//                        Animation anim = AnimationUtils.loadAnimation(
//                                MainActivity.this, android.R.anim.slide_out_right
//                        );
//                        anim.setDuration(500);
//                        mListView.startAnimation(anim);
//
//                        new Handler().postDelayed(new Runnable() {
//
//                            public void run() {
//
//                                FavouritesManager.getInstance().remove(
//                                        FavouritesManager.getInstance().getTripManagerAtIndex(index)
//                                );
//                                populateList();
//                                adapter.notifyDataSetChanged();
//
//                            }
//
//                        }, anim.getDuration());

                        task.execute(mainUrl);
                        mAdapter.notifyDataSetChanged();


                    }
                }, 1000);
            }
        });
    }

    private void setPostListView() {
        mListView = (ListView) findViewById(R.id.listview);

        mAdapter = new PostListViewAdapter(this, postListData);
        mListView.setAdapter(mAdapter);

        mAdapter.setMode(Attributes.Mode.Single);

        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("ListView", "OnTouch");
                return false;
            }
        });
//        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(mContext, "OnItemLongClickListener", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.e("ListView", "onScrollStateChanged");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ListView", "onItemSelected:" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("ListView", "onNothingSelected:");
            }
        });

        mAdapter.notifyDataSetChanged();

    }

}