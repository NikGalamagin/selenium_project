package pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OrderPage {
    private final WebDriver driver;
    private final By StatusButton = By.xpath("//button[text()='Посмотреть статус']");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
    }

    private WebElement getNameInput() {
        return driver.findElement(By.xpath("//input[@type='text' and @placeholder='* Имя']"));
    }

    private WebElement getSurnameInput() {
        return driver.findElement(By.xpath("//input[@type='text' and @placeholder='* Фамилия']"));
    }

    private WebElement getAddressInput() {
        return driver.findElement(By.xpath("//input[@type='text' and @placeholder='* Адрес: куда привезти заказ']"));
    }

    private WebElement getMetroInput() {
        return driver.findElement(By.xpath("//input[@class='select-search__input' and @placeholder='* Станция метро']"));
    }

    private WebElement getPhoneInput() {
        return driver.findElement(By.xpath("//input[@type='text' and @placeholder='* Телефон: на него позвонит курьер']"));
    }

    private WebElement getNextElement() {
        return driver.findElement(By.xpath("//button[contains(@class, 'Button_Button__ra12g') and contains(@class, 'Button_Middle__1CSJM') and text()='Далее']"));
    }

    private WebElement getDeliveryDateInput() {
        return driver.findElement(By.xpath("//input[@placeholder='* Когда привезти самокат']"));
    }

    private WebElement getRentalPeriodElement() {
        return driver.findElement(By.xpath("//div[@class='Dropdown-placeholder'][text()='* Срок аренды']"));
    }

    private WebElement getOptionElement() {
        return driver.findElement(By.xpath("//div[@class='Dropdown-option' and text()='сутки']"));
    }

    private WebElement getCheckboxElement() {
        return driver.findElement(By.xpath("//input[@id='black' and @type='checkbox']"));
    }

    private WebElement getCommentElement() {
        return driver.findElement(By.xpath("//input[@placeholder='Комментарий для курьера']"));
    }

    private WebElement getOrderElement() {
        return driver.findElement(By.xpath("//button[contains(@class, 'Button_Button__ra12g') and contains(@class, 'Button_Middle__1CSJM') and text()='Заказать']"));
    }

    private WebElement getAgreeElement() {
        return driver.findElement(By.xpath("//button[text()='Да']"));
    }

    public void waitForLoadProfileData() {
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(driver -> (
                driver.findElement(StatusButton).getText() != null
                        && !driver.findElement(StatusButton).getText().isEmpty()
        ));
    }


    public void fillOrderForm(String name, String surname, String address, String phone, String comment, String metro) {
        getNameInput().sendKeys(name);
        getSurnameInput().sendKeys(surname);
        getAddressInput().sendKeys(address);
        getMetroInput().click();
        getMetroInput().sendKeys(metro);
        getMetroInput().sendKeys(Keys.ARROW_DOWN);
        getMetroInput().sendKeys(Keys.ENTER);
        getPhoneInput().sendKeys(phone);
        getNextElement().click();
        getDeliveryDateInput().click();
        getDeliveryDateInput().sendKeys(Keys.ENTER);
        getRentalPeriodElement().click();
        getOptionElement().click();
        getCheckboxElement().click();
        getCommentElement().sendKeys(comment);
        getOrderElement().click();
        getAgreeElement().click();

    }


}