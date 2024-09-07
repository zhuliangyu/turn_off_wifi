package org.example;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HighlightElement {
    // Method to highlight the element
    public static void highlightElement(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        // Use JavaScript to change the element's style (add a red border and yellow background)
        js.executeScript("arguments[0].setAttribute('style', 'border: 2px solid red; background: yellow;');", element);
    }
}
