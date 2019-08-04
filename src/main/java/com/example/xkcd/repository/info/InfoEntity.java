package com.example.xkcd.repository.info;

import androidx.room.Entity;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "info_table")
public class InfoEntity extends Info {

    private boolean favorite;

    public InfoEntity(Info info) {
        super(info.getMonth(),
                info.getNum(),
                info.getLink(),
                info.getYear(),
                info.getNews(),
                info.getSafe_title(),
                info.getTranscript(),
                info.getAlt(),
                info.getImg(),
                info.getTitle(),
                info.getDay()
        );
        this.favorite = false;
    }

    public InfoEntity(String month, int num, String link, String year, String news, String safe_title, String transcript, String alt, String img, String title, String day, boolean favorite) {
        super(month, num, link, year, news, safe_title, transcript, alt, img, title, day);
        this.favorite = favorite;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean isFavorite) {
        favorite = isFavorite;
    }

    @NotNull
    @Override
    public String toString() {
        return "InfoEntity{" +
                "favorite=" + favorite +
                ", " +
                super.toString() +
                '}';
    }
}
