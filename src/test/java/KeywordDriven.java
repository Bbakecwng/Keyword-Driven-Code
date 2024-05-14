
import static org.testng.Assert.assertEquals;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
 
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
 
import io.github.bonigarcia.wdm.WebDriverManager;
 
public class KeywordDriven {
 
	WebDriver driver;
 
    @Test
    public void executeTest() throws InterruptedException {
 
        try {
            FileInputStream file = new FileInputStream(new File("TestData.xlsx"));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheet("testdata");
 
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
 
                String keyword = row.getCell(1).getStringCellValue();
                String locator = row.getCell(2).getStringCellValue();
                String data = row.getCell(3).getStringCellValue();
 
                performAction(keyword.toLowerCase(), locator, data);
 
                // Log test steps and results using your desired reporting mechanism
            }
 
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
 
    private void performAction(String keyword, String locator, String data) throws InterruptedException {
        if (!keyword.isEmpty()) {
            switch (keyword) {
                case "open_browser":
                	WebDriverManager.chromedriver().setup();
                	driver = new ChromeDriver();
                	driver.manage().window().maximize();                	
                    break;
                    
                case "navigate":               	
                    driver.get(data);
                    break;
                    
                case "type":
                    driver.findElement(By.xpath(locator)).sendKeys(data);
                    break;
                    
                case "click":
                    driver.findElement(By.xpath(locator)).click();
                    break;
                    
                case "verify_text":
                	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
    				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h4[text()='Visa Overview']")));
                    String actualText = driver.findElement(By.xpath(locator)).getText();
                    assertEquals(actualText, data);
                    
                    break;
                    
                default:
                    System.out.println("Unhandled keyword encountered: " + keyword);
            }
        }
    }
    
    @AfterTest
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
 
}