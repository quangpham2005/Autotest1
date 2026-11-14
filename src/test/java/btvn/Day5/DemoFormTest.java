package btvn.Day5;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.example.until.ReadExcel.readExcelData;

public class DemoFormTest {


    private static final String EXCEL_PATH = "dataDemo.xlsx";
    private static final String SHEET_NAME = "Sheet1";
    private static final String FORM_URL = "https://saucelabs.com/request-demo";

    // ===== LOCATOR (kiểm tra lại bằng F12 -> Inspect nếu không tìm thấy) =====
    // Đã xác nhận từ log lần chạy trước:
    private static final By EMAIL = By.id("Business-Email");
    private static final By COMPANY = By.id("Company");
    private static final By USE_CASE = By.id("Use-Case");
    private static final By COMMENTS = By.id("Comments");
    // Đoán theo quy luật đặt id của form (chưa xác nhận):
    // Dùng "bắt đầu bằng ... (không phân biệt hoa/thường)" để chịu được sai khác First-name / First-Name / First name
    private static final By FIRST_NAME = By.cssSelector("input[id^='first' i], input[name^='first' i]");
    private static final By LAST_NAME = By.cssSelector("input[id^='last' i], input[name^='last' i]");
    private static final By PHONE = By.cssSelector(
            "input[type='tel'], input[id*='phone' i], input[name*='phone' i]");
    private static final By COUNTRY = By.cssSelector("select[id^='country' i], select[name^='country' i]");
    private static final By STATE = By.cssSelector("select[id^='state' i], select[name^='state' i]");
    private static final By CONSENT = By.cssSelector("input[type='checkbox']");

    /** Chọn lựa chọn trong dropdown: ưu tiên khớp hoàn toàn, sau đó khớp "có chứa" (không phân biệt hoa/thường). */
    private static void selectByText(WebElement selectEl, String text) {
        Select select = new Select(selectEl);
        for (WebElement option : select.getOptions()) {
            if (option.getText().trim().equalsIgnoreCase(text.trim())) {
                select.selectByVisibleText(option.getText());
                return;
            }
        }
        for (WebElement option : select.getOptions()) {
            if (option.getText().toLowerCase().contains(text.trim().toLowerCase())) {
                select.selectByVisibleText(option.getText());
                return;
            }
        }
        StringBuilder available = new StringBuilder();
        for (WebElement option : select.getOptions()) {
            available.append("\n      - ").append(option.getText().trim());
        }
        throw new NoSuchElementException("Dropdown không có lựa chọn \"" + text
                + "\". Các lựa chọn hiện có:" + available);
    }

    public static void main(String[] args) throws InterruptedException {
        List<Map<String, String>> testData = readExcelData(EXCEL_PATH, SHEET_NAME);
        WebDriver driver;
        for (Map<String, String> row : testData) {
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            String email = row.get("Business Email");
            String firstName = row.get("First name");
            String lastName = row.get("Last name");
            String company = row.get("Company");
            String phone = row.get("Phone number");
            String country = row.get("Country");
            String state = row.get("State");
            String useCase = row.get("Use Case");
            String comments = row.get("Comments");
            String consent = row.get("Consent");

            driver.get(FORM_URL);

            // Form hiện dần các ô sau khi nhập email -> dùng wait cho từng ô
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL));
            emailField.sendKeys(email); // gõ email TRƯỚC -> các ô còn lại mới xuất hiện

            WebElement firstNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(FIRST_NAME));
            firstNameField.sendKeys(firstName);

            WebElement lastNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(LAST_NAME));
            lastNameField.sendKeys(lastName);

            WebElement companyField = wait.until(ExpectedConditions.visibilityOfElementLocated(COMPANY));
            companyField.sendKeys(company);

            // Country, State, Use Case là dropdown -> dùng Select
            // Chọn Country TRƯỚC Phone number (ô điện thoại có vẻ chỉ hiện sau khi chọn quốc gia)
            WebElement countryField = wait.until(ExpectedConditions.visibilityOfElementLocated(COUNTRY));
            selectByText(countryField, country);

            if (state != null && !state.isEmpty()) {
                WebElement stateField = wait.until(ExpectedConditions.visibilityOfElementLocated(STATE));
                selectByText(stateField, state);
            }

            WebElement phoneField = wait.until(ExpectedConditions.visibilityOfElementLocated(PHONE));
            phoneField.sendKeys(phone);

            WebElement useCaseField = wait.until(ExpectedConditions.visibilityOfElementLocated(USE_CASE));
            selectByText(useCaseField, useCase);

            WebElement commentsField = wait.until(ExpectedConditions.visibilityOfElementLocated(COMMENTS));
            commentsField.sendKeys(comments);

            // Checkbox đồng ý nhận thông tin (bị che bởi giao diện tuỳ biến -> click bằng JS)
            if (consent != null && (consent.equalsIgnoreCase("yes") || consent.equalsIgnoreCase("true"))) {
                WebElement box = driver.findElement(CONSENT);
                if (!box.isSelected()) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", box);
                }
            }

            // driver.findElement(By.cssSelector("input[type='submit']")).click();

            Thread.sleep(3000); // dừng 3 giây để bạn nhìn kết quả trước khi đóng
            driver.quit();
        }
    }
}