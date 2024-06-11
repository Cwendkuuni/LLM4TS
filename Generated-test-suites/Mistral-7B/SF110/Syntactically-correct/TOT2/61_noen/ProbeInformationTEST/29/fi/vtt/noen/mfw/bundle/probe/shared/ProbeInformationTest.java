package fi.vtt.noen.mfw.bundle.probe.shared.test;

import org.junit.Test;
import static org.junit.Assert.*;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @Test
    public void testConstructor_NullInputs() {
        try {
            new ProbeInformation(null, null, null, null, null, null, 0, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testConstructor_EmptyInputs() {
        try {
            new ProbeInformation("", "", "", "", "", "", 0, "");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetters_NullInputs() {
        ProbeInformation probeInfo = new ProbeInformation("TestTarget", "TestType", "TestBMClass", "TestBMName", "TestBMDescription", "TestProbeDescription", 1, "TestXMLRpcUrl");

        // Test getTargetType()
        String expected = "TestType";
        assertEquals(expected, probeInfo.getTargetType());

        // Test getBmName()
        String emptyBmName = "";
        String nullBmName = null;
        assertEquals(probeInfo.getBmName(), emptyBmName);
        assertNull(probeInfo.getBmName(null));

        // Test getProbeName()
        String emptyProbeName = "";
        String nullProbeName = null;
        assertEquals(probeInfo.getProbeName(), emptyProbeName);
        assertNull(probeInfo.getProbeName(null));

        // Test getTargetName()
        String emptyTargetName = "";
        String nullTargetName = null;
        assertEquals(probeInfo.getTargetName(), emptyTargetName);
        assertNull(probeInfo.getTargetName(null));

        // Test getBmClass()
        String emptyBmClass = "";
        String nullBmClass = null;
        assertEquals(probeInfo.getBmClass(), emptyBmClass);
        assertNull(probeInfo.getBmClass(null));

        // Test getBmDescription()
        String emptyBmDescription = "";
        String nullBmDescription = null;
        assertEquals(probeInfo.getBmDescription(), emptyBmDescription);
        assertNull(probeInfo.getBmDescription(null));

        // Test getPrecision()
        int expectedPrecision = 1;
        assertEquals(expectedPrecision, probeInfo.getPrecision());

        // Test getMeasureURI()
        String expectedMeasureURI = Const.createMeasureURI("TestType", "TestTarget", "TestBMClass", "TestBMName");
        assertEquals(expectedMeasureURI, probeInfo.getMeasureURI());

        // Test getXmlRpcUrl()
        String emptyXmlRpcUrl = "";
        String nullXmlRpcUrl = null;
        assertEquals(probeInfo.getXmlRpcUrl(), "TestXMLRpcUrl");
        assertNull(probeInfo.getXmlRpcUrl(null));

        // Test toString()
        String expectedToString = "ProbeInformation{targetName='TestTarget', targetType='TestType', bmClass='TestBMClass', bmName='TestBMName', bmDescription='TestBMDescription', probeDescription='TestProbeDescription', precision=1}";
        assertEquals(expectedToString, probeInfo.toString());

        // Test equals()
        ProbeInformation sameProbeInfo = new ProbeInformation("TestTarget", "TestType", "TestBMClass", "TestBMName", "TestBMDescription", "TestProbeDescription", 1, "TestXMLRpcUrl");
        ProbeInformation differentProbeInfo = new ProbeInformation("TestTarget", "TestType", "TestBMClass", "TestBMName", "TestBMDescription", "TestProbeDescription2", 1, "TestXMLRpcUrl");
        ProbeInformation nullProbeInfo = null;
        assertTrue(sameProbeInfo.equals(probeInfo));
        assertFalse(differentProbeInfo.equals(probeInfo));
        assertFalse(probeInfo.equals(nullProbeInfo));
    }

    @Test
    public void testHashCode() {
        ProbeInformation probeInfo1 = new ProbeInformation("TestTarget1", "TestType1", "TestBMClass1", "TestBMName1", "TestBMDescription1", "TestProbeDescription1", 1, "TestXMLRpcUrl1");
        ProbeInformation probeInfo2 = new ProbeInformation("TestTarget2", "TestType2", "TestBMClass2", "TestBMName2", "TestBMDescription2", "TestProbeDescription2", 1, "TestXMLRpcUrl2");
        int expectedHashCode1 = probeInfo1.hashCode();
        int expectedHashCode2 = probeInfo2.hashCode();
        int actualHashCode1 = probeInfo1.hashCode();
        int actualHashCode2 = probeInfo2.hashCode();
        assertEquals(expectedHashCode1, actualHashCode1);
        assertNotEquals(expectedHashCode1, actualHashCode2);
    }
}