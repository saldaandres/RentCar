package com.example.rentcar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    Bundle resultado = new Bundle();
    String tag;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        tag = getTag();
        final Calendar calendario = Calendar.getInstance();
        int year = calendario.get(Calendar.YEAR);
        int month = calendario.get(Calendar.MONTH);
        int day = calendario.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(requireContext(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        LocalDate fecha = LocalDate.of(year, month + 1, dayOfMonth);
        Toast.makeText(getContext(), fecha.toString(), Toast.LENGTH_SHORT).show();
        switch (tag) {
            case "fechaInicial" :
                resultado.putString("inicial", fecha.toString());
                getParentFragmentManager().setFragmentResult("1", resultado);
                break;
            case "fechaFinal" :
                resultado.putString("final", fecha.toString());
                getParentFragmentManager().setFragmentResult("2", resultado);
                break;
            case "fechaRetorno" :
                resultado.putString("retorno", fecha.toString());
                getParentFragmentManager().setFragmentResult("3", resultado);
        }
    }
}
