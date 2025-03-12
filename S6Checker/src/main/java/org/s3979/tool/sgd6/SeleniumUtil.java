package org.s3979.tool.sgd6;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumUtil {

    private static WebDriver driver;

    public static boolean init() {
        if (driver == null) {
            String dir = System.getProperty("user.dir");
            String path = String.format("%s/chromedriver.exe", dir);
            System.setProperty("webdriver.chrome.driver", path);
            driver = new ChromeDriver();

            driver.get("https://xosothantai.mobi/");

            Dimension size = new Dimension(1920, 1080);
            driver.manage().window().setSize(size);

            System.out.println(driver.getTitle());

            return true;
        }
        return false;
    }

    public static String run() {
        boolean init = init();
        if (!init)
            driver.navigate().refresh();
        return driver.getPageSource();
    }

    public static void quit() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
