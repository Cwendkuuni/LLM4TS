package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testProbeInformationConstruction() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeName = "probeName";
        int precision = 42;
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(targetName, probeInformation.getTargetName());
        assertEquals(targetType, probeInformation.getTargetType());
        assertEquals(bmClass, probeInformation.getBmClass());
        assertEquals(bmName, probeInformation.getBmName());
        assertEquals(bmDescription, probeInformation.getBmDescription());
        assertEquals(probeName, probeInformation.getProbeName());
        assertEquals(precision, probeInformation.getPrecision());
        assertEquals(xmlRpcUrl, probeInformation.getXmlRpcUrl());
    }

    @Test
    public void testGetters() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 42, "xmlRpcUrl");

        assertEquals("targetType", probeInformation.getTargetType());
        assertEquals("bmName", probeInformation.getBmName());
        assertEquals("probeName", probeInformation.getProbeName());
        assertEquals("targetName", probeInformation.getTargetName());
        assertEquals("bmClass", probeInformation.getBmClass());
        assertEquals("bmDescription", probeInformation.getBmDescription());
        assertEquals(42, probeInformation.getPrecision());
        assertNotEquals(null, probeInformation.getXmlRpcUrl());
    }

    @Test
    public void testToString() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 42, "xmlRpcUrl");
        assertEquals("ProbeInformation{targetName='targetName', targetType='targetType', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeName', precision=42}", probeInformation.toString());
    }

    @Test
    public void testEquals() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 42, "xmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName2", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 42, "xmlRpcUrl1");

        ProbeInformation probeInformation3 = new ProbeInformation("targetName1", "targetType2", "bmClass1", "bmName1", "bmDescription1", "probeName1", 42, "xmlRpcUrl1");
        ProbeInformation probeInformation4 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName2", "bmDescription1", "probeName1", 42, "xmlRpcUrl1");
        ProbeInformation probeInformation5 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription2", "probeName1", 42, "xmlRpcUrl1");

        ProbeInformation probeInformation6 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName2", 42, "xmlRpcUrl1");
        ProbeInformation probeInformation7 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 43, "xmlRpcUrl1");

        ProbeInformation probeInformation8 = new ProbeInformation("targetName1", "targetType1", null, "bmName1", "bmDescription1", "probeName1", 42, "xmlRpcUrl1");
        ProbeInformation probeInformation9 = new ProbeInformation(null, "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 42, "xmlRpcUrl1");

        assertEquals(probeInformation1, probeInformation1);
        assertEquals(probeInformation1, probeInformation2);
        assertNotEquals(probeInformation1, probeInformation3);
        assertNotEquals(probeInformation1, probeInformation4);
        assertNotEquals(probeInformation1, probeInformation5);
        assertNotEquals(probeInformation1, probeInformation6);
        assertNotEquals(probeInformation1, probeInformation7);
        assertNotEquals(probeInformation1, probeInformation8);
        assertNotEquals(probeInformation1, probeInformation9);

        ProbeInformation probeInformation10 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 42, null);
        assertEquals(probeInformation1, probeInformation10);
    }

    @Test
    public void testHashCode() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 42, "xmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName2", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 42, "xmlRpcUrl1");

        int hashCode1 = probeInformation1.hashCode();
        int hashCode2 = probeInformation2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 42, "xmlRpcUrl");
        assertEquals("targetType:targetName:bmClass:bmName", probeInformation.getMeasureURI());
    }
}