package examproject.group22.roominator.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import examproject.group22.roominator.R;

// KILDE:
// https://developer.android.com/guide/topics/ui/dialogs.html


public class DeleteUserFragment extends DialogFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private DeleteUserDialogListener mListener;

    public DeleteUserFragment() {
    }

    public static DeleteUserFragment newInstance(String param1, String param2) {
        DeleteUserFragment fragment = new DeleteUserFragment();
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
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_user_title);
        builder.setMessage(R.string.dialog_user_message)
                .setPositiveButton(R.string.dialog_user_btnOK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onUserDialogPositiveClick(DeleteUserFragment.this);
                    }
                })
                .setNegativeButton(R.string.dialog_user_btnCancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onUserDialogNegativeClick(DeleteUserFragment.this);
                    }
                });

        return builder.create();
    }

    public void onButtonPressed(DialogFragment dialog) {
        if (mListener != null) {
            mListener.onUserDialogPositiveClick(dialog);
            mListener.onUserDialogNegativeClick(dialog);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DeleteUserDialogListener) {
            mListener = (DeleteUserDialogListener) context;
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

    public interface DeleteUserDialogListener {
        //void onUserItemLongClick(Uri uri);
        public void onUserDialogPositiveClick(DialogFragment dialog);
        public void onUserDialogNegativeClick(DialogFragment dialog);
    }
}
