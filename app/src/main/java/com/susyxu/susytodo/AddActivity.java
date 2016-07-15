package com.susyxu.susytodo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.susyxu.susytodo.Alarm.AlarmReceiver;
import com.susyxu.susytodo.Database.MyDatabaseHelper;
import com.susyxu.susytodo.MyClass.ScheduleItem;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by susy on 16/7/5.
 */
public class AddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String START_DATE_PICK_DLG_TAG = "start_date_pick_dlg";
    private static final String END_DATE_PICK_DLG_TAG = "end_date_pick_dlg";
    private static final String START_TIME_PICK_DLG_TAG = "start_date_pick_dlg";
    private static final String END_TIME_PICK_DLG_TAG = "end_date_pick_dlg";

    private Button mStartDate;
    private Button mEndDate;
    private Button mStartTime;
    private Button mEndTime;
    private Button mState;
    private Button mAlarm;
    private Button mType;
    private EditText mName;
    private EditText mComments;

    private String controlFlag;
    private int scheduleId;
    private MyDatabaseHelper dbHelper;

    Calendar mCalendar;//广播设置闹钟时候用到

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //要将这个activity的theme在manifest里设置成NoActionBar，因为AppCompatActivity本身就有ActionBar了
        setContentView(R.layout.activity_add);

        //mCalendar初始化，后面在在picker里面正式设置
        mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());

        //自定义ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);

        //设置开始 和 结束日期
        mStartDate = (Button) findViewById(R.id.btn_schedule_date_start);
        mEndDate = (Button) findViewById(R.id.btn_schedule_date_end);
        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartDatePickerDialog();
            }
        });
        mEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndDatePickerDialog();
            }
        });

        //设置开始 和 结束时间
        mStartTime = (Button) findViewById(R.id.btn_schedule_time_start);
        mEndTime = (Button) findViewById(R.id.btn_schedule_time_end);
        mStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartTimePickerDialog();
            }
        });
        mEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndTimePickerDialog();
            }
        });

        //设置状态
        mState = (Button) findViewById(R.id.btn_state);
        mState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList<String>();
                list.add("已完成");
                list.add("未完成");
                new LovelyChoiceDialog(AddActivity.this)
                        .setTopColorRes(R.color.colorAccent)
                        .setTitle("请选择事务状态")
                        .setItems(list, new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                            @Override
                            public void onItemSelected(int position, String item) {
                                if (item.equals("已完成"))
                                    mState.setText("已完成");
                                else if(item.equals("未完成"))
                                    mState.setText("未完成");
                            }
                        })
                        .show();
            }
        });

        //设置闹钟提醒
        mAlarm = (Button) findViewById(R.id.btn_alarm_time);
        mAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList<String>();
                list.add("不提醒");
                list.add("提前10分钟提醒");
                list.add("提前30分钟提醒");
                list.add("提前1小时提醒");
                new LovelyChoiceDialog(AddActivity.this)
                        .setTopColorRes(R.color.colorAccent)
                        .setTitle("请选择事务提醒")
                        .setItems(list, new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                            @Override
                            public void onItemSelected(int position, String item) {
                                if (item.equals("不提醒"))
                                    mAlarm.setText("不提醒");
                                else if(item.equals("提前10分钟提醒"))
                                    mAlarm.setText("提前10分钟提醒");
                                else if(item.equals("提前30分钟提醒"))
                                    mAlarm.setText("提前30分钟提醒");
                                else if(item.equals("提前1小时提醒"))
                                    mAlarm.setText("提前1小时提醒");
                            }
                        })
                        .show();
            }
        });

        //设置分类
        mType = (Button) findViewById(R.id.btn_schedule_type);
        mType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList<String>();
                list.add("会议");
                list.add("学习");
                list.add("约会");
                list.add("生活");
                list.add("娱乐");
                new LovelyChoiceDialog(AddActivity.this)
                        .setTopColorRes(R.color.colorAccent)
                        .setTitle("请选择事务类型")
                        .setItems(list, new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                            @Override
                            public void onItemSelected(int position, String item) {
                                if (item.equals("会议"))
                                    mType.setText("会议");
                                else if(item.equals("学习"))
                                    mType.setText("学习");
                                else if(item.equals("约会"))
                                    mType.setText("约会");
                                else if(item.equals("生活"))
                                    mType.setText("生活");
                                else if(item.equals("娱乐"))
                                    mType.setText("娱乐");
                            }
                        })
                        .show();
            }
        });

        //标题和备注
        mName = (EditText) findViewById(R.id.edit_schedule_title);
        mComments = (EditText) findViewById(R.id.edit_schedule_note);

        //获取传递过来的对象
        final ScheduleItem scheduleItem = (ScheduleItem) getIntent().getSerializableExtra("ScheduleItem");
        controlFlag = getIntent().getStringExtra("updateORInsert");

        //新建一个事务的各个控件初始值
        if (controlFlag.equals("insert")) {
            Calendar now = Calendar.getInstance();
            String date;
            String month = (now.get(Calendar.MONTH) + 1) + "";
            String day = now.get(Calendar.DAY_OF_MONTH) + "";
            if ((now.get(Calendar.MONTH) + 1) <= 8)
                month = "0" + month;
            if (now.get(Calendar.DAY_OF_MONTH) <= 9)
                day = "0" + day;
            date = now.get(Calendar.YEAR) + "." + month + "." + day;
            mStartDate.setText(date);
            mEndDate.setText(date);
            mStartTime.setText("08:00");
            mEndTime.setText("10:00");
        }
        //修改一个事务的各个控件初始值
        else if (controlFlag.equals("update")) {
            scheduleId = scheduleItem.getId();

            mName.setText(scheduleItem.getName());
            mStartDate.setText(scheduleItem.getStartDate());
            mEndDate.setText(scheduleItem.getEndDate());
            mStartTime.setText(scheduleItem.getStartTime());
            mEndTime.setText(scheduleItem.getEndTime());
            if (scheduleItem.getState() == 0) {
                mState.setText("未完成");
                mState.setTextColor(0xFFF44336);
            } else {
                mState.setText("已完成");
                mState.setTextColor(0xFF4CAF50);
            }
            if (scheduleItem.getAlarm() == 0)
                mAlarm.setText("不提醒");
            else if (scheduleItem.getAlarm() == 10)
                mAlarm.setText("提前10分钟提醒");
            else if (scheduleItem.getAlarm() == 30)
                mAlarm.setText("提前30分钟提醒");
            else if (scheduleItem.getAlarm() == 60)
                mAlarm.setText("提前1小时提醒");
            if (scheduleItem.getType().equals("会议"))
                mType.setText("会议");
            else if (scheduleItem.getType().equals("学习"))
                mType.setText("学习");
            else if (scheduleItem.getType().equals("约会"))
                mType.setText("约会");
            else if (scheduleItem.getType().equals("生活"))
                mType.setText("生活");
            else if (scheduleItem.getType().equals("娱乐"))
                mType.setText("娱乐");
            mComments.setText(scheduleItem.getComments());
        }
    }

    //初始化提示对话框
    MaterialDialog mWarningMaterialDialog = new MaterialDialog(this)
            .setTitle("提示")
            .setMessage("请填写事务名称")
            .setPositiveButton("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mWarningMaterialDialog.dismiss();
                }
            });

    //初始化提示对话框2
    MaterialDialog mWarning2MaterialDialog = new MaterialDialog(this)
            .setTitle("提示")
            .setMessage("请填写正确的时间")
            .setPositiveButton("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mWarning2MaterialDialog.dismiss();
                }
            });

    //选择结束时间的对话框
    private void showEndTimePickerDialog() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                        String hour = hourOfDay + "";
                        String min = minute + "";
                        if (hourOfDay <= 9)
                            hour = "0" + hour;
                        if (minute <= 9)
                            min = "0" + min;
                        mEndTime.setText(hour + ":" + min);
                    }
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.show(getFragmentManager(), END_TIME_PICK_DLG_TAG);
    }

    //选择开始时间的对话框
    private void showStartTimePickerDialog() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                        String hour = hourOfDay + "";
                        String min = minute + "";
                        if (hourOfDay <= 9)
                            hour = "0" + hour;
                        if (minute <= 9)
                            min = "0" + min;
                        mStartTime.setText(hour + ":" + min);
                        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        mCalendar.set(Calendar.MINUTE, minute);
                        mCalendar.set(Calendar.SECOND, 0);
                    }
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.show(getFragmentManager(), START_TIME_PICK_DLG_TAG);
    }

    //选择结束日期的对话框
    private void showEndDatePickerDialog() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                AddActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), END_DATE_PICK_DLG_TAG);
    }

    //选择开始日期的对话框
    private void showStartDatePickerDialog() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                AddActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), START_DATE_PICK_DLG_TAG);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date;
        String month = (monthOfYear + 1) + "";
        String day = dayOfMonth + "";
        switch (view.getTag()) {
            case START_DATE_PICK_DLG_TAG:
                if (monthOfYear <= 8)
                    month = "0" + month;
                if (dayOfMonth <= 9)
                    day = "0" + day;
                date = year + "." + month + "." + day;
                mStartDate.setText(date);
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH,monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                break;
            case END_DATE_PICK_DLG_TAG:
                if (monthOfYear <= 8)
                    month = "0" + month;
                if (dayOfMonth <= 9)
                    day = "0" + day;
                date = year + "." + month + "." + day;
                mEndDate.setText(date);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Intent intent = new Intent(this, MainActivity.class);
        switch (item.getItemId()) {
            case R.id.action_save:
                if (mName.getText().toString().length() == 0 || mName.getText().toString().trim().length() == 0) {  //判断信息是否完整
                    mWarningMaterialDialog.show();
                    return true;
                }
                else if(comfirmTimeLegal()==false){
                    mWarning2MaterialDialog.show();
                    return true;
                }
                else {
                    //对添加的数据进行保存insert，广播增加闹钟，请求码为id
                    if (controlFlag.equals("insert")) {
                        insetItemToSQLite();
                    }
                    //对当前事务进行修改update，广播删除闹钟，重新增加，请求码为id
                    else if (controlFlag.equals("update")) {
                        updateItemToSQLite();
                    }
                    //NavUtils.navigateUpTo(this, intent);
                    finish();
                    return true;
                }
            case android.R.id.home:
                //NavUtils.navigateUpTo(this, intent);
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateItemToSQLite() {
        dbHelper = new MyDatabaseHelper(AddActivity.this, "BookStore.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int alarm = 0;
        if(mAlarm.getText().toString().equals("不提醒"))
            alarm = 0;
        else if (mAlarm.getText().toString().equals("提前10分钟提醒"))
            alarm = 10;
        else if (mAlarm.getText().toString().equals("提前30分钟提醒"))
            alarm = 30;
        else if (mAlarm.getText().toString().equals("提前1小时提醒"))
            alarm = 60;
        int state = 0;
        if(mState.getText().toString().equals("已完成"))
            state = 1;
        else if (mState.getText().toString().equals("未完成"))
            state = 0;
        String[] sqlVar = new String[]{
                mName.getText().toString(),
                mStartDate.getText().toString(),
                mEndDate.getText().toString(),
                mStartTime.getText().toString(),
                mEndTime.getText().toString(),
                String.valueOf(alarm),
                mType.getText().toString(),
                mComments.getText().toString(),
                String.valueOf(state),
                String.valueOf(scheduleId)
                };
        db.execSQL("update schedule set name=?, startDate=?, endDate=?, startTime=?, endTime=?, alarm=?, type=?, comments=?, state=? where id=?"
                , sqlVar);
        //广播删除闹钟，重新增加，请求码为id(即scheduleId)
        //(因为AlarmManager相同id后一个会重写前一个，所以再写一遍就可以)
        if(mAlarm.getText().toString().equals("不提醒")) { ; }
        else {
            int before = 0;
            if(mAlarm.getText().toString().equals("提前10分钟提醒"))
                before = 10*60*1000;
            else if(mAlarm.getText().toString().equals("提前30分钟提醒"))
                before = 30*60*1000;
            else if(mAlarm.getText().toString().equals("提前1小时提醒"))
                before = 60*60*1000;
            Intent intent = new Intent(AddActivity.this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(AddActivity.this, scheduleId, intent, 0);
            //用来传id号
            SharedPreferences.Editor editor = getSharedPreferences("flagdata", MODE_PRIVATE).edit();
            editor.putInt("alarmItemId", scheduleId);
            editor.putString("name", mName.getText().toString());
            editor.putString("startDate", mStartDate.getText().toString());
            editor.putString("endDate", mEndDate.getText().toString());
            editor.putString("startTime", mStartTime.getText().toString());
            editor.putString("endTime",mEndTime.getText().toString());
            editor.commit();
            AlarmManager am;
            am = (AlarmManager) getSystemService(ALARM_SERVICE);
            //这里需要加上一个判断，若是过去时间就不广播了
            if (mCalendar.getTimeInMillis()-before > System.currentTimeMillis()){
                am.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis()-before, pendingIntent);
            }
        }
    }

    private void insetItemToSQLite() {
        dbHelper = new MyDatabaseHelper(AddActivity.this, "BookStore.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select max(id) as max_id from schedule", null);
        int max_id = 0;
        if (cursor.moveToFirst()) {
            do {
                max_id = cursor.getInt(cursor.getColumnIndex("max_id"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        int alarm = 0;
        if(mAlarm.getText().toString().equals("不提醒"))
            alarm = 0;
        else if (mAlarm.getText().toString().equals("提前10分钟提醒"))
            alarm = 10;
        else if (mAlarm.getText().toString().equals("提前30分钟提醒"))
            alarm = 30;
        else if (mAlarm.getText().toString().equals("提前1小时提醒"))
            alarm = 60;
        int state = 0;
        if(mState.getText().toString().equals("已完成"))
            state = 1;
        else if (mState.getText().toString().equals("未完成"))
            state = 0;
        String[] sqlVar = new String[]{
                String.valueOf(max_id + 1),
                mName.getText().toString(),
                mStartDate.getText().toString(),
                mEndDate.getText().toString(),
                mStartTime.getText().toString(),
                mEndTime.getText().toString(),
                String.valueOf(alarm),
                mType.getText().toString(),
                mComments.getText().toString(),
                String.valueOf(state)};
        db.execSQL("insert into schedule (id, name, startDate, endDate, startTime, endTime, alarm, type, comments, state) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                , sqlVar);
        //广播增加闹钟，请求码为id(即max_id + 1)
        if(mAlarm.getText().toString().equals("不提醒")) { ; }
        else {
            int before = 0;
            if(mAlarm.getText().toString().equals("提前10分钟提醒"))
                before = 10*60*1000;
            else if(mAlarm.getText().toString().equals("提前30分钟提醒"))
                before = 30*60*1000;
            else if(mAlarm.getText().toString().equals("提前1小时提醒"))
                before = 60*60*1000;
            Intent intent = new Intent(AddActivity.this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(AddActivity.this, max_id + 1, intent, 0);
            //用来传id号
            SharedPreferences.Editor editor = getSharedPreferences("flagdata", MODE_PRIVATE).edit();
            editor.putInt("alarmItemId", max_id + 1);
            editor.putString("name", mName.getText().toString());
            editor.putString("startDate", mStartDate.getText().toString());
            editor.putString("endDate", mEndDate.getText().toString());
            editor.putString("startTime", mStartTime.getText().toString());
            editor.putString("endTime",mEndTime.getText().toString());
            editor.commit();
            AlarmManager am;
            am = (AlarmManager) getSystemService(ALARM_SERVICE);
            //这里需要加上一个判断，若是过去时间就不广播了
            if (mCalendar.getTimeInMillis()-before > System.currentTimeMillis()){
                am.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis()-before, pendingIntent);
            }
        }
    }

    public boolean comfirmTimeLegal(){
        int startYear;
        int startMonth;
        int startDay;

        int endYear;
        int endMonth;
        int endDay;

        int startHour;
        int startMinute;

        int endHour;
        int endMinute;
        String[] arrayStartDate = mStartDate.getText().toString().split("\\.");
        startYear = Integer.parseInt(arrayStartDate[0]);
        startMonth = Integer.parseInt(arrayStartDate[1]);
        startDay = Integer.parseInt(arrayStartDate[2]);

        String[] arrayEndDate = mEndDate.getText().toString().split("\\.");
        endYear = Integer.parseInt(arrayEndDate[0]);
        endMonth = Integer.parseInt(arrayEndDate[1]);
        endDay = Integer.parseInt(arrayEndDate[2]);

        String[] arrayStartTime = mStartTime.getText().toString().split(":");
        startHour = Integer.parseInt(arrayStartTime[0]);
        startMinute = Integer.parseInt(arrayStartTime[1]);

        String[] arrayEndTime = mEndTime.getText().toString().split(":");
        endHour = Integer.parseInt(arrayEndTime[0]);
        endMinute = Integer.parseInt(arrayEndTime[1]);

        if(startYear>endYear)
            return false;
        else{
            if(startMonth>endMonth)
                return false;
            else{
                if(startDay>endDay)
                    return false;
                else{
                    if(startHour>endHour)
                        return false;
                    else{
                        if (startMinute>endMinute)
                            return false;
                        else
                            return true;
                    }
                }
            }
        }
    }
}
