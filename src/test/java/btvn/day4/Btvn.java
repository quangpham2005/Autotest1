package btvn.day4;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Btvn {
    static WebDriver driver;
    static WebDriverWait wait;

    static void check(String message, boolean ok) {
        System.out.println((ok ? "[PASS] " : "[FAIL] ") + message);
    }
    static double money(String text) {          // "$29.99" / "Item total: $39.98" -> số
        return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
    }

    public static void main(String[] args) {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try{
            //Step1
            driver.get("https://www.saucedemo.com/");
            driver.findElement(By.xpath("//input[@id = 'user-name']")).sendKeys("standard_user");
            driver.findElement(By.xpath("//input[@id = 'password']")).sendKeys("secret_sauce");
            driver.findElement(By.xpath("//input[@value = 'Login']")).click();
            //Step2
            driver.findElement(By.xpath("//select")).click();
            driver.findElement(By.xpath("//option[@value = 'lohi']")).click();
            //Step3
//            driver.findElement(By.xpath("//div[text() = 'Sauce Labs Onesie' ]/ancestor::div[contains(@class, 'inventory_item_description')]/descendant::button")).click();
//            driver.findElement(By.xpath("//div[text() = 'Sauce Labs Bike Light' ]/ancestor::div[contains(@class, 'inventory_item_description')]/descendant::button")).click();
            List<WebElement> items = driver.findElements(By.className("inventory_item"));
            List<String> names = new ArrayList<>();
            List<String> prices = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                WebElement item = items.get(i);
                names.add(item.findElement(By.className("inventory_item_name")).getText());
                prices.add(item.findElement(By.className("inventory_item_price")).getText());
                item.findElement(By.tagName("button")).click();
            }
            String badge = driver.findElement(By.className("shopping_cart_badge")).getText();
            System.out.println("Giỏ hàng hiển thị số: " + badge);
            check("Icon giỏ hàng hiển thị số 2", badge.equals("2"));
            //Step 4
            driver.findElement(By.xpath("//div[@id='shopping_cart_container']")).click();

            driver.findElement(By.id("checkout")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first-name")));
            driver.findElement(By.id("first-name")).sendKeys("Duy");
            driver.findElement(By.id("last-name")).sendKeys("Nguyen");
            driver.findElement(By.id("postal-code")).sendKeys("100000");
            System.out.println("Step 5: Đã nhập First name, Last name, Zip code");

            //Step 6
            driver.findElement(By.id("continue")).click();
            wait.until(ExpectedConditions.urlContains("checkout-step-two"));
            System.out.println("Step 6: Đã Continue sang trang Overview");

            List<WebElement> overview = driver.findElements(By.className("cart_item"));
            boolean overviewOk = overview.size() == 2;
            for (int i = 0; i < overview.size() && overviewOk; i++) {
                String q = overview.get(i).findElement(By.className("cart_quantity")).getText();
                String n = overview.get(i).findElement(By.className("inventory_item_name")).getText();
                String p = overview.get(i).findElement(By.className("inventory_item_price")).getText();
                overviewOk = q.equals("1") && names.contains(n)
                        && prices.get(names.indexOf(n)).equals(p);
            }
            check("Description hiển thị đúng 2 sản phẩm với số lượng, tên và giá tiền đúng", overviewOk);

            // Shipping Information = giá trị thứ 2 trong .summary_value_label (1 = Payment)
            String shipping = driver.findElements(By.className("summary_value_label")).get(1).getText();
            check("Shipping Information hiển thị đúng \"Free Pony Express Delivery!\" (thực tế: "
                    + shipping + ")", shipping.equals("Free Pony Express Delivery!"));

            double expectedSub = money(prices.get(0)) + money(prices.get(1));
            String subText = driver.findElement(By.className("summary_subtotal_label")).getText();
            check("Item total hiển thị đúng tổng tiền 2 sản phẩm (" + subText + ", kỳ vọng $"
                            + String.format("%.2f", expectedSub) + ")",
                    Math.abs(money(subText) - expectedSub) < 0.001);

            double tax = money(driver.findElement(By.className("summary_tax_label")).getText());
            String totalText = driver.findElement(By.className("summary_total_label")).getText();
            check("Total hiển thị đúng Item total + Tax (" + totalText + ", kỳ vọng $"
                            + String.format("%.2f", expectedSub + tax) + ")",
                    Math.abs(money(totalText) - (expectedSub + tax)) < 0.001);

            check("Button Finish hiển thị", driver.findElement(By.id("finish")).isDisplayed());

            //Step 7
            driver.findElement(By.id("finish")).click();
            wait.until(ExpectedConditions.urlContains("checkout-complete"));
            System.out.println("Step 7: Đã click Finish");

            String title = driver.findElement(By.className("title")).getText();
            check("Hiển thị \"Checkout: Complete!\" (thực tế: " + title + ")",
                    title.equals("Checkout: Complete!"));

            String header = driver.findElement(By.className("complete-header")).getText();
            check("Hiển thị \"Thank you for your order!\" (thực tế: " + header + ")",
                    header.equals("Thank you for your order!"));

            String text = driver.findElement(By.className("complete-text")).getText();
            check("Hiển thị thông báo \"Your order has been dispatched, and will arrive just as fast as the pony can get there!\"",
                    text.equals("Your order has been dispatched, and will arrive just as fast as the pony can get there!"));

            WebElement back = driver.findElement(By.id("back-to-products"));
            check("Hiển thị button Back Home", back.isDisplayed() && back.getText().equals("Back Home"));

        }catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
           driver.quit();
        }
    }
}
