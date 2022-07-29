package subscriber_update_su_epx_api_proc;

import java.sql.ResultSet;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import ilab.generic.functions.commonBase;

public class Verify_get_linked_accs_Retrieve_subscribers_linked_to_a_specified_MSISDN_with_invalid_MSISDN extends commonBase {
	
	String DefaultPath = System.getProperty("user.dir");
    public static ExtentTest parent;
    public String TCID = null, TestName = null;
    public static ExtentTest test;
    ResultSet resultset = null;             
    String[] sMSISDN = new String[3];
    String name;
    
    //String[] sIDnumber = new String[3];
    
    
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
		      TCID = "696";
		                   parent = repo.setParent(TCID +"Verify get linked accs - Retrieve the master account holder for a specified MSISDN with an invalid MSISDN");
		                   test = repo.setChildNode("Verify get linked accs - Retrieve the master account holder for a specified MSISDN with an invalid MSISDN", parent);
		                   resultset = data.ConnectAndQueryEPPIXServer("", "", "", "select first 5 vam_msisdn_no,mad_account, * from mad_master_acc_details, vam_active_msisdn where mad_subs_id = vam_subscriber_id and mad_mah_flag = 'Y' AND vam_stat_code = 1");
		                                                 
		                   if (resultset != null) {
		
		                                  for (int i = 1; i < 3; i++) {
		
		                                  sMSISDN[i] = data.getCellData("vam_msisdn_no", i, null, null, resultset, "SQLSERVER").trim();
		                                                 
		                                  }
		                                  //System.out.println("MSISDN :" + sMSISDN[1]);
		                                  repo.childLogPass("Verify get linked accs - Retrieve the master account holder for a specified MSISDN with an invalid MSISDN","Retrieved the master account holder for a specified MSISDN with blank MSISDN",
		                                                                "Retrieved the master account holder for a specified MSISDN with blank MSISDN", false, test, utils);
		
		                   } else {
		                                  System.out.println("NO records returned");
		                                  repo.childLogFail("Verify query mah priv - Retrieve master account holder privileges without User","Retrieved master account holder privileges without User",
		                                		  						"Retrieved master account holder privileges", false, test, utils);
		                   }
		
		                   // EXECUTE PROCEDURE su_epx_api('UPD_SBD_BASIC')
		                   String query_mah_retrieve = "execute procedure su_epx_api('GET_LINKED_ACCS','MSISDN=[],USER=[D_TJ125_PRINCE],','','','','')";
		                   resultset = data.ConnectAndQueryEPPIXServer("", "", "", query_mah_retrieve);
		
		                   // Get the query results and store them in a variable
		                   String sResults2 = data.getQueryResults(resultset, 1);
		                   if (sResults2 != null) {
		
		                                  // Split the string with a comma and compare Status and the Description
		                                  String[] Compare = sResults2.split(",");
		                                  if (Compare[1].equals("EPX_STATUS=[E]") && Compare[2].equalsIgnoreCase("MESSAGE=[MSISDN does not Exist]")) {
		                                                 System.out.println(Compare[1]+","+Compare[2]);
		                                                 repo.childLogPass("Verify get linked accs - Retrieve the master account holder for a specified MSISDN with blank MSISDN",
		                                                                               ""+Compare[1]+","+Compare[2]+"",
		                                                                               ""+Compare[1]+","+Compare[2]+"", false, test, utils);
		
		                                  }
		
		                                  else {
		                                                 System.out.println("Did not update subscriber details with valid ID number");
		                                                 repo.childLogFail("Verify master account holders were swapped successfully ",
		                                                                               "MAH Successfully swapped", sResults2, false, test, utils);
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
