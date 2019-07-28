package com.susy_xu.susy_todo.mainFragments;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.susy_xu.susy_todo.DetailsActivity;
import com.susy_xu.susy_todo.R;
import com.susy_xu.susy_todo.adapters.ScheduleItemsAdapter;
import com.susy_xu.susy_todo.database.MyDatabaseHelper;
import com.susy_xu.susy_todo.myClass.ScheduleItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by susy on 16/7/6.
 */
public class StudyFragment extends Fragment {
    private ListView mListView;
    private List<ScheduleItem> mScheduleItems;
    private MyDatabaseHelper dbHelper;
    ScheduleItemsAdapter scheduleItemsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, null);
        mListView = (ListView) view.findViewById(R.id.all_listview);

        initItems();

        scheduleItemsAdapter = new ScheduleItemsAdapter(getActivity(), mScheduleItems);
        mListView.setAdapter(scheduleItemsAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("ScheduleItemsObj", mScheduleItems.get(position));
                startActivity(intent);
            }
        });
        return view;
    }

    private void initItems() {
        dbHelper = new MyDatabaseHelper(getActivity(), "BookStore.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from schedule where state = 0 and type='学习'", null);
        mScheduleItems = new ArrayList<ScheduleItem>();

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
    public void onResume() {
        super.onResume();
        mScheduleItems.clear();
        initItems();
        scheduleItemsAdapter.refresh(mScheduleItems);
    }
}
