package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testProbeInformationCreation() {
        String targetName = "targetName1";
        String targetType = "targetType1";
        String bmClass = "bmClass1";
        String bmName = "bmName1";
        String bmDescription = "bmDescription1";
        String probeName = "probeName1";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl1";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(probeInfo1, probeInfo2);
        assertEquals(probeInfo1.hashCode(), probeInfo2.hashCode());
    }

    @Test
    public void testProbeInformationDifferentValues() {
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
        String probeName1 = "probeName1";
        String probeName2 = "probeName2";
        int precision1 = 1;
        int precision2 = 2;
        String xmlRpcUrl1 = "xmlRpcUrl1";
        String xmlRpcUrl2 = "xmlRpcUrl2";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);

        assertNotEquals(probeInfo1, probeInfo2);
        assertNotSame(probeInfo1, probeInfo2);
        assertNotEquals(probeInfo1.hashCode(), probeInfo2.hashCode());

        assertEquals(targetName1, probeInfo1.getTargetName());
        assertEquals(targetType1, probeInfo1.getTargetType());
        assertEquals(bmClass1, probeInfo1.getBmClass());
        assertEquals(bmName1, probeInfo1.getBmName());
        assertEquals(bmDescription1, probeInfo1.getBmDescription());
        assertEquals(probeName1, probeInfo1.getProbeName());
        assertEquals(precision1, probeInfo1.getPrecision());
        assertEquals(xmlRpcUrl1, probeInfo1.getXmlRpcUrl());

        assertEquals(targetName2, probeInfo2.getTargetName());
        assertEquals(targetType2, probeInfo2.getTargetType());
        assertEquals(bmClass2, probeInfo2.getBmClass());
        assertEquals(bmName2, probeInfo2.getBmName());
        assertEquals(bmDescription2, probeInfo2.getBmDescription());
        assertEquals(probeName2, probeInfo2.getProbeName());
        assertEquals(precision2, probeInfo2.getPrecision());
        assertEquals(xmlRpcUrl2, probeInfo2.getXmlRpcUrl());
    }

    @Test
    public void testGetMeasureURI() {
        String targetType = "targetType";
        String targetName = "targetName";
        String bmClass = "bmClass";
        String bmName = "bmName";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, "", "", 0, "");

        assertEquals(Const.createMeasureURI(targetType, targetName, bmClass, bmName), probeInfo.getMeasureURI());
    }

    @Test
    public void testToString() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeName = "probeName";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals("ProbeInformation{targetName='" + targetName + "', targetType='" + targetType + "', bmClass='" + bmClass + "', bmName='" + bmName + "', bmDescription='" + bmDescription + "', probeDescription='" + probeName + "', precision=1}", probeInfo.toString());
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
        String probeName1 = "probeName1";
        String probeName2 = "probeName2";
        int precision1 = 1;
        int precision2 = 2;
        String xmlRpcUrl1 = "xmlRpcUrl1";
        String xmlRpcUrl2 = "xmlRpcUrl2";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);
        ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        assertTrue(probeInfo1.equals(probeInfo3));
        assertFalse(probeInfo1.equals(probeInfo2));
    }

    @Test
    public void testHashCode() {
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
        String probeName1 = "probeName1";
        String probeName2 = "probeName2";
        int precision1 = 1;
        int precision2 = 2;
        String xmlRpcUrl1 = "xmlRpcUrl1";
        String xmlRpcUrl2 = "xmlRpcUrl2";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);
        ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        int hashcode1 = probeInfo1.hashCode();
        int hashcode2 = probeInfo2.hashCode();
        int hashcode3 = probeInfo3.hashCode();

        assertEquals(hashcode1, hashcode3);
        assertNotEquals(hashcode1, hashcode2);
    }

    @Test
    public void testNullValues() {
        ProbeInformation probeInfo1 = new ProbeInformation(null, null, null, null, null, null, 0, null);

        assertNull(probeInfo1.getTargetName());
        assertNull(probeInfo1.getTargetType());
        assertNull(probeInfo1.getBmClass());
        assertNull(probeInfo1.getBmName());
        assertNull(probeInfo1.getBmDescription());
        assertNull(probeInfo1.getProbeName());
        assertEquals(0, probeInfo1.getPrecision());
        assertNull(probeInfo1.getXmlRpcUrl());
    }

    @Test
    public void testEmptyStrings() {
        ProbeInformation probeInfo1 = new ProbeInformation("", "", "", "", "", "", 0, "");

        assertEquals("", probeInfo1.getTargetName());
        assertEquals("", probeInfo1.getTargetType());
        assertEquals("", probeInfo1.getBmClass());
        assertEquals("", probeInfo1.getBmName());
        assertEquals("", probeInfo1.getBmDescription());
        assertEquals("", probeInfo1.getProbeName());
        assertEquals(0, probeInfo1.getPrecision());
        assertEquals("", probeInfo1.getXmlRpcUrl());
    }
}