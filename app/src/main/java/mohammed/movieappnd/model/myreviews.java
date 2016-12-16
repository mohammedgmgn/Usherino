
package mohammed.movieappnd.model;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mohammed.movieappnd.R;
import mohammed.movieappnd.volleysingletone.ApplicationController;

public class myreviews extends AppCompatActivity {
    private TextView mytxt;
    ArrayList<String> revi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myreviews);
        mytxt = (TextView) findViewById(R.id.mytxt);
        setTitle("Reviews");

        String id = getIntent().getExtras().getString("ID");

        Uri build = Uri.parse("https://api.themoviedb.org/3/movie").buildUpon()
                .appendPath(id).appendPath("reviews").appendQueryParameter("api_key", "69f8d44407d7b73a4103add4c76fccb6").build();

        sendJsonRequest(build.toString());

    }

    public void sendJsonRequest(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                revi = new ArrayList<>();
                try {
                    JSONArray array = jsonObject.getJSONArray("results");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);

                        revi.add("written by: " + object.getString("author") + "\n" + object.getString("content"));
                    }
                    StringBuilder mybuilder = new StringBuilder();
                    for (int i = 0; i < revi.size(); i++) {

                        mybuilder.append(revi.get(i) + "\n\n--------------------------------------\n\n");
                    }
                    String res = mybuilder.toString();
                    mytxt.setText(res);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        ApplicationController.getInstance().addToRequestQueue(request);

    }

}