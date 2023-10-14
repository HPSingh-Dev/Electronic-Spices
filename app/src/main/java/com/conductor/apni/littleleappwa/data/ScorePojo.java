package com.conductor.apni.littleleappwa.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Ajay on 7/21/2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScorePojo {
    private int id;
    private String name;
    private int correct_words;
    private int total_words;

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

    public int getCorrect_words() {
        return correct_words;
    }

    public void setCorrect_words(int correct_words) {
        this.correct_words = correct_words;
    }

    public int getTotal_words() {
        return total_words;
    }

    public void setTotal_words(int total_words) {
        this.total_words = total_words;
    }
}
