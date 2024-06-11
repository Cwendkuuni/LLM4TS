package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;

import java.util.Objects;

import org.junit.Test;

import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @Test
    public void testConstructor() {
        String targetName = "TargetName";
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "XmlRpcUrl";

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
        ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XmlRpcUrl");

        assertEquals("TargetType", probeInfo.getTargetType());
        assertEquals("BMName", probeInfo.getBmName());
        assertEquals("ProbeDescription", probeInfo.getProbeName());
        assertEquals("TargetName", probeInfo.getTargetName());
        assertEquals("BMClass", probeInfo.getBmClass());
        assertEquals("BMDescription", probeInfo.getBmDescription());
        assertEquals(1, probeInfo.getPrecision());
        assertEquals("XmlRpcUrl", probeInfo.getXmlRpcUrl());
    }

    @Test
    public void testToString() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XmlRpcUrl");

        String expected = "ProbeInformation{targetName='TargetName', targetType='TargetType', bmClass='BMClass', bmName='BMName', bmDescription='BMDescription', probeDescription='ProbeDescription', precision=1}";
        assertEquals(expected, probeInfo.toString());
    }

    @Test
    public void testEquals() {
        ProbeInformation probeInfo1 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        ProbeInformation probeInfo2 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");

        ProbeInformation probeInfo3 = new ProbeInformation("TargetName2", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        ProbeInformation probeInfo4 = new ProbeInformation("TargetName1", "TargetType2", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");

        ProbeInformation probeInfo5 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription2", 1, "XmlRpcUrl1");

        assertTrue(probeInfo1.equals(probeInfo2));
        assertFalse(probeInfo1.equals(probeInfo3));
        assertFalse(probeInfo1.equals(probeInfo4));
        assertFalse(probeInfo1.equals(probeInfo5));
        assertFalse(probeInfo1.equals(null));
        assertFalse(probeInfo1.equals(""));
        assertFalse(probeInfo1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        ProbeInformation probeInfo1 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        ProbeInformation probeInfo2 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");

        int expected = 16777619 + (Objects.hashCode(probeInfo1.getTargetName()) * 31) + (Objects.hashCode(probeInfo1.getTargetType()) * 31)
                + (Objects.hashCode(probeInfo1.getBmClass()) * 31) + (Objects.hashCode(probeInfo1.getBmName()) * 31)
                + (Objects.hashCode(probeInfo1.getBmDescription()) * 31) + (Objects.hashCode(probeInfo1.getProbeName()) * 31)
                + probeInfo1.getPrecision();

        int hashCode = probeInfo1.hashCode();
        assertEquals(expected, hashCode);
        assertEquals(hashCode, probeInfo2.hashCode());
    }
}