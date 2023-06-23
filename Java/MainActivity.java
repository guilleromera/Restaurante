package com.example.restaurante;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Configuración de variables
    private Spinner sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Principal");

        sp=findViewById(R.id.spinner);
        //método de llamada a configurar Spinner
        configurarSpinner();
    }

    private void configurarSpinner() {

        // Creo un Adapter al que le paso la lista de valores del array creado para los servicios
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.opciones_spinner, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

        sp.setAdapter(adapter);

        // Listener que espera a que una opción sea seleccionada
        sp.setSelection(0);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int opc = sp.getSelectedItemPosition();
                if (opc == 1) { // Si la opción escogida es distinta a la que está por defecto, entra
                    Intent intent = new Intent(MainActivity.this, ReservasTodas.class); // Lo envío a ContratarServicios
                    startActivity(intent);
                }
                else if (opc == 2) { // Si la opción escogida es distinta a la que está por defecto, entra
                    Intent intent = new Intent(MainActivity.this, SeleccionarDia.class); // Lo envío a ContratarServicios
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // No se ha seleccionado ninguna opción
            }
        });
    }

}