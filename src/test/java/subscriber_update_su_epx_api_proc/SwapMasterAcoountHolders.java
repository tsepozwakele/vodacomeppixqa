package subscriber_update_su_epx_api_proc;

import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import ilab.generic.functions.commonBase;

import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.sql.ResultSet;

import org.testng.annotations.AfterMethod;

public class SwapMasterAcoountHolders  extends commonBase {
	String DefaultPath = System.getProperty("user.dir");
	public static ExtentTest parent;
	public String TCID = null, TestName = null;
	public static ExtentTest test;
	ResultSet resultset = null;	
	String[] sMSISDN = new String[3];
	String[] sAccountNumber = new String[3];
	String[] sMadflag = new String[3];
	
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
	  TCID = "1234";
	  parent = repo.setParent(TCID +" Swap master account hoder with a valid msisdn");
		test = repo.setChildNode("Swap master account hoder with a valid msisdn", parent);
		resultset = data.ConnectAndQueryEPPIXServer("", "", "", "select first 2 sbd_dialling_no,mad_account,mad_mah_flag from mad_master_acc_details, sbd_sub_dets where mad_subs_id = sbd_subscriber_id and sbd_bill_ac_no = mad_account and mad_account = 'C0010272' order by mad_mah_flag desc");
		
		if (resultset != null)
		{

		for (int i = 1;i < 3 ; i++)
			{
				sMSISDN[i] = data.getCellData( "sbd_dialling_no",i, null, null,  resultset,"SQLSERVER").trim();
				sAccountNumber[i]  = data.getCellData( "mad_account",i, null, null,  resultset,"SQLSERVER").trim();
				sMadflag[i]  = data.getCellData( "mad_mah_flag",i, null, null,  resultset,"SQLSERVER").trim();
			}
	
    		System.out.println("Obtain two msisdns and account_nos to swap from mad_master_acc_details, Old_msisdn_no: ["+sMSISDN[1]+"], Old_mad_account: ["+sAccountNumber[1]+"], Madflag: ["+sMadflag[1]+"] and New_msisdn_no: ["+sMSISDN[2]+"], New_mad_account: ["+sAccountNumber[2]+"], Madflag: ["+sMadflag[1]+"] obtained");     
    		//test.pass("<b>Obtain two msisdns and account_nos to swap from mad_master_acc_details, Old_msisdn_no: ["+sMSISDN[1]+"], Old_mad_account: ["+sAccountNumber[1]+"], Madflag: ["+sMadflag[1]+"] and New_msisdn_no: ["+sMSISDN[2]+"], New_mad_account: ["+sAccountNumber[2]+"], Madflag: ["+sMadflag[1]+"] obtained  </b><br/>");
    		repo.childLogPass("Obtain two msisdns and account_nos to swap from mad_master_acc_details", sMSISDN[1], sMSISDN[1],false,test,utils);
		}
	else
		{
			System.out.println("No records retrieved from mad_master_acc_details"+resultset);
			repo.childLogError("<b>No records retrieved from mad_master_acc_details </b><br/>"+resultset,test);
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
			repo.childLogPass("Verify master account holders were swapped successfully ", "MAH Successfully swapped"+sMSISDN[1] +" "+ sMSISDN[2], "MAH Successfully swapped"+sMSISDN[1] +" "+ sMSISDN[2],false,test,utils);
			
		}
		
		else
		{
			System.out.println("MAH not Successfully swapped");
			repo.childLogFail("Verify master account holders were swapped successfully ", "MAH Successfully swapped",sResults2,false,test,utils);
		}
	}
	
	else
	{
		 System.out.println("Function could not be executed");
		 repo.childLogError("<b>Function could not be executed </b><br/>"+sResults2,test);
	}
			
		// test.fail("Function could not be executed"+sResults2);
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
