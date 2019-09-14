package com.appsdevsa.threedp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.appsdevsa.threedp.models.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity {
    private Spinner model;
    List<String> mode_list;
    String model_value;
    private ProgressDialog mProgressDialog;

    ArrayList<DataModel> dataModels;
    ListView listView;
    private static CustomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        model = (Spinner) findViewById(R.id.spinner);

        mode_list = new ArrayList<String>();
        mode_list.add("Plane");
        mode_list.add("Car");

        listView = (ListView)findViewById(R.id.list);

        displayLoader();

        RetrofitClient.getAssessmentService()
                .getModelAssessment()
                .enqueue(new Callback<List<Model>>() {
                    @Override
                    public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                        mProgressDialog.dismiss();
                        if (response.code()==200){
                            List<Model> user = response.body();
                            if (user!=null){
                                //storeData(response.body());
                                Log.d("ASSSESS1", "onResponse:onSuccess "+user.size());
                                for (int i=0;i<user.size();i++){
                                    mode_list.add(user.get(i).getTitle());
                                    //Log.d("Formation: "+(i+1),""+user.get(i).getFormation());
                                    //Log.d("Title "+(i+1),""+user.get(i).getTitle());
                                }

                                //Toast.makeText(MenuActivity.this, ""+user, Toast.LENGTH_SHORT).show();

                                //Log.d("ASSSESS", "onResponse:onSuccess "+user.toString());
                            }
                        }else{
                            Log.d("ASSSESS3", "onResponse:onSuccess "+response.message());
                            //Toast.makeText(LoginActivity.this, "Your username or password is incorrect!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Model>> call, Throwable t) {
                        mProgressDialog.dismiss();
                        Log.d("ASSSESS", "onResponse:onFailure "+t.getMessage()+" hello");
                    }
                });

        //ListViewStart
        dataModels= new ArrayList<>();

        dataModels.add(new DataModel("Apple Pie", "Android 1.0", "1","September 23, 2008"));
        dataModels.add(new DataModel("Banana Bread", "Android 1.1", "2","February 9, 2009"));
        dataModels.add(new DataModel("Cupcake", "Android 1.5", "3","April 27, 2009"));
        dataModels.add(new DataModel("Donut","Android 1.6","4","September 15, 2009"));
        dataModels.add(new DataModel("Eclair", "Android 2.0", "5","October 26, 2009"));
        dataModels.add(new DataModel("Froyo", "Android 2.2", "8","May 20, 2010"));
        dataModels.add(new DataModel("Gingerbread", "Android 2.3", "9","December 6, 2010"));
        dataModels.add(new DataModel("Honeycomb","Android 3.0","11","February 22, 2011"));
        dataModels.add(new DataModel("Ice Cream Sandwich", "Android 4.0", "14","October 18, 2011"));
        dataModels.add(new DataModel("Jelly Bean", "Android 4.2", "16","July 9, 2012"));
        dataModels.add(new DataModel("Kitkat", "Android 4.4", "19","October 31, 2013"));
        dataModels.add(new DataModel("Lollipop","Android 5.0","21","November 12, 2014"));
        dataModels.add(new DataModel("Marshmallow", "Android 6.0", "23","October 5, 2015"));

        adapter= new CustomAdapter(dataModels,getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DataModel dataModel= dataModels.get(position);

                Toast.makeText(MenuActivity.this, ""+dataModel.getName()+"\n"+dataModel.getType()+" API: "+dataModel.getVersion_number(), Toast.LENGTH_SHORT).show();
                //Snackbar.make(view, , Snackbar.LENGTH_LONG).setAction("No action", null).show();
            }
        });
        //ListViewEnd


        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mode_list);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        model.setAdapter(dataAdapter1);

        model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                model_value =  parent.getItemAtPosition(position).toString();

                Toast.makeText(MenuActivity.this, ""+model_value, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void displayLoader() {
        mProgressDialog = new ProgressDialog(MenuActivity.this);
        mProgressDialog.setMessage(getString(R.string.fetching));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

    }

    public void goToPrompt(View view) {
        if (model_value.equals("3D Model")) {
            Toast.makeText(this, "Please select a model", Toast.LENGTH_SHORT).show();
        }else {
            String value2 = "";
            switch (model_value){
                case "Plane":
                    value2 = "ship.obj";
                    break;
                case "Car":
                    value2 = "model.obj";
                    break;
            }

            Intent i = new Intent(getApplicationContext(),AndroidLauncher.class);
            i.putExtra("modelv",value2);
            startActivity(i);
            finish();
        }

    }
}
