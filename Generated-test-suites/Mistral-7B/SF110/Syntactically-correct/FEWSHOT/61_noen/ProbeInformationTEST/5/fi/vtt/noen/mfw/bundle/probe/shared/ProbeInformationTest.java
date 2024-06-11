package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testProbeInformationConstruction() {
        String targetName = "Target1";
        String targetType = "Type1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeName = "ProbeName1";
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
    }

    @Test
    public void testGetters() {
        String targetName = "Target1";
        String targetType = "Type1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeName = "ProbeName1";
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
    }

    @Test
    public void testMeasureURI() {
        String targetType = "Type1";
        String targetName = "Target1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";

        ProbeInformation probeInfo = new ProbeInformation("Target1", "Type1", "BMClass1", "BMName1", null, null, 1, null);

        String expectedMeasureURI = Const.createMeasureURI(targetType, targetName, bmClass, bmName);
        String actualMeasureURI = probeInfo.getMeasureURI();

        assertEquals(expectedMeasureURI, actualMeasureURI);
    }

    @Test
    public void testEquals() {
        String targetName1 = "Target1";
        String targetName2 = "Target2";
        String targetType1 = "Type1";
        String targetType2 = "Type2";
        String bmClass1 = "BMClass1";
        String bmClass2 = "BMClass2";
        String bmName1 = "BMName1";
        String bmName2 = "BMName2";
        String bmDescription1 = "BMDescription1";
        String bmDescription2 = "BMDescription2";
        String probeName1 = "ProbeName1";
        String probeName2 = "ProbeName2";
        int precision1 = 1;
        int precision2 = 2;
        String xmlRpcUrl1 = "http://test.com/xmlrpc1";
        String xmlRpcUrl2 = "http://test.com/xmlrpc2";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);

        ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        assertTrue(probeInfo1.equals(probeInfo3));
        assertFalse(probeInfo1.equals(probeInfo2));

        probeInfo1.setPrecision(2);

        assertTrue(probeInfo1.equals(probeInfo3));
        assertFalse(probeInfo1.equals(probeInfo2));

        probeInfo1.setBmClass("BMClass3");

        assertFalse(probeInfo1.equals(probeInfo3));
        assertFalse(probeInfo1.equals(probeInfo2));
    }

    @Test
    public void testHashCode() {
        String targetName = "Target1";
        String targetType = "Type1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeName = "ProbeName1";
        int precision = 1;

        ProbeInformation probeInfo1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, null);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, null);

        int hashCode1 = probeInfo1.hashCode();
        int hashCode2 = probeInfo2.hashCode();

        assertEquals(hashCode1, hashCode2);

        probeInfo1.setBmClass("BMClass2");

        int hashCode3 = probeInfo1.hashCode();
        int hashCode4 = probeInfo2.hashCode();

        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
    }

    @Test
    public void testToString() {
        String targetName = "Target1";
        String targetType = "Type1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeName = "ProbeName1";
        int precision = 1;

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, null);

        String expectedToString = "ProbeInformation{targetName='" + targetName + "', targetType='" + targetType + "', bmClass='" + bmClass + "', bmName='" + bmName + "', bmDescription='" + bmDescription + "', probeDescription='" + probeName + "', precision=1}";
        String actualToString = probeInfo.toString();

        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testNullArguments() {
        ProbeInformation probeInfo1 = new ProbeInformation(null, null, null, null, null, null, 0, null);
        ProbeInformation probeInfo2 = new ProbeInformation("", "", null, null, null, null, 0, null);
        ProbeInformation probeInfo3 = new ProbeInformation(null, null, null, null, null, null, 0, null);

        assertEquals(probeInfo1, probeInfo3);
        assertNull(probeInfo1.getTargetName());
        assertNull(probeInfo1.getTargetType());
        assertNull(probeInfo1.getBmClass());
        assertNull(probeInfo1.getBmName());
        assertNull(probeInfo1.getBmDescription());
        assertNull(probeInfo1.getProbeName());
    }
}