package com.example.vt332;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivityRegistros extends AppCompatActivity {
    private TextView txtGastos,txtIngresos;
    private Button bbtnVolver;
    private Spinner spAgnio,spMies;
    private ArrayList<String> litAgno,listMies,listGastos,listGiros;
    private ArrayAdapter<String> adAgno,adMies;
    private ArrayList<Integer> listGastosInt,listGirosInt;
    private DatabaseReference nodio= FirebaseDatabase.getInstance().getReference();
    private ArrayList<Integer> arlistIngresos;
    private ArrayList<String> arlistNombres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registros);

        txtGastos=findViewById(R.id.textView8);
        txtIngresos=findViewById(R.id.textView7);

        bbtnVolver=findViewById(R.id.button5);

        arlistIngresos=new ArrayList<>();
        arlistNombres=new ArrayList<>();

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
        bbtnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        txtGastos.setText("");
        txtIngresos.setText("");
    }

    private void seteaReporte() {
        nodio.child("unidades").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itec:snapshot.child("depas").getChildren()){
                    arlistNombres.add(itec.getKey().toString());
                    if (itec.child("pagos").child(spAgnio.getSelectedItem().toString()).child(spMies.getSelectedItem().toString()).child("Alquiler").exists())
                        arlistIngresos.add(Integer.parseInt(itec.child("pagos").child(spAgnio.getSelectedItem().toString()).
                                child(spMies.getSelectedItem().toString()).child("Alquiler").getValue().toString()));
                    else arlistIngresos.add(0);
                    if (itec.child("pagos").child(spAgnio.getSelectedItem().toString()).child(spMies.getSelectedItem().toString()).child("Deposito").exists())
                        arlistIngresos.add(Integer.parseInt(itec.child("pagos").child(spAgnio.getSelectedItem().toString()).
                                child(spMies.getSelectedItem().toString()).child("Deposito").getValue().toString()));
                    else arlistIngresos.add(0);
                    if (itec.child("pagos").child(spAgnio.getSelectedItem().toString()).child(spMies.getSelectedItem().toString()).child("Servicios").exists())
                        arlistIngresos.add(Integer.parseInt(itec.child("pagos").child(spAgnio.getSelectedItem().toString()).
                                child(spMies.getSelectedItem().toString()).child("Servicios").getValue().toString()));
                    else arlistIngresos.add(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        nodio.child("balance").child(spAgnio.getSelectedItem().toString()).child(spMies.getSelectedItem().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item:snapshot.child("gastos").getChildren()){
                    listGastos.add(item.getKey().toString());
                    listGastosInt.add(Integer.parseInt(item.getValue().toString()));
                }
                for (DataSnapshot iten:snapshot.child("giros").getChildren()){
                    listGiros.add(iten.getKey().toString());
                    listGirosInt.add(Integer.parseInt(iten.getValue().toString()));
                }
                seteaReporte2();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void seteaReporte2() {
        nodio.child("unidades").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item:snapshot.child("cuartos").getChildren()){
                    arlistNombres.add(item.getKey().toString());
                    if(item.child("pagos").child(spAgnio.getSelectedItem().toString()).child(spMies.getSelectedItem().toString()).child("Alquiler").exists())
                        arlistIngresos.add(Integer.parseInt(item.child("pagos").child(spAgnio.getSelectedItem().toString()).
                                child(spMies.getSelectedItem().toString()).child("Alquiler").getValue().toString()));
                    else arlistIngresos.add(0);
                    if(item.child("pagos").child(spAgnio.getSelectedItem().toString()).child(spMies.getSelectedItem().toString()).child("Deposito").exists())
                        arlistIngresos.add(Integer.parseInt(item.child("pagos").child(spAgnio.getSelectedItem().toString()).
                                child(spMies.getSelectedItem().toString()).child("Deposito").getValue().toString()));
                    else arlistIngresos.add(0);
                }
                seteaReporte3();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void seteaReporte3() {
        int ingresosINT=0;
        for(int i=0;i<arlistIngresos.size();i++){
            ingresosINT=ingresosINT+arlistIngresos.get(i);
        }

        String ingresos="";
        int posicion=0;
        String ingrestoST="";

        for (int i=0;i<8;i++){

            int pos=0;
            ingresos=ingresos+arlistNombres.get(i).toString()+"\n";

            for (int e=posicion;e<(posicion+3);e++){

                String quefue="";
                if (pos==0) quefue="Alquiler=";
                if (pos==1) quefue="Deposito=";
                if (pos==2) quefue="Servicios=";

                if(arlistIngresos.get(e)>0){
                    ingresos=ingresos+quefue+arlistIngresos.get(e).toString()+"\n";
                }
                pos++;
            }
            posicion=posicion+3;
        }

        for (int i=8;i<arlistNombres.size();i++){

            ingresos=ingresos+arlistNombres.get(i).toString()+"\n";

            String quefue="";
            int pos=0;

            for (int e=posicion;e<(posicion+2);e++){
                if (pos==0) quefue="Alquiler=";
                if (pos==1) quefue="Deposito=";
                if (arlistIngresos.get(e)>0) ingresos=ingresos+quefue+arlistIngresos.get(e).toString()+"\n";

                pos++;
            }
            posicion=posicion+2;
        }
        txtIngresos.setText(ingresos+"\n"+"total ingresos= "+ingresosINT);
// seteo de gastos mensuales y giros
        int totGastos=0;
        for(Integer i:listGastosInt) {
            totGastos=totGastos+i;
        }
        int totGiros=0;
        for (Integer e:listGirosInt){
            totGiros=totGiros+e;
        }
        int totEgresos=totGiros+totGastos;
        int balance=ingresosINT-totEgresos;

        String egresos="";
        String giros="";

        for (int i=0;i<listGastos.size();i++){
            egresos=egresos+listGastos.get(i)+"="+listGastosInt.get(i).toString()+"\n";
        }
        for (int e=0;e<listGiros.size();e++){
            giros=giros+listGiros.get(e)+"="+listGirosInt.get(e).toString()+"\n";
        }
        txtGastos.setText(egresos+"\n"+giros+"\n"+"total gasto="+totGastos+"\n"+"total giros="+totGiros+"\n"+"\n"+"Balance= "+balance);
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