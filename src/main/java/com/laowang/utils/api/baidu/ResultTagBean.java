package com.laowang.utils.api.baidu;

import java.util.List;

public class ResultTagBean {
    private String lv1_tag;
    private List<String> lv2_tag;
    private String labels;

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getLv1_tag() {
        return lv1_tag;
    }

    public void setLv1_tag(String lv1_tag) {
        this.lv1_tag = lv1_tag;
    }

    public List<String> getLv2_tag() {
        return lv2_tag;
    }

    public void setLv2_tag(List<String> lv2_tag) {
        this.lv2_tag = lv2_tag;
    }

    @Override
    public String toString() {
        return "{" +
                "lv1_tag='" + lv1_tag + '\'' +
                ", lv2_tag=" + lv2_tag +
                ", labels='" + labels + '\'' +
                '}';
    }
}
