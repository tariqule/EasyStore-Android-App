/*

Filename:       PhoneDialogFragment.java
Description:    For Custom Dialog Fragment creation, specifically used for phone confirmation.

 */

package com.example.nazif.comp304_miniproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class PhoneDialogFragment extends DialogFragment {

    // PhoneDialogListener interface
    // Requires activity that uses the fragment to implement this in order to add other
    // functionality after button press
    public interface PhoneDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
    }

    PhoneDialogListener listener;

    // onAttach method
    // Specifically for adding the listener to activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Confirm context using the fragment implements the Listener interface
        try {
            // Instantiate the listener onto the context
            listener = (PhoneDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Must implement PhoneDialogDialogListener");
        }
    }

    // onCreateDialog method
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get layout inflater
        final LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View viewInflater = inflater.inflate(R.layout.dialog_confirm_phone, null);

        final EditText inputCodeText = viewInflater.findViewById(R.id.digitConfirm);

        // Set dialog properties
        builder.setView(viewInflater)
                // Add action buttons
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        // Confirm the input code with the sent code.
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("confirm_phone",MODE_PRIVATE);
                        String code = sharedPreferences.getString("code", "");
                        String inputCode = inputCodeText.getText().toString().trim();

                        if (code.equals(inputCode))
                            listener.onDialogPositiveClick(PhoneDialogFragment.this);
                        else{
                            Toast.makeText(getContext(),"Input code did not match sent code !", Toast.LENGTH_LONG).show();
                        }

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PhoneDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
}
