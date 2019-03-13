package edu.stanford.cs108.bunnyworld;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

/*
    The edu.stanford.cs108.bunnyworld.Shape class represents an object that has been added
    to an editor page view.
 */
public class AddShapeDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.shapeadder_popup, null));


        return builder.create();
              /*
        // make the dialog
        AlertDialog.Builder makeShape = new AlertDialog.Builder(getActivity());
        makeShape.setTitle("Add a Shape!");

        // make the inflator
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // inflate and pass layout for the dialog
        makeShape.setView(inflater.inflate(R.layout.shapeadder_popup, null));

        return makeShape.create(); */

    }
}