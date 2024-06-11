package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;

import fi.vtt.noen.mfw.bundle.common.Const;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

import org.junit.Test;

public class ProbeInformationTest
{
    @Test
    public void testProbeInformationConstructor()
    {
        String targetName = "TestTarget";
        String targetType = "TestType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeDescription = "TestProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "http://test.com/xmlrpc";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        assertEquals(targetName, probeInformation.getTargetName());
        assertEquals(targetType, probeInformation.getTargetType());
        assertEquals(bmClass, probeInformation.getBmClass());
        assertEquals(bmName, probeInformation.getBmName());
        assertEquals(bmDescription, probeInformation.getBmDescription());
        assertEquals(probeDescription, probeInformation.getProbeName());
        assertEquals(precision, probeInformation.getPrecision());
        assertEquals(xmlRpcUrl, probeInformation.getXmlRpcUrl());
    }

    @Test
    public void testGetTargetType()
    {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");

        assertEquals("TargetType", probeInformation.getTargetType());
    }

    @Test
    public void testGetBmName()
    {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");

        assertEquals("BMName", probeInformation.getBmName());
    }

    @Test
    public void testGetProbeName()
    {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");

        assertEquals("ProbeDescription", probeInformation.getProbeName());
    }

    @Test
    public void testGetTargetName()
    {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");

        assertEquals("TargetName", probeInformation.getTargetName());
    }

    @Test
    public void testGetBmClass()
    {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");

        assertEquals("BMClass", probeInformation.getBmClass());
    }

    @Test
    public void testGetBmDescription()
    {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");

        assertEquals("BMDescription", probeInformation.getBmDescription());
    }

    @Test
    public void testGetPrecision()
    {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");

        assertEquals(1, probeInformation.getPrecision());
    }

    @Test
    public void testGetMeasureURI()
    {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");

        assertEquals(Const.createMeasureURI("TargetType", "TargetName", "BMClass", "BMName"), probeInformation.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl()
    {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");

        assertEquals("XMLRpcUrl", probeInformation.getXmlRpcUrl());
    }

    @Test
    public void testToString()
    {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");

        String expected = "ProbeInformation{targetName='TargetName', targetType='TargetType', bmClass='BMClass', bmName='BMName', bmDescription='BMDescription', probeDescription='ProbeDescription', precision=1}";
        String actual = probeInformation.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testEquals()
    {
        ProbeInformation probeInformation1 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XMLRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("TargetName2", "TargetType2", "BMClass2", "BMName2", "BMDescription2", "ProbeDescription2", 1, "XMLRpcUrl2");

        assertNotEquals(probeInformation1, probeInformation2);

        ProbeInformation probeInformation3 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XMLRpcUrl1");

        assertEquals(probeInformation1, probeInformation3);
    }

    @Test
    public void testHashCode()
    {
        ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XMLRpcUrl1");

        int hashCode1 = probeInformation.hashCode();

        ProbeInformation probeInformation2 = new ProbeInformation("TargetName2", "TargetType2", "BMClass2", "BMName2", "BMDescription2", "ProbeDescription2", 1, "XMLRpcUrl2");

        assertNotEquals(hashCode1, probeInformation2.hashCode());

        ProbeInformation probeInformation3 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XMLRpcUrl1");

        assertEquals(hashCode1, probeInformation3.hashCode());
    }
}