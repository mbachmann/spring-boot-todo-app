package com.example.todo.testcontainer.e2e.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class VersionPage {
    private WebDriver driver;

    @FindBy(xpath = "//h1[@class='page-title']")
    private WebElement pageTitle;

    @FindBy(xpath = "//li[contains(text(), 'Application Version:')]/span")
    private WebElement applicationVersion;

    @FindBy(xpath = "//li[contains(text(), 'Spring Boot Version:')]/span")
    private WebElement springBootVersion;

    @FindBy(xpath = "//li[contains(text(), 'Java Version:')]/span")
    private WebElement javaVersion;

    @FindBy(xpath = "//li[contains(text(), 'SpringDoc Version:')]/span")
    private WebElement springDocVersion;

    @FindBy(xpath = "//li[contains(text(), 'Application Version:')]/span")
    private WebElement applicationVersionValue;

    @FindBy(xpath = "//li[contains(text(), 'Spring Boot Version:')]/span")
    private WebElement springBootVersionValue;

    @FindBy(xpath = "//li[contains(text(), 'Java Version:')]/span")
    private WebElement javaVersionValue;

    @FindBy(xpath = "//li[contains(text(), 'SpringDoc Version:')]/span")
    private WebElement springDocVersionValue;

    @FindBy(xpath = "//button[contains(text(), 'Home')]")
    private WebElement homeButton;

    public VersionPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getPageTitle() {
        return pageTitle.getText();
    }

    public String getApplicationVersion() {
        return applicationVersion.getText();
    }

    public String getSpringBootVersion() {
        return springBootVersion.getText();
    }

    public String getJavaVersion() {
        return javaVersion.getText();
    }

    public String getSpringDocVersion() {
        return springDocVersion.getText();
    }

    public String getApplicationVersionValue() {
        return applicationVersionValue.getText();
    }

    public String getSpringBootVersionValue() {
        return springBootVersionValue.getText();
    }

    public String getJavaVersionValue() {
        return javaVersionValue.getText();
    }

    public String getSpringDocVersionValue() {
        return springDocVersionValue.getText();
    }

    public void goToHomePage() {
        homeButton.click();
    }
}
