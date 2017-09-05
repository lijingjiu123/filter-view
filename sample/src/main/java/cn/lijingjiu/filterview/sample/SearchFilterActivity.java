package cn.lijingjiu.filterview.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchFilterActivity extends AppCompatActivity{
    private TextView  tvFilterTitle, tvFilterSubTitle, tvFilterArrow;
    private ExpandableListView eplv;
    private SearchFilterExpandableAdapter epAdapter;
    private List<String> groupArray;
    private List<List<String>> childArray;
    private ArrayList<String> filterList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filter);
        initViews();
        initData();
        setViews();
    }

    private void initData() {
        groupArray = Arrays.asList("Equipment","Fitness Level","Body Part");

        childArray = Arrays.asList(Arrays.asList("No Equipment","Smart Devices","Equipment"),
                                    Arrays.asList("Elementary","Intermediate","Advanced"),
                                    Arrays.asList("Shoulder","The Front Shoulder","The Back Shoulder","Neck","Chest","Back","Upper Limb Biceps","Up to the triceps","Abdomen","Hip","The Thigh","The Calf","The Waist"));
        filterList = getIntent().getStringArrayListExtra("filter");
        if (filterList == null) filterList = new ArrayList<>();

    }

    private void showSubTitle(int pos){
        tvFilterSubTitle.setText(groupArray.get(pos));
        tvFilterSubTitle.setVisibility(View.VISIBLE);
        tvFilterArrow.setVisibility(View.VISIBLE);
    }
    private void hideSubTitle(){
        tvFilterSubTitle.setVisibility(View.GONE);
        tvFilterArrow.setVisibility(View.GONE);
    }

    private void setViews() {

        eplv.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < epAdapter.getGroupCount(); i++) {
                    if (groupPosition != i) eplv.collapseGroup(i);
                }
                showSubTitle(groupPosition);
            }
        });
        eplv.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                hideSubTitle();
            }
        });
        eplv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String child = childArray.get(groupPosition).get(childPosition);
                if (filterList.contains(child)){
                    filterList.remove(child);
                }else{
                    filterList.add(child);

                }

                eplv.collapseGroup(groupPosition);
                eplv.expandGroup(groupPosition);
                return true;
            }
        });
        epAdapter = new SearchFilterExpandableAdapter(this,groupArray,childArray, filterList);
        eplv.setAdapter(epAdapter);
    }



    private void initViews() {

        tvFilterTitle = (TextView) findViewById(R.id.tv_discover_search_filter_title);
        tvFilterSubTitle = (TextView) findViewById(R.id.tv_discover_search_filter_subtitle);
        tvFilterArrow = (TextView) findViewById(R.id.tv_discover_search_filter_arrow);
        eplv = (ExpandableListView) findViewById(R.id.eplv_discover_search_filter);
    }

    private void setResultData(){
        Intent data = new Intent();
        data.putStringArrayListExtra("filter_list", filterList);
        setResult(RESULT_OK,data);
    }

    @Override
    public void onBackPressed() {
        setResultData();
        super.onBackPressed();
    }
}
