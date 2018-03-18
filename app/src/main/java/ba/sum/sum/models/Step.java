package ba.sum.sum.models;

import android.widget.Button;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ba.hljubic.jsonorm.JsonTable;

/**
 * Created by Darko on 18.3.2018..
 */

public class Step extends JsonTable<Step> {
    private String title;
    private String image;
    private String content;
    private Button button;
    @SerializedName("user_id")
    private int userId;

    private List<Step> items;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Step> getItems() {
        return items;
    }

    public void setItems(List<Step> items) {
        this.items = items;
    }
}
