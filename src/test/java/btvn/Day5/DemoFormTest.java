package btvn.Day5;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
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

    // ===== LOCATOR =====
    private static final By EMAIL = By.id("Business-Email");
    private static final By COMPANY = By.id("Company");
    private static final By USE_CASE = By.id("Use-Case");
    private static final By COMMENTS = By.id("Comments");
    private static final By FIRST_NAME = By.cssSelector("input[id^='first' i], input[name^='first' i]");
    private static final By LAST_NAME = By.cssSelector("input[id^='last' i], input[name^='last' i]");
    private static final By PHONE = By.cssSelector(
            "input[type='tel'], input[id*='phone' i], input[name*='phone' i]");
    private static final By COUNTRY = By.cssSelector("select[id^='country' i], select[name^='country' i]");
    private static final By STATE = By.cssSelector("select[id^='state' i], select[name^='state' i]");
    private static final By CONSENT = By.cssSelector("input[type='checkbox']");

    // ===== HÀM HỖ TRỢ =====

    /** Chờ ô xuất hiện; không thấy thì trả về null (không ném lỗi). */
    private static WebElement waitFor(WebDriverWait wait, By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            return null;
        }
    }

    /** Điền ô văn bản. Giá trị trống -> bỏ qua. Trả về false nếu không thấy ô. */
    private static boolean typeText(WebDriverWait wait, By locator, String value, String name) {
        if (value == null || value.isEmpty()) {
            System.out.println("  [BỎ QUA]       " + name + " (để trống)");
            return true;
        }
        WebElement el = waitFor(wait, locator);
        if (el == null) {
            System.out.println("  [KHÔNG THẤY Ô] " + name + " (form chưa hiện ô này)");
            return false;
        }
        el.clear();
        el.sendKeys(value);
        System.out.println("  [OK]           " + name + " = " + value);
        return true;
    }

    /** Chọn dropdown. Giá trị trống -> bỏ qua. Trả về false nếu lỗi. */
    private static boolean selectDropdown(WebDriverWait wait, By locator, String value, String name) {
        if (value == null || value.isEmpty()) {
            System.out.println("  [BỎ QUA]       " + name + " (để trống)");
            return true;
        }
        WebElement el = waitFor(wait, locator);
        if (el == null) {
            System.out.println("  [KHÔNG THẤY Ô] " + name + " (form chưa hiện ô này)");
            return false;
        }
        try {
            selectByText(el, value);
            System.out.println("  [OK]           " + name + " = " + value);
            return true;
        } catch (NoSuchElementException e) {
            System.out.println("  [LỖI]          " + name + ": " + e.getMessage());
            return false;
        }
    }

    private static void tickConsent(WebDriver driver, String value) {
        boolean wanted = value != null && (value.equalsIgnoreCase("yes")
                || value.equalsIgnoreCase("true") || value.equals("1") || value.equalsIgnoreCase("x"));
        if (!wanted) {
            System.out.println("  [BỎ QUA]       Consent (không tick)");
            return;
        }
        try {
            WebElement box = driver.findElement(CONSENT);
            if (!box.isSelected()) {
                // Checkbox bị che bởi giao diện tuỳ biến -> click bằng JS
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", box);
            }
            System.out.println("  [OK]           Consent = tick");
        } catch (Exception e) {
            System.out.println("  [LỖI]          Consent: không tick được");
        }
    }

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

    // ===== MAIN =====
    public static void main(String[] args) throws InterruptedException {
        List<Map<String, String>> testData = readExcelData(EXCEL_PATH, SHEET_NAME);
        int index = 1;

        for (Map<String, String> row : testData) {
            String email = row.get("Business Email");
            System.out.println("=== Case " + index++ + ": "
                    + (email == null || email.isEmpty() ? "<email trống>" : email) + " ===");

            WebDriver driver = new ChromeDriver();
            try {
                driver.manage().window().maximize();
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
                driver.get(FORM_URL);

                // 1) Email trước: các ô còn lại chỉ hiện sau khi nhập email hợp lệ
                typeText(wait, EMAIL, email, "Business Email");

                // Kiểm tra form có hiện các ô tiếp theo không (email trống / email cá nhân sẽ bị chặn)
                if (waitFor(wait, FIRST_NAME) == null) {
                    System.out.println("  -> Form KHÔNG hiện các ô tiếp theo (email trống hoặc bị từ chối). Bỏ qua case này.");
                    continue; // finally vẫn chạy driver.quit()
                }

                // 2) Các ô còn lại; ô trống trong Excel sẽ được bỏ qua
                typeText(wait, FIRST_NAME, row.get("First name"), "First name");
                typeText(wait, LAST_NAME, row.get("Last name"), "Last name");
                typeText(wait, COMPANY, row.get("Company"), "Company");

                // Country/State TRƯỚC Phone (ô điện thoại chỉ hiện sau khi chọn quốc gia)
                selectDropdown(wait, COUNTRY, row.get("Country"), "Country");
                selectDropdown(wait, STATE, row.get("State"), "State");
                typeText(wait, PHONE, row.get("Phone number"), "Phone number");

                selectDropdown(wait, USE_CASE, row.get("Use Case"), "Use Case");
                typeText(wait, COMMENTS, row.get("Comments"), "Comments");
                tickConsent(driver, row.get("Consent"));

                // Bỏ comment dòng dưới nếu muốn GỬI form thật:
                // driver.findElement(By.cssSelector("input[type='submit']")).click();

                Thread.sleep(2000); // dừng 2 giây để nhìn kết quả trước khi đóng
            } finally {
                driver.quit(); // luôn đóng trình duyệt dù case có lỗi
            }
        }
    }
}