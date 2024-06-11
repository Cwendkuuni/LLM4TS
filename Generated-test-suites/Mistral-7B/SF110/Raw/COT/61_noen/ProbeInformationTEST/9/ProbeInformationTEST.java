package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;

import fi.vtt.noen.mfw.bundle.common.Const;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

import org.junit.Test;

public class ProbeInformationTest {

	@Test
	public void testConstructor() {
		String targetName = "testTargetName";
		String targetType = "testTargetType";
		String bmClass = "testBmClass";
		String bmName = "testBmName";
		String bmDescription = "testBmDescription";
		String probeDescription = "testProbeDescription";
		int precision = 1;
		String xmlRpcUrl = "testXmlRpcUrl";

		ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

		assertEquals(targetName, probeInformation.getTargetName());
		assertEquals(targetType, probeInformation.getTargetType());
		assertEquals(bmClass, probeInformation.getBmClass());
		assertEquals(bmName, probeInformation.getBmName());
		assertEquals(bmDescription, probeInformation.getBmDescription());
		assertEquals(probeDescription, probeInformation.getProbeName());
		assertEquals(precision, probeInformation.getPrecision());
		assertEquals(xmlRpcUrl, probeInformation.getXmlRpcUrl());
	}

	@Test
	public void testGetters() {
		String targetName = "testTargetName";
		String targetType = "testTargetType";
		String bmClass = "testBmClass";
		String bmName = "testBmName";
		String bmDescription = "testBmDescription";
		String probeName = "testProbeName";
		int precision = 1;
		String xmlRpcUrl = "testXmlRpcUrl";

		ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

		assertEquals(targetName, probeInformation.getTargetName());
		assertEquals(targetType, probeInformation.getTargetType());
		assertEquals(bmClass, probeInformation.getBmClass());
		assertEquals(bmName, probeInformation.getBmName());
		assertEquals(bmDescription, probeInformation.getBmDescription());
		assertEquals(probeName, probeInformation.getProbeName());
		assertEquals(precision, probeInformation.getPrecision());
		assertEquals(xmlRpcUrl, probeInformation.getXmlRpcUrl());
	}

	@Test(expected = NullPointerException.class)
	public void testGettersWithNullArgument() {
		ProbeInformation probeInformation = new ProbeInformation(null, null, null, null, null, null, 0, null);

		probeInformation.getTargetName();
		probeInformation.getTargetType();
		probeInformation.getBmClass();
		probeInformation.getBmName();
		probeInformation.getBmDescription();
		probeInformation.getProbeName();
	}

	@Test
	public void testToString() {
		String targetName = "testTargetName";
		String targetType = "testTargetType";
		String bmClass = "testBmClass";
		String bmName = "testBmName";
		String bmDescription = "testBmDescription";
		String probeDescription = "testProbeDescription";
		int precision = 1;
		String xmlRpcUrl = "testXmlRpcUrl";

		ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

		String expected = "ProbeInformation{targetName='" + targetName + "\'\', targetType='" + targetType + "\'\', bmClass='" + bmClass + "\'\', bmName='" + bmName + "\'\', bmDescription='" + bmDescription + "\'\', probeDescription='" + probeDescription + "\'\', precision=1}";
		assertEquals(expected, probeInformation.toString());
	}

	@Test
	public void testEquals() {
		String targetName1 = "testTargetName1";
		String targetName2 = "testTargetName2";
		String targetType1 = "testTargetType1";
		String targetType2 = "testTargetType2";
		String bmClass1 = "testBmClass1";
		String bmClass2 = "testBmClass2";
		String bmName1 = "testBmName1";
		String bmName2 = "testBmName2";
		String bmDescription1 = "testBmDescription1";
		String bmDescription2 = "testBmDescription2";
		String probeName1 = "testProbeName1";
		String probeName2 = "testProbeName2";
		int precision1 = 1;
		int precision2 = 2;
		String xmlRpcUrl1 = "testXmlRpcUrl1";
		String xmlRpcUrl2 = "testXmlRpcUrl2";

		ProbeInformation probeInformation1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
		ProbeInformation probeInformation2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);
		ProbeInformation probeInformation3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

		assertTrue(probeInformation1.equals(probeInformation1));
		assertTrue(probeInformation1.equals(probeInformation3));
		assertFalse(probeInformation1.equals(probeInformation2));
		assertFalse(probeInformation1.equals(null));
		assertFalse(probeInformation1.equals(""));
	}

	@Test
	public void testHashCode() {
		String targetName1 = "testTargetName1";
		String targetName2 = "testTargetName2";
		String targetType1 = "testTargetType1";
		String targetType2 = "testTargetType2";
		String bmClass1 = "testBmClass1";
		String bmClass2 = "testBmClass2";
		String bmName1 = "testBmName1";
		String bmName2 = "testBmName2";
		String bmDescription1 = "testBmDescription1";
		String bmDescription2 = "testBmDescription2";
		String probeName1 = "testProbeName1";
		String probeName2 = "testProbeName2";
		int precision1 = 1;
		int precision2 = 2;
		String xmlRpcUrl1 = "testXmlRpcUrl1";
		String xmlRpcUrl2 = "testXmlRpcUrl2";

		ProbeInformation probeInformation1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
		ProbeInformation probeInformation2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);
		ProbeInformation probeInformation3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

		int hashCode1 = probeInformation1.hashCode();
		int hashCode2 = probeInformation2.hashCode();
		int hashCode3 = probeInformation3.hashCode();

		assertEquals(hashCode1, hashCode3);
		assertNotEquals(hashCode1, hashCode2);
	}

	@Test
	public void testGetMeasureURI() {
		String targetType = "testTargetType";
		String targetName = "testTargetName";
		String bmClass = "testBmClass";
		String bmName = "testBmName";

		ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, null, null, null, 0, null);

		String expected = Const.createMeasureURI(targetType, targetName, bmClass, bmName);
		assertEquals(expected, probeInformation.getMeasureURI());
	}

	@Test
	public void testGetMeasureURIWithNullArgument() {
		String targetType = null;

		ProbeInformation probeInformation = new ProbeInformation(null, null, null, null, null, null, 0, null);

		assertNull(probeInformation.getMeasureURI());
	}
}