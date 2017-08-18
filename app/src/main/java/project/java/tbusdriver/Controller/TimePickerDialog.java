package project.java.tbusdriver.Controller;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import project.java.tbusdriver.R;

/**
 * Created by אור איטח on 14/08/2017.
 */

public class TimePickerDialog extends DialogFragment implements DialogInterface.OnClickListener
{
    View myView;
    TimePicker clock;
    public TimePickerDialog()
    {
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        myView = getActivity().getLayoutInflater().inflate(R.layout.fragment_time_picker_dialog, new LinearLayout(getActivity()), false);

        // Retrieve layout elements
        clock = (TimePicker) myView.findViewById(R.id.clock);
        clock.setIs24HourView(true);

        // Build dialog
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("pick a time");
        builder.setView(myView);
        builder.setPositiveButton("ok",this);
        builder.setNegativeButton("cancel",this);
        return builder.create();
    }

    @Override
    @TargetApi(23)
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i)
        {
            //ok
            case -1:
                clock = (TimePicker) myView.findViewById(R.id.clock);
                clock.getHour();
                clock.getMinute();
                sendResult(0, clock.getHour()+":"+clock.getMinute());
                break;
            //cancel
            case -2:
                sendResult(0, "");
                break;
        }
    }



    private void sendResult(int REQUEST_CODE, String time) {
        Intent intent = new Intent();
        intent.putExtra("result",time);
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), REQUEST_CODE, intent);
    }
}
