package ilab.generic.functions;



import com.codoid.products.fillo.Recordset;

import ilab.application.specific.functions.API.API;

import simswap.GetSimSwapStatus;

import org.apache.poi.ss.usermodel.Sheet;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.asserts.SoftAssert;

import java.sql.ResultSet;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public abstract class commonBase {
    protected WebDriver driver;
    protected UtilityFunctions utils = new UtilityFunctions();
    protected Reporting repo = new Reporting();
    protected DataFunctions data = new DataFunctions();
    protected mobileConfig mobile = new mobileConfig();
    protected GetSimSwapStatus getsimswapstatus;
    protected API api;
    protected int iRow;
    protected Sheet sheet;
    protected Recordset record;
    public static String sDefaultPath;
    protected ResultSet resultset;
    protected SoftAssert s_assert = new SoftAssert();
    protected String testCase;
    protected String reportLocation;
    protected String sDataType;
    protected DesiredCapabilities cap;
    protected ExtentTest logger;
    protected ExtentReports extent;
	String TestData = "\\TestData\\GUI\\MemberZoneDataExcel.xlsx";

    @BeforeTest
	public void beforeMethod() {
		try {

			setup(TestData,"Controller");
			setupReport();

		} catch (Exception e) {
			System.out.println("Before Test Error : " + e.getMessage());
		}
	}

	@AfterMethod
	public void afterMethod() throws Exception {
		try {
			repo.getExtent().flush();
			Collapse();
		}catch (Exception e){
			System.out.println("Error After Teat : "+ e.getMessage());
		}
	}

    public void setup(String Location,String sheetname) throws Exception {
    	try
		{
			sDefaultPath = System.getProperty("user.dir");
			sDefaultPath = sDefaultPath.replace("batch", "");
			data.GetEnvironmentVariables();
			sDataType = data.getDataType();
			repo.setExtent(repo.initializeExtentReports(data.getReportName(), data.getAppendReport(), utils));
			
				switch (data.getDataType().toUpperCase())
				{
		
					case "EXCEL": sheet = data.ReadExcel(sDefaultPath+Location,sheetname);
								iRow = data.rowCount(sheet, record, resultset, sDataType)-1;
								 break;
			
					case "FILLO": record = data.ConnectFillo(sDefaultPath+Location,"Select * from Sheet1");
							
								 iRow = data.rowCount(sheet, record, resultset, sDataType);
								 break;
								 
					case "SQLSERVER":resultset = data.ConnectAndQuerySQLServer(data.getDBHost(), data.getDBUsername(),data.getDBPassword(), "Select * from  [BookFlights].[dbo].[BookFlights]");
								 iRow = data.rowCount(sheet, record, resultset, sDataType);
								 resultset = data.ConnectAndQuerySQLServer(data.getDBHost(), data.getDBUsername(),data.getDBPassword(), "Select * from  [BookFlights].[dbo].[BookFlights]");
				}

		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
    }

    public void setupDriver() throws Exception {
		utils.setWebDriver(utils.initializeWedriver(data.getCellData("Browser",1,sheet,null,null,sDataType), null,  null));
	}

    public  void setupReport() throws Exception{
    	data.GetEnvironmentVariables();
    	sDataType = data.getDataType();
    	repo.setExtent(repo.initializeExtentReports(data.getReportName(), data.getAppendReport(), utils));
    }
    
    public void setupAPI(String Location,String sheetname) throws Exception {
    	try
		{
			sDefaultPath = System.getProperty("user.dir");
			sDefaultPath = sDefaultPath.replace("batch", "");
			data.GetEnvironmentVariables();
			sDataType = data.getDataType();
			repo.setExtent(repo.initializeExtentReports(data.getReportName(), data.getAppendReport(), utils));
			
				switch (data.getDataType().toUpperCase())
				{		
					case "EXCEL": sheet = data.ReadExcel(sDefaultPath+Location,sheetname);
								iRow = data.rowCount(sheet, record, resultset, sDataType)-1;
								 break;
			
					case "FILLO": record = data.ConnectFillo(sDefaultPath+Location,"Select * from Sheet1");
							
								 iRow = data.rowCount(sheet, record, resultset, sDataType);
								 break;
								 
					case "SQLSERVER":resultset = data.ConnectAndQuerySQLServer(data.getDBHost(), data.getDBUsername(),data.getDBPassword(), "Select * from  [BookFlights].[dbo].[BookFlights]");
								 iRow = data.rowCount(sheet, record, resultset, sDataType);
								 resultset = data.ConnectAndQuerySQLServer(data.getDBHost(), data.getDBUsername(),data.getDBPassword(), "Select * from  [BookFlights].[dbo].[BookFlights]");
				}
			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
    }   
    
    public void setupMobile(String Location, String sMobileType, String strAppiumVersion, String strAutomationName, String strPlatformName, String strPlatformVersion, String strDeviceName, String strApplicationName, String strBundleID,String strudid, String strPackage, String strActivity, String strSessionName, String strDescription, String strDeviceOrientation, String strCaptureScreenshot,String strBrowserName, String strDeviceGroup, String strURL) throws Exception {
    	try
		{
			sDefaultPath = System.getProperty("user.dir");
			sDefaultPath = sDefaultPath.replace("batch", "");
			data.GetEnvironmentVariables();
			sDataType = data.getDataType();
			repo.setExtent(repo.initializeExtentReports(data.getReportName(), data.getAppendReport(), utils));
			
				switch (data.getDataType().toUpperCase())
				{
		
					case "EXCEL": sheet = data.ReadExcel(sDefaultPath+Location,"Sheet1");
								iRow = data.rowCount(sheet, record, resultset, sDataType)-1;
								 break;
	
					case "FILLO": record = data.ConnectFillo(sDefaultPath+Location,"Select * from Sheet1");
							
								 iRow = data.rowCount(sheet, record, resultset, sDataType);
								 break;
								 
					case "SQLSERVER":resultset = data.ConnectAndQuerySQLServer(data.getDBHost(), data.getDBUsername(),data.getDBPassword(), "Select * from  [BookFlights].[dbo].[BookFlights]");
								 iRow = data.rowCount(sheet, record, resultset, sDataType);
								 resultset = data.ConnectAndQuerySQLServer(data.getDBHost(), data.getDBUsername(),data.getDBPassword(), "Select * from  [BookFlights].[dbo].[BookFlights]");
				}
				
				
				cap =	mobile.setDesiredCapabilities(sMobileType, strAppiumVersion, strAutomationName, strPlatformName, strPlatformVersion, strDeviceName, strApplicationName, strBundleID, strudid, strPackage, strActivity, strSessionName, strDescription, strDeviceOrientation, strCaptureScreenshot, strBrowserName, strDeviceGroup);		
		utils.setWebDriver(utils.initializeWedriver(data.getCellData("Browser",1,sheet,null,null,sDataType), strURL, cap ));
			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
    }
    

    protected void initialiseFunctions() {

    	getsimswapstatus = new GetSimSwapStatus(data, utils, repo);
        

        api = new API(data);
    }

    
    public void Collapse() throws Exception
    {
    	 utils.getWebdriver().quit();
    }
    /*
    public void tearDown() {
        utils.shutdown();
        testReports.getExtent().flush();
        s_assert.assertAll();
    }
    */
}
