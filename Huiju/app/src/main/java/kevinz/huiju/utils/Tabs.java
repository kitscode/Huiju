package kevinz.huiju.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/10.
 */

public class Tabs{
    public static String[] channel_tag = {"hot", "frontier", "review", "interview", "visual", "brief", "fact", "techb"};
    public static String[] channel_title = {"热点", "前沿", "评论", "专访", "视觉", "速读", "谣言粉碎机", "商业科技"};
    public static Map<String,String> tabMap=new HashMap<>();

    static {
        for(int i=0;i<channel_tag.length;i++){
            tabMap.put(channel_tag[i],channel_title[i]);
        }
    }
}
