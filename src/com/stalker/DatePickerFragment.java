package com.stalker;


import java.util.Calendar;

import android.app.*;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment 
implements DatePickerDialog.OnDateSetListener{

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
     
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
	
	@Override
	public void onDateSet(DatePicker arg0, int year, int month, int date) {
		AddToDoActivity.startYear = year;
		AddToDoActivity.startMonth = month;
		AddToDoActivity.startDay = date;
		
	}

}
