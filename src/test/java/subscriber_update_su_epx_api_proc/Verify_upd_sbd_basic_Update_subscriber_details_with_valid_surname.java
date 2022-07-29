
//Automated By :  Doctor Dlamini
//Date 		 :  21/02/2020


package subscriber_update_su_epx_api_proc;

import java.sql.ResultSet;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import ilab.generic.functions.commonBase;

public class Verify_upd_sbd_basic_Update_subscriber_details_with_valid_surname extends commonBase {
	String DefaultPath = System.getProperty("user.dir");
	public static ExtentTest parent;
	public String TCID = null, TestName = null;
	public static ExtentTest test;
	ResultSet resultset = null;	
	String[] sMSISDN = new String[3];
	String[] sSurname = new String[3];
	
	
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
	  TCID = "698";
	  	parent = repo.setParent(TCID +"Verify upd_sbd_basic Update subscriber details with valid Surname");
	  	test = repo.setChildNode("Verify upd_sbd_basic Update subscriber details with valid Surname", parent);
	  	resultset = data.ConnectAndQueryEPPIXServer("", "", "", "select first 2 * from sbd_sub_dets,sba_sub_aux, vam_active_msisdn where vam_msisdn_no = sbd_dialling_no and sbd_subscriber_id = sba_subscriber_id and vam_stat_code = '1' and sbd_gender Is Not Null and sbd_dob Is Not Null and sbd_fax Is Not Null and sba_id_number Is Not Null and sbd_email <> '' and sbd_email Is Not Null and sbd_dob Is Not Null and sbd_fax Is Not Null");
	  			
	  	if (resultset != null) {

			for (int i = 1; i < 3; i++) {

				sMSISDN[i] = data.getCellData("sbd_dialling_no", i, null, null, resultset, "SQLSERVER").trim();
				sSurname[i] = data.getCellData("sbd_Surname", i, null, null, resultset, "SQLSERVER").trim();

			}
			System.out.println("MSISDN :" + sMSISDN[1]);
			System.out.println("Surname: " + sSurname[1]);
			repo.childLogPass("Getting MSISDN and Surname","MSISDN obtained :" + sMSISDN[1] + " and Surname: " + sSurname[1],
					"MSISDN obtained :" + sMSISDN[1] + " and Surname :" + sSurname[1], false, test, utils);

		} else {
			System.out.println("NO records returned");
			repo.childLogFail("Getting MSISDN and Surname",
					"MSISDN obtained :" + sMSISDN[1] + " and Surname: " + sSurname,
					"MSISDN obtained :" + sMSISDN[1] + " and Surname :" + sSurname, false, test, utils);
		
		}

		String strSwapMAH = "execute procedure su_epx_api('UPD_SBD_BASIC','MSISDN=[" + sMSISDN[1]
				+ "],USER=[duraanwy],OC3PSWD=[gn225],LEGALPSWD=[ga],SSPSWD=[1ss],TEST_SPONS_SIM=[2],COST_CENTRE=[181700]','sbd_address1=[15 Piet str], sbd_address2=[ ], sbd_address3=[WingatePark], sbd_address4=[PRETORIA],sbd_postcode=[0002],sbd_sub_tel=[0119734436],sbd_con_tel=[0121234567], sbd_email=[some@vodac.co.za],sbd_title=[MR], sbd_firstname=[Stainless],sbd_surname=[" + sSurname
				+ "], sbd_gender=[M], sbd_dob=[24061978], sbd_fax=[0116535414],','sba_analysis_1=[FAX],sba_analysis_3=[N],sba_analysis_4=[031],sba_analysis_5=[],sba_analysis_6=[B],sba_cust_dept=[cust dept],sba_comments=[make another comment],sba_id_number=[6911245117082],','sba3_email=[wynand@vodacom.co.za],sba3_email_flag=[Y],','sbv_credit_limit=[500],sbv_percent_check=[80],')";
		resultset = data.ConnectAndQueryEPPIXServer("", "", "", strSwapMAH);
			
			
		// Get the query results and store them in a variable
				String sResults2 = data.getQueryResults(resultset, 1);
				if (sResults2 != null) {

					// Split the string with a comma and compare Status and the Description
					String[] Compare = sResults2.split(",");
					if (Compare[1].equals("EPX_STATUS=[C]") && Compare[2].equalsIgnoreCase("MESSAGE=[Update successful!]")) {
						 System.out.println(Compare[1]+","+Compare[2]);
						 	repo.childLogPass("Verify upd_sbd_basic- Update subscriber details with valid Surname",
				                    ""+Compare[1]+","+Compare[2]+"",
				                     ""+Compare[1]+","+Compare[2]+"", false, test, utils); 
					}

					else {
						System.out.println("Did not update subscriber details with valid Surname");
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