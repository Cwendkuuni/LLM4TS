package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.*;

import org.junit.Test;

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
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        assertEquals("targetType", probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        assertEquals("bmName", probeInfo.getBmName());
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        assertEquals("probeDescription", probeInfo.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        assertEquals("targetName", probeInfo.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        assertEquals("bmClass", probeInfo.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        assertEquals("bmDescription", probeInfo.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        assertEquals(1, probeInfo.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        assertEquals("http://example.com/measure?targetType=targetType&targetName=targetName&bmClass=bmClass&bmName=bmName", probeInfo.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        assertEquals("xmlRpcUrl", probeInfo.getXmlRpcUrl());
    }

    @Test
    public void testEquals() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName2", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInfo3 = new ProbeInformation("targetName1", "targetType2", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInfo4 = new ProbeInformation("targetName1", "targetType1", "bmClass2", "bmName2", "bmDescription2", "probeDescription2", 2, "xmlRpcUrl2");

        assertTrue(probeInfo1.equals(probeInfo1));
        assertTrue(probeInfo1.equals(probeInfo2));
        assertFalse(probeInfo1.equals(probeInfo3));
        assertFalse(probeInfo1.equals(probeInfo4));
        assertFalse(probeInfo1.equals(null));
        assertFalse(probeInfo1.equals(""));
        assertFalse(probeInfo1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName2", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInfo3 = new ProbeInformation("targetName1", "targetType2", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInfo4 = new ProbeInformation("targetName1", "targetType1", "bmClass2", "bmName2", "bmDescription2", "probeDescription2", 2, "xmlRpcUrl2");

        int hashCode1 = probeInfo1.hashCode();
        int hashCode2 = probeInfo2.hashCode();
        int hashCode3 = probeInfo3.hashCode();
        int hashCode4 = probeInfo4.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
    }
}