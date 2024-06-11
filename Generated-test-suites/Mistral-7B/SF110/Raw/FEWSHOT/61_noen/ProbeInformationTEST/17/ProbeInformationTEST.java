package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProbeInformationTest {
    @Test
    public void testProbeInformationConstruction() {
        String targetName = "TestTarget";
        String targetType = "TestType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "http://localhost:8080/xmlrpc";

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
    public void testGetMeasureURI() {
        String targetType = "TestType";
        String targetName = "TestTarget";
        String bmClass = "BMClass";
        String bmName = "BMName";

        ProbeInformation probeInfo = new ProbeInformation("TestTarget", targetType, bmClass, bmName, null, null, 1, null);

        String measureURI = probeInfo.getMeasureURI();

        assertEquals("http://localhost:8080/measure/" + targetType + "/" + targetName + "/" + bmClass + "/" + bmName, measureURI);
    }

    @Test
    public void testEquals() {
        String targetName1 = "TestTarget1";
        String targetName2 = "TestTarget2";
        String targetType = "TestType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "http://localhost:8080/xmlrpc";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        assertTrue(probeInfo1.equals(probeInfo1));
        assertTrue(probeInfo1.equals(probeInfo2));

        String differentTargetName = "DifferentTarget";
        ProbeInformation differentProbeInfo = new ProbeInformation(differentTargetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        assertFalse(probeInfo1.equals(differentProbeInfo));

        String differentPrecision = 2;
        ProbeInformation differentPrecisionProbeInfo = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeDescription, differentPrecision, xmlRpcUrl);

        assertFalse(probeInfo1.equals(differentPrecisionProbeInfo));

        String differentBmClass = "DifferentBMClass";
        ProbeInformation differentBmClassProbeInfo = new ProbeInformation(targetName1, targetType, differentBmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        assertFalse(probeInfo1.equals(differentBmClassProbeInfo));

        String differentBmName = "DifferentBMName";
        ProbeInformation differentBmNameProbeInfo = new ProbeInformation(targetName1, targetType, bmClass, differentBmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        assertFalse(probeInfo1.equals(differentBmNameProbeInfo));

        String differentBmDescription = "DifferentBMDescription";
        ProbeInformation differentBmDescriptionProbeInfo = new ProbeInformation(targetName1, targetType, bmClass, bmName, differentBmDescription, probeDescription, precision, xmlRpcUrl);

        assertFalse(probeInfo1.equals(differentBmDescriptionProbeInfo));

        String differentProbeDescription = "DifferentProbeDescription";
        ProbeInformation differentProbeDescriptionProbeInfo = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, differentProbeDescription, precision, xmlRpcUrl);

        assertFalse(probeInfo1.equals(differentProbeDescriptionProbeInfo));

        String differentXmlRpcUrl = "http://localhost:8080/differentxmlrpc";
        ProbeInformation differentXmlRpcUrlProbeInfo = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeDescription, precision, differentXmlRpcUrl);

        assertFalse(probeInfo1.equals(differentXmlRpcUrlProbeInfo));
    }

    @Test
    public void testHashCode() {
        String targetName = "TestTarget";
        String targetType = "TestType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "http://localhost:8080/xmlrpc";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        int hashCode = probeInfo.hashCode();

        String differentTargetName = "DifferentTarget";
        ProbeInformation differentProbeInfo = new ProbeInformation(differentTargetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        assertNotEquals(hashCode, differentProbeInfo.hashCode());

        String differentTargetType = "DifferentTargetType";
        ProbeInformation differentTargetTypeProbeInfo = new ProbeInformation(targetName, differentTargetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        assertNotEquals(hashCode, differentTargetTypeProbeInfo.hashCode());

        String differentBmClass = "DifferentBMClass";
        ProbeInformation differentBmClassProbeInfo = new ProbeInformation(targetName, targetType, differentBmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        assertNotEquals(hashCode, differentBmClassProbeInfo.hashCode());

        String differentBmName = "DifferentBMName";
        ProbeInformation differentBmNameProbeInfo = new ProbeInformation(targetName, targetType, bmClass, differentBmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        assertNotEquals(hashCode, differentBmNameProbeInfo.hashCode());

        String differentBmDescription = "DifferentBMDescription";
        ProbeInformation differentBmDescriptionProbeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, differentBmDescription, probeDescription, precision, xmlRpcUrl);

        assertNotEquals(hashCode, differentBmDescriptionProbeInfo.hashCode());

        String differentProbeDescription = "DifferentProbeDescription";
        ProbeInformation differentProbeDescriptionProbeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, differentProbeDescription, precision, xmlRpcUrl);

        assertNotEquals(hashCode, differentProbeDescriptionProbeInfo.hashCode());

        String differentXmlRpcUrl = "http://localhost:8080/differentxmlrpc";
        ProbeInformation differentXmlRpcUrlProbeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, differentXmlRpcUrl);

        assertNotEquals(hashCode, differentXmlRpcUrlProbeInfo.hashCode());

        ProbeInformation sameProbeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        assertEquals(hashCode, sameProbeInfo.hashCode());
    }

    @Test
    public void testToString() {
        String targetName = "TestTarget";
        String targetType = "TestType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "http://localhost:8080/xmlrpc";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        String expectedToString = "ProbeInformation{targetName='TestTarget', targetType='TestType', bmClass='BMClass', bmName='BMName', bmDescription='BMDescription', probeDescription='ProbeDescription', precision=1}";

        assertEquals(expectedToString, probeInfo.toString());
    }
}