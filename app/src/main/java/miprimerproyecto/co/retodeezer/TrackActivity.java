package miprimerproyecto.co.retodeezer;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.deezer.sdk.model.Playlist;
import com.deezer.sdk.model.Track;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.request.AsyncDeezerTask;
import com.deezer.sdk.network.request.DeezerRequest;
import com.deezer.sdk.network.request.DeezerRequestFactory;
import com.deezer.sdk.network.request.event.JsonRequestListener;

import java.util.ArrayList;
import java.util.List;

public class TrackActivity extends AppCompatActivity {

    private ImageView img_track;
    private TextView tv_name_track;
    private TextView tv_artist_track;
    private TextView tv_album_track;
    private TextView tv_duration_track;
    private Button btn_listen_track;


    private DeezerConnect deezerConnect;
    private  Long track_id_send;
    private  String applicationID = "348444";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        track_id_send=getIntent().getExtras().getLong("track");



        img_track =findViewById(R.id.img_track);
        tv_name_track=findViewById(R.id.tv_name_track);
        tv_artist_track=findViewById(R.id.tv_artist_track);
        tv_album_track=findViewById(R.id.tv_album_track);
        tv_duration_track=findViewById(R.id.tv_duration_track);
        btn_listen_track=findViewById(R.id.btn_listen_track);


        deezerConnect = new DeezerConnect(this, applicationID);
        //DeezerConnect deezerConnect = DeezerConnect.forApp(applicationID). withContext(this). build();

        DeezerRequest request= DeezerRequestFactory.requestTrack(track_id_send);
        AsyncDeezerTask task = new AsyncDeezerTask(deezerConnect,
                new JsonRequestListener() {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onResult(Object result, Object requestId) {

                        tv_name_track.setText(((Track) result).getShortTitle());
                        tv_artist_track.setText(((Track) result).getArtist().getName());
                        tv_album_track.setText(((Track) result).getAlbum().getTitle());
                        tv_duration_track.setText(((Track) result).getDuration()+"");

                        Glide.with(getApplicationContext()).load(((Track) result).getAlbum().getMediumImageUrl()).into(img_track);


                    }

                    @Override
                    public void onUnparsedResult(final String response, final Object requestId) {
                        Log.e(">>>","unparse error");

                    }

                    @Override
                    public void onException(final Exception exception,
                                            final Object requestId) {
                        Log.e(">>>","exception "+exception.getMessage());
                    }

                });
        task.execute(request);

    }
}
