package base;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.apache.commons.io.FileUtils;  //add depencies in pom.xml



public class TestBase {
	
	protected static WebDriver driver;
	public static Properties envConfig;
	WebDriverWait wait;
	


	//BROWSER value fetched from POM with Chrome being the default value 
	private static final String BROWSER = "Chrome";
	
		
	//Automation suite setup method to configure and instantiate a particular browser
	@BeforeSuite
    public void suiteSetup() throws Exception {
		
		//Browser configuration - can add more browsers and remote driver here
		if (BROWSER.equals("Firefox")) {
			
         }
		else if (BROWSER.equals("Chrome")) {
			
			  System.setProperty("webdriver.chrome.driver", "E:\\selenium softwares\\chromedriver_win32\\chromedriver_103.exe"); 
			driver = new ChromeDriver();             
         }
		 else if (BROWSER.equals("IE")) {
		
         }
		 else {
             throw new RuntimeException("Browser type unsupported");
         }
		
		//Setting implicit wait
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		driver.manage().window().maximize();
		
		//Setting WebDriverWait with max timeout value of 20 seconds
		wait = new WebDriverWait(driver, 20);

		//Environment specific properties file loading
		InputStream configFile = new FileInputStream(System.getProperty("user.dir") + 
				"\\src\\config\\properties"  +  ".properties");	
		
		envConfig = new Properties();
		envConfig.load(configFile);
	
	}

	
	@BeforeMethod()
    public void loadBaseUrl(Method method) {
		
        driver.get(envConfig.getProperty("baseUrl"));
        
    }
 

	@AfterMethod
	public void screenshotAndDeleteCookies(ITestResult testResult) throws IOException {
		//Taking screenshot in case of failure
		if(testResult.getStatus() == ITestResult.FAILURE){
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File("errorScreenshots\\" + testResult.getName() + "-" 
					+ Arrays.toString(testResult.getParameters()) +  ".jpg"));	
			}
		
		//Deleting cookies
		driver.manage().deleteAllCookies();
	}
 

   // @AfterSuite
    public void suiteTearDown() {
    	driver.quit();
    }

}