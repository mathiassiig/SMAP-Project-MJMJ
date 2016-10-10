package examproject.group22.roominator.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import examproject.group22.roominator.DetailListProvider;
import examproject.group22.roominator.R;


/**
 * Created by Maria Dam on 10-10-2016.
 */

public class DetailListAdapter extends ArrayAdapter{
    List list = new ArrayList();

    public DetailListAdapter(Context context, int resource){
        super(context, resource);
    }

    static class DataHandler{
        TextView productname;
        TextView productprice;
    }

    @Override
    public void add(Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        DetailListAdapter.DataHandler handler;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.custom_details,parent,false);
            handler = new DetailListAdapter.DataHandler();
            handler.productname = (TextView) row.findViewById(R.id.detail_lbproduct);
            handler.productprice = (TextView) row.findViewById(R.id.detail_lbPrice);
            //handler.productprice = (TextView) row.findViewById(R.id.TextviewProductnumber);
            row.setTag(handler);
        }
        else{
            handler = (DetailListAdapter.DataHandler) row.getTag();
        }

        DetailListProvider dataprovider;
        dataprovider =(DetailListProvider) this.getItem(position);
        handler.productname.setText(dataprovider.getProductName());
        handler.productprice.setText(dataprovider.getProductPrice());

        return row;
    }
}
