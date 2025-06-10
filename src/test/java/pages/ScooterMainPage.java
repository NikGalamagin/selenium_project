package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ScooterMainPage {
    public final By howMuchItCostButton = By.xpath("//*[@id='accordion__heading-0']");
    public final By multiplyScooterButton = By.xpath("//*[@id='accordion__heading-1']");
    public final By arendaTimeButton = By.xpath("//*[@id='accordion__heading-2']");
    public final By todayRentButton = By.xpath("//*[@id='accordion__heading-3']");
    public final By returnScooterButton = By.xpath("//*[@id='accordion__heading-4']");
    public final By rechargeButton = By.xpath("//*[@id='accordion__heading-5']");
    public final By cancelOrderButton = By.xpath("//*[@id='accordion__heading-6']");
    public final By moscowDistrictButton = By.xpath("//*[@id='accordion__heading-7']");
    public final String[] expectedTexts = {
            "Сколько это стоит? И как оплатить?",
            "Хочу сразу несколько самокатов! Так можно?",
            "Как рассчитывается время аренды?",
            "Можно ли заказать самокат прямо на сегодня?",
            "Можно ли продлить заказ или вернуть самокат раньше?",
            "Вы привозите зарядку вместе с самокатом?",
            "Можно ли отменить заказ?",
            "Я живу за МКАДом, привезёте?"
    };

    public WebElement orderButtonUpper, orderButtonBottom;
    public By[] buttons = {
            howMuchItCostButton,
            multiplyScooterButton,
            arendaTimeButton,
            todayRentButton,
            returnScooterButton,
            rechargeButton,
            cancelOrderButton,
            moscowDistrictButton
    };
    private final WebDriver driver;

    public ScooterMainPage(WebDriver driver) {
        this.driver = driver;
        orderButtonUpper = driver.findElement(By.xpath("//button[@class='Button_Button__ra12g' and text()='Заказать']"));
        orderButtonBottom = driver.findElement(By.xpath("//button[@class='Button_Button__ra12g' and text()='Заказать']"));
    }
}