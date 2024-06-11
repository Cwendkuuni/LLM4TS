package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;

import org.junit.Test;

import fi.vtt.noen.mfw.bundle.common.Const;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

	@Test
	public void testProbeInformationConstructor() {
		String targetName = "TestTarget";
		String targetType = "TestType";
		String bmClass = "TestBMClass";
		String bmName = "TestBMName";
		String bmDescription = "TestBMDescription";
		String probeName = "TestProbeName";
		int precision = 1;
		String xmlRpcUrl = "http://test.com/xmlrpc";

		ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

		assertEquals(targetName, probeInfo.getTargetName());
		assertEquals(targetType, probeInfo.getTargetType());
		assertEquals(bmClass, probeInfo.getBmClass());
		assertEquals(bmName, probeInfo.getBmName());
		assertEquals(bmDescription, probeInfo.getBmDescription());
		assertEquals(probeName, probeInfo.getProbeName());
		assertEquals(precision, probeInfo.getPrecision());
		assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());
		assertEquals(probeInfo.toString(), "ProbeInformation{targetName='TestTarget', targetType='TestType', bmClass='TestBMClass', bmName='TestBMName', bmDescription='TestBMDescription', probeDescription='TestProbeName', precision=1}");
	}

	@Test
	public void testGetMethods() {
		String targetName = "TestTarget";
		String targetType = "TestType";
		String bmClass = "TestBMClass";
		String bmName = "TestBMName";
		String bmDescription = "TestBMDescription";
		String probeName = "TestProbeName";
		int precision = 1;
		String xmlRpcUrl = "http://test.com/xmlrpc";

		ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

		assertEquals(targetType, probeInfo.getTargetType());
		assertEquals(bmName, probeInfo.getBmName());
		assertEquals(probeName, probeInfo.getProbeName());
		assertEquals(targetName, probeInfo.getTargetName());
		assertEquals(bmClass, probeInfo.getBmClass());
		assertEquals(bmDescription, probeInfo.getBmDescription());
		assertEquals(precision, probeInfo.getPrecision());
		assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());
		assertEquals(Const.createMeasureURI(targetType, targetName, bmClass, bmName), probeInfo.getMeasureURI());
	}

	@Test
	public void testEquals() {
		String targetName1 = "TestTarget1";
		String targetName2 = "TestTarget2";
		String targetType1 = "TestType1";
		String targetType2 = "TestType2";
		String bmClass1 = "TestBMClass1";
		String bmClass2 = "TestBMClass2";
		String bmName1 = "TestBMName1";
		String bmName2 = "TestBMName2";
		String bmDescription1 = "TestBMDescription1";
		String bmDescription2 = "TestBMDescription2";
		String probeName1 = "TestProbeName1";
		String probeName2 = "TestProbeName2";
		int precision1 = 1;
		int precision2 = 2;
		String xmlRpcUrl1 = "http://test1.com/xmlrpc";
		String xmlRpcUrl2 = "http://test2.com/xmlrpc";

		ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
		ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);
		ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

		assertTrue(probeInfo1.equals(probeInfo3));
		assertFalse(probeInfo1.equals(probeInfo2));
	}

	@Test
	public void testHashCode() {
		String targetName1 = "TestTarget1";
		String targetName2 = "TestTarget2";
		String targetType1 = "TestType1";
		String targetType2 = "TestType2";
		String bmClass1 = "TestBMClass1";
		String bmClass2 = "TestBMClass2";
		String bmName1 = "TestBMName1";
		String bmName2 = "TestBMName2";
		String bmDescription1 = "TestBMDescription1";
		String bmDescription2 = "TestBMDescription2";
		String probeName1 = "TestProbeName1";
		String probeName2 = "TestProbeName2";
		int precision1 = 1;
		int precision2 = 2;
		String xmlRpcUrl1 = "http://test1.com/xmlrpc";
		String xmlRpcUrl2 = "http://test2.com/xmlrpc";

		ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
		ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);
		ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

		int hashCode1 = probeInfo1.hashCode();
		int hashCode2 = probeInfo2.hashCode();
		int hashCode3 = probeInfo3.hashCode();

		assertEquals(hashCode1, hashCode3);
		assertNotSame(hashCode1, hashCode2);
	}
}