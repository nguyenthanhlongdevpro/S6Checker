package org.s3979.tool.sgd6;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SeleniumUtil {

    private static WebDriver driver;

    public static boolean init() {
        if (driver == null) {
            String dir = System.getProperty("user.dir");
            String path = String.format("%s/chromedriver.exe", dir);
            System.setProperty("webdriver.chrome.driver", path);

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");  // Vô hiệu hóa GPU để tăng hiệu suất
            options.addArguments("--disable-dev-shm-usage"); // Giảm lỗi bộ nhớ trong container
            options.addArguments("--no-sandbox"); // Chạy không cần sandbox (hữu ích khi chạy trên Docker)

            driver = new ChromeDriver(options);

            driver.get("https://xosothantai.mobi/");

            Dimension size = new Dimension(1920, 1080);
            driver.manage().window().setSize(size);

            System.out.println(driver.getTitle());

            return true;
        }
        return false;
    }

    public static String run() {
        init();
        return driver.getPageSource();
    }

    public static void quit() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
