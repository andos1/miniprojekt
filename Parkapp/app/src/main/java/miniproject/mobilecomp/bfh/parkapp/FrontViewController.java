package miniproject.mobilecomp.bfh.parkapp;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stefan on 01.05.15.
 */
public class FrontViewController extends Activity {

    private TextView currentLocationTv;
    private ListView locationList;
    private List<String> strList=new ArrayList<>();
    private ArrayAdapter<String> adapter;
    @Override
    protected void onStart(){
        super.onStart();
        adapter= new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,strList);
        Location location = getCurrentLocation();
        List<String> locations;

        //load xml layout file
        setContentView(R.layout.frontview);

        currentLocationTv=(TextView)findViewById(R.id.currentLocation);

        currentLocationTv.append("\n"+location.toString());

        locationList=(ListView)findViewById(R.id.locationlist);



        locationList.setAdapter(adapter);

        Backend back= new Backend();
        back.getLocationsNear(location,this);



    }

    public Location getCurrentLocation(){
        LocationManager lManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        String foo = lManager.getBestProvider(criteria,true);

        Location location = null;

        if(foo!=null) {
            location = lManager.getLastKnownLocation(foo);
        }

        return location;
    }

    public void addString(String s){


        strList.add(s);
        adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,strList);
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onResume(){
        super.onResume();
        this.onStart();
    }
 }
