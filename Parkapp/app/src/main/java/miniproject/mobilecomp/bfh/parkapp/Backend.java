package miniproject.mobilecomp.bfh.parkapp;


import android.app.Activity;
import android.app.Application;
import android.location.Location;
import android.util.Log;
import android.widget.ArrayAdapter;

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

import org.apache.http.message.BasicNameValuePair;

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

    private FrontViewController a;

    private  RequestToken token;
    private RequestToken mRefresh;
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

    public void getLocationsNear(Location location,FrontViewController a){

        this.a=a;


        BaasHandler handler = new BaasHandler<List<BaasDocument>>() {
            @Override
            public void handle(BaasResult<List<BaasDocument>> listBaasResult) {
                if(listBaasResult.isSuccess())
                    for(BaasDocument doc:listBaasResult.value()) {

                        Backend.this.a.addString(doc.getString("name"));
                        Backend.this.a.addString(doc.getString("latitude"));
                        Backend.this.a.addString(doc.getString("longitude"));
                        Log.d("latitude", doc.getString("latitude"), listBaasResult.error());

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



        token = mRefresh =BaasDocument.fetchAll("group-gerber",handler);



       /* client.rest(HttpRequest.GET,
                "distance(latitude,longitude,"+location.getLatitude()+"," +location.getLongitude()+" < 5000",
                new JsonArray(),
                true,
                handler
           );*/

    }

}
