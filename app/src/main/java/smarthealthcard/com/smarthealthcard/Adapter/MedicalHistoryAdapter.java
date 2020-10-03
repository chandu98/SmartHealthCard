package smarthealthcard.com.smarthealthcard.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import smarthealthcard.com.smarthealthcard.Model.HistoryModel;
import smarthealthcard.com.smarthealthcard.Model.UploadModel;
import smarthealthcard.com.smarthealthcard.PdfViewerActivity;
import smarthealthcard.com.smarthealthcard.R;

public class MedicalHistoryAdapter extends BaseAdapter {
    Context context;
    ArrayList<HistoryModel> historyUploadModelArrayList;

    public MedicalHistoryAdapter(Context context, ArrayList<HistoryModel> historyUploadModelArrayList) {
        this.context = context;
        this.historyUploadModelArrayList = historyUploadModelArrayList;
    }

    @Override
    public int getCount() {
        return historyUploadModelArrayList.size();
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
        textView.setText("Age: "+historyUploadModelArrayList.get(position).getAge() + "\nWeight: " + historyUploadModelArrayList.get(position).getWeight()+ "\nBlood Group: " + historyUploadModelArrayList.get(position).getBloodGroup()+ "\nDisease: " + historyUploadModelArrayList.get(position).getDisease()+ "\nAllergy: " + historyUploadModelArrayList.get(position).getAllergy());
        dateText.setText(historyUploadModelArrayList.get(position).getDate());
        return convertView;
    }
}
