package com.webnotics.swipee.call;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.webnotics.swipee.R;


public class Dialog {

    public static AlertDialog createConnectDialog(EditText participantEditText,
                                                  DialogInterface.OnClickListener callParticipantsClickListener,
                                                  DialogInterface.OnClickListener cancelClickListener,
                                                  Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setIcon(R.drawable.ic_video_call_white_24dp);
        alertDialogBuilder.setTitle(R.string.connromm);
        alertDialogBuilder.setPositiveButton(R.string.connect, callParticipantsClickListener);
        alertDialogBuilder.setNegativeButton(R.string.cancelbtn, cancelClickListener);
        alertDialogBuilder.setCancelable(false);

        setRoomNameFieldInDialog(participantEditText, alertDialogBuilder);

        return alertDialogBuilder.create();
    }

    private static void setRoomNameFieldInDialog(EditText roomNameEditText,
                                                 AlertDialog.Builder alertDialogBuilder) {
        roomNameEditText.setHint(R.string.roomname);
        alertDialogBuilder.setView(roomNameEditText);
    }

}
