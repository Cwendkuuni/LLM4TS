package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import fi.vtt.noen.mfw.bundle.common.*;

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
        String targetType = "targetType";
        ProbeInformation probeInfo = new ProbeInformation("targetName", targetType, null, null, null, null, 0, null);
        assertEquals(targetType, probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        String bmName = "bmName";
        ProbeInformation probeInfo = new ProbeInformation(null, null, null, bmName, null, null, 0, null);
        assertEquals(bmName, probeInfo.getBmName());
    }

    @Test
    public void testGetProbeName() {
        String probeName = "probeName";
        ProbeInformation probeInfo = new ProbeInformation(null, null, null, null, null, probeName, 0, null);
        assertEquals(probeName, probeInfo.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        String targetName = "targetName";
        ProbeInformation probeInfo = new ProbeInformation(targetName, null, null, null, null, null, 0, null);
        assertEquals(targetName, probeInfo.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        String bmClass = "bmClass";
        ProbeInformation probeInfo = new ProbeInformation(null, null, bmClass, null, null, null, 0, null);
        assertEquals(bmClass, probeInfo.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        String bmDescription = "bmDescription";
        ProbeInformation probeInfo = new ProbeInformation(null, null, null, null, bmDescription, null, 0, null);
        assertEquals(bmDescription, probeInfo.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        int precision = 1;
        ProbeInformation probeInfo = new ProbeInformation(null, null, null, null, null, null, precision, null);
        assertEquals(precision, probeInfo.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        String targetType = "targetType";
        String targetName = "targetName";
        String bmClass = "bmClass";
        String bmName = "bmName";
        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, null, null, 0, null);
        assertEquals(Const.createMeasureURI(targetType, targetName, bmClass, bmName), probeInfo.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        String xmlRpcUrl = "xmlRpcUrl";
        ProbeInformation probeInfo = new ProbeInformation(null, null, null, null, null, null, 0, xmlRpcUrl);
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

        String expected = "ProbeInformation{targetName='" + targetName + "', targetType='" + targetType + "', bmClass='" + bmClass + "', bmName='" + bmName + "', bmDescription='" + bmDescription + "', probeDescription='" + probeDescription + "', precision=1}";
        assertEquals(expected, probeInfo.toString());
    }

    @Test
    public void testEquals() {
        String targetName1 = "targetName1";
        String targetName2 = "targetName2";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeDescription = "probeDescription";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeDescription, precision, null);
        ProbeInformation probeInfo4 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl + "1");
        ProbeInformation probeInfo5 = new ProbeInformation("targetName1", targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
        ProbeInformation probeInfo6 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeDescription, precision + 1, xmlRpcUrl);

        ProbeInformation probeInfo7 = new ProbeInformation(targetName1, targetType, null, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
        ProbeInformation probeInfo8 = new ProbeInformation(targetName1, null, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        // Test self-equality
        assertEquals(probeInfo1, probeInfo1);

        // Test equality with null
        assertNull(probeInfo1.equals(null));

        // Test equality with different classes
        assertNotEquals(probeInfo1, new Object());

        // Test equality with same values
        assertEquals(probeInfo1, probeInfo2);

        // Test equality with different values
        assertNotEquals(probeInfo1, probeInfo3);
        assertNotEquals(probeInfo1, probeInfo4);
        assertNotEquals(probeInfo1, probeInfo5);
        assertNotEquals(probeInfo1, probeInfo6);
        assertNotEquals(probeInfo1, probeInfo7);
        assertNotEquals(probeInfo1, probeInfo8);
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

        ProbeInformation probeInfo1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, null);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, null);

        int hashCode1 = probeInfo1.hashCode();
        int hashCode2 = probeInfo2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }
}