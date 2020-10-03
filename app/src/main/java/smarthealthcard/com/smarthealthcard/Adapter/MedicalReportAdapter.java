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

public class MedicalReportAdapter extends BaseAdapter {
    Context context;
    ArrayList<UploadModel> reportUploadModelArrayList;

    public MedicalReportAdapter(Context context, ArrayList<UploadModel> repoUploadModelArrayList) {
        this.context = context;
        this.reportUploadModelArrayList = repoUploadModelArrayList;
    }

    @Override
    public int getCount() {
        return reportUploadModelArrayList.size();
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
        textView.setText(reportUploadModelArrayList.get(position).getTitle() + "\n" + reportUploadModelArrayList.get(position).getDescription());
        dateText.setText(reportUploadModelArrayList.get(position).getDate());
        convertView.setTag(position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = Integer.parseInt(v.getTag().toString());
                UploadModel reportModel = reportUploadModelArrayList.get(pos);
                Intent intent = new Intent(context, PdfViewerActivity.class);
                intent.putExtra("DOC_URL", reportModel.getDocUrl());
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}