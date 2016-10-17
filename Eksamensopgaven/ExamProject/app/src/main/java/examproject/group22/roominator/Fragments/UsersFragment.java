package examproject.group22.roominator.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import examproject.group22.roominator.Activities.OverviewActivity;
import examproject.group22.roominator.DatabaseService;
import examproject.group22.roominator.Models.Apartment;
import examproject.group22.roominator.Models.GroceryItem;
import examproject.group22.roominator.Models.User;
import examproject.group22.roominator.R;
import examproject.group22.roominator.Adapters.UserInfoAdapter;

// KILDER:
// https://developer.android.com/reference/android/widget/AdapterView.OnItemLongClickListener.html


public class UsersFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    ArrayList<User> userInfo;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public Apartment currentApartment;
    private ListAdapter userAdapter;
    private ListView listView;
    private final ArrayList<User> users = new ArrayList<>();
    private UserItemClickListener mListener;
    private TextView txtview_Total;


    public UsersFragment() {
        // Required empty public constructor
    }

    public static UsersFragment newInstance(String param1, String param2) {
        UsersFragment fragment = new UsersFragment();
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

    @Override
    public void onResume()
    {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReciever,new IntentFilter(OverviewActivity.INTENT_UPDATE_USERS_FRAGMENT));
    }

    @Override
    public void onPause()
    {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReciever);
        super.onPause();
    }

    private final BroadcastReceiver mReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            users.clear();
            ArrayList<User> allusers = (ArrayList<User>) intent.getSerializableExtra("users");
            users.addAll(allusers);
            ((UserInfoAdapter) listView.getAdapter()).notifyDataSetChanged();
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users, container, false);

        txtview_Total = (TextView)view.findViewById(R.id.customUser_txtTotal);
        listView = (ListView) view.findViewById(R.id.overviewList);

        userAdapter = new UserInfoAdapter(getContext(),users);
        listView.setAdapter(userAdapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UserItemClickListener) {
            mListener = (UserItemClickListener) context;
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
            mListener.onUserItemClick(parent,view,position,id);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (mListener != null) {
            mListener.onUserItemLongClick(parent, view, position, id);
        }
        return true;
    }

    public interface UserItemClickListener {
        void onUserItemLongClick(AdapterView<?> parent, View view, int position, long id);
        void onUserItemClick(AdapterView<?> parent, View view, int position, long id);
    }
}
