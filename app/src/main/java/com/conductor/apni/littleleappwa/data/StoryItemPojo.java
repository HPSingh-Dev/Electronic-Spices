package com.conductor.apni.littleleappwa.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@DatabaseTable(tableName = StoryItemPojo.Column.TABLE_NAME)
public class StoryItemPojo implements Serializable {

    @DatabaseField(
            generatedId = true,
            unique = true,
            columnName = Column._ID)
    private int id;

    @DatabaseField(
            columnName = Column.STORYID,canBeNull = true)
    private int activity_id;

    @DatabaseField(
            columnName = Column.IS_COMPLETED,canBeNull = true)
    private int is_completed;

    @DatabaseField(
            columnName = Column.IMAGE,canBeNull = true)
    private String image;

    @DatabaseField(
            columnName = Column.TEXT,canBeNull = true)
    private String line;

    public int getIs_completed() {
        return is_completed;
    }

    public void setIs_completed(int is_completed) {
        this.is_completed = is_completed;
    }

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

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public interface Column {
        String TABLE_NAME= "StoryItem";
        String IMAGE = "image";
        String TEXT = "text";
        String IS_COMPLETED = "is_completed";
        String _ID="id";
        String STORYID="storyid";
    }
}
