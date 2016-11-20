package kevinz.huiju.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import kevinz.huiju.R;
import kevinz.huiju.bean.guoke.ArticleBean;
import kevinz.huiju.support.Settings;
import kevinz.huiju.ui.guoke.GuokeDetailsActivity;

/**
 * Created by Administrator on 2016/10/21.
 */

public class GuokeAdapter extends RecyclerView.Adapter<GuokeAdapter.GuokeViewHolder>{

    List<ArticleBean>  mlist = new ArrayList<>();
    protected Context mContext;
    public  GuokeAdapter(Context context,List<ArticleBean> data){
        mContext = context;
        mlist = data;
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    @Override
    public GuokeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview,parent,false);
        GuokeViewHolder holder = new GuokeViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(GuokeViewHolder holder, int position) {
        final ArticleBean article = mlist.get(position);
        holder.title.setText(article.getTitle());
        if(new Settings().getBoolean("no_pics",false)){
            holder.image.setImageResource(R.drawable.no_pics);
        }else {
            holder.image.setImageURI(Uri.parse(article.getImage_info().getUrl()));
        }
        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("article",article);
                Intent intent = new Intent(mContext,GuokeDetailsActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    class GuokeViewHolder extends RecyclerView.ViewHolder{
        private View parentView;
        private TextView title;
        private SimpleDraweeView image;
        public GuokeViewHolder(View view){
            super(view);
            parentView = view;
            title = (TextView)view.findViewById(R.id.title);
            image = (SimpleDraweeView)view.findViewById(R.id.image);
        }
    }
}
