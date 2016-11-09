package com.webservicex.loadXmlWithXmlTemplate;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.orasi.utils.Randomness;
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;
import com.webservicex.genericBarCode.operations.GenerateBarCode;


public class TestSoapService extends TestEnvironment{
    @BeforeClass
    public void setup(){
	TestReporter.setDebugLevel(TestReporter.TRACE);
    }
	@Test
	public void makeBarGenericBarCode(){
		GenerateBarCode generate = new GenerateBarCode();
		setSoapService(generate);
		generate.sendRequest();
		System.out.println(generate.getBarCodeBytes());
		TestReporter.logAPI(generate.getBarCodeBytes() != "", "Generated byte string for barcode", generate);
		generate.generateBarCodeImage();
	}

	//@Test
	public void makeBarCode_FontSize(){
		GenerateBarCode generate = new GenerateBarCode();
		setSoapService(generate);
		generate.setFontSize("14");
		generate.sendRequest();
		System.out.println(generate.getBarCodeBytes());
		TestReporter.logAPI(generate.getBarCodeBytes() != "", "Generated byte string for barcode", generate);
		generate.generateBarCodeImage();
	}

	//@Test
	public void makeBarCode_DisplayText(){
		GenerateBarCode generate = new GenerateBarCode();
		generate.setBarCodeText(Randomness.randomNumber(12));
		setSoapService(generate);
		generate.sendRequest();
		System.out.println(generate.getBarCodeBytes());
		TestReporter.logAPI(generate.getBarCodeBytes() != "", "Generated byte string for barcode", generate);
		generate.generateBarCodeImage();
	}

	//@Test
	public void makeBarCode_DisplayPosition(){
		GenerateBarCode generate = new GenerateBarCode();
		setSoapService(generate);
		generate.setShowTextPosition("TopCenter");
		generate.sendRequest();
		System.out.println(generate.getBarCodeBytes());
		TestReporter.logAPI(generate.getBarCodeBytes() != "", "Generated byte string for barcode", generate);
		generate.generateBarCodeImage();
	}
}
