package edu.stanford.cs108.bunnyworld;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.*;

/*
    The edu.stanford.cs108.bunnyworld.Shape class represents an object that has been added
    to an editor page view.
 */
public class AddShapeDialog extends DialogFragment {

    private PopupView menu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        GridView gridView = new GridView(getActivity());
//
//        ArrayList<ShapeResource>  mList = new ArrayList<ShapeResource>();
//
//        mList.add(new ShapeResource("carrot", R.drawable.carrot));
//        mList.add(new ShapeResource("carrot2", R.drawable.carrot2));
//
//
//        GridViewAdapter iconItems = new GridViewAdapter(getActivity(), mList);
//
//        gridView.setAdapter(iconItems);
//        gridView.setNumColumns(3);               // Number of columns
//        gridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);       // Choice mode
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // do something here
//                Toast.makeText(getActivity(), "Position: " + position, Toast.LENGTH_SHORT).show();
//                builder.dismiss(); // Dismiss dialog after click on item
//            }
//        });
//        builder.setView(gridView);
//        builder.setTitle("Select an icon");
//        builder = builder.create();
//        iconDialog.show();
//
//        if(menu == null) System.out.println("NULL FOUND");
//
          return null;
//
//    }
    }
}