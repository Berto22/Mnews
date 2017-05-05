import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.displaymyjokes.DisplayMyJokesActivity;
import com.example.android.jokesonme.R;
import com.example.berto.jokesonme.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

/**
 * Created by berto on 5/2/2017.
 */

//public class MainActivity {
//}
class MyJokesAsyncTask extends AsyncTask<String, Void, String> {
    private MyApi myApiService = null;
    private Context context;


    MyJokesAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        //return null;
        if(myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    //.setRootUrl("http://192.168.1.69:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }

        try {
            return myApiService.getJoke().execute().getMMyJokes();
        } catch (IOException e) {
            return e.getMessage();

        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("message", "joke here" + s);

        Intent intent = new Intent(context, DisplayMyJokesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("display", s);
        context.startActivity(intent);
        /*if(s != null) {
            Intent intent = new Intent(context, DisplayMyJokesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("display", s);
            context.startActivity(intent);

        } else {
            Toast.makeText(context, "There are mo jokes", Toast.LENGTH_LONG).show();

        }*/

    }
}

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static String newJoke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544/6300978111");
        //"ca-app-pub-1822618669019557/2990058229"

        //View adContainer = findViewById(R.id.adView);

        /*AdView adView = (AdView) findViewById(R.id.adView);
        //AdView adView = new AdView(getApplicationContext());

        //adView.setAdSize(AdSize.BANNER);
        //adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest
                .DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);  */

        //"MD5:2fa0e16c8cceb7d385183284107c0c88"
        Toast.makeText(this, "This is the paid version", Toast.LENGTH_LONG).show();

        Button button = (Button) findViewById(R.id.display_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyJokesAsyncTask(getApplicationContext()).execute();

            }
        });


    }

    public void updateUi() {

    }


}
