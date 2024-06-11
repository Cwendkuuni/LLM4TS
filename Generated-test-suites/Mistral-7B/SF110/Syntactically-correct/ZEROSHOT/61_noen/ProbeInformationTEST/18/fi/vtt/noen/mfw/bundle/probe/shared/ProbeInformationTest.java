package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fi.vtt.noen.mfw.bundle.common.Const;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest
{
    @Test
    public void testProbeInformationConstruction()
    {
        String targetName = "TargetName";
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "XmlRpcUrl";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        assertNotNull(probeInformation);
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
    public void testGetters()
    {
        String targetName = "TargetName";
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "XmlRpcUrl";

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
    public void testGetMeasureURI()
    {
        String targetType = "TargetType";
        String targetName = "TargetName";
        String bmClass = "BMClass";
        String bmName = "BMName";

        ProbeInformation probeInformation = new ProbeInformation("TargetName", targetType, bmClass, bmName, null, null, 1, null);

        String expectedMeasureURI = Const.createMeasureURI(targetType, targetName, bmClass, bmName);
        String actualMeasureURI = probeInformation.getMeasureURI();

        assertEquals(expectedMeasureURI, actualMeasureURI);
    }

    @Test
    public void testToString()
    {
        String targetName = "TargetName";
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "XmlRpcUrl";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        String expectedToString = "ProbeInformation{targetName='" + targetName + "', targetType='" + targetType + "', bmClass='" + bmClass + "', bmName='" + bmName + "', bmDescription='" + bmDescription + "', probeDescription='" + probeDescription + "', precision=1}";
        String actualToString = probeInformation.toString();

        assertEquals(expectedToString, actualToString);
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
        String xmlRpcUrl1 = "XmlRpcUrl1";
        String xmlRpcUrl2 = "XmlRpcUrl2";

        ProbeInformation probeInformation1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        ProbeInformation probeInformation2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeDescription2, precision2, xmlRpcUrl2);

        assertNotSame(probeInformation1, probeInformation2);

        ProbeInformation probeInformation3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        assertTrue(probeInformation1.equals(probeInformation3));

        ProbeInformation probeInformation4 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        assertTrue(probeInformation1.equals(probeInformation4));

        ProbeInformation probeInformation5 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        probeInformation5.setPrecision(precision2);
        assertNotSame(probeInformation1, probeInformation5);

        ProbeInformation probeInformation6 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        ProbeInformation probeInformation7 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl2);
        assertNotSame(probeInformation1, probeInformation7);

        ProbeInformation probeInformation8 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        ProbeInformation probeInformation9 = new ProbeInformation(targetName2, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        assertNotSame(probeInformation1, probeInformation9);
    }

    @Test
    public void testHashCode()
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
        String xmlRpcUrl1 = "XmlRpcUrl1";
        String xmlRpcUrl2 = "XmlRpcUrl2";

        ProbeInformation probeInformation1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        ProbeInformation probeInformation2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeDescription2, precision2, xmlRpcUrl2);

        int hashCode1 = probeInformation1.hashCode();
        int hashCode2 = probeInformation2.hashCode();

        assertNotSame(hashCode1, hashCode2);

        ProbeInformation probeInformation3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        int hashCode3 = probeInformation3.hashCode();

        assertEquals(hashCode1, hashCode3);

        ProbeInformation probeInformation4 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        int hashCode4 = probeInformation4.hashCode();

        assertEquals(hashCode1, hashCode4);

        ProbeInformation probeInformation5 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        probeInformation5.setPrecision(precision2);
        int hashCode5 = probeInformation5.hashCode();

        assertNotEqual(hashCode1, hashCode5);

        ProbeInformation probeInformation6 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        ProbeInformation probeInformation7 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl2);
        int hashCode6 = probeInformation6.hashCode();

        assertNotEqual(hashCode1, hashCode6);

        ProbeInformation probeInformation8 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        ProbeInformation probeInformation9 = new ProbeInformation(targetName2, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        int hashCode7 = probeInformation8.hashCode();

        assertNotEqual(hashCode1, hashCode7);
    }

    private void assertNotEqual(int expectedHashCode, int actualHashCode)
    {
        int difference = Math.abs(expectedHashCode - actualHashCode);

        assertTrue("Expected hashcode: " + expectedHashCode + ", but was: " + actualHashCode + ". Difference: " + difference, difference > 0);
    }
}