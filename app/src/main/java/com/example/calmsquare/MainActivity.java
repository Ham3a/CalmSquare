package com.example.calmsquare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.service.autofill.OnClickAction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calmsquare.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    SearchView mySearchView;

    ArrayList<String> list;

    ArrayAdapter<String> adapter;

    ListView myList;
    String[] name = {"Nearest Hospitals","Helplines", "Settings", "Diary", "Home"};
    ArrayAdapter<String>arrayAdapter;
    BottomNavigationView bottomNavigationView;
    private FirebaseAuth firebaseAuth;

    //private RecyclerViewAdaptery adapterr;
   // private StaggeredGridLayoutManager manager;
    //private List<row> appList;

    //array of images
    //int[] covers = new int[]{
            //R.drawable.fash1,
            //R.drawable.fash2,
           // R.drawable.fash3,
           // R.drawable.fash4,
            //R.drawable.fash5,
           // R.drawable.fash6,


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        myList = findViewById(R.id.myList);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser =firebaseAuth.getCurrentUser();

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new FirstFragment()).commit();




        bottomNavigationView = findViewById(R.id.bottomnavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.firstFragment:
                        fragment = new FirstFragment();
                        break;
                    case R.id.secondFragment:
                        fragment = new SecondFragment();
                        break;
                    case R.id.thirdFragment:
                        fragment = new thirdFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
                return true;
            }
        });




        //Clear items
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator
                (R.drawable.clear);






    }

    //private void InitializeDataIntoRecyclerView() {

        //row a =new row(covers[0]);
        //appList.add(a);
        //a =new row(covers[1]);
        //appList.add(a);
       // a =new row(covers[2]);
       // appList.add(a);
        //a =new row(covers[3]);
        //appList.add(a);
        //a =new row(covers[4]);
        //appList.add(a);
        //a =new row(covers[5]);
        //appList.add(a);

        //adapterr.notifyDataSetChanged();





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,name);
        myList.setAdapter(arrayAdapter);


        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                arrayAdapter.getFilter().filter(newText);

                return false;
            }

        });

        return super.onCreateOptionsMenu(menu);


    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        myList.setVisibility(View.VISIBLE);

        if (id == R.id.action_search){

        }else {

            myList.setVisibility(View.INVISIBLE);

        }
        return super.onOptionsItemSelected(item);


    }


    public void logout (View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }



}
