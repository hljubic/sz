package ba.sum.sum.models;

import com.google.gson.annotations.SerializedName;

import ba.hljubic.jsonorm.JsonTable;
import ba.sum.sum.utils.Constants;

/**
 * Created by Darko on 18.3.2018..
 */

public class Step extends JsonTable<Step> {
    private String title;
    private String image;
    private String content;
    private String button;
    @SerializedName("user_id")
    private int userId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return Constants.BASE_API_URL + "preuzmi/" + image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
