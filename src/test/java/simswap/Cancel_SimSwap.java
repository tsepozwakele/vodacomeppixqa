package simswap;

import java.sql.ResultSet;

import com.aventstack.extentreports.ExtentTest;

import ilab.generic.functions.DataFunctions;
import ilab.generic.functions.Reporting;
import ilab.generic.functions.UtilityFunctions;
import ilab.generic.functions.commonBase;

public class Cancel_SimSwap extends commonBase{

	ResultSet resultset = null;
	String[] sMSISDN = new String[3];
	String[] sSSA_orig_sim = new String[3];
	String[] sSSa_new_sim = new String[3];
	
	public Cancel_SimSwap(DataFunctions data, UtilityFunctions utils, Reporting repo) {
		this.data = data;
        this.utils = utils;
        this.repo = repo;
	}
	
	//remove system application
		public void removeSystemApplication(String sDBURL, String sUserName, String sPassword, String sQuery, String sStatus, String sMessage, ExtentTest test) throws Exception {
			
			 resultset = data.ConnectAndQueryEPPIXServer(sDBURL, sUserName, sPassword, sQuery);
	 		if (resultset != null) {

	 			for (int k = 1; k < 3; k++) {

	 				sMSISDN[k] = data.getCellData("ssa_msisdn", k, null, null, resultset, "SQLSERVER").trim();
	 				sSSA_orig_sim[k] = data.getCellData("ssa_orig_sim", k, null, null, resultset, "SQLSERVER").trim();
	 				sSSa_new_sim[k] = data.getCellData("ssa_new_sim", k, null, null, resultset, "SQLSERVER").trim();

	 			}
	 			
	 			repo.childLogPass("Obtain ssa_msisdn, ssa_orig_sim, and ssa_new_sim",
	 					"Obtained ssa_msisdn , ssa_orig_sim, and ssa_new_sim",
	 					"Obtained ssa_msisdn ["+sMSISDN[1]+"], ssa_orig_sim ["+sSSA_orig_sim[1]+"], and ssa_new_sim ["+sSSa_new_sim[1]+"]", false, test, utils);

	 		} else {
	 			System.out.println("NO records returned");
	 			repo.childLogFail("Verify update_mah_flag - Change master account holder status with mah_flag = Y",
	 					"Change master account holder status with mah_flag = Y",
	 					"Change master account holder status with mah_flag = Y", false, test, utils);
	 		}

	 		// EXECUTE PROCEDURE su_epx_api('UPD_SBD_BASIC')
	 		String query_mah_retrieve = "execute procedure swp_epx_api('GET_SIMSWAP_STATUS','ssa_msisdn=["+sMSISDN[1]+"]','ssa_orig_sim=["+sSSA_orig_sim[1]+"]'"
	 				+ ",'ssa_new_sim=["+sSSa_new_sim[1]+"]','SS','COMMENTS=[Old Sim Card Located],ICAPCHK=[N],SIMCHK=[Y],AAFLAG=[N],IMMEDIATE=[Y],','W1','',"
	 				+ "'MIISimSwaps','rademeza')";
	 		resultset = data.ConnectAndQueryEPPIXServer(sDBURL, sUserName, sPassword, query_mah_retrieve);

	 		// Get the query results and store them in a variable
	 		String sResults2 = data.getQueryResults(resultset, 1);
	 		if (sResults2 != null) {

	 			// Split the string with a comma and compare Status and the Description
	 			String[] Compare = sResults2.split(",");  
	 			if (Compare[1].equals(sStatus)
	 					&& Compare[3].equalsIgnoreCase(sMessage)) {
	 				System.out.println(Compare[1] + "," + Compare[3]);
	 				repo.childLogPass("SimSwap.4ge - Get_The_Status_Of_A_Sim_Swap(API) - remove system name",
	 						"" + Compare[1] + "," + Compare[3] + "", "" + Compare[1] + "," + Compare[3] + "", false, test,
	 						utils);

	 			}

	 			else {
	 				System.out.println("Did not Change master account holder status with mah_flag = Y");
	 				repo.childLogFail("Change master account holder status with mah_flag = Y",
	 						"account holder status changed with mah_flag = Y", sResults2, false, test, utils);
	 			}
	 		}

	 		else {
	 			System.out.println("Function could not be executed");
	 			repo.childLogError("<b>Function could not be executed </b><br/>" + sResults2, test);
	 		}
		}
}
