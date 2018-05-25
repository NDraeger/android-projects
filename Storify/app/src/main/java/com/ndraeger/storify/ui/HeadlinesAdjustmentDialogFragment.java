package com.ndraeger.storify.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.ndraeger.storify.R;

public class HeadlinesAdjustmentDialogFragment extends DialogFragment {

    public static HeadlinesAdjustmentDialogFragment newInstance() {
        Bundle args = new Bundle();

        HeadlinesAdjustmentDialogFragment fragment = new HeadlinesAdjustmentDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_headlines_adjustment, null);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.dialog_adjustment_title)
                .create();
    }
}
