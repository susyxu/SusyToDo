package com.susyxu.susytodo.Settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.susyxu.susytodo.MainActivity;
import com.susyxu.susytodo.R;

import java.io.File;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by susy on 16/7/5.
 */
public class SettingActivity extends AppCompatActivity {
    private Button mButton01;
    private Button mButton02;

    public static final int ButtonRingtone = 0;
    private String strRingtoneFolder = "/sdcard/music/ringtones";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //自定义ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
        actionBar.setTitle("设置");

        setContentView(R.layout.activity_setting);

        mButton01 = (Button) findViewById(R.id.btn_setting);
        mButton02 = (Button) findViewById(R.id.btn_about);

        mButton01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bFolder(strRingtoneFolder)) {
                    //打开系统铃声设置
                    Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                    //类型为来电RINGTONE
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
                    //设置显示的title
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "设置铃声");
                    //当设置完成之后返回到当前的Activity
                    startActivityForResult(intent, ButtonRingtone);
                }
            }
        });

        mButton02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.show();
            }
        });
    }

    MaterialDialog mMaterialDialog = new MaterialDialog(SettingActivity.this)
            .setTitle("关于软件")
            .setMessage("SusyTodo V1.3"+"\n"+"于2016.07.15更新")
            .setPositiveButton("关闭", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMaterialDialog.dismiss();
                }
            });

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ButtonRingtone:
                try {
                    //得到选择的铃声
                    Uri pickedUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    //将选择的铃声设置成为默认
                    if (pickedUri != null) {
                        //Log.i("test", pickedUri.toString());
                        SharedPreferences.Editor editor = getSharedPreferences("flagdata", MODE_PRIVATE).edit();
                        editor.putString("ringtoneUriString", pickedUri.toString());
                        editor.commit();
                    }
                } catch (Exception e) {
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //检测是否存在指定的文件夹
    //如果不存在则创建
    private boolean bFolder(String strFolder) {
        boolean btmp = false;
        File f = new File(strFolder);
        if (!f.exists()) {
            if (f.mkdirs()) {
                btmp = true;
            } else {
                btmp = false;
            }
        } else {
            btmp = true;
        }
        return btmp;
    }
}
