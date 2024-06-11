package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;

import org.junit.Test;

import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        String targetType = "testTargetType";
        ProbeInformation probeInformation = new ProbeInformation("testTargetName", targetType, "testBmClass", "testBmName", "testBmDescription", "testProbeName", 1, "testXmlRpcUrl");
        assertEquals(targetType, probeInformation.getTargetType());
    }

    @Test
    public void testGetBmName() {
        String bmName = "testBmName";
        ProbeInformation probeInformation = new ProbeInformation("testTargetName", "testTargetType", "testBmClass", bmName, "testBmDescription", "testProbeName", 1, "testXmlRpcUrl");
        assertEquals(bmName, probeInformation.getBmName());
    }

    @Test
    public void testGetProbeName() {
        String probeName = "testProbeName";
        ProbeInformation probeInformation = new ProbeInformation("testTargetName", "testTargetType", "testBmClass", "testBmName", "testBmDescription", probeName, 1, "testXmlRpcUrl");
        assertEquals(probeName, probeInformation.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        String targetName = "testTargetName";
        ProbeInformation probeInformation = new ProbeInformation(targetName, "testTargetType", "testBmClass", "testBmName", "testBmDescription", "testProbeName", 1, "testXmlRpcUrl");
        assertEquals(targetName, probeInformation.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        String bmClass = "testBmClass";
        ProbeInformation probeInformation = new ProbeInformation("testTargetName", "testTargetType", bmClass, "testBmName", "testBmDescription", "testProbeName", 1, "testXmlRpcUrl");
        assertEquals(bmClass, probeInformation.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        String bmDescription = "testBmDescription";
        ProbeInformation probeInformation = new ProbeInformation("testTargetName", "testTargetType", "testBmClass", "testBmName", bmDescription, "testProbeName", 1, "testXmlRpcUrl");
        assertEquals(bmDescription, probeInformation.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        int precision = 1;
        ProbeInformation probeInformation = new ProbeInformation("testTargetName", "testTargetType", "testBmClass", "testBmName", "testBmDescription", "testProbeName", precision, "testXmlRpcUrl");
        assertEquals(precision, probeInformation.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        String targetType = "testTargetType";
        String targetName = "testTargetName";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, "testBmDescription", "testProbeName", 1, "testXmlRpcUrl");
        String expectedMeasureURI = Const.createMeasureURI(targetType, targetName, bmClass, bmName);
        assertEquals(expectedMeasureURI, probeInformation.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation("testTargetName", "testTargetType", "testBmClass", "testBmName", "testBmDescription", "testProbeName", 1, xmlRpcUrl);
        assertEquals(xmlRpcUrl, probeInformation.getXmlRpcUrl());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullTargetName() {
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        new ProbeInformation(null, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullTargetType() {
        String targetName = "testTargetName";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        new ProbeInformation(targetName, null, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullBmClass() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        new ProbeInformation(targetName, targetType, null, bmName, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullBmName() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        new ProbeInformation(targetName, targetType, bmClass, null, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullBmDescription() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String probeName = "testProbeName";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        new ProbeInformation(targetName, targetType, bmClass, bmName, null, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullProbeName() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, null, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullXmlRpcUrl() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = 1;
        new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, null);
    }
}