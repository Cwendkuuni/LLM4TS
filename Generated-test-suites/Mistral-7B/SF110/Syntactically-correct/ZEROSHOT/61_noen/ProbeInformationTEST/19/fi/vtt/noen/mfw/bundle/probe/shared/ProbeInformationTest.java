package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        assertTrue(probeInformation.toString().contains(targetName));
        assertTrue(probeInformation.toString().contains(targetType));
        assertTrue(probeInformation.toString().contains(bmClass));
        assertTrue(probeInformation.toString().contains(bmName));
        assertTrue(probeInformation.toString().contains(bmDescription));
        assertTrue(probeInformation.toString().contains(probeDescription));
        assertTrue(probeInformation.toString().contains(Integer.toString(precision)));
        assertTrue(probeInformation.toString().contains(xmlRpcUrl));
    }

    @Test
    public void testGetters() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");

        assertNotNull(probeInformation.getTargetName());
        assertNotNull(probeInformation.getTargetType());
        assertNotNull(probeInformation.getBmClass());
        assertNotNull(probeInformation.getBmName());
        assertNotNull(probeInformation.getBmDescription());
        assertNotNull(probeInformation.getProbeName());
        assertEquals(1, probeInformation.getPrecision());
        assertNotNull(probeInformation.getXmlRpcUrl());
    }

    @Test
    public void testMeasureURI() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");

        assertEquals(Const.createMeasureURI("targetType", "targetName", "bmClass", "bmName"), probeInformation.getMeasureURI());
    }

    @Test
    public void testEquals() {
        ProbeInformation probeInformation1 = new ProbeInformation("target1Name", "target1Type", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("target2Name", "target2Type", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInformation3 = new ProbeInformation("target3Name", "target3Type", "bmClass2", "bmName2", "bmDescription2", "probeDescription2", 2, "xmlRpcUrl2");

        assertTrue(probeInformation1.equals(probeInformation1));
        assertTrue(probeInformation1.equals(probeInformation2));
        assertFalse(probeInformation1.equals(probeInformation3));
        assertFalse(probeInformation1.equals(null));
        assertFalse(probeInformation1.equals(""));
        assertFalse(probeInformation1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        ProbeInformation probeInformation1 = new ProbeInformation("target1Name", "target1Type", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("target2Name", "target2Type", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInformation3 = new ProbeInformation("target3Name", "target3Type", "bmClass2", "bmName2", "bmDescription2", "probeDescription2", 2, "xmlRpcUrl2");

        assertEquals(probeInformation1.hashCode(), probeInformation1.hashCode());
        assertEquals(probeInformation1.hashCode(), probeInformation2.hashCode());
        assertNotEquals(probeInformation1.hashCode(), probeInformation3.hashCode());
        assertNotEquals(probeInformation1.hashCode(), 123);
    }

}