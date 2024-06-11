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
        int precision = 4;
        String xmlRpcUrl = "http://example.com/xmlrpc";

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
    public void testGetMeasureURI() {
        String targetType = "TargetType";
        String targetName = "TargetName";
        String bmClass = "BMClass";
        String bmName = "BMName";

        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", null, null, 4, null);

        String measureURI = probeInformation.getMeasureURI();

        String expectedMeasureURI = Const.createMeasureURI(targetType, targetName, bmClass, bmName);

        assertEquals(expectedMeasureURI, measureURI);
    }

    @Test
    public void testEquals() {
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
        String probeName1 = "ProbeName1";
        String probeName2 = "ProbeName2";
        int precision1 = 4;
        int precision2 = 5;
        String xmlRpcUrl1 = "http://example1.com/xmlrpc";
        String xmlRpcUrl2 = "http://example2.com/xmlrpc";

        ProbeInformation probeInformation1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        ProbeInformation probeInformation2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);
        ProbeInformation probeInformation3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        assertTrue(probeInformation1.equals(probeInformation3));
        assertFalse(probeInformation1.equals(probeInformation2));
    }

    @Test
    public void testHashCode() {
        String targetName1 = "TargetName1";
        String targetType1 = "TargetType1";
        String bmClass1 = "BMClass1";
        String bmName1 = "BMName1";
        String bmDescription1 = "BMDescription1";
        String probeName1 = "ProbeName1";
        int precision1 = 4;
        String xmlRpcUrl1 = "http://example1.com/xmlrpc";

        ProbeInformation probeInformation1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        int hashCode1 = probeInformation1.hashCode();

        String targetName2 = "TargetName2";
        String targetType2 = "TargetType2";
        String bmClass2 = "BMClass2";
        String bmName2 = "BMName2";
        String bmDescription2 = "BMDescription2";
        String probeName2 = "ProbeName2";
        int precision2 = 5;
        String xmlRpcUrl2 = "http://example2.com/xmlrpc";

        ProbeInformation probeInformation2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);

        String targetName3 = targetName1;
        String targetType3 = targetType1;
        String bmClass3 = bmClass1;
        String bmName3 = bmName1;
        String bmDescription3 = bmDescription1;
        String probeName3 = probeName1;
        int precision3 = precision1;
        String xmlRpcUrl3 = xmlRpcUrl1;

        ProbeInformation probeInformation3 = new ProbeInformation(targetName3, targetType3, bmClass3, bmName3, bmDescription3, probeName3, precision3, xmlRpcUrl3);

        int hashCode2 = probeInformation2.hashCode();
        int hashCode3 = probeInformation3.hashCode();

        assertEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        String targetName = "TargetName";
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 4;
        String xmlRpcUrl = "http://example.com/xmlrpc";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        String expectedString = "ProbeInformation{targetName='TargetName', targetType='TargetType', bmClass='BMClass', bmName='BMName', bmDescription='BMDescription', probeDescription='ProbeDescription', precision=4}";

        assertEquals(expectedString, probeInformation.toString());
    }
}