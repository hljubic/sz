package ba.sum.sum.models;

import java.util.List;

import ba.hljubic.jsonorm.JsonTable;

public class Favorite extends JsonTable<Favorite> {
    public String institutionId;
    public boolean liked;

    public Favorite(String institutionId, boolean liked) {
        this.institutionId = institutionId;
        this.liked = liked;
    }

    public static Favorite findByInstitutionId(String institutionId) {
        List<Favorite> favorites = Favorite.listAll(Favorite.class);

        for (Favorite favorite : favorites) {
            if (favorite.getInstitutionId().equals(institutionId)) {
                return favorite;
            }
        }

        return null;
    }

    public static void toggleLike(String institutionId) {
        Favorite favorite = findByInstitutionId(institutionId);

        if (favorite == null) {
            favorite = new Favorite(institutionId, true);
            favorite.saveAsync();
        } else {
            favorite.setLiked(!favorite.isLiked());
            favorite.updateAsync();
        }
    }

    public String getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
