package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import fi.vtt.noen.mfw.bundle.common.Const;

import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testProbeInformationCreation() {
        String targetName = "TestTarget";
        String targetType = "TestType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeName = "TestProbeName";
        int precision = 5;
        String xmlRpcUrl = "http://localhost:8080/probe";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(targetName, probeInfo.getTargetName());
        assertEquals(targetType, probeInfo.getTargetType());
        assertEquals(bmClass, probeInfo.getBmClass());
        assertEquals(bmName, probeInfo.getBmName());
        assertEquals(bmDescription, probeInfo.getBmDescription());
        assertEquals(probeName, probeInfo.getProbeName());
        assertEquals(precision, probeInfo.getPrecision());
        assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());
    }

    @Test
    public void testGetMeasureURI() {
        String targetType = "TestType";
        String targetName = "TestTarget";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";

        ProbeInformation probeInfo = new ProbeInformation("TestTarget", targetType, bmClass, bmName, null, null, 0, null);

        assertEquals(Const.createMeasureURI(targetType, targetName, bmClass, bmName), probeInfo.getMeasureURI());
    }

    @Test
    public void testToString() {
        String targetName = "TestTarget";
        String targetType = "TestType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeName = "TestProbeName";
        int precision = 5;
        String xmlRpcUrl = "http://localhost:8080/probe";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        String expected = "ProbeInformation{targetName='TestTarget', targetType='TestType', bmClass='TestBMClass', bmName='TestBMName', bmDescription='TestBMDescription', probeDescription='TestProbeName', precision=5}";
        assertEquals(expected, probeInfo.toString());
    }

    @Test
    public void testEquals() {
        String targetName1 = "TestTarget1";
        String targetName2 = "TestTarget2";
        String targetType1 = "TestType1";
        String targetType2 = "TestType2";
        String bmClass1 = "TestBMClass1";
        String bmClass2 = "TestBMClass2";
        String bmName1 = "TestBMName1";
        String bmName2 = "TestBMName2";
        String bmDescription1 = "TestBMDescription1";
        String bmDescription2 = "TestBMDescription2";
        String probeName1 = "TestProbeName1";
        String probeName2 = "TestProbeName2";
        int precision1 = 5;
        int precision2 = 6;
        String xmlRpcUrl1 = "http://localhost:8080/probe1";
        String xmlRpcUrl2 = "http://localhost:8080/probe2";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);
        ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        assertTrue(probeInfo1.equals(probeInfo3));
        assertFalse(probeInfo1.equals(probeInfo2));
        assertNull(probeInfo1.equals(null));
    }

    @Test
    public void testHashCode() {
        String targetName = "TestTarget";
        String targetType = "TestType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeName = "TestProbeName";
        int precision = 5;

        ProbeInformation probeInfo1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, null);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, null);

        int hashCode1 = probeInfo1.hashCode();
        int hashCode2 = probeInfo2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testGetters() {
        String targetName = "TestTarget";
        String targetType = "TestType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeName = "TestProbeName";
        int precision = 5;
        String xmlRpcUrl = "http://localhost:8080/probe";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(targetName, probeInfo.getTargetName());
        assertEquals(targetType, probeInfo.getTargetType());
        assertEquals(bmClass, probeInfo.getBmClass());
        assertEquals(bmName, probeInfo.getBmName());
        assertEquals(bmDescription, probeInfo.getBmDescription());
        assertEquals(probeName, probeInfo.getProbeName());
        assertEquals(precision, probeInfo.getPrecision());
        assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());

        assertNull(probeInfo.getTargetName(null));
        assertNull(probeInfo.getTargetType(null));
        assertNull(probeInfo.getBmClass(null));
        assertNull(probeInfo.getBmName(null));
        assertNull(probeInfo.getBmDescription(null));
        assertNull(probeInfo.getProbeName(null));
        assertNull(probeInfo.getPrecision(null));
        assertNull(probeInfo.getXmlRpcUrl(null));
    }

    @Test
    public void testNullArguments() {
        String targetName = null;
        String targetType = null;
        String bmClass = null;
        String bmName = null;
        String bmDescription = null;
        String probeName = null;
        int precision = 5;
        String xmlRpcUrl = null;

        try {
            new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEmptyStrings() {
        String targetName = "";
        String targetType = "";
        String bmClass = "";
        String bmName = "";
        String bmDescription = "";
        String probeName = "";
        int precision = 5;
        String xmlRpcUrl = "";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(targetName, probeInfo.getTargetName());
        assertEquals(targetType, probeInfo.getTargetType());
        assertEquals(bmClass, probeInfo.getBmClass());
        assertEquals(bmName, probeInfo.getBmName());
        assertEquals(bmDescription, probeInfo.getBmDescription());
        assertEquals(probeName, probeInfo.getProbeName());
        assertEquals(precision, probeInfo.getPrecision());
        assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());
    }

    @Test
    public void testEqualsWithDifferentObjects() {
        String targetName1 = "TestTarget1";
        String targetName2 = "TestTarget2";
        String targetType1 = "TestType1";
        String targetType2 = "TestType2";
        String bmClass1 = "TestBMClass1";
        String bmClass2 = "TestBMClass2";
        String bmName1 = "TestBMName1";
        String bmName2 = "TestBMName2";
        String bmDescription1 = "TestBMDescription1";
        String bmDescription2 = "TestBMDescription2";
        String probeName1 = "TestProbeName1";
        String probeName2 = "TestProbeName2";
        int precision1 = 5;
        int precision2 = 6;

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, null);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, null);

        assertNotEquals(probeInfo1, probeInfo2);
    }

    @Test
    public void testHashCodeWithDifferentObjects() {
        String targetName1 = "TestTarget1";
        String targetName2 = "TestTarget2";
        String targetType1 = "TestType1";
        String targetType2 = "TestType2";
        String bmClass1 = "TestBMClass1";
        String bmClass2 = "TestBMClass2";
        String bmName1 = "TestBMName1";
        String bmName2 = "TestBMName2";
        String bmDescription1 = "TestBMDescription1";
        String bmDescription2 = "TestBMDescription2";
        String probeName1 = "TestProbeName1";
        String probeName2 = "TestProbeName2";
        int precision1 = 5;
        int precision2 = 6;

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, null);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, null);

        int hashCode1 = probeInfo1.hashCode();
        int hashCode2 = probeInfo2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }
}