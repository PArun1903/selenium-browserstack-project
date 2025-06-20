package Tests;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

public class BaseTest {
    protected WebDriver driver;

    @BeforeMethod
    public void setUp(Method method) throws Exception {
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("browserName", "Chrome");
        capabilities.setCapability("browserVersion", "latest");

        Map<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("os", "Windows");
        bstackOptions.put("osVersion", "10");
        bstackOptions.put("userName", "arunpujari_Igznmy");
        bstackOptions.put("accessKey", "et9QTDTiCTzPCxuppPZJ");
        bstackOptions.put("buildName", "browserstack-build-1.0");
        bstackOptions.put("sessionName", method.getName()); // Dynamic session name

        capabilities.setCapability("bstack:options", bstackOptions);

        driver = new RemoteWebDriver(new URI("https://hub-cloud.browserstack.com/wd/hub").toURL(), capabilities);

        // Optional: explicitly set session name (redundant but helpful)
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        JSONObject sessionName = new JSONObject();
        sessionName.put("action", "setSessionName");
        sessionName.put("arguments", new JSONObject().put("name", method.getName()));
        jse.executeScript(String.format("browserstack_executor: %s", sessionName.toString()));
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (driver != null) {
            try {
                // Only execute if session is still active
                if (((RemoteWebDriver) driver).getSessionId() != null) {
                    JavascriptExecutor jse = (JavascriptExecutor) driver;
                    JSONObject statusObject = new JSONObject();
                    JSONObject arguments = new JSONObject();

                    if (result.getStatus() == ITestResult.SUCCESS) {
                        arguments.put("status", "passed");
                        arguments.put("reason", "Test passed.");
                    } else {
                        arguments.put("status", "failed");
                        arguments.put("reason", result.getThrowable() != null
                            ? result.getThrowable().toString()
                            : "Unknown failure.");
                    }

                    statusObject.put("action", "setSessionStatus");
                    statusObject.put("arguments", arguments);

                    jse.executeScript(String.format("browserstack_executor: %s", statusObject.toString()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                driver.quit();
            }
        }
    }

}
