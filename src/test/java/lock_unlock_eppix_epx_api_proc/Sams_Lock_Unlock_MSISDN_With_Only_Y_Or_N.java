/*
 *  Name of the test  : Sams_Lock_Unlock_MSISDN_With_Only_Y_Or_N
 *  Author			  : Sello Molokomme
 *	Date			  : 27-02-2020
 *  Maintenance by	  :
 */

package lock_unlock_eppix_epx_api_proc;

import java.sql.ResultSet;
import java.time.LocalDate;

import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import ilab.generic.functions.commonBase;

public class Sams_Lock_Unlock_MSISDN_With_Only_Y_Or_N extends commonBase {
	
	//Associate function libraries and declaration
	String DefaultPath = System.getProperty("user.dir");
	public static ExtentTest parent;
	public String TCID = null, TestName = null;
	public static ExtentTest test;
	ResultSet resultse,resultset1 = null;
	
	

	String[] msisdn_no = new String[3];
	String[] subscriber_id = new String[3];
	String[] sim_no = new String[3];
	String[] contr_terminat = new String[3];
	String[] v_status = new String[3];
	String[] vstatus = new String[3];
	String[] vmessage = new String[3];
	String[] vam_stat_code = new String[3];
	String[] ec_operator = new String[3];
	String[] ec_subscriber_id = new String[3];
	String[] ec_command = new String[3];
	String[] ec_status = new String[3];
	String[] ec_date = new String[3];
	String[] ecd_text = new String[3];
	String[] ec_message = new String[3];
	String[] ec_msisdn = new String[3];
	String[] ec_type = new String[3];
	String[] vmsisdn = new String[3];
	
	


	@Test
	public void LockUnlock() throws Exception {

		TCID = "???";                                                                        //Test case ID
		parent = repo.setParent(TCID + " lock_unlock_eppix_epx_api_pro");
		test = repo.setChildNode("Sams_Lock_Unlock_MSISDN_With_Only_Y_Or_N", parent);			//Test case name
		
		//Obtain vam_msisdn_no,vam_subscriber_id,vam_sim_no and vam_contr_terminat service 
		resultset = data.ConnectAndQueryEPPIXServer("", "", "",
				"select first 5 vam_msisdn_no,vam_subscriber_id,vam_sim_no,vam_contr_terminat,* from vam_active_msisdn where LENGTH(vam_msisdn_no) = 12 and vam_stat_code = '1' and vam_contr_terminat >= today and NOT EXISTS(Select sim_no from activation_pend where vam_sim_no = sim_no) and not exists(select csd_dialling_no from c3d_sbd_detail where csd_dialling_no = vam_msisdn_no)");

		
		/*Loop through data table and Trim values obtained
		 * Then print results and write to report
		 */
		if (resultset != null) {

			for (int i = 1; i < 3; i++) {
				msisdn_no[i] = data.getCellData("vam_msisdn_no", i, null, null, resultset, "SQLSERVER").trim()
						.toString();
				subscriber_id[i] = data.getCellData("vam_subscriber_id", i, null, null, resultset, "SQLSERVER").trim()
						.toString();
				sim_no[i] = data.getCellData("vam_sim_no", i, null, null, resultset, "SQLSERVER").trim();
				contr_terminat[i] = data.getCellData("vam_contr_terminat", i, null, null, resultset, "SQLSERVER")
						.trim();
			}

			System.out.println("Obtain active 12 digits msisdn_no from vam_active_msisdn,vam_msisdn_no: ["+ msisdn_no[1] + "], vam_subscriber_id: [" + subscriber_id[1] + "], vam_sim_no: [" + sim_no[1]+ "] and vam_contr_terminat : [" + contr_terminat[1] + "] obtained");
			repo.childLogPass("Obtain msisdn_no vam_active_msisdn,vam_msisdn_no", msisdn_no[1], msisdn_no[1], false,test, utils);
			repo.childLogPass("Obtain subscriber_id vam_active_msisdn,vam_msisdn_no", subscriber_id[1],subscriber_id[1], false, test, utils);
			repo.childLogPass("Obtain subscriber_id vam_active_msisdn,vam_msisdn_no", sim_no[1], sim_no[1], false, test,utils);
			repo.childLogPass("Obtain subscriber_id vam_active_msisdn,vam_msisdn_no", contr_terminat[1],contr_terminat[1], false, test, utils);

		} else {

			System.out.println("No records obtained from vam_active_msisdn,vam_msisdn_no" + resultset);
			repo.childLogError("<b>No records retrieved from mam_active_msisdn,vam_msisdn_no </b><br/>" + resultset,
					test);

		}
		
		//Execute ext_services:sams_lock_unlockstored procedure

		String strLockUnlock = "EXECUTE PROCEDURE ext_services:sams_lock_unlock('L','"+msisdn_no[1] +"','SAMS','Y','molokommes','SAMS');";
		resultset1 = data.ConnectAndQueryEPPIXServer("", "", "", strLockUnlock);

		String sResults2 = data.getQueryResults(resultset1, 1);
		
		/*Loop through data table and Trim values obtained
		 * Then print results and write to report
		 */
		if (sResults2 != null) {

			for (int i = 1; i < 3; i++) {
				vmsisdn[i] = data.getCellData("vmsisdn", i, null, null, resultset1, "SQLSERVER").trim().toString();
				vstatus[i] = data.getCellData("vstatus", i, null, null, resultset1, "SQLSERVER").trim().toString();
				vmessage[i] = data.getCellData("vmessage", i, null, null, resultset1, "SQLSERVER").trim().toString();

				if (vstatus[1].equals("C") && vmessage[1].equals("Locking/Unlocking Successful")) {

					System.out.println(
							"Verify sams lock unlock without send MSISDN_With_Only_Y_Or_N was successful, sams lock unlock without MSISDN_With_Only_Y_Or_N  was successful as expected. vstatus: ["
									+ vstatus[1] + "],and vmessage:[" + vmessage[1] + "]");
					repo.ExtentLogPass(
							"Verify sams lock unlock without send MSISDN_With_Only_Y_Or_N was  successful, sams lock unlock without MSISDN_With_Only_Y_Or_N  was successful as expected. vstatus: ["
									+ vstatus[1] + "],and vmessage:[" + vmessage[1] + "]",
							false, utils, test);

				}

				else {
					System.out.println(
							"Verify sams lock unlock without send MSISDN_With_Only_Y_Or_N was successful, sams lock unlock without send MSISDN_With_Only_Y_Or_N was successful. vstatus: ["
									+ vstatus[1] + "],and vmessage:[" + vmessage[1] + "].Please Investigate");
					repo.ExtentLogFail(
							"Verify sams lock unlock without send MSISDN_With_Only_Y_Or_N was successful, sams lock unlock without send MSISDN_With_Only_Y_Or_N was successful. vstatus: ["
									+ vstatus[1] + "],and vmessage:[" + vmessage[1] + "].Please Investigate",
							false, utils, test);

				}

			}
				} 
		else {

			System.out.println("EXECUTE PROCEDURE sams_lock_unlock to lock msisdn, Stored proc: sams_lock_unlock('L',["
					+ vstatus[1] + "]','SAMS','N',user,'SAMS') could not be executed.");
			repo.ExtentLogFail("EXECUTE PROCEDURE sams_lock_unlock to lock msisdn, Stored proc: sams_lock_unlock('L',["
					+ vstatus[1] + "]','SAMS','N',user,'SAMS') could not be executed.", false, utils, test);
			}

		resultset = data.ConnectAndQueryEPPIXServer("", "", "","select vam_msisdn_no,vam_subscriber_id,vam_sim_no,vam_stat_code from vam_active_msisdn where vam_msisdn_no ='"+ msisdn_no[2] +"'");
		
		//716887702000
		//["+msisdn_no[1] +"]

		String sResults3 = data.getQueryResults(resultset, 1);

		if (sResults3 != null) {
			for (int i = 1; i < 3; i++) 
			{
				vam_stat_code[i] = data.getCellData("vam_stat_code", i, null, null, resultset, "SQLSERVER").trim()
						.toString();
				if (vam_stat_code[1].equals("1")) {

					System.out.println(
							"Obtain record from vam_active_msisdn and verify vam_stat_code, Record with vam_msisdn_no: ["
									+ msisdn_no[1] + "],vam_subscriber_id: [" + subscriber_id[1] + "], vam_sim_no: ["
									+ sim_no[1] + "] and vam_stat_code was NOT updated as expected : ["
									+ vam_stat_code[1] + "]");
					repo.ExtentLogPass(
							"Obtain record from vam_active_msisdn and verify vam_stat_code, Record with vam_msisdn_no: ["
									+ msisdn_no[1] + "],vam_subscriber_id: [" + subscriber_id[1] + "], vam_sim_no: ["
									+ sim_no[1] + "] and vam_stat_code was NOT updated as expected : ["
									+ vam_stat_code[1] + "]",
							false, utils, test);

				}
				else {
					
					System.out.println("Obtain record from vam_active_msisdn and verify vam_stat_code, No active msisdns found in vam_active_msisdn");
					repo.ExtentLogFail("Obtain record from vam_active_msisdn and verify vam_stat_code, No active msisdns found in vam_active_msisdn", false, utils, test);
				} 
			}
		}
		
		//Obtain select ec_operator,ec_subscriber_id,ec_command,ec_status,ec_date and ecd_text,ec_message
		resultset = data.ConnectAndQueryEPPIXServer("", "", "","select ec_operator,ec_subscriber_id,ec_command,ec_status,ec_date,ecd_text,ec_message,* from ecd_event_cmd_det a INNER JOIN ec_event_command b ON b.ec_command = a.ecd_command where ec_msisdn = '716887702000' and ec_date = today order by ec_time desc");
		String sResults4 = data.getQueryResults(resultset, 1);
		
		if(sResults4 != null) {
			for(int i =1; i<3; i++)
			{
				ec_msisdn[i] = data.getCellData("vam_stat_code", i, null, null, resultset, "SQLSERVER").trim()
						.toString();
				ec_operator[i] = data.getCellData("vam_stat_code", i, null, null, resultset, "SQLSERVER").trim()
						.toString();
				ec_subscriber_id[i] = data.getCellData("vam_stat_code", i, null, null, resultset, "SQLSERVER").trim()
						.toString();
				ec_command[i] = data.getCellData("vam_stat_code", i, null, null, resultset, "SQLSERVER").trim()
						.toString();
				ec_status[i] = data.getCellData("vam_stat_code", i, null, null, resultset, "SQLSERVER").trim()
						.toString();
				ec_date[i] = data.getCellData("vam_stat_code", i, null, null, resultset, "SQLSERVER").trim()
						.toString();
				ecd_text[i] = data.getCellData("vam_stat_code", i, null, null, resultset, "SQLSERVER").trim()
						.toString();
				ec_message[i] = data.getCellData("vam_stat_code", i, null, null, resultset, "SQLSERVER").trim()
						.toString();
				ec_type[i] = data.getCellData("vam_stat_code", i, null, null, resultset, "SQLSERVER").trim()
						.toString();
				
				
				//Getting todays date
				@SuppressWarnings("unused")
				LocalDate today = LocalDate.now();
				
				if(ec_message[1].equals("PERM_DEACTIVATION") && ec_message[1].equals("V")&& ec_status[1].equals("")&&ec_date[1].equals(LocalDate.now())) {
					
					System.out.println("Verify ec_message = 'DELINK  SIMS' or ec_message = 'PERM_DEACTIVATION' AND ec_status = 'V' AND ec_date = GetTodayDate, Verification was successful. ec_msisdn:["+ec_msisdn+"],ec_operator:["+ec_operator+"],ec_command:["+ec_command+"],ec_status["+ec_status+"],ec_date["+ec_date+"],ec_type["+ec_type+"],and ec_message:["+ec_message+"]");
					repo.ExtentLogPass("Verify ec_message = 'DELINK  SIMS' or ec_message = 'PERM_DEACTIVATION' AND ec_status = 'V' AND ec_date = GetTodayDate, Verification was successful. ec_msisdn:["+ec_msisdn+"],ec_operator:["+ec_operator+"],ec_command:["+ec_command+"],ec_status["+ec_status+"],ec_date["+ec_date+"],ec_type["+ec_type+"],and ec_message:["+ec_message+"]", false, utils, test);
				}
				else{
					repo.ExtentLogFail("Verify ec_message = 'DELINK  SIMS' or ec_message = 'PERM_DEACTIVATION' AND ec_status = 'V' AND ec_date = GetTodayDate, Verification was successful. ec_msisdn:["+ec_msisdn+"],ec_operator:["+ec_operator+"],ec_command:["+ec_command+"],ec_status["+ec_status+"],ec_date["+ec_date+"],ec_type["+ec_type+"],and ec_message:["+ec_message+"]", false, utils, test);
					
				}
	
			}
		}
		
		else {
			System.out.println("Verify record in ec_event_command and obtain ec_command,No records were returned for ec_msisdn = ["+ec_msisdn[1]+"] as expected");
		}

	}
}
