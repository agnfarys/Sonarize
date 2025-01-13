package com.sonarize.sonarize_backend.e2e;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SurveyTest {
    private WebDriver driver;

    @BeforeAll
    void setUp() {
        System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterAll
    void tearDown() {
        driver.quit();
    }

    @Test
    void testSurveyNavigation() {
        driver.get("http://localhost:5173/survey");
        WebElement firstAnswer = driver.findElement(By.xpath("//button[contains(text(),'happy')]"));
        firstAnswer.click();
        WebElement nextButton = driver.findElement(By.className("arrow-button"));
        nextButton.click();
        WebElement questionNumber = driver.findElement(By.xpath("//span[contains(text(),'2/9')]"));
        Assertions.assertTrue(questionNumber.isDisplayed());
    }

    @Test
    void testSurveySubmission() {
        driver.get("http://localhost:5173/survey");

        String[] answers = {"happy", "pop", "calm", "workout", "pitbull", "new releases", "english", "10"};
        for (String answer : answers) {
            WebElement answerButton = driver.findElement(By.xpath("//button[contains(text(),'" + answer + "')]"));
            answerButton.click();
            WebElement nextButton = driver.findElement(By.className("arrow-button"));
            nextButton.click();
        }

        // Sprawdzenie, czy zapytanie zostało wysłane
        WebElement responseMessage = driver.findElement(By.xpath("//*[contains(text(),'Playlist created successfully')]"));
        Assertions.assertTrue(responseMessage.isDisplayed());
    }
}
