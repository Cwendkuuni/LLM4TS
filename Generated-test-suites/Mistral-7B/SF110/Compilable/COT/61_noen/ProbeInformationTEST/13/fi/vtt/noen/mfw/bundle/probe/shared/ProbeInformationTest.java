package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;

import fi.vtt.noen.mfw.bundle.common.Const;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

import org.junit.Test;

public class ProbeInformationTest
{
    @Test
    public void testGetTargetType()
    {
        String targetType = "testTargetType";
        ProbeInformation probeInfo = new ProbeInformation("testTargetName", targetType, "testBmClass", "testBmName", "testBmDescription", "testProbeName", 1, "testXmlRpcUrl");
        String returnedTargetType = probeInfo.getTargetType();

        assertEquals(targetType, returnedTargetType);
    }

    @Test
    public void testGetBmName()
    {
        String bmName = "testBmName";
        ProbeInformation probeInfo = new ProbeInformation("testTargetName", "testTargetType", "testBmClass", bmName, "testBmDescription", "testProbeName", 1, "testXmlRpcUrl");
        String returnedBmName = probeInfo.getBmName();

        assertEquals(bmName, returnedBmName);
    }

    @Test
    public void testGetProbeName()
    {
        String probeName = "testProbeName";
        ProbeInformation probeInfo = new ProbeInformation("testTargetName", "testTargetType", "testBmClass", "testBmName", "testBmDescription", probeName, 1, "testXmlRpcUrl");
        String returnedProbeName = probeInfo.getProbeName();

        assertEquals(probeName, returnedProbeName);
    }

    @Test
    public void testGetTargetName()
    {
        String targetName = "testTargetName";
        ProbeInformation probeInfo = new ProbeInformation(targetName, "testTargetType", "testBmClass", "testBmName", "testBmDescription", "testProbeName", 1, "testXmlRpcUrl");
        String returnedTargetName = probeInfo.getTargetName();

        assertEquals(targetName, returnedTargetName);
    }

    @Test
    public void testGetBmClass()
    {
        String bmClass = "testBmClass";
        ProbeInformation probeInfo = new ProbeInformation("testTargetName", "testTargetType", bmClass, "testBmName", "testBmDescription", "testProbeName", 1, "testXmlRpcUrl");
        String returnedBmClass = probeInfo.getBmClass();

        assertEquals(bmClass, returnedBmClass);
    }

    @Test
    public void testGetBmDescription()
    {
        String bmDescription = "testBmDescription";
        ProbeInformation probeInfo = new ProbeInformation("testTargetName", "testTargetType", "testBmClass", "testBmName", bmDescription, "testProbeName", 1, "testXmlRpcUrl");
        String returnedBmDescription = probeInfo.getBmDescription();

        assertEquals(bmDescription, returnedBmDescription);
    }

    @Test
    public void testGetPrecision()
    {
        int precision = 1;
        ProbeInformation probeInfo = new ProbeInformation("testTargetName", "testTargetType", "testBmClass", "testBmName", "testBmDescription", "testProbeName", precision, "testXmlRpcUrl");
        int returnedPrecision = probeInfo.getPrecision();

        assertEquals(precision, returnedPrecision);
    }

    @Test
    public void testGetMeasureURI()
    {
        String targetType = "testTargetType";
        String targetName = "testTargetName";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, "testBmDescription", "testProbeName", 1, "testXmlRpcUrl");
        String returnedMeasureURI = probeInfo.getMeasureURI();

        String expectedMeasureURI = Const.createMeasureURI(targetType, targetName, bmClass, bmName);
        assertEquals(expectedMeasureURI, returnedMeasureURI);
    }

    @Test
    public void testGetXmlRpcUrl()
    {
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInfo = new ProbeInformation("testTargetName", "testTargetType", "testBmClass", "testBmName", "testBmDescription", "testProbeName", 1, xmlRpcUrl);
        String returnedXmlRpcUrl = probeInfo.getXmlRpcUrl();

        assertEquals(xmlRpcUrl, returnedXmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullTargetName()
    {
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInfo = new ProbeInformation(null, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullTargetType()
    {
        String targetName = "testTargetName";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInfo = new ProbeInformation(targetName, null, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullBmClass()
    {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, null, bmName, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullBmName()
    {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, null, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullBmDescription()
    {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String probeName = "testProbeName";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, null, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullProbeName()
    {
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
    public void testConstructorWithEmptyTargetName()
    {
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInfo = new ProbeInformation("", targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyTargetType()
    {
        String targetName = "testTargetName";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInfo = new ProbeInformation(targetName, "", bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyBmClass()
    {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, "", bmName, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyBmName()
    {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, "", bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyBmDescription()
    {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String probeName = "testProbeName";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, "", probeName, precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyProbeName()
    {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, "", precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativePrecision()
    {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = -1;
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
    }
}