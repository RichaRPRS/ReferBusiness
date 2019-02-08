package rprs.business.refer.referbusiness;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class BusinessAdapter extends ArrayAdapter<BusinessModel> {

    private ArrayList<BusinessModel> dataSet;
    Context mContext;
    Activity activity;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtType;
        TextView status;
    }

    public BusinessAdapter(ArrayList<BusinessModel> data, Context context,Activity activity) {
        super(context, R.layout.layout_list, data);
        this.dataSet = data;
        this.mContext=context;
        this.activity=activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final BusinessModel dataModel = getItem(position);
        BusinessAdapter.ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new BusinessAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.layout_list, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.type);
            viewHolder.status = (TextView) convertView.findViewById(R.id.status);

            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (BusinessAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }
        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.txtType.setText(dataModel.getMobile());
        viewHolder.status.setText(dataModel.getStatus());
        if (dataModel.getStatus().equalsIgnoreCase("in process")){
            viewHolder.status.setTextColor(Color.RED);
        }else if (dataModel.getStatus().equalsIgnoreCase("Project Completed")){
            viewHolder.status.setTextColor(Color.BLUE);
        }else {
            viewHolder.status.setTextColor(mContext.getResources().getColor(R.color.ForestGreen));
        }

        return convertView;
    }
}
