package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;

import fi.vtt.noen.mfw.bundle.common.Const;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

import org.junit.Test;

public class ProbeInformationTest {

	@Test
	public void testProbeInformationConstructor() {
		String targetName = "TargetName";
		String targetType = "TargetType";
		String bmClass = "BMClass";
		String bmName = "BMName";
		String bmDescription = "BMDescription";
		String probeName = "ProbeName";
		int precision = 4;
		String xmlRpcUrl = "http://localhost:8080/xmlrpc";

		ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

		assertEquals(targetName, probeInfo.getTargetName());
		assertEquals(targetType, probeInfo.getTargetType());
		assertEquals(bmClass, probeInfo.getBmClass());
		assertEquals(bmName, probeInfo.getBmName());
		assertEquals(bmDescription, probeInfo.getBmDescription());
		assertEquals(probeName, probeInfo.getProbeName());
		assertEquals(precision, probeInfo.getPrecision());
		assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());
	}

	@Test
	public void testGetters() {
		String targetName = "TargetName";
		String targetType = "TargetType";
		String bmClass = "BMClass";
		String bmName = "BMName";
		String bmDescription = "BMDescription";
		String probeName = "ProbeName";
		int precision = 4;
		String xmlRpcUrl = "http://localhost:8080/xmlrpc";

		ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

		assertEquals(targetName, probeInfo.getTargetName());
		assertEquals(targetType, probeInfo.getTargetType());
		assertEquals(bmClass, probeInfo.getBmClass());
		assertEquals(bmName, probeInfo.getBmName());
		assertEquals(bmDescription, probeInfo.getBmDescription());
		assertEquals(probeName, probeInfo.getProbeName());
		assertEquals(precision, probeInfo.getPrecision());
		assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());
	}

	@Test
	public void testToString() {
		String targetName = "TargetName";
		String targetType = "TargetType";
		String bmClass = "BMClass";
		String bmName = "BMName";
		String bmDescription = "BMDescription";
		String probeName = "ProbeName";
		int precision = 4;
		String xmlRpcUrl = "http://localhost:8080/xmlrpc";

		ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

		String expectedResult = "ProbeInformation{targetName='" + targetName + "\', targetType='" + targetType + "\', bmClass='" + bmClass + "\', bmName='" + bmName + "\', bmDescription='" + bmDescription + "\', probeDescription='" + probeName + "\', precision=4}";
		assertEquals(expectedResult, probeInfo.toString());
	}

	@Test
	public void testEquals() {
		String targetName1 = "TargetName1";
		String targetName2 = "TargetName2";
		String targetType1 = "TargetType1";
		String targetType2 = "TargetType2";
		String bmClass1 = "BMClass1";
		String bmClass2 = "BMClass2";
		String bmName1 = "BMName1";
		String bmName2 = "BMName2";
		String bmDescription1 = "BMDescription1";
		String bmDescription2 = "BMDescription2";
		String probeName1 = "ProbeName1";
		String probeName2 = "ProbeName2";
		int precision1 = 4;
		int precision2 = 4;
		String xmlRpcUrl1 = "http://localhost:8080/xmlrpc";
		String xmlRpcUrl2 = "http://localhost:8080/xmlrpc";

		ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
		ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);
		ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

		assertTrue(probeInfo1.equals(probeInfo3));
		assertFalse(probeInfo1.equals(probeInfo2));
	}

	@Test
	public void testHashCode() {
		String targetName = "TargetName";
		String targetType = "TargetType";
		String bmClass = "BMClass";
		String bmName = "BMName";
		String bmDescription = "BMDescription";
		String probeName = "ProbeName";
		int precision = 4;
		String xmlRpcUrl = "http://localhost:8080/xmlrpc";

		ProbeInformation probeInfo1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
		ProbeInformation probeInfo2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

		int hashCode1 = probeInfo1.hashCode();
		int hashCode2 = probeInfo2.hashCode();

		assertEquals(hashCode1, hashCode2);
	}

	@Test
	public void testCreateMeasureURI() {
		String targetType = "TargetType";
		String targetName = "TargetName";
		String bmClass = "BMClass";
		String bmName = "BMName";

		assertEquals(Const.createMeasureURI(targetType, targetName, bmClass, bmName), Const.createMeasureURI(targetType, targetName, bmClass, bmName));
	}

}