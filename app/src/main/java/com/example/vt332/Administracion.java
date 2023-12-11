package com.example.vt332;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Administracion extends AppCompatActivity {
    private EditText ETmotivo,ETmonto;
    private Button btnregistrar;
    private TextView txtingresos,txtegresos;
    private String stIngreso,stEgresos,stBalance;
//seteo variables fechas
    private Date fecha=new Date();
    private SimpleDateFormat anio=new SimpleDateFormat("YYYY");
    private SimpleDateFormat mies=new SimpleDateFormat("MM");
    private String Anio=anio.format(fecha);
    private String Mies=mies.format(fecha);
//seteo nodo firebase
    private DatabaseReference nodox=FirebaseDatabase.getInstance().getReference();
//seteo arrays para ingresos y egresos
    private ArrayList<String> arrayInmueble,arrayGasto;
    private ArrayList<Double> arrayIngresos,arrayEgresos;
    private double dbGastos,dbIngresos;
    private String STingresos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administracion);
        seteaIngresos();
        ETmonto=findViewById(R.id.editTextText15);
        ETmotivo=findViewById(R.id.editTextText4);
        btnregistrar=findViewById(R.id.button3);
        txtingresos=findViewById(R.id.textView71);
        txtegresos=findViewById(R.id.textView81);
        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botonaso();
            }
        });
        arrayInmueble=new ArrayList<>();
        arrayIngresos=new ArrayList<>();
        arrayEgresos=new ArrayList<>();
        arrayGasto=new ArrayList<>();
    }

    private void seteaIngresos() {
        nodox.child("unidades").child("cuartos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item:snapshot.getChildren()){
                    arrayInmueble.add(item.getKey().toString());
                    if(item.child("pagos").child(Anio).child(Mies).child("Alquiler").exists())
                        arrayIngresos.add(Double.parseDouble(item.child("pagos").child(Anio).child(Mies).child("Alquiler").getValue().toString()));
                    else arrayIngresos.add(0.0);
                    if(item.child("pagos").child(Anio).child(Mies).child("Deposito").exists())
                        arrayIngresos.add(Double.parseDouble(item.child("pagos").child(Anio.toString()).child(Mies.toString()).child("Deposito").getValue().toString()));
                    else arrayIngresos.add(0.0);
                }seteaIngresos2();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void seteaIngresos2() {
        nodox.child("unidades").child("depas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot item:snapshot.getChildren()){
                    arrayInmueble.add(item.getKey().toString());
                    if(item.child("pagos").child(Anio).child(Mies).child("Alquiler").exists())
                        arrayIngresos.add(Double.parseDouble(item.child("pagos").child(Anio).child(Mies).child("Alquiler").getValue().toString()));
                    else arrayIngresos.add(0.0);
                    if(item.child("pagos").child(Anio).child(Mies).child("Deposito").exists())
                        arrayIngresos.add(Double.parseDouble(item.child("pagos").child(Anio).child(Mies).child("Deposito").getValue().toString()));
                    else arrayIngresos.add(0.0);
                    if(item.child("pagos").child(Anio).child(Mies).child("Servicios").exists())
                        arrayIngresos.add(Double.parseDouble(item.child("pagos").child(Anio).child(Mies).child("Servicios").getValue().toString()));
                    else arrayIngresos.add(0.0);
                }seteaIngresos3();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void seteaIngresos3() {
        STingresos="";
        dbIngresos=0;
        int ii=0;
        for(double ing:arrayIngresos){
            dbIngresos=dbIngresos+ing;
        }
        for(int i=0;i<8;i++){
            STingresos=STingresos+arrayInmueble.get(i).toString()+"\n";
            for (int e=ii;e<ii+2;e++){
                STingresos=STingresos+"     "+arrayIngresos.get(e).toString()+"\n";
            }ii=ii+2;
        }

        int ee=22;
        for (int i=10;i<16;i++){
            STingresos=STingresos+arrayInmueble.get(i).toString()+"\n";
            for (int e=ee;e<ee+3;e++){
                STingresos=STingresos+"     "+arrayIngresos.get(e).toString()+"\n";
            }ee=ee+3;
        }
        /*String prueba="";
        for(Double e:arrayIngresos){
            prueba=prueba+Double.toString(e)+"\n";
        }*/
        txtingresos.setText(STingresos+"\n"+"total= "+dbIngresos);
    }

    private void botonaso() {
        if(ETmonto.getText().toString().length()==0&&ETmotivo.getText().toString().length()==0){
            finish();
        }else {
            nodox.child("balance").child(Anio).child(Mies).child(ETmotivo.getText().toString()).setValue(ETmonto.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                   ETmonto.setText(null);
                   ETmotivo.setText(null);
                   seteaIngresos();
                }
            });
        }
    }

}