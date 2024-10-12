package org.s3979.tool.sgd6;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RssReader {

    public static List<RssItemModel> read(String url) {
        List<RssItemModel> items = new ArrayList<>();
        try {
            URL feedSource = new URL(url);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedSource));
            System.out.println("Feed Title: " + feed.getTitle());
            List<SyndEntry> entries = feed.getEntries();
            for (SyndEntry entry : entries) {
                RssItemModel item = new RssItemModel();
                item.title = entry.getTitle();
                item.link = entry.getLink();
                item.pubDate = entry.getPublishedDate().toString();
                item.description = entry.getDescription().getValue();
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }
}
