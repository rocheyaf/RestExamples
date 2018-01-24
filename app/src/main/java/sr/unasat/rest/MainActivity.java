package sr.unasat.rest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sr.unasat.rest.dto.CountryDto;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.text);
        getCountryData();
    }

    private void getCountryData() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        final String ALL_COUNTRIES = "https://restcountries.eu/rest/v2/all";
      //  final String ONE_COUNTRY = "https://restcountries.eu/rest/v2/name/" + countryName;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ALL_COUNTRIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<CountryDto> countryDto = mapJsonToCountryObject(response);
                        System.out.println(countryDto);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("something went wrong");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    private List<CountryDto> mapJsonToCountryObject(String jsonArray) {
        ObjectMapper mapper = new ObjectMapper();
        List<CountryDto> countryList = new ArrayList<>();
        List<Map<String, ?>> countryArray = null;
        CountryDto country = null;

        try {
            countryArray = mapper.readValue(jsonArray, List.class);
            for (Map<String, ?> map : countryArray) {
                country = new CountryDto((String) map.get("name"), (String) map.get("capital"), (String) map.get("region"));
                countryList.add(country);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Er is wat fout gegaan bij het parsen van de json data");
        }
        return countryList;
    }

}
