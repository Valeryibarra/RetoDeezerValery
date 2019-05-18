package miprimerproyecto.co.retodeezer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.connect.event.DialogListener;
import com.deezer.sdk.player.AlbumPlayer;
import com.deezer.sdk.player.networkcheck.NetworkStateCheckerFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // replace with your own Application ID
        String applicationID = "348444";
        DeezerConnect deezerConnect = new DeezerConnect(this, applicationID);

        //DeezerConnect deezerConnect = DeezerConnect.forApp(applicationID). withContext(this). build();


    }


}
