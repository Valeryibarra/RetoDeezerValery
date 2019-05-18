package miprimerproyecto.co.retodeezer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.deezer.sdk.model.Album;
import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.model.PlayableEntity;
import com.deezer.sdk.model.Playlist;
import com.deezer.sdk.model.Track;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.connect.event.DialogListener;
import com.deezer.sdk.network.request.AsyncDeezerTask;
import com.deezer.sdk.network.request.DeezerRequest;

import com.deezer.sdk.network.request.DeezerRequestFactory;
import com.deezer.sdk.network.request.event.DeezerError;
import com.deezer.sdk.network.request.event.JsonRequestListener;
import com.deezer.sdk.network.request.event.RequestListener;
import com.deezer.sdk.player.AlbumPlayer;
import com.deezer.sdk.player.networkcheck.NetworkStateCheckerFactory;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterPlaylist.OnItemClickListener {

    /** The list of playlist of displayed by this activity. */
    private List<Playlist> mPlaylistList = new ArrayList<Playlist>();

    private SearchView sv_playlist;
    private Button btn_search_playlist;

    private RecyclerView rv_playlist;
    private  AdapterPlaylist adapterPlaylist;

    private String playlist_default="rihanna";
    private DeezerConnect deezerConnect;

    private  String applicationID = "348444";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn_search_playlist= findViewById(R.id.btn_search_playlist);
        sv_playlist =findViewById(R.id.sv_playlist);
        rv_playlist = findViewById(R.id.rv_playlist);
        rv_playlist.setLayoutManager(new LinearLayoutManager(this));
        adapterPlaylist = new AdapterPlaylist();
        adapterPlaylist.setListener(this);
        rv_playlist.setAdapter(adapterPlaylist);
        rv_playlist.setHasFixedSize(true);



        deezerConnect = new DeezerConnect(this, applicationID);
        //DeezerConnect deezerConnect = DeezerConnect.forApp(applicationID). withContext(this). build();

        //le pongo por default la playlist de rihanna
        searchPlaylist(playlist_default);

        btn_search_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tiene que tomar lo que escribio en el buscar y pasarlo al request
                Log.e(">>>","entra al on click");

                CharSequence query = sv_playlist.getQuery(); // get the query string currently in the text field
                searchPlaylist(query.toString());
                Log.e(">>>","sale al on click" +query.toString());
            }
        });

    }


    public void searchPlaylist(String playlistName){
        DeezerRequest request= DeezerRequestFactory.requestSearchPlaylists(playlistName);
        AsyncDeezerTask task = new AsyncDeezerTask(deezerConnect,
                new JsonRequestListener() {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onResult(final Object result, final Object requestId) {

                        mPlaylistList.clear();

                        try {
                            mPlaylistList.addAll((List<Playlist>) result);
                                              }
                        catch (ClassCastException e) {
                            Log.e(">>>",""+e.getMessage());
                        }

                        if (mPlaylistList.isEmpty()) {
                            Log.e(">>>","la lista esta vacia");

                        }

                        adapterPlaylist.modifyData((ArrayList<Playlist>) mPlaylistList);
                        adapterPlaylist.notifyDataSetChanged();
                        adapterPlaylist.showAllPlaylist((ArrayList<Playlist>) mPlaylistList);


                        Log.e(">>>","");

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
    public void onItemClick(Playlist playlist) {
        Intent i = new Intent(this, PlaylistActivity.class);
        i.putExtra("playlist", playlist.getId());
        startActivity(i);
    }


}
