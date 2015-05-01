package miniproject.mobilecomp.bfh.parkapp;

import android.app.Activity;
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

        //load xml layout file
        setContentView(R.layout.frontview);

        currentLocationTv=(TextView)findViewById(R.id.currentLocationtv);
        locationList=(ListView)findViewById(R.id.lolcationlist);

        

    }


}
