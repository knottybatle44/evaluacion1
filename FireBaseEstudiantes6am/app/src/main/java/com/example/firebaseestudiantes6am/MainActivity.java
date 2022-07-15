package com.example.firebaseestudiantes6am;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText jetidentificacion,jetnombre,jetcarrera,jetsemestre;
    Button jbtadicionar,jbtconsultar,jbtmodificar,jbteliminar,jbtlimpiar;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    String identificacion,nombre,carrera,semestre;
    String idusuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        jetidentificacion=findViewById(R.id.etidentificacion);
        jetnombre=findViewById(R.id.etnombre);
        jetcarrera=findViewById(R.id.etcarrera);
        jetsemestre=findViewById(R.id.etsemestre);
        jbtadicionar=findViewById(R.id.btadicionar);
        jbtconsultar=findViewById(R.id.btconsultar);
        jbtmodificar=findViewById(R.id.btmodificar);
        jbteliminar=findViewById(R.id.bteliminar);
        jbtlimpiar=findViewById(R.id.btlimpiar);

        jbtadicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Adicionar();
            }
        });

        jbtconsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Consultar();
            }
        });

        jbtmodificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Modificar();
            }
        });

        jbteliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Eliminar();
            }
        });

        jbtlimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Limpiar_campos();
            }
        });
    }

    private void Adicionar() {
        identificacion=jetidentificacion.getText().toString();
        nombre=jetnombre.getText().toString();
        carrera=jetcarrera.getText().toString();
        semestre=jetsemestre.getText().toString();
        if (identificacion.isEmpty() || nombre.isEmpty() || carrera.isEmpty() || semestre.isEmpty()){
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetidentificacion.requestFocus();
        }
        else {
            Map<String, Object> user = new HashMap<>();
            user.put("identificacion",identificacion);
            user.put("nombre", nombre);
            user.put("carrera",carrera);
            user.put("semestre",semestre);

            // Add a new document with a generated ID
            db.collection("Estudiante")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Limpiar_campos();
                            Toast.makeText(MainActivity.this, "Documento adicionado", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Error adicionando documento", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void Consultar(){
        identificacion=jetidentificacion.getText().toString();
        if (identificacion.isEmpty()){
            Toast.makeText(this, "La identificacion es requerida para consultar", Toast.LENGTH_SHORT).show();
            jetidentificacion.requestFocus();
        }
        else {
            db.collection("Estudiante")
                    .whereEqualTo("identificacion", jetidentificacion.getText().toString())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    idusuario=document.getId();
                                    jetnombre.setText(document.getString("nombre"));
                                    jetcarrera.setText(document.getString("carrera"));
                                    jetsemestre.setText(document.getString("semestre"));
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Registro no encontrado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void Modificar(){
        identificacion=jetidentificacion.getText().toString();
        nombre=jetnombre.getText().toString();
        carrera=jetcarrera.getText().toString();
        semestre=jetsemestre.getText().toString();
        if (identificacion.isEmpty() || nombre.isEmpty() || carrera.isEmpty() || semestre.isEmpty()){
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetidentificacion.requestFocus();
        }
        else {
            Map<String, Object> user = new HashMap<>();
            user.put("identificacion", jetidentificacion.getText().toString());
            user.put("nombre", jetnombre.getText().toString());
            user.put("carrera", jetcarrera.getText().toString());
            user.put("semestre", jetsemestre.getText().toString());

            db.collection("Estudiante").document(idusuario)
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this,"Estudiante actualizada correctmente...",Toast.LENGTH_SHORT).show();
                            Limpiar_campos();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this,"Error actualizando persona...",Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    private void Eliminar(){
        identificacion=jetidentificacion.getText().toString();
        if (identificacion.isEmpty()){
            Toast.makeText(this, "Digite una identificacion", Toast.LENGTH_SHORT).show();
            jetidentificacion.requestFocus();
        }
        else {
            db.collection("Estudiante").document(idusuario)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Limpiar_campos();
                            Toast.makeText(MainActivity.this, "Estudiante eliminada correctamente...", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Error eliminando Estudiante...", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void Limpiar_campos(){
        jetidentificacion.setText("");
        jetnombre.setText("");
        jetsemestre.setText("");
        jetcarrera.setText("");
        jetidentificacion.requestFocus();
    }
}