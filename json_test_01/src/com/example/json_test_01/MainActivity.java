/*
 * Leo un Simple JSON desde una Variable.
 * https://abhiandroid.com/programming/json
 */
package com.example.json_test_01;

import android.app.Activity;
import android.os.Bundle;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {
    
    // Ej de un astructura JSON:   {"employees": { "name": "JuanCarlos", "Salario": 65000 }}
    String JSON_STRING = "{\"employee\":{\"name\":\"Abhishek Saini\",\"salary\":65000}}";
    String name, salary;
    TextView employeeName, employeeSalary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     // get the reference of TextView's
        employeeName = (TextView) findViewById(R.id.name);
        employeeSalary = (TextView) findViewById(R.id.salary);

        try {
            JSONObject obj = new JSONObject(JSON_STRING);							// Le de un JSON

            if (obj.has("employe")){																		// verifico si exite este key or tag de los elementps del JSON file-
            	JSONObject objEmployee = obj.getJSONObject("employe");				// leo el Tag = "employee"
	            name = objEmployee.getString("name");											// le el SubTag = "name" de "employee"
	            salary = objEmployee.getString("salary");
	            
	            // Displayo los valores de C/Tag
	            employeeName.setText("Name: "+name);
	            employeeSalary.setText("Salary: "+salary);
            }else {
            	Log.i("ERROR :", "NO existe esta KEY");
            }

        } catch (JSONException e) {
        	// org.json.JSONException: No value for employe

            e.printStackTrace();
            Log.i("ERROR :", "NO existe esta KEY  e=" + e.toString());
        }
        

    }


}

/*
 * Reemplazo el  comillas dobles "  por  balcslash y comillas dobles  \"

"{
  \"users\": [
    {
        \"id\": \"0007\",
        \"name\": \"Juan Carlos Dergan Febres\",
        \"email\": \"jcdergan@aol.com\",
        \"gender\": \"male\",
        \"contact\": {
          \"mobile\": \"+51915189781\",
          \"home\": \"00 000000\",
          \"office\": \"00 000000\"
        }
    },
    {
      \"id\": \"1087\",
      \"name\": \"Abhishek Saini\",
      \"email\": \"saini.abhishek@gmail.com\",
      \"gender\": \"male\",
      \"contact\": {
        \"mobile\": \"+91 0000000000\",
        \"home\": \"00 000000\",
        \"office\": \"00 000000\"
      }
    },
    {
      \"id\": \"1088\",
      \"name\": \"Gourav\",
      \"email\": \"gourav9188@gmail.com\",
      \"gender\": \"male\",
      \"contact\": {
        \"mobile\": \"+91 0000000000\",
        \"home\": \"00 000000\",
        \"office\": \"00 000000\"
      }
    },
    {
      \"id\": \"1089\",
      \"name\": \"Akshay\",
      \"email\": \"akshay@gmail.com\",
      \"gender\": \"male\",
      \"contact\": {
        \"mobile\": \"+91 0000000000\",
        \"home\": \"00 000000\",
        \"office\": \"00 000000\"
      }
    }
  ]
}"


*/
