package examproject.group22.roominator.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import examproject.group22.roominator.Models.Apartment;
import examproject.group22.roominator.Models.User;
import examproject.group22.roominator.R;
import examproject.group22.roominator.Adapters.UserInfoAdapter;

// KILDER:
// https://developer.android.com/reference/android/widget/AdapterView.OnItemLongClickListener.html


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserItemClickListener} interface
 * to handle interaction events.
 * Use the {@link UsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsersFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    ArrayList<User> userInfo;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ListAdapter userAdapter;
    ListView listView;
    private UserItemClickListener mListener;

    public UsersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UsersFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users, container, false);

        Bundle b = getArguments();
        ArrayList<User> users = (ArrayList<User> )b.getSerializable("users");


        listView = (ListView) view.findViewById(R.id.overviewList);
        userAdapter = new UserInfoAdapter(this.getContext(),users);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface UserItemClickListener {
        // TODO: Update argument type and name
        void onUserItemLongClick(AdapterView<?> parent, View view, int position, long id);
        void onUserItemClick(AdapterView<?> parent, View view, int position, long id);
    }
}
