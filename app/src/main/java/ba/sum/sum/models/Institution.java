package ba.sum.sum.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ba.hljubic.jsonorm.JsonTable;
import ba.hljubic.jsonorm.annotations.InFile;
import ba.sum.sum.utils.Constants;

/**
 * Created by HP_PC on 8.3.2018..
 */

@InFile("institutions.json")
public class Institution extends JsonTable<Institution> {
    public String name;
    public boolean expanded;
    private String logo;
    private String address;
    private String web;
    private String email;
    private String phone;
    private String type;
    private String latitude;
    private String longitude;
    private String content;
    @SerializedName("institution_id")
    private int institutionId;
    private List<Document> images;
    private List<Document> documents;
    private List<Institution> children;

    public Institution(String s) {
    }

    public static Institution findParentOrChildById(String id) {
        List<Institution> institutions = listAll(Institution.class);

        for (Institution institution : institutions) {
            if (institution.getId().equals(id)) {
                return institution;
            }
        }

        for (Institution institution : institutions) {

            for (Institution child : institution.getChildren()) {
                if (child.getId().equals(id)) {
                    return child;
                }
            }
        }

        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return Constants.BASE_API_URL + "preuzmi/" + logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getLatitude() {
        if (latitude == null)
            return null;

        return Double.parseDouble(latitude);
    }

    public Double getLongitude() {
        if (longitude == null)
            return null;

        return Double.parseDouble(longitude);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(int institutionId) {
        this.institutionId = institutionId;
    }

    public List<Document> getImages() {
        return images;
    }

    public void setImages(List<Document> images) {
        this.images = images;
    }

    public List<Institution> getChildren() {
        return children;
    }

    public void setChildren(List<Institution> children) {
        this.children = children;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public String getFeaturedImage() {
        if (this.images != null && this.images.size() > 0) {
            return this.images.get(0).getFile();
        }

        return "";
    }

    public boolean isLiked() {
        Favorite favorite = Favorite.findByInstitutionId(this.getId());

        return favorite != null && favorite.isLiked();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}