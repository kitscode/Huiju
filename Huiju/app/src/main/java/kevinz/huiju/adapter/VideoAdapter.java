package kevinz.huiju.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import kevinz.huiju.R;
import kevinz.huiju.bean.video.Videobean;
import kevinz.huiju.utils.Settings;
import kevinz.huiju.ui.video.PalyActivity;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {

    List<Videobean.Details> mlist = new ArrayList<>();
    protected Context mContext;

    public  VideoAdapter(Context context,List<Videobean.Details> data){
        mContext = context;
        mlist= data;
    }
    @Override
    public int getItemCount() {
        return mlist.size();
    }


    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_card, parent, false);
        VideoHolder holder = new VideoHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        final Videobean.Details details = mlist.get(position);
        if(new Settings().getBoolean("no_pics",false)){
            holder.image.setImageResource(R.drawable.no_pics_gray);
        }else {
            holder.image.setImageURI(Uri.parse(details.getCover()));
        }
        holder.text.setText(details.getTitle());
        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,PalyActivity.class);
                intent.putExtra("mp4_url",details.getMp4_url());
                mContext.startActivity(intent);
            }
        });
    }

    class VideoHolder extends RecyclerView.ViewHolder {
        private View parentView;
        private SimpleDraweeView image;
        private TextView text;

        public VideoHolder(View view) {
            super(view);
            parentView = view;
            image = (SimpleDraweeView) view.findViewById(R.id.image);
            text = (TextView) view.findViewById(R.id.title);
        }
    }

}
