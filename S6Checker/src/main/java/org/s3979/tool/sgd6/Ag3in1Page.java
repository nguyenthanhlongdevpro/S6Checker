package org.s3979.tool.sgd6;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

public class Ag3in1Page {

    private WebDriver driver;

    private static final String USERNAME = "b90036subaa";
    private static final String PASSWORD = "Zxcv1122@";
    private static final String CODE = "123123";

    public void init() {
        if (driver == null) {
            String dir = System.getProperty("user.dir");
            String path = String.format("%s/chromedriver", dir);
            System.setProperty("webdriver.chrome.driver", path);

            ChromeOptions options = new ChromeOptions();
            // options.addArguments("--headless");
            options.addArguments("--disable-gpu");  // Vô hiệu hóa GPU để tăng hiệu suất
            options.addArguments("--disable-dev-shm-usage"); // Giảm lỗi bộ nhớ trong container
            options.addArguments("--no-sandbox"); // Chạy không cần sandbox (hữu ích khi chạy trên Docker)

            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.get("https://agent.3in1bet.me/");

            Dimension size = new Dimension(1920, 1080);
            driver.manage().window().setSize(size);

            System.out.println(driver.getTitle());
        }
    }

    public String getPageSource() {
        if (driver != null) {
            return driver.getPageSource();
        }
        return null;
    }

    public void login() {
        String user = "//*[@id='txtUserName']";
        String pw = "//*[@id='txtPassword']";
        String sub = "//*[@id='sub']";

        WebElement elementUser = driver.findElement(By.xpath(user));
        elementUser.sendKeys(USERNAME);
        WebElement elementPW = driver.findElement(By.xpath(pw));
        elementPW.sendKeys(PASSWORD);

        sleep(5000);

        WebElement elementSubmit = driver.findElement(By.xpath(sub));
        elementSubmit.click();
    }

    public void passSecurityCode() {
        String code = "//*[@id='txt_pwd']";
        String sub = "//input[@value='Submit']";

        WebElement elementUser = driver.findElement(By.xpath(code));
        elementUser.sendKeys(CODE);

        WebElement elementSubmit = driver.findElement(By.xpath(sub));
        elementSubmit.click();
    }

    public void quit() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    private void sleep(long time){
        try {
            Thread.sleep(time);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
