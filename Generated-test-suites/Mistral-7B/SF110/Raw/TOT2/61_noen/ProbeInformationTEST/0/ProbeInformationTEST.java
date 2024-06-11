import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        assertEquals("targetType", probeInfo.getTargetType());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullTargetName() {
        new ProbeInformation(null, "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullTargetType() {
        new ProbeInformation("targetName", null, "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
    }

    @Test
    public void testGetNullBmName() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", null, "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        assertEquals(null, probeInfo.getBmName());
    }

    // Add test cases for other methods and edge cases

    @Test
    public void testToString() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        assertEquals("ProbeInformation{targetName='targetName', targetType='targetType', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeDescription', precision=1}", probeInfo.toString());
    }

    @Test
    public void testEquals() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertTrue(probeInfo1.equals(probeInfo2));
    }

    @Test
    public void testHashCode() {
        ProbeInformation probeInfo1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInfo2 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertEquals(probeInfo1.hashCode(), probeInfo2.hashCode());
    }

    // Add test cases for other methods and edge cases

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        assertEquals("http://example.com/targetType/targetName/bmClass/bmName", probeInfo.getMeasureURI());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMeasureURIWithNullTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", null, "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        probeInfo.getMeasureURI();
    }

    // Add test cases for other methods and edge cases

    @Test
    public void testXmlRpcUrl() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "http://example.com/xmlRpcUrl");
        assertEquals("http://example.com/xmlRpcUrl", probeInfo.getXmlRpcUrl());
    }

    @Test(expected = NullPointerException.class)
    public void testXmlRpcUrlWithNullUrl() {
        ProbeInformation probeInfo = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, null);
    }

    // Add test cases for getter methods with non-matching types
}