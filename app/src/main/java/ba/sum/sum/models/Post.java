package ba.sum.sum.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HP_PC on 12.3.2018..
 */

public class Post {
    private int id;
    private String title;
    private String content;
    private String link;
    @SerializedName("user_id")
    private int userId;

    public Post(int id, String title, String content, String link, int userId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.link = link;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
