package com.susy_xu.susy_todo.alarm;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by susy on 16/7/14.
 */
public class AlarmActivity extends Activity {
    MediaPlayer alarmMusic;
    int id = 0;
    String name;
    String startDate;
    String endDate;
    String startTime;
    String endTime;
    String ringtoneUriString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("flagdata", MODE_PRIVATE);
        id = sharedPreferences.getInt("alarmItemId", 0);
        name = sharedPreferences.getString("name", "null");
        startDate = sharedPreferences.getString("startDate", "null");
        endDate = sharedPreferences.getString("endDate", "null");
        startTime = sharedPreferences.getString("startTime", "null");
        endTime = sharedPreferences.getString("endTime", "null");
        ringtoneUriString = sharedPreferences.getString("ringtoneUriString", getSystemDefultRingtoneUri().toString());

        //将设置的铃声转换为Uri类型
        Uri uri = Uri.parse(ringtoneUriString);
        alarmMusic = MediaPlayer.create(this, uri);
        alarmMusic.setLooping(true);
        alarmMusic.start();
        materialDialog.setTitle("" + name);
        materialDialog.show();
        materialDialog.setMessage("" + startDate + " 的 " + startTime + " 到 " + "\n" + endDate + " 的 " + endTime);
    }

    MaterialDialog materialDialog = new MaterialDialog(AlarmActivity.this)
            .setTitle("title")
            .setMessage("gogogogog!")
            .setPositiveButton("关闭", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alarmMusic.stop();

                    AlarmActivity.this.finish();
                }
            });

    private Uri getSystemDefultRingtoneUri() {
        return RingtoneManager.getActualDefaultRingtoneUri(this,
                RingtoneManager.TYPE_RINGTONE);
    }
}
