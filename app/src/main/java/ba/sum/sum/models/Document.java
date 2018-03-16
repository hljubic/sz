package ba.sum.sum.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ba.hljubic.jsonorm.JsonTable;
import ba.sum.sum.utils.Constants;

/**
 * Created by Darko on 15.3.2018..
 */

public class Document extends JsonTable<Institution> {
    private String title;
    @SerializedName("desc")
    private String description;
    private String file;
    private String type;
    private String featured;
    @SerializedName("institution_id")
    private int institutionId;
    @SerializedName("post_id")
    private int postId;
    public boolean expanded;

    private List<Document> documents;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFile() {
        return Constants.BASE_URL + "preuzmi/" + file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFeatured() {
        return featured;
    }

    public void setFeatured(String featured) {
        this.featured = featured;
    }

    public int getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(int institutionId) {
        this.institutionId = institutionId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}
