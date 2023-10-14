package com.conductor.apni.littleleappwa.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QItemPojo implements Serializable {

    private int id;

    private int activity_id;

    private String image;

    private String hint;

    private String question;

    private int is_completed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public int getIs_completed() {
        return is_completed;
    }

    public void setIs_completed(int is_completed) {
        this.is_completed = is_completed;
    }

    public interface Column {
        String TABLE_NAME= "StoryItem";
        String IMAGE = "image";
        String TEXT = "text";
        String _ID="id";
        String STORYID="storyid";
    }
}
