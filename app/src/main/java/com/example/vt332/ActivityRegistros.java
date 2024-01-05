package com.example.vt332;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivityRegistros extends AppCompatActivity {
    private Spinner spAgnio,spMies;
    private ArrayList<String> litAgno,listMies,listGastos,listGiros;
    private ArrayAdapter<String> adAgno,adMies;
    private ArrayList<Integer> listGastosInt,listGirosInt;
    private DatabaseReference nodio= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registros);

        spAgnio=findViewById(R.id.spinner4);
        spMies=findViewById(R.id.spinner5);

        listGastos=new ArrayList<>();
        listGiros=new ArrayList<>();
        listGastosInt=new ArrayList<>();
        listGirosInt=new ArrayList<>();

        litAgno=new ArrayList<>();
        listMies=new ArrayList<>();
        adAgno=new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,litAgno);
        adMies=new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,listMies);
        spAgnio.setAdapter(adAgno);
        spMies.setAdapter(adMies);
        seteaSpinnerAnio();
        spAgnio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                seteaSpinnerMes();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spMies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                seteaReporte();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void seteaReporte() {
        Toast.makeText(this,"Realizando informe",Toast.LENGTH_SHORT).show();
    }

    private void seteaSpinnerAnio() {
        nodio.child("balance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item:snapshot.getChildren()){
                    litAgno.add(item.getKey().toString());
                }
                adAgno.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void seteaSpinnerMes() {
        nodio.child("balance").child(spAgnio.getSelectedItem().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item:snapshot.getChildren()){
                    listMies.add(item.getKey().toString());
                }
                adMies.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}