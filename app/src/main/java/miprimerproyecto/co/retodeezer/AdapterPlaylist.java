package miprimerproyecto.co.retodeezer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.deezer.sdk.model.Playlist;

import java.util.ArrayList;


public class AdapterPlaylist extends RecyclerView.Adapter<AdapterPlaylist.CustomViewHolder> {

    ArrayList<Playlist> data;

    public void showAllPlaylist(ArrayList<Playlist> allPlaylist) {
        for(int i = 0 ; i<allPlaylist.size() ; i++){
            if(!data.contains(allPlaylist.get(i))){
                data.add(allPlaylist.get(i));
            }
        }
        //que ya el adapter tiene valores
        notifyDataSetChanged();
    }



    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;
        public CustomViewHolder(LinearLayout v) {
            super(v);
            root = v;
        }
    }

    public AdapterPlaylist(){
        data = new ArrayList<Playlist>();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.renglon_playlist, parent, false);
        CustomViewHolder vh = new CustomViewHolder(v);


        return vh;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        ImageView img = holder.root.findViewById(R.id.renglon_list_img);
        Glide.with(holder.root.getContext()).load(data.get(position).getMediumImageUrl()).into(img);
        ((TextView) holder.root.findViewById(R.id.renglon_list_name)).setText(data.get(position).getTitle()+"");
        ((TextView) holder.root.findViewById(R.id.renglon_user_creator)).setText(data.get(position).getCreator().getName()+"");
        ((TextView) holder.root.findViewById(R.id.renglon_num_items)).setText(data.get(position).getTracks().size()+"");

        //para cuando le de click en un reglon ya sepa que hacer
        holder.root.findViewById(R.id.renglon_playlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(data.get(position));
            }
        });

    }

    public void modifyData(ArrayList<Playlist> data) {
        this.data = data;
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    //OBSERVER
    public interface OnItemClickListener{
        void onItemClick(Playlist playlist);
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener){
        this.listener = listener;
    }



}