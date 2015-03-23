package com.weathercheck;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * This class can be used to generate custom alerts or dialogs given a specified event.
 * Pretty much copy and paste this code for whenever you need the user to know something that
 * has happened.
 */
public class AlertDialogFragment extends DialogFragment
{
    public Dialog onCreateDialog(Bundle b)
    {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.error_message))
                    .setMessage(context.getString(R.string.error_message_dialog))
                    .setPositiveButton(context.getString(R.string.ok), null);

        AlertDialog dialog = builder.create();
        return dialog;

    }

}
