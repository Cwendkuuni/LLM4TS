package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;

import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertEquals("targetType1", probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertEquals("bmName1", probeInfo.getBmName());
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation probeInfo = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertEquals("probeDescription1", probeInfo.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        ProbeInformation probeInfo = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertEquals("targetName1", probeInfo.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        ProbeInformation probeInfo = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertEquals("bmClass1", probeInfo.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInfo = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertEquals("bmDescription1", probeInfo.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        ProbeInformation probeInfo = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertEquals(1, probeInfo.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertEquals("http://localhost:8080/measure?targetType=targetType1&targetName=targetName1&bmClass=bmClass1&bmName=bmName1", probeInfo.getMeasureURI());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullTargetName() {
        new ProbeInformation(null, "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullTargetType() {
        new ProbeInformation("targetName1", null, "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullBmClass() {
        new ProbeInformation("targetName1", "targetType1", null, "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullBmName() {
        new ProbeInformation("targetName1", "targetType1", "bmClass1", null, "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullBmDescription() {
        new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", null, "probeDescription1", 1, "xmlRpcUrl1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullProbeDescription() {
        new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", null, 1, "xmlRpcUrl1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullXmlRpcUrl() {
        new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, null);
    }

    @Test
    public void testEquals() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertTrue(probeInfo1.equals(probeInfo2));

        ProbeInformation probeInfo3 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName2", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertFalse(probeInfo1.equals(probeInfo3));

        ProbeInformation probeInfo4 = new ProbeInformation("targetName2", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertFalse(probeInfo1.equals(probeInfo4));

        ProbeInformation probeInfo5 = new ProbeInformation("targetName1", "targetType2", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertFalse(probeInfo1.equals(probeInfo5));
    }

    @Test
    public void testHashCode() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertEquals(probeInfo1.hashCode(), probeInfo2.hashCode());

        ProbeInformation probeInfo3 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName2", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertNotEquals(probeInfo1.hashCode(), probeInfo3.hashCode());

        ProbeInformation probeInfo4 = new ProbeInformation("targetName2", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertNotEquals(probeInfo1.hashCode(), probeInfo4.hashCode());

        ProbeInformation probeInfo5 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription2", "probeDescription1", 1, "xmlRpcUrl1");
        assertNotEquals(probeInfo1.hashCode(), probeInfo5.hashCode());
    }

    @Test
    public void testToString() {
        ProbeInformation probeInfo = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertEquals("ProbeInformation{targetName='targetName1', targetType='targetType1', bmClass='bmClass1', bmName='bmName1', bmDescription='bmDescription1', probeDescription='probeDescription1', precision=1}", probeInfo.toString());
    }
}