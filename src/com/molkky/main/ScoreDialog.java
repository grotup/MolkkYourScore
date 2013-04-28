package com.molkky.main;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ScoreDialog extends DialogFragment {
	String[] liste = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		    builder.setTitle("Score : ");
		    builder.setItems(liste, new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int which) {
		            	   
		           }
		    });
		    return builder.create();

    }
}
