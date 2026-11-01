package Class.Day3;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class FindElement {
    static WebDriver driver = null;
    public static void main(String[] args) throws InterruptedException {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
        driver.navigate().to("https://saucelabs.com/request-demo");
//        WebElement element = driver.findElement(By.xpath("//span[contains(text(), 'Ship')]"));
//        WebElement element1 = driver.findElement(By.xpath("//h3[contains(text(), 'Request')]"));
//        WebElement element3 = driver.findElement(By.xpath("//span[contains(text(), 'Test')]"));
//        WebElement element4 = driver.findElement(By.xpath("//p[contains(text(), 'You')]"));
//        WebElement element5 = driver.findElement(By.xpath("//button[contains(text(), 'request a demo')]"));
//
//        System.out.println("element: " + element.getText());
//        System.out.println("element1: " + element1.getText());
//        System.out.println("element3: " + element3.getText());
//        System.out.println("element4: " + element4.getText());
//        System.out.println("element4: " + element5.getText());
        WebElement element3 = driver.findElement(By.className("form-input"));
        element3.sendKeys("abc@gmail.com");
        WebElement element2 = driver.findElement(By.name("Company"));
        element2.sendKeys("abc");
        WebElement element1 = driver.findElement(By.id("Comments"));
        element1.sendKeys("Auto");
        WebElement element4 = driver.findElement(By.className("form-button"));
        element4.click();
        List<WebElement> paragraphs = driver.findElements(By.tagName("p"));
        System.out.println("\nParagraphs:");
        for (WebElement paragraph : paragraphs) {
            System.out.println(paragraph.getText());
        }
//        String url = driver.getCurrentUrl();
//        System.out.println("String url la" + url);
//        driver.close();
        // driver.quit()   | dóng tất cả các browser

    }
}
