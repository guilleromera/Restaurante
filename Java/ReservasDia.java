package com.example.restaurante;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ReservasDia extends AppCompatActivity {

    //Variables
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private TextView totalReservas;
    private String fechaseleccionada;

    private Button botoncito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva_dia);
        this.setTitle("Reservas del día");

        //instancia de FirebaseAuth para realizar operaciones de autenticación de usuarios en Firebase
        mAuth = FirebaseAuth.getInstance();

        //Obtengo la referencia de la base de datos
        db = FirebaseFirestore.getInstance();

        // Obtengo el correo electrónico enviado desde la otra Activity
        Intent intent = getIntent();
        fechaseleccionada= intent.getStringExtra("fecha");
        botoncito=findViewById(R.id.botoncito);

        //inicializo el textview y le paso a fecha en cuestión
        totalReservas=findViewById(R.id.textViewReservas);
        totalReservas.setText("Reservas de: "+fechaseleccionada);

        // Llamar al método de inicio de sesión
        iniciarSesion("juillemellamo@gmail.com", "Tx_vnvmKJUZ:Q6x");

        // Configurar el OnClickListener del botón para volver atrás
        botoncito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReservasDia.this, MainActivity.class); // Lo envío al menú
                startActivity(intent);
            }
        });

        mostrarReservas();
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
                            Toast.makeText(ReservasDia.this, "Error en el inicio de sesión",
                                    Toast.LENGTH_SHORT).show();
                            finish(); //finalizo la activity o quizá lo mande a Activity de inicio
                        }
                    }
                });
    }

    private void mostrarReservas() {
        String nuevaFecha=fechaseleccionada;
        db.collection("Reservas").whereEqualTo("fecha", nuevaFecha).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    LinearLayout linearLayout = findViewById(R.id.linear); // ID del LinearLayout contenedor en el XML
                    linearLayout.removeAllViews(); // Limpiar las vistas anteriores (si las hay)

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Obtener los datos de cada reserva
                        String nombre = document.getString("nombre");
                        String correo = document.getString("email");
                        String fecha = document.getString("fecha");
                        String hora = document.getString("hora");
                        String comensales = document.getString("comensales");
                        String primero = document.getString("primero");
                        String segundo = document.getString("segundo");
                        String postre = document.getString("postre");

                        // Crea una instancia de CardView
                        CardView cardView = new CardView(ReservasDia.this);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                //se establece el ancho y el alto de cada layout
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        //Obtiene el valor del margen definido de una carpeta llamada dime
                        int margin = getResources().getDimensionPixelSize(R.dimen.card_margin);
                        //seteo los márgenes parámetros del layout
                        layoutParams.setMargins(margin, margin, margin, margin);
                        cardView.setLayoutParams(layoutParams);

                        // Configurar la apariencia de la CardView
                        cardView.setCardElevation(getResources().getDimensionPixelSize(R.dimen.card_elevation));
                        cardView.setUseCompatPadding(true);

                        // Crear TextViews para mostrar los datos de la reserva
                        TextView textViewNombre = new TextView(ReservasDia.this);
                        textViewNombre.setText("Nombre: " + nombre);
                        TextView textViewCorreo = new TextView(ReservasDia.this);
                        textViewCorreo.setText("Correo: " + correo);
                        TextView textViewFecha = new TextView(ReservasDia.this);
                        textViewFecha.setText("Fecha: " + fecha);
                        TextView textViewHora = new TextView(ReservasDia.this);
                        textViewHora.setText("Hora: " + hora);
                        TextView textViewComensales = new TextView(ReservasDia.this);
                        textViewComensales.setText("Comensales: " + comensales);
                        TextView textViewPrimero = new TextView(ReservasDia.this);
                        textViewPrimero.setText("Primero: " + primero);
                        TextView textViewSegundo = new TextView(ReservasDia.this);
                        textViewSegundo.setText("Segundo: " + segundo);
                        TextView textViewPostre = new TextView(ReservasDia.this);
                        textViewPostre.setText("Postre: " + postre);

                        // Agregar los TextViews a la CardView
                        LinearLayout cardContentLayout = new LinearLayout(ReservasDia.this);
                        cardContentLayout.setOrientation(LinearLayout.VERTICAL);
                        cardContentLayout.addView(textViewNombre);
                        cardContentLayout.addView(textViewCorreo);
                        cardContentLayout.addView(textViewFecha);
                        cardContentLayout.addView(textViewHora);
                        cardContentLayout.addView(textViewComensales);
                        cardContentLayout.addView(textViewPrimero);
                        cardContentLayout.addView(textViewSegundo);
                        cardContentLayout.addView(textViewPostre);
                        cardView.addView(cardContentLayout);

                        // Agregar la CardView al LinearLayout
                        linearLayout.addView(cardView);
                    }
                } else {
                    Toast.makeText(ReservasDia.this, "Error al obtener las reservas", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




}