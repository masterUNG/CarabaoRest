package appewtc.masterung.carabaorest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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


public class OrderActivity extends ActionBarActivity {

    //Explicit
    private TextView txtShowOfficer;
    private Spinner spnDesk;
    private ListView listMenu;
    private String strMyOfficer, strMyDesk,
            strMyFood, strMyItem, strSpinnerDesk[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //Bind Widget
        bindWidget();

        //Synchronize JSON to SQLite
        synJSONtoSQLite();

        //Show Officer
        showOfficer();

        //Create Spinner
        createSpinner();

        //Create ListView
        createListView();




    }// onCreate



    private void createListView() {

        FoodTABLE objFoodTABLE = new FoodTABLE(this);
        final String[] strFood = objFoodTABLE.readAllFood();
        String[] strPrice = objFoodTABLE.readAllPrice();

        int[] intImageFood = {R.drawable.food1, R.drawable.food2, R.drawable.food3,
                R.drawable.food4, R.drawable.food5, R.drawable.food6, R.drawable.food7,
                R.drawable.food8, R.drawable.food9, R.drawable.food10, R.drawable.food11,
                R.drawable.food12, R.drawable.food13, R.drawable.food14, R.drawable.food15,
                R.drawable.food16, R.drawable.food17, R.drawable.food18, R.drawable.food19,
                R.drawable.food20, R.drawable.food21, R.drawable.food22, R.drawable.food23,
                R.drawable.food24, R.drawable.food25, R.drawable.food26, R.drawable.food27,
                R.drawable.food28, R.drawable.food29, R.drawable.food30, R.drawable.food31,
                R.drawable.food32, R.drawable.food33, R.drawable.food34, R.drawable.food35,
                R.drawable.food36, R.drawable.food37, R.drawable.food38, R.drawable.food39,
                R.drawable.food40, R.drawable.food41, R.drawable.food42, R.drawable.food43,
                R.drawable.food44, R.drawable.food45, R.drawable.food46, R.drawable.food47,
                R.drawable.food48, R.drawable.food49, R.drawable.food50};

        MyAdapter objMyAdapter = new MyAdapter(OrderActivity.this, strFood, strPrice, intImageFood);
        listMenu.setAdapter(objMyAdapter);

        //Active Click
        listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                strMyFood = strFood[i];
                chooseItem();

            }
        });


    }   // createListView

    private void chooseItem() {

        CharSequence[] objSequences = {"1 set", "2 set", "3 set", "4 set", "5 set", "6 set", };
        AlertDialog.Builder objBuilder = new AlertDialog.Builder(this);
        objBuilder.setTitle("How many set ?");
        objBuilder.setSingleChoiceItems(objSequences, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                switch (i) {
                    case 0:
                        strMyItem = "1";
                        break;
                    case 1:
                        strMyItem = "2";
                        break;
                    case 2:
                        strMyItem = "3";
                        break;
                    case 3:
                        strMyItem = "4";
                        break;
                    case 4:
                        strMyItem = "5";
                        break;
                    case 5:
                        strMyItem = "6";
                        break;
                }

                dialogInterface.dismiss();

            }
        });
        objBuilder.show();


    }   // chooseItem

    private void createSpinner() {

        strSpinnerDesk = getResources().getStringArray(R.array.my_desk);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strSpinnerDesk);
        spnDesk.setAdapter(myAdapter);

        //Choose Spinner Desk
        spnDesk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strMyDesk = strSpinnerDesk[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                strMyDesk = strSpinnerDesk[0];
            }
        });

    }   // createSpiner

    private void showOfficer() {

        strMyOfficer = getIntent().getExtras().getString("Officer");
        txtShowOfficer.setText(strMyOfficer);

    }

    private void bindWidget() {
        txtShowOfficer = (TextView) findViewById(R.id.txtShowOfficer);
        spnDesk = (Spinner) findViewById(R.id.spnDesk);
        listMenu = (ListView) findViewById(R.id.listMenu);
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
            HttpPost objHttpPost = new HttpPost("http://swiftcodingthai.com/bao/get_data_food.php");
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

            while ((strLine = objBufferedReader.readLine()) != null) {

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

                String strFood = objJSONObject.getString("Food");
                String strPrice = objJSONObject.getString("Price");

                FoodTABLE objFoodTABLE = new FoodTABLE(this);
                objFoodTABLE.addFood(strFood, strPrice);

            }   // for

        } catch (Exception e) {
            Log.d("bao", "Error Update ==> " + e.toString());
        }


    }   // synJSONtoSQLite

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}   // Main Class
