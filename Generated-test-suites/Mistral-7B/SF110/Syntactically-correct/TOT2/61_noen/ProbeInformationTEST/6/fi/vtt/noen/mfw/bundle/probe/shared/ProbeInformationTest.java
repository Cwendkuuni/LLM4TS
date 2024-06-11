import org.junit.Test;
import static org.junit.Assert.*;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        String targetType = "sensor";
        ProbeInformation probeInformation = new ProbeInformation("Target1", targetType, "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        assertEquals(targetType, probeInformation.getTargetType());
        // Test edge cases and exception handling scenarios here
    }

    @Test
    public void testGetBmName() {
        String bmName = "BMName1";
        ProbeInformation probeInformation = new ProbeInformation("Target1", "TargetType1", "BMClass1", bmName, "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        assertEquals(bmName, probeInformation.getBmName());
        // Test edge cases and exception handling scenarios here
    }

    @Test
    public void testGetProbeName() {
        String probeName = "ProbeDescription1";
        ProbeInformation probeInformation = new ProbeInformation("Target1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", probeName, 1, "XmlRpcUrl1");
        assertEquals(probeName, probeInformation.getProbeName());
        // Test edge cases and exception handling scenarios here
    }

    @Test
    public void testGetTargetName() {
        String targetName = "Target1";
        ProbeInformation probeInformation = new ProbeInformation(targetName, "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        assertEquals(targetName, probeInformation.getTargetName());
        // Test edge cases and exception handling scenarios here
    }

    @Test
    public void testGetBmClass() {
        String bmClass = "BMClass1";
        ProbeInformation probeInformation = new ProbeInformation("Target1", "TargetType1", bmClass, "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        assertEquals(bmClass, probeInformation.getBmClass());
        // Test edge cases and exception handling scenarios here
    }

    @Test
    public void testGetBmDescription() {
        String bmDescription = "BMDescription1";
        ProbeInformation probeInformation = new ProbeInformation("Target1", "TargetType1", "BMClass1", "BMName1", bmDescription, "ProbeDescription1", 1, "XmlRpcUrl1");
        assertEquals(bmDescription, probeInformation.getBmDescription());
        // Test edge cases and exception handling scenarios here
    }

    @Test
    public void testGetPrecision() {
        int precision = 1;
        ProbeInformation probeInformation = new ProbeInformation("Target1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", precision, "XmlRpcUrl1");
        assertEquals(precision, probeInformation.getPrecision());
        // Test edge cases and exception handling scenarios here
    }

    @Test
    public void testGetMeasureURI() {
        String targetType = "sensor";
        String targetName = "Target1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        String expected = Const.createMeasureURI(targetType, targetName, bmClass, bmName);
        assertEquals(expected, probeInformation.getMeasureURI());
        // Test edge cases and exception handling scenarios here
    }

    @Test
    public void testGetXmlRpcUrl() {
        String xmlRpcUrl = "XmlRpcUrl1";
        ProbeInformation probeInformation = new ProbeInformation("Target1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, xmlRpcUrl);
        assertEquals(xmlRpcUrl, probeInformation.getXmlRpcUrl());
        // Test edge cases and exception handling scenarios here
    }

    @Test
    public void testToString() {
        String targetName = "Target1";
        String targetType = "sensor";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeDescription = "ProbeDescription1";
        int precision = 1;
        String xmlRpcUrl = "XmlRpcUrl1";
        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
        String expected = "ProbeInformation{targetName='Target1', targetType='sensor', bmClass='BMClass1', bmName='BMName1', bmDescription='BMDescription1', probeDescription='ProbeDescription1', precision=1}";
        assertEquals(expected, probeInformation.toString());
        // Test edge cases and exception handling scenarios here
    }

    @Test
    public void testEquals() {
        // Test equals method with same object
        ProbeInformation probeInformation1 = new ProbeInformation("Target1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        ProbeInformation probeInformation2 = probeInformation1;
        assertTrue(probeInformation1.equals(probeInformation2));
        // Test equals method with different objects
        ProbeInformation probeInformation3 = new ProbeInformation("Target1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        assertFalse(probeInformation1.equals(probeInformation3));
        // Test equals method with null object
        assertFalse(probeInformation1.equals(null));
        // Test equals method with different data types
        assertFalse(probeInformation1.equals(new Object()));
        // Test edge cases and exception handling scenarios here
    }

    @Test
    public void testHashCode() {
        String targetName = "Target1";
        String targetType = "sensor";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeDescription = "ProbeDescription1";
        int precision = 1;
        String xmlRpcUrl = "XmlRpcUrl1";
        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
        int expectedHashCode = targetName.hashCode() * 31 + targetType.hashCode() * 31 + bmClass.hashCode() * 31 + bmName.hashCode() * 31 + bmDescription.hashCode() * 31 + probeDescription.hashCode() * 31 + precision;
        int actualHashCode = probeInformation.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
        // Test edge cases and exception handling scenarios here
    }
}