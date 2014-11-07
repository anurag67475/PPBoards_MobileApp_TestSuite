import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class PPBoards_Tests {
	AndroidDriver driver;
	
	@BeforeTest
	public void setup() throws MalformedURLException{
		File app = new File("D:\\Appium\\02_Softwares\\Software_Selendroid_Appium_pack\\Appium_Selendroid_pack\\apps\\com.projectplace.boards-1.apk");
		DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
        capabilities.setCapability("deviceName","Nexus 5");
        capabilities.setCapability("platformVersion", "4.4.4");
        capabilities.setCapability("platformName","Android");
        capabilities.setCapability("app", app.getAbsolutePath());

        capabilities.setCapability("appPackage", "com.projectplace.boards");
        capabilities.setCapability("appActivity", "com.projectplace.boards.ui.login.LoginActivity");
       
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        
	}
	
	@AfterTest
	public void quit() throws InterruptedException{
		Thread.sleep(2000);
		if(driver != null){
			driver.quit();
		}
	}
	
	@Test(priority=1)
	public void testLoginLogout() throws MalformedURLException, InterruptedException {
		login();
		//logout();
	}
	
	@Test(priority=2)
	public void searchContactInPeople() throws InterruptedException{
       //login();
	   driver.findElement(By.xpath("//android.widget.TextView[contains(@text,'People')]")).click();
       driver.findElement(By.xpath("//android.widget.EditText[contains(@text,'Search')]")).sendKeys("Nayan");
       Thread.sleep(250);
       driver.findElement(By.xpath("//android.widget.TextView[contains(@text,'Nayan')]")).click();
       Thread.sleep(2000);
       driver.findElement(By.id("android:id/button2")).click();
       driver.findElement(By.id("android:id/home")).click();
   	   WebDriverWait wait = new WebDriverWait(driver, 15);
       wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.projectplace.boards:id/left_drawer")));
       //logout();
	}
	
	@Test(priority=3)
	public void testCreateCard() throws MalformedURLException, InterruptedException {
		//login();
		WebElement menuBoardItem = findElementByNameAttribute("android.widget.TextView", "Boards", true);
		menuBoardItem.click();

		WebElement testBoardTextView = findElementByNameAttribute("android.widget.TextView", "HackAWeek", true);
		testBoardTextView.click();

		Thread.sleep(250);

		//Workaround for weird error where sendKey doesn't work; clicking on EditText before sendKeys
		driver.findElement(By.id("com.projectplace.boards:id/create_card")).click();
		Thread.sleep(250);
		driver.findElement(By.id("com.projectplace.boards:id/create_card")).sendKeys("NewTestCard");
		Thread.sleep(250);
		driver.sendKeyEvent(66); //done button, this creates the card

		
		Thread.sleep(250);

		WebElement cardItemTextView = findElementByNameAttribute("android.widget.TextView", "NewTestCard", false);
		cardItemTextView.click(); //doesn't work with single click?!
		cardItemTextView.click();

		Thread.sleep(250);

		//driver.sendKeyEvent(82); //menu button

		//Thread.sleep(5000);
		driver.findElement(By.id("android:id/up")).click();
		driver.findElement(By.id("android:id/home")).click();
		driver.findElement(By.id("android:id/home")).click();
   	    WebDriverWait wait = new WebDriverWait(driver, 15);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.projectplace.boards:id/left_drawer")));
		//logout();
	}

	@Test(priority=4)
	public void testMoveCard() throws InterruptedException {
		//login();
		//Thread.sleep(2000);
		driver.findElement(By.xpath("//android.widget.TextView[contains(@text,'Boards') and @index=1]")).click();
		Thread.sleep(250);
		driver.findElement(By.xpath("//android.widget.TextView[contains(@text,'HackAWeek')]")).click();
		Thread.sleep(2000);

		List<WebElement> cardList = driver.findElements(By.id("com.projectplace.boards:id/card_title"));
		WebElement card1 = cardList.remove(0);
		WebElement card2 = cardList.remove(0);
		
		TouchAction action = new TouchAction(driver);
		action.press(card1).waitAction(10000).moveTo(card2, 500, 500).release();
		driver.performTouchAction( action );

		driver.findElement(By.id("android:id/home")).click();
		driver.findElement(By.id("android:id/home")).click();
   	    WebDriverWait wait = new WebDriverWait(driver, 15);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.projectplace.boards:id/left_drawer")));
        logout();
}

	
	public void login() throws InterruptedException{
		driver.findElement(By.id("com.projectplace.boards:id/email")).sendKeys("1.anurag.singh@fwd.projectplace.com");
		Thread.sleep(250);
		driver.findElement(By.id("com.projectplace.boards:id/password")).sendKeys("Test@123");
		Thread.sleep(250);
		driver.findElement(By.id("com.projectplace.boards:id/email_sign_in_button")).click();
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.projectplace.boards:id/login_progress")));
	}
	
	public void logout() throws InterruptedException{
			driver.findElement(By.xpath("//android.widget.TextView[contains(@text,'Logout')]")).click();
			Thread.sleep(2500);
		    driver.findElement(By.xpath("//android.widget.Button[contains(@text,'Logout')]")).click();
			
	}
	
	public WebElement findElementByNameAttribute(String className, String attributeName, boolean removeFirst) {
		List<WebElement> list = driver.findElementsByClassName(className);

		if(removeFirst) list.remove(0);

		for(WebElement elem : list) {
			if(elem.getAttribute("name").equals(attributeName))
			{
				return elem;
			}
		}
		return null;
	}
}
