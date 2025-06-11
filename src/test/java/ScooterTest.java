import java.time.Duration;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
                new Object[]{0, By.xpath("//*[@id='accordion__heading-0']"), "Сколько это стоит? И как оплатить?"},
                new Object[]{1, By.xpath("//*[@id='accordion__heading-1']"), "Хочу сразу несколько самокатов! Так можно?"},
                new Object[]{2, By.xpath("//*[@id='accordion__heading-2']"), "Как рассчитывается время аренды?"},
                new Object[]{3, By.xpath("//*[@id='accordion__heading-3']"), "Можно ли заказать самокат прямо на сегодня?"},
                new Object[]{4, By.xpath("//*[@id='accordion__heading-4']"), "Можно ли продлить заказ или вернуть самокат раньше?"},
                new Object[]{5, By.xpath("//*[@id='accordion__heading-5']"), "Вы привозите зарядку вместе с самокатом?"},
                new Object[]{6, By.xpath("//*[@id='accordion__heading-6']"), "Можно ли отменить заказ?"},
                new Object[]{7, By.xpath("//*[@id='accordion__heading-7']"), "Я живу за МКАДом, привезёте?"}
        );
    }

    private String getExpectedAnswerText(By buttonLocator) {
        if (buttonLocator.equals(By.xpath("//*[@id='accordion__heading-0']"))) {
            return "Сутки — 400 рублей. Оплата курьеру — наличными или картой.";
        } else if (buttonLocator.equals(By.xpath("//*[@id='accordion__heading-1']"))) {
            return "Пока что у нас так: один заказ — один самокат. Если хотите покататься с друзьями, можете просто сделать несколько заказов — один за другим.";
        } else if (buttonLocator.equals(By.xpath("//*[@id='accordion__heading-2']"))) {
            return "Допустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. "
                    + "Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру. Если мы привезли самокат 8 мая в 20:30, суточная аренда закончится 9 мая в 20:30.";
        } else if (buttonLocator.equals(By.xpath("//*[@id='accordion__heading-3']"))) {
            return "Только начиная с завтрашнего дня. Но скоро станем расторопнее.";
        } else if (buttonLocator.equals(By.xpath("//*[@id='accordion__heading-4']"))) {
            return "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010.";
        } else if (buttonLocator.equals(By.xpath("//*[@id='accordion__heading-5']"))) {
            return "Самокат приезжает к вам с полной зарядкой. Этого хватает на восемь суток — даже если будете кататься без передышек и во сне. Зарядка не понадобится.";
        } else if (buttonLocator.equals(By.xpath("//*[@id='accordion__heading-6']"))) {
            return "Да, пока самокат не привезли. Штрафа не будет, объяснительной записки тоже не попросим. Все же свои.";
        } else if (buttonLocator.equals(By.xpath("//*[@id='accordion__heading-7']"))) {
            return "Да, обязательно. Всем самокатов! И Москве, и Московской области.";
        } else {
            throw new IllegalArgumentException("Локатор кнопки не распознан: " + buttonLocator);
        }
    }

    @BeforeEach
    public void setUp() {
        // Initialize the WebDriver
        if (driver == null) {
            driver = new ChromeDriver();
        }
    }

    @ParameterizedTest
    @MethodSource("buttonTextsProvider")
    @DisplayName("Проверка соответствия текста кнопкам в блоке Вопросы о важном")
    public void mainPageButtonsTextTest(int index, By buttonLocator, String expectedText) {
        driver.get("https://qa-scooter.praktikum-services.ru/");
        WebElement button = driver.findElement(buttonLocator);
        String actualText = button.getText();
        assertEquals(expectedText, actualText);
    }

    @ParameterizedTest
    @MethodSource("buttonTextsProvider")
    @DisplayName("Проверка соответствия текста ответов на кнопках в блоке Вопросы о важном")
    public void buttonsAnswerTest(int index, By buttonLocator, String expectedResponse) {
        driver.get("https://qa-scooter.praktikum-services.ru/");
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(buttonLocator));
        WebElement button = driver.findElement(buttonLocator);
        button.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        By answerLocator = By.xpath("//*[@id='accordion__panel-" + index + "']/p");
        WebElement answerElement = wait.until(ExpectedConditions.visibilityOfElementLocated(answerLocator));
        String actualAnswerText = answerElement.getText();
        String expectedAnswerText = getExpectedAnswerText(buttonLocator);

        assertEquals(expectedAnswerText, actualAnswerText,
                "Текст ответа не соответствует ожидаемому для кнопки: " + buttonLocator);
    }

    @ParameterizedTest
    @MethodSource("combinedProvider")
    @DisplayName("e2e сценарий для разных браузеров и кнопок заказа")
    public void endToEndScenarioTest(String buttonPosition, String browser) throws InterruptedException {
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