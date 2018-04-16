package ftn.tim2.finder.model;

import android.os.Parcel;
import android.os.Parcelable;

public class NavigationItem implements Parcelable {

    private String title;
    private int icon;

    public NavigationItem() {

    }

    public NavigationItem(String title, int icon) {

        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(icon);
    }
}
