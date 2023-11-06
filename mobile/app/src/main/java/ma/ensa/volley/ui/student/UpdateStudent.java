package ma.ensa.volley.ui.student;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ma.ensa.volley.R;
import ma.ensa.volley.beans.Filiere;
import ma.ensa.volley.beans.Role;
import ma.ensa.volley.beans.Student;

public class UpdateStudent extends AppCompatActivity {
    RequestQueue requestQueue;
    private  String id;
    private Student student;
    private List<Filiere> filieres = new ArrayList<>();
    private List<Role> roles = new ArrayList<>();
    private Filiere selectedFiliere;
    private List<Role> selectedRoles;
    private boolean[] checkedRoles;
    private EditText firstname, lastName, userName, email, password, phone;
    private TextView chooseRoles;
    private Button submit;
    private Button deleteBtn;
    private String rolesStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        getFilieres();
        getRoles();
        getStudent(id);
        firstname = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        userName = findViewById(R.id.userName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        submit = findViewById(R.id.add);
        deleteBtn = findViewById(R.id.deletebtn);
        chooseRoles = findViewById(R.id.roles);
        chooseRoles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRolesDialog();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitStudent();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateStudent.this);
                builder.setTitle("Confirmation de suppression");
                builder.setMessage("Do you want to delete this student?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteStudent(Integer.parseInt(id));
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

    }
    public void getStudent(String id){
        String getSUrl = "http://192.168.1.109:8080/api/v1/students/"+id;
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                getSUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                TypeToken<Student> token = new TypeToken<Student>() {};
                student = gson.fromJson(response.toString(), token.getType());
                Log.d("student", student.toString());
                firstname.setText(student.getFirstName());
                lastName.setText(student.getLastName());
                userName.setText(student.getUsername());
                email.setText(student.getEmail());
                password.setText(student.getPassword());
                phone.setText(student.getPhone()+"");
                setFiliereAndRoles();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Erreur", error.toString());
            }
        });
        requestQueue.add(request);
    }
    private void getFilieres() {
        String getFUrl = "http://192.168.1.109:8080/api/v1/filieres";
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                getFUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
                Log.d("filieres",response.toString());
                TypeToken<List<Filiere>> token = new TypeToken<List<Filiere>>() {};
                filieres = gson.fromJson(response.toString(), token.getType());


                final HashMap<String, Filiere> filiereMap = new HashMap<>();
                for (Filiere filiere : filieres) {
                    filiereMap.put(filiere.getLibelle(), filiere);
                }


                List<String> nomFilieres = new ArrayList<>(filiereMap.keySet());

                // Initialiser et lier le Spinner avec la liste de noms de filières
                ArrayAdapter<String> adapter = new ArrayAdapter<>(UpdateStudent.this, android.R.layout.simple_spinner_item, nomFilieres);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Spinner spinner = findViewById(R.id.spinner);
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        // Obtenez le nom de la filière sélectionnée
                        String selectedFiliereNom = (String) parentView.getItemAtPosition(position);

                        // Obtenez l'objet Filiere correspondant à partir du HashMap
                        selectedFiliere = filiereMap.get(selectedFiliereNom);

                        // Faites quelque chose avec l'objet Filiere sélectionné
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // Code à exécuter lorsque rien n'est sélectionné
                    }
                });
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Erreur", error.toString());
            }
        });
        requestQueue.add(request);
    }
    private void getRoles() {
        String getRolesUrl = "http://192.168.1.109:8080/api/v1/roles";
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, getRolesUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Gson gson = new Gson();
                        TypeToken<List<Role>> token = new TypeToken<List<Role>>() {};
                        roles = gson.fromJson(response.toString(), token.getType());

                        // Initialize checkedRoles array with the same size as roles list
                        checkedRoles = new boolean[roles.size()];
                        Arrays.fill(checkedRoles, false);

                        Log.d("roles", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Erreur", error.toString());
            }
        });
        requestQueue.add(request);
    }

    // Modify showRolesDialog() method
    private void showRolesDialog() {
        if (roles.isEmpty()) {
            // Handle the case when roles list is empty, maybe show an error message
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choisissez les rôles");

        // Créez un tableau de noms de rôles à partir de la liste de rôles disponibles
        String[] rolesArray = new String[roles.size()];

        for (int i = 0; i < roles.size(); i++) {
            rolesArray[i] = roles.get(i).getName();
        }

        // Configurez les cases à cocher dans l'AlertDialog
        builder.setMultiChoiceItems(rolesArray, checkedRoles, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                // Mettez à jour l'état de la case à cocher lorsqu'elle est cliquée
                checkedRoles[which] = isChecked;
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Traitez les rôles cochés ici
                selectedRoles = new ArrayList<>();
                for (int j = 0; j < roles.size(); j++) {
                    if (checkedRoles[j]) {
                        selectedRoles.add(roles.get(j));
                        rolesStr = "";
                        for (Role role: selectedRoles){
                            rolesStr += role.getName() + "   ";
                        }
                        chooseRoles.setText(rolesStr);
                    }
                }

                // Faites quelque chose avec la liste de rôles sélectionnée (selectedRoles)
            }
        });

        builder.setNegativeButton("Annuler", null);

        // Affichez l'AlertDialog
        builder.show();
    }

    public void submitStudent() {
        String insertUrl = "http://192.168.1.109:8080/api/v1/students";
        JSONObject jsonBody = new JSONObject();
        try {
            // Créer un JSONArray pour stocker les rôles sélectionnés
            JSONArray rolesArray = new JSONArray();

            // Ajouter les rôles sélectionnés au JSONArray
            if (selectedRoles != null && !selectedRoles.isEmpty()) {
                for (Role role : selectedRoles) {
                    JSONObject roleObject = new JSONObject();
                    roleObject.put("id", role.getId());
                    rolesArray.put(roleObject);
                }
            }

            // Ajouter les champs au JSONObject principal
            jsonBody.put("id", Integer.parseInt(id)); // Remplacez la valeur de l'ID par la valeur appropriée
            jsonBody.put("roles", rolesArray);
            jsonBody.put("username", userName.getText().toString());
            jsonBody.put("password", password.getText().toString());
            jsonBody.put("firstName", firstname.getText().toString());
            jsonBody.put("lastName", lastName.getText().toString());
            jsonBody.put("phone", Integer.parseInt(phone.getText().toString()));
            jsonBody.put("email", email.getText().toString());

            // Ajouter l'objet filière au JSONObject
            JSONObject filiereObject = new JSONObject();
            filiereObject.put("id", selectedFiliere.getId());
            filiereObject.put("code", selectedFiliere.getCode());
            filiereObject.put("libelle", selectedFiliere.getLibelle());
            jsonBody.put("filiere", filiereObject);

            // Afficher le JSONObject dans les logs pour vérification
            Log.d("student", jsonBody.toString());
            Toast.makeText(UpdateStudent.this, "Student updated", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdateStudent.this, StudentList.class);
            startActivity(intent);
            UpdateStudent.this.finish();

            // Envoyer le JSONObject à l'API ou effectuer d'autres opérations nécessaires ici

        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                insertUrl, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("resultat", response+"");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Erreur", error.toString());
            }
        });
        requestQueue.add(request);
    }
    private void setFiliereAndRoles() {
        // Remplissez la filière
        Spinner spinner = findViewById(R.id.spinner);
        if (student.getFiliere() != null) {
            int filiereIndex = getFiliereIndex(student.getFiliere().getId());
            if (filiereIndex != -1) {
                spinner.setSelection(filiereIndex);
            }
        }

        // Remplissez les rôles
        StringBuilder rolesStr = new StringBuilder();
        if (student.getRoles() != null && !student.getRoles().isEmpty()) {
            selectedRoles = student.getRoles();
            for (Role role : selectedRoles) {
                rolesStr.append(role.getName()).append("   ");
            }
            chooseRoles.setText(rolesStr.toString());
        }
    }

    // Obtenez l'indice de la filière dans la liste des filières en fonction de son ID
    private int getFiliereIndex(int filiereId) {
        for (int i = 0; i < filieres.size(); i++) {
            if (filieres.get(i).getId() == filiereId) {
                return i;
            }
        }
        return -1;
    }
    public void deleteStudent(int id) {
        String deleteUrl = "http://192.168.1.109:8080/api/v1/students/" + id;
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.DELETE, deleteUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        Toast.makeText(UpdateStudent.this, "Student deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateStudent.this, StudentList.class);
                        UpdateStudent.this.finish();
                        startActivity(intent);
                        UpdateStudent.this.finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Erreur", error.toString());
            }
        });

        requestQueue.add(request);
    }

}