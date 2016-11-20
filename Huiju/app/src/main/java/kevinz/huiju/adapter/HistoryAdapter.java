package kevinz.huiju.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import kevinz.huiju.R;
import kevinz.huiju.api.HistoryApi;
import kevinz.huiju.bean.history.HistoryDetails;
import kevinz.huiju.support.Settings;
import kevinz.huiju.ui.history.HistoryDetailsActivity;


/**
 * Created by vienan on 2015/9/17.
 */
public class HistoryAdapter extends BaseExpandableListAdapter {
    private LayoutInflater inflater = null;
    private List<HistoryDetails> groupList;
    private Context context;

    /**
     * 构造方法
     *
     * @param context
     * @param group_list
     */
    public HistoryAdapter(Context context, List<HistoryDetails> group_list) {
        this.context = context;
        this.groupList = group_list;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    /**
     * 返回一级Item总数
     */
    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return groupList.size();
    }

    /**
     * 返回二级Item总数
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    /**
     * 获取一级Item内容
     */
    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }


    /**
     * 获取二级Item内容
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return getCombinedChildId(groupPosition, childPosition);
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return super.getCombinedChildId(groupId, childId);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        TextView date;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.history_date, null);
        }
        date = (TextView) convertView.findViewById(R.id.one_status_name);

        date.setText(""+groupList.get(groupPosition).getYear());

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder = new ChildViewHolder();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.cardview, null);
        }
        final HistoryDetails details = groupList.get(groupPosition);

        holder.title = (TextView) convertView.findViewById(R.id.title);
        holder.title.setText(details.getTitle());
        holder.image = (SimpleDraweeView) convertView.findViewById(R.id.image);
        if(new Settings().getBoolean("no_pics",false)){
            holder.image.setImageResource(R.drawable.no_pics);
        }else {
            if(details.getPic()!="") {
                holder.image.setImageURI(Uri.parse(details.getPic()));
            }else {
                holder.image.setImageResource(R.drawable.history_default);
            }
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,HistoryDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", HistoryApi.history_content+details.get_id());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }


    static class ChildViewHolder {
        TextView title;
        SimpleDraweeView image;
    }



}
