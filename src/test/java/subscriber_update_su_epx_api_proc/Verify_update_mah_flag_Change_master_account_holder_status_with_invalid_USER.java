package subscriber_update_su_epx_api_proc;

import java.sql.ResultSet;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import ilab.generic.functions.commonBase;

public class Verify_update_mah_flag_Change_master_account_holder_status_with_invalid_USER extends commonBase {
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
	  TCID = "IES-817";                            //Jira ticket 
	  	parent = repo.setParent(TCID +"Verify_update_mah_flag_Change_master_account_holder_status_with_invalid_USER");
	  	test = repo.setChildNode("Verify_update_mah_flag_Change_master_account_holder_status_with_invalid_USER", parent);
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

		String strSwapMAH = "execute procedure su_epx_api('UPDATE_MAH_FLAG','MSISDN=["+ sMSISDN[1]
				+ "],MAH_FLAG=[Y],APPLICATION_ID=[MII],USER=[!!!!],','','','','')";
		resultset = data.ConnectAndQueryEPPIXServer("", "", "", strSwapMAH);
			
			
		// Get the query results and store them in a variable
				String sResults2 = data.getQueryResults(resultset, 1);
				if (sResults2 != null) {

					// Split the string with a comma and compare Status and the Description
					String[] Compare = sResults2.split(",");
					if (Compare[1].equals("EPX_STATUS=[C]") && Compare[2].equalsIgnoreCase("MESSAGE=[MAH Flag successfully changed]")) {
						 System.out.println(Compare[1]+","+Compare[2]);
						 	repo.childLogPass("Verify_update_mah_flag_Change_master_account_holder_status_with_invalid_USER",
				                    ""+Compare[1]+","+Compare[2]+"",
				                     ""+Compare[1]+","+Compare[2]+"", false, test, utils); 
					}

					else {
						System.out.println("Did not update master account holder status invalid User");
						repo.childLogFail("Verify subscriber details updated successfully ",
								"subscriber details NOT Successfully updated", sResults2, false, test, utils);
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