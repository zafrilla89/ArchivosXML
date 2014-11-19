package com.example.izv.telefonos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Xml;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class Principal extends Activity {
    private ArrayList<Telefono> datos;
    private Adaptador ad;
    private ListView lv;
    private EditText etmarca, etmodelo, etprecio, etstock;
    String marca, modelo, precio, stock;
    boolean semaforo=false;


    /***********************************************************************/
    /*                                                                     */
    /*                              METODOS ON                             */
    /*                                                                     */
    /***********************************************************************/

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id=item.getItemId();
        AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int index=info.position;
        if (id == R.id.m_editar) {
            editar(index);
        }else {
            if (id == R.id.m_borrar) {
                datos.remove(index);
                ad.notifyDataSetChanged();
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);
        iniciarComponentes();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menucontextual,menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.m_anadir) {
            return anadir();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        datos =savedInstanceState.getParcelableArrayList("datos");
    }

    @Override
    protected void onSaveInstanceState(Bundle savingInstanceState) {
        super.onSaveInstanceState(savingInstanceState);
        savingInstanceState.putParcelableArrayList("datos", datos);
    }

    /***********************************************************************/
    /*                                                                     */
    /*                        METODOS AUXILIARES                           */
    /*                                                                     */
    /***********************************************************************/

    public boolean anadir(){
        Intent i = new Intent(this,Anadir.class);
        Bundle b = new Bundle();
        b.putParcelableArrayList("datos", datos);
        i.putExtras(b);
        startActivity(i);
        return true;
    }

    public void añadirarchivo(){
        Telefono tl=new Telefono();
        try {
            FileOutputStream fosxml= new FileOutputStream(new File(getExternalFilesDir(null),"archivo.xml"));
            XmlSerializer docxml = Xml.newSerializer();
            docxml.setOutput(fosxml, "UTF-8");
            docxml.startDocument(null, Boolean.valueOf(true));
            docxml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            docxml.startTag(null, "Telefonos");
            for (int i=0;i<datos.size();i++) {
                tl=datos.get(i);
                docxml.startTag(null, "Telefono");
                docxml.attribute(null, "Id", tl.getId());
                docxml.attribute(null, "Marca", tl.getMarca());
                docxml.attribute(null, "Modelo", tl.getModelo());
                docxml.attribute(null, "Precio", tl.getPrecio());
                docxml.attribute(null, "Stock", tl.getStock());
                docxml.endTag(null, "Telefono");
            }
            docxml.endTag(null, "Telefonos");
            docxml.endDocument();
            docxml.flush();
            fosxml.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean comprueba(Telefono tl){
        Telefono tl2;
        for (int i=0;i<datos.size();i++){
            tl2=datos.get(i);
            if(tl.equals(tl2)){
                return false;
            }
        }
        return true;
    }

    public boolean editar(final int index){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.editartelefono);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.dialogo, null);
        alert.setView(vista);
        etmarca = (EditText) vista.findViewById(R.id.etmarca);
        etmodelo = (EditText) vista.findViewById(R.id.etmodelo);
        etprecio=(EditText)vista.findViewById(R.id.etprecio);
        etstock=(EditText)vista.findViewById(R.id.etstock);
        Telefono tl=new Telefono();
        tl=datos.get(index);
        marca=tl.getMarca();
        modelo=tl.getModelo();
        precio=tl.getPrecio();
        stock=tl.getStock();
        etmarca.setText(marca);
        etmodelo.setText(modelo);
        etprecio.setText(precio);
        etstock.setText(stock);
        alert.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String id="";
                        etmarca = (EditText) vista.findViewById(R.id.etmarca);
                        etmodelo = (EditText) vista.findViewById(R.id.etmodelo);
                        etprecio=(EditText)vista.findViewById(R.id.etprecio);
                        etstock=(EditText)vista.findViewById(R.id.etstock);
                        marca=etmarca.getText().toString().trim();
                        modelo=etmodelo.getText().toString().trim();
                        precio=etprecio.getText().toString();
                        stock=etstock.getText().toString();
                        if (marca.length() > 0 && modelo.length() > 0 && precio.length() > 0 && stock.length() > 0 ) {
                            Telefono tl2=new Telefono(marca,modelo,precio,stock, id);
                            boolean comprobar=true;
                            comprobar=comprueba(tl2);
                            if(comprobar==true) {
                                tl2.setId(index+1+"");
                                editarxml(tl2);
                                datos.set(index, tl2);
                                tostada("TELÉFONO EDITADO");
                            }else{
                                tostada("TELÉFONO REPETIDO");
                            }
                            Collections.sort(datos);
                            ad.notifyDataSetChanged();
                            semaforo=true;
                        }else{
                            semaforo=false;
                        }
                        if(semaforo==false) {
                            tostada("NO RELLENASTE TODOS LOS CAMPOS");
                        }
                    }
                });
        alert.setNegativeButton(android.R.string.no,null);
        alert.show();
        return true;
    }

    public void editarxml(Telefono tl2){
        Telefono tl=new Telefono();
        try {
            FileOutputStream fosxml= new FileOutputStream(new File(getExternalFilesDir(null),"archivo.xml"));
            XmlSerializer docxml = Xml.newSerializer();
            docxml.setOutput(fosxml, "UTF-8");
            docxml.startDocument(null, Boolean.valueOf(true));
            docxml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            docxml.startTag(null, "Telefonos");
            for (int i=0;i<datos.size();i++) {
                tl=datos.get(i);
                if(tl2.getId().compareTo(i+1+"")==0){
                    docxml.startTag(null, "Telefono");
                    docxml.attribute(null, "Id", tl2.getId());
                    docxml.attribute(null, "Marca", tl2.getMarca());
                    docxml.attribute(null, "Modelo", tl2.getModelo());
                    docxml.attribute(null, "Precio", tl2.getPrecio());
                    docxml.attribute(null, "Stock", tl2.getStock());
                    docxml.endTag(null, "Telefono");
                }else {
                    docxml.startTag(null, "Telefono");
                    docxml.attribute(null, "Id", tl.getId());
                    docxml.attribute(null, "Marca", tl.getMarca());
                    docxml.attribute(null, "Modelo", tl.getModelo());
                    docxml.attribute(null, "Precio", tl.getPrecio());
                    docxml.attribute(null, "Stock", tl.getStock());
                    docxml.endTag(null, "Telefono");
                }
            }
            docxml.endTag(null, "Telefonos");
            docxml.endDocument();
            docxml.flush();
            fosxml.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void iniciarComponentes() {
        datos = new ArrayList();
        Bundle b = getIntent().getExtras();
        if(b !=null ){
            datos = b.getParcelableArrayList("datos");
            Collections.sort(datos);
            añadirarchivo();
        }else {
            leerarchivo();
        }
        Collections.sort(datos);
        ad = new Adaptador(this,R.layout.itemlista,datos);
        lv = (ListView)findViewById(R.id.lvlista);
        lv.setAdapter(ad);
        registerForContextMenu(lv);
    }

    public void leerarchivo(){

        File archivo = new File(getExternalFilesDir(null), "archivo.xml");
        if (archivo.exists()) {
            XmlPullParser lectorxml = Xml.newPullParser();
            try {
                lectorxml.setInput(new FileInputStream(new File(getExternalFilesDir(null), "archivo.xml")), "utf-8");
                int evento = lectorxml.getEventType();
                while (evento != XmlPullParser.END_DOCUMENT) {
                    if (evento == XmlPullParser.START_TAG) {
                        Telefono tl = new Telefono();
                        String etiqueta = lectorxml.getName();
                        if (etiqueta.compareTo("Telefono") == 0) {
                            tl.setId(lectorxml.getAttributeValue(null, "Id"));
                            tl.setMarca(lectorxml.getAttributeValue(null, "Marca"));
                            tl.setModelo(lectorxml.getAttributeValue(null, "Modelo"));
                            tl.setPrecio(lectorxml.getAttributeValue(null, "Precio"));
                            tl.setStock(lectorxml.getAttributeValue(null, "Stock"));
                            datos.add(tl);
                        }
                    }
                    evento = lectorxml.next();
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void tostada (String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    /***********************************************************************/
    /*                                                                     */
    /*                METODOS ONCLICK SOBRE LAS IMAGENES                   */
    /*                                                                     */
    /***********************************************************************/

    public void delete(View v){
        int index;
        index=(Integer)v.getTag();
        datos.remove(index);
        añadirarchivo();
        ad.notifyDataSetChanged();
    }

    public void edit(View v){
        int index=-1;
        index=(Integer)v.getTag();
        editar(index);
    }

}
