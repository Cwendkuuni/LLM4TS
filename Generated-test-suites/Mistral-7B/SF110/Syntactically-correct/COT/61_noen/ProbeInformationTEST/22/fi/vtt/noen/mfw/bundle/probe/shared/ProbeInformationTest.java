package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;

import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        String targetType = "TestTargetType";
        ProbeInformation probeInformation = new ProbeInformation("TestTargetName", targetType, "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        String actual = probeInformation.getTargetType();
        assertEquals(targetType, actual);
    }

    @Test
    public void testGetBmName() {
        String bmName = "BMName";
        ProbeInformation probeInformation = new ProbeInformation("TestTargetName", "TargetType", "BMClass", bmName, "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        String actual = probeInformation.getBmName();
        assertEquals(bmName, actual);
    }

    @Test
    public void testGetProbeName() {
        String probeName = "ProbeName";
        ProbeInformation probeInformation = new ProbeInformation("TestTargetName", "TargetType", "BMClass", "BMName", "BMDescription", probeName, 1, "XMLRpcUrl");
        String actual = probeInformation.getProbeName();
        assertEquals(probeName, actual);
    }

    @Test
    public void testGetTargetName() {
        String targetName = "TestTargetName";
        ProbeInformation probeInformation = new ProbeInformation(targetName, "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        String actual = probeInformation.getTargetName();
        assertEquals(targetName, actual);
    }

    @Test
    public void testGetBmClass() {
        String bmClass = "BMClass";
        ProbeInformation probeInformation = new ProbeInformation("TestTargetName", "TargetType", bmClass, "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        String actual = probeInformation.getBmClass();
        assertEquals(bmClass, actual);
    }

    @Test
    public void testGetBmDescription() {
        String bmDescription = "BMDescription";
        ProbeInformation probeInformation = new ProbeInformation("TestTargetName", "TargetType", "BMClass", "BMName", bmDescription, "ProbeDescription", 1, "XMLRpcUrl");
        String actual = probeInformation.getBmDescription();
        assertEquals(bmDescription, actual);
    }

    @Test
    public void testGetPrecision() {
        int precision = 1;
        ProbeInformation probeInformation = new ProbeInformation("TestTargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", precision, "XMLRpcUrl");
        int actual = probeInformation.getPrecision();
        assertEquals(precision, actual);
    }

    @Test
    public void testGetMeasureURI() {
        String targetType = "TestTargetType";
        String targetName = "TestTargetName";
        String bmClass = "BMClass";
        String bmName = "BMName";
        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        String expected = Const.createMeasureURI(targetType, targetName, bmClass, bmName);
        String actual = probeInformation.getMeasureURI();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetXmlRpcUrl() {
        String xmlRpcUrl = "XMLRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation("TestTargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, xmlRpcUrl);
        String actual = probeInformation.getXmlRpcUrl();
        assertEquals(xmlRpcUrl, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructWithNullTargetName() {
        String targetType = "TestTargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";
        new ProbeInformation(null, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructWithNullTargetType() {
        String targetName = "TestTargetName";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";
        new ProbeInformation(targetName, null, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructWithNullBmClass() {
        String targetName = "TestTargetName";
        String targetType = "TestTargetType";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";
        new ProbeInformation(targetName, targetType, null, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructWithNullBmName() {
        String targetName = "TestTargetName";
        String targetType = "TestTargetType";
        String bmClass = "BMClass";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";
        new ProbeInformation(targetName, targetType, bmClass, null, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructWithNullBmDescription() {
        String targetName = "TestTargetName";
        String targetType = "TestTargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";
        new ProbeInformation(targetName, targetType, bmClass, bmName, null, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructWithNullProbeDescription() {
        String targetName = "TestTargetName";
        String targetType = "TestTargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";
        new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, null, precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithEmptyTargetName() {
        String targetType = "TestTargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";
        new ProbeInformation("", targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithEmptyTargetType() {
        String targetName = "TestTargetName";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";
        new ProbeInformation(targetName, "", bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithEmptyBmClass() {
        String targetName = "TestTargetName";
        String targetType = "TestTargetType";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";
        new ProbeInformation(targetName, targetType, "", bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithEmptyBmName() {
        String targetName = "TestTargetName";
        String targetType = "TestTargetType";
        String bmClass = "BMClass";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";
        new ProbeInformation(targetName, targetType, bmClass, "", bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test
    public void testToString() {
        String targetName = "TestTargetName";
        String targetType = "TestTargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
        String expected = "ProbeInformation{targetName='TestTargetName', targetType='TestTargetType', bmClass='BMClass', bmName='BMName', bmDescription='BMDescription', probeDescription='ProbeDescription', precision=1}";
        String actual = probeInformation.toString();
        assertEquals(expected, actual);
    }
}