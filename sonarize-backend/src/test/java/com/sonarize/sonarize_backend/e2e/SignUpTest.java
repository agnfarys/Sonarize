package com.sonarize.sonarize_backend.e2e;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SignUpTest {
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
    void testSignUpForm() {
        driver.get("http://localhost:5173/signup");
        WebElement emailInput = driver.findElement(By.xpath("//input[@type='email']"));
        emailInput.sendKeys("test@example.com");
        WebElement passwordInput = driver.findElement(By.xpath("//input[@type='password']"));
        passwordInput.sendKeys("password123");
        WebElement signUpButton = driver.findElement(By.className("submit-button"));
        signUpButton.click();

        Assertions.assertEquals("http://localhost:5173/survey", driver.getCurrentUrl());
    }
}
