

import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCarSP {

    private static final int WAIT_MAX = 5;
    static WebDriver driver;

    @BeforeClass
    public static void setup() {
        /*########################### IMPORTANT ######################*/
        /*## Change this, according to your own OS and location of driver(s) ##*/
        /*############################################################*/
        System.setProperty("webdriver.chrome.driver", "D:\\drivers\\chromedriver.exe");
        System.setProperty("webdriver.gecko.driver", "D:\\drivers\\geckodriver.exe");

        //Reset Database
        com.jayway.restassured.RestAssured.given().get("http://localhost:7777/reset");
        driver = new ChromeDriver();
        driver.get("http://localhost:7777");
        
    }

//    @AfterClass
//    public static void tearDown() {
//        driver.quit();
//        //Reset Database 
//        com.jayway.restassured.RestAssured.given().get("http://localhost:7777/reset");
//    }

    
    @Test
    public void dOMConstructedTable() {
        //searching for tbody
       (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<Boolean>) (WebDriver d) -> {
      WebElement e = d.findElement(By.tagName("tbody"));
      List<WebElement> rows = e.findElements(By.tagName("tr"));
      Assert.assertThat(rows.size(), is(5));
      return true;
    });
    }
    
    @Test
    public void test2002Filter() {
        //searching for filter & sending key
        WebElement filter = (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<WebElement>) (WebDriver d) -> {
            return d.findElement(By.id("filter"));
        });
        filter.sendKeys("2002");

        //searching for tbody
        WebElement table = (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<WebElement>) (WebDriver d) -> {
            return d.findElement(By.tagName("tbody"));
        });

        //checking whehter number of rows is 2
        Assert.assertThat(table.findElements(By.tagName("tr")).size(), is(2));

    }
     @Test
    public void vTestClearFilter() {
        //searching for filter & sending key
        WebElement filter = (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<WebElement>) (WebDriver d) -> {
            return d.findElement(By.id("filter"));
        });

        filter.sendKeys(Keys.BACK_SPACE);

        //searching for tbody
        WebElement table = (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<WebElement>) (WebDriver d) -> {
            return d.findElement(By.tagName("tbody"));
        });

        //checking whether number of rows is 5
        Assert.assertThat(table.findElements(By.tagName("tr")).size(), is(5));
    }

    @Test
    public void testSorting() {
        //searching for sort button
        WebElement sortButton = (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<WebElement>) (WebDriver d) -> {
            return d.findElement(By.id("h_year"));
        });
        sortButton.click();

        //searching for tbody
        WebElement table = (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<WebElement>) (WebDriver d) -> {
            return d.findElement(By.tagName("tbody"));
        });

        //listing all tr
        List<WebElement> tableSorted = table.findElements(By.tagName("tr"));
        Assert.assertThat(tableSorted.get(0).findElements(By.tagName("td")).get(0).getText(), is("938"));
        Assert.assertThat(tableSorted.get(4).findElements(By.tagName("td")).get(0).getText(), is("940"));

    }
    
    
    @Test
     public void testEdit() {
        //searching for tbody
        List<WebElement> tableRows = (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<WebElement>) (WebDriver d) -> {
            return d.findElement(By.tagName("tbody"));
        }).findElements(By.tagName("tr"));
        
        
        
        
        WebElement editButton = null;
        for (int i = 0; i < tableRows.size(); i++) {
            if (tableRows.get(i).findElements(By.tagName("td")).get(0).getText().equalsIgnoreCase("938")) {
                editButton = tableRows.get(i);
                break;
            }
        }
        
        editButton.findElements(By.tagName("td")).get(7).findElements(By.tagName("a")).get(0).click();
        WebElement element = driver.findElement(By.id("description"));
        element.clear();
        element.sendKeys("Cool car");
        driver.findElement(By.id("save")).click();

        //searching for new tbody
        List<WebElement> tableRowsUpdated = (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<WebElement>) (WebDriver d) -> {
            return d.findElement(By.tagName("tbody"));
        }).findElements(By.tagName("tr"));

        String descriptionNew = "";
        for (int i = 0; i < tableRowsUpdated.size(); i++) {
            if (tableRowsUpdated.get(i).findElements(By.tagName("td")).get(0).getText().equalsIgnoreCase("938")) {
                descriptionNew = tableRowsUpdated.get(i).findElements(By.tagName("td")).get(5).getText();
                break;
            }
        }
        Assert.assertThat(descriptionNew, is("Nicerrrrr"));
    }
    
    
    
    
    
    
    
    
     
    @Test
    public void addNewT() {
        WebElement buttonNew = (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<WebElement>) (WebDriver d) -> {
            return d.findElement(By.id("new"));
        });
        buttonNew.click();
        driver.findElement(By.id("year")).sendKeys("2008");
        driver.findElement(By.id("registered")).sendKeys("2002-5-5");
        driver.findElement(By.id("make")).sendKeys("Kia");
        driver.findElement(By.id("model")).sendKeys("Rio");
        driver.findElement(By.id("description")).sendKeys("As new");
        driver.findElement(By.id("price")).sendKeys("31000");
        driver.findElement(By.id("save")).click();

        WebElement table = (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<WebElement>) (WebDriver d) -> {
            return d.findElement(By.tagName("tbody"));
        });
        Assert.assertThat(table.findElements(By.tagName("tr")).size(), is(6));

    }


}
