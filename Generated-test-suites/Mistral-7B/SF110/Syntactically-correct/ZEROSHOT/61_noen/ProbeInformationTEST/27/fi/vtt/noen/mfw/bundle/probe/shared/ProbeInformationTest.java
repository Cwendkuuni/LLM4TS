package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;

import org.junit.Test;

import fi.vtt.noen.mfw.bundle.common.Const;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @Test
    public void testProbeInformationConstructor() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeDescription = "probeDescription";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl";

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
    public void testGetters() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeDescription = "probeDescription";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl";

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
    public void testMeasureURI() {
        String targetType = "targetType";
        String targetName = "targetName";
        String bmClass = "bmClass";
        String bmName = "bmName";

        ProbeInformation probeInformation = new ProbeInformation("targetName", targetType, bmClass, bmName, null, null, 1, null);

        String measureURI = probeInformation.getMeasureURI();

        assertEquals(Const.createMeasureURI(targetType, targetName, bmClass, bmName), measureURI);
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

        ProbeInformation probeInformation1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        ProbeInformation probeInformation2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeDescription2, precision2, xmlRpcUrl2);

        assertFalse(probeInformation1.equals(null));
        assertFalse(probeInformation1.equals(new Object()));
        assertFalse(probeInformation1.equals(probeInformation2));

        ProbeInformation probeInformation3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);

        assertTrue(probeInformation1.equals(probeInformation3));

        probeInformation1.setPrecision(precision2);

        assertFalse(probeInformation1.equals(probeInformation3));

        probeInformation3.setPrecision(precision2);

        assertTrue(probeInformation1.equals(probeInformation3));

        probeInformation1.setBmClass(bmClass2);

        assertFalse(probeInformation1.equals(probeInformation3));

        probeInformation3.setBmClass(bmClass2);

        assertTrue(probeInformation1.equals(probeInformation3));

        probeInformation1.setBmName(bmName2);

        assertFalse(probeInformation1.equals(probeInformation3));

        probeInformation3.setBmName(bmName2);

        assertTrue(probeInformation1.equals(probeInformation3));

        probeInformation1.setBmDescription(bmDescription2);

        assertFalse(probeInformation1.equals(probeInformation3));

        probeInformation3.setBmDescription(bmDescription2);

        assertTrue(probeInformation1.equals(probeInformation3));

        probeInformation1.setProbeName(probeDescription2);

        assertFalse(probeInformation1.equals(probeInformation3));

        probeInformation3.setProbeName(probeDescription2);

        assertTrue(probeInformation1.equals(probeInformation3));

        probeInformation1.setTargetName(targetName2);

        assertFalse(probeInformation1.equals(probeInformation3));

        probeInformation3.setTargetName(targetName2);

        assertTrue(probeInformation1.equals(probeInformation3));

        probeInformation1.setTargetType(targetType2);

        assertFalse(probeInformation1.equals(probeInformation3));

        probeInformation3.setTargetType(targetType2);

        assertTrue(probeInformation1.equals(probeInformation3));
    }

    @Test
    public void testHashCode() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeDescription = "probeDescription";
        int precision = 1;

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, null);

        int hashCode = probeInformation.hashCode();

        ProbeInformation probeInformation2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, null);

        assertEquals(hashCode, probeInformation2.hashCode());

        probeInformation.setTargetName("targetName2");

        assertEquals(probeInformation.hashCode(), probeInformation2.hashCode());

        probeInformation2.setTargetName("targetName2");

        assertEquals(hashCode, probeInformation.hashCode());

        probeInformation.setTargetType("targetType2");

        assertNotEquals(probeInformation.hashCode(), probeInformation2.hashCode());

        probeInformation2.setTargetType("targetType2");

        assertEquals(hashCode, probeInformation.hashCode());

        probeInformation.setBmClass("bmClass2");

        assertNotEquals(probeInformation.hashCode(), probeInformation2.hashCode());

        probeInformation2.setBmClass("bmClass2");

        assertEquals(hashCode, probeInformation.hashCode());

        probeInformation.setBmName("bmName2");

        assertNotEquals(probeInformation.hashCode(), probeInformation2.hashCode());

        probeInformation2.setBmName("bmName2");

        assertEquals(hashCode, probeInformation.hashCode());

        probeInformation.setBmDescription("bmDescription2");

        assertNotEquals(probeInformation.hashCode(), probeInformation2.hashCode());

        probeInformation2.setBmDescription("bmDescription2");

        assertEquals(hashCode, probeInformation.hashCode());

        probeInformation.setProbeName("probeName2");

        assertNotEquals(probeInformation.hashCode(), probeInformation2.hashCode());

        probeInformation2.setProbeName("probeName2");

        assertEquals(hashCode, probeInformation.hashCode());

        probeInformation.setPrecision(2);

        assertNotEquals(probeInformation.hashCode(), probeInformation2.hashCode());

        probeInformation2.setPrecision(2);

        assertEquals(hashCode, probeInformation.hashCode());
    }

    @Test
    public void testToString() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeDescription = "probeDescription";
        int precision = 1;

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, null);

        String expected = "ProbeInformation{targetName='" + targetName + "', targetType='" + targetType + "', bmClass='" + bmClass + "', bmName='" + bmName + "', bmDescription='" + bmDescription + "', probeDescription='" + probeDescription + "', precision=1}";

        assertEquals(expected, probeInformation.toString());
    }
}