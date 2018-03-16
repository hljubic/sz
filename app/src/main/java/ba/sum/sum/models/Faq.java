package ba.sum.sum.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ba.hljubic.jsonorm.JsonTable;

/**
 * Created by HP_PC on 13.3.2018..
 */

public class Faq extends JsonTable<Faq> {

    public boolean expanded;
    List<Faq> faqs;
    @SerializedName("question")
    private String pitanje;
    @SerializedName("answer")
    private String odgovor;
    @SerializedName("faq_id")
    private int faqId;

    public Faq() {
    }


    public Faq(String pitanje, String odgovor) {
        this.pitanje = pitanje;
        this.odgovor = odgovor;
    }

    public int getFaqId() {
        return faqId;
    }

    public void setFaqId(int faqId) {
        this.faqId = faqId;
    }

    public String getPitanje() {
        return pitanje;
    }

    public void setPitanje(String pitanje) {
        this.pitanje = pitanje;
    }

    public String getOdgovor() {
        return odgovor;
    }

    public void setOdgovor(String odgovor) {
        this.odgovor = odgovor;
    }

    @Override
    public String toString() {
        return "Faq{" +
                "pitanje='" + pitanje + '\'' +
                ", odgovor='" + odgovor + '\'' +
                '}';
    }


}
