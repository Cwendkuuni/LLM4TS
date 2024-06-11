package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProbeInformationTest {
    @Test
    public void testConstructor() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeName = "probeName";
        int precision = 4;
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(targetName, probeInformation.getTargetName());
        assertEquals(targetType, probeInformation.getTargetType());
        assertEquals(bmClass, probeInformation.getBmClass());
        assertEquals(bmName, probeInformation.getBmName());
        assertEquals(bmDescription, probeInformation.getBmDescription());
        assertEquals(probeName, probeInformation.getProbeName());
        assertEquals(precision, probeInformation.getPrecision());
        assertEquals(xmlRpcUrl, probeInformation.getXmlRpcUrl());
    }

    @Test
    public void testGetters() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeName = "probeName";
        int precision = 4;
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(targetName, probeInformation.getTargetName());
        assertEquals(targetType, probeInformation.getTargetType());
        assertEquals(bmClass, probeInformation.getBmClass());
        assertEquals(bmName, probeInformation.getBmName());
        assertEquals(bmDescription, probeInformation.getBmDescription());
        assertEquals(probeName, probeInformation.getProbeName());
        assertEquals(precision, probeInformation.getPrecision());
        assertEquals(xmlRpcUrl, probeInformation.getXmlRpcUrl());
    }

    @Test
    public void testToString() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeName = "probeName";
        int precision = 4;
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        String expected = "ProbeInformation{targetName='" + targetName + "', targetType='" + targetType + "', bmClass='" + bmClass + "', bmName='" + bmName + "', bmDescription='" + bmDescription + "', probeDescription='" + probeName + "', precision=4}";
        assertEquals(expected, probeInformation.toString());
    }

    @Test
    public void testEquals() {
        String targetName1 = "targetName1";
        String targetName2 = "targetName2";
        String targetType1 = "targetType1";
        String targetType2 = "targetType2";
        String bmClass1 = "bmClass1";
        String bmClass2 = "bmClass2";
        String bmName1 = "bmName1";
        String bmName2 = "bmName2";
        String bmDescription1 = "bmDescription1";
        String bmDescription2 = "bmDescription2";
        String probeName1 = "probeName1";
        String probeName2 = "probeName2";
        int precision1 = 4;
        int precision2 = 5;
        String xmlRpcUrl1 = "xmlRpcUrl1";
        String xmlRpcUrl2 = "xmlRpcUrl2";

        ProbeInformation probeInformation1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        ProbeInformation probeInformation2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);

        assertFalse(probeInformation1.equals(probeInformation2));

        ProbeInformation probeInformation3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        assertTrue(probeInformation1.equals(probeInformation3));

        ProbeInformation probeInformation4 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        assertTrue(probeInformation1.equals(probeInformation4));
    }

    @Test
    public void testHashCode() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeName = "probeName";
        int precision = 4;
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        int expectedHashCode = targetName.hashCode() * 31 + targetType.hashCode() * 31 + bmClass.hashCode() * 31 + bmName.hashCode() * 31 + bmDescription.hashCode() * 31 + probeName.hashCode() * 31 + precision;
        assertEquals(expectedHashCode, probeInformation.hashCode());
    }

    @Test
    public void testGetMeasureURI() {
        String targetType = "targetType";
        String targetName = "targetName";
        String bmClass = "bmClass";
        String bmName = "bmName";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, "", "", "", 0, "");

        String expected = Const.createMeasureURI(targetType, targetName, bmClass, bmName);
        assertEquals(expected, probeInformation.getMeasureURI());
    }
}