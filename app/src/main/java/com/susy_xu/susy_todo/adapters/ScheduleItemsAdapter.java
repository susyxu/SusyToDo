package com.susy_xu.susy_todo.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.susy_xu.susy_todo.R;
import com.susy_xu.susy_todo.myClass.ScheduleItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by susy on 16/7/6.
 */
public class ScheduleItemsAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater; //用来读布局
    private List<ScheduleItem> mItems = new ArrayList<ScheduleItem>();//用来存放对象的列表

    public ScheduleItemsAdapter(Context context, List<ScheduleItem> items) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder;

        if (convertView == null) { //加判断，复用，让效率更高
            convertView = mLayoutInflater.inflate(R.layout.schedule_list_items, null);
            myViewHolder = new MyViewHolder();

            //获取控件
            myViewHolder.typeImage = (ImageView) convertView.findViewById(R.id.iv_schedule_item_color_strip);
            myViewHolder.startDate = (TextView) convertView.findViewById(R.id.tv_schedule_item_start_date);
            myViewHolder.startTime = (TextView) convertView.findViewById(R.id.tv_schedule_start_time);
            myViewHolder.name = (TextView) convertView.findViewById(R.id.tv_schedule_title);
            myViewHolder.id = (TextView) convertView.findViewById(R.id.tv_schedule_id);

            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }

        //和数据进行绑定
        if (mItems.get(position).getType().equals("会议")) {
            myViewHolder.typeImage.setBackgroundColor(Color.parseColor("#E91E63"));
        } else if (mItems.get(position).getType().equals("学习")) {
            myViewHolder.typeImage.setBackgroundColor(Color.parseColor("#FF9800"));
        } else if (mItems.get(position).getType().equals("约会")) {
            myViewHolder.typeImage.setBackgroundColor(Color.parseColor("#ffeb38"));
        } else if (mItems.get(position).getType().equals("生活")) {
            myViewHolder.typeImage.setBackgroundColor(Color.parseColor("#00BCD4"));
        } else if (mItems.get(position).getType().equals("娱乐")) {
            myViewHolder.typeImage.setBackgroundColor(Color.parseColor("#9C27B0"));
        }
        if (mItems.get(position).getState() == 1) { //已经完成
            myViewHolder.typeImage.setBackgroundColor(Color.parseColor("#4CAF50"));
        }

        myViewHolder.startDate.setText(mItems.get(position).getStartDate());
        myViewHolder.startTime.setText(mItems.get(position).getStartTime());
        myViewHolder.name.setText(mItems.get(position).getName());
        myViewHolder.id.setText(String.valueOf(mItems.get(position).getId()));

        return convertView;
    }

    class MyViewHolder {
        ImageView typeImage;
        TextView startDate;
        TextView startTime;
        TextView name;
        TextView id;
    }

    /*刷新数据的方法*/
    public void refresh(List<ScheduleItem> items) {
        mItems = items;
        notifyDataSetChanged();
    }
}
