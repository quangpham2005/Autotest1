package Class.Day5;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.until.ReadExcel.readExcelData;

public class LoginPageTest {

    private static final String EXCEL_PATH = "dataLogin.xlsx";
    private static final String SHEET_NAME = "Sheet1";
    private static final String LOGIN_URL = "https://www.saucedemo.com/";
    public static void main(String[] args) {
        List<Map<String, String>> testData = readExcelData(EXCEL_PATH, SHEET_NAME);
        WebDriver driver;
        for (Map<String, String> row : testData) {
            driver = new ChromeDriver();
            String user = row.get("Username");
            String pass = row.get("Password");
            driver.get(LOGIN_URL);
            WebElement userField = driver.findElement(By.id("user-name"));
            WebElement passField = driver.findElement(By.id("password"));
            WebElement loginBtn = driver.findElement(By.id("login-button"));
            userField.sendKeys(user);
            passField.sendKeys(pass);
            loginBtn.click();
            driver.quit();
        }
    }

}
