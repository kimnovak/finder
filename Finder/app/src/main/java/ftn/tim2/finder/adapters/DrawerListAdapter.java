package ftn.tim2.finder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ftn.tim2.finder.R;
import ftn.tim2.finder.model.NavigationItem;

public class DrawerListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<NavigationItem> navigationItems;

    public DrawerListAdapter(Context context, ArrayList<NavigationItem> navItems) {
        mContext = context;
        navigationItems = navItems;
    }

    @Override
    public int getCount() {
        return navigationItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navigationItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.drawer_list_item, null);
        }
        else {
            view = convertView;
        }

        TextView titleView = view.findViewById(R.id.drawer_title);
        ImageView iconView = view.findViewById(R.id.drawer_icon);

        titleView.setText( navigationItems.get(position).getTitle() );
        iconView.setImageResource(navigationItems.get(position).getIcon());

        return view;
    }
}
