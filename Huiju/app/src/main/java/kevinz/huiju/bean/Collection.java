package kevinz.huiju.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/18.
 */

public class Collection implements Serializable {
    String title;
    String image;
    String description;
    String url;

    public int getIfcollected() {
        return ifcollected;
    }

    public void setIfcollected(int ifcollected) {
        this.ifcollected = ifcollected;
    }

    int ifcollected;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}