package Class.Day4;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class DropdownTest {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://saucelabs.com/request-demo");
        WebElement  interesDropdown = driver.findElement(By.id("Use-Case"));
        Select select = new Select(interesDropdown);
        //select.selectByValue("Visual Testing");
        select.selectByIndex(5);
//        select.selectByVisibleText("Visual Testing");
    }
}
