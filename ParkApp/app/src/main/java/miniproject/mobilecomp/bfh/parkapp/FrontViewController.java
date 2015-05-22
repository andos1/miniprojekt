package miniproject.mobilecomp.bfh.parkapp;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by stefan on 01.05.15.
 */
public class FrontViewController extends Activity{

    private TextView currentLocationTv;
    private ListView locationList;

    @Override
    protected void onStart(){
        super.onStart();

        Location location = getCurrentLocation();

        //load xml layout file
        setContentView(R.layout.frontview);

        currentLocationTv=(TextView)findViewById(R.id.currentLocationtv);

        currentLocationTv.append("\n"+location.toString());

        locationList=(ListView)findViewById(R.id.lolcationlist);


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
 }
