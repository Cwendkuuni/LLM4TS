package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;

import fi.vtt.noen.mfw.bundle.common.Const;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        String targetType = "testTargetType";
        ProbeInformation probeInfo = new ProbeInformation("testTargetName", targetType, "testBmClass", "testBmName", "testBmDescription", "testProbeName", 1, "testXmlRpcUrl");
        String actualTargetType = probeInfo.getTargetType();
        assertEquals(targetType, actualTargetType);
    }

    @Test
    public void testGetBmName() {
        String bmName = "testBmName";
        ProbeInformation probeInfo = new ProbeInformation("testTargetName", "testTargetType", "testBmClass", bmName, "testBmDescription", "testProbeName", 1, "testXmlRpcUrl");
        String actualBmName = probeInfo.getBmName();
        assertEquals(bmName, actualBmName);
    }

    @Test
    public void testGetProbeName() {
        String probeName = "testProbeName";
        ProbeInformation probeInfo = new ProbeInformation("testTargetName", "testTargetType", "testBmClass", "testBmName", "testBmDescription", probeName, 1, "testXmlRpcUrl");
        String actualProbeName = probeInfo.getProbeName();
        assertEquals(probeName, actualProbeName);
    }

    @Test
    public void testGetTargetName() {
        String targetName = "testTargetName";
        ProbeInformation probeInfo = new ProbeInformation(targetName, "testTargetType", "testBmClass", "testBmName", "testBmDescription", "testProbeName", 1, "testXmlRpcUrl");
        String actualTargetName = probeInfo.getTargetName();
        assertEquals(targetName, actualTargetName);
    }

    @Test
    public void testGetBmClass() {
        String bmClass = "testBmClass";
        ProbeInformation probeInfo = new ProbeInformation("testTargetName", "testTargetType", bmClass, "testBmName", "testBmDescription", "testProbeName", 1, "testXmlRpcUrl");
        String actualBmClass = probeInfo.getBmClass();
        assertEquals(bmClass, actualBmClass);
    }

    @Test
    public void testGetBmDescription() {
        String bmDescription = "testBmDescription";
        ProbeInformation probeInfo = new ProbeInformation("testTargetName", "testTargetType", "testBmClass", "testBmName", bmDescription, "testProbeName", 1, "testXmlRpcUrl");
        String actualBmDescription = probeInfo.getBmDescription();
        assertEquals(bmDescription, actualBmDescription);
    }

    @Test
    public void testGetPrecision() {
        int precision = 1;
        ProbeInformation probeInfo = new ProbeInformation("testTargetName", "testTargetType", "testBmClass", "testBmName", "testBmDescription", "testProbeName", precision, "testXmlRpcUrl");
        int actualPrecision = probeInfo.getPrecision();
        assertEquals(precision, actualPrecision);
    }

    @Test
    public void testGetMeasureURI() {
        String targetType = "testTargetType";
        String targetName = "testTargetName";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, "testBmDescription", "testProbeName", 1, "testXmlRpcUrl");
        String expectedMeasureURI = Const.createMeasureURI(targetType, targetName, bmClass, bmName);
        String actualMeasureURI = probeInfo.getMeasureURI();
        assertEquals(expectedMeasureURI, actualMeasureURI);
    }

    @Test
    public void testGetXmlRpcUrl() {
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInfo = new ProbeInformation("testTargetName", "testTargetType", "testBmClass", "testBmName", "testBmDescription", "testProbeName", 1, xmlRpcUrl);
        String actualXmlRpcUrl = probeInfo.getXmlRpcUrl();
        assertEquals(xmlRpcUrl, actualXmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testEqualsNullObject() {
        ProbeInformation probeInfo1 = new ProbeInformation("testTargetName1", "testTargetType1", "testBmClass1", "testBmName1", "testBmDescription1", "testProbeName1", 1, "testXmlRpcUrl1");
        Object nullObject = null;
        assertFalse(probeInfo1.equals(nullObject));
    }

    @Test
    public void testEqualsSameObject() {
        ProbeInformation probeInfo1 = new ProbeInformation("testTargetName1", "testTargetType1", "testBmClass1", "testBmName1", "testBmDescription1", "testProbeName1", 1, "testXmlRpcUrl1");
        ProbeInformation probeInfo2 = probeInfo1;
        assertTrue(probeInfo1.equals(probeInfo2));
    }

    @Test
    public void testEqualsDifferentObjects() {
        ProbeInformation probeInfo1 = new ProbeInformation("testTargetName1", "testTargetType1", "testBmClass1", "testBmName1", "testBmDescription1", "testProbeName1", 1, "testXmlRpcUrl1");
        ProbeInformation probeInfo2 = new ProbeInformation("testTargetName2", "testTargetType2", "testBmClass2", "testBmName2", "testBmDescription2", "testProbeName2", 1, "testXmlRpcUrl2");
        assertFalse(probeInfo1.equals(probeInfo2));
    }

    @Test
    public void testHashCodeSameObjects() {
        ProbeInformation probeInfo1 = new ProbeInformation("testTargetName1", "testTargetType1", "testBmClass1", "testBmName1", "testBmDescription1", "testProbeName1", 1, "testXmlRpcUrl1");
        ProbeInformation probeInfo2 = new ProbeInformation("testTargetName1", "testTargetType1", "testBmClass1", "testBmName1", "testBmDescription1", "testProbeName1", 1, "testXmlRpcUrl1");
        int actualHashCode1 = probeInfo1.hashCode();
        int actualHashCode2 = probeInfo2.hashCode();
        assertEquals(actualHashCode1, actualHashCode2);
    }

    @Test
    public void testHashCodeDifferentObjects() {
        ProbeInformation probeInfo1 = new ProbeInformation("testTargetName1", "testTargetType1", "testBmClass1", "testBmName1", "testBmDescription1", "testProbeName1", 1, "testXmlRpcUrl1");
        ProbeInformation probeInfo2 = new ProbeInformation("testTargetName2", "testTargetType2", "testBmClass2", "testBmName2", "testBmDescription2", "testProbeName2", 1, "testXmlRpcUrl2");
        int actualHashCode1 = probeInfo1.hashCode();
        int actualHashCode2 = probeInfo2.hashCode();
        assertNotEquals(actualHashCode1, actualHashCode2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullTargetName() {
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInfo = new ProbeInformation(null, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullTargetType() {
        String targetName = "testTargetName";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInfo = new ProbeInformation(targetName, null, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullBmClass() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, null, bmName, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullBmName() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, null, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullBmDescription() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String probeName = "testProbeName";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, null, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullProbeName() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, null, precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullPrecision() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, 0, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullXmlRpcUrl() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = 1;
        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, null);
    }
}