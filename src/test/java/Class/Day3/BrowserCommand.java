package Class.Day3;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class BrowserCommand {
    static WebDriver driver = null;
    public static void main(String[] args) throws InterruptedException {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
        driver.navigate().to("https://saucelabs.com/request-demo");
        Thread.sleep(5000);
        driver.navigate().forward();
        Thread.sleep(5000);
        driver.navigate().back();
        Thread.sleep(5000);
        driver.navigate().refresh();
        String url = driver.getCurrentUrl();
        System.out.println("String url la" + url);
        driver.close();
        // driver.quit()   | dóng tất cả các browser

    }
}
