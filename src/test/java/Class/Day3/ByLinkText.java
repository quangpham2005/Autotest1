package Class.Day3;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class ByLinkText {
    static WebDriver driver = null;
    public static void main(String[] args) throws InterruptedException {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
        driver.navigate().to("https://saucelabs.com/request-demo");
        //WebElement element = driver.findElement(By.linkText("Terms of Service"));
        WebElement element = driver.findElement(By.partialLinkText("Terms of"));
        element.click();
    }
}
