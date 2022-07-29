
//scripted by: Doctor Dlamini
//Date : 04 March 2020


package Bulk_vas_Activation_epx_blk_vas_api_proc;

import java.sql.ResultSet;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import ilab.generic.functions.commonBase;

public class activate_BARO extends commonBase {
	
	//Declarations
	String DefaultPath = System.getProperty("user.dir");
	public static ExtentTest parent;
	public String TCID = null, TestName = null;
	public static ExtentTest test;
	ResultSet resultset ,resultset1= null;	
	String[] sAccountNumber = new String[3];
	String[] sDealerID = new String[3];
	String[] sSalesmanCode = new String[3];
	String[] sEmail = new String[3];
	String[] sAction = new String[3];
	String[] sUser = new String[3];
	String[] sStatus = new String[2];
	String[] sMessage= new String[2];
	
@BeforeMethod
	  public void beforeMethod() {
		  try {
			  data.ReadExcelWorkbook(DefaultPath + "\\TestData\\API\\DataAPI.xlsx");
			  	setupReport();
		  } catch (Exception e) {
				System.out.println("Before Test Error : " + e.getMessage());
			}
	  }

@Test	
public void f() throws Exception {
		  TCID = "IES-382";                                         //JIRA ticket number 
		  	parent = repo.setParent(TCID +"Activate_BARO(Softlock) N2N = N");
		  		test = repo.setChildNode("Activate_BARO(Soft lock) N2N = N", parent);
		  			resultset = data.ConnectAndQueryEPPIXServer("", "", "", "select first 3 ebv_account,ebv_dealer_id,ebv_salesman,ebv_service_code,ebv_email,ebv_email_address,ebv_action,ebv_user from epx_blk_vas");
		  			
		  	if (resultset != null) {

				for (int i = 1; i < 3; i++) {
					 
					//Select data from the SQL Query
					sAccountNumber[i] = data.getCellData("ebv_account", i, null, null, resultset, "SQLSERVER").trim();
						sDealerID[i] = data.getCellData("ebv_dealer_id", i, null, null, resultset, "SQLSERVER").trim();
							sSalesmanCode[i] = data.getCellData("ebv_salesman", i, null, null, resultset, "SQLSERVER").trim();
								sEmail[i] = data.getCellData("ebv_email_address", i, null, null, resultset, "SQLSERVER").trim();
									sAction[i] = data.getCellData("ebv_action", i, null, null, resultset, "SQLSERVER").trim();
										sUser[i] = data.getCellData("ebv_user", i, null, null, resultset, "SQLSERVER").trim();

				}
				
				//Print and display data from the SQL Query
				System.out.println("AccountNumber :" + sAccountNumber[1]);
					System.out.println("DealerID: " + sDealerID[1]);
						System.out.println("Salesman Code :" + sSalesmanCode[1]);
							System.out.println("Email address :" + sEmail[1]);
								System.out.println("Action :" + sAction[1]);
									System.out.println("User :" + sUser[1]);
				
				repo.childLogPass("Getting Account Number and Dealer ID","Account Number obtained :" + sAccountNumber[1] + " and Dealer ID: " + sDealerID[1],
						"Account Number obtained :" + sAccountNumber[1] + " and Dealer ID :" + sDealerID[1], false, test, utils);

			} else {
				System.out.println("NO records returned");
					repo.childLogFail("Getting Account Number and Dealer ID",
						"Account Number obtained :" + sAccountNumber[1] + " and Dealer ID: " + sDealerID,
							"Account Number obtained :" + sAccountNumber[1] + " and sAccountNumber :" + sDealerID, false, test, utils);
			}
				
		String strActivateBaro = "EXECUTE PROCEDURE epx_blk_vas_api_proc('["+ sAccountNumber[1] +" ]','','["+ sSalesmanCode[1] +"]','ATT','N','["+ sEmail[1] +"]','["+ sAction+"]','["+sUser+"]')";
				resultset1 = data.ConnectAndQueryEPPIXServer("", "", "", strActivateBaro);	
				
				
				// Get the query results and store them in a variable
				String sResults2 = data.getQueryResults(resultset1, 1);
				if (sResults2 != null) {

					//iterate and compare Status and the Description
					for (int i = 1; i < 2; i++) {
						 
						//Select data from the SQL Query
						sStatus[i] = data.getCellData("ebvstatus", i, null, null, resultset1, "SQLSERVER").trim();
							sMessage[i] = data.getCellData("ebvstatusdesc", i, null, null, resultset1, "SQLSERVER").trim();
						
						System.out.println(sStatus[i]);
							System.out.println(sMessage[i]);
					}
					
							
					if (sStatus[1].equals("C") && sMessage[1].contains("Request has been succefully logged: Ref: ")) {
						// System.out.println(Compare[1]+","+Compare[1]);
						 	repo.childLogPass("Activate_BARO", "Status="+sStatus[1]+", Message="+sMessage[1]+"","Status="+sStatus[1]+", Message="+sMessage[1]+"", false, test, utils); 
						 	
					}

					else {
						System.out.println("Did not Activate_Baro service, Please Investigate");
						repo.childLogFail("Verify Baro Activated successfully",
								"Baro Activated successfully", sResults2, false, test, utils);
					}
				}

				else {
					System.out.println("Function could not be executed");
					repo.childLogError("<b>Function could not be executed </b><br/>" + sResults2, test);
				}
			

			}
@AfterMethod
public void afterMethod()
	{
		try {
		  	 
		  	repo.getExtent().flush();
		 } catch (Exception e) {
		  		System.out.print("After Test Error :" + e.getMessage());
		  		}
			}

	 }