package com.studio08.ronen.Zivug.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.studio08.ronen.Zivug.Model.ContactLab;
import com.studio08.ronen.Zivug.R;

import java.util.UUID;

/**
 * Created by Ronen on 6/9/16.
 */

public abstract class AddFilterDialogFragment<E extends ContactLab.Filter> extends DialogFragment {

    private onAddSelectedListener mCallback;
    private EditText editTextName;
    private E mFilter;
    private boolean editing;

    int updateButton = R.string.update_tag;
    int addButton = R.string.add_tag;

    abstract E getFilter(UUID mId); // ContactLab.get(getContext()).getTag(mId);
    abstract void setButtonText();


    interface onAddSelectedListener {
        void onAddSelected(ContactLab.Filter filter);

        void onEditSelected(ContactLab.Filter filter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (onAddSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement onAddSelectedListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String id = getArguments().getString("id");

        setButtonText();

        if (!id.isEmpty())
            editing = true;

        if (editing) {
            UUID mId = UUID.fromString(id);

            mFilter = getFilter(mId);
        }
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View content = inflater.inflate(R.layout.fragment_dialog_tag, null);
        builder.setView(content);

        editTextName = (EditText) content.findViewById(R.id.editTextName);

        if (mFilter != null) editTextName.setText(mFilter.getName());

        if (editing) {
            builder.setPositiveButton(updateButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mFilter.setName(editTextName.getText().toString());
                    mCallback.onEditSelected(mFilter);
                }
            });
        } else {
            builder.setPositiveButton(addButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mCallback.onAddSelected(new ContactLab.Tag(editTextName.getText().toString()));
                }
            });
        }
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
