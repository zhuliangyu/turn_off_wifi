package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;

/**
 * todo: login and git
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!2" );

        ArrayList<String> names = new ArrayList<>();
        names.add("alex ipad");
        names.add("Dining-room-TV");
        names.add("Alina-new-DESKTOP-Q1JVARU");
        names.add("Alex-iPad");

        // Setup ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();


        WebDriver driver = new ChromeDriver();

        driver.get("https://openwrt.enbut.com/mobile_access_net.asp");
        driver.switchTo().frame("net_access_list");

        names.forEach((machine_name) -> {
            try {
                turn_off_machine(driver, machine_name );
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        try {
            // Pause for 5000 milliseconds for checking result(5 seconds)
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Close the browser
        driver.quit();
    }

    public static void turn_off_machine(WebDriver driver, String machine_name) throws InterruptedException {
        driver.get("https://openwrt.enbut.com/mobile_access_net.asp");
        driver.switchTo().frame("net_access_list");

        // Create an explicit wait for up to 4 seconds
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));

        String xpath_expression = String.format("//td[contains(text(), '%s')]", machine_name);

        try {
            WebElement element_machine = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath_expression)));
            System.out.println(STR."\{machine_name} is found!");
//            HighlightElement.highlightElement(driver, element_machine);

            // Wait until element_machine is visible or intractable (optional, depending on your needs)
            wait.until(ExpectedConditions.visibilityOf(element_machine)); // Wait until visible
            wait.until(ExpectedConditions.elementToBeClickable(element_machine)); // Wait until clickable (if you need to click it)

            WebElement parentElement = element_machine.findElement(By.xpath("./../../../../.."));
            HighlightElement.highlightElement(driver, parentElement);

            WebElement element_toggle_with_open_status = parentElement.findElement(By.xpath(".//td[@id='access_enable' and @title='允许上网']"));
            element_toggle_with_open_status.click();
            System.out.println(STR."Turn off \{machine_name} wifi!");
        }catch (NoSuchElementException e) {
            // Handle case where the element is not found
            System.out.println(STR."\{machine_name} wifi has been closed. No aciton!");
        }
        Thread.sleep(5000);
    }
}
