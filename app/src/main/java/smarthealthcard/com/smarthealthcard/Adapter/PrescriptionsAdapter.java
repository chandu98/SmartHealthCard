package smarthealthcard.com.smarthealthcard.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import smarthealthcard.com.smarthealthcard.Model.UploadModel;
import smarthealthcard.com.smarthealthcard.PdfViewerActivity;
import smarthealthcard.com.smarthealthcard.R;

public class PrescriptionsAdapter extends BaseAdapter {
    Context context;
    ArrayList<UploadModel> prescriptionsUploadModelArrayList;

    public PrescriptionsAdapter(Context context, ArrayList<UploadModel> prescriptionsUploadModelArrayList) {
        this.context = context;
        this.prescriptionsUploadModelArrayList = prescriptionsUploadModelArrayList;
    }

    @Override
    public int getCount() {
        return prescriptionsUploadModelArrayList.size();
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
        TextView dateText = convertView.findViewById(R.id.dateText);
        textView.setText(prescriptionsUploadModelArrayList.get(position).getTitle() + "\n" + prescriptionsUploadModelArrayList.get(position).getDescription());
        dateText.setText(prescriptionsUploadModelArrayList.get(position).getDate());
        convertView.setTag(position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = Integer.parseInt(v.getTag().toString());
                UploadModel reportModel = prescriptionsUploadModelArrayList.get(pos);
                Intent intent = new Intent(context, PdfViewerActivity.class);
                intent.putExtra("DOC_URL", reportModel.getDocUrl());
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}