package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testProbeInformationConstruction() {
        String targetName = "target1";
        String targetType = "type1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeDescription = "ProbeDescription1";
        int precision = 1;
        String xmlRpcUrl = "http://example.com/xmlrpc";

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
    public void testGetters() {
        String targetName = "target1";
        String targetType = "type1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeDescription = "ProbeDescription1";
        int precision = 1;
        String xmlRpcUrl = "http://example.com/xmlrpc";

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
        String targetType = "type1";
        String targetName = "target1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";

        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "BMClass1", "BMName1", null, null, 1, null);
        String measureURI = probeInfo.getMeasureURI();

        assertEquals(Const.createMeasureURI(targetType, targetName, bmClass, bmName), measureURI);
    }

    @Test
    public void testEquals() {
        String targetName1 = "target1";
        String targetName2 = "target2";
        String targetType1 = "type1";
        String targetType2 = "type2";
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
        String xmlRpcUrl1 = "http://example.com/xmlrpc";
        String xmlRpcUrl2 = "http://otherexample.com/xmlrpc";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeDescription2, precision2, xmlRpcUrl2);

        assertNotEquals(probeInfo1, probeInfo2);

        ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);

        assertEquals(probeInfo1, probeInfo3);

        ProbeInformation probeInfo4 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);

        assertEquals(probeInfo1, probeInfo4);

        ProbeInformation probeInfo5 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);

        ProbeInformation probeInfo6 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);

        assertEquals(probeInfo5, probeInfo6);

        ProbeInformation probeInfo7 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);

        ProbeInformation probeInfo8 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);

        assertEquals(probeInfo7, probeInfo8);
    }

    @Test
    public void testHashCode() {
        String targetName = "target1";
        String targetType = "type1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeDescription = "ProbeDescription1";
        int precision = 1;
        String xmlRpcUrl = "http://example.com/xmlrpc";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        int hashCode = probeInfo.hashCode();

        String targetName2 = "target2";
        String targetType2 = "type2";
        String bmClass2 = "BMClass2";
        String bmName2 = "BMName2";
        String bmDescription2 = "BMDescription2";
        String probeDescription2 = "ProbeDescription2";
        int precision2 = 2;
        String xmlRpcUrl2 = "http://example.com/xmlrpc2";

        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeDescription2, precision2, xmlRpcUrl2);

        int hashCode2 = probeInfo2.hashCode();

        assertEquals(hashCode, probeInfo.hashCode());
        assertNotEquals(hashCode, hashCode2);
    }

    @Test
    public void testToString() {
        String targetName = "target1";
        String targetType = "type1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeDescription = "ProbeDescription1";
        int precision = 1;
        String xmlRpcUrl = "http://example.com/xmlrpc";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        String expectedToString = "ProbeInformation{targetName='" + targetName + "', targetType='" + targetType + "', bmClass='" + bmClass + "', bmName='" + bmName + "', bmDescription='" + bmDescription + "', probeDescription='" + probeDescription + "', precision=1}";

        assertEquals(expectedToString, probeInfo.toString());
    }

    @Test
    public void testGettersNullValues() {
        String targetName = null;
        String targetType = null;
        String bmClass = null;
        String bmName = null;
        String bmDescription = null;
        String probeDescription = null;
        int precision = 0;
        String xmlRpcUrl = null;

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        assertNull(probeInfo.getTargetName());
        assertNull(probeInfo.getTargetType());
        assertNull(probeInfo.getBmClass());
        assertNull(probeInfo.getBmName());
        assertNull(probeInfo.getBmDescription());
        assertNull(probeInfo.getProbeName());
        assertEquals(0, probeInfo.getPrecision());
        assertNull(probeInfo.getXmlRpcUrl());
    }
}