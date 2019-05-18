package miprimerproyecto.co.retodeezer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
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

public class PlaylistActivity extends AppCompatActivity implements AdapterTracks.OnItemClickListener {

    /** The list of tracks of displayed by this activity. */
    private List<Track> mTrackList = new ArrayList<Track>();

    private RecyclerView rv_tracks;
    private AdapterTracks adapterTracks;

    private DeezerConnect deezerConnect;
    private  Long prueba_id_playlist=3110421322L;
    private  String applicationID = "348444";

    private ImageView img_playlist_big;

    private TextView tv_playlist_name;
    private TextView tv_playlist_description;
    private TextView tv_playlist_num_tracks;
    private TextView tv_playlist_num_fans;

    private Long  playlist_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        playlist_send = getIntent().getExtras().getLong("playlist");


        img_playlist_big =findViewById(R.id.img_playlist_big);
        tv_playlist_description=findViewById(R.id.tv_playlist_description);
        tv_playlist_num_tracks=findViewById(R.id.tv_playlist_num_tracks);
        tv_playlist_num_fans=findViewById(R.id.tv_playlist_num_fans);
        tv_playlist_name=findViewById(R.id.tv_playlist_name);

        rv_tracks = findViewById(R.id.rv_tracks);
        rv_tracks.setLayoutManager(new LinearLayoutManager(this));
        adapterTracks = new AdapterTracks();
        adapterTracks.setListener(this);
        rv_tracks.setAdapter(adapterTracks);
        rv_tracks.setHasFixedSize(true);

        deezerConnect = new DeezerConnect(this, applicationID);
        //DeezerConnect deezerConnect = DeezerConnect.forApp(applicationID). withContext(this). build();


        showPlaylistElements(playlist_send);

    }



    public void  showPlaylistElements(Long playlistName){
        DeezerRequest request= DeezerRequestFactory.requestPlaylist(playlistName);
        AsyncDeezerTask task = new AsyncDeezerTask(deezerConnect,
                new JsonRequestListener() {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onResult(Object result, Object requestId) {

                        mTrackList.clear();

                        try {
                            mTrackList.addAll((List<Track>) ((Playlist) result).getTracks());
                        }
                        catch (ClassCastException e) {
                            Log.e(">>>",""+e.getMessage());
                        }

                        if (mTrackList.isEmpty()) {
                            Log.e(">>>","la lista esta vacia");

                        }
                        adapterTracks.showAllTracks((ArrayList<Track>) mTrackList);
                        //cambio la imagen grande

                        tv_playlist_name.setText(((Playlist) result).getTitle());
                        tv_playlist_description.setText(((Playlist) result).getDescription());
                        tv_playlist_num_tracks.setText(((Playlist) result).getTracks().size()+" songs");
                        tv_playlist_num_fans.setText(((Playlist) result).getFans()+" fans");

                        Glide.with(getApplicationContext()).load(((Playlist) result).getBigImageUrl()).into(img_playlist_big);


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


    @Override
    public void onItemClick(Track track) {
        Intent i = new Intent(this, TrackActivity.class);
        i.putExtra("track", track.getId());
        startActivity(i);
    }
}
