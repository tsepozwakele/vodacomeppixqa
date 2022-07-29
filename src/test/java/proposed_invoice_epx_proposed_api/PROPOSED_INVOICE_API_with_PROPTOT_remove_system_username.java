/*
 *  Name of the test  : PROPOSED_INVOICE_API with PROPOSED remove system username
 *  Author			  : Sello Molokomme
 *	Date			  : 24-02-2020
 *  Maintenance by	  :
 */

package proposed_invoice_epx_proposed_api;

import java.sql.ResultSet;

import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import ilab.generic.functions.commonBase;

public class PROPOSED_INVOICE_API_with_PROPTOT_remove_system_username extends commonBase {
	
	//Associate function libraries and declaration

	String DefaultPath = System.getProperty("user.dir");
	public static ExtentTest parent;
	public String TCID = null, TestName = null;
	public static ExtentTest test;
	ResultSet  resultset = null;
	
	
	String[] vam_msisdn_no = new String[3];
	String[] vam_subscriber_id = new String[3];
	String[] ebb_unbilled = new String[3];

	@Test
	public void f() throws Exception {
		
		TCID = "IES_793";																								//Test case id																							
		parent = repo.setParent(TCID + " Proposed_invoce");																//Test case name
		test = repo.setChildNode("PROPOSED_INVOICE_API_with_PROPTOT_remove_system_username", parent);
		resultset = data.ConnectAndQueryEPPIXServer("", "", "","select first 2 vam_msisdn_no,ebb_unbilled,* from vam_active_msisdn inner join ebb_epx_billsofar_bulk ON vam_msisdn_no = ebb_msisdn_no where vam_stat_code IN(1,4) AND vam_intern_tariff not in('PRE','C3D') and ebb_unbilled > 100"); //Obtain vam_msisdn_no from vam_active_msisdn where vam_stat_code IN(1,4) AND vam_intern_tariff not in('PRE','C3D')
		
		/*Loop through data table and Trim values obtained
		 * Then print results and write to report
		 */
		
		if(resultset!=null)
		{																															
			for (int i = 1; i < 3; i++) 
			{
				
				vam_msisdn_no[i] = data.getCellData("vam_msisdn_no", i, null, null, resultset, "SQLSERVER").trim();
				vam_subscriber_id[i] = data.getCellData("vam_subscriber_id", i, null, null, resultset, "SQLSERVER").trim();
				ebb_unbilled[i] = data.getCellData("ebb_unbilled", i, null, null, resultset, "SQLSERVER").trim();
				
			}
			System.out.println("Obtain vam_msisdn_no from vam_active_msisdn,vam_msisdn_no: ["+ vam_msisdn_no[1] + "], vam_subscriber_id: [" + vam_subscriber_id[1]+ "],and ebb_unbilled: [" + ebb_unbilled[1] + "] obtained");
			repo.ExtentLogPass("Obtain vam_msisdn_no from vam_active_msisdn,vam_msisdn_no:["+ vam_msisdn_no[1] + "], vam_subscriber_id: [" + vam_subscriber_id[1] + "],and bb_unbilled: [" + ebb_unbilled[1] +"] obtained]",false, utils, test);

		}
		else
		{
			System.out.println("Obtain vam_msisdn_no from vam_active_msisdn ,No records retrieved from vam_active_msisdn and ebb_epx_billsofar_bulk");
			repo.ExtentLogFail("Obtain vam_msisdn_no from vam_active_msisdn ,No records retrieved from vam_active_msisdn and ebb_epx_billsofar_bulk", false, utils, test);
		}
		
		
		//Run stored proc to obtain totals
		
		String propInvoice = "EXECUTE PROCEDURE 'informix'.epx_proposed_api('PROPTOT','"+ vam_msisdn_no[1] +"','eppix','eppix',' ',user)";
		resultset = data.ConnectAndQueryEPPIXServer("", "", "", propInvoice);
	
		@SuppressWarnings("unused")
		String sResults =null;
		try {
			 sResults = data.getQueryResults(resultset , 1);
		} catch (Exception e) {
			repo.childLogPass("Obtain Execute_PROPOTOT SP Details, No system username found.", "No System User Parameter was speficied.", "No System User Parameter was speficied.", false, parent, utils);
		}
	
		/*Loop through data table and Trim values obtained
		 * Then print results and write to report*/

	
		}
		
}
