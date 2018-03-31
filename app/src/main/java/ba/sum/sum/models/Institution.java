package ba.sum.sum.models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import ba.hljubic.jsonorm.JsonTable;
import ba.hljubic.jsonorm.annotations.InFile;
import ba.sum.sum.utils.App;
import ba.sum.sum.utils.Constants;

/**
 * Created by HP_PC on 8.3.2018..
 */

@InFile("institutions.json")
public class Institution extends JsonTable<Institution> {
    private static final String TYPE_UNDERGRADUATE = "Preddiplomski studij";
    private static final String TYPE_GRADUATE = "Diplomski studij";
    private static final String TYPE_POSTGRADUATE = "Postdiplomski studij";
    private static final String TYPE_EXPERT = "Stručni studij";
    private static final String TYPE_OTHER = "Ostalo";

    public String name;
    private String logo;
    private String address;
    private String web;
    private String email;
    private String phone;
    private String type;
    private String latitude;
    private String longitude;
    private String content;
    private String contact;
    @SerializedName("desc")
    private String desc;
    @SerializedName("faculty_type")
    private String facultyType;
    @SerializedName("institution_id")
    private int institutionId;
    private boolean section;
    private List<Document> images;
    private List<Document> documents;
    private List<Institution> children;

    public Institution() {
    }

    public Institution(String name) {
        this.name = name;
        this.section = true;
    }

    @NonNull
    public static Institution findParentOrChildById(String id) {
        List<Institution> institutions = App.get().getInstitutions();

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

        return new Institution();
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

    public String getWebPlain() {
        if (web == null) {
            return "";
        }

        if (web.startsWith("http://")) {
            return web.substring(7, web.length());
        } else if (web.startsWith("https://")) {
            return web.substring(8, web.length());
        } else {
            return web;
        }
    }

    public String getWeb() {
        if (web.startsWith("http://") || web.startsWith("https://")) {
            return web;
        } else {
            return "http://" + web;
        }
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
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContact() {
        if (contact == null)
            return "";

        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getFacultyType() {
        return facultyType;
    }

    public void setFacultyType(String facultyType) {
        this.facultyType = facultyType;
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

    public List<Institution> getChildrenSectioned() {
        List<Institution> undegraduate = new ArrayList<>();
        List<Institution> graduate = new ArrayList<>();
        List<Institution> postgraduate = new ArrayList<>();
        List<Institution> expert = new ArrayList<>();
        List<Institution> other = new ArrayList<>();

        for (Institution institution : children) {
            switch (institution.getFacultyType()) {
                case TYPE_UNDERGRADUATE:
                    undegraduate.add(institution);
                    break;
                case TYPE_GRADUATE:
                    graduate.add(institution);
                    break;
                case TYPE_POSTGRADUATE:
                    postgraduate.add(institution);
                    break;
                case TYPE_EXPERT:
                    expert.add(institution);
                    break;
                case TYPE_OTHER:
                    other.add(institution);
                    break;
            }
        }

        List<Institution> sectionedChildren = new ArrayList<>();

        if (undegraduate.size() > 0) {
            sectionedChildren.add(new Institution("Preddiplomski studij"));
            sectionedChildren.addAll(undegraduate);
        }

        if (graduate.size() > 0) {
            sectionedChildren.add(new Institution("Diplomski studij"));
            sectionedChildren.addAll(graduate);
        }

        if (postgraduate.size() > 0) {
            sectionedChildren.add(new Institution("Postdiplomski studij"));
            sectionedChildren.addAll(postgraduate);
        }

        if (expert.size() > 0) {
            sectionedChildren.add(new Institution("Stručni studij"));
            sectionedChildren.addAll(expert);
        }

        if (other.size() > 0) {
            sectionedChildren.add(new Institution("Ostalo"));
            sectionedChildren.addAll(other);
        }

        return sectionedChildren;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public boolean isSection() {
        return section;
    }

    public void setSection(boolean section) {
        this.section = section;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}