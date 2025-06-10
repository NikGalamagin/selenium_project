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

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();

    }

    @Test
    @DisplayName("Проверка соответствия текста кнопкам в блоке Вопросы о важном")
    public void testMainPageButtonsText() {

        for (int i = 0; i < scooterMainPage.buttons.length; i++) {
            WebElement button = driver.findElement(scooterMainPage.buttons[i]);
            String actualText = button.getText();
            String expectedText = scooterMainPage.expectedTexts[i];

            assertEquals(expectedText, actualText);
        }
    }

    @ParameterizedTest
    @MethodSource("combinedProvider")
    @DisplayName("e2e сценарий для разных браузеров и кнопок заказа")
    public void endToEndScenario(String buttonPosition, String browser) throws InterruptedException {
        if (browser.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        }

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

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
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