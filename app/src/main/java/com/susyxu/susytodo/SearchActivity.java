package com.susyxu.susytodo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.susyxu.susytodo.Adapters.ScheduleItemsAdapter;
import com.susyxu.susytodo.Database.MyDatabaseHelper;
import com.susyxu.susytodo.MyClass.ScheduleItem;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by susy on 16/7/10.
 */
public class SearchActivity extends AppCompatActivity {
    EditText mEditText;
    private ListView mListView;
    private List<ScheduleItem> mScheduleItems;
    private MyDatabaseHelper dbHelper;
    ScheduleItemsAdapter scheduleItemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //自定义ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);

        mEditText = (EditText)findViewById(R.id.edit_schedule_search);
        mEditText.addTextChangedListener(textWatcher);

        mListView = (ListView)findViewById(R.id.search_listview);
        mScheduleItems = new ArrayList<ScheduleItem>();
        scheduleItemsAdapter = new ScheduleItemsAdapter(SearchActivity.this, mScheduleItems);
        mListView.setAdapter(scheduleItemsAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, DetailsActivity.class);
                intent.putExtra("ScheduleItemsObj", mScheduleItems.get(position));
                startActivity(intent);
            }
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            initItems(s);
            //刷新ListView
            scheduleItemsAdapter.refresh(mScheduleItems);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void initItems(CharSequence s) {
        //查询含有字符的item
        mScheduleItems.clear();
        dbHelper = new MyDatabaseHelper(SearchActivity.this, "BookStore.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sqlString = "select * from schedule where name like '%" + s + "%'";
        Cursor cursor = db.rawQuery(sqlString, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String startDate = cursor.getString(cursor.getColumnIndex("startDate"));
                String endDate = cursor.getString(cursor.getColumnIndex("endDate"));
                String startTime = cursor.getString(cursor.getColumnIndex("startTime"));
                String endTime = cursor.getString(cursor.getColumnIndex("endTime"));
                int alarm = cursor.getInt(cursor.getColumnIndex("alarm"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String comments = cursor.getString(cursor.getColumnIndex("comments"));
                int state = cursor.getInt(cursor.getColumnIndex("state"));
                mScheduleItems.add(new ScheduleItem(id, name, startDate, endDate, startTime, endTime, alarm, type, comments, state));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Intent intent = new Intent(this, MainActivity.class);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScheduleItems.clear();
        initItems(mEditText.getText().toString());
        scheduleItemsAdapter.refresh(mScheduleItems);
    }
}
