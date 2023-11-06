package ma.ensa.volley.ui.student;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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

public class AddStudent extends AppCompatActivity {
    RequestQueue requestQueue;
    private List<Filiere> filieres = new ArrayList<>();
    private List<Role> roles = new ArrayList<>();
    private Filiere selectedFiliere;
    private List<Role> selectedRoles;
    private boolean[] checkedRoles;
    private EditText firstname, lastName, userName, email, password, phone;
    private TextView chooseRoles;
    private Button submit;
    private String rolesStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        getFilieres();
        getRoles();
        firstname = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        userName = findViewById(R.id.userName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        submit = findViewById(R.id.add);
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

                // Créer un HashMap pour associer les noms des filières à leurs objets correspondants
                final HashMap<String, Filiere> filiereMap = new HashMap<>();
                for (Filiere filiere : filieres) {
                    filiereMap.put(filiere.getLibelle(), filiere);
                }


                List<String> nomFilieres = new ArrayList<>(filiereMap.keySet());


                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddStudent.this, android.R.layout.simple_spinner_item, nomFilieres);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Spinner spinner = findViewById(R.id.spinner);
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                        String selectedFiliereNom = (String) parentView.getItemAtPosition(position);


                        selectedFiliere = filiereMap.get(selectedFiliereNom);


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {

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


    private void showRolesDialog() {
        if (roles.isEmpty()) {
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


            }
        });

        builder.setNegativeButton("Annuler", null);


        builder.show();
    }

    public void submitStudent() {
        String insertUrl = "http://192.168.1.109:8080/api/v1/students";
        JSONObject jsonBody = new JSONObject();
        try {

            JSONArray rolesArray = new JSONArray();


            if (selectedRoles != null && !selectedRoles.isEmpty()) {
                for (Role role : selectedRoles) {
                    JSONObject roleObject = new JSONObject();
                    roleObject.put("id", role.getId());
                    rolesArray.put(roleObject);
                }
            }


            jsonBody.put("id", "");
            jsonBody.put("roles", rolesArray);
            jsonBody.put("username", userName.getText().toString());
            jsonBody.put("password", password.getText().toString());
            jsonBody.put("firstName", firstname.getText().toString());
            jsonBody.put("lastName", lastName.getText().toString());
            jsonBody.put("phone", Integer.parseInt(phone.getText().toString()));
            jsonBody.put("email", email.getText().toString());
            JSONObject filiereObject = new JSONObject();
            filiereObject.put("id", selectedFiliere.getId());
            filiereObject.put("code", selectedFiliere.getCode());
            filiereObject.put("libelle", selectedFiliere.getLibelle());
            jsonBody.put("filiere", filiereObject);
            Log.d("student", jsonBody.toString());
            Toast.makeText(AddStudent.this, "Student Added", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddStudent.this, StudentList.class);
            startActivity(intent);
            AddStudent.this.finish();
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

}
