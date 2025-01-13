package com.sonarize.sonarize_backend.e2e;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LogInTest {
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
    void testLogInWithSpotify() {
        driver.get("http://localhost:5173/login");
        WebElement spotifyButton = driver.findElement(By.className("spotify-button"));
        spotifyButton.click();
        Assertions.assertTrue(driver.getCurrentUrl().contains("accounts.spotify.com/authorize"));
    }
}
