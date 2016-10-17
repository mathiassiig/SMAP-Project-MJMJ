package examproject.group22.roominator.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import examproject.group22.roominator.Activities.OverviewActivity;
import examproject.group22.roominator.Adapters.GroceryItemAdapter;
import examproject.group22.roominator.Adapters.UserInfoAdapter;
import examproject.group22.roominator.Models.Apartment;
import examproject.group22.roominator.Models.GroceryItem;
import examproject.group22.roominator.Models.User;
import examproject.group22.roominator.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductListFragment.GroceryItemClickListener} interface
 * to handle interaction events.
 * Use the {@link ProductListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductListFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public ArrayList<GroceryItem> groceries = new ArrayList<>();

    FloatingActionButton fab;
    ListView listView;


    private GroceryItemClickListener  mListener;

    public ProductListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume()
    {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReciever,new IntentFilter(OverviewActivity.INTENT_UPDATE_GROCERIES_FRAGMENT));
    }

    @Override
    public void onPause()
    {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReciever);
        super.onPause();
    }

    private BroadcastReceiver mReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            groceries.clear();
            ArrayList<GroceryItem> allGroceries = (ArrayList<GroceryItem>) intent.getSerializableExtra("groceries");
            groceries.addAll(allGroceries);
            ((GroceryItemAdapter) listView.getAdapter()).notifyDataSetChanged();
        }
    };

    public static ProductListFragment newInstance(String param1, String param2) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    GroceryItemAdapter G_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        fab = (FloatingActionButton) view.findViewById(R.id.btnFloatingActionAddProduct);
        fab.setOnClickListener(this);

        listView = (ListView) view.findViewById(R.id.ProductListView);

        G_adapter = new GroceryItemAdapter(getContext(), groceries);
        listView.setAdapter(G_adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GroceryItemClickListener) {
            mListener = (GroceryItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DeleteUserDialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mListener != null){
            mListener.onGroceryItemClick(parent,view,position,id);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (mListener != null){
            mListener.onGroceryItemLongClick(parent,view,position,id);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null){
            mListener.onFABClick(v);
        }
    }


    public interface GroceryItemClickListener {
        void onGroceryItemClick(AdapterView<?> parent, View view, int position, long id);
        void onGroceryItemLongClick(AdapterView<?> parent, View view, int position, long id);
        void onFABClick(View view);
    }
}
