package org.s3979.tool.sgd6;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Ag3in1Page {

    private WebDriver driver;

    private static String USERNAME = "";
    private static String PASSWORD = "";
    private static String CODE = "";

    private static final HashMap<String, String> refs = new HashMap<>();
    static String currentOutStd = "";

    private static final List<String> filters = new ArrayList<>();

    public void init() throws Exception {
        if (driver == null) {
            String dir = System.getProperty("user.dir");
            String path = String.format("%s/chromedriver", dir);

            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                path += ".exe";
                System.out.println(path);
            }

            System.setProperty("webdriver.chrome.driver", path);

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");  // Vô hiệu hóa GPU để tăng hiệu suất
            options.addArguments("--disable-dev-shm-usage"); // Giảm lỗi bộ nhớ trong container
            options.addArguments("--no-sandbox"); // Chạy không cần sandbox (hữu ích khi chạy trên Docker)

            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
            driver.get("https://agent.3in1bet.me/");

            Dimension size = new Dimension(1920, 1080);
            driver.manage().window().setSize(size);
        }

        readProp();
        initFilter();
    }

    private void initFilter() {
        if (filters.isEmpty()) {
            filters.add("+1.25");
            filters.add("+1.50");
            filters.add("+1.75");

            filters.add("+2.00");
            filters.add("+2.25");
            filters.add("+2.50");
            filters.add("+2.75");

            filters.add("+3.00");
            filters.add("+3.25");
            filters.add("+3.50");
            filters.add("+3.75");

            filters.add("+4.00");
            filters.add("+4.25");
            filters.add("+4.50");
            filters.add("+4.75");

            filters.add("+5.00");
            filters.add("+5.25");
            filters.add("+5.50");
            filters.add("+5.75");

            // Minus

            filters.add("-1.25");
            filters.add("-1.50");
            filters.add("-1.75");

            filters.add("-2.00");
            filters.add("-2.25");
            filters.add("-2.50");
            filters.add("-2.75");

            filters.add("-3.00");
            filters.add("-3.25");
            filters.add("-3.50");
            filters.add("-3.75");

            filters.add("-4.00");
            filters.add("-4.25");
            filters.add("-4.50");
            filters.add("-4.75");

            filters.add("-5.00");
            filters.add("-5.25");
            filters.add("-5.50");
            filters.add("-5.75");
        }
    }

    private void readProp() throws Exception {
        Properties props = new Properties();

        FileInputStream fis = new FileInputStream("config.properties");
        props.load(fis); // Load properties from file

        // Access properties
        String usr = props.getProperty("acc.usr");
        String pw = props.getProperty("acc.pw");
        String code = props.getProperty("acc.code");

        USERNAME = usr;
        PASSWORD = pw;
        CODE = code;
    }

    public void login() throws Exception {
        String user = "//*[@id='txtUserName']";
        String pw = "//*[@id='txtPassword']";
        String sub = "//*[@id='sub']";

        WebElement elementUser = driver.findElement(By.xpath(user));
        elementUser.sendKeys(USERNAME);
        WebElement elementPW = driver.findElement(By.xpath(pw));
        elementPW.sendKeys(PASSWORD);

        passCaptcha();

        WebElement elementSubmit = driver.findElement(By.xpath(sub));
        elementSubmit.click();
    }

    private void passCaptcha() throws Exception {
        String path = "//*[@id='verifyimg']";
        WebElement element = driver.findElement(By.xpath(path));
        String code = ImageCaptchaSolver.run(driver, element);
        if (code != null) {
            path = "//*[@id='txtInvalidation']";
            element = driver.findElement(By.xpath(path));
            element.sendKeys(code);
        }
    }

    public void passSecurityCode() {
        String code = "//*[@id='txt_pwd']";
        String sub = "//input[@value='Submit']";

        WebElement elementUser = driver.findElement(By.xpath(code));
        elementUser.sendKeys(CODE);

        WebElement elementSubmit = driver.findElement(By.xpath(sub));
        elementSubmit.click();
    }

    public void closeDialogIfDisplay() {
        switchFrame("mainFrame");

        String xpath = "//*[@id='Button1']";
        WebElement element = driver.findElement(By.xpath(xpath));
        if (element.isDisplayed()) element.click();
    }

    public void clickOnReportMenu() {
        switchBackToRootFrame();
        switchFrame("leftFrame");

        String xpath = "//*[@id='divLeftBox']//div[contains(text(),'Report')]";
        WebElement element = driver.findElement(By.xpath(xpath));
        element.click();
    }

    public void clickOnOutStandingButton() {
        switchBackToRootFrame();
        switchFrame("leftFrame");

        String xpath = "//a[contains(text(), 'Outstanding')]";
        WebElement element = driver.findElement(By.xpath(xpath));
        element.click();
    }

    public void doScan() throws Exception {
        switchBackToRootFrame();
        switchFrame("mainFrame");

        String path = "//table[@id='tableGridView']//a";

//        List<WebElement> elMasters = driver.findElements(By.xpath(path));
//        for (int i = 0; i < elMasters.size(); i++) {
//            elMasters = driver.findElements(By.xpath(path));
//            WebElement elMaster = elMasters.get(i);
//            elMaster.click();
//
//            // Code agent here
//        }

//        List<WebElement> elAgents = driver.findElements(By.xpath(path));
//        for (int j = 0; j < elAgents.size(); j++) {
//            elAgents = driver.findElements(By.xpath(path));
//            WebElement elAgent = elAgents.get(j);
//            clickElement(elAgent);
//
//            List<WebElement> elMembers = driver.findElements(By.xpath(path));
//            for (int k = 0; k < elMembers.size(); k++) {
//                elMembers = driver.findElements(By.xpath(path));
//                WebElement elMember = elMembers.get(k);
//                String user = elMember.getText().trim();
//                clickElement(elMember);
//
//                logTicket(user);
//                clickBack();
//            }
//            clickBack();
//        }

        List<WebElement> elMembers = driver.findElements(By.xpath(path));
        for (int k = 0; k < elMembers.size(); k++) {
            elMembers = driver.findElements(By.xpath(path));
            WebElement elMember = elMembers.get(k);
            String user = elMember.getText().trim();
            clickElement(elMember);

            logTicket(user);
            clickBack();
        }
    }

    private void logTicket(String user) {
        String pathRow = "//table[@id='Grid1']//tr";
        List<WebElement> rows = driver.findElements(By.xpath(pathRow));
        int sz = rows.size();
        for (int i = 1; i < sz - 1; i++) {
            WebElement row = rows.get(i);

            String pathCol = ".//td[3]";
            WebElement element = row.findElement(By.xpath(pathCol));
            String text = element.getText();

            String key = text.substring(0, 10);
            if (!refs.containsKey(key)) {

                String pathCol2 = ".//td[4]";
                WebElement element2 = row.findElement(By.xpath(pathCol2));
                String text2 = element2.getText();

                if (checkTicket(text2)) {
                    refs.put(key, text2);
                    System.out.println(user.toUpperCase() + "\n" + text2 + "\n");
                    TelegramSender.sendMessage("*" + user.toUpperCase() + "*" + "\n" + text2);
                }
            }
        }
    }

    private boolean checkTicket(String info) {
        info = info.toLowerCase();
        for (String filter : filters) {
            if (info.contains("handicap") && info.contains(filter))
                return true;
        }
        return false;
    }

    public void clickBack() throws Exception {
        String back = "//input[@value='Back']";
        boolean isBackDisplay = isElementPresent(By.xpath(back), driver);
        if (isBackDisplay) {
            WebElement elBack = driver.findElement(By.xpath(back));
            clickElement(elBack);
        }
    }

    public boolean isOutStdChanged() {
        String path = "(//table[@id='tableGridView']//tr)[last()]/td[2]";
        boolean isDisplay = isElementPresent(By.xpath(path), driver);
        if (isDisplay) {
            WebElement element = driver.findElement(By.xpath(path));
            String text = element.getText();
            if (currentOutStd.isEmpty() || !currentOutStd.equals(text)) {
                currentOutStd = text;
                return true;
            }
        }
        return false;
    }

    public boolean isElementPresent(By locator, WebDriver driver) {
        return !driver.findElements(locator).isEmpty();
    }

    private void clickElement(WebElement element) throws Exception {
        element.click();
        sleep(2000);
    }

    public void refresh() throws Exception {
        switchBackToRootFrame();
        switchFrame("mainFrame");
        String path = "//input[@value='Refresh']";
        WebElement element = driver.findElement(By.xpath(path));
        clickElement(element);
    }

    public void quit() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    private void sleep(long time) throws Exception {
        Thread.sleep(time);
    }

    private void switchFrame(String id) {
        driver.switchTo().frame(id);
    }

    private void switchBackToRootFrame() {
        driver.switchTo().defaultContent();
    }
}
