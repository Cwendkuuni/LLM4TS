package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;

import fi.vtt.noen.mfw.bundle.common.Const;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

import org.junit.Test;

public class ProbeInformationTest
{
    @Test
    public void testProbeInformationConstruction()
    {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeDescription = "testProbeDescription";
        int precision = 4;
        String xmlRpcUrl = "testXmlRpcUrl";

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
        String targetType = "testTargetType";
        String targetName = "testTargetName";
        String bmClass = "testBmClass";
        String bmName = "testBmName";

        ProbeInformation probeInformation = new ProbeInformation("testTargetName", targetType, bmClass, bmName, null, null, 4, null);

        String measureURI = probeInformation.getMeasureURI();

        String expectedMeasureURI = Const.createMeasureURI(targetType, targetName, bmClass, bmName);

        assertEquals(expectedMeasureURI, measureURI);
    }

    @Test
    public void testEquals()
    {
        String targetName1 = "testTargetName1";
        String targetName2 = "testTargetName2";
        String targetType1 = "testTargetType1";
        String targetType2 = "testTargetType2";
        String bmClass1 = "testBmClass1";
        String bmClass2 = "testBmClass2";
        String bmName1 = "testBmName1";
        String bmName2 = "testBmName2";
        String bmDescription1 = "testBmDescription1";
        String bmDescription2 = "testBmDescription2";
        String probeName1 = "testProbeName1";
        String probeName2 = "testProbeName2";
        int precision1 = 4;
        int precision2 = 4;
        String xmlRpcUrl1 = "testXmlRpcUrl1";
        String xmlRpcUrl2 = "testXmlRpcUrl2";

        ProbeInformation probeInformation1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        ProbeInformation probeInformation2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);

        ProbeInformation probeInformation3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        assertTrue(probeInformation1.equals(probeInformation3));

        ProbeInformation probeInformation4 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName2, precision1, xmlRpcUrl1);

        assertFalse(probeInformation1.equals(probeInformation4));

        ProbeInformation probeInformation5 = new ProbeInformation(targetName1, targetType2, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl2);

        assertFalse(probeInformation1.equals(probeInformation5));

        ProbeInformation probeInformation6 = new ProbeInformation(targetName2, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        assertFalse(probeInformation1.equals(probeInformation6));

        ProbeInformation probeInformation7 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision2, xmlRpcUrl1);

        assertFalse(probeInformation1.equals(probeInformation7));

        ProbeInformation probeInformation8 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, null);

        assertFalse(probeInformation1.equals(probeInformation8));

        ProbeInformation probeInformation9 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, null, probeName1, precision1, xmlRpcUrl1);

        assertFalse(probeInformation1.equals(probeInformation9));

        ProbeInformation probeInformation10 = new ProbeInformation(targetName1, targetType1, bmClass1, null, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        assertFalse(probeInformation1.equals(probeInformation10));

        ProbeInformation probeInformation11 = new ProbeInformation(targetName1, targetType1, null, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        assertFalse(probeInformation1.equals(probeInformation11));

        ProbeInformation probeInformation12 = new ProbeInformation(targetName1, null, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        assertFalse(probeInformation1.equals(probeInformation12));

        ProbeInformation probeInformation13 = new ProbeInformation(null, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        assertFalse(probeInformation1.equals(probeInformation13));

        ProbeInformation probeInformation14 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, null);

        assertFalse(probeInformation1.equals(probeInformation14));
    }

    @Test
    public void testHashCode()
    {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = 4;
        String xmlRpcUrl = "testXmlRpcUrl";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        int hashCode = probeInformation.hashCode();

        String targetName2 = "testTargetName2";
        String targetType2 = "testTargetType2";
        String bmClass2 = "testBmClass2";
        String bmName2 = "testBmName2";
        String bmDescription2 = "testBmDescription2";
        String probeName2 = "testProbeName2";
        int precision2 = 4;
        String xmlRpcUrl2 = "testXmlRpcUrl2";

        ProbeInformation probeInformation2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);

        int expectedHashCode = (targetName != null ? targetName.hashCode() : 0) * 31 +
                              (targetType != null ? targetType.hashCode() : 0) * 31 +
                              (bmClass != null ? bmClass.hashCode() : 0) * 31 +
                              (bmName != null ? bmName.hashCode() : 0) * 31 +
                              (bmDescription != null ? bmDescription.hashCode() : 0) * 31 +
                              (probeName != null ? probeName.hashCode() : 0) * 31 +
                              precision;

        int hashCode2 = probeInformation2.hashCode();

        assertEquals(hashCode, expectedHashCode);
        assertEquals(hashCode2, expectedHashCode);
    }

    @Test
    public void testToString()
    {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = 4;
        String xmlRpcUrl = "testXmlRpcUrl";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        String toString = probeInformation.toString();

        String expectedToString = "ProbeInformation{targetName='" + targetName + "', targetType='" + targetType + "', bmClass='" + bmClass + "', bmName='" + bmName + "', bmDescription='" + bmDescription + "', probeDescription='" + probeName + "', precision=" + precision + "}";

        assertEquals(expectedToString, toString);
    }
}