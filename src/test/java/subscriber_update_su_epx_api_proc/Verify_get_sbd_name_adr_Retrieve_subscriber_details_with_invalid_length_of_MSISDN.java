package subscriber_update_su_epx_api_proc;

import java.sql.ResultSet;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import ilab.generic.functions.commonBase;

public class Verify_get_sbd_name_adr_Retrieve_subscriber_details_with_invalid_length_of_MSISDN extends commonBase {
	String DefaultPath = System.getProperty("user.dir");
	public static ExtentTest parent;
	public String TCID = null, TestName = null;
	public static ExtentTest test;
	ResultSet resultset = null;	
	String[] sMSISDN = new String[3];
	String[] sFirstname = new String[3];
	
	
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
	  TCID = "690";
	  	parent = repo.setParent(TCID +"Verify_get_sbd_name_adr_Retrieve_subscriber_details_with_invalid_length_of_MSISDN");
	  	test = repo.setChildNode("Verify_get_sbd_name_adr_Retrieve_subscriber_details_with_invalid_length_of_MSISDN", parent);
	  	resultset = data.ConnectAndQueryEPPIXServer("", "", "", "select first 2 * from sbd_sub_dets,sba_sub_aux, vam_active_msisdn where vam_msisdn_no = sbd_dialling_no and sbd_subscriber_id = sba_subscriber_id and vam_stat_code = '1' and sbd_gender Is Not Null and sbd_dob Is Not Null and sbd_fax Is Not Null and sba_id_number Is Not Null and sbd_email <> '' and sbd_email Is Not Null and sbd_dob Is Not Null and sbd_fax Is Not Null");
	  			
	  	if (resultset != null) {

			for (int i = 1; i < 3; i++) {

				sMSISDN[i] = data.getCellData("sbd_dialling_no", i, null, null, resultset, "SQLSERVER").trim();
				sFirstname[i] = data.getCellData("sbd_firstname", i, null, null, resultset, "SQLSERVER").trim();

			}
			System.out.println("MSISDN :" + sMSISDN[1]);
			System.out.println("Subscriber Firstname : " + sFirstname[1]);
			repo.childLogPass("Getting MSISDN and Subscriber Firstname","MSISDN obtained :" + sMSISDN[1] + " and Subscriber Firstname: " + sFirstname[1],
					"MSISDN obtained :" + sMSISDN[1] + " and Subscriber Firstname :" + sFirstname[1], false, test, utils);

		} else {
			System.out.println("NO records returned");
			repo.childLogFail("Getting MSISDN and Firstname",
					"MSISDN obtained :" + sMSISDN[1] + " and Subscriber Firstname: " + sFirstname,
					"MSISDN obtained :" + sMSISDN[1] + " and Subscriber Firstname :" + sFirstname, false, test, utils);
		
		}

		String strSwapMAH = "execute procedure su_epx_api('GET_SBD_NAME_ADR','MSISDN=[8241336030],USER=[viandah],','','','','')";
		resultset = data.ConnectAndQueryEPPIXServer("", "", "", strSwapMAH);
			
			
		// Get the query results and store them in a variable
				String sResults2 = data.getQueryResults(resultset, 1);
				if (sResults2 != null) {

					// Split the string with a comma and compare Status and the Description
					String[] Compare = sResults2.split(",");
					if (Compare[1].equals("EPX_STATUS=[E]") && Compare[2].equalsIgnoreCase("MESSAGE=[Select from vam_active_msisdn table Failed]")) {
						 System.out.println(Compare[1]+","+Compare[2]);
						 	repo.childLogPass("Verify upd_sbd_basic- Update subscriber details with invalid length of MSISDN",
				                    ""+Compare[1]+","+Compare[2]+"",
				                     ""+Compare[1]+","+Compare[2]+"", false, test, utils); 
					}

					else {
						System.out.println("Did not update subscriber details with invalid length of MSISDN");
						repo.childLogFail("Verify subscriber details updated successfully ",
								"subscriber details Successfully updated", sResults2, false, test, utils);
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