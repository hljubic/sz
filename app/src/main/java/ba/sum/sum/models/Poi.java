package ba.sum.sum.models;

import ba.hljubic.jsonorm.JsonTable;

public class Poi extends JsonTable<Poi> {
    private String title;
    private String desc;
    private String latitude;
    private String longitude;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Double getLatitude() {
        return Double.parseDouble(latitude);
    }

    public Double getLongitude() {
        return Double.parseDouble(longitude);
    }
}
