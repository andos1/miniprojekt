package miniproject.mobilecomp.bfh.parkapp;


import android.app.Activity;
import android.app.Application;
import android.location.Location;
import android.util.Log;

import com.baasbox.android.BaasBox;
import com.baasbox.android.BaasDocument;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasQuery;
import com.baasbox.android.BaasResult;
import com.baasbox.android.BaasUser;
import com.baasbox.android.RequestToken;
import com.baasbox.android.json.JsonArray;
import com.baasbox.android.json.JsonObject;
import com.baasbox.android.net.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stefan on 22.05.15.
 */
public class Backend extends Application {

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
                    else{
                        Log.d("WIN", "WIN", result.error());
                    }
                }
            };

    public List<Location> getLocationsNear(Location location,Activity a){

        final List<Location> locations = new ArrayList<Location>();


        BaasHandler handler = new BaasHandler<List<BaasDocument>>() {
            @Override
            public void handle(BaasResult<List<BaasDocument>> listBaasResult) {
                if(listBaasResult.isSuccess())
                    for(BaasDocument doc:listBaasResult.value()) {

                        JsonObject jo = doc.getObject(Location.class.getName());
                        Location l = new Location("baaslocation");
                        l.setLatitude(jo.getDouble("latitude"));
                        l.setLongitude(jo.getDouble("longitude"));
                        l.setLongitude(jo.getDouble("name"));
                        locations.add(l);
                        Log.d("WIN", "FOOBAR", listBaasResult.error());
                    }
                else
                    Log.d("ERROR", "FOOBAR", listBaasResult.error());

            }
        };


        BaasBox.Builder b = new BaasBox.Builder(a);
        client = b.setApiDomain(domain)
                .setAppCode(appcode)
                .setAuthentication(BaasBox.Config.AuthType.SESSION_TOKEN)
                .setPort(9000)
                .init();

        login();



        client.rest(HttpRequest.GET,
                "distance(latitude,longitude,"+location.getLatitude()+"," +location.getLongitude()+" < 5000",
                new JsonArray(),
                true,
                handler
           );

        return locations;
    }

}
