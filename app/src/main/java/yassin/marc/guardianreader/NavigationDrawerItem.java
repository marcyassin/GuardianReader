package yassin.marc.guardianreader;

/**
 * Created by Marc on 7/25/15.
 */
public class NavigationDrawerItem {
    public String feedName;
    public int notificationCountIcon;
    public int rowIconDrawable;

    public NavigationDrawerItem(String feedName, int rowIconDrawable, int notificationCountIcon){
        this.feedName = feedName;
        this.rowIconDrawable = rowIconDrawable;
        this.notificationCountIcon = notificationCountIcon;
    }

}
