package com.example.izv.telefonos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;


public class Anadir extends Activity {
    private EditText etmarca, etmodelo, etprecio, etstock;
    private String marca, modelo, precio, stock, id;
    private boolean semaforo=false;
    private ArrayList <Telefono> datos;
    private Telefono tl;
    boolean comprobar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_anadir);
        Bundle b = getIntent().getExtras();
        if(b !=null ){
            datos = b.getParcelableArrayList("datos");
        }
    }

    public void generarid(){
        Telefono tl;
        for (int i=0;i<datos.size();i=i+1) {
            tl=datos.get(i);
            tl.setId(i+"");
            datos.set(i,tl);
        }
    }

    public boolean comprueba(Telefono tl2){
        Telefono tl;
        for (int i=0;i<datos.size();i++){
            tl=datos.get(i);
            if(tl.equals(tl2)==true){
                return false;
            }
        }
        return true;
    }

    public void guardar(View view){
        id="";
        etmarca = (EditText) findViewById(R.id.etamarca);
        etmodelo = (EditText) findViewById(R.id.etamodelo);
        etprecio=(EditText)findViewById(R.id.etaprecio);
        etstock=(EditText)findViewById(R.id.etastock);
        marca=etmarca.getText().toString().trim();
        modelo=etmodelo.getText().toString().trim();
        precio=etprecio.getText().toString();
        stock=etstock.getText().toString();
        if (marca.length() > 0 && modelo.length() > 0 && precio.length() > 0 && stock.length() > 0 ) {
            comprobar=true;
            tl = new Telefono(marca, modelo, precio, stock,id);
            comprobar=comprueba(tl);
            if(comprobar==true){
                datos.add(tl);
                tostada("TELÉFONO REGISTRADO");
            }else{
                tostada("TELÉFONO REPETIDO");
            }
            Collections.sort(datos);
            generarid();
            semaforo=true;
        }else{
            semaforo=false;
        }
        if(semaforo==false) {
            tostada("NO RELLENASTE TODOS LOS CAMPOS");
        }

        Intent i = new Intent(this,Principal.class);
        Bundle b = new Bundle();
        b.putParcelableArrayList("datos", datos);
        i.putExtras(b);
        startActivity(i);
    }

    public void tostada (String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


}