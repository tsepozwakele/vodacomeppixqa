package ilab.generic.functions;

import ilab.api.tests.APIController;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.aventstack.extentreports.ExtentTest;
import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;


public class DataFunctions {

	protected UtilityFunctions utils = new UtilityFunctions();
	public FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-ZA"), new RandomService());
	public Faker faker = new Faker(new Locale("en-ZA"));

	SoftAssert soft = new SoftAssert();

	private String sWebURL;
	private String sDealerID;
	private String sPassword;
	private String sUserID;
	private String sMSISDN;
	private String sAppLoc;
	private String sAppUsername;
	private String sAppPassword;
	private String sApi;
	private String sDataLocation;
	private String sDataType;
	private String sDBPassword;
	private String sDBUsername;
	private String sEmail;
	private String sEmailFrom;
	private String sEmailTo;
	private String sDBHost;
	private String sBrowser;
	private String sKobitonUsername;
	private String sKobitonPassword;
	private String sIOSUsername;
	private String sIOSPassword;
	private String sTestManagement;
	private String sTestMURL;
	private String sTestMUsername;
	private String sTestMPassword;
	private String sDomain;
	private String sProject;
	private String sReportName;
	private String sAppendReport;
	private String sAndroidUsername;
	private String sAndroidPassword;
	private String sbrowserDrivers;
	private String sApiAuthHost;
	private String sApiUsername;
	private String sApiPassword;
	private String sCustomerUsername;
	private String sCustomerPassword;

	private String[] result;
	public Connection connect;
	public java.sql.Connection conn = null;
	public Sheet sheet;
	public Workbook workbook;
	int col, Column_Count, Row_Count;
	int colnNum = 0;
	int fillonum = 1;

	ArrayList<String> lines = new ArrayList<String>();

	public String getWebURL() {
		return sWebURL;
	}

	public String getDealerID() {
		return sDealerID;
	}

	public String getWebPassword() {
		return sPassword;
	}

	public String getWebUserName() {
		return sUserID;
	}

	public String getMSISDN() {
		return sMSISDN;
	}

	public String getWindowsAppLocation() {
		return sAppLoc;
	}

	public String getWindowsAppUsername() {
		return sAppUsername;
	}

	public String getWindowsAppPassword() {
		return sAppPassword;
	}

	public String getAPI() {
		return sApi;
	}

	public String getDataLocation() {
		return sDataLocation;
	}

	public String getDBHost() {
		return sDBHost;
	}

	public String getDBUsername() {
		return sDBUsername;
	}

	public String getDBPassword() {
		return sDBPassword;
	}

	public String getDataType() {
		return sDataType;
	}

	public String getSendEmail() {
		return sEmail;
	}

	public String getEmailFrom() {
		return sEmailFrom;
	}

	public String getEmailTo() {
		return sEmailTo;
	}

	public String getBrowser() {
		return sBrowser;
	}

	public String getKobitonPassword() {
		return sKobitonPassword;
	}

	public String getKobitonUsername() {
		return sKobitonUsername;
	}

	public String getIOSUsername() {
		return sIOSUsername;
	}

	public String getIOSPassword() {
		return sIOSPassword;
	}

	public String getTestManagement() {
		return sTestManagement;
	}

	public String getTestMURL() {
		return sTestMURL;
	}

	public String getTestMUsername() {
		return sTestMUsername;
	}

	public String getTestMPassword() {
		return sTestMPassword;
	}

	public String getDomain() {
		return sDomain;
	}

	public String getProject() {
		return sProject;
	}

	public String getReportName() {
		return sReportName;
	}

	public String getAppendReport() {
		return sAppendReport;
	}

	public String getAndroidUsername() {
		return sAndroidUsername;
	}

	public String getAndroidPassword() {
		return sAndroidPassword;
	}

	public String getBrowserDrivers() {
		return sbrowserDrivers;
	}

	public String getApiAuthHost() {
		return sApiAuthHost;
	}

	public String getApiUsername() {
		return sApiUsername;
	}

	public String getApiPassword() {
		return sApiPassword;
	}

	public String getCustomerUsername() {
		return sCustomerUsername;
	}

	public String getCustomerPassword() {
		return sCustomerPassword;
	}

	/*****************************************************************************
	 * Function Name: GetEnvironmentVariables Description: gets environment
	 * variables from the config json file Date Created: 13/09/2017
	 *
	 ******************************************************************************/
	public void GetEnvironmentVariables() throws IOException, ParseException {
		File f1 = null;
		FileReader fr = null;

		JSONParser parser = new JSONParser();
		try {
			f1 = new File("conf/Environment.json");
			fr = new FileReader(f1);
			Object obj = parser.parse(fr);
			JSONObject jsonObject = (JSONObject) obj;
			// System.out.print(jsonObject);
			// String[] env=new String[10];

			sWebURL = (String) jsonObject.get("weburl");
			sUserID = (String) jsonObject.get("webusername");
			sPassword = (String) jsonObject.get("webpassword");
			sAppLoc = (String) jsonObject.get("windowsapplocation");
			sAppUsername = (String) jsonObject.get("windowsappusername");
			sAppPassword = (String) jsonObject.get("windowsapppassword");
			sApi = (String) jsonObject.get("API");
			sDataLocation = (String) jsonObject.get("datalocation");
			sDataType = (String) jsonObject.get("datatype");
			sDBHost = (String) jsonObject.get("dbhost");
			sDBPassword = (String) jsonObject.get("dbpass");
			sDBUsername = (String) jsonObject.get("dbusername");
			sEmail = (String) jsonObject.get("sendemail");
			sEmailFrom = (String) jsonObject.get("emailfrom");
			sEmailTo = (String) jsonObject.get("emailto");
			sBrowser = (String) jsonObject.get("Browser");
			sKobitonUsername = (String) jsonObject.get("kobitonusername");
			sKobitonPassword = (String) jsonObject.get("kobitonpassword");
			sIOSUsername = (String) jsonObject.get("iosusername");
			sIOSPassword = (String) jsonObject.get("iospassword");
			sTestManagement = (String) jsonObject.get("testmanagement");
			sTestMURL = (String) jsonObject.get("testurl");
			sTestMUsername = (String) jsonObject.get("testmanagememntusername");
			sTestMPassword = (String) jsonObject.get("testmanagmentpassword");
			sDomain = (String) jsonObject.get("domain");
			sProject = (String) jsonObject.get("project");
			sReportName = (String) jsonObject.get("reportname");
			sAppendReport = (String) jsonObject.get("appendreport");
			sAndroidUsername = (String) jsonObject.get("androidusername");
			sAndroidPassword = (String) jsonObject.get("androidpassword");
			sbrowserDrivers = (String) jsonObject.get("browserDrivers");

			sApiAuthHost = (String) jsonObject.get("apiAuthHost");
			sApiUsername = (String) jsonObject.get("apiUsername");
			sApiPassword = (String) jsonObject.get("apiPassword");
			sCustomerUsername = (String) jsonObject.get("customerUsername");
			sCustomerPassword = (String) jsonObject.get("customerPassword");

		} finally {
			try {
				Objects.requireNonNull(fr).close();

			} catch (IOException ioe)

			{
				ioe.printStackTrace();
			}
		}

	}

	// Read and Write data from/To a TextFile
	public String ReadTextFile(String filePath) throws IOException {
		BufferedReader input = null;
		FileReader file = new FileReader(filePath);
		input = new BufferedReader(file);

		String value = input.readLine();
		input.close();

		return value;

	}

	public void WriteTextFile(String filePath, String outputData) throws IOException {
		Writer output = null;
		File file = new File(filePath);
		output = new BufferedWriter(new FileWriter(file));

		output.write(outputData);
		// output.write("\r\n");
		output.close();

	}

	public Sheet ReadExcel(String FILE_NAME, String strSheetName) throws IOException {

		FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
		workbook = new XSSFWorkbook(excelFile);
		// sheet = workbook.getSheetAt(0);
		sheet = workbook.getSheet(strSheetName);
		return sheet;
	}

	public void ReadExcelWorkbook(String FILE_NAME) throws IOException {

		FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
		workbook = new XSSFWorkbook(excelFile);

		// return workbook;
	}

	public void SheetData(String sheetval) throws Exception {

		Sheet sheet1 = workbook.getSheet(sheetval);
		String Execute = null, TestName = null;
		// String

		int rowcount = workbook.getSheet(sheetval).getPhysicalNumberOfRows();

		for (int i = 1; i < rowcount; i++) {
			// Read the execute sheet values
			Execute = getCellData("Execute", i, sheet1, null, null, "Excel");
			TestName = getCellData("TestName", i, sheet1, null, null, "Excel");
			Execute = getCellData("Execute", i, sheet1, null, null, "Excel");
			getCellData("TestSetID", i, sheet1, null, null, "Excel");
			getCellData("TestCaseID", i, sheet1, null, null, "Excel");

			if (Execute.equals("Yes")) {
				// Switch to the applicable Sheet

				Sheet newsheet = workbook.getSheet(TestName);
				int rwcount = workbook.getSheet(TestName).getPhysicalNumberOfRows();
				// Loop through sheet that has scenarios

				for (int j = 1; j < rwcount; j++) {
					// System.out.println(workbook.getSheetName(j));
					String UrlendPoint = getCellData("Scenario", j, newsheet, null, null, "Excel");
					System.out.println(UrlendPoint);
				}

			}

		}

	}

	public Recordset ConnectFillo(String path, String Query) throws FilloException {

		Fillo fillo = new Fillo();

		Recordset record;
		// List<String> data = new ArrayList<String>();

		// String value = null;

		connect = fillo.getConnection(path);
		record = connect.executeQuery(Query);

		/*
		 * while(record.next()) { String valContain=record.getField("Used_By");
		 * 
		 * if (valContain.isEmpty()) { value=record.getField(vColumnValue); break; } }
		 */

		return record;

	}

	public String getCellData(String strColumn, int iRow, Sheet sheet, Recordset record, ResultSet resultset,
			String Type) throws Exception {
		String sValue = null;
		switch (Type.toUpperCase()) {

		case "EXCEL":

			Row row = sheet.getRow(0);
			for (int i = 0; i < columnCount(sheet); i++) {
				if (row.getCell(i).getStringCellValue().trim().equals(strColumn)) {
					Row raw = sheet.getRow(iRow);
					Cell cell = raw.getCell(i);
					DataFormatter formatter = new DataFormatter();
					sValue = formatter.formatCellValue(cell);
					break;
				}

			}
			break;

		case "FILLO":

			if (iRow == fillonum) {
				if (record.next()) {
					fillonum = iRow + 1;
					sValue = record.getField(strColumn);
				}
			} else {
				sValue = record.getField(strColumn);
			}
			break;

		case "SQLSERVER":

			if (iRow == fillonum) {
				if (resultset.next()) {
					fillonum = iRow + 1;
					sValue = resultset.getString(strColumn);
				}
			} else {
				sValue = resultset.getString(strColumn);
			}
			break;

		}

		return sValue;

	}
	
	public String getQueryResults(ResultSet resultset, int row) throws Exception {
		
		String sValue = null;

	
				if (resultset.next()) {
					sValue = resultset.getString(row);
				}
		return sValue;

	}

	public int rowCount(Sheet sheet, Recordset record, ResultSet resultset, String Type) throws Exception {
		int count = 0;
		switch (Type.toUpperCase()) {
		case "EXCEL":
			count = sheet.getPhysicalNumberOfRows();
			break;
		case "FILLO":

			count = record.getCount();
			break;
		case "SQLSERVER":
			int i = 0;
			while (resultset.next()) {
				i++;
			}
			count = i;
		}
		return count;
	}

	public int columnCount(Sheet sheet) throws Exception {
		return sheet.getRow(0).getLastCellNum();
	}

	public void writeData(String[] sColumn, int Row, String[] sData, String filepath, String Type, String sQuery, String sheetname) throws IOException, InvalidFormatException, FilloException, SQLException {
		try{
			switch (Type.toUpperCase()) 
			{
				case "EXCEL":
					int CoulmnNo = 0;
					FileInputStream file = new FileInputStream(filepath);
					Workbook wb = WorkbookFactory.create(file);
					// sheet = wb.getSheetAt(0);
					sheet = wb.getSheet(sheetname);
		
					org.apache.poi.ss.usermodel.Cell cell = null;
					Row row = sheet.getRow(0);
		
					for (int c = 0; c < sColumn.length; c++) {
						for (int i = 0; i < row.getLastCellNum(); i++) {
							if (row.getCell(i).getStringCellValue().trim().equals(sColumn[c])) {
								CoulmnNo = i;
								Row raw = sheet.getRow(Row);
								cell = raw.createCell(CoulmnNo);
								cell.setCellValue(sData[c]);
								
								break;
							}
						}
					}
					
					FileOutputStream fileOut = new FileOutputStream(filepath);
					wb.write(fileOut);
					fileOut.close();
					//wb.close();
		
					break;
				case "FILLO":
					System.setProperty("ROW", "1");// Table start row
					connect.executeUpdate(sQuery);
					// connect.close();
					break;
				case "SQLSERVER":
					Statement st = conn.createStatement();
					st.execute(sQuery);
			}
		}catch(Exception e) {
			System.out.print("Error Writing to File :"+e.getMessage());
		}

	}

	public ResultSet ConnectAndQuerySQLServer(String sDBURL, String sUserName, String sPassword, String sQuery) {

		ResultSet rs = null;

		try {

			String dbURL = sDBURL;
			String user = sUserName;
			String pass = sPassword;
			conn = DriverManager.getConnection(dbURL, user, pass);
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(sQuery);

		} catch (SQLException ex) {
			ex.printStackTrace();

		}
		return rs;
	}
	

	public ResultSet ConnectAndQueryEPPIXServer(String sDBURL, String sUserName, String sPassword, String sQuery) {

		ResultSet rs = null;

		try {
			
			Properties prop = new Properties();
		       prop.setProperty("user", "monyanel");
		       prop.setProperty("password", "Sprite@2019");
		       prop.setProperty("serverName", "csgeppixc3d_tcp");
		       prop.setProperty("PROTOCAL", "olsoctcp");
		       prop.setProperty("informixServer", "csgeppixqa_tcp"); // this my be set or else it wont work
		       prop.setProperty("DATABASE", "Eppix");       
		                                                   
		       conn = DriverManager.getConnection("jdbc:informix-sqli://10.102.217.188:6444/eppix",prop);  
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(sQuery);

		} catch (SQLException ex) {
			ex.printStackTrace();

		}
		return rs;
	}

	public long generateRandom() {
		return (long) (Math.random() * 100000 + 3333300000L);
	}

	/**
	 * Generates random SA ID number using regex expression
	 * 
	 * @return 7712315046081
	 */
	public String GenerateSAIdNumber() {
		String saIdNumber = fakeValuesService.regexify(
				// with spaces and dashes
				// "(((\\d{2}((0[13578]|1[02])(0[1-9]|[12]\\d|3[01])|(0[13456789]|1[012])(0[1-9]|[12]\\d|30)|02(0[1-9]|1\\d|2[0-8])))|([02468][048]|[13579][26])0229))((
				// |-)(\\d{4})( |-)(\\d{3})|(\\d{7}))");
				"(((\\d{2}((0[13578]|1[02])(0[1-9]|[12]\\d|3[01])|(0[13456789]|1[012])(0[1-9]|[12]\\d|30)|02(0[1-9]|1\\d|2[0-8])))|([02468][048]|[13579][26])0229))((\\d{4})(\\d{3})|(\\d{7}))");
		return saIdNumber;
	}

	/**
	 * Generates cell number using prefix. Uses regex pattern from OTP api to verify
	 * number validity
	 * 
	 * @return +27729876541
	 */
	public String GenerateSACellNumber(Boolean valid) {
		String saCellNumber = null;
		if (valid) {
			while (true) {
				saCellNumber = fakeValuesService.regexify("[+](27)[156789][0-9]{8}");
				Pattern pattern = Pattern.compile("[+](27)[156789][0-9]{8}");
				Matcher matcher = pattern.matcher(saCellNumber);
				if (matcher.matches()) {
					break;
				}
			}
			return saCellNumber;
		} else {
			while (true) {
				saCellNumber = fakeValuesService.bothify("+27#########");
				Pattern pattern = Pattern.compile("[+](27)[156789][0-9]{8}");
				Matcher matcher = pattern.matcher(saCellNumber);
				if (!matcher.matches()) {
					break;
				}
			}
			return saCellNumber;
		}
	}

	/**
	 * Generate random birthday using using minimum and maxium age with required
	 * format
	 * 
	 * @param minAge - 18
	 * @param maxAge - 50
	 * @param format - dd/MM/yyyy
	 * @return 04/06/1981
	 */
	public String GenerateRandomBirthdayWithFormat(int minAge, int maxAge, String format) {

		Date date = faker.date().birthday(minAge, maxAge);
		String formattedBirthday = new SimpleDateFormat("dd/MM/yyyy").format(date);
		return formattedBirthday;
	}

	@SuppressWarnings("deprecation")
	public String updateData(String[] ColumnValue, String[] SearchValue, String xmlPath) throws IOException {
		String sBody = null;
		FileInputStream inputStream = new FileInputStream(xmlPath);
		try {
			sBody = IOUtils.toString(inputStream);
			for (int i = 0; i < SearchValue.length; i++) {
				if (sBody.contains(SearchValue[i])) {
					if(SearchValue[i].equalsIgnoreCase("sClientID")) {
						sBody = sBody.replace(SearchValue[i], APIController.clientID);
					}else if(SearchValue[i].equalsIgnoreCase("sClientPaypointID")) {
						sBody = sBody.replace(SearchValue[i], APIController.paypointID);
					}else {
						sBody = sBody.replace(SearchValue[i], ColumnValue[i]);
					}
					
				}

			}

		} finally {
			inputStream.close();
		}
		return sBody;
	}

	public String ReadDataJson(String xmlPath) throws IOException, ParseException {
		File f1 = null;
		FileReader fr = null;

		JSONParser parser = new JSONParser();
		try {
			f1 = new File(xmlPath);
			fr = new FileReader(f1);
			Object obj = parser.parse(fr);
			JSONObject jsonObject = (JSONObject) obj;
			// System.out.print(jsonObject);
			String sBody = jsonObject.toString();
			return sBody;
		} finally {
			try {
				lines.clear();
				Objects.requireNonNull(fr).close();

				// out.close();

			} catch (IOException ioe)

			{
				ioe.printStackTrace();
			}
		}

	}

	@SuppressWarnings("deprecation")
	public String ReadData(String xmlPath) throws IOException {
		String sBody = null;
		FileInputStream inputStream = new FileInputStream(xmlPath);
		try {
			sBody = IOUtils.toString(inputStream);

		} finally {
			inputStream.close();
		}
		return sBody;
	}

	public String[] validateData(String[] data, String outputData, int icount, String sPath, String format,
								 String sRunStatus, Reporting repo, String sheetname, String DescriptURL,
								 String ExecuteScenario, ExtentTest test) throws Exception {
		StringBuilder sOutput = null;
		result = new String[3];
		// String[] sData3 = null;
		if (format.equals("JSON")) {

			for (int v = 0; v < data.length; v++) {
				if (data[v].contains("NotNull")) {
					String[] dataHold = data[v].split("_");
					String[] hold = outputData.split(",");
					for (int l = 0; l < hold.length; l++) {
						if (hold[l].contains(dataHold[0])) {
							String[] dataCheckNotNulll = hold[l].split(":");
							if (dataCheckNotNulll[1].trim().isEmpty() || dataCheckNotNulll[1].trim() == null) {
								if (sOutput == null) {

									sOutput = new StringBuilder(data[v]);
								} else {
									sOutput.append(",").append(data[v]);
								} 
							} 
						}
					} 
				}

				else if (outputData.contains(data[v]) == true) {
					// ValidationResults
					if (sOutput == null) {
						sOutput = new StringBuilder(data[v]);
					} 
				}
			} 

		} else
			
			for (int v = 0; v < data.length; v++) {
				if (data[v].contains("NotNull")) {
					String[] dataHold = data[v].split("_");
					String[] hold = outputData.split(",");
					for (int l = 0; l < hold.length; l++) {
						if (hold[l].contains(dataHold[0])) {
							String[] dataCheckNotNulll = hold[l].split(":");
							if (dataCheckNotNulll[1].trim().isEmpty() || dataCheckNotNulll[1].trim() == null) {
								if (sOutput == null) {

									sOutput = new StringBuilder(data[v]);
								} else {
									sOutput.append(",").append(data[v]);
								} 
							} 
						}
					} 
				}

				else if (outputData.contains(data[v]) == true) {
					// ValidationResults
					if (sOutput == null) {
						sOutput = new StringBuilder(data[v]);
					} 
				}
			} 
		/*
			for (int n = 0; n < data.length; n++) {
				String[] dataValue = data[n].split("_");

				String sData = xmlParser(outputData, data[0], dataValue[0]);

				if (!sData.equals(dataValue[0])) {
					if (sOutput == null) {
						sOutput = new StringBuilder(dataValue[0]);
					} else
						sOutput.append(",").append(dataValue[0]);
				}
			}
		*/
		String[] sColumn = { "ValidationStatus", "ValidationResults" };

		if (sOutput != null) {

			repo.childLogPass("Validation" , data[0],  sOutput.toString(),false,test,utils);

			sRunStatus = "Passed";
			String[] sData3 = { "Passed", "Validation Successful for " + data[0] };

			writeData(sColumn, icount, sData3, sPath, "Excel", null, sheetname);
			result[0] = sRunStatus;
			result[1] = data[0];
			result[2] = sOutput.toString();
			
			if(ExecuteScenario.equalsIgnoreCase("CreateClientHeadOffice")) {
				String[] clientIDSplitOne = outputData.split("id", 5);
				System.out.println("Clietn ID : " + clientIDSplitOne[1]);
				String[] clientIDSplitTwo = clientIDSplitOne[1].split(">");
				System.out.println("Clietn ID : " + clientIDSplitTwo[1]);
				String[] client = clientIDSplitTwo[1].split("<");
				System.out.println("Clietn ID : " + client[0]);
				APIController.clientID = client[0];
			}
			
			if(ExecuteScenario.equalsIgnoreCase("CreateClientPaypoint")) {
				String[] clientIDSplitOne = outputData.split("id", 5);
				System.out.println("Paypint ID : " + clientIDSplitOne[1]);
				String[] clientIDSplitTwo = clientIDSplitOne[1].split(">");
				System.out.println("Paypint ID : " + clientIDSplitTwo[1]);
				String[] client = clientIDSplitTwo[1].split("<");
				System.out.println("Paypint ID : " + client[0]);
				APIController.paypointID = client[0];
			}
		} else {

			repo.childLogFail("Validation" ,data[0],  data[0],false, test,utils);

			sRunStatus = "Failed";
			String[] sData3 = { "Failed", "Validation failed for : " + data[0] };

			writeData(sColumn, icount, sData3, sPath, "Excel", null, sheetname);
			result[0] = sRunStatus;
			result[1] = data[0];
			result[2] = "Validation failed ";

		}

		//soft.assertEquals(sOutput, data[0]);
		return result;

	}

	public String validateDataJson(String[] data, String outputData, int icount, String sPath, String format,
			String sRunStatus, Reporting repo, String sheetname, ExtentTest test) throws Exception {
		StringBuilder sOutput = null;
		// String[] sData3 = null;
		if (format.equals("JSON")) {

			for (int v = 0; v < data.length; v++) {
				if (data[v].contains("NotNull")) {
					String[] dataHold = data[v].split("_");
					String[] hold = outputData.split(",");
					for (int l = 0; l < hold.length; l++) {
						if (hold[l].contains(dataHold[0])) {
							String[] dataCheckNotNulll = hold[l].split(":");
							if (dataCheckNotNulll[1].trim().isEmpty() || dataCheckNotNulll[1].trim() == null) {
								if (sOutput == null) {

									sOutput = new StringBuilder(data[v]);
								} else {
									sOutput.append(",").append(data[v]);
								} 
							} 

						} 
					} 

				}

				else if (outputData.contains(data[v]) == true) {
					// ValidationResults
					if (sOutput == null) {
						sOutput = new StringBuilder(data[v]);
					} 
				}
			} 

		} else {
			
			for (int n = 0; n < data.length; n++) {
				String[] dataValue = data[n].split("_");

				String sData = xmlParser(outputData, data[0], dataValue[0]);

				if (sData.equals(dataValue[0]) == false) {
					if (sOutput == null) {
						sOutput = new StringBuilder(dataValue[0]);
					} else {
						sOutput.append(",").append(dataValue[0]);
					} 
				}
			}
		}
		String[] sColumn = { "ValidationStatus", "ValidationResults" };

		if (sOutput != null) {

			//repo.childLogPass(sStepName, sExpected, sActual, Screenshot, logger, utils);("<b> Expected results : </b><br/>" + data[0] + "<br/><b> Actual results : </b><br/>" + sOutput,false, utils,test);

			sRunStatus = sRunStatus + " Passed";
			String[] sData3 = { "Passed", "Validation Successful for " + data[0] };

			writeData(sColumn, icount, sData3, sPath, "Excel", null, sheetname);
		} else {
			sRunStatus = sRunStatus + " Failed";
			repo.ExtentLogFail("<b>Expected results: </b><br/>" + data[0]
					+ "<br/><b> Actual : Validation failed for </b><br/>" + sOutput, false, utils, test);

			String[] sData3 = { "Failed", "Validation failed for " + data[0] };
			writeData(sColumn, icount, sData3, sPath, "Excel", null, sheetname);

		}
		return sRunStatus;

	}

	public String xmlParser(String outputData, String tagName, String fieldName)throws SAXException, IOException, ParserConfigurationException {
		
		String element = null;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(outputData));
		org.w3c.dom.Document doc = dBuilder.parse(is);
		
		try {
			
			

			doc.getDocumentElement().normalize();

			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName(tagName);

			System.out.println("----------------------------");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					element = eElement.getElementsByTagName(tagName).item(0).getTextContent();

				}
			} 
			
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return element;
	}
}
