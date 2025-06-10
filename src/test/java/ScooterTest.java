import java.time.Duration;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.OrderPage;
import pages.ScooterMainPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScooterTest {
    private WebDriver driver;
    private ScooterMainPage scooterMainPage;

    static Stream<Object[]> combinedProvider() {
        String[] browsers = {"chrome", "firefox"};
        String[] buttonPositions = {"upper", "bottom"};

        return Stream.of(browsers)
                .flatMap(browser -> Stream.of(buttonPositions)
                        .map(button -> new Object[]{button, browser}));
    }

    static Stream<Object[]> buttonTextsProvider() {
        return Stream.of(
                new Object[]{By.xpath("//*[@id='accordion__heading-0']"), "Сколько это стоит? И как оплатить?"},
                new Object[]{By.xpath("//*[@id='accordion__heading-1']"), "Хочу сразу несколько самокатов! Так можно?"},
                new Object[]{By.xpath("//*[@id='accordion__heading-2']"), "Как рассчитывается время аренды?"},
                new Object[]{By.xpath("//*[@id='accordion__heading-3']"), "Можно ли заказать самокат прямо на сегодня?"},
                new Object[]{By.xpath("//*[@id='accordion__heading-4']"), "Можно ли продлить заказ или вернуть самокат раньше?"},
                new Object[]{By.xpath("//*[@id='accordion__heading-5']"), "Вы привозите зарядку вместе с самокатом?"},
                new Object[]{By.xpath("//*[@id='accordion__heading-6']"), "Можно ли отменить заказ?"},
                new Object[]{By.xpath("//*[@id='accordion__heading-7']"), "Я живу за МКАДом, привезёте?"}
        );
    }

    @BeforeEach
    public void setUp() {
        if (driver == null) {
            driver = new ChromeDriver();
        }
    }

    @ParameterizedTest
    @MethodSource("buttonTextsProvider")
    @DisplayName("Проверка соответствия текста кнопкам в блоке Вопросы о важном")
    public void testMainPageButtonsText(By buttonLocator, String expectedText) {
        driver.get("https://qa-scooter.praktikum-services.ru/");
        WebElement button = driver.findElement(buttonLocator);
        String actualText = button.getText();
        assertEquals(expectedText, actualText);
    }

    @ParameterizedTest
    @MethodSource("combinedProvider")
    @DisplayName("e2e сценарий для разных браузеров и кнопок заказа")
    public void endToEndScenario(String buttonPosition, String browser) throws InterruptedException {
        if (driver != null) {
            driver.quit();
        }
        driver = "chrome".equalsIgnoreCase(browser) ? new ChromeDriver() : new FirefoxDriver();

        driver.get("https://qa-scooter.praktikum-services.ru/");
        scooterMainPage = new ScooterMainPage(driver);

        OrderPage orderPage = new OrderPage(driver);

        WebElement orderButton;
        if ("upper".equals(buttonPosition)) {
            orderButton = scooterMainPage.orderButtonUpper;
        } else {
            orderButton = scooterMainPage.orderButtonBottom;
        }

        orderButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        WebElement nameInputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='text' and @placeholder='* Имя']")));
        orderPage.fillOrderForm("Коля", "Пупкин", "Кошково 15", "89045551122", "Побыстрее", "Сокольники");
        orderPage.waitForLoadProfileData();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}