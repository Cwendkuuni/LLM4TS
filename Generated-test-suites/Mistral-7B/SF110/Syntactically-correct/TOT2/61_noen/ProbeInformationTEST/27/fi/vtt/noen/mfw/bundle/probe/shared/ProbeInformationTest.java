
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testConstructor() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeName = "probeName";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(targetName, probeInformation.getTargetName());
        assertEquals(targetType, probeInformation.getTargetType());
        assertEquals(bmClass, probeInformation.getBmClass());
        assertEquals(bmName, probeInformation.getBmName());
        assertEquals(bmDescription, probeInformation.getBmDescription());
        assertEquals(probeName, probeInformation.getProbeName());
        assertEquals(precision, probeInformation.getPrecision());
        assertEquals(bmClass + "." + bmName, probeInformation.getMeasureURI());
        assertEquals(xmlRpcUrl, probeInformation.getXmlRpcUrl());
        assertEquals(toString(probeInformation), "ProbeInformation{targetName='targetName', targetType='targetType', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeName', precision=1}");
    }

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 1, "xmlRpcUrl");
        String expectedTargetType = "targetType";
        String actualTargetType = probeInformation.getTargetType();
        assertEquals(expectedTargetType, actualTargetType);
    }

    @Test
    public void testGetBmName_null() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", null, "bmDescription", "probeName", 1, "xmlRpcUrl");
        assertNull(probeInformation.getBmName());
    }

    @Test
    public void testGetBmName_empty() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "", "bmDescription", "probeName", 1, "xmlRpcUrl");
        assertEquals("", probeInformation.getBmName());
    }

    @Test
    public void testGetBmName_longString() {
        String longString = "Long string that exceeds the maximum length limit.";
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", longString, "bmDescription", "probeName", 1, "xmlRpcUrl");
        // Test the length of the returned string and the toString method to check if the long string is correctly truncated.
    }

    // Test other getter methods in a similar way.

    @Test
    public void testEquals_null() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
        ProbeInformation probeInformation2 = null;
        assertFalse(probeInformation1.equals(probeInformation2));
    }

    @Test
    public void testEquals_differentClass() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
        Object notProbeInformation = new Object();
        assertFalse(probeInformation1.equals(notProbeInformation));
    }

    @Test
    public void testEquals_differentButEquivalentObjects() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
        assertTrue(probeInformation1.equals(probeInformation2));
    }

    @Test
    public void testHashCode_sameObjects() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
        int expectedHashCode = probeInformation1.hashCode();
        int actualHashCode = probeInformation2.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    private String toString(ProbeInformation probeInformation) {
        return "ProbeInformation{targetName='" + probeInformation.getTargetName() + "', targetType='" + probeInformation.getTargetType() + "', bmClass='" + probeInformation.getBmClass() + "', bmName='" + probeInformation.getBmName() + "', bmDescription='" + probeInformation.getBmDescription() + "', probeDescription='" + probeInformation.getProbeName() + "', precision=" + probeInformation.getPrecision() + '}';
    }

    // Add other test methods for the test cases for the other getter methods.

}
