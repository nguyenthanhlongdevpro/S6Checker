package org.s3979.tool.sgd6;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Ag3in1Page {

    private WebDriver driver;

    private static final String USERNAME = "b90036subaa";
    private static final String PASSWORD = "Zxcv1122@";
    private static final String CODE = "123123";

    private static HashMap<String, String> refs = new HashMap<>();
    static String currentOutStd = "";

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
            driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
            driver.get("https://agent.3in1bet.me/");

            Dimension size = new Dimension(1920, 1080);
            driver.manage().window().setSize(size);
        }
    }

    public void login() {
        String user = "//*[@id='txtUserName']";
        String pw = "//*[@id='txtPassword']";
        String sub = "//*[@id='sub']";

        WebElement elementUser = driver.findElement(By.xpath(user));
        elementUser.sendKeys(USERNAME);
        WebElement elementPW = driver.findElement(By.xpath(pw));
        elementPW.sendKeys(PASSWORD);

        // sleep(5000);

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

    public void doScan() {
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

        List<WebElement> elAgents = driver.findElements(By.xpath(path));
        for (int j = 0; j < elAgents.size(); j++) {
            elAgents = driver.findElements(By.xpath(path));
            WebElement elAgent = elAgents.get(j);
            clickElement(elAgent);

            List<WebElement> elMembers = driver.findElements(By.xpath(path));
            for (int k = 0; k < elMembers.size(); k++) {
                elMembers = driver.findElements(By.xpath(path));
                WebElement elMember = elMembers.get(k);
                clickElement(elMember);

                logTicket();
                clickBack();
            }
            clickBack();
        }
    }

    private void logTicket() {
        String pathRow = "//table[@id='Grid1']//tr";
        List<WebElement> rows = driver.findElements(By.xpath(pathRow));
        int sz = rows.size();
        for (int i = 1; i < sz - 1; i++) {
            WebElement row = rows.get(i);

            String pathCol = ".//td[3]";
            WebElement element = row.findElement(By.xpath(pathCol));
            String text = element.getText();

            String pathCol2 = ".//td[4]";
            WebElement element2 = row.findElement(By.xpath(pathCol2));
            String text2 = element2.getText();

            String key = text.substring(0, 10);
            if (!refs.containsKey(key)) {
                refs.put(key, text2);
                System.out.println(text2);
                TelegramSender.sendMessage(text2);
            }
        }
    }

    public void clickBack() {
        String back = "//input[@value='Back']";
        boolean isBackDisplay = isElementPresent(By.xpath(back), driver);
        if (isBackDisplay) {
            WebElement element = driver.findElement(By.xpath(back));
            clickElement(element);
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

    private void clickElement(WebElement element) {
        element.click();
        sleep(1000);
    }

    public void refresh() {
        switchBackToRootFrame();
        switchFrame("mainFrame");
        String path = "//input[@value='Refresh']";
        WebElement element = driver.findElement(By.xpath(path));
        element.click();
    }

    public void quit() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void switchFrame(String id) {
        driver.switchTo().frame(id);
    }

    private void switchBackToRootFrame() {
        driver.switchTo().defaultContent();
    }

    private void jsClick(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }
}
