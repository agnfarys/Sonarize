package com.sonarize.sonarize_backend.e2e;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NavbarTest {
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
    void testNavigateToHomePage() {
        driver.get("http://localhost:5173");
        WebElement logo = driver.findElement(By.xpath("//h1[text()='SONARIZE']"));
        logo.click();
        Assertions.assertEquals("http://localhost:5173/", driver.getCurrentUrl());
    }

    @Test
    void testToggleMobileMenu() {
        driver.get("http://localhost:5173");
        WebElement menuIcon = driver.findElement(By.xpath("//img[@alt='menu']"));
        menuIcon.click();
        WebElement sidebar = driver.findElement(By.className("sidebar"));
        Assertions.assertTrue(sidebar.isDisplayed());
    }
}
