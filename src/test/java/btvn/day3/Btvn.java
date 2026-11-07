package btvn.day3;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.util.HashMap;
import java.util.Map;

public class Btvn {

    static final String URL = "https://saucelabs.com/request-demo";
    static final boolean SUBMIT_REAL_FORM = false;

    // --- Locator ---
    static final By EMAIL     = By.xpath("//input[contains(@id, 'Email')]");
    static final By COMPANY   = By.id("Company");
    static final By USE_CASE  = By.id("Use-Case");
    static final By COMMENTS  = By.id("Comments");
    static final By SUBMIT    = By.xpath("//button[@type='submit']");

    static WebDriver driver;
    static int passed = 0;
    static int failed = 0;

    public static void main(String[] args) {
        driver = new ChromeDriver(createOptions());
        driver.manage().window().maximize();

        try {
            runTest("TC01 - Happy case: Các trường thông tin hợp lệ", () -> tc01_happyCase());
            runTest("TC02 - Unhappy case: Bỏ trống Company", () -> tc02_emptyCompany());
            runTest("TC03 - Unhappy case: Bỏ trống Business Email", () -> tc03_emptyEmail());
            runTest("TC04 - Unhappy case: Email sai định dạng (thiếu @)", () -> tc04_invalidEmailWithoutAt());
            runTest("TC05 - Unhappy case: Bỏ trống tất cả các trường required", () -> tc05_emptyAllRequired());
            runTest("TC06 - Kiểm tra giá trị mặc định của Use Case", () -> tc06_useCaseDefault());
            runTest("TC07 - Unhappy case: Email thiếu domain (abc@)", () -> tc07_emailWithoutDomain());
            runTest("TC08 - Boundary value: Company vượt quá 256 ký tự", () -> tc08_companyMaxLength());

        } finally {
            driver.quit();
        }

        System.out.println("=====================================");
        System.out.println("TỔNG KẾT: PASS = " + passed + ", FAIL = " + failed);
    }

    static void tc01_happyCase() throws InterruptedException {
        openForm();
        fillForm("a@fpt.com", "Company", "AI Test Authoring", "Automation");

        verify(validationMessage(EMAIL).isEmpty(), "Email đang báo lỗi");
        verify(validationMessage(COMPANY).isEmpty(), "Company đang báo lỗi");
        verify(driver.findElement(SUBMIT).isEnabled(), "Nút submit đang bị khóa");

        if (SUBMIT_REAL_FORM) {
            driver.findElement(SUBMIT).click();
            Thread.sleep(3000);
        } else {
            System.out.println("    (Bỏ qua bước click submit vì SUBMIT_REAL_FORM = false)");
        }
    }

    static void tc02_emptyCompany() throws InterruptedException {
        openForm();
        enterEmail("a@fpt.com");
        clickSubmit();

        verifyBlockedBy(COMPANY, "Please fill out this field.");
    }

    static void tc03_emptyEmail() throws InterruptedException {
        openForm();
        driver.findElement(COMPANY).sendKeys("Company");
        clickSubmit();

        verifyBlockedBy(EMAIL, "Please fill out this field.");
    }

    static void tc04_invalidEmailWithoutAt() throws InterruptedException {
        openForm();
        enterEmail("abc");
        driver.findElement(COMPANY).sendKeys("Company");
        clickSubmit();

        String message = validationMessage(EMAIL);
        verify(!message.isEmpty(), "Email sai định dạng nhưng không báo lỗi");
    }

    static void tc05_emptyAllRequired() throws InterruptedException {
        openForm();
        clickSubmit();

        verifyBlockedBy(EMAIL, "Please fill out this field.");
    }

    static void tc06_useCaseDefault() throws InterruptedException {
        openForm();
        Select useCaseSelect = new Select(driver.findElement(USE_CASE));
        String defaultOption = useCaseSelect.getFirstSelectedOption().getText();
        verify(defaultOption.equals("AI Test Authoring"), "Giá trị mặc định sai: " + defaultOption);
        verify(useCaseSelect.getOptions().size() > 0, "Danh sách Use Case bị trống");
    }

    static void tc07_emailWithoutDomain() throws InterruptedException {
        openForm();
        enterEmail("abc@");
        driver.findElement(COMPANY).sendKeys("Company");
        clickSubmit();

        String message = validationMessage(EMAIL);
        verify(!message.isEmpty(), "Email thiếu domain nhưng không hiện lỗi");
    }

    static void tc08_companyMaxLength() throws InterruptedException {
        openForm();
        String longText = "A".repeat(300); // Chuỗi 300 ký tự
        WebElement companyBox = driver.findElement(COMPANY);
        companyBox.sendKeys(longText);

        int actualLength = companyBox.getAttribute("value").length();
        verify(actualLength == 256, "Độ dài thực tế là " + actualLength + ", mong đợi tối đa 256");
    }

    static ChromeOptions createOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=en-US");
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        return options;
    }

    static void openForm() throws InterruptedException {
        driver.get(URL);
        Thread.sleep(3000);
    }

    static void enterEmail(String email) throws InterruptedException {
        WebElement emailBox = driver.findElement(EMAIL);
        emailBox.clear();
        emailBox.sendKeys(email);
        driver.findElement(COMPANY).click();
        Thread.sleep(1000);
    }

    static void fillForm(String email, String company, String useCaseText, String comments) throws InterruptedException {
        if (!email.isEmpty()) {
            enterEmail(email);
        }
        if (!company.isEmpty()) {
            driver.findElement(COMPANY).sendKeys(company);
        }
        if (!useCaseText.isEmpty()) {
            new Select(driver.findElement(USE_CASE)).selectByVisibleText(useCaseText);
        }
        if (!comments.isEmpty()) {
            driver.findElement(COMMENTS).sendKeys(comments);
        }
    }

    static void clickSubmit() throws InterruptedException {
        driver.findElement(SUBMIT).click();
        Thread.sleep(1000);
    }

    static String validationMessage(By locator) {
        return driver.findElement(locator).getAttribute("validationMessage");
    }

    static boolean isStillOnForm() {
        return driver.getCurrentUrl().contains("/request-demo");
    }

    static void verifyBlockedBy(By field, String expectedMessage) {
        verify(isStillOnForm(), "Form lẽ ra không được gửi đi.");
        String actual = validationMessage(field);
        verify(actual.contains(expectedMessage),
                "Thông báo lỗi không khớp. Mong đợi chứa: '" + expectedMessage + "' - Thực tế: '" + actual + "'");
    }

    static void verify(boolean condition, String failMessage) {
        if (!condition) {
            throw new AssertionError(failMessage);
        }
    }


    interface TestCase {
        void run() throws Exception;
    }

    static void runTest(String name, TestCase testCase) {
        try {
            testCase.run();
            passed++;
            System.out.println("PASS  " + name);
        } catch (AssertionError e) {
            failed++;
            System.out.println("FAIL  " + name + "\n      -> " + e.getMessage());
        } catch (Exception e) {
            failed++;
            System.out.println("ERROR " + name + "\n      -> " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
}
