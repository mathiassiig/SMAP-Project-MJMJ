package examproject.group22.roominator.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import examproject.group22.roominator.Models.GroceryItemModel;
import examproject.group22.roominator.R;

/**
 * Created by Mathias on 11-10-2016.
 */

public class GroceryItemAdapter extends ArrayAdapter<GroceryItemModel>
{
    ArrayList<GroceryItemModel> groceryItems;
    public GroceryItemAdapter(Context context, ArrayList<GroceryItemModel> groceryItems)
    {
        super(context, 0, groceryItems);
        this.groceryItems = groceryItems;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final GroceryItemModel grocery = getItem(position);
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.groceryitem_layout, parent, false);
        TextView tvName = (TextView) convertView.findViewById(R.id.groceryitem_name);

        tvName.setText(grocery.name);
        return convertView;
    }
}
