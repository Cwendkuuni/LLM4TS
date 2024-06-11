package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;

import org.junit.Test;

import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    // Test constructor
    @Test
    public void testProbeInformationConstruction() {
        String targetName = "TestTarget";
        String targetType = "TestType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeName = "TestProbeName";
        int precision = 5;
        String xmlRpcUrl = "http://test.com";

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

    // Test getters

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("TestTarget", "TestType", "TestBMClass", "TestBMName", "TestBMDescription", "TestProbeName", 5, "http://test.com");

        String expected = "TestType";
        assertEquals(expected, probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("TestTarget", "TestType", "TestBMClass", "TestBMName", "TestBMDescription", "TestProbeName", 5, "http://test.com");

        String expected = "TestBMName";
        assertEquals(expected, probeInfo.getBmName());
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation probeInfo = new ProbeInformation("TestTarget", "TestType", "TestBMClass", "TestBMName", "TestBMDescription", "TestProbeName", 5, "http://test.com");

        String expected = "TestProbeName";
        assertEquals(expected, probeInfo.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        ProbeInformation probeInfo = new ProbeInformation("TestTarget", "TestType", "TestBMClass", "TestBMName", "TestBMDescription", "TestProbeName", 5, "http://test.com");

        String expected = "TestTarget";
        assertEquals(expected, probeInfo.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        ProbeInformation probeInfo = new ProbeInformation("TestTarget", "TestType", "TestBMClass", "TestBMName", "TestBMDescription", "TestProbeName", 5, "http://test.com");

        String expected = "TestBMClass";
        assertEquals(expected, probeInfo.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInfo = new ProbeInformation("TestTarget", "TestType", "TestBMClass", "TestBMName", "TestBMDescription", "TestProbeName", 5, "http://test.com");

        String expected = "TestBMDescription";
        assertEquals(expected, probeInfo.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        ProbeInformation probeInfo = new ProbeInformation("TestTarget", "TestType", "TestBMClass", "TestBMName", "TestBMDescription", "TestProbeName", 5, "http://test.com");

        int expected = 5;
        assertEquals(expected, probeInfo.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("TestTarget", "TestType", "TestBMClass", "TestBMName", "TestBMDescription", "TestProbeName", 5, "http://test.com");

        String expected = Const.createMeasureURI("TestType", "TestTarget", "TestBMClass", "TestBMName");
        assertEquals(expected, probeInfo.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInfo = new ProbeInformation("TestTarget", "TestType", "TestBMClass", "TestBMName", "TestBMDescription", "TestProbeName", 5, "http://test.com");

        String expected = "http://test.com";
        assertEquals(expected, probeInfo.getXmlRpcUrl());
    }

    // Test toString()
    @Test
    public void testToString() {
        ProbeInformation probeInfo = new ProbeInformation("TestTarget", "TestType", "TestBMClass", "TestBMName", "TestBMDescription", "TestProbeName", 5, "http://test.com");

        String expected = "ProbeInformation{targetName='TestTarget', targetType='TestType', bmClass='TestBMClass', bmName='TestBMName', bmDescription='TestBMDescription', probeDescription='TestProbeName', precision=5}";
        assertEquals(expected, probeInfo.toString());
    }

    // Test equals()
    @Test
    public void testEquals() {
        ProbeInformation probeInfo1 = new ProbeInformation("TestTarget1", "TestType1", "TestBMClass1", "TestBMName1", "TestBMDescription1", "TestProbeName1", 5, "http://test.com1");
        ProbeInformation probeInfo2 = new ProbeInformation("TestTarget1", "TestType1", "TestBMClass1", "TestBMName1", "TestBMDescription1", "TestProbeName1", 5, "http://test.com1");

        ProbeInformation probeInfo3 = new ProbeInformation("TestTarget2", "TestType2", "TestBMClass2", "TestBMName2", "TestBMDescription2", "TestProbeName2", 5, "http://test.com2");

        assertTrue(probeInfo1.equals(probeInfo2));
        assertFalse(probeInfo1.equals(probeInfo3));
    }

    // Test hashCode()
    @Test
    public void testHashCode() {
        ProbeInformation probeInfo1 = new ProbeInformation("TestTarget1", "TestType1", "TestBMClass1", "TestBMName1", "TestBMDescription1", "TestProbeName1", 5, "http://test.com1");
        ProbeInformation probeInfo2 = new ProbeInformation("TestTarget1", "TestType1", "TestBMClass1", "TestBMName1", "TestBMDescription1", "TestProbeName1", 5, "http://test.com1");
        ProbeInformation probeInfo3 = new ProbeInformation("TestTarget2", "TestType2", "TestBMClass2", "TestBMName2", "TestBMDescription2", "TestProbeName2", 5, "http://test.com2");

        int expected1 = probeInfo1.hashCode();
        int expected2 = probeInfo2.hashCode();
        int expected3 = probeInfo3.hashCode();

        assertEquals(expected1, expected2);
        assertNotEquals(expected1, expected3);
    }

    // Edge cases and exception handling tests

    @Test(expected = NullPointerException.class)
    public void testNullTargetName() {
        String targetType = "TestType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeName = "TestProbeName";
        int precision = 5;
        String xmlRpcUrl = "http://test.com";

        ProbeInformation probeInfo = new ProbeInformation(null, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testNullTargetType() {
        String targetName = "TestTarget";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeName = "TestProbeName";
        int precision = 5;
        String xmlRpcUrl = "http://test.com";

        ProbeInformation probeInfo = new ProbeInformation(targetName, null, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testNullBmClass() {
        String targetName = "TestTarget";
        String targetType = "TestType";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeName = "TestProbeName";
        int precision = 5;
        String xmlRpcUrl = "http://test.com";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, null, bmName, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testNullBmName() {
        String targetName = "TestTarget";
        String targetType = "TestType";
        String bmClass = "TestBMClass";
        String bmDescription = "TestBMDescription";
        String probeName = "TestProbeName";
        int precision = 5;
        String xmlRpcUrl = "http://test.com";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, null, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testNullBmDescription() {
        String targetName = "TestTarget";
        String targetType = "TestType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String probeName = "TestProbeName";
        int precision = 5;
        String xmlRpcUrl = "http://test.com";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, null, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testNullProbeName() {
        String targetName = "TestTarget";
        String targetType = "TestType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        int precision = 5;
        String xmlRpcUrl = "http://test.com";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, null, precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativePrecision() {
        String targetName = "TestTarget";
        String targetType = "TestType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeName = "TestProbeName";
        int precision = -1;
        String xmlRpcUrl = "http://test.com";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullXmlRpcUrl() {
        String targetName = "TestTarget";
        String targetType = "TestType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeName = "TestProbeName";
        int precision = 5;
        String xmlRpcUrl = null;

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
    }
}