package ilab.api.tests;

import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;
import ilab.generic.functions.commonBase;

public class APIController extends commonBase {

	String sBody = null, sxmlPath = null, sUpdatedPath = null, sResponse = null, sIpurl = null, sSOAPAction = null;
	String sDataType = null, sScenaTestName = null, sValidation = null, sDataToValidate = null;
	String sRunStatusOne = null,sJSONValidation = null, sIdTokenRequired = null, sAccessTokenReq = null;
	String sRunStatusTwo = null,sMethod = null, sReadData = null, sType = null, sFormat = null,xmlPart1 = null,xmlPart2 = null;
	String sJsonResponseField = null, sUserName = null, sPassWord = null, sResource = null, sParameter = null;
	String[] RunResult = null;

	long iRequestID, iSessionID;
	int iExpected;
	String DefaultPath = System.getProperty("user.dir");
	public static ExtentTest test;
	public static ExtentTest parent;
	public static String clientID;
	public static String paypointID;

	SoftAssert soft = new SoftAssert();

	@BeforeMethod
	public void beforeMethod() {
		try {
			data.ReadExcelWorkbook(DefaultPath + "\\TestData\\API\\DataAPI.xlsx");
			setupReport();
		} catch (Exception e) {
			System.out.println("Before Test Error : " + e.getMessage());
		}
	}

	@SuppressWarnings("unused")
	@Test
	public void APITestExecution() throws Exception {
		try {
			String Execute = null, TestName = null, ExecuteScenario = null, TCID = null;
		//	initialiseFunctions();
			sDataType = data.getDataType();
			String[] sColumnValue = new String[50];

			Sheet sheetController = data.workbook.getSheet("Controller");

			int rowcount = data.workbook.getSheet("Controller").getPhysicalNumberOfRows();

			for (int i = 1; i <= rowcount; i++) {
				try {

					// Read the execute sheet values
					Execute = data.getCellData("Execute", i, sheetController, null, null, sDataType);
					TestName = data.getCellData("TestName", i, sheetController, null, null, sDataType);
					TCID = data.getCellData("TCID", i, sheetController, null, null, sDataType);
					if (Execute.toUpperCase().equals("YES")) {
						try {

							parent = repo.setParent(TCID +" : "+TestName);
							// Switch to the applicable Sheet
							Sheet newsheet = data.workbook.getSheet(TestName);
							int rwcount = data.workbook.getSheet(TestName).getPhysicalNumberOfRows();

							for (int j = 1; j < rwcount; j++) {

								ExecuteScenario = data.getCellData("ExecuteScenario", j, newsheet, null, null,sDataType);

								// Execute the specific scenario
								if (ExecuteScenario.toUpperCase().equals("YES")) {

									try {

										sIpurl = data.getCellData("EndPoint", j, newsheet, null, null, sDataType);
										sxmlPath = data.getCellData("PAYLOAD", j, newsheet, null, null, sDataType);
										iRequestID = data.generateRandom();
										iSessionID = data.generateRandom();
										sReadData = data.getCellData("UpdateData", j, newsheet, null, null, sDataType);
										sType = data.getCellData("webservicetype", j, newsheet, null, null, sDataType);
										sFormat = data.getCellData("format", j, newsheet, null, null, sDataType);
										sValidation = data.getCellData("Validation", j, newsheet, null, null,	sDataType);
										sDataToValidate = data.getCellData("DataToValidate", j, newsheet, null, null,	sDataType);
										iExpected = Integer.parseInt(data.getCellData("Expected", j, newsheet, null, null, sDataType));
										sJsonResponseField = data.getCellData("JsonResponseField", j, newsheet, null,null, sDataType);
										sUserName = data.getCellData("UserName", j, newsheet, null, null, sDataType);
										sPassWord = data.getCellData("PassWord", j, newsheet, null, null, sDataType);
										sResource = data.getCellData("Resource", j, newsheet, null, null, sDataType);
										sParameter = data.getCellData("Parameter", j, newsheet, null, null, sDataType);
										sJSONValidation = data.getCellData("JSONValidation", j, newsheet, null, null,	sDataType);
										sScenaTestName = data.getCellData("Scenario", j, newsheet, null, null,sDataType);
										sIdTokenRequired = data.getCellData("IdTokenReq", j, newsheet, null, null,sDataType);
										sAccessTokenReq = data.getCellData("AccessTokenReq", j, newsheet, null, null,	sDataType);

										sIpurl = sIpurl + sResource + sParameter;

										String[] Output = null;

										test = repo.setChildNode(sScenaTestName, parent);
										try {
											// Updates the xml parameters with the values
											if (sxmlPath.isEmpty() == false) {
												if (sReadData == "false") {
													if (sFormat.toUpperCase().equals("JSON")) {
														sBody = data.ReadDataJson(DefaultPath + "\\XMLInputData\\" + sxmlPath);
													} else {
														sBody = data.ReadData(DefaultPath + "\\XMLInputData\\" + sxmlPath);
													}
												} else {
													String sDataToUpdate = data.getCellData("DataToUpdate", j, newsheet,null, null, sDataType);
													String[] sSearchValue = sDataToUpdate.split(",");
													for (int s = 0; s < sSearchValue.length; s++) {
														sColumnValue[s] = data.getCellData(sSearchValue[s], j, newsheet,null, null, sDataType);
													}

													// Updates the xml parameters with the values
													sBody = data.updateData(sColumnValue, sSearchValue,DefaultPath + "\\XMLInputData\\" + sxmlPath);
												}
											} else {
												sBody = null;
											}

											if (sType.toUpperCase().equals("REST")) {

												sMethod = data.getCellData("Method", j, newsheet, null, null,	sDataType);
												// get the response and code using the specific request for the test												
												Output = api.WebServiceContentREST(sIpurl, sMethod,data.getCellData("ContentType", j, newsheet,	null, null,sDataType),sBody, sUserName, sPassWord);									
												

											} else {
																								
												sSOAPAction = data.getCellData("SOAPAction", j, newsheet, null, null,	sDataType);
												Output = api.WebServiceContentTextSOAP(sBody, sIpurl, data.getCellData("SOAPAction", j, newsheet, null, null, sDataType));
												//api.WriteJsonOutputBodyToFile(Output[0], sScenaTestName);
											}
											
											// Put the End Point Used on the report
											test.info("<b>End Point Utitlised for Test : </b><br/>" + sIpurl);
											
											xmlPart1 = sBody.replace("<", "{");
											xmlPart2 = xmlPart1.replace(">", "}");
											test.info("<b>Request Message : </b><br/>" + xmlPart2);

											if (sValidation.toUpperCase().equals("TRUE")) {
												String[] Validation = sDataToValidate.split(",");
												RunResult = data.validateData(Validation, Output[0], j,DefaultPath + "\\TestData\\API\\DataAPI.xlsx", sFormat,sRunStatusOne, repo, TestName, sIpurl,sScenaTestName, test);
												sRunStatusOne = RunResult[0];
											}

											// excel columns to write data output
											String[] sColumn = { "Actual", "Status", "Response" };
											//String[] sColumn = { "Actual", "Status" };

											// Report in extent reports and also in the excel sheet
											if (Output[1] != null) {
												if (Integer.parseInt(Output[1]) == iExpected) {
													sRunStatusTwo = "Passed";
													String[] sData = { Output[1], "Passed", Output[0] };
													//String[] sData = { Output[1], "Passed" };
													data.writeData(sColumn, j, sData,DefaultPath + "\\TestData\\API\\DataAPI.xlsx", sDataType, null,TestName);									
													repo.childLogPass("Status Code ", "Status Code Expected :"+ iExpected, "Status Code Returned: " + Output[1],false,test,utils);
													xmlPart1 = Output[0].replace("<", "{");
													xmlPart2 = xmlPart1.replace(">", "}");
													test.info("<b>Response Message : </b><br/>" + xmlPart2);
												} else {
													sRunStatusTwo = "Failed";
													String[] sData2 = { Output[1], "Failed", Output[0] };
													data.writeData(sColumn, j, sData2,DefaultPath + "\\TestData\\API\\DataAPI.xlsx", sDataType, null,TestName);
													repo.childLogFail("Status Code ", "Status Code Expected :"+ iExpected, "Status Code Returned: " + Output[1],false,test,utils);
													xmlPart1 = Output[0].replace("<", "{");
													xmlPart2 = xmlPart1.replace(">", "}");
													test.info("<b>Response Message : </b><br/>" + xmlPart2);

												}
											} else {
												sRunStatusTwo = "Failed";
												String[] sData2 = { Output[1], "Failed", Output[0] };
												data.writeData(sColumn, j, sData2, DefaultPath + "\\TestData\\API\\DataAPI.xlsx", sDataType, null,TestName);

												repo.childLogFail("Status Code ", "Status Code Expected :"+ iExpected, "Status Code Returned: " + Output[1],false,test,utils);
												xmlPart1 = Output[0].replace("<", "{");
												xmlPart2 = xmlPart1.replace(">", "}");
												test.info("<b>Response Message : </b><br/>" + xmlPart2);
											}

											System.out.println(
													"Test Execution completed for scenario : " + sScenaTestName);

										} catch (Exception e) {

											if (e.getMessage() == null) {
												System.out.println("null point exception" + e.getMessage());
												continue;
											}

											String[] sColumn = { "Status", "Response" };
											sRunStatusOne = "Failed";
											String[] Data = { "Failed", e.getMessage() };
											data.writeData(sColumn, j, Data, DefaultPath + "\\TestData\\API\\DataAPI.xlsx","Excel", null, TestName);
											repo.childLogError("<b>Run time exception : </b><br/>" + e, test);
											System.out.print(e.getMessage());
										}
									} catch (Exception e) {
										if (e.getMessage() == null) {
											System.out.println("null point exception" + e.getMessage());
											continue;
										}
										System.out.println("Scenario loop Error : " + e.getMessage());
									}
								}

							}
							// send email report via email
							if (Boolean.parseBoolean(data.getSendEmail())) {
								utils.SendEmail(data.getEmailFrom(), data.getEmailTo(), data.getReportName());
							}
						} catch (Exception e) {
							if (e.getMessage() == null) {
								System.out.println("null point exception" + e.getStackTrace());
								continue;
							}
							System.out.println("Execute Test From " + TestName + " Controller Error : " + e.getMessage());
						}

					}
				} catch (Exception e) {
					if (e.getMessage() == null) {
						System.out.println("null point exception" + e.getMessage());
						continue;
					}
					System.out.println("Controller Error : " + e.getMessage());
				}

			}

		} catch (Exception e) {

			repo.childLogError("Run time exception : " + e, parent);
			System.out.print("Test Error :" + e.getMessage());
		}

	}

	@AfterMethod
	public void afterMethod() throws Exception {
		try {
			repo.getExtent().flush();
		} catch (Exception e) {
			System.out.print("After Test Error :" + e.getMessage());
		}
	}
}