package Class.Day4;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CheckboxTest {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
//        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        // Mở trang web
        driver.get("https://demo.guru99.com/test/radio.html");
        List<WebElement> checkboxes = new ArrayList<>();
        checkboxes.add(driver.findElement(By.id("vfb-6-0")));
        checkboxes.add(driver.findElement(By.id("vfb-6-1")));

        for(WebElement checkbox : checkboxes) {
            checkbox.click();
            System.out.println("Checkbox: " + checkbox.getAttribute("value"));
            System.out.println("Is selected" + checkbox.isSelected());
        }
    }
}
