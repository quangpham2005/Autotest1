package Class.Day4;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

public class RadioButton {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
//        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        // Mở trang web
        driver.get("https://demo.guru99.com/test/radio.html");
        WebElement radioButton = driver.findElement(By.id("vfb-7-1"));
        radioButton.click();
        System.out.println("Radio: " + radioButton.getAttribute("value"));
        System.out.println("Radio isSlect" + radioButton.isSelected());
    }
}
