package yassin.marc.guardianreader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Marc on 7/21/15.
 */
public class PostListViewAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private ArrayList<PostData> mPostDataArrayList;
    private ImageView nullThumbnail;

    public PostListViewAdapter(Context mContext, ArrayList<PostData> pdList) {
        super();
        this.mContext = mContext;
        this.mPostDataArrayList = pdList;
    }


    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.post_item, null);
        SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        return v;
    }



    @Override
    public void fillValues(int position, View convertView) {
        TextView postTitleTextView = (TextView)convertView.findViewById(R.id.postTitleLabel);
        postTitleTextView.setText(mPostDataArrayList.get(position).getPostTitle());
        TextView postDateTextView = (TextView)convertView.findViewById(R.id.postDateLabel);
        postDateTextView.setText(mPostDataArrayList.get(position).getPostDate());
        TextView postLinkTextView = (TextView)convertView.findViewById(R.id.postLinkUrl);
        postLinkTextView.setText(mPostDataArrayList.get(position).getPostLink());
        if (mPostDataArrayList.get(position).getPostThumbUrl() == (null)){}
        else {
            new DownloadImageTask((ImageView) convertView.findViewById(R.id.postThumb))
                    .execute(mPostDataArrayList.get(position).getPostThumbUrl());
        }

        SwipeLayout swipeLayout = (SwipeLayout)convertView.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.save_button));
            }
        });

        setBottomClicks(convertView, swipeLayout, position); // set actions for the bottom view action buttons


    }

    @Override
    public int getCount() {
        return mPostDataArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPostDataArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void setBottomClicks(View v, SwipeLayout swipeLayout, final int position) {
//        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
//            @Override
//            public void onDoubleClick(SwipeLayout layout, boolean surface) {
//                Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
//
//                //Double Click code
//            }
//        });

        v.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "save", Toast.LENGTH_SHORT).show();
                // Save post
            }
        });
        v.findViewById(R.id.open_in_browser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "open in browser", Toast.LENGTH_SHORT).show();
//                 open in browser code
                String browserUrl = mPostDataArrayList.get(position).postLink;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(browserUrl));
                mContext.startActivity(i);

            }
        });
        v.findViewById(R.id.hide_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "hide", Toast.LENGTH_SHORT).show();
                // hide post code
            }
        });
        v.findViewById(R.id.share_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "share", Toast.LENGTH_SHORT).show();
                // share post code
            }
        });
        v.findViewById(R.id.mark_read_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "mark read", Toast.LENGTH_SHORT).show();
                // mark read code
//                LinearLayout linlayout =  (LinearLayout) view.findViewById(R.id.linearLayout);
//                linlayout.setBackgroundColor(R.color.red);
            }
        });
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}

