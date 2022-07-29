package simswap_tests;

import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import ilab.generic.functions.commonBase;
import org.apache.poi.ss.usermodel.Sheet;




public class Get_The_Status_Of_A_Sim_Swap_API_Remove_System_Name extends commonBase{
	
	String DefaultPath = System.getProperty("user.dir");
	String TestData = "\\TestData\\API\\VodacomAPI.xlsx";
    public static ExtentTest parent;
    public static ExtentTest test;
    public String TCID = null, TestName = null;
	
	
	
    

@Test
public void simswap() throws Exception {
	
	try {

        String Execute = null, ScenarioName = null, ScenarioCategory = null, UserName = null, Password = null,
               ExecuteScenario = null, DBURL = null, Query = null, Status = null, Message = null, Scenario = null
               , TestCaseID = null;

        data.ReadExcelWorkbook(DefaultPath + TestData);
        initialiseFunctions();
        sDataType = data.getDataType();

        Sheet sheetController = data.workbook.getSheet("Controller");

        int rowcount = data.workbook.getSheet("Controller").getPhysicalNumberOfRows();

        for (int i = 1; i < rowcount; i++) {
            try {

                Execute = data.getCellData("Execute", i, sheetController, null, null, sDataType);
                ScenarioName = data.getCellData("ScenarioName", i, sheetController, null, null, sDataType);
                ScenarioCategory = data.getCellData("ScenarioCategory", i, sheetController, null, null, sDataType);
                                              
         
                if (Execute.toUpperCase().equals("YES")) {

                    if (ScenarioName.equalsIgnoreCase("GET_SIMSWAP_STATUS")) {
                        //setupDriver();
                        //utils.navigate(URL);

                        //parent = repo.setParent(ScenarioCategory + " : " + ScenarioName);
                        // Switch to the applicable Sheet
                        Sheet ScenarioSheet = data.workbook.getSheet(ScenarioName);
                        int rwcount = data.workbook.getSheet(ScenarioName).getPhysicalNumberOfRows();

                        for (int j = 1; j < rwcount; j++) {
                            try {
                                ExecuteScenario = data.getCellData("Execute", j, ScenarioSheet, null, null, sDataType);

                                if (ExecuteScenario.toUpperCase().equals("YES")) {
                                    //Login Credentials
                                	TestCaseID = data.getCellData("TestCaseID", j, ScenarioSheet, null, null, sDataType); 
                                	Scenario = data.getCellData("Scenario", j, ScenarioSheet, null, null, sDataType);
                                    UserName = data.getCellData("Username", j, ScenarioSheet, null, null, sDataType);
                                    Password = data.getCellData("Password", j, ScenarioSheet, null, null, sDataType);
                                    DBURL = data.getCellData("DBURL", j, ScenarioSheet, null, null, sDataType);
                                    Query = data.getCellData("Query", j, ScenarioSheet, null, null, sDataType);
                                    Status = data.getCellData("Status", j, ScenarioSheet, null, null, sDataType);
                                    Message = data.getCellData("Message", j, ScenarioSheet, null, null, sDataType);
                                                                        
                                                                        
                                    ///////
                                    
                                    parent = repo.setParent(ScenarioCategory + " : " + ScenarioName);
                                	test = repo.setChildNode(TestCaseID + " : " + Scenario, parent);
                                	
                                	
                                	getsimswapstatus.getsimswapstatus(DBURL, UserName, Password, Query, Status, Message, test);
                                   /////

                                }
                            } catch (Exception e) {
                                if (e.getMessage() == null) {
                                    System.out.println("null point exception For Scenario Beneficiaries" + e.getMessage());
                                    continue;
                                }
                                System.out.println("Scenario Beneficiaries loop Error : " + e.getMessage());
                            }

                            //send email report via email
                            if (Boolean.parseBoolean(data.getSendEmail())) {
                                utils.SendEmail(data.getEmailFrom(), data.getEmailTo(), data.getReportName());
                            }

                        }
                    }
                }

            } catch (Exception e) {
                if (e.getMessage() == null) {
                    System.out.println("null point exception for Beneficiaries Controller" + e.getMessage());
                    continue;
                }
                System.out.println("Controller Error for Beneficiaries : " + e.getMessage());
            }
        }
    } catch (Exception e) {
        repo.childLogError("<b>Run time exception : </b><br/>" + e.getMessage(), parent);
    }
	
	

}



}
