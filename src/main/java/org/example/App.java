package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        ArrayList<String> names = new ArrayList<>();

        String fileName = "/ssid.txt";
        String admin_portal = "http://192.168.124.1/mobile_access_net.asp";
        // Try-with-resources to automatically close the resources
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(App.class.getResourceAsStream(fileName)))) {

            String line;
            // Read the file line by line
            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
                names.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println( "Hello World!2" );

        // Setup ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();


        WebDriver driver = new ChromeDriver();

        driver.get(admin_portal);
        driver.switchTo().frame("net_access_list");

        names.forEach((machine_name) -> {
            try {
                turn_off_machine(driver, machine_name, admin_portal );
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

    public static void turn_off_machine(WebDriver driver, String machine_name, String admin_portal) throws InterruptedException {
        driver.get(admin_portal);
        driver.switchTo().frame("net_access_list");

        // Create an explicit wait for up to 4 seconds
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));

        String xpath_expression = String.format("//td[text()='%s']", machine_name);

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
