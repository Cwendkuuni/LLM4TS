import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        String targetType = probeInformation.getTargetType();
        assertEquals("targetType", targetType);
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        String bmName = probeInformation.getBmName();
        assertEquals("bmName", bmName);
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 1, "xmlRpcUrl");
        String probeName = probeInformation.getProbeName();
        assertEquals("probeName", probeName);
    }

    @Test
    public void testGetTargetName() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        String targetName = probeInformation.getTargetName();
        assertEquals("targetName", targetName);
    }

    @Test
    public void testGetBmClass() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        String bmClass = probeInformation.getBmClass();
        assertEquals("bmClass", bmClass);
    }

    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        String bmDescription = probeInformation.getBmDescription();
        assertEquals("bmDescription", bmDescription);
    }

    @Test
    public void testGetPrecision() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        int precision = probeInformation.getPrecision();
        assertEquals(1, precision);
    }

    @Test (expected = NullPointerException.class)
    public void testGetTargetTypeWithNullTargetType() {
        ProbeInformation probeInformation = new ProbeInformation(null, "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
    }

    @Test (expected = NullPointerException.class)
    public void testGetBmNameWithNullBmName() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", null, "bmDescription", "probeDescription", 1, "xmlRpcUrl");
    }

    @Test (expected = NullPointerException.class)
    public void testGetProbeNameWithNullProbeName() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", null, 1, "xmlRpcUrl");
    }

    @Test (expected = NullPointerException.class)
    public void testGetTargetNameWithNullTargetName() {
        ProbeInformation probeInformation = new ProbeInformation(null, "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
    }

    @Test (expected = NullPointerException.class)
    public void testGetBmClassWithNullBmClass() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", null, "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
    }

    @Test (expected = NullPointerException.class)
    public void testGetBmDescriptionWithNullBmDescription() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", null, "probeDescription", 1, "xmlRpcUrl");
    }

    @Test
    public void testGetMeasureURI() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        String measureURI = probeInformation.getMeasureURI();
        String expectedMeasureURI = Const.createMeasureURI("targetType", "targetName", "bmClass", "bmName");
        assertEquals(expectedMeasureURI, measureURI);
    }

    @Test
    public void testToString() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        String toStringResult = probeInformation.toString();
        String expectedToStringResult = "ProbeInformation{targetName='targetName', targetType='targetType', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeDescription', precision=1}";
        assertEquals(expectedToStringResult, toStringResult);
    }

    @Test
    public void testEquals() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertTrue(probeInformation1.equals(probeInformation2));
    }

    @Test
    public void testEqualsWithDifferentTargetName() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName2", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertFalse(probeInformation1.equals(probeInformation2));
    }

    @Test
    public void testEqualsWithDifferentTargetType() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName1", "targetType2", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertFalse(probeInformation1.equals(probeInformation2));
    }

    @Test
    public void testEqualsWithDifferentBmClass() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName1", "targetType1", "bmClass2", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertFalse(probeInformation1.equals(probeInformation2));
    }

    @Test
    public void testEqualsWithDifferentBmName() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName2", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertFalse(probeInformation1.equals(probeInformation2));
    }

    @Test
    public void testEqualsWithDifferentBmDescription() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription2", "probeDescription1", 1, "xmlRpcUrl1");
        assertFalse(probeInformation1.equals(probeInformation2));
    }

    @Test
    public void testEqualsWithDifferentProbeName() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription2", 1, "xmlRpcUrl1");
        assertFalse(probeInformation1.equals(probeInformation2));
    }

    @Test
    public void testEqualsWithDifferentPrecision() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 2, "xmlRpcUrl1");
        assertFalse(probeInformation1.equals(probeInformation2));
    }

    @Test
    public void testHashCode() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        int hashCode = probeInformation.hashCode();
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeDescription = "probeDescription";
        int precision = 1;
        int expectedHashCode = targetName.hashCode() * 31 + targetType.hashCode() * 31 + bmClass.hashCode() * 31 + bmName.hashCode() * 31 + bmDescription.hashCode() * 31 + probeDescription.hashCode() * 31 + precision;
        assertEquals(expectedHashCode, hashCode);
    }
}