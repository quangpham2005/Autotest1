package Class.Day4;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

public class DoubleClickTest {
    public static void main(String[] args) throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        // Mở trang web
        driver.get("https://demo.guru99.com/test/simple_context_menu.html");
        Actions actions = new Actions(driver);
        WebElement button = driver.findElement(By.xpath("//button"));
        actions.doubleClick(button).perform();
        Thread.sleep(5000);
        String alertText = driver.switchTo().alert().getText();
        System.out.println("Alret text: " + alertText);
        driver.switchTo().alert().accept();
        Thread.sleep(5000);
        driver.quit();


    }
}
