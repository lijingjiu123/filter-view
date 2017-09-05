package cn.lijingjiu.filterview.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

import cn.lijingjiu.filterview.FilterView;

public class MainActivity extends AppCompatActivity {

    private FilterView fv;
    private ArrayList<String> filterList;
    private static final int REQUEST_FOR_FILTER = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fv = (FilterView) findViewById(R.id.fv_filter);
        fv.setIconClickListener(new FilterView.IconClickListener() {
            @Override
            public void onIconClick() {
                Intent intent = new Intent(MainActivity.this, SearchFilterActivity.class);
                if (filterList != null && filterList.size() != 0)
                    intent.putStringArrayListExtra("filter", filterList);
                startActivityForResult(intent, REQUEST_FOR_FILTER);
            }

            @Override
            public void onItemDelete() {
                Toast.makeText(MainActivity.this,"delete",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FOR_FILTER && resultCode == RESULT_OK) {
            filterList = data.getStringArrayListExtra("filter_list");
            fv.setData(filterList);
        }
    }
}
