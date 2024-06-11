package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fi.vtt.noen.mfw.bundle.common.Const;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

	@Test
	public void testProbeInformationConstruction() {
		String targetName = "targetName";
		String targetType = "targetType";
		String bmClass = "bmClass";
		String bmName = "bmName";
		String bmDescription = "bmDescription";
		String probeName = "probeName";
		int precision = 1;
		String xmlRpcUrl = "xmlRpcUrl";

		ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

		assertEquals(targetName, probeInfo.getTargetName());
		assertEquals(targetType, probeInfo.getTargetType());
		assertEquals(bmClass, probeInfo.getBmClass());
		assertEquals(bmName, probeInfo.getBmName());
		assertEquals(bmDescription, probeInfo.getBmDescription());
		assertEquals(probeName, probeInfo.getProbeName());
		assertEquals(precision, probeInfo.getPrecision());
		assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());
		assertEquals(Const.createMeasureURI(targetType, targetName, bmClass, bmName), probeInfo.getMeasureURI());
	}

	@Test
	public void testGetters() {
		String targetName = "targetName";
		String targetType = "targetType";
		String bmClass = "bmClass";
		String bmName = "bmName";
		String bmDescription = "bmDescription";
		String probeName = "probeName";
		int precision = 1;
		String xmlRpcUrl = "xmlRpcUrl";

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
	public void testEquals() {
		String targetName1 = "targetName1";
		String targetName2 = "targetName2";
		String targetType = "targetType";
		String bmClass = "bmClass";
		String bmName = "bmName";
		String bmDescription = "bmDescription";
		String probeName = "probeName";
		int precision = 1;
		String xmlRpcUrl = "xmlRpcUrl";

		ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
		ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
		ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeName, precision, "xmlRpcUrlDifferent");
		ProbeInformation probeInfo4 = new ProbeInformation("differentTargetName", targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

		assertTrue(probeInfo1.equals(probeInfo1));
		assertTrue(probeInfo1.equals(probeInfo2));
		assertFalse(probeInfo1.equals(probeInfo3));
		assertFalse(probeInfo1.equals(probeInfo4));
	}

	@Test
	public void testHashCode() {
		String targetName = "targetName";
		String targetType = "targetType";
		String bmClass = "bmClass";
		String bmName = "bmName";
		String bmDescription = "bmDescription";
		String probeName = "probeName";
		int precision = 1;
		String xmlRpcUrl = "xmlRpcUrl";

		ProbeInformation probeInfo1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
		ProbeInformation probeInfo2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
		ProbeInformation probeInfo3 = new ProbeInformation("differentTargetName", targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

		int hashCode1 = probeInfo1.hashCode();
		int hashCode2 = probeInfo2.hashCode();
		int hashCode3 = probeInfo3.hashCode();

		assertNotSame(hashCode1, hashCode2);
		assertNotSame(hashCode1, hashCode3);
		assertEquals(hashCode1, hashCode2);
	}

	@Test
	public void testToString() {
		String targetName = "targetName";
		String targetType = "targetType";
		String bmClass = "bmClass";
		String bmName = "bmName";
		String bmDescription = "bmDescription";
		String probeName = "probeName";
		int precision = 1;
		String xmlRpcUrl = "xmlRpcUrl";

		ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

		String expectedString = "ProbeInformation{targetName='" + targetName + "', targetType='" + targetType + "', bmClass='" + bmClass + "', bmName='" + bmName + "', bmDescription='" + bmDescription + "', probeDescription='" + probeName + "', precision=1}";
		assertEquals(expectedString, probeInfo.toString());
	}

	@Test
	public void testNullValues() {
		String targetName = null;
		String targetType = null;
		String bmClass = null;
		String bmName = null;
		String bmDescription = null;
		String probeName = null;
		int precision = 1;
		String xmlRpcUrl = null;

		ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

		assertNull(probeInfo.getTargetName());
		assertNull(probeInfo.getTargetType());
		assertNull(probeInfo.getBmClass());
		assertNull(probeInfo.getBmName());
		assertNull(probeInfo.getBmDescription());
		assertNull(probeInfo.getProbeName());
		assertEquals(0, probeInfo.getPrecision());
		assertNull(probeInfo.getXmlRpcUrl());
		assertNull(probeInfo.getMeasureURI());
	}
}