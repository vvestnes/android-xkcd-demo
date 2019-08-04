package com.example.xkcd.repository.info;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * Database Entity (row) representing one xkcd from json. Expected format:
 * {
 * "month": "7",
 * "num": 614,
 * "link": "",
 * "year": "2009",
 * "news": "",
 * "safe_title": "Woodpecker",
 * "transcript": "[[A man with a beret and a woman are standing on a boardwalk, leaning on a handrail.]]\nMan: A woodpecker!\n<<Pop pop pop>>\nWoman: Yup.\n\n[[The woodpecker is banging its head against a tree.]]\nWoman: He hatched about this time last year.\n<<Pop pop pop pop>>\n\n[[The woman walks away.  The man is still standing at the handrail.]]\n\nMan: ... woodpecker?\nMan: It's your birthday!\n\nMan: Did you know?\n\nMan: Did... did nobody tell you?\n\n[[The man stands, looking.]]\n\n[[The man walks away.]]\n\n[[There is a tree.]]\n\n[[The man approaches the tree with a present in a box, tied up with ribbon.]]\n\n[[The man sets the present down at the base of the tree and looks up.]]\n\n[[The man walks away.]]\n\n[[The present is sitting at the bottom of the tree.]]\n\n[[The woodpecker looks down at the present.]]\n\n[[The woodpecker sits on the present.]]\n\n[[The woodpecker pulls on the ribbon tying the present closed.]]\n\n((full width panel))\n[[The woodpecker is flying, with an electric drill dangling from its feet, held by the cord.]]\n\n{{Title text: If you don't have an extension cord I can getByNum that too.  Because we're friends!  Right?}}",
 * "alt": "If you don't have an extension cord I can getByNum that too.  Because we're friends!  Right?",
 * "img": "https://imgs.xkcd.com/comics/woodpecker.png",
 * "title": "Woodpecker",
 * "day": "24"
 * }
 */

public class Info {

    private String month;

    @PrimaryKey(autoGenerate = false)
    private int num;

    private String link;

    private String year;

    private String news;

    private String safe_title;

    private String transcript;

    private String alt;

    private String img;

    private String title;

    private String day;

    public Info(String month, int num, String link, String year, String news, String safe_title, String transcript, String alt, String img, String title, String day) {
        this.month = month;
        this.num = num;
        this.link = link;
        this.year = year;
        this.news = news;
        this.safe_title = safe_title;
        this.transcript = transcript;
        this.alt = alt;
        this.img = img;
        this.title = title;
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public int getNum() {
        return num;
    }

    public String getLink() {
        return link;
    }

    public String getYear() {
        return year;
    }

    public String getNews() {
        return news;
    }

    public String getSafe_title() {
        return safe_title;
    }

    public String getTranscript() {
        return transcript;
    }

    public String getAlt() {
        return alt;
    }

    public String getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public String getDay() {
        return day;
    }

    @NotNull
    @Override
    public String toString() {
        return "Info{" +
                "month='" + month + '\'' +
                ", num=" + num +
                ", link='" + link + '\'' +
                ", year='" + year + '\'' +
                ", news='" + news + '\'' +
                ", safe_title='" + safe_title + '\'' +
                ", transcript='" + transcript + '\'' +
                ", alt='" + alt + '\'' +
                ", img='" + img + '\'' +
                ", title='" + title + '\'' +
                ", day='" + day + '\'' +
                '}';
    }


}
