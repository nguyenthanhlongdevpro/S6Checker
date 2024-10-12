package org.s3979.tool.sgd6;

public class MyMain {

    public static void main(String[] args) {

        RSSFeedParser parser = new RSSFeedParser("https://kqxs.net.vn/rss-feed/mien-nam-xsmn.rss");
        Feed feed = parser.readFeed();
        System.out.println(feed);
        for (FeedMessage message : feed.getMessages()) {
            System.out.println(message.description);
        }
    }
}
