package yassin.marc.guardianreader;

import android.text.format.DateUtils;

/**
 * Created by Marc on 7/21/15.
 */
public class PostData {
    public String postThumbUrl;
    public String postDate;
    public String postLink;
    public String postTitle;

    public PostData(){
        super();
    }

    public PostData(String postTitle, String postDate, String postThumbUrl, String postLink) {
        super();
        this.postTitle = postTitle;
        this.postDate = postDate;
        this.postLink = postLink;
        this.postThumbUrl = postThumbUrl;
    }

    public String getPostThumbUrl() {
        return postThumbUrl;
    }

    public void setPostThumbUrl(String postThumbUrl) {
        this.postThumbUrl = postThumbUrl;
    }

    public String getPostLink() {
        return postLink;
    }

    public void setPostLink(String postLink) {
        this.postLink = postLink;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPostTitle(){
        return postTitle;
    }

    public String getPostDate(){
        //String timeAgo = (String) DateUtils.getRelativeTimeSpanString(postDate, 1306767835, 0);
        return postDate;
    }
}