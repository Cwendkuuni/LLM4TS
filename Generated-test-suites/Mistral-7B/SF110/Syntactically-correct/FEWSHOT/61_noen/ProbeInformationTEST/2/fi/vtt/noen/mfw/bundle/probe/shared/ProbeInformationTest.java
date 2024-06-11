package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testConstructor() {
        String targetName = "target1";
        String targetType = "type1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeName = "ProbeName1";
        int precision = 1;
        String xmlRpcUrl = "http://example.com/xmlrpc";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(targetName, probeInfo1.getTargetName());
        assertEquals(targetType, probeInfo1.getTargetType());
        assertEquals(bmClass, probeInfo1.getBmClass());
        assertEquals(bmName, probeInfo1.getBmName());
        assertEquals(bmDescription, probeInfo1.getBmDescription());
        assertEquals(probeName, probeInfo1.getProbeName());
        assertEquals(precision, probeInfo1.getPrecision());
        assertEquals(xmlRpcUrl, probeInfo1.getXmlRpcUrl());

        String measureURI = Const.createMeasureURI(targetType, targetName, bmClass, bmName);
        assertEquals(measureURI, probeInfo1.getMeasureURI());
    }

    @Test
    public void testGetters() {
        String targetName = "target1";
        String targetType = "type1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeName = "ProbeName1";
        int precision = 1;
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
        String probeName1 = "ProbeName1";
        String probeName2 = "ProbeName2";
        int precision1 = 1;
        int precision2 = 2;
        String xmlRpcUrl1 = "http://example.com/xmlrpc1";
        String xmlRpcUrl2 = "http://example.com/xmlrpc2";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);
        ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        assertNotSame(probeInfo1, probeInfo2);
        assertSame(probeInfo1, probeInfo3);

        ProbeInformation probeInfo4 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        probeInfo1.setTargetName("target3");
        assertNotSame(probeInfo1, probeInfo4);

        probeInfo1.setTargetType(targetType2);
        assertNotSame(probeInfo1, probeInfo4);

        probeInfo1.setBmClass(bmClass2);
        assertNotSame(probeInfo1, probeInfo4);

        probeInfo1.setBmName(bmName2);
        assertNotSame(probeInfo1, probeInfo4);

        probeInfo1.setBmDescription(bmDescription2);
        assertNotSame(probeInfo1, probeInfo4);

        probeInfo1.setProbeName(probeName2);
        assertNotSame(probeInfo1, probeInfo4);

        probeInfo1.setPrecision(precision2);
        assertNotSame(probeInfo1, probeInfo4);

        probeInfo1.setXmlRpcUrl(xmlRpcUrl2);
        assertNotSame(probeInfo1, probeInfo4);
    }

    @Test
    public void testHashCode() {
        String targetName = "target1";
        String targetType = "type1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeName = "ProbeName1";
        int precision = 1;

        ProbeInformation probeInfo1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, null);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, null);

        assertEquals(probeInfo1.hashCode(), probeInfo2.hashCode());

        probeInfo1.setTargetName("target2");
        int hashCode1 = probeInfo1.hashCode();
        probeInfo1.setTargetName(targetName);
        assertEquals(hashCode1, probeInfo1.hashCode());

        probeInfo1.setTargetType("type2");
        int hashCode2 = probeInfo1.hashCode();
        probeInfo1.setTargetType(targetType);
        assertEquals(hashCode2, probeInfo1.hashCode());

        probeInfo1.setBmClass("BMClass2");
        int hashCode3 = probeInfo1.hashCode();
        probeInfo1.setBmClass(bmClass);
        assertEquals(hashCode3, probeInfo1.hashCode());

        probeInfo1.setBmName("BMName2");
        int hashCode4 = probeInfo1.hashCode();
        probeInfo1.setBmName(bmName);
        assertEquals(hashCode4, probeInfo1.hashCode());

        probeInfo1.setBmDescription("BMDescription2");
        int hashCode5 = probeInfo1.hashCode();
        probeInfo1.setBmDescription(bmDescription);
        assertEquals(hashCode5, probeInfo1.hashCode());

        probeInfo1.setProbeName("ProbeName2");
        int hashCode6 = probeInfo1.hashCode();
        probeInfo1.setProbeName(probeName);
        assertEquals(hashCode6, probeInfo1.hashCode());

        probeInfo1.setPrecision(2);
        int hashCode7 = probeInfo1.hashCode();
        probeInfo1.setPrecision(precision);
        assertEquals(hashCode7, probeInfo1.hashCode());
    }

    @Test
    public void testToString() {
        String targetName = "target1";
        String targetType = "type1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeName = "ProbeName1";
        int precision = 1;

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, null);

        String expected = "ProbeInformation{targetName='" + targetName + "', targetType='" + targetType + "', bmClass='" + bmClass + "', bmName='" + bmName + "', bmDescription='" + bmDescription + "', probeDescription='" + probeName + "', precision=1}";
        assertEquals(expected, probeInfo.toString());
    }
}