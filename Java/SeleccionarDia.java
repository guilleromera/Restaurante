package com.example.restaurante;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SeleccionarDia extends AppCompatActivity {

    //Variables
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private Button boton;
    private String fechaSeleccionada;
    private EditText fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_dia);
        this.setTitle("Selecciona el día");

        fecha=findViewById(R.id.editTextFecha);

        //instancia de FirebaseAuth para realizar operaciones de autenticación de usuarios en Firebase
        mAuth = FirebaseAuth.getInstance();

        //Obtengo la referencia de la base de datos
        db = FirebaseFirestore.getInstance();

        // Obtener referencias de las vistas
        boton = findViewById(R.id.btnConfirmar);

        // Llamar al método de inicio de sesión
        iniciarSesion("juillemellamo@gmail.com", "Tx_vnvmKJUZ:Q6x");

        // Configura el OnClickListener para mostrar el DatePicker cuando se haga clic en el EditText de fecha
        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatePicker();
            }
        });

        // Configurar el OnClickListener del botón
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarReservasPorDia();
            }
        });
    }

    private void mostrarDatePicker() {
        // Obtiene la fecha actual para establecerla como fecha predeterminada en el DatePicker
        Calendar calendar = Calendar.getInstance();
        int año = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        // Crea un DatePickerDialog con la fecha actual
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int año, int mes, int dia) {
                // Actualiza el texto del EditText con la fecha seleccionada
                fechaSeleccionada = dia + "/" + (mes + 1) + "/" + año;
                fecha.setText(fechaSeleccionada);
            }
        }, año, mes, dia);

        // Muestra el DatePickerDialog
        datePickerDialog.show();
    }

    //método para verificar el inicio de sesión solo con los usuarios creados para ello en firestore
    private void iniciarSesion(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Inicio de sesión exitoso
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // Error en el inicio de sesión
                            Toast.makeText(SeleccionarDia.this, "Error en el inicio de sesión",
                                    Toast.LENGTH_SHORT).show();
                            finish(); //finalizo la activity o quizá lo mande a Activity de inicio
                        }
                    }
                });
    }

    //método para consultar si existen reservas en ese día, si no existen lo muestra por pantalla
    private void consultarReservasPorDia() {

        // Realizar la consulta en Firestore para obtener las reservas en la fecha seleccionada
        db.collection("Reservas")
                .whereEqualTo("fecha", fechaSeleccionada)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                // No hay reservas para la fecha seleccionada
                                Toast.makeText(SeleccionarDia.this, "No hay reservas para este día", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(SeleccionarDia.this, ReservasDia.class); // Lo envío a ReservaDia
                                startActivity(intent);
                                //Envío los datos a la otra activity
                                intent.putExtra("fecha", fechaSeleccionada);
                                startActivity(intent);
                            }
                        } else {
                            // Error al obtener las reservas
                            Toast.makeText(SeleccionarDia.this, "Error al obtener las reservas", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}