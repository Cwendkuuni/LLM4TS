package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testProbeInformationWithValidArguments() {
        String targetName = "target1";
        String targetType = "type1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeDescription = "ProbeDescription1";
        int precision = 1;
        String xmlRpcUrl = "http://localhost:8080/xmlrpc";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        assertEquals(targetName, probeInfo.getTargetName());
        assertEquals(targetType, probeInfo.getTargetType());
        assertEquals(bmClass, probeInfo.getBmClass());
        assertEquals(bmName, probeInfo.getBmName());
        assertEquals(bmDescription, probeInfo.getBmDescription());
        assertEquals(probeDescription, probeInfo.getProbeName());
        assertEquals(precision, probeInfo.getPrecision());
        assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());
    }

    @Test
    public void testGetMeasureURI() {
        String targetType = "type1";
        String targetName = "target1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";

        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "BMClass1", "BMName1", null, null, 1, null);

        assertEquals(Const.createMeasureURI(targetType, targetName, bmClass, bmName), probeInfo.getMeasureURI());
    }

    @Test
    public void testToString() {
        String targetName = "target1";
        String targetType = "type1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeDescription = "ProbeDescription1";
        int precision = 1;
        String xmlRpcUrl = "http://localhost:8080/xmlrpc";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        String expectedString = "ProbeInformation{targetName='target1', targetType='type1', bmClass='BMClass1', bmName='BMName1', bmDescription='BMDescription1', probeDescription='ProbeDescription1', precision=1}";

        assertEquals(expectedString, probeInfo.toString());
    }

    @Test
    public void testEqualsAndHashCode() {
        String targetName1 = "target1";
        String targetName2 = "target2";
        String targetType = "type1";
        String bmClass1 = "BMClass1";
        String bmClass2 = "BMClass2";
        String bmName1 = "BMName1";
        String bmName2 = "BMName2";
        String bmDescription1 = "BMDescription1";
        String bmDescription2 = "BMDescription2";
        String probeDescription1 = "ProbeDescription1";
        String probeDescription2 = "ProbeDescription2";
        int precision1 = 1;
        int precision2 = 2;
        String xmlRpcUrl1 = "http://localhost:8080/xmlrpc";
        String xmlRpcUrl2 = "http://localhost:8080/xmlrpc";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType, bmClass2, bmName2, bmDescription2, probeDescription2, precision2, xmlRpcUrl2);

        ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);

        ProbeInformation probeInfo4 = new ProbeInformation(targetName1, targetType, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl2);

        assertTrue(probeInfo1.equals(probeInfo3));
        assertFalse(probeInfo1.equals(probeInfo2));
        assertFalse(probeInfo1.equals(null));
        assertEquals(probeInfo1.hashCode(), probeInfo3.hashCode());
        assertNotEquals(probeInfo1.hashCode(), probeInfo2.hashCode());
        assertNotEquals(probeInfo1.hashCode(), null.hashCode());
    }
}