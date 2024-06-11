package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;

import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        String targetType = "testTargetType";
        ProbeInformation probeInformation = new ProbeInformation("testTargetName", targetType, "testBmClass", "testBmName", "testBmDescription", "testProbeDescription", 1, "testXmlRpcUrl");
        String actualTargetType = probeInformation.getTargetType();
        assertEquals(targetType, actualTargetType);
    }

    @Test
    public void testGetBmName() {
        String bmName = "testBmName";
        ProbeInformation probeInformation = new ProbeInformation("testTargetName", "testTargetType", "testBmClass", bmName, "testBmDescription", "testProbeDescription", 1, "testXmlRpcUrl");
        String actualBmName = probeInformation.getBmName();
        assertEquals(bmName, actualBmName);
    }

    @Test
    public void testGetProbeName() {
        String probeName = "testProbeName";
        ProbeInformation probeInformation = new ProbeInformation("testTargetName", "testTargetType", "testBmClass", "testBmName", "testBmDescription", probeName, 1, "testXmlRpcUrl");
        String actualProbeName = probeInformation.getProbeName();
        assertEquals(probeName, actualProbeName);
    }

    @Test
    public void testGetTargetName() {
        String targetName = "testTargetName";
        ProbeInformation probeInformation = new ProbeInformation(targetName, "testTargetType", "testBmClass", "testBmName", "testBmDescription", "testProbeDescription", 1, "testXmlRpcUrl");
        String actualTargetName = probeInformation.getTargetName();
        assertEquals(targetName, actualTargetName);
    }

    @Test
    public void testGetBmClass() {
        String bmClass = "testBmClass";
        ProbeInformation probeInformation = new ProbeInformation("testTargetName", "testTargetType", bmClass, "testBmName", "testBmDescription", "testProbeDescription", 1, "testXmlRpcUrl");
        String actualBmClass = probeInformation.getBmClass();
        assertEquals(bmClass, actualBmClass);
    }

    @Test
    public void testGetBmDescription() {
        String bmDescription = "testBmDescription";
        ProbeInformation probeInformation = new ProbeInformation("testTargetName", "testTargetType", "testBmClass", "testBmName", bmDescription, "testProbeDescription", 1, "testXmlRpcUrl");
        String actualBmDescription = probeInformation.getBmDescription();
        assertEquals(bmDescription, actualBmDescription);
    }

    @Test
    public void testGetPrecision() {
        int precision = 1;
        ProbeInformation probeInformation = new ProbeInformation("testTargetName", "testTargetType", "testBmClass", "testBmName", "testBmDescription", "testProbeDescription", precision, "testXmlRpcUrl");
        int actualPrecision = probeInformation.getPrecision();
        assertEquals(precision, actualPrecision);
    }

    @Test
    public void testGetMeasureURI() {
        String targetType = "testTargetType";
        String targetName = "testTargetName";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, "testBmDescription", "testProbeDescription", 1, "testXmlRpcUrl");
        String actualMeasureURI = probeInformation.getMeasureURI();
        String expectedMeasureURI = Const.createMeasureURI(targetType, targetName, bmClass, bmName);
        assertEquals(expectedMeasureURI, actualMeasureURI);
    }

    @Test
    public void testGetXmlRpcUrl() {
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation("testTargetName", "testTargetType", "testBmClass", "testBmName", "testBmDescription", "testProbeDescription", 1, xmlRpcUrl);
        String actualXmlRpcUrl = probeInformation.getXmlRpcUrl();
        assertEquals(xmlRpcUrl, actualXmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullTargetName() {
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeDescription = "testProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation(null, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullTargetType() {
        String targetName = "testTargetName";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeDescription = "testProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation(targetName, null, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullBmClass() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeDescription = "testProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, null, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullBmName() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmDescription = "testBmDescription";
        String probeDescription = "testProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, null, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullBmDescription() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String probeDescription = "testProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, null, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullProbeDescription() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, null, precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyTargetName() {
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeDescription = "testProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation("", targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyTargetType() {
        String targetName = "testTargetName";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeDescription = "testProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation(targetName, "", bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyBmClass() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeDescription = "testProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, "", bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyBmName() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmDescription = "testBmDescription";
        String probeDescription = "testProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, "", bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyBmDescription() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String probeDescription = "testProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, "", probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyProbeDescription() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, "", precision, xmlRpcUrl);
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
        String expectedToString = "ProbeInformation{targetName='testTargetName', targetType='testTargetType', bmClass='testBmClass', bmName='testBmName', bmDescription='testBmDescription', probeDescription='testProbeDescription', precision=1}";
        String actualToString = probeInformation.toString();
        assertEquals(expectedToString, actualToString);
    }
}