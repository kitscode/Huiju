package kevinz.huiju.bean;

/**
 * Created by Administrator on 2016/11/13.
 */

public class Videobean {
    private Details[] V9LG4B3A0;

    public Details[] getV9LG4B3A0() {
        return V9LG4B3A0;
    }

    public void setV9LG4B3A0(Details[] v9LG4B3A0) {
        V9LG4B3A0 = v9LG4B3A0;
    }

    public class Details{
        String cover;
        String title;
        String mp4_url;

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMp4_url() {
            return mp4_url;
        }

        public void setMp4_url(String mp4_url) {
            this.mp4_url = mp4_url;
        }
    }
}
