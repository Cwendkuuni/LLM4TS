package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;

import org.junit.Test;

import fi.vtt.noen.mfw.bundle.common.Const;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @Test
    public void testConstructor() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeDescription = "probeDescription";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        assertEquals(targetName, probeInfo.getTargetName());
        assertEquals(targetType, probeInfo.getTargetType());
        assertEquals(bmClass, probeInfo.getBmClass());
        assertEquals(bmName, probeInfo.getBmName());
        assertEquals(bmDescription, probeInfo.getBmDescription());
        assertEquals(probeDescription, probeInfo.getProbeName());
        assertEquals(precision, probeInfo.getPrecision());
        assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());
        assertEquals(Const.createMeasureURI(targetType, targetName, bmClass, bmName), probeInfo.getMeasureURI());
    }

    @Test
    public void testGetters() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeDescription = "probeDescription";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl";

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
        String probeDescription1 = "probeDescription1";
        String probeDescription2 = "probeDescription2";
        int precision1 = 1;
        int precision2 = 2;
        String xmlRpcUrl1 = "xmlRpcUrl1";
        String xmlRpcUrl2 = "xmlRpcUrl2";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeDescription2, precision2, xmlRpcUrl2);

        ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);

        assertTrue(probeInfo1.equals(probeInfo3));

        assertFalse(probeInfo1.equals(probeInfo2));
    }

    @Test
    public void testHashCode() {
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
        String probeDescription1 = "probeDescription1";
        String probeDescription2 = "probeDescription2";
        int precision1 = 1;
        int precision2 = 2;
        String xmlRpcUrl1 = "xmlRpcUrl1";
        String xmlRpcUrl2 = "xmlRpcUrl2";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeDescription2, precision2, xmlRpcUrl2);

        ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);

        int hashCode1 = probeInfo1.hashCode();
        int hashCode2 = probeInfo2.hashCode();
        int hashCode3 = probeInfo3.hashCode();

        assertEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode2);
    }
}