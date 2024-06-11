package fi.vtt.noen.mfw.bundle.probe.shared;

import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("TestTarget", "TestType", "TestBMClass", "TestBMName", "TestBMDescription", "TestProbeName", 4, "TestXMLRPCUrl");
        assertEquals("TestType", probeInfo.getTargetType());
    }

    @Test
    public void testConstructorWithNullValues() {
        try {
            new ProbeInformation(null, null, null, null, null, null, 0, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetBmNameWithEmptyString() {
        ProbeInformation probeInfo = new ProbeInformation("TestTarget", "TestType", "TestBMClass", "", "TestBMDescription", "TestProbeName", 4, "TestXMLRPCUrl");
        assertEquals("", probeInfo.getBmName());
    }

    // Add more test cases...

    @Test
    public void testEqualsWithNullObject() {
        ProbeInformation probeInfo1 = new ProbeInformation("TestTarget1", "TestType1", "TestBMClass1", "TestBMName1", "TestBMDescription1", "TestProbeName1", 4, "TestXMLRPCUrl1");
        ProbeInformation probeInfo2 = null;
        assertFalse(probeInfo1.equals(probeInfo2));
    }

    @Test
    public void testEqualsWithDifferentObjects() {
        ProbeInformation probeInfo1 = new ProbeInformation("TestTarget1", "TestType1", "TestBMClass1", "TestBMName1", "TestBMDescription1", "TestProbeName1", 4, "TestXMLRPCUrl1");
        ProbeInformation probeInfo2 = new ProbeInformation("TestTarget2", "TestType2", "TestBMClass2", "TestBMName2", "TestBMDescription2", "TestProbeName2", 4, "TestXMLRPCUrl2");
        assertFalse(probeInfo1.equals(probeInfo2));
    }

    @Test
    public void testEqualsWithSameObjects() {
        ProbeInformation probeInfo1 = new ProbeInformation("TestTarget1", "TestType1", "TestBMClass1", "TestBMName1", "TestBMDescription1", "TestProbeName1", 4, "TestXMLRPCUrl1");
        ProbeInformation probeInfo2 = probeInfo1;
        assertTrue(probeInfo1.equals(probeInfo2));
    }

    @Test
    public void testHashCodeWithSameObjects() {
        ProbeInformation probeInfo1 = new ProbeInformation("TestTarget1", "TestType1", "TestBMClass1", "TestBMName1", "TestBMDescription1", "TestProbeName1", 4, "TestXMLRPCUrl1");
        ProbeInformation probeInfo2 = probeInfo1;
        assertEquals(probeInfo1.hashCode(), probeInfo2.hashCode());
    }

    @Test
    public void testHashCodeWithDifferentObjects() {
        ProbeInformation probeInfo1 = new ProbeInformation("TestTarget1", "TestType1", "TestBMClass1", "TestBMName1", "TestBMDescription1", "TestProbeName1", 4, "TestXMLRPCUrl1");
        ProbeInformation probeInfo2 = new ProbeInformation("TestTarget2", "TestType2", "TestBMClass2", "TestBMName2", "TestBMDescription2", "TestProbeName2", 4, "TestXMLRPCUrl2");
        assertNotEquals(probeInfo1.hashCode(), probeInfo2.hashCode());
    }

    // Add more test cases for the edge cases and exceptions...

}