package com.example.izv.telefonos;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Telefono implements Serializable, Comparable, Parcelable {
    private String marca, modelo, precio, stock ,id;

    public Telefono(String marca, String modelo, String precio, String stock, String id) {
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.stock = stock;
        this.id=id;
    }

    public Telefono() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    @Override
    public int compareTo(Object o) {
        Telefono tl = (Telefono) o;
        if (this.getMarca().compareToIgnoreCase(tl.getMarca()) == 0) {
            if (this.getModelo().compareToIgnoreCase(tl.getModelo()) != 0) {
                return this.getModelo().compareToIgnoreCase(tl.getModelo());
            }
        } else {
            return this.getMarca().compareToIgnoreCase(tl.getMarca());
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        Telefono telefono = (Telefono) o;

        if (marca.equalsIgnoreCase(telefono.marca) && modelo.equalsIgnoreCase(telefono.modelo)) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int result = marca.hashCode();
        result = 31 * result + modelo.hashCode();
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.marca);
        parcel.writeString(this.modelo);
        parcel.writeString(this.precio);
        parcel.writeString(this.stock);
        parcel.writeString(this.id);
    }


    public static final Parcelable.Creator<Telefono> CREATOR = new Parcelable.Creator<Telefono>() {

        @Override
        public Telefono createFromParcel(Parcel p) {
            String marca = p.readString();
            String modelo = p.readString();
            String precio = p.readString();
            String stock = p.readString();
            String id = p.readString();
            return new Telefono(marca, modelo, precio, stock, id);
        }

        @Override
        public Telefono[] newArray(int i) {
            return new Telefono[i];
        }
    };
}
