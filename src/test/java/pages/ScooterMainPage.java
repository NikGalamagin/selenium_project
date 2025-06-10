package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ScooterMainPage {

    public WebElement orderButtonUpper, orderButtonBottom;
    private final WebDriver driver;

    public ScooterMainPage(WebDriver driver) {
        this.driver = driver;
        orderButtonUpper = driver.findElement(By.xpath("//button[@class='Button_Button__ra12g' and text()='Заказать']"));
        orderButtonBottom = driver.findElement(By.xpath("//button[@class='Button_Button__ra12g' and text()='Заказать']"));
    }
}