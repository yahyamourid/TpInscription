package ma.ensa.volley.ui.filiere;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ma.ensa.volley.R;
import ma.ensa.volley.beans.Filiere;
import ma.ensa.volley.beans.Student;
import ma.ensa.volley.ui.student.StudentList;
import ma.ensa.volley.ui.student.UpdateStudent;

public class UpdateFiliere extends AppCompatActivity {

    RequestQueue requestQueue;
    private EditText code, libelle;
    private Button submit, delete;
    private String id ;
    private Filiere filiere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_filiere);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        code = findViewById(R.id.code);
        libelle = findViewById(R.id.libelle);
        getFiliere(id);
        submit = findViewById(R.id.add);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFiliere();
            }
        });
        delete =findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateFiliere.this);
                builder.setTitle("Confirmation ");
                builder.setMessage("Do you want to delete this major ?");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // L'utilisateur a confirmé la suppression, appelez la méthode deleteStudent
                        deleteFiliere(id);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // L'utilisateur a annulé la suppression, fermez la boîte de dialogue
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }
    public void getFiliere(String id){
        String getUrl = "http://192.168.1.109:8080/api/v1/filieres/"+id;
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                getUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                TypeToken<Filiere> token = new TypeToken<Filiere>() {};
                filiere = gson.fromJson(response.toString(), token.getType());
                code.setText(filiere.getCode().toString());
                libelle.setText(filiere.getLibelle().toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Erreur", error.toString());
            }
        });
        requestQueue.add(request);
    }
    public void submitFiliere() {
        String insertUrl = "http://192.168.1.109:8080/api/v1/filieres/"+id;
        JSONObject jsonBody = new JSONObject();
        try {
            JSONArray rolesArray = new JSONArray();
            jsonBody.put("id", Integer.parseInt(id));
            jsonBody.put("code", code.getText().toString());
            jsonBody.put("libelle", libelle.getText().toString());
            Log.d("filiereUpdated", jsonBody.toString());
            Toast.makeText(UpdateFiliere.this, "Major updated", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdateFiliere.this, FiliereList.class);
            UpdateFiliere.this.finish();
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT,
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
    public void deleteFiliere(String id) {
        String deleteUrl = "http://192.168.1.109:8080/api/v1/filieres/" + id;
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.DELETE, deleteUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        Toast.makeText(UpdateFiliere.this, "Major deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateFiliere.this, FiliereList.class);
                        UpdateFiliere.this.finish();
                        startActivity(intent);
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