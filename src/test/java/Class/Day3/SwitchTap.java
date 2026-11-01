package Class.Day3;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;

public class SwitchTap {
    static WebDriver driver = null;
    public static void main(String[] args) throws InterruptedException {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://saucelabs.com/request-demo");
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get("https://google.com");
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get("https://facebook.com");
        Thread.sleep(10000);
        driver.switchTo().window(tabs.get(0));


    }
}
