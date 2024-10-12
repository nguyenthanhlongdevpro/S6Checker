package org.s3979.tool.sgd6;

/*
 * Represents one RSS message
 */
public class FeedMessage {

    String title;
    String description;
    String link;
    String pubDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String author) {
        this.pubDate = author;
    }

    @Override
    public String toString() {
        return "FeedMessage [title=" + title + ", description=" + description + ", link=" + link + ", pubDate=" + pubDate + "]";
    }

}