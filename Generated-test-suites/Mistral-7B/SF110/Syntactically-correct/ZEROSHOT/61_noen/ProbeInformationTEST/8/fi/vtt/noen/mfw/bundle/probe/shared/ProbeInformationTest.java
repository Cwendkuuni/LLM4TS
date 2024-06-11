package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;

import org.junit.Test;

import fi.vtt.noen.mfw.bundle.common.Const;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @Test
    public void testProbeInformationConstruction() {
        String targetName = "TargetName";
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "http://localhost:8080/xmlrpc";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        assertEquals(targetName, probeInfo.getTargetName());
        assertEquals(targetType, probeInfo.getTargetType());
        assertEquals(bmClass, probeInfo.getBmClass());
        assertEquals(bmName, probeInfo.getBmName());
        assertEquals(bmDescription, probeInfo.getBmDescription());
        assertEquals(probeDescription, probeInfo.getProbeName());
        assertEquals(precision, probeInfo.getPrecision());
        assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());
    }

    @Test
    public void testGetMeasureURI() {
        String targetType = "TargetType";
        String targetName = "TargetName";
        String bmClass = "BMClass";
        String bmName = "BMName";

        ProbeInformation probeInfo = new ProbeInformation("TargetName", targetType, bmClass, bmName, "BMDescription", "ProbeDescription", 1, "http://localhost:8080/xmlrpc");

        String measureURI = probeInfo.getMeasureURI();
        String expectedMeasureURI = Const.createMeasureURI(targetType, targetName, bmClass, bmName);

        assertEquals(expectedMeasureURI, measureURI);
    }

    @Test
    public void testEquals() {
        String targetName1 = "TargetName1";
        String targetType1 = "TargetType1";
        String bmClass1 = "BMClass1";
        String bmName1 = "BMName1";
        String bmDescription1 = "BMDescription1";
        String probeDescription1 = "ProbeDescription1";
        int precision1 = 1;
        String xmlRpcUrl1 = "http://localhost:8080/xmlrpc";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);

        String targetName2 = "TargetName2";
        String targetType2 = targetType1;
        String bmClass2 = bmClass1;
        String bmName2 = bmName1;
        String bmDescription2 = bmDescription1;
        String probeDescription2 = probeDescription1;
        int precision2 = precision1;
        String xmlRpcUrl2 = xmlRpcUrl1;

        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeDescription2, precision2, xmlRpcUrl2);

        boolean equalsResult1 = probeInfo1.equals(probeInfo1);
        assertTrue(equalsResult1);

        boolean equalsResult2 = probeInfo1.equals(probeInfo2);
        assertFalse(equalsResult2);

        boolean equalsResult3 = probeInfo2.equals(probeInfo1);
        assertFalse(equalsResult3);

        boolean equalsResult4 = probeInfo1.equals(null);
        assertFalse(equalsResult4);
    }

    @Test
    public void testHashCode() {
        String targetName1 = "TargetName1";
        String targetType1 = "TargetType1";
        String bmClass1 = "BMClass1";
        String bmName1 = "BMName1";
        String bmDescription1 = "BMDescription1";
        String probeDescription1 = "ProbeDescription1";
        int precision1 = 1;
        String xmlRpcUrl1 = "http://localhost:8080/xmlrpc";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);

        String targetName2 = "TargetName2";
        String targetType2 = targetType1;
        String bmClass2 = bmClass1;
        String bmName2 = bmName1;
        String bmDescription2 = bmDescription1;
        String probeDescription2 = probeDescription1;
        int precision2 = precision1;
        String xmlRpcUrl2 = xmlRpcUrl1;

        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeDescription2, precision2, xmlRpcUrl2);

        int hashCodeResult1 = probeInfo1.hashCode();
        int hashCodeResult2 = probeInfo2.hashCode();

        assertNotSame(hashCodeResult1, hashCodeResult2);
    }

    @Test
    public void testToString() {
        String targetName = "TargetName";
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "http://localhost:8080/xmlrpc";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        String toStringResult = probeInfo.toString();

        String expectedToStringResult = "ProbeInformation{targetName='" + targetName + "', targetType='" + targetType + "', bmClass='" + bmClass + "', bmName='" + bmName + "', bmDescription='" + bmDescription + "', probeDescription='" + probeDescription + "', precision=" + precision + "}";

        assertEquals(expectedToStringResult, toStringResult);
    }

}