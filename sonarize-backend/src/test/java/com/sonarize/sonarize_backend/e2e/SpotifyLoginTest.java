package com.sonarize.sonarize_backend.e2e;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SpotifyLoginTest {
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
    void shouldLoginWithSpotify() {
        driver.get("http://localhost:5173/login");

        // Kliknięcie przycisku logowania przez Spotify
        WebElement spotifyButton = driver.findElement(By.className("spotify-button"));
        spotifyButton.click();

        // Sprawdzenie przekierowania na stronę Spotify
        Assertions.assertTrue(driver.getCurrentUrl().contains("accounts.spotify.com/authorize"));
    }
}
