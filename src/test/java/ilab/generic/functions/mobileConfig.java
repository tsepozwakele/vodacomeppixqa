package ilab.generic.functions;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.remote.MobileCapabilityType;

public class mobileConfig {
	
	public static DesiredCapabilities capabilities = new DesiredCapabilities();
	public DesiredCapabilities setDesiredCapabilities(String sMobileType, String strAppiumVersion, String strAutomationName, String strPlatformName, String strPlatformVersion, String strDeviceName, String strApplicationName, String strBundleID,String strudid, String strPackage, String strActivity, String strSessionName, String strDescription, String strDeviceOrientation, String strCaptureScreenshot,String strBrowserName, String strDeviceGroup)
	{
		switch (sMobileType.toUpperCase())
		{
			case "IOS":
					
				capabilities.setCapability(MobileCapabilityType.APPIUM_VERSION, strAppiumVersion);
				capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, strAutomationName);
				capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, strPlatformName);
				capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, strPlatformVersion);
				capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, strDeviceName);
				capabilities.setCapability("app", strApplicationName);	
				capabilities.setCapability("bundleid", strBundleID);
				break;
			case "ANDROID":
				
				capabilities.setCapability(MobileCapabilityType.APPIUM_VERSION, strAppiumVersion);
				capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, strAutomationName);
				capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, strPlatformName);
				capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, strPlatformVersion);
				capabilities.setCapability("deviceName", strDeviceName);
				capabilities.setCapability("udid", strudid);
				capabilities.setCapability("appPackage", strPackage);
				capabilities.setCapability("appActivity", strActivity);
				break;
			case "KOBITON":
				
				capabilities.setCapability("sessionName", strSessionName);
				capabilities.setCapability("sessionDescription", strDescription); 
				capabilities.setCapability("deviceOrientation", strDeviceOrientation);  
				capabilities.setCapability("captureScreenshots", strCaptureScreenshot); 
				capabilities.setCapability("browserName", strBrowserName); 
				capabilities.setCapability("deviceGroup", strDeviceGroup); 
				capabilities.setCapability("deviceName", strDeviceName);
				capabilities.setCapability("platformVersion", strPlatformVersion);
				capabilities.setCapability("platformName", strPlatformName); 
		}
		return capabilities;
				
	}
	
	public DesiredCapabilities setCapabilitiesAndroid(String strAppiumVersion, String strAutomationName, String strPlatformName, String strPlatformVersion, String strDeviceName, String strudid, String strPackage, String strActivity)
	{
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(MobileCapabilityType.APPIUM_VERSION, strAppiumVersion);
			capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, strAutomationName);
			capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, strPlatformName);
			capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, strPlatformVersion);
			capabilities.setCapability("deviceName", strDeviceName);
			capabilities.setCapability("udid", strudid);
			capabilities.setCapability("appPackage", strPackage);
			capabilities.setCapability("appActivity", strActivity);
			capabilities.setCapability("unlockType", "pin");
			capabilities.setCapability("unlockKey", "123456");
			return capabilities;
	}
}
