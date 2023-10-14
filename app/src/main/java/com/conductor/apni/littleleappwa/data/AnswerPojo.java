package com.conductor.apni.littleleappwa.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@DatabaseTable(tableName = AnswerPojo.Column.TABLE_NAME)
public class AnswerPojo implements Serializable {

    @DatabaseField(
            generatedId = true,
            unique = true,
            columnName = Column._ID)
    private int id;

    @DatabaseField(
            columnName = Column.STORYID,canBeNull = true)
    private int activity_id;

    @DatabaseField(
            columnName = Column.TEXT,canBeNull = true)
    private String line;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        String TABLE_NAME= "AnswerItem";
        String IMAGE = "image";
        String TEXT = "text";
        String _ID="id";
        String STORYID="storyid";
    }
}
