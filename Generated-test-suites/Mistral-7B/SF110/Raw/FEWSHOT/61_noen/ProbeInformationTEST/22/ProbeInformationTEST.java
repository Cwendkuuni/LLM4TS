package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import fi.vtt.noen.mfw.bundle.common.*;

public class ProbeInformationTest {

    @Test
    public void testProbeInformationConstruction() {
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
        String targetType = "targetType";
        ProbeInformation probeInfo = new ProbeInformation("targetName", targetType, "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");

        assertEquals(targetType, probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        String bmName = "bmName";
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", bmName, "bmDescription", "probeDescription", 1, "xmlRpcUrl");

        assertEquals(bmName, probeInfo.getBmName());
    }

    @Test
    public void testGetProbeName() {
        String probeName = "probeName";
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", null, probeName, 1, "xmlRpcUrl");

        assertEquals(probeName, probeInfo.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        String targetName = "targetName";
        ProbeInformation probeInfo = new ProbeInformation(targetName, "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");

        assertEquals(targetName, probeInfo.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        String bmClass = "bmClass";
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", bmClass, "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");

        assertEquals(bmClass, probeInfo.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        String bmDescription = "bmDescription";
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", bmDescription, "probeDescription", 1, "xmlRpcUrl");

        assertEquals(bmDescription, probeInfo.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        int precision = 1;
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", precision, "xmlRpcUrl");

        assertEquals(precision, probeInfo.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        String targetType = "targetType";
        String targetName = "targetName";
        String bmClass = "bmClass";
        String bmName = "bmName";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, null, null, 1, null);

        String measureURI = Const.createMeasureURI(targetType, targetName, bmClass, bmName);
        assertEquals(measureURI, probeInfo.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        String xmlRpcUrl = "xmlRpcUrl";
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, xmlRpcUrl);

        assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());
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
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        String expected = "ProbeInformation{targetName='" + targetName + "', targetType='" + targetType + "', bmClass='" + bmClass + "', bmName='" + bmName + "', bmDescription='" + bmDescription + "', probeDescription='" + probeDescription + "', precision=" + precision + "}";
        assertEquals(expected, probeInfo.toString());
    }

    @Test
    public void testEquals() {
        String targetName = "targetName1";
        String targetType = "targetType1";
        String bmClass = "bmClass1";
        String bmName = "bmName1";
        String bmDescription = "bmDescription1";
        String probeDescription = "probeDescription1";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl1";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        String targetName2 = "targetName2";
        String targetType2 = "targetType1";
        String bmClass2 = "bmClass1";
        String bmName2 = "bmName1";
        String bmDescription2 = "bmDescription1";
        String probeDescription2 = "probeDescription1";
        int precision2 = 1;
        String xmlRpcUrl2 = "xmlRpcUrl2";

        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeDescription2, precision2, xmlRpcUrl2);

        ProbeInformation probeInfo3 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        ProbeInformation probeInfo4 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        ProbeInformation probeInfo5 = null;

        assertTrue(probeInfo1.equals(probeInfo1));
        assertTrue(probeInfo1.equals(probeInfo4));
        assertFalse(probeInfo1.equals(probeInfo2));
        assertFalse(probeInfo1.equals(probeInfo3));
        assertFalse(probeInfo1.equals(probeInfo5));
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
        String xmlRpcUrl = "xmlRpcUrl";

        int hashCode1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl).hashCode();

        String targetName2 = "targetName2";
        int hashCode2 = new ProbeInformation(targetName2, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl).hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testNullTargetName() {
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeDescription = "probeDescription";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInfo = new ProbeInformation(null, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        assertNull(probeInfo.getTargetName());
    }

    @Test
    public void testNullTargetType() {
        String targetName = "targetName";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeDescription = "probeDescription";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInfo = new ProbeInformation(targetName, null, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        assertNull(probeInfo.getTargetType());
    }

    @Test
    public void testNullBmClass() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeDescription = "probeDescription";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, null, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        assertNull(probeInfo.getBmClass());
    }

    @Test
    public void testNullBmName() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmDescription = "bmDescription";
        String probeDescription = "probeDescription";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, null, bmDescription, probeDescription, precision, xmlRpcUrl);

        assertNull(probeInfo.getBmName());
    }

    @Test
    public void testNullBmDescription() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String probeDescription = "probeDescription";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, null, probeDescription, precision, xmlRpcUrl);

        assertNull(probeInfo.getBmDescription());
    }

    @Test
    public void testNullProbeDescription() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, null, precision, xmlRpcUrl);

        assertNull(probeInfo.getProbeDescription());
    }

    @Test
    public void testNullPrecision() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeDescription = "probeDescription";
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, 0, xmlRpcUrl);

        assertEquals(0, probeInfo.getPrecision());
    }

    @Test
    public void testNullXmlRpcUrl() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeDescription = "probeDescription";
        int precision = 1;

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, null);

        assertNull(probeInfo.getXmlRpcUrl());
    }
}