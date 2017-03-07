package kevinz.huiju.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import kevinz.huiju.R;
import kevinz.huiju.bean.Collections;
import kevinz.huiju.ui.home.CollectDetailsActivity;


public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.CollectHolder> {

    Cursor mCursor;
    Context mContext;

    public CollectAdapter(Cursor mCursor, Context context) {
        this.mCursor = mCursor;
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    @Override
    public void onBindViewHolder(CollectHolder holder, int position) {
        mCursor.moveToNext();
        final Collections collections = new Collections();
        collections.setTitle(mCursor.getString(0));
        collections.setImage(mCursor.getString(1));
        collections.setDescription(mCursor.getString(2));
        collections.setUrl(mCursor.getString(3));
        collections.setIfcollected(mCursor.getInt(4));
        holder.title.setText(mCursor.getString(0));
        holder.image.setImageURI(Uri.parse(mCursor.getString(1)));
        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("details",collections);
                Intent intent = new Intent(mContext, CollectDetailsActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public CollectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview,parent,false);
        CollectHolder holder = new CollectHolder(view);
        return holder;
    }

    class CollectHolder extends RecyclerView.ViewHolder{
        private View parentView;
        private TextView title;
        private SimpleDraweeView image;
        public CollectHolder(View view){
            super(view);
            parentView = view;
            title = (TextView)view.findViewById(R.id.title);
            image = (SimpleDraweeView)view.findViewById(R.id.image);
        }
    }
}
