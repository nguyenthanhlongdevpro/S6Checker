package org.s3979.tool.sgd6;

import com.twocaptcha.TwoCaptcha;
import com.twocaptcha.captcha.Normal;
import org.openqa.selenium.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageCaptchaSolver {

    static String API_KEY = "82c588aae121b49ddc631b3c7499719d";

    public static String run(WebDriver driver, WebElement captchaImage) throws Exception {

        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        BufferedImage fullImg = ImageIO.read(screenshot);

        Point point = captchaImage.getLocation();
        int width = captchaImage.getSize().getWidth();
        int height = captchaImage.getSize().getHeight();

        BufferedImage captcha = fullImg.getSubimage(point.getX(), point.getY(), width, height);

        File captchaFile = new File("captcha.png");
        ImageIO.write(captcha, "png", captchaFile);

        TwoCaptcha solver = new TwoCaptcha(API_KEY);
        Normal normal = new Normal("captcha.png");
        try {
            solver.solve(normal);
            String code = normal.getCode();
            System.out.println("Captcha solved: " + code);
            return code;
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }

        return null;
    }
}
