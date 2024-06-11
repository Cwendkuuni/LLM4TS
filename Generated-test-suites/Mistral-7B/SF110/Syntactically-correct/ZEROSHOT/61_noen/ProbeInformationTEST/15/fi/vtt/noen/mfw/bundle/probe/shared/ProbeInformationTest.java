package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import fi.vtt.noen.mfw.bundle.common.Const;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest
{
    @Test
    public void testProbeInformationConstructor()
    {
        String targetName = "TargetName1";
        String targetType = "TargetType1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeDescription = "ProbeDescription1";
        int precision = 1;
        String xmlRpcUrl = "XMLRPCUrl1";

        ProbeInformation probeInformation1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        assertEquals(targetName, probeInformation1.getTargetName());
        assertEquals(targetType, probeInformation1.getTargetType());
        assertEquals(bmClass, probeInformation1.getBmClass());
        assertEquals(bmName, probeInformation1.getBmName());
        assertEquals(bmDescription, probeInformation1.getBmDescription());
        assertEquals(probeDescription, probeInformation1.getProbeName());
        assertEquals(precision, probeInformation1.getPrecision());
        assertEquals(xmlRpcUrl, probeInformation1.getXmlRpcUrl());
        assertEquals(Const.createMeasureURI(targetType, targetName, bmClass, bmName), probeInformation1.getMeasureURI());
    }

    @Test
    public void testGetters()
    {
        String targetName = "TargetName1";
        String targetType = "TargetType1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeDescription = "ProbeDescription1";
        int precision = 1;
        String xmlRpcUrl = "XMLRPCUrl1";

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
    public void testEquals()
    {
        String targetName1 = "TargetName1";
        String targetName2 = "TargetName2";
        String targetType1 = "TargetType1";
        String targetType2 = "TargetType2";
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
        String xmlRpcUrl1 = "XMLRPCUrl1";
        String xmlRpcUrl2 = "XMLRPCUrl2";

        ProbeInformation probeInformation1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        ProbeInformation probeInformation2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeDescription2, precision2, xmlRpcUrl2);
        ProbeInformation probeInformation3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);

        assertNotSame(probeInformation1, probeInformation2);
        assertSame(probeInformation1, probeInformation3);
        assertSame(probeInformation3, new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1));

        probeInformation1.setTargetName("TargetName3");
        assertNotSame(probeInformation1, probeInformation3);

        probeInformation1.setTargetType(targetType2);
        assertNotSame(probeInformation1, probeInformation3);

        probeInformation1.setBmClass(bmClass2);
        assertNotSame(probeInformation1, probeInformation3);

        probeInformation1.setBmName(bmName2);
        assertNotSame(probeInformation1, probeInformation3);

        probeInformation1.setBmDescription(bmDescription2);
        assertNotSame(probeInformation1, probeInformation3);

        probeInformation1.setProbeName(probeDescription2);
        assertNotSame(probeInformation1, probeInformation3);

        probeInformation1.setPrecision(precision2);
        assertNotSame(probeInformation1, probeInformation3);

        probeInformation1.setXmlRpcUrl(xmlRpcUrl2);
        assertNotSame(probeInformation1, probeInformation3);

        probeInformation1.setTargetName(null);
        assertNotSame(probeInformation1, probeInformation3);

        probeInformation1.setTargetType(null);
        assertSame(probeInformation1, probeInformation3);

        probeInformation1.setBmClass(null);
        assertSame(probeInformation1, probeInformation3);

        probeInformation1.setBmName(null);
        assertSame(probeInformation1, probeInformation3);

        probeInformation1.setBmDescription(null);
        assertSame(probeInformation1, probeInformation3);

        probeInformation1.setProbeName(null);
        assertSame(probeInformation1, probeInformation3);

        probeInformation1.setPrecision(0);
        assertSame(probeInformation1, probeInformation3);

        probeInformation1.setXmlRpcUrl(null);
        assertSame(probeInformation1, probeInformation3);
    }

    @Test
    public void testHashCode()
    {
        String targetName = "TargetName1";
        String targetType = "TargetType1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeDescription = "ProbeDescription1";
        int precision = 1;
        String xmlRpcUrl = "XMLRPCUrl1";

        ProbeInformation probeInformation1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        int hashCode1 = probeInformation1.hashCode();

        probeInformation1.setTargetName("TargetName2");
        int hashCode2 = probeInformation1.hashCode();

        assertEquals(hashCode1, probeInformation1.hashCode());
        assertNotEqual(hashCode1, hashCode2);

        ProbeInformation probeInformation2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
        assertEquals(hashCode1, probeInformation2.hashCode());

        probeInformation2.setTargetName(null);
        int hashCode3 = probeInformation2.hashCode();

        assertNotEqual(hashCode1, hashCode3);

        probeInformation2.setBmName(null);
        int hashCode4 = probeInformation2.hashCode();

        assertNotEqual(hashCode1, hashCode4);

        probeInformation2.setBmDescription(null);
        int hashCode5 = probeInformation2.hashCode();

        assertNotEqual(hashCode1, hashCode5);

        probeInformation2.setProbeName(null);
        int hashCode6 = probeInformation2.hashCode();

        assertNotEqual(hashCode1, hashCode6);

        probeInformation2.setPrecision(2);
        int hashCode7 = probeInformation2.hashCode();

        assertNotEqual(hashCode1, hashCode7);

        probeInformation2.setXmlRpcUrl(null);
        int hashCode8 = probeInformation2.hashCode();

        assertNotEqual(hashCode1, hashCode8);
    }

    @Test
    public void testToString()
    {
        String targetName = "TargetName1";
        String targetType = "TargetType1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeDescription = "ProbeDescription1";
        int precision = 1;
        String xmlRpcUrl = "XMLRPCUrl1";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        String expectedToString = "ProbeInformation{targetName='TargetName1', targetType='TargetType1', bmClass='BMClass1', bmName='BMName1', bmDescription='BMDescription1', probeDescription='ProbeDescription1', precision=1, xmlRpcUrl='XMLRPCUrl1'}";

        assertEquals(expectedToString, probeInformation.toString());
    }

    @Test
    public void testMeasureURI()
    {
        String targetName = "TargetName1";
        String targetType = "TargetType1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, null, null, null, 0, null);

        String expectedMeasureURI = Const.createMeasureURI(targetType, targetName, bmClass, bmName);

        assertEquals(expectedMeasureURI, probeInformation.getMeasureURI());
    }

    @Test
    public void testGettersWithNullValues()
    {
        ProbeInformation probeInformation = new ProbeInformation(null, null, null, null, null, null, 0, null);

        assertNull(probeInformation.getTargetName());
        assertNull(probeInformation.getTargetType());
        assertNull(probeInformation.getBmClass());
        assertNull(probeInformation.getBmName());
        assertNull(probeInformation.getBmDescription());
        assertNull(probeInformation.getProbeName());
        assertEquals(0, probeInformation.getPrecision());
        assertNull(probeInformation.getXmlRpcUrl());
    }

    private void assertNotEqual(int expected, int actual)
    {
        assertEquals(expected, actual);
        fail("Expected: " + expected + ", but got: " + actual);
    }
}