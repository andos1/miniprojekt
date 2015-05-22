package miniproject.mobilecomp.bfh.parkapp;


import android.app.Activity;
import android.app.Application;
import android.app.DownloadManager;
import android.location.Location;
import android.util.Log;

import com.baasbox.android.BaasBox;
import com.baasbox.android.BaasDocument;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasQuery;
import com.baasbox.android.BaasResult;
import com.baasbox.android.BaasUser;
import com.baasbox.android.RequestToken;
import com.baasbox.android.json.JsonObject;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by stefan on 22.05.15.
 */
public class Backend extends Application{

    private BaasBox client;
    private String domain = "vmbackend.bfh.ch",
        appcode = "2501",
        username = "admin",
        pw = "mobile15";

   private  RequestToken token;

    private void login(){


        BaasUser user= BaasUser.withUserName(username);
        user.setPassword(pw);
        token = user.login(onComplete);


    }

    private final BaasHandler<BaasUser> onComplete =
            new BaasHandler<BaasUser>() {
                @Override
                public void handle(BaasResult<BaasUser> result) {
                    token = null;
                    if (result.isFailed()) {
                        Log.d("ERROR", "ERROR", result.error());
                    }
                }
            };


    public List<Location> getLocationsNear(Location location,Activity a){

        BaasBox.Builder b = new BaasBox.Builder(a);
        client = b.setApiDomain(domain).setAppCode(appcode).init();
        login();

        final List<Location> locations = new ArrayList<Location>();

        BaasQuery.Criteria filter = BaasQuery.builder()
                .pagination(0,20)
                .where("distance(latitude,longitude,41.872389,12.480180) < 5000")
                .criteria();

        BaasDocument.fetchAll("collection",filter,new BaasHandler<List<BaasDocument>>() {
            @Override
            public void handle(BaasResult<List<BaasDocument>> listBaasResult) {
                if(listBaasResult.isSuccess())
                    for(BaasDocument doc:listBaasResult.value()) {

                        JsonObject jo = doc.getObject(Location.class.getName());
                        Location l = new Location("baaslocation");
                        l.setLatitude(jo.getDouble("latitude"));
                        l.setLongitude(jo.getDouble("longitude"));
                        locations.add(l);
                    }
            }
        });

        return locations;
    }

}
