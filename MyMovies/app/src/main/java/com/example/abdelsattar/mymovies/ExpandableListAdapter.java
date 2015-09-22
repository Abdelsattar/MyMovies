package com.example.abdelsattar.mymovies;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Mohammed on 9/20/2015.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<Object>> _listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<Object>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public String[] getChild(int groupPosition, int childPosititon) {

        Review review;
        Video video = new Video();

        String[] data;
        if(this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon).getClass().equals(video.getClass())){

            video = (Video) this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosititon);

            data = new String[1];
            data[0]=  video.getName();
        }
        else{

            review = (Review) this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosititon);
            data = new String[2];
            data[0] = review.getAuthor();
            data[1]=  review.getContent();

        }
        return data;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String[] childText =  getChild(groupPosition, childPosition);

        if ( childText.length==1 ) {

            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.video_list_item, null);
            Log.d("Where", "length is 1 and convert list ");
            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.Title);

            TextView txtListChild2 = (TextView) convertView
                    .findViewById(R.id.Title);

            txtListChild.setText(childText[0]);
            txtListChild2.setText(childText[0]);
            Log.d("Where", "length is 1 ");

            //           }else {

            //        }
        } else {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.review_list_item, null);

            TextView author = (TextView) convertView
                    .findViewById(R.id.authorTV);
            TextView content = (TextView) convertView
                    .findViewById(R.id.contentTV);

            author.setText(childText[0]);
            content.setText(childText[1]);

        }

        return convertView;
    }
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
