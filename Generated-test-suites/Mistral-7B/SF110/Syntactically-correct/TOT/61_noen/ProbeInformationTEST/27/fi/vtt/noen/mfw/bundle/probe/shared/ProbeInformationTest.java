package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        String expectedTargetType = "targetType1";
        String actualTargetType = probeInformation.getTargetType();
        assertEquals(expectedTargetType, actualTargetType);

        // Test with null targetType
        ProbeInformation nullTargetTypeProbeInformation = new ProbeInformation("targetName2", null, "bmClass2", "bmName2", "bmDescription2", "probeDescription2", 2, "xmlRpcUrl2");
        String nullExpectedTargetType = null;
        String actualNullTargetType = null;
        assertNull(actualNullTargetType);
        assertNull(expectedTargetType);
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        String expectedBmName = "bmName1";
        String actualBmName = probeInformation.getBmName();
        assertEquals(expectedBmName, actualBmName);

        // Test with null bmName
        ProbeInformation nullBmNameProbeInformation = new ProbeInformation("targetName3", "targetType3", "bmClass3", null, "bmDescription3", "probeDescription3", 3, "xmlRpcUrl3");
        String nullExpectedBmName = null;
        String actualNullBmName = null;
        assertNull(actualNullBmName);
        assertNull(expectedBmName);
    }

    @Test
    public void testToString() {
        ProbeInformation probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        String expectedToString = "ProbeInformation{targetName='targetName1', targetType='targetType1', bmClass='bmClass1', bmName='bmName1', bmDescription='bmDescription1', probeDescription='probeDescription1', precision=1}";
        String actualToString = probeInformation.toString();
        assertEquals(expectedToString, actualToString);

        // Test with null probeInformation
        ProbeInformation nullProbeInformation = null;
        String actualNullToString = null;
        String expectedNullToString = null;
        assertNull(actualNullToString);
        assertNull(expectedNullToString);
    }

    // Add test cases for other methods and edge cases here

    @Test
    public void testEquals() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        boolean expectedEquals = true;
        boolean actualEquals = probeInformation1.equals(probeInformation2);
        assertEquals(expectedEquals, actualEquals);

        ProbeInformation probeInformation3 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName2", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        boolean expectedNotEquals = false;
        boolean actualNotEquals = probeInformation1.equals(probeInformation3);
        assertFalse(actualNotEquals);

        ProbeInformation probeInformation4 = new ProbeInformation("targetName1", "targetType2", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        boolean expectedNotEqualsDifferentTargetType = false;
        boolean actualNotEqualsDifferentTargetType = probeInformation1.equals(probeInformation4);
        assertFalse(actualNotEqualsDifferentTargetType);
    }

    @Test
    public void testHashCode() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        int expectedHashCode1 = 12345; // Arbitrary value for testing
        int actualHashCode1 = probeInformation1.hashCode();

        ProbeInformation probeInformation2 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        int expectedHashCode2 = expectedHashCode1;
        int actualHashCode2 = probeInformation2.hashCode();
        assertEquals(expectedHashCode1, actualHashCode1);
        assertEquals(expectedHashCode1, actualHashCode2);

        // Test with different values
        ProbeInformation probeInformation3 = new ProbeInformation("targetName2", "targetType1", "bmClass1", "bmName1", "bmDescription2", "probeDescription1", 1, "xmlRpcUrl2");
        int expectedHashCode3 = 67890; // Arbitrary value for testing
        int actualHashCode3 = probeInformation3.hashCode();
        assertNotEquals(expectedHashCode1, actualHashCode3);
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        String expectedMeasureURI = "http://example.com/targetType1/targetName1/bmClass1/bmName1";
        String actualMeasureURI = probeInformation.getMeasureURI();
        assertEquals(expectedMeasureURI, actualMeasureURI);
    }

    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        String expectedXmlRpcUrl = "xmlRpcUrl1";
        String actualXmlRpcUrl = probeInformation.getXmlRpcUrl();
        assertEquals(expectedXmlRpcUrl, actualXmlRpcUrl);

        // Test with null xmlRpcUrl
        ProbeInformation nullXmlRpcUrlProbeInformation = new ProbeInformation("targetName4", "targetType4", "bmClass4", "bmName4", "bmDescription4", "probeDescription4", 4, null);
        String expectedNullXmlRpcUrl = null;
        String actualNullXmlRpcUrl = null;
        assertEquals(expectedNullXmlRpcUrl, actualNullXmlRpcUrl);
    }
}