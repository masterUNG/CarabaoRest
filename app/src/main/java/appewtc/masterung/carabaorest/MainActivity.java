package appewtc.masterung.carabaorest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends ActionBarActivity {

    //Explicit
    private UserTABLE objUserTABLE;
    private FoodTABLE objFoodTABLE;
    private EditText edtUser, edtPassword;
    private String strUserChoose, strPasswordChoose, strPasswordTrue, strOfficer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initial Widget
        initialWidget();

        //Connected Database
        objUserTABLE = new UserTABLE(this);
        objFoodTABLE = new FoodTABLE(this);

        //Test Add Data
       // testAddData();

        // Delete All Data
        deleteAllData();

        //Synchronize JSON to SQLite
        synJSONtoSQLite();

    }   // onCreate

    public void clickLogin(View view) {

        strUserChoose = edtUser.getText().toString().trim();
        strPasswordChoose = edtPassword.getText().toString().trim();

        if (strUserChoose.equals("") || strPasswordChoose.equals("") ) {

            //Call Alert
            MyAlertDialog objMyAlertDialog = new MyAlertDialog();
            objMyAlertDialog.dialogError(MainActivity.this, "มีช่องว่าง", "Please Fill in Every Blank");

        } else {

            checkUser();

        }

    }   // clickLogin

    private void checkUser() {
        try {

            String strMyResult[] = objUserTABLE.searchUser(strUserChoose);
            strPasswordTrue = strMyResult[2];
            strOfficer = strMyResult[3];

            Log.d("bao", "Wellcom == " + strOfficer);

            checkPassword();

        } catch (Exception e) {
            MyAlertDialog objMyAlertDialog = new MyAlertDialog();
            objMyAlertDialog.dialogError(MainActivity.this, "No This User", "No " + strUserChoose + " in my Database");
        }
    }

    private void checkPassword() {

        if (strPasswordChoose.equals(strPasswordTrue)) {
            welcomeOfficer();
        } else {
            MyAlertDialog objMyAlertDialog = new MyAlertDialog();
            objMyAlertDialog.dialogError(MainActivity.this, "Password False", "Please Type Again Password False");
        }

    }

    private void welcomeOfficer() {

        AlertDialog.Builder objBuilder = new AlertDialog.Builder(this);
        objBuilder.setIcon(R.drawable.restaurant);
        objBuilder.setTitle("Welcome Officer");
        objBuilder.setMessage("Welcome " + strOfficer + "\n" + "To Our Carabao Restaurant");
        objBuilder.setCancelable(false);
        objBuilder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                edtUser.setText("");
                edtPassword.setText("");
                dialogInterface.dismiss();
            }
        });
        objBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //Intent to OrderActivity
                Intent objIntent = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(objIntent);
                finish();
            }
        });
        objBuilder.show();
    }


    private void initialWidget() {
        edtUser = (EditText) findViewById(R.id.editText);
        edtPassword = (EditText) findViewById(R.id.editText2);
    }

    private void deleteAllData() {

        SQLiteDatabase objDatabase = openOrCreateDatabase("Carabao.db", MODE_PRIVATE, null);
        objDatabase.delete("userTABLE", null, null);
        objDatabase.delete("foodTABLE", null, null);

    }

    private void synJSONtoSQLite() {

        //Setup Policy
        if (Build.VERSION.SDK_INT > 9) {

            StrictMode.ThreadPolicy objPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(objPolicy);
        }

        //Create InputStream
        InputStream objInputStream = null;
        String strJSON = "";

        try {

            HttpClient objHttpClient = new DefaultHttpClient();
            HttpPost objHttpPost = new HttpPost("http://swiftcodingthai.com/bao/get_data_master.php");
            HttpResponse objHttpResponse = objHttpClient.execute(objHttpPost);
            HttpEntity objHttpEntity = objHttpResponse.getEntity();
            objInputStream = objHttpEntity.getContent();

        } catch (Exception e) {
            Log.d("bao", "Create InputStream ==> " + e.toString());
        }


        //Create strJSON
        try {

            BufferedReader objBufferedReader = new BufferedReader(new InputStreamReader(objInputStream, "UTF-8"));
            StringBuilder objStringBuilder = new StringBuilder();
            String strLine = null;

            while ((strLine = objBufferedReader.readLine()) != null ) {

                objStringBuilder.append(strLine);

            }

            objInputStream.close();
            strJSON = objStringBuilder.toString();

        } catch (Exception e) {
            Log.d("bao", "Error strJSON ==> " + e.toString());
        }


        //Update to SQLite
        try {

            final JSONArray objJsonArray = new JSONArray(strJSON);

            for (int i = 0; i < objJsonArray.length(); i++) {

                JSONObject objJSONObject = objJsonArray.getJSONObject(i);
                String strUser = objJSONObject.getString("User");
                String strPassword = objJSONObject.getString("Password");
                String strOffier = objJSONObject.getString("Officer");

                objUserTABLE.addNewValue(strUser, strPassword, strOffier);

            }   // for

        } catch (Exception e) {
            Log.d("bao", "Error Update ==> " + e.toString());
        }



    }   //sybJSONtoSQLite

    private void testAddData() {
        objUserTABLE.addNewValue("testUser", "testPass", "testOfficer");
        objFoodTABLE.addFood("testFood", "50");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.itmeSyn:
                deleteAllData();
                synJSONtoSQLite();
                Toast.makeText(getApplicationContext(), "โหลดข้อมูลใหม่แล้ว คะ", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}   // Main Class
