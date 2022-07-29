/*
 *  Name of the test  : Swap_a_Subscriber_without_chnge_address_with_Y
 *  Author			  : Sello Molokomme
 *	Date			  : 26-02-2020
 *  Maintenance by	  :
 */

package subcustswap_ss_epx_api_proc;

import java.sql.ResultSet;

import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import ilab.generic.functions.commonBase;

public class Swap_a_Subscriber_without_change_address_with_Y extends commonBase {

	//Associate function libraries and declaration
	String DefaultPath = System.getProperty("user.dir");
	public static ExtentTest parent;
	public String TCID = null, TestName = null;
	public static ExtentTest test;
	ResultSet Obtained_old_acc, Obtained_new_acc, resultset = null;

	String[] vam_msisdn_no = new String[3];
	String[] vam_subscriber_id = new String[3];
	String[] sbd_bill_ac_no = new String[3];
	String[] account_no = new String[3];
	String[] referral_no = new String[3];

	
	
	@Test
	public void SubCusSwap() throws Exception {
		
		//String Obtained_old_acc, Obtained_new_acc, resultset = null;

		TCID = "IES_796";																	//Test Case ID
		parent = repo.setParent(TCID + " SUBCUSSWAP.4ge");
		test = repo.setChildNode("Swap_a_Subscriber_without_chnge_address_with_Y", parent);				//Test case name
		
		
		//Obtain account_no and sbd_dialling_no with IBIL service 
		
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
			System.out.println("Obtain account_no and sbd_dialling_no with any service:old_account_no ["
					+ sbd_bill_ac_no[1] + "], vam_subscriber_id: [" + vam_subscriber_id[1]
					+ "],and sbd_dialling_no: [" + vam_msisdn_no[1]+ "] obtained");
			repo.ExtentLogPass("Obtain account_no and sbd_dialling_no with any service:old_account_no["+ sbd_bill_ac_no[1] + "], vam_subscriber_id: [" + vam_subscriber_id[1] + "],and sbd_dialling_no: [" + vam_msisdn_no[1] +"] obtained]",false, utils, test);
			
			}
		
		else {
			System.out.println("Obtain account_no and sbd_dialling_no with any service:old_account_no, unsecessfull");
			repo.ExtentLogFail("", false, utils, test);
		}
		
		
		//Obtain current email address and sbd_account_no without IBIL service  
		Obtained_new_acc = data.ConnectAndQueryEPPIXServer("", "", "",
				"select First 5 account_no, referral_no from rica_individual,cta_cust_aux,ac2_aux_cust c where cta_customer = account_no and c.ac2_bill_ac_no = account_no and cta_email_type = 0 and ac2_analysis_a = 'P0700' and not exists(select csd_bill_ac_no from c3d_sbd_detail where csd_bill_ac_no = account_no)");
		
		String sResults = data.getQueryResults(Obtained_new_acc,1);
		
		//Obtained_new_acc.getString(1).trim();
		
		//System.out.println(Obtained_new_acc);
	
		/*Loop through data table and Trim values obtained
		 * Then print results and write to report
		 */
		if (sResults != null) 
			{
			
				for (int i = 1; i < 3; i++) 
				{
					account_no[i] = data.getCellData("account_no", i, null, null, Obtained_new_acc, "SQLSERVER").trim();
					referral_no[i] = data.getCellData("referral_no", i, null, null, Obtained_new_acc, "SQLSERVER").trim();
		
				}
				
				System.out.println("Obtain account_no without any service,new_account_no: ["+ account_no[2] + "], and referral_no: [" + referral_no[2]+ "] obtained");
				repo.ExtentLogPass("Obtain account_no without any service,new_account_no:["+ account_no[2] + "],and referral_no: [" + referral_no[2] + "],]",false, utils, test);
				
			}
		
		else {
			System.out.println("");
			repo.ExtentLogFail("", false, utils, test);
		}
		
		 //Execute SUBCUS UPDATE stored procedure
		String swapSubscriber = "execute procedure ss_epx_api_proc('SUBCUS UPDATE','"+ vam_msisdn_no[1]+"','"+ account_no[1] +"','Y','DEFCHRG=[N]','','','','','EPPIX','MAINTENANCE',user,user,'REASON=[TNFR],DEALER=[VODAC],OC3RSN=[AA],SMS=[N],OC3_IGN=[Y],')";
		resultset = data.ConnectAndQueryEPPIXServer("", "", "", swapSubscriber);
		
		String sResults1 = data.getQueryResults(resultset, 1);
		
		//Validate error messages coming back from the db

		if (sResults1 != null && sResults1.contains("EPX_STATUS")) {
			
			
			String [] Swap = sResults1.split(",");
					
			if(Swap[1].equals("EPX_STATUS=[C]") && Swap[2].equals("MESSAGE=[Successful]") ) 
			{
		
				System.out.println("Change address with Y Successfully updated!..!");
				repo.ExtentLogPass("Verify SUBCUS UPDATE, without change address with Y was successful!..!", false, utils, test);
			}
			
			else if(Swap[1].equals("EPX_STATUS=[E]") && Swap[2].equals("MESSAGE=[New Customer Same as the Old Customer]") )
			{
				System.out.println("New Customer is the same as the old customer");
				repo.ExtentLogPass("New Customer is the same as the old customer",false, utils, test);
				
			}
			
			else if(Swap[1].equals("EPX_STATUS=[E]") && Swap[2].equals("MESSAGE=[Account does not have required RICA details.Account transfer not allowed.]") )
			{
				System.out.println("Account does not have required RICA details.Account transfer not allowed");
				repo.ExtentLogPass("Account does not have required RICA details.Account transfer not allowed",false, utils,test);
				
			}
			else if(Swap[1].equals("EPX_STATUS=[E]") && Swap[2].equals("MESSAGE=[To Customer Doesnt Have Valid Email Address.  Cannot Swap]") )
			{
				System.out.println("To Customer Doesnt Have Valid Email Address.  Cannot Swap");
				repo.ExtentLogPass("To Customer Doesnt Have Valid Email Address.  Cannot Swap",false, utils,test);
				
			}
			
		}
		
		//Check if we got the response from stored proc
		else if(sResults1 != null)
			try {
				
				sResults1 = data.getQueryResults(resultset , 1);
				repo.ExtentLogPass("<b>No response found for request... please check update stored proc</b><br>", false, utils, test);
     		    } catch (Exception e) {
     		    	repo.ExtentLogPass("<b>No response found for request... please check update stored proc</b><br>", false, utils, test);
     		    }

		
		}
	}


