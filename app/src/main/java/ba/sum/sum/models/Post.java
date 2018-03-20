package ba.sum.sum.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ba.hljubic.jsonorm.JsonTable;
import ba.sum.sum.utils.Constants;

/**
 * Created by HP_PC on 12.3.2018..
 */

public class Post extends JsonTable<Post> {

    private String title;
    private String content;
    private String link;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("created_at")
    private String createdAt;
    private List<Document> images;


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

    public String getCreatedAt() {
        return createdAt.split(" ")[0];
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<Document> getImages() {
        return images;
    }

    public void setImages(List<Document> images) {
        this.images = images;
    }

    public String getFeaturedImage() {
        if (this.images != null && this.images.size() > 0) {
            // TODO: Change to getFile();
            return Constants.BASE_API_URL + "preuzmi/" + this.images.get(0).getTitle();
        }

        return "";
    }
}
