package subscriber_update_su_epx_api_proc;

import java.sql.ResultSet;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import ilab.generic.functions.commonBase;

public class Verify_query_most_dialled_Retrieve_most_dialled_MSISDN_without_USER extends commonBase {
	
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
		                   parent = repo.setParent(TCID +"Verify query most dialled - Retrieve most dialled MSISDN without user");
		                   test = repo.setChildNode("Verify query most dialled - Retrieve most dialled MSISDN without user", parent);
		                   resultset = data.ConnectAndQueryEPPIXServer("", "", "", "select * from call_0120200301");
		                                                 
		                   if (resultset != null) {
		
		                                  for (int i = 1; i < 3; i++) {
		
		                                  sMSISDN[i] = data.getCellData("vam_msisdn_no", i, null, null, resultset, "SQLSERVER").trim();
		                                                 
		                                  }
		                                  //System.out.println("MSISDN :" + sMSISDN[1]);
		                                  repo.childLogPass("Verify query most dialled - Retrieve most dialled MSISDN","Retrieved most dialled MSISDN",
		                                                                "Retrieved the master account holder for a specified MSISDN with blank MSISDN", false, test, utils);
		
		                   } else {
		                                  System.out.println("NO records returned");
		                                  repo.childLogFail("Verify query mah priv - Retrieve master account holder privileges without User","Retrieved master account holder privileges without User",
		                                		  						"Retrieved master account holder privileges", false, test, utils);
		                   }
		
		                   // EXECUTE PROCEDURE su_epx_api('UPD_SBD_BASIC')
		                   String query_mah_retrieve = "execute procedure su_epx_api('QUERY_MOST_DIALLED',' USER=[],MSISDN=["+sMSISDN[1]+"],','','','','')";
		                   resultset = data.ConnectAndQueryEPPIXServer("", "", "", query_mah_retrieve);
		
		                   // Get the query results and store them in a variable
		                   String sResults2 = data.getQueryResults(resultset, 1);
		                   if (sResults2 != null) {
		
		                                  // Split the string with a comma and compare Status and the Description
		                                  String[] Compare = sResults2.split(",");
		                                  if (Compare[1].equals("EPX_STATUS=[E]") && Compare[2].equalsIgnoreCase("MESSAGE=[Back-end process may have terminated abnormally. The request id is 1041259023.,]")) {
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
