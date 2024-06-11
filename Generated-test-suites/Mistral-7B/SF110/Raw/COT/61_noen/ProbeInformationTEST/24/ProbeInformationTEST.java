package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;

import org.junit.Test;

import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        String targetType = "TestTargetType";
        ProbeInformation probeInformation = new ProbeInformation("TestTargetName", targetType, "TestBMClass", "TestBMName", "TestBMDescription", "TestProbeName", 4, "TestXmlRpcUrl");
        String actualTargetType = probeInformation.getTargetType();
        assertEquals(targetType, actualTargetType);
    }

    @Test
    public void testGetBmName() {
        String bmName = "TestBMName";
        ProbeInformation probeInformation = new ProbeInformation("TestTargetName", "TestTargetType", "TestBMClass", bmName, "TestBMDescription", "TestProbeName", 4, "TestXmlRpcUrl");
        String actualBmName = probeInformation.getBmName();
        assertEquals(bmName, actualBmName);
    }

    @Test
    public void testGetProbeName() {
        String probeName = "TestProbeName";
        ProbeInformation probeInformation = new ProbeInformation("TestTargetName", "TestTargetType", "TestBMClass", "TestBMName", "TestBMDescription", probeName, 4, "TestXmlRpcUrl");
        String actualProbeName = probeInformation.getProbeName();
        assertEquals(probeName, actualProbeName);
    }

    @Test
    public void testGetTargetName() {
        String targetName = "TestTargetName";
        ProbeInformation probeInformation = new ProbeInformation(targetName, "TestTargetType", "TestBMClass", "TestBMName", "TestBMDescription", "TestProbeName", 4, "TestXmlRpcUrl");
        String actualTargetName = probeInformation.getTargetName();
        assertEquals(targetName, actualTargetName);
    }

    @Test
    public void testGetBmClass() {
        String bmClass = "TestBMClass";
        ProbeInformation probeInformation = new ProbeInformation("TestTargetName", "TestTargetType", bmClass, "TestBMName", "TestBMDescription", "TestProbeName", 4, "TestXmlRpcUrl");
        String actualBmClass = probeInformation.getBmClass();
        assertEquals(bmClass, actualBmClass);
    }

    @Test
    public void testGetBmDescription() {
        String bmDescription = "TestBMDescription";
        ProbeInformation probeInformation = new ProbeInformation("TestTargetName", "TestTargetType", "TestBMClass", "TestBMName", bmDescription, "TestProbeName", 4, "TestXmlRpcUrl");
        String actualBmDescription = probeInformation.getBmDescription();
        assertEquals(bmDescription, actualBmDescription);
    }

    @Test
    public void testGetPrecision() {
        int precision = 4;
        ProbeInformation probeInformation = new ProbeInformation("TestTargetName", "TestTargetType", "TestBMClass", "TestBMName", "TestBMDescription", "TestProbeName", precision, "TestXmlRpcUrl");
        int actualPrecision = probeInformation.getPrecision();
        assertEquals(precision, actualPrecision);
    }

    @Test
    public void testGetMeasureURI() {
        String targetType = "TestTargetType";
        String targetName = "TestTargetName";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, "TestBMDescription", "TestProbeName", 4, "TestXmlRpcUrl");
        String expectedMeasureURI = Const.createMeasureURI(targetType, targetName, bmClass, bmName);
        String actualMeasureURI = probeInformation.getMeasureURI();
        assertEquals(expectedMeasureURI, actualMeasureURI);
    }

    @Test
    public void testGetXmlRpcUrl() {
        String xmlRpcUrl = "TestXmlRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation("TestTargetName", "TestTargetType", "TestBMClass", "TestBMName", "TestBMDescription", "TestProbeName", 4, xmlRpcUrl);
        String actualXmlRpcUrl = probeInformation.getXmlRpcUrl();
        assertEquals(xmlRpcUrl, actualXmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testEqualsNullObject() {
        ProbeInformation probeInformation = new ProbeInformation("TestTargetName", "TestTargetType", "TestBMClass", "TestBMName", "TestBMDescription", "TestProbeName", 4, "TestXmlRpcUrl");
        assertFalse(probeInformation.equals(null));
    }

    @Test
    public void testEqualsEqualObjects() {
        String targetName = "TestTargetName";
        String targetType = "TestTargetType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeName = "TestProbeName";
        int precision = 4;
        String xmlRpcUrl = "TestXmlRpcUrl";
        ProbeInformation probeInformation1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
        ProbeInformation probeInformation2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
        assertTrue(probeInformation1.equals(probeInformation2));
    }

    @Test
    public void testEqualsDifferentObjects() {
        String targetName1 = "TestTargetName1";
        String targetName2 = "TestTargetName2";
        String targetType1 = "TestTargetType1";
        String targetType2 = "TestTargetType2";
        String bmClass1 = "TestBMClass1";
        String bmClass2 = "TestBMClass2";
        String bmName1 = "TestBMName1";
        String bmName2 = "TestBMName2";
        String bmDescription1 = "TestBMDescription1";
        String bmDescription2 = "TestBMDescription2";
        String probeName1 = "TestProbeName1";
        String probeName2 = "TestProbeName2";
        int precision1 = 4;
        int precision2 = 5;
        String xmlRpcUrl1 = "TestXmlRpcUrl1";
        String xmlRpcUrl2 = "TestXmlRpcUrl2";
        ProbeInformation probeInformation1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        ProbeInformation probeInformation2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);
        assertFalse(probeInformation1.equals(probeInformation2));
    }

    @Test
    public void testHashCodeEqualObjects() {
        String targetName = "TestTargetName";
        String targetType = "TestTargetType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeName = "TestProbeName";
        int precision = 4;
        String xmlRpcUrl = "TestXmlRpcUrl";
        ProbeInformation probeInformation1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
        ProbeInformation probeInformation2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
        int expectedHashCode = probeInformation1.hashCode();
        int actualHashCode = probeInformation2.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testHashCodeDifferentObjects() {
        String targetName1 = "TestTargetName1";
        String targetName2 = "TestTargetName2";
        String targetType1 = "TestTargetType1";
        String targetType2 = "TestTargetType2";
        String bmClass1 = "TestBMClass1";
        String bmClass2 = "TestBMClass2";
        String bmName1 = "TestBMName1";
        String bmName2 = "TestBMName2";
        String bmDescription1 = "TestBMDescription1";
        String bmDescription2 = "TestBMDescription2";
        String probeName1 = "TestProbeName1";
        String probeName2 = "TestProbeName2";
        int precision1 = 4;
        int precision2 = 5;
        String xmlRpcUrl1 = "TestXmlRpcUrl1";
        String xmlRpcUrl2 = "TestXmlRpcUrl2";
        ProbeInformation probeInformation1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        ProbeInformation probeInformation2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);
        int expectedHashCode1 = probeInformation1.hashCode();
        int expectedHashCode2 = probeInformation2.hashCode();
        assertNotNull(expectedHashCode1);
        assertNotNull(expectedHashCode2);
        assertNotEquals(expectedHashCode1, expectedHashCode2);
    }
}