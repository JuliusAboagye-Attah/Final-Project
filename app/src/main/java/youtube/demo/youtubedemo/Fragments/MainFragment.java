package youtube.demo.youtubedemo.Fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.provider.Settings.Secure;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;

import youtube.demo.youtubedemo.MainActivity;
import youtube.demo.youtubedemo.R;




public class MainFragment extends Fragment {
    private Button sendBtn;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    private TrackGPS gps;
    double longitude;
    double latitude;
    String   imeistring = null;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //EditText fname = (EditText) view.findViewById(R.id.fname);
        sendBtn = (Button)view.findViewById(R.id.sendBtn);
        imeistring = Secure.getString(getActivity().getApplicationContext().getApplicationContext().getContentResolver(),Secure.ANDROID_ID);

        gps = new TrackGPS(MainFragment.this.getContext());


        if(gps.canGetLocation()){


            longitude = gps.getLongitude();
            latitude = gps .getLatitude();

            Toast.makeText(MainFragment.this.getContext(),"Longitude:"+Double.toString(longitude)+"\nLatitude:"+Double.toString(latitude)+"\nDevice Id:"+imeistring,Toast.LENGTH_LONG).show();
        }
        else
        {

            gps.showSettingsAlert();
        }
        //Start from Here
        progressDialog = new ProgressDialog(this.getContext());

        requestQueue = Volley.newRequestQueue(this.getContext());
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("Adding First Name...");
                progressDialog.show();

                try {
                    StringRequest request = new StringRequest(Request.Method.POST,
                            Utils.SEND_GEO_DATA, new Response.Listener<String>() {//This should have the link to the Database
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String Response = jsonObject.getString("response");
                                Toast.makeText(MainFragment.this.getContext(), Response, Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                        @Override //This should have the records being posted to your database
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parameters = new HashMap<>();
                            parameters.put("user_id", imeistring);
                            parameters.put("geo_loc", String.valueOf(latitude)+" "+String.valueOf(longitude));
                            return parameters;
                        }

                    };
                    requestQueue.add(request);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
                Intent intent = new Intent(MainFragment.this.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });//This place ends the codes





    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_main,container,false);
        return myInflatedView;

    }



}
