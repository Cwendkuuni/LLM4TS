package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;

import org.junit.Test;

import fi.vtt.noen.mfw.bundle.common.Const;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @Test
    public void testProbeInformationConstructor() {
        String targetName = "TargetName";
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";

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
    public void testGetTargetType() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        assertEquals("TargetType", probeInformation.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        assertEquals("BMName", probeInformation.getBmName());
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        assertEquals("ProbeDescription", probeInformation.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        assertEquals("TargetName", probeInformation.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        assertEquals("BMClass", probeInformation.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        assertEquals("BMDescription", probeInformation.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        assertEquals(1, probeInformation.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        String measureURI = Const.createMeasureURI(probeInformation.getTargetType(), probeInformation.getTargetName(), probeInformation.getBmClass(), probeInformation.getBmName());
        assertEquals(measureURI, probeInformation.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        assertEquals("XMLRpcUrl", probeInformation.getXmlRpcUrl());
    }

    @Test
    public void testToString() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        String expectedString = "ProbeInformation{targetName='TargetName', targetType='TargetType', bmClass='BMClass', bmName='BMName', bmDescription='BMDescription', probeDescription='ProbeDescription', precision=1}";
        assertEquals(expectedString, probeInformation.toString());
    }

    @Test
    public void testEquals() {
        ProbeInformation probeInformation1 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XMLRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("TargetName2", "TargetType2", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XMLRpcUrl1");
        ProbeInformation probeInformation3 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription2", 1, "XMLRpcUrl1");
        ProbeInformation probeInformation4 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 2, "XMLRpcUrl1");
        ProbeInformation probeInformation5 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName2", "BMDescription1", "ProbeDescription1", 1, "XMLRpcUrl1");

        assertTrue(probeInformation1.equals(probeInformation1));
        assertTrue(probeInformation1.equals(probeInformation2));
        assertFalse(probeInformation1.equals(probeInformation3));
        assertFalse(probeInformation1.equals(probeInformation4));
        assertFalse(probeInformation1.equals(probeInformation5));
    }

    @Test
    public void testHashCode() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        int hashCode1 = probeInformation.hashCode();
        ProbeInformation probeInformation2 = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        int hashCode2 = probeInformation2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }
}