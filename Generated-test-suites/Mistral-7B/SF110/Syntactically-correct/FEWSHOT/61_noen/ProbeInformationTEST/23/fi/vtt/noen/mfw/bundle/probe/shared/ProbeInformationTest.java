package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testProbeInformationCreation() {
        String targetName = "TargetName";
        String targetType = "Text/Plain";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeName = "ProbeName1";
        int precision = 2;
        String xmlRpcUrl = "http://example.com/xmlrpc";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(targetName, probeInfo.getTargetName());
        assertEquals(targetType, probeInfo.getTargetType());
        assertEquals(bmClass, probeInfo.getBmClass());
        assertEquals(bmName, probeInfo.getBmName());
        assertEquals(bmDescription, probeInfo.getBmDescription());
        assertEquals(probeName, probeInfo.getProbeName());
        assertEquals(precision, probeInfo.getPrecision());
        assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());
    }

    @Test
    public void testGetters() {
        String targetName = "TargetName";
        String targetType = "Text/Plain";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeName = "ProbeName1";
        int precision = 2;
        String xmlRpcUrl = "http://example.com/xmlrpc";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertNotNull(probeInfo.getTargetName());
        assertNotNull(probeInfo.getTargetType());
        assertNotNull(probeInfo.getBmClass());
        assertNotNull(probeInfo.getBmName());
        assertNotNull(probeInfo.getBmDescription());
        assertNotNull(probeInfo.getProbeName());
        assertNotNull(probeInfo.getXmlRpcUrl());

        assertNull(probeInfo.getTargetName(), () -> new IllegalStateException("TargetName should be null"));
        assertNull(probeInfo.getTargetType(), () -> new IllegalStateException("TargetType should be null"));
        assertNull(probeInfo.getBmClass(), () -> new IllegalStateException("BMClass should be null"));
        assertNull(probeInfo.getBmName(), () -> new IllegalStateException("BMName should be null"));
        assertNull(probeInfo.getBmDescription(), () -> new IllegalStateException("BMDescription should be null"));
        assertNull(probeInfo.getProbeName(), () -> new IllegalStateException("ProbeName should be null"));
        assertNull(probeInfo.getXmlRpcUrl(), () -> new IllegalStateException("XMLRpcUrl should be null"));
    }

    @Test
    public void testToString() {
        String targetName = "TargetName";
        String targetType = "Text/Plain";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeName = "ProbeName1";
        int precision = 2;
        String xmlRpcUrl = "http://example.com/xmlrpc";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        String expected = "ProbeInformation{targetName='TargetName', targetType='Text/Plain', bmClass='BMClass1', bmName='BMName1', bmDescription='BMDescription1', probeDescription='ProbeName1', precision=2}";

        assertEquals(expected, probeInfo.toString());
    }

    @Test
    public void testEquals() {
        String targetName1 = "TargetName1";
        String targetName2 = "TargetName2";
        String targetType1 = "Text/Plain";
        String targetType2 = "application/octet-stream";
        String bmClass1 = "BMClass1";
        String bmClass2 = "BMClass2";
        String bmName1 = "BMName1";
        String bmName2 = "BMName2";
        String bmDescription1 = "BMDescription1";
        String bmDescription2 = "BMDescription2";
        String probeName1 = "ProbeName1";
        String probeName2 = "ProbeName2";
        int precision1 = 2;
        int precision2 = 3;
        String xmlRpcUrl1 = "http://example.com/xmlrpc";
        String xmlRpcUrl2 = "http://example.com/xmlrpc";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);

        ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        ProbeInformation probeInfo4 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl2);

        assertEquals(probeInfo1, probeInfo3);
        assertNotEquals(probeInfo1, probeInfo2);
        assertNotEquals(probeInfo1, null);
    }

    @Test
    public void testHashCode() {
        String targetName = "TargetName";
        String targetType = "Text/Plain";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeName = "ProbeName1";
        int precision = 2;

        int hashCode1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, null).hashCode();
        int hashCode2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, "http://example.com/xmlrpc").hashCode();

        assertEquals(hashCode1, hashCode2);
    }
}