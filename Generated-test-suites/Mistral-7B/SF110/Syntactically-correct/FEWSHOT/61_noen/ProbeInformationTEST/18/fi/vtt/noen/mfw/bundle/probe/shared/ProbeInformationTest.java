package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testProbeInformationConstructor() {
        String targetName1 = "targetName1";
        String targetType1 = "targetType1";
        String bmClass1 = "bmClass1";
        String bmName1 = "bmName1";
        String bmDescription1 = "bmDescription1";
        String probeName1 = "probeName1";
        int precision1 = 1;
        String xmlRpcUrl1 = "xmlRpcUrl1";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        assertEquals(targetName1, probeInfo1.getTargetName());
        assertEquals(targetType1, probeInfo1.getTargetType());
        assertEquals(bmClass1, probeInfo1.getBmClass());
        assertEquals(bmName1, probeInfo1.getBmName());
        assertEquals(bmDescription1, probeInfo1.getBmDescription());
        assertEquals(probeName1, probeInfo1.getProbeName());
        assertEquals(precision1, probeInfo1.getPrecision());
        assertEquals(xmlRpcUrl1, probeInfo1.getXmlRpcUrl());

        String targetName2 = "targetName2";
        String targetType2 = "targetType2";
        String bmClass2 = "bmClass2";
        String bmName2 = "bmName2";
        String bmDescription2 = "bmDescription2";
        String probeName2 = "probeName2";
        int precision2 = 2;
        String xmlRpcUrl2 = "xmlRpcUrl2";

        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);

        assertNotEquals(probeInfo1, probeInfo2);
    }

    @Test
    public void testGetters() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 1, "xmlRpcUrl");

        assertEquals("targetType", probeInfo.getTargetType());
        assertEquals("bmName", probeInfo.getBmName());
        assertEquals("probeName", probeInfo.getProbeName());
        assertEquals("targetName", probeInfo.getTargetName());
        assertEquals("bmClass", probeInfo.getBmClass());
        assertEquals("bmDescription", probeInfo.getBmDescription());
        assertEquals(1, probeInfo.getPrecision());
        assertEquals("xmlRpcUrl", probeInfo.getXmlRpcUrl());
    }

    @Test
    public void testMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("targetType", "targetName", "bmClass", "bmName", null, null, 1, null);
        assertEquals(Const.createMeasureURI("targetType", "targetName", "bmClass", "bmName"), probeInfo.getMeasureURI());
    }

    @Test
    public void testToString() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 1, "xmlRpcUrl");
        String expected = "ProbeInformation{targetName='targetName', targetType='targetType', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeName', precision=1}";
        assertEquals(expected, probeInfo.toString());
    }

    @Test
    public void testEquals() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
        ProbeInformation probeInfo3 = new ProbeInformation("targetName2", "targetType2", "bmClass2", "bmName2", "bmDescription2", "probeName2", 2, "xmlRpcUrl2");

        assertEquals(probeInfo1, probeInfo1);
        assertEquals(probeInfo1, probeInfo2);
        assertNotEquals(probeInfo1, probeInfo3);
        assertNotEquals(probeInfo1, null);
    }

    @Test
    public void testHashCode() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
        ProbeInformation probeInfo3 = new ProbeInformation("targetName2", "targetType2", "bmClass2", "bmName2", "bmDescription2", "probeName2", 2, "xmlRpcUrl2");

        int hashCode1 = probeInfo1.hashCode();
        int hashCode2 = probeInfo2.hashCode();
        int hashCode3 = probeInfo3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }
}