package smarthealthcard.com.smarthealthcard.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import smarthealthcard.com.smarthealthcard.Model.RecentModel;
import smarthealthcard.com.smarthealthcard.R;

public class RecentAdapter extends BaseAdapter {
    Context context;
    ArrayList<RecentModel> recentModelArrayList;

    public RecentAdapter(Context context, ArrayList<RecentModel> recentModelArrayList) {
        this.context = context;
        this.recentModelArrayList = recentModelArrayList;
    }

    @Override
    public int getCount() {
        return recentModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.recent_list, null);
        TextView textView = convertView.findViewById(R.id.remarkText);
        TextView textView2 = convertView.findViewById(R.id.sent_by);
        TextView dateText = convertView.findViewById(R.id.dateText);
        textView.setText("Sent By: "+recentModelArrayList.get(position).getEmail()+"\n\nRemarks: "+recentModelArrayList.get(position).getRemarks());
        dateText.setText("\n\n"+recentModelArrayList.get(position).getDate());
//        convertView.setTag(position);
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int pos = Integer.parseInt(v.getTag().toString());
//                RecentModel recentModel = recentModelArrayList.get(pos);
//                Gson gson = new Gson();
//                String eventJson = gson.toJson(recentModel);
//                Intent intent = new Intent(context, ViewVolunteerEventActivity.class);
//                intent.putExtra("EventData", eventJson);
//                context.startActivity(intent);
//            }
//        });
        return convertView;
    }
}