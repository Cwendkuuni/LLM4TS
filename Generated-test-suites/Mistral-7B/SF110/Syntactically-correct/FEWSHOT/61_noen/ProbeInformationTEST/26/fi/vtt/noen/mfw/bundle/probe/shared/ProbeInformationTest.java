package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProbeInformationTest {
    @Test
    public void testProbeInformationConstruction() {
        String targetName = "TargetName1";
        String targetType = "TargetType1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeName = "ProbeName1";
        int precision = 1;
        String xmlRpcUrl = "http://example.com/xmlrpc";

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
    public void testGetMeasureURI() {
        String targetType = "TargetType1";
        String targetName = "TargetName1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";

        ProbeInformation probeInfo = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "", "", 1, "");

        assertEquals(Const.createMeasureURI(targetType, targetName, bmClass, bmName), probeInfo.getMeasureURI());
    }

    @Test
    public void testEquals() {
        String targetName1 = "TargetName1";
        String targetType1 = "TargetType1";
        String bmClass1 = "BMClass1";
        String bmName1 = "BMName1";
        String bmDescription1 = "BMDescription1";
        String probeName1 = "ProbeName1";
        int precision1 = 1;
        String xmlRpcUrl1 = "http://example.com/xmlrpc";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        String targetName2 = "TargetName2";
        String targetType2 = targetType1;
        String bmClass2 = bmClass1;
        String bmName2 = bmName1;
        String bmDescription2 = bmDescription1;
        String probeName2 = probeName1;
        int precision2 = precision1;
        String xmlRpcUrl2 = xmlRpcUrl1;

        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);

        ProbeInformation probeInfo3 = new ProbeInformation("TargetName3", "TargetType3", "BMClass3", "BMName3", "BMDescription3", "ProbeName3", 2, "http://example.com/xmlrpc2");

        assertTrue(probeInfo1.equals(probeInfo1));
        assertTrue(probeInfo1.equals(probeInfo2));
        assertFalse(probeInfo1.equals(probeInfo3));
        assertFalse(probeInfo1.equals(null));
        assertFalse(probeInfo1.equals("StringObject"));
    }

    @Test
    public void testHashCode() {
        String targetName = "TargetName1";
        String targetType = "TargetType1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeName = "ProbeName1";
        int precision = 1;
        String xmlRpcUrl = "http://example.com/xmlrpc";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        int hashCode = probeInfo.hashCode();

        ProbeInformation probeInfo2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(hashCode, probeInfo2.hashCode());
    }

    @Test
    public void testToString() {
        String targetName = "TargetName1";
        String targetType = "TargetType1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeName = "ProbeName1";
        int precision = 1;
        String xmlRpcUrl = "http://example.com/xmlrpc";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        String expectedString = "ProbeInformation{targetName='TargetName1', targetType='TargetType1', bmClass='BMClass1', bmName='BMName1', bmDescription='BMDescription1', probeDescription='ProbeName1', precision=1}";

        assertEquals(expectedString, probeInfo.toString());
    }
}