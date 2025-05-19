package org.s3979.tool.sgd6;

public class CopyBet {

    public static void main(String[] args) {

        Ag3in1Page ag3in1Page = new Ag3in1Page();
        ag3in1Page.init();
        ag3in1Page.login();
        ag3in1Page.passSecurityCode();

        String html = ag3in1Page.getPageSource();
        System.out.println(html);
    }
}
