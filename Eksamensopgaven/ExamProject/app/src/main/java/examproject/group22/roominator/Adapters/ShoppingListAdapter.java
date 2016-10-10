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

import examproject.group22.roominator.R;
import examproject.group22.roominator.ShoppingListProvider;

/**
 * Created by Jonas on 10/9/2016.
 */

public class ShoppingListAdapter extends ArrayAdapter {

    List list = new ArrayList();

    public ShoppingListAdapter(Context context, int resource){
        super(context, resource);
    }

    static class DataHandler{
        TextView productname;
        TextView productnumber;
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
            DataHandler handler;
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row=inflater.inflate(R.layout.row_shoppinglist_layout,parent,false);
                handler = new DataHandler();
                handler.productname = (TextView) row.findViewById(R.id.detail_lbDate);
                handler.productnumber = (TextView) row.findViewById(R.id.detail_lbPrice);
                row.setTag(handler);
            }
        else{
                handler = (DataHandler) row.getTag();
            }

        ShoppingListProvider dataprovider;
        dataprovider =(ShoppingListProvider) this.getItem(position);
        handler.productname.setText(dataprovider.getProduct_name());
        handler.productnumber.setText(dataprovider.getProduct_number());

        return row;
    }
}
