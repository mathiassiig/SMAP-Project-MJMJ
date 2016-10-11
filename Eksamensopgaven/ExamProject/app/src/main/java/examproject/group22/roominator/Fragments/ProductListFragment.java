package examproject.group22.roominator.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import examproject.group22.roominator.Activities.BuyProductActivity;
import examproject.group22.roominator.Activities.OverviewActivity;
import examproject.group22.roominator.Adapters.GroceryItemAdapter;
import examproject.group22.roominator.Models.Apartment;
import examproject.group22.roominator.Models.GroceryItem;
import examproject.group22.roominator.R;
import examproject.group22.roominator.Adapters.ShoppingListAdapter;
import examproject.group22.roominator.ShoppingListProvider;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductListFragment.GroceryItemClickListener} interface
 * to handle interaction events.
 * Use the {@link ProductListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductListFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Apartment currentApartment;


    private GroceryItemClickListener  mListener;

    public ProductListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductListFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    String[] Products;
    String[] Number;
    //ShoppingListAdapter S_adapter;
    GroceryItemAdapter G_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.btnFloatingActionAddProduct);
        fab.setOnClickListener(this);

        ListView listView = (ListView) view.findViewById(R.id.ProductListView);

        Products = getResources().getStringArray(R.array.product_names);
        Number = getResources().getStringArray(R.array.product_number);
        //S_adapter = new ShoppingListAdapter(getContext(), R.layout.row_shoppinglist_layout);


        Bundle b = getArguments();
        Apartment apartment = (Apartment)b.getSerializable("apartment");


        G_adapter = new GroceryItemAdapter(getContext(), apartment.groceries);
        listView.setAdapter(G_adapter);
        listView.setOnItemClickListener(this);
/*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(getActivity(), BuyProductActivity.class);
                String messageproduct = Products[position];
                int messagenumber = Integer.parseInt(Number[position]);
                intent.putExtra("productname", messageproduct);
                intent.putExtra("productnumber", messagenumber);
                startActivity(intent);
            }
        }); */

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
    public void onClick(View v) {
        if (mListener != null){
            mListener.onFABClick(v);
        }
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
    public interface GroceryItemClickListener {
        void onGroceryItemClick(AdapterView<?> parent, View view, int position, long id);
        void onFABClick(View view);

    }
}
