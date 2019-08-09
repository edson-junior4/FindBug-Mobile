package com.example.findbug.Dominio;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAcess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAcess instance;

    public DatabaseAcess(Context context){
        this.openHelper = new BancoController(context);
    }
    public static DatabaseAcess getInstance(Context context){
        if (instance == null){
            instance = new DatabaseAcess(context);
        }
        return instance;
    }

    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    public void close(){
        if(database != null){
            this.database.close();
        }

    }
    public static final String TABELA_INSETO = "tb_insetos";
    public static final String COLUNA_COD = "_id";
    public static final String COLUNA_TIPO = "tipo";
    public static final String COLUNA_LAVOURA = "lavoura";
    public static final String COLUNA_INF_ADICIONAIS = "inf_adicionais";
    public static final String COLUNA_IMAGENS = "imagem";

    public Inseto selecionarInseto(int codigo){

        SQLiteDatabase db = this.openHelper.getReadableDatabase();

        Cursor cursor = db.query(TABELA_INSETO, new String[]{COLUNA_COD,
                        COLUNA_TIPO, COLUNA_LAVOURA, COLUNA_INF_ADICIONAIS, COLUNA_IMAGENS}, COLUNA_COD + " = ?",
                new String[]{String.valueOf(codigo)}, null, null, null, null);

        if(cursor!=null) {
            cursor.moveToFirst();
        }

        Inseto inseto = new Inseto(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));


        return inseto;
    }

    public List<String> SearchInseto(String Tipo, String Lavoura) {
        List<String> result = new ArrayList<String>();
        SQLiteDatabase db = this.openHelper.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();


        Log.d("Lavoura", String.valueOf(Lavoura));
        Log.d("Tipo", String.valueOf(Tipo));

        qb.setTables(TABELA_INSETO);

        Cursor c1 = db.query(TABELA_INSETO, new String[]{COLUNA_COD,
                COLUNA_TIPO, COLUNA_LAVOURA, COLUNA_INF_ADICIONAIS, COLUNA_IMAGENS}, COLUNA_TIPO + "=? and " + COLUNA_LAVOURA + "=?",
                new String[]{String.valueOf(Tipo),String.valueOf(Lavoura)}, null, null,null,null);

        if(c1.moveToFirst()){
            do{
                Log.d("Ent", "Entrou no bagui");
                result.add(c1.getString(0));
            }while(c1.moveToNext());
        }

        return result;
    }

    public List<Inseto> todosInsetos(){

        List<Inseto> listaInsetos = new ArrayList<Inseto>();

        String query = "SELECT * FROM " + TABELA_INSETO;

        SQLiteDatabase db = this.openHelper.getReadableDatabase();

        Cursor c = db.rawQuery(query, null);

        if(c.moveToFirst()){
            do {
                Inseto inseto = new Inseto();
                inseto.setCodigo(Integer.parseInt(c.getString(0)));
                inseto.setTipo(c.getString(1));
                inseto.setLavoura(c.getString(2));
                inseto.setInf_adicionais(c.getString(3));

                listaInsetos.add(inseto);
            }while(c.moveToNext());
        }
        return listaInsetos;
    }
    public List<String> getQuotes(){
        List<String> list = new ArrayList<>();
        Cursor c = database.rawQuery("SELECT * FROM tb_insetos", null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            list.add(String.valueOf(c.getLong(0)));
            c.moveToNext();
        }
        c.close();
        return list;
    }

    public byte[] PegarImagensByID(String ID) {
        SQLiteDatabase database = this.openHelper.getWritableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] select = {"_id", "tipo", "lavoura", "inf_adicionais", "imagem"};

        qb.setTables("tb_insetos");

        Cursor c = qb.query(database, select, "_id = ?", new String[]{String.valueOf(ID)}, null, null, null);
        byte[] resultado = null;
        if (c.moveToFirst()) {
            do {
                resultado = c.getBlob(4);
            } while (c.moveToNext());
        }
        return resultado;
    }






}
