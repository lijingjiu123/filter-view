package cn.lijingjiu.filterview.sample;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/18.
 */

public class SearchFilterExpandableAdapter implements ExpandableListAdapter{
    private List<String> groupArray;
    private List<List<String>> childArray;
    private Context mContext;
    private ArrayList<String> filterArray;

    public SearchFilterExpandableAdapter(Context mContext, List<String> groupArray, List<List<String>> childArray, ArrayList<String> filterArray) {
        this.groupArray = groupArray;
        this.childArray = childArray;
        this.mContext = mContext;
        this.filterArray = filterArray;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return groupArray.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childArray.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupArray.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childArray.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
       GroupHolder holder = null;
        if (convertView == null){
            holder = new GroupHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.search_filter_listview_item_group,null);
            holder.groupName = (TextView) convertView.findViewById(R.id.search_filter_listview_group_text);
            holder.arrow = (TextView) convertView.findViewById(R.id.search_filter_listview_group_arrow);
            convertView.setTag(holder);
        }else{
            holder = (GroupHolder) convertView.getTag();
        }
        //判断是否已经打开列表
        if(isExpanded){
            holder.arrow.setRotation(90);
        }else{
            holder.arrow.setRotation(0);
        }
        holder.groupName.setText(groupArray.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder = null;
        if(convertView == null){
            holder = new ChildHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.search_filter_listview_item_child, null);
            holder.childName = (TextView) convertView.findViewById(R.id.search_filter_listview_child_text);
            holder.addIcon = (TextView) convertView.findViewById(R.id.search_filter_listview_child_add);
            holder.llBackground = (LinearLayout) convertView.findViewById(R.id.ll_search_filter_listview_child);
            convertView.setTag(holder);
        }else{
            holder = (ChildHolder) convertView.getTag();
        }
        String child = childArray.get(groupPosition).get(childPosition);
        if (filterArray.contains(child)){
            holder.llBackground.setBackgroundResource(R.color.strong_pink);
        }else {
            holder.llBackground.setBackgroundResource(R.color.tealishThree_60);
        }
        holder.childName.setText(child);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }

    class GroupHolder{
        public TextView groupName;
        public TextView arrow;
    }

    class ChildHolder{
        public TextView childName;
        public TextView addIcon;
        public LinearLayout llBackground;
    }

}
