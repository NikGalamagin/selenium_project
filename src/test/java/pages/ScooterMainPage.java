package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ScooterMainPage {

    private final WebDriver driver;
    public WebElement orderButtonUpper, orderButtonBottom;
    public By ButtonOne = By.xpath("//div[@id='accordion__heading-0' and @class='accordion__button']");
    public ScooterMainPage(WebDriver driver) {
        this.driver = driver;
        orderButtonUpper = driver.findElement(By.xpath("//button[@class='Button_Button__ra12g' and text()='Заказать']"));
        orderButtonBottom = driver.findElement(By.xpath("//button[@class='Button_Button__ra12g' and text()='Заказать']"));
    }


}