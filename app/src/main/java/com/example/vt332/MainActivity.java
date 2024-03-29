package com.example.vt332;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
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
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private Button btnPago,btnLectura,btnServicios,btnRegistros;
    private Spinner spTipoInm,spUnidad,spGlosa;
    private EditText ETnombre,ETnroPersonas,ETfingreso,ETpago;
    private EditText ETlectura;
    private TextView txtservicios,txtEdesa;
    private Switch zwitch;
    private String cabre;
//declarando views para servicios
    private EditText ETlecElec, ETmontoElec;
    private EditText ETimp;
    private EditText ETlecAgu,ETmontAgu;
    private EditText ETlecGas,ETmontoGas;
    private EditText ETcable,ETintern;
    private TextView txtMorosos;
    private ScrollView scMorosos;
//punto de enlace con firebase database
    private DatabaseReference nodo= FirebaseDatabase.getInstance().getReference();
//declarando arrays y adaptadores para los spinners
    private ArrayList<String> arrayTipo,arrayUni,arrayGlosa;
    private ArrayAdapter<String> adTipo,adUni,adGlosa;
//seteo de fechas
    private Date fecha=new Date();
    private SimpleDateFormat agno=new SimpleDateFormat("YYYY");
    private SimpleDateFormat mes=new SimpleDateFormat("MM");
    private SimpleDateFormat dia=new SimpleDateFormat("dd");
    private String Agno=agno.format(fecha);
    private String Mes=mes.format(fecha);
    private String Dia=dia.format(fecha);
//declaracion e instanciamiento de variables para calcular servicios
    private double nroCables;
    private double nroInqs,nro;
    private double iluz;
    private double lectCuart2;
    private HashMap<String,Double> lecsMap=new HashMap<>();
    private HashMap<String,Double> lecsMap2=new HashMap<>();
    private double MontoEdesa, LecturaEdesa,LecturaEdesa2,LecCuartos,LecCuartos2,LecTOT,LecTOT2,Magua,Mgas;
    private double Mcable,Minternet,Mimpuestos;
    private double nroInqCuartos;
//variables para deudores
    private String deudores="";
    private String deudores2="";
//declarando variables para mostrar deudores
    private ArrayList<String> arrayCuartos,arrayDepas;
//metodo on create
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getNroInq();
        getNroCable();
        arrayCuartos=new ArrayList<>();
        arrayDepas=new ArrayList<>();
        txtEdesa=findViewById(R.id.textView);
        txtEdesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                esconderKeyboard();
            }
        });
        seteaArrayDeudores();
//instanciando views para datos especificos de cada inquilino
        spTipoInm=findViewById(R.id.spinner);
        spUnidad=findViewById(R.id.spinner2);
        spGlosa=findViewById(R.id.spinner3);

        zwitch=findViewById(R.id.switch1);

        ETnombre=findViewById(R.id.editTextText);
        ETnroPersonas=findViewById(R.id.editTextText2);
        ETfingreso=findViewById(R.id.editTextText3);
        ETpago=findViewById(R.id.editText4);
        ETlectura=findViewById(R.id.editTextText5);

        txtservicios=findViewById(R.id.textview1);
//instanciando los views para servicios
        ETlecElec=findViewById(R.id.editTextText6);
        ETmontoElec=findViewById(R.id.editTextText7);
        ETimp=findViewById(R.id.editTextText8);
        ETlecAgu=findViewById(R.id.editTextText9);
        ETmontAgu=findViewById(R.id.editTextText10);
        ETlecGas=findViewById(R.id.editTextText11);
        ETmontoGas=findViewById(R.id.editTextText12);
        ETcable=findViewById(R.id.editTextText13);
        ETintern=findViewById(R.id.editTextText14);
        
        txtMorosos=findViewById(R.id.txtDeudores);
        scMorosos=findViewById(R.id.SCnoPagaron);
//seteando e instanciando tools para los spinners
        arrayTipo=new ArrayList<>();
        arrayUni=new ArrayList<>();
        arrayGlosa=new ArrayList<>();
        adTipo=new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,arrayTipo);
        adUni=new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,arrayUni);
        adGlosa=new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,arrayGlosa);
        spTipoInm.setAdapter(adTipo);
        spUnidad.setAdapter(adUni);
        spGlosa.setAdapter(adGlosa);
        seteaSptipo();
        seteaSpglosa();
        spTipoInm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                arrayUni.clear();
                seteaSpUni(spTipoInm.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spUnidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                seteaDatosInq(spUnidad.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
//seteo de botones
        btnPago=findViewById(R.id.button);
        btnLectura=findViewById(R.id.button2);
        btnServicios=findViewById(R.id.botonServicios);
        btnRegistros=findViewById(R.id.button4);

        btnPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPago();
            }
        });
        btnLectura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLectura();
            }
        });
        btnServicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setServiciosGrales();
            }
        });
        btnRegistros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irRegistro();
            }
        });
//agnadiendo accion al texto
        txtservicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setServicios();
            }
        });

    }

    private void irRegistro() {
        Intent intent=new Intent(this, ActivityRegistros.class);
        startActivity(intent);
    }

    private void seteaArrayDeudores() {
        arrayDepas.clear();
        arrayCuartos.clear();
        for(int i=1;i<9;i++){
            arrayCuartos.add("CUARTO "+i);
        }
        arrayDepas.add("DEPA 1");
        arrayDepas.add("DEPA 2");
        for (int i=1;i<7;i++){
            arrayDepas.add("MONO "+i);
        }
        seteaDeudores();
    }

    private void setServicios() {
        nodo.child("servicios").child(Agno).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(Mes).exists())setServicios2();
                else txtservicios.setText("no ingresaste servicios");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setServicios2() {
    //OBTENIENDO DATOS
        nodo.child("servicios").child(Agno).child(Mes).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Magua=Double.parseDouble(snapshot.child("LAG").child("agua").child("monto").getValue().toString());
                Mgas=Double.parseDouble(snapshot.child("LAG").child("gas").child("monto").getValue().toString());
                MontoEdesa=Double.parseDouble(snapshot.child("LAG").child("electricidad").child("monto").getValue().toString());
                LecturaEdesa=Double.parseDouble(snapshot.child("LAG").child("electricidad").child("lectura").getValue().toString());
                Mcable=Double.parseDouble(snapshot.child("CII").child("cable").getValue().toString());
                LecCuartos=Double.parseDouble(snapshot.child("CII").child("cuartos").getValue().toString());
                Minternet=Double.parseDouble(snapshot.child("CII").child("wifi").getValue().toString());
                Mimpuestos=Double.parseDouble(snapshot.child("CII").child("impuestos").getValue().toString());
                muestraServicios();
                setServicios2a();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void muestraServicios() {
        double tot=(Magua+Mgas+MontoEdesa+Mcable+Minternet)*1.0;
        Toast.makeText(this,"servicios sin claro cuartos= "+tot,Toast.LENGTH_SHORT).show();
    }

    private void setServicios2a() {

        if(Integer.parseInt(Mes)==1){
            int agnoz=Integer.parseInt(Agno)-1;
            nodo.child("servicios").child(Integer.toString(agnoz)).child("12").child("CII").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    LecCuartos2=Double.parseDouble(snapshot.child("cuartos").getValue().toString());
                    setServicios2b();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }else {
            int mez=Integer.parseInt(Mes)-1;
            nodo.child("servicios").child(Agno).child(Integer.toString(mez)).child("CII").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    LecCuartos2=Double.parseDouble(snapshot.child("cuartos").getValue().toString());
                    setServicios2b();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    private void setServicios2b() {

        LecTOT=0;
            nodo.child("unidades").child("depas").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child(spUnidad.getSelectedItem().toString()).child("lecturas").child(Agno).child(Mes).exists()){
                        int m=0;
                        for (DataSnapshot iten:snapshot.getChildren()){
                            if(iten.child("lecturas").child(Agno).child(Mes).exists()){
                                m++;
                                lecsMap.put(iten.getKey().toString(),Double.parseDouble(iten.child("lecturas").child(Agno).child(Mes).getValue().toString()));
                                LecTOT=LecTOT+Double.parseDouble(iten.child("lecturas").child(Agno).child(Mes).getValue().toString());
                                if(m==8) setServicios3a();
                            }
                        }if (m<7)Toast.makeText(getApplicationContext(),"te falta algun depa",Toast.LENGTH_SHORT).show();
                    }else if (spTipoInm.getSelectedItem().toString().equalsIgnoreCase("cuartos")){
                        int m=0;
                        for (DataSnapshot iten:snapshot.getChildren()){
                            if(iten.child("lecturas").child(Agno).child(Mes).exists()){
                                m++;
                                lecsMap.put(iten.getKey().toString(),Double.parseDouble(iten.child("lecturas").child(Agno).child(Mes).getValue().toString()));
                                LecTOT=LecTOT+Double.parseDouble(iten.child("lecturas").child(Agno).child(Mes).getValue().toString());
                                if(m==8) setServicios3a();
                            }
                        }if (m<7)Toast.makeText(getApplicationContext(),"te falta algun depa",Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

    }

    private void setServicios3a() {
        //Toast.makeText(this,"servicios 3a",Toast.LENGTH_SHORT).show();

        if(Mes.equalsIgnoreCase("01")){
            nodo.child("unidades").child("depas").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String anuel=Integer.toString(Integer.parseInt(Agno)-1);
                    int m=0;
                    LecTOT2=0;
                    for (DataSnapshot iten:snapshot.getChildren()){
                        m++;
                        lecsMap2.put(iten.getKey().toString(),Double.parseDouble(iten.child("lecturas").child(anuel).child("12").getValue().toString()));
                        LecTOT2=LecTOT2+Double.parseDouble(iten.child("lecturas").child(anuel).child("12").getValue().toString());
                        if (m==8) setServicios3();
                    }
                    if(m<8) Toast.makeText(getApplicationContext(),"faltan lecturas",Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }else{
            nodo.child("unidades").child("depas").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int m=0;
                    LecTOT2=0;
                    for (DataSnapshot iten:snapshot.getChildren()){
                        m++;
                        lecsMap2.put(iten.getKey().toString(),Double.parseDouble(iten.child("lecturas").child(Agno).
                                child(Integer.toString(Integer.parseInt(Mes)-1)).getValue().toString()));
                        LecTOT2=LecTOT2+Double.parseDouble(iten.child("lecturas").child(Agno).child(Integer.toString(Integer.parseInt(Mes)-1)).getValue().toString());
                        if (m==8) setServicios3();
                    }
                    if(m<8) Toast.makeText(getApplicationContext(),"faltan lecturas",Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private void setServicios3() {
        //Toast.makeText(this,"servicuartos",Toast.LENGTH_SHORT).show();
            if(spTipoInm.getSelectedItem().toString().equalsIgnoreCase("cuartos")) serviCuartos();

            if(spTipoInm.getSelectedItem().toString().equalsIgnoreCase("depas")) {
                double coble = 0;
                double intelnet = Minternet / 8;
                if (cabre.equalsIgnoreCase("1")) {
                    coble = Mcable / nroCables;
                }
                double agua = Magua / nroInqs * Integer.parseInt(ETnroPersonas.getText().toString());
                double gas = Mgas / nroInqs * Integer.parseInt(ETnroPersonas.getText().toString());
                iluz = lecsMap.get(spUnidad.getSelectedItem().toString()) - lecsMap2.get(spUnidad.getSelectedItem().toString());
                double eluz = LecTOT - LecTOT2 + LecCuartos - LecCuartos2;

                double luz = iluz / eluz * (MontoEdesa - Mimpuestos);
                double totServices = gas + agua + coble + intelnet + luz;
                txtservicios.setText(Double.toString(Math.round(totServices * 1.0)));
                //Toast.makeText(this,"= "+LecTOT2,Toast.LENGTH_SHORT).show();

            }

    }

    private void serviCuartos() {
        //Toast.makeText(this,"servicuartos",Toast.LENGTH_SHORT).show();
        if(Mes.equalsIgnoreCase("01")){
            nodo.child("servicios").child(Integer.toString(Integer.parseInt(Agno)-1)).child("12").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    lectCuart2=Double.parseDouble(snapshot.child("CII").child("cuartos").getValue().toString());
                    serviCuartos2();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            nodo.child("servicios").child(Agno).child(Double.toString(Double.parseDouble(Mes)-1)).child("CII").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    lectCuart2=Double.parseDouble(snapshot.child("cuartos").getValue().toString());
                    serviCuartos2();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void serviCuartos2() {
        double intelnet = Minternet/2;
        double agua = Magua / nroInqs * nroInqCuartos;
        double gas = Mgas / nroInqs * nroInqCuartos;
        iluz = LecCuartos - lectCuart2;
        double eluz = LecTOT - LecTOT2 + LecCuartos - LecCuartos2;

        double luz = iluz / eluz * (MontoEdesa - Mimpuestos);
        double totServices = gas + agua + intelnet + luz;
        txtservicios.setText(Double.toString(Math.round(totServices * 1.0)));
        //Toast.makeText(this,"="+nroInqCuartos,Toast.LENGTH_SHORT).show();
    }

    private void setLectura() {

        if(ETlectura.getText().toString().length()>2){
            if(zwitch.isChecked()){
                nodo.child("unidades").child(spTipoInm.getSelectedItem().toString()).child(spUnidad.getSelectedItem().toString()).child("cable").setValue("1");
                esconderKeyboard();
            }else {
                nodo.child("unidades").child(spTipoInm.getSelectedItem().toString()).child(spUnidad.getSelectedItem().toString()).child("cable").setValue("0");
                esconderKeyboard();
            }

            if(spTipoInm.getSelectedItem().toString().equalsIgnoreCase("depas")){
                nodo.child("unidades").child(spTipoInm.getSelectedItem().toString()).child(spUnidad.getSelectedItem().toString()).child("lecturas").
                        child(Agno).child(Mes).setValue(ETlectura.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                ETlectura.setText(null);
                                Toast.makeText(getApplicationContext(),"Lectura de medidor registrada",Toast.LENGTH_SHORT).show();
                            }
                        });
                esconderKeyboard();
            }else {
                nodo.child("servicios").child(Agno).child(Mes).child("CII").child("cuartos").setValue(ETlectura.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        ETlectura.setText(null);
                        Toast.makeText(getApplicationContext(),"Lectura de medidor registrada",Toast.LENGTH_SHORT).show();
                    }
                });
                esconderKeyboard();
            }

        }else Toast.makeText(this,"ingrese lectura de medidor",Toast.LENGTH_SHORT).show();
    }

    private void setPago() {
        String tIn=spTipoInm.getSelectedItem().toString();
        String idIn=spUnidad.getSelectedItem().toString();
        if(ETpago.getText().toString().length()>1){
            nodo.child("unidades").child(tIn).child(idIn).child("pagos").child(Agno).child(Mes).
                    child(spGlosa.getSelectedItem().toString()).setValue(ETpago.getText().toString());
            nodo.child("unidades").child(tIn).child(idIn).child("ingreso").setValue(ETfingreso.getText().toString());
            nodo.child("unidades").child(tIn).child(idIn).child("inq").setValue(ETnombre.getText().toString());
            nodo.child("unidades").child(tIn).child(idIn).child("nro").setValue(ETnroPersonas.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    ETpago.setText("");
                    esconderKeyboard();
                    seteaArrayDeudores();
                }
            });
        }else Toast.makeText(this,"ingrese monto de pago",Toast.LENGTH_SHORT).show();
    }

    private void seteaSpglosa() {
            nodo.child("glosas").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot item:snapshot.getChildren()){
                        arrayGlosa.add(item.getValue().toString());
                    }adGlosa.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
    }

    private void seteaSpUni(String uni) {
       nodo.child("unidades").child(uni).addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot item:snapshot.getChildren()){
                   arrayUni.add(item.getKey().toString());
               }
               adUni.notifyDataSetChanged();
           }
           @Override
           public void onCancelled(@NonNull DatabaseError error) {
           }
       });

    }

    private void seteaSptipo() {
        nodo.child("unidades").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item:snapshot.getChildren()){
                    arrayTipo.add(item.getKey().toString());
                }
                adTipo.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void seteaDatosInq(String inq){
        nodo.child("unidades").child(spTipoInm.getSelectedItem().toString()).child(spUnidad.getSelectedItem().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ETnombre.setText(snapshot.child("inq").getValue().toString());
                ETnroPersonas.setText(snapshot.child("nro").getValue().toString());
                nro=Integer.parseInt(snapshot.child("nro").getValue().toString());
                ETfingreso.setText(snapshot.child("ingreso").getValue().toString());
                cabre=snapshot.child("cable").getValue().toString();
                if(cabre.equalsIgnoreCase("1")){
                    zwitch.setChecked(true);
                }else zwitch.setChecked(false);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setServiciosGrales(){
        if(ETmontoElec.getText().length()>2&&ETmontAgu.getText().length()>2&&ETmontoGas.getText().length()>2){
            nodo.child("servicios").child(Agno).child(Mes).child("CII").child("cable").setValue(ETcable.getText().toString());
            nodo.child("servicios").child(Agno).child(Mes).child("CII").child("wifi").setValue(ETintern.getText().toString());
            nodo.child("servicios").child(Agno).child(Mes).child("CII").child("impuestos").setValue(ETimp.getText().toString());
            nodo.child("servicios").child(Agno).child(Mes).child("LAG").child("electricidad").child("lectura").setValue(ETlecElec.getText().toString());
            nodo.child("servicios").child(Agno).child(Mes).child("LAG").child("electricidad").child("monto").setValue(ETmontoElec.getText().toString());
            nodo.child("servicios").child(Agno).child(Mes).child("LAG").child("agua").child("lectura").setValue(ETlecAgu.getText().toString());
            nodo.child("servicios").child(Agno).child(Mes).child("LAG").child("agua").child("monto").setValue(ETmontAgu.getText().toString());
            nodo.child("servicios").child(Agno).child(Mes).child("LAG").child("gas").child("lectura").setValue(ETlecGas.getText().toString());
            nodo.child("servicios").child(Agno).child(Mes).child("LAG").child("gas").child("monto").setValue(ETmontoGas.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    ETcable.setText(null);
                    ETimp.setText(null);
                    ETintern.setText(null);
                    ETlecElec.setText(null);
                    ETmontoElec.setText(null);
                    ETlecAgu.setText(null);
                    ETmontAgu.setText(null);
                    ETlecGas.setText(null);
                    ETmontoGas.setText(null);
                }
            });
            esconderKeyboard();
        }else irAdministracion();
    }

    private void irAdministracion() {
        Intent intent=new Intent(this, Administracion.class);
        startActivity(intent);
    }

    private void getNroInq(){
        nroInqs=0;
        nroInqCuartos=0;
        nodo.child("unidades").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item:snapshot.getChildren()){
                    if(item.getKey().toString().equalsIgnoreCase("cuartos")){
                        for (DataSnapshot ritem:item.getChildren()){
                            nroInqCuartos=nroInqCuartos+Double.parseDouble(ritem.child("nro").getValue().toString());
                        }
                    }
                    nodo.child("unidades").child(item.getKey().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot iten:snapshot.getChildren()){
                                int nn=Integer.parseInt(iten.child("nro").getValue().toString());
                                nroInqs= nroInqs + nn;
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void getNroCable(){
        nroCables=0;
        nodo.child("unidades").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item:snapshot.getChildren()){
                    nodo.child("unidades").child(item.getKey().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot iten:snapshot.getChildren()){
                                int nm=Integer.parseInt(iten.child("cable").getValue().toString());
                                nroCables=nroCables+nm;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void seteaDeudores(){
        nodo.child("unidades").child("cuartos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int m=0;
                for(DataSnapshot item:snapshot.getChildren()){
                    if(item.child("pagos").child(Agno).child(Mes).exists()){
                        arrayCuartos.set(m,"");
                    }m++;
                }seteaDeudores2();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void seteaDeudores2() {
        nodo.child("unidades").child("depas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int m=0;
                for(DataSnapshot item:snapshot.getChildren()){
                    if(item.child("pagos").child(Agno).child(Mes).child("Alquiler").exists()){
                        arrayDepas.set(m,"");
                    }m++;
                }seteaDeudores3();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void seteaDeudores3() {
        deudores="";
        deudores2="";
        int n=1;
        int l=1;
        for(int i=0;i<8;i++){
            int x=0;
            if (arrayCuartos.get(i).equalsIgnoreCase(""))x=1;
            if (x==0){
                if(n%2==0){
                    deudores=deudores+arrayCuartos.get(i)+"\n";
                    n++;
                }else{
                    deudores=deudores+arrayCuartos.get(i)+" ";
                    n++;
                }
            }
        }
        for(int i=0;i<8;i++){
            int y=0;
            if (arrayDepas.get(i).equalsIgnoreCase(""))y=1;
            if (y==0){
                if(l%2==0){
                    deudores2=deudores2+arrayDepas.get(i)+"\n";
                    l++;
                }else{
                    deudores2=deudores2+arrayDepas.get(i)+" ";
                    l++;
                }
            }
        }
        txtMorosos.setText(deudores+"\n"+deudores2);
    }
    private void esconderKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}