package com.susyxu.susytodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.susyxu.susytodo.MyClass.ScheduleItem;

/**
 * Created by susy on 16/7/6.
 */
public class DetailsActivity extends AppCompatActivity{

    private Button mStartDate;
    private Button mEndDate;
    private Button mStartTime;
    private Button mEndTime;
    private Button mState;
    private Button mAlarm;
    private Button mType;
    private EditText mComments;

    private int schedule_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
            actionBar.setTitle("sch name");
        }

        //获取控件
        mStartDate = (Button)findViewById(R.id.btn_schedule_date_start);
        mEndDate = (Button)findViewById(R.id.btn_schedule_date_end);
        mStartTime = (Button)findViewById(R.id.btn_schedule_time_start);
        mEndTime = (Button)findViewById(R.id.btn_schedule_time_end);
        mState = (Button)findViewById(R.id.btn_state);
        mAlarm = (Button)findViewById(R.id.btn_alarm_time);
        mType = (Button)findViewById(R.id.btn_schedule_type);
        mComments = (EditText)findViewById(R.id.edit_schedule_note);

        //获取传过来的ScheduleItem对象
        final ScheduleItem scheduleItem = (ScheduleItem)getIntent().getSerializableExtra("ScheduleItemsObj");
        actionBar.setTitle(scheduleItem.getName());
        mStartDate.setText(scheduleItem.getStartDate());
        mEndDate.setText(scheduleItem.getEndDate());
        mStartTime.setText(scheduleItem.getStartTime());
        mEndTime.setText(scheduleItem.getEndTime());
        if(scheduleItem.getState()==0){
            mState.setText("未完成");
            mState.setTextColor(0xFFF44336);
        }
        else{
            mState.setText("已完成");
            mState.setTextColor(0xFF4CAF50);
        }
        if(scheduleItem.getAlarm()==0)
            mAlarm.setText("不提醒");
        else if(scheduleItem.getAlarm()==10)
            mAlarm.setText("提前10分钟提醒");
        else if(scheduleItem.getAlarm()==30)
            mAlarm.setText("提前30分钟提醒");
        else if(scheduleItem.getAlarm()==60)
            mAlarm.setText("提前1小时提醒");
        if(scheduleItem.getType().equals("会议"))
            mType.setText("会议");
        else if(scheduleItem.getType().equals("学习"))
            mType.setText("学习");
        else if(scheduleItem.getType().equals("约会"))
            mType.setText("约会");
        else if(scheduleItem.getType().equals("生活"))
            mType.setText("生活");
        else if(scheduleItem.getType().equals("娱乐"))
            mType.setText("娱乐");
        mComments.setText(scheduleItem.getComments());

        //获取当前id方便数据库的删除
        schedule_id = scheduleItem.getId();

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.edit_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this,AddActivity.class);
                intent.putExtra("updateORInsert","update");
                intent.putExtra("ScheduleItem",scheduleItem); //传递当前的item过去
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_details_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class);
        switch (item.getItemId()){
            case R.id.action_del:
                //把id为schedule_id的事务删除
                NavUtils.navigateUpTo(this, intent);
                return true;
            case android.R.id.home:
                NavUtils.navigateUpTo(this, intent);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
