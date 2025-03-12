package org.s3979.tool.sgd6;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JsoupUtil {

    public static Document load(String url){
        try {
            return Jsoup.connect(url).get();
        }catch (Exception ex){
            return null;
        }
    }

    public static Document pase(String html){
        try {
            return Jsoup.parse(html);
        }catch (Exception ex){
            return null;
        }
    }
}
