package frame;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class Sudarshan {
	public static void main(String[] args) throws Exception {
		File abPath=new File("./resources/trips1.xlsx");
		FileInputStream fis=new FileInputStream(abPath);
		Workbook workbook = WorkbookFactory.create(fis);
		Sheet sheet = workbook.getSheet("Sheet1");
		System.setProperty("webdriver.chrome.driver","./drivers/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(3,TimeUnit.SECONDS);
		driver.get("https://www.makemytrip.com/");
		Actions action=new Actions(driver);
		action.click().perform();
		for(int i=1;i<=3;i++){
			String from = sheet.getRow(i).getCell(0).getStringCellValue();
			String to = sheet.getRow(i).getCell(1).getStringCellValue();
			driver.findElement(By.id("fromCity")).click();
			driver.findElement(By.xpath("//input[@placeholder='From']")).sendKeys(from);
			Thread.sleep(1000);
			driver.findElement(By.xpath("//p[contains(text(),'"+from+"')]")).click();
			action.click().perform();
			driver.findElement(By.id("toCity")).click();
			driver.findElement(By.xpath("//input[@placeholder='To']")).sendKeys(to);
			Thread.sleep(1000);
			driver.findElement(By.xpath("//p[contains(text(),'"+to+"')]")).click();
			driver.findElement(By.xpath("//span[text()='DEPARTURE']")).click();
			LocalDateTime ldt = LocalDateTime.now().plusDays(1);
			String month = ldt.getMonth().name();
			month= month.substring(0, 1).toUpperCase()+month.substring(1).toLowerCase();
			int day = ldt.getDayOfMonth();
			int year = ldt.getYear();
			calenderDate(driver, month, day, year);
			driver.findElement(By.linkText("SEARCH")).click();
			JavascriptExecutor js=(JavascriptExecutor) driver;
			js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
			List<WebElement> flights = driver.findElements(By.xpath("//p[text()='"+from+"']"));
			int count=0;
			count=flights.size();
			System.out.println("the number of Flights from "+from+" to "+to+" is  "+count);
			driver.findElement(By.className("logoContainer")).click();
		}
		Thread.sleep(4000);
		driver.quit();
		//p[text()='"+from+"']
		//div[@class='listingCard']
	}
	public static void calenderDate(WebDriver driver,String month,int day,int year){
		for(;;){
			try{
				driver.findElement(By.xpath("//div[text()='"+month+" "+year+"']/../..//p[text()='"+day+"']")).click();
				break;
			}
			catch(Exception e){
				driver.findElement(By.xpath("//span[@aria-label='Next Month']")).click();
			}
		}
	}
}
