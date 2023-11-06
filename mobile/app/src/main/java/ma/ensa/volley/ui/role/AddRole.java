package ma.ensa.volley.ui.role;

import androidx.appcompat.app.AppCompatActivity;

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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ma.ensa.volley.R;
import ma.ensa.volley.ui.filiere.AddFiliere;
import ma.ensa.volley.ui.filiere.FiliereList;

public class AddRole extends AppCompatActivity {

    RequestQueue requestQueue;
    private EditText name;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_role);
        name = findViewById(R.id.code);
        submit = findViewById(R.id.add);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRole();
            }
        });
    }
    public void submitRole() {
        String insertUrl = "http://192.168.1.109:8080/api/v1/roles";
        JSONObject jsonBody = new JSONObject();
        try {
            JSONArray rolesArray = new JSONArray();
            jsonBody.put("id", "");
            jsonBody.put("name", name.getText().toString());
            Log.d("student", jsonBody.toString());
            Toast.makeText(AddRole.this, "Role Added", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddRole.this, RoleList.class);
            AddRole.this.finish();
            startActivity(intent);
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