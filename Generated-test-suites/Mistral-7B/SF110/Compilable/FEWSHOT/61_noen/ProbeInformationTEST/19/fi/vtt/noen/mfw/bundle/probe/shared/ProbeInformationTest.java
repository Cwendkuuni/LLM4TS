package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testProbeInformationConstructors() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeName = "probeName";
        int precision = 42;
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(probeInfo1, probeInfo2);
    }

    @Test
    public void testGetters() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeName = "probeName";
        int precision = 42;
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(targetType, probeInfo.getTargetType());
        assertEquals(bmName, probeInfo.getBmName());
        assertEquals(probeName, probeInfo.getProbeName());
        assertEquals(targetName, probeInfo.getTargetName());
        assertEquals(bmClass, probeInfo.getBmClass());
        assertEquals(bmDescription, probeInfo.getBmDescription());
        assertEquals(precision, probeInfo.getPrecision());
        assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());
    }

    @Test
    public void testToString() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeName = "probeName";
        int precision = 42;
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        String expectedResult = "ProbeInformation{targetName='" + targetName + "', targetType='" + targetType + "', bmClass='" + bmClass + "', bmName='" + bmName + "', bmDescription='" + bmDescription + "', probeDescription='" + probeName + "', precision=42}";

        assertEquals(expectedResult, probeInfo.toString());
    }

    @Test
    public void testEqualsAndHashcode() {
        String targetName1 = "targetName1";
        String targetName2 = "targetName2";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeName1 = "probeName1";
        String probeName2 = "probeName2";
        int precision = 42;
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeName1, precision, xmlRpcUrl);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType, bmClass, bmName, bmDescription, probeName2, precision, xmlRpcUrl);
        ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeName1, precision, xmlRpcUrl);
        ProbeInformation probeInfo4 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeName2, precision, xmlRpcUrl);

        assertTrue(probeInfo1.equals(probeInfo3));
        assertTrue(probeInfo1.hashCode() == probeInfo3.hashCode());
        assertFalse(probeInfo1.equals(probeInfo2));
        assertFalse(probeInfo1.hashCode() == probeInfo2.hashCode());
    }
}