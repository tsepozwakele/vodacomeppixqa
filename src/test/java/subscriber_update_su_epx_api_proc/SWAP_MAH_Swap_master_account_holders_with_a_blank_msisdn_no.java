package subscriber_update_su_epx_api_proc;

import java.sql.ResultSet;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Sheet;

import com.aventstack.extentreports.ExtentTest;
//import com.codoid.products.fillo.Recordset;

import ilab.generic.functions.DataFunctions;
import ilab.generic.functions.Reporting;
import ilab.generic.functions.UtilityFunctions;
import ilab.generic.functions.commonBase;





public class SWAP_MAH_Swap_master_account_holders_with_a_blank_msisdn_no extends commonBase
{
	//Initialization of Variables
	private static DataFunctions data = new DataFunctions();
	private static UtilityFunctions utils = new UtilityFunctions();
	private static Reporting repo = new Reporting();
	public static ExtentTest test = null;
	
	
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ResultSet resultset = null;	
		String[] sMSISDN = new String[3];
		String[] sAccountNumber = new String[3];
		String[] sMadflag = new String[3];
		//data.GetEnvironmentVariables();
    	//sDataType = data.getDataType();
		
    	//repo.setExtent(repo.initializeExtentReports("test","false", utils));
	//    parent = repo.setParent("Regression" + " : " + "Subscriber Update"); 
	//    test = repo.setChildNode("Subscriber Update", parent);
		
		//Store a query to obtain msisdn from ebb_epx_billsofar_bulk table in a variable
		resultset = data.ConnectAndQueryEPPIXServer("", "", "", "select first 2 sbd_dialling_no,mad_account,mad_mah_flag from mad_master_acc_details, sbd_sub_dets where mad_subs_id = sbd_subscriber_id and sbd_bill_ac_no = mad_account and mad_account = 'C0010272' order by mad_mah_flag desc");
		
		//Loop through the array to obtain the required values
		if (resultset != null)
			{
			for (int i = 1;i < 3 ; i++)
				{
					sMSISDN[i] = data.getCellData( "sbd_dialling_no",i, null, null,  resultset,"SQLSERVER").trim();
					sAccountNumber[i]  = data.getCellData( "mad_account",i, null, null,  resultset,"SQLSERVER").trim();
					sMadflag[i]  = data.getCellData( "mad_mah_flag",i, null, null,  resultset,"SQLSERVER").trim();
				}
		
	    		System.out.println("Obtain two msisdns and account_nos to swap from mad_master_acc_details, Old_msisdn_no: ["+sMSISDN[1]+"], Old_mad_account: ["+sAccountNumber[1]+"], Madflag: ["+sMadflag[1]+"] and New_msisdn_no: ["+sMSISDN[2]+"], New_mad_account: ["+sAccountNumber[2]+"], Madflag: ["+sMadflag[1]+"] obtained");     
	    		repo.ExtentLogFail("Obtain two msisdns and account_nos to swap from mad_master_acc_details, Old_msisdn_no: ["+sMSISDN[1]+"], Old_mad_account: ["+sAccountNumber[1]+"], Madflag: ["+sMadflag[1]+"] and New_msisdn_no: ["+sMSISDN[2]+"], New_mad_account: ["+sAccountNumber[2]+"], Madflag: ["+sMadflag[1]+"] obtained", false, utils, test);
			}
		else
			{
				System.out.println("No records retrieved from mad_master_acc_details"+resultset);
				repo.ExtentLogFail("No records retrieved from mad_master_acc_details"+resultset, false, utils, test);
			}
	    
	    //EXECUTE PROCEDURE su_epx_api('SWAP_MAH')
	    String strSwapMAH = "execute procedure su_epx_api('SWAP_MAH','NEW_MSISDN=["+sMSISDN[2]+"],OLD_MSISDN=["+sMSISDN[1]+"],APPLICATION_ID=[MII],USER=[user]','','','','')";
		resultset = data.ConnectAndQueryEPPIXServer("", "", "", strSwapMAH); 
		
		
		//Get the query results and store them in a variable
		String sResults2 =  data.getQueryResults(resultset,  1);
		if (sResults2 != null)
		{
			
			//Split the string with a comma and compare Status and the Description
			String[] Compare = sResults2.split(",");
			if (Compare[1].equals("EPX_STATUS=[C]") && Compare[2].equalsIgnoreCase("MESSAGE=[MAH Successfully swapped]"))
			{
				System.out.println("MAH Successfully swapped");
				repo.ExtentLogPass("MAH Successfully swapped", false, utils, test);
			}
			
			else
			{
				System.out.println("MAH not Successfully swapped");
				repo.ExtentLogFail("MAH not Successfully swapped", false, utils, test);
			}
		}
		
		else
		{
			 System.out.println("Function could not be executed");
			 repo.ExtentLogFail("Function could not be executed"+sResults2, false, utils, test);
		}

	}
}

