package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;

import fi.vtt.noen.mfw.bundle.common.Const;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        String targetType = "testTargetType";
        ProbeInformation probeInfo = new ProbeInformation("testTargetName", targetType, "testBmClass", "testBmName", "testBmDescription", "testProbeDescription", 1, "testXmlRpcUrl");
        String actualTargetType = probeInfo.getTargetType();
        assertEquals(targetType, actualTargetType);
    }

    @Test
    public void testGetBmName() {
        String bmName = "testBmName";
        ProbeInformation probeInfo = new ProbeInformation("testTargetName", "testTargetType", "testBmClass", bmName, "testBmDescription", "testProbeDescription", 1, "testXmlRpcUrl");
        String actualBmName = probeInfo.getBmName();
        assertEquals(bmName, actualBmName);
    }

    @Test
    public void testGetProbeName() {
        String probeName = "testProbeName";
        ProbeInformation probeInfo = new ProbeInformation("testTargetName", "testTargetType", "testBmClass", "testBmName", "testBmDescription", probeName, 1, "testXmlRpcUrl");
        String actualProbeName = probeInfo.getProbeName();
        assertEquals(probeName, actualProbeName);
    }

    @Test
    public void testGetTargetName() {
        String targetName = "testTargetName";
        ProbeInformation probeInfo = new ProbeInformation(targetName, "testTargetType", "testBmClass", "testBmName", "testBmDescription", "testProbeDescription", 1, "testXmlRpcUrl");
        String actualTargetName = probeInfo.getTargetName();
        assertEquals(targetName, actualTargetName);
    }

    @Test
    public void testGetBmClass() {
        String bmClass = "testBmClass";
        ProbeInformation probeInfo = new ProbeInformation("testTargetName", "testTargetType", bmClass, "testBmName", "testBmDescription", "testProbeDescription", 1, "testXmlRpcUrl");
        String actualBmClass = probeInfo.getBmClass();
        assertEquals(bmClass, actualBmClass);
    }

    @Test
    public void testGetBmDescription() {
        String bmDescription = "testBmDescription";
        ProbeInformation probeInfo = new ProbeInformation("testTargetName", "testTargetType", "testBmClass", "testBmName", bmDescription, "testProbeDescription", 1, "testXmlRpcUrl");
        String actualBmDescription = probeInfo.getBmDescription();
        assertEquals(bmDescription, actualBmDescription);
    }

    @Test
    public void testGetPrecision() {
        int precision = 1;
        ProbeInformation probeInfo = new ProbeInformation("testTargetName", "testTargetType", "testBmClass", "testBmName", "testBmDescription", "testProbeDescription", precision, "testXmlRpcUrl");
        int actualPrecision = probeInfo.getPrecision();
        assertEquals(precision, actualPrecision);
    }

    @Test
    public void testGetMeasureURI() {
        String targetType = "testTargetType";
        String targetName = "testTargetName";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, "testBmDescription", "testProbeDescription", 1, "testXmlRpcUrl");
        String actualMeasureURI = probeInfo.getMeasureURI();
        String expectedMeasureURI = Const.createMeasureURI(targetType, targetName, bmClass, bmName);
        assertEquals(expectedMeasureURI, actualMeasureURI);
    }

    @Test
    public void testGetXmlRpcUrl() {
        String xmlRpcUrl = "testXmlRpcUrl";
        ProbeInformation probeInfo = new ProbeInformation("testTargetName", "testTargetType", "testBmClass", "testBmName", "testBmDescription", "testProbeDescription", 1, xmlRpcUrl);
        String actualXmlRpcUrl = probeInfo.getXmlRpcUrl();
        assertEquals(xmlRpcUrl, actualXmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_NullValues() {
        new ProbeInformation(null, null, null, null, null, null, 0, null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetTargetName_NullTargetName() {
        ProbeInformation probeInfo = new ProbeInformation("", "testTargetType", "testBmClass", "testBmName", "testBmDescription", "testProbeDescription", 1, "testXmlRpcUrl");
        String actualTargetName = probeInfo.getTargetName();
        assertNull(actualTargetName);
    }

    @Test(expected = NullPointerException.class)
    public void testGetBmName_NullBmName() {
        ProbeInformation probeInfo = new ProbeInformation("testTargetName", "testTargetType", "testBmClass", null, "testBmDescription", "testProbeDescription", 1, "testXmlRpcUrl");
        String actualBmName = probeInfo.getBmName();
        assertNull(actualBmName);
    }

    @Test(expected = NullPointerException.class)
    public void testGetBmDescription_NullBmDescription() {
        ProbeInformation probeInfo = new ProbeInformation("testTargetName", "testTargetType", "testBmClass", "testBmName", null, "testProbeDescription", 1, "testXmlRpcUrl");
        String actualBmDescription = probeInfo.getBmDescription();
        assertNull(actualBmDescription);
    }

    @Test(expected = NullPointerException.class)
    public void testGetProbeName_NullProbeName() {
        ProbeInformation probeInfo = new ProbeInformation("testTargetName", "testTargetType", "testBmClass", "testBmName", "testBmDescription", null, 1, "testXmlRpcUrl");
        String actualProbeName = probeInfo.getProbeName();
        assertNull(actualProbeName);
    }

    @Test
    public void testEquals() {
        String targetName1 = "testTargetName1";
        String targetName2 = "testTargetName2";
        String sameBmClass = "sameBmClass";
        String differentBmClass = "differentBmClass";
        String sameBmName = "sameBmName";
        String differentBmName = "differentBmName";
        String sameBmDescription = "sameBmDescription";
        String differentBmDescription = "differentBmDescription";
        String sameProbeDescription = "sameProbeDescription";
        String differentProbeDescription = "differentProbeDescription";
        int samePrecision = 1;
        int differentPrecision = 2;
        String sameXmlRpcUrl = "sameXmlRpcUrl";
        String differentXmlRpcUrl = "differentXmlRpcUrl";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, "testTargetType", sameBmClass, sameBmName, sameBmDescription, sameProbeDescription, samePrecision, sameXmlRpcUrl);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, "testTargetType", sameBmClass, sameBmName, sameBmDescription, sameProbeDescription, samePrecision, sameXmlRpcUrl);
        ProbeInformation probeInfo3 = new ProbeInformation(targetName1, "testTargetType", differentBmClass, differentBmName, differentBmDescription, differentProbeDescription, samePrecision, sameXmlRpcUrl);
        ProbeInformation probeInfo4 = new ProbeInformation(targetName1, "testTargetType", sameBmClass, sameBmName, sameBmDescription, sameProbeDescription, samePrecision, differentXmlRpcUrl);

        assertTrue(probeInfo1.equals(probeInfo2));
        assertFalse(probeInfo1.equals(probeInfo3));
        assertFalse(probeInfo1.equals(probeInfo4));
    }

    @Test
    public void testHashCode() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeDescription = "testProbeDescription";
        int precision = 1;

        int hashCode1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, null).hashCode();
        int hashCode2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, null).hashCode();

        assertEquals(hashCode1, hashCode2);

        String differentTargetName = "differentTargetName";
        int differentPrecision = 2;

        int hashCode3 = new ProbeInformation(differentTargetName, targetType, bmClass, bmName, bmDescription, probeDescription, differentPrecision, null).hashCode();

        assertNotEquals(hashCode1, hashCode3);
    }
}