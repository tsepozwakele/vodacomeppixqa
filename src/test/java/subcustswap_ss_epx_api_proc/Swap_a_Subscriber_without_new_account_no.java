/*
 *  Name of the test  : PROPOSED_INVOICE_API_with_PROPOSED_remove_MSISDN
 *  Author			  : Sello Molokomme
 *	Date			  : 18-02-2020
 *  Maintenance by	  :
 */

package subcustswap_ss_epx_api_proc;

import java.sql.ResultSet;

import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import ilab.generic.functions.commonBase;

public class Swap_a_Subscriber_without_new_account_no extends commonBase {

	//Associate function libraries and declaration
	String DefaultPath = System.getProperty("user.dir");
	public static ExtentTest parent;
	public String TCID = null, TestName = null;
	public static ExtentTest test;
	ResultSet Obtained_old_acc, Obtained_new_acc, resultset = null;

	String[] vam_msisdn_no = new String[3];
	String[] vam_subscriber_id = new String[3];
	String[] sbd_bill_ac_no = new String[3];
	String[] account_no = new String[2];
	String[] referral_no = new String[2];
	String[] results = new String [3];
	
	
	@Test
	public void f() throws Exception {

		TCID = "IES_794";																	//Test Case ID
		parent = repo.setParent(TCID + " SUBCUSSWAP.4ge");
		test = repo.setChildNode("Swap_a_Subscriber_without_new_account_no", parent);				//Test case name
		
		
		//Obtain account_no and sbd_dialling_no with any service 
		Obtained_old_acc = data.ConnectAndQueryEPPIXServer("", "", "",
				"select First 5 vam_msisdn_no, vam_subscriber_id, sbd_bill_ac_no from vam_active_msisdn,sbd_sub_dets,cta_cust_aux,ac2_aux_cust c where vam_stat_code = '1' and cta_email_type <> 0 and sbd_dialling_no = vam_msisdn_no and sbd_bill_ac_no = c.ac2_bill_ac_no and EXISTS (select vas_subscriber_id from vas_active_service where vas_service_code in ('C3DL','A373','GP00','VBIL','EBIL')) and ac2_analysis_a = 'P0700'");

		
		/*Loop through data table and Trim values obtained
		 * Then print results and write to report
		 */
		if (Obtained_old_acc != null) {

			for (int i = 1; i < 3; i++) {
				
			vam_msisdn_no[i] = data.getCellData("vam_msisdn_no", i, null, null, Obtained_old_acc, "SQLSERVER").trim();
			vam_subscriber_id[i] = data.getCellData("vam_subscriber_id", i, null, null, Obtained_old_acc, "SQLSERVER").trim();
			sbd_bill_ac_no[i] = data.getCellData("sbd_bill_ac_no", i, null, null, Obtained_old_acc, "SQLSERVER").trim();
			}
			System.out.println("Obtain account_no and sbd_dialling_no with IBIL service:old_account_no ["
					+ sbd_bill_ac_no[1] + "], vam_subscriber_id: [" + vam_subscriber_id[1]
					+ "],and sbd_dialling_no: [" + vam_msisdn_no[1] + "] obtained");
			repo.ExtentLogPass("Obtain account_no and sbd_dialling_no with IBIL service:old_account_no["+ sbd_bill_ac_no[1] + "], vam_subscriber_id: [" + vam_subscriber_id[1] + "],and sbd_dialling_no: [" + vam_msisdn_no[1] +"] obtained]",false, utils, test);
			
			}
		
		else {
			System.out.println("");
			repo.ExtentLogFail("", false, utils, test);
		}
		//Obtain current email address and sbd_account_no without any(A373) service  
		Obtained_new_acc = data.ConnectAndQueryEPPIXServer("", "", "",
				"select First 2 account_no, referral_no from rica_individual,cta_cust_aux,ac2_aux_cust c where cta_customer = account_no and c.ac2_bill_ac_no = account_no and cta_email_type = 0 and ac2_analysis_a = 'P0700' and not exists(select csd_bill_ac_no from c3d_sbd_detail where csd_bill_ac_no = account_no)");
		
		String sResults = data.getQueryResults(Obtained_new_acc, 1);
			
		
		/*Loop through data table and Trim values obtained
		 * Then print results and write to report
		 */
		if (sResults != null) 
			{
				for (int i = 1; i < 2; i++) 
				{
					account_no[i] = data.getCellData("account_no", i, null, null, Obtained_new_acc, "SQLSERVER").trim();
					referral_no[i] = data.getCellData("referral_no", i, null, null, Obtained_new_acc, "SQLSERVER").trim();
		
				}
				
				System.out.println("Obtain account_no without IBIL service,new_account_no: ["+ account_no[1] + "], and referral_no: [" + referral_no[1]+ "] obtained");
				repo.ExtentLogPass("Obtain account_no without IBIL service,new_account_no:["+ account_no[1] + "],and referral_no: [" + referral_no[1] + "],]",false, utils, test);
				
			}
		
		else {
			System.out.println("");
			repo.ExtentLogFail("", false, utils, test);
		}
		
		 //Execute SUBCUS UPDATE stored procedure in attempt to swap
		String swapSubscriber = "execute procedure ss_epx_api_proc('SUBCUS UPDATE','"+vam_msisdn_no[1] +"','','Y','DEFCHRG=[N]','','','','','EPPIX','MAINTENANCE',user,user,'REASON=[TNFR],DEALER=[VODAC],OC3RSN=[AA],SMS=[N],OC3_IGN=[Y],')";
		resultset = data.ConnectAndQueryEPPIXServer("", "", "", swapSubscriber);
		
		String sResults1 =null;
		try {
			
			sResults1 = data.getQueryResults(resultset , 1);
		} catch (Exception e) {
			repo.childLogPass("Verify SUBCUS UPDATE, without  new account number  was successful,No MSISDN number was specified.", "No new account number was specified.", "No new account number was specified.", false, test, utils);
		}

		}
	}
	// Cannot validate error message as is invalid

