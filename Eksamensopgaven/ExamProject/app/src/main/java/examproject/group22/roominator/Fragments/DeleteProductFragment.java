package examproject.group22.roominator.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import examproject.group22.roominator.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DeleteProductDialogListener} interface
 * to handle interaction events.
 * Use the {@link DeleteProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeleteProductFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DeleteProductDialogListener mListener;

    public DeleteProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeleteProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeleteProductFragment newInstance(String param1, String param2) {
        DeleteProductFragment fragment = new DeleteProductFragment();
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
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_product_message)
                .setPositiveButton(R.string.dialog_product_btnOK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onProductDialogPositiveClick(DeleteProductFragment.this);
                    }
                })
                .setNegativeButton(R.string.dialog_product_btnCancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onProductDialogNegativeClick(DeleteProductFragment.this);
                    }
                });

        return builder.create();
    }

/*    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onProductDialogNegativeClick();
            mListener.onProductDialogPositiveClick();
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DeleteProductDialogListener) {
            mListener = (DeleteProductDialogListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DeleteProductDialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface DeleteProductDialogListener {
        public void onProductDialogPositiveClick(DialogFragment dialog);
        public void onProductDialogNegativeClick(DialogFragment dialog);
    }
}
