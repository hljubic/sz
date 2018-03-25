package ba.sum.sum.utils;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import ba.hljubic.jsonorm.JsonOrm;
import ba.sum.sum.models.Institution;

/**
 * Created by hrvoje on 25/03/2018.
 */

public class App extends Application {

    // Static instance
    private static App instance;

    private List<Institution> institutions;

    public static synchronized App get() {
        if (instance == null) {
            instance = new App();
        }

        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        JsonOrm.with(this);

        institutions = new ArrayList<>();
    }

    public void initInstitutions() {
        institutions = Institution.listAll(Institution.class);
    }

    public List<Institution> getInstitutions() {
        return institutions;
    }

    public void setInstitutions(List<Institution> institutions) {
        this.institutions = institutions;
    }
}
