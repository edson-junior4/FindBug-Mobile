package com.example.findbug;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.findbug.Dominio.DatabaseAcess;
import com.example.findbug.Dominio.Inseto;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner SpnTipo;
    Spinner SpnLavoura;
    Inseto ClassInseto;
    public String Lavoura;
    public String TIPO;
    DatabaseAcess db;
    public static List<String> resultado;

    //Função para transmição do resultado do search para o MenuBar
    public static List<String> getResult() {
        return resultado;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //init database
        db = new DatabaseAcess(this);
        ClassInseto = new Inseto();
        final DatabaseAcess databaseAcess = DatabaseAcess.getInstance(this);
        databaseAcess.open();
        SpnTipo = findViewById(R.id.SpnTipo);
        SpnLavoura = findViewById(R.id.SpnLavoura);
        resultado = new ArrayList<>();

        //===================CONFIGURAÇÃO SPINNERS=========================
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.TIPO, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(R.layout.spinner_item);
        SpnTipo.setAdapter(adapter1);
        SpnTipo.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.LAVOURA, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(R.layout.spinner_item);
        SpnLavoura.setAdapter(adapter2);
        SpnLavoura.setOnItemSelectedListener(this);
        //=================================================================

        //Função FLOATINGBUTTON, onde a operação do DatabaseAcess-SearchInseto é chamada.
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resultado = db.SearchInseto(TIPO, Lavoura);

                if (String.valueOf(resultado) == "[]" || String.valueOf(resultado) == null) {
                    Toast.makeText(Search.this, "RESULTADO NÃO ENCONTRADO", Toast.LENGTH_SHORT).show();
                } else {
                    //Chamar o activity MenuBar
                    Intent it = new Intent(Search.this, MenuBar.class);
                    startActivity(it);
                }
            }
        });
    }

    //Chamada do menu (Haverá mais funções no futuro)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem search = menu.findItem(R.id.search);
        return true;

    }

    //Avaliação no Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.avaliar:

                //String url = "https://forms.gle/MAZdKCCQiZNQWDF5A";

                //Intent it = new Intent(Intent.ACTION_VIEW);
                //it.setData(Uri.parse(url));
                Intent it = new Intent(Search.this, Fav.class);
                startActivity(it);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //=====Registro das opções selecionadas nos Spinners====
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spin = (Spinner)parent;

        if(spin.getId() == R.id.SpnTipo){
            if (position == 0) {
                TIPO = null;
            } else {
                TIPO = SpnTipo.getItemAtPosition(position).toString();
            }
        }
        if(spin.getId() == R.id.SpnLavoura){
            if (position == 0) {
                Lavoura = null;
            } else {
                Lavoura = SpnLavoura.getItemAtPosition(position).toString();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    //=======================================================


}
