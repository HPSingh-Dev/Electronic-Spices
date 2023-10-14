package com.conductor.apni.littleleappwa.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@DatabaseTable(tableName = StoryPojo.Column.TABLE_NAME)
public class StoryPojo implements Serializable {

    @DatabaseField(
            id = true,
            unique = true,
            columnName = Column._ID)
    private int id;

    @DatabaseField(
            columnName = Column.NAME,canBeNull = true)
    private String name;

    @DatabaseField(
            columnName = Column.IMG,canBeNull = true)
    private String image;

    @DatabaseField(
            columnName = Column.DESCRIPTION,canBeNull = true)
    private String descriptions;

    @DatabaseField(
            columnName = Column.BUCKET,canBeNull = true)
    private String skill_bucket;

    @DatabaseField(
            columnName = Column.DESC,canBeNull = true)
    private String short_desc;

    @DatabaseField(
            columnName = Column.INSTRUCTION,canBeNull = true)
    private String instruction;

//    @ForeignCollectionField(eager = false)
//    private Collection<StoryItemPojo> mobile_app_content;
//
//    public Collection<StoryItemPojo> getMobile_app_content() {
//        return mobile_app_content;
//    }
//
//    public void setMobile_app_content(Collection<StoryItemPojo> mobile_app_content) {
//        this.mobile_app_content = mobile_app_content;
//    }


    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkill_bucket() {
        return skill_bucket;
    }

    public void setSkill_bucket(String skill_bucket) {
        this.skill_bucket = skill_bucket;
    }

    public String getShort_desc() {
        return short_desc;
    }

    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    @Override
    public String toString() {
        return "StoryPojo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", skill_bucket='" + skill_bucket + '\'' +
                ", short_desc='" + short_desc + '\'' +
                '}';
    }

    public interface Column {
        String TABLE_NAME= "Story";
        String NAME = "name";
        String DESCRIPTION = "description";
        String INSTRUCTION = "instruction";
        String IMG = "img";
        String BUCKET = "skill_bucket";
        String DESC = "desc";
        String _ID="id";
    }
}
