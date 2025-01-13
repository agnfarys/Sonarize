package com.sonarize.sonarize_backend.e2e;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SurveyPlaylistTest {
    private WebDriver driver;

    @BeforeAll
    void setUp() {
        System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterAll
    void tearDown() {
        driver.quit();
    }

    @Test
    void shouldGeneratePlaylistBasedOnSurvey() {
        driver.get("http://localhost:5173/survey");

        // Wypełnianie ankiety
        String[] answers = {"happy", "pop", "calm", "workout", "pitbull", "new releases", "english", "10"};
        for (String answer : answers) {
            WebElement answerButton = driver.findElement(By.xpath("//button[contains(text(),'" + answer + "')]"));
            answerButton.click();
            WebElement nextButton = driver.findElement(By.className("arrow-button"));
            nextButton.click();
        }

        // Sprawdzenie wyniku
        WebElement successMessage = driver.findElement(By.xpath("//*[contains(text(),'Playlist created successfully')]"));
        Assertions.assertTrue(successMessage.isDisplayed());
    }
}
