package ba.sum.sum.models;

import com.google.gson.annotations.SerializedName;

import ba.hljubic.jsonorm.JsonTable;
import ba.sum.sum.utils.Constants;

/**
 * Created by HP_PC on 17.3.2018..
 */

public class Setting extends JsonTable<Setting> {

    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("filename")
    private String filename;

    public Setting() {
    }

    public Setting(String title, String content, String filnename) {
        this.title = title;
        this.content = content;
        this.filename = filnename;
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

    public String getFilename() {
        return Constants.BASE_API_URL + "preuzmi/" + filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return "Setting{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", filnename='" + filename + '\'' +
                '}';
    }


}
