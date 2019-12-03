package ua.hotline;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class TestHotline {

    @org.junit.Test
    public void Test1() {
        System.setProperty("webdriver.chrome.driver", "/chromedriver_win32 (1)/chromedriver.exe");
        ChromeDriver driver = new ChromeDriver();

        driver.get("https://hotline.ua/");

        WebElement element_enter = driver.findElement(By.xpath("//*[@id='searchbox']"));
        element_enter.sendKeys("Телевизор");

        WebElement element_enter2 = driver.findElement(By.xpath("//*[@id='doSearch']"));
        element_enter2.click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement element_enter3 = driver.findElement(By.xpath("/html//div[@id='page-search']/div[@class='cell-fixed-indent cell-md']//div[@class='viewbox']//div[@class='row']/ul//a"));
        element_enter3.click();

        WebElement type = driver.findElement(By.xpath("//*[@id=\"page-product\"]/div[6]/div/div[2]/div[2]/div[1]/div[2]/table/tbody/tr[2]/td[2]"));
        String text = type.getText();
        System.out.println(text);
        Assert.assertTrue(text.toLowerCase().contains("телевизор"));

        driver.quit();
    }

    @org.junit.Test
    public void Test2() {
        System.setProperty("webdriver.chrome.driver", "/chromedriver_win32 (1)/chromedriver.exe");
        ChromeDriver driver = new ChromeDriver();

        driver.get("https://hotline.ua/");

        WebElement element_enter = driver.findElement(By.xpath("//*[@id='searchbox']"));
        element_enter.sendKeys("Телевизор");

        WebElement element_enter2 = driver.findElement(By.xpath("//*[@id='doSearch']"));
        element_enter2.click();

        try {
            Thread.sleep(2000); // TODO Need to change
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Select sorting by price
        List<WebElement> options = new Select(driver.findElement(By.className("sorting-in")).findElement(By.className("field"))).getOptions();
        WebElement priceOption = null;
        for (int i = 0; i < options.size(); i++) {
            WebElement option = options.get(i);
            if (option.getText().toLowerCase().contains("цене")) {
                priceOption = option;
                break;
            }
        }
        Assert.assertNotNull("Sort by price option not found", priceOption);

        priceOption.click();

        try {
            Thread.sleep(3000); // TODO Need to change
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int currentPage = 1;
        while (currentPage <= 5) {
            // Iterate through all items on the page
            System.out.println("Current page: " + currentPage);
            List<WebElement> itemsList = driver.findElements(By.xpath("//*[@id=\"page-search\"]/div[2]/div/div[1]/div[3]/div/ul/li"));
            for (int i = 0; i < itemsList.size(); i++) {
                try {
                    WebElement priceItem = itemsList.get(i).findElement(By.className("price-md"));
                    String priceString = priceItem.findElement(By.className("value")).getText();
                    String pennyString = priceItem.findElement(By.className("penny")).getText();

                    String fullPriceString = priceString + pennyString;
                    System.out.println(fullPriceString);
                    // Replace all illegal symbols to parse string to float
                    fullPriceString = fullPriceString.replace(" ", "").replace(",", ".");
                    float fullPrice = Float.valueOf(fullPriceString);
                    Assert.assertTrue(fullPrice < 10000);
                } catch (NoSuchElementException e) {
                    System.out.println("No price available");
                    // Item has no price
                }
            }

            // Move to next page
            driver.findElement(By.xpath("//*[@id=\"page-search\"]/div[2]/div/div[2]/div[3]/a[1]")).click();
            currentPage++;
        }
        driver.quit();
    }
}
