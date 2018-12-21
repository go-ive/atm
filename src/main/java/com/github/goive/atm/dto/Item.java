package com.github.goive.atm.dto;

import java.util.List;
import java.util.Objects;

public class Item {

    private transient Double titleScore = 0.0;
    private transient Double synonymScore = 0.0;

    private String type;
    private String title;
    private String picture;
    private String thumbnail;
    private int episodes;
    private List<String> sources;
    private List<String> relations;
    private List<String> synonyms;

    public Item(String type, String title, String picture, String thumbnail, int episodes, List<String> sources, List<String> relations, List<String> synonyms) {
        this.type = type;
        this.title = title;
        this.picture = picture;
        this.thumbnail = thumbnail;
        this.episodes = episodes;
        this.sources = sources;
        this.relations = relations;
        this.synonyms = synonyms;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getPicture() {
        return picture;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public int getEpisodes() {
        return episodes;
    }

    public List<String> getSources() {
        return sources;
    }

    public List<String> getRelations() {
        return relations;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public Double getTitleScore() {
        return titleScore;
    }

    public void setTitleScore(Double titleScore) {
        this.titleScore = titleScore;
    }

    public Double getSynonymScore() {
        return synonymScore;
    }

    public void setSynonymScore(Double synonymScore) {
        this.synonymScore = synonymScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(type, item.type) &&
                Objects.equals(title, item.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, title);
    }

    @Override
    public String toString() {
        return "Item{" +
                "type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", picture='" + picture + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", episodes=" + episodes +
                ", sources=" + sources +
                ", relations=" + relations +
                ", synonyms=" + synonyms +
                '}';
    }
}
