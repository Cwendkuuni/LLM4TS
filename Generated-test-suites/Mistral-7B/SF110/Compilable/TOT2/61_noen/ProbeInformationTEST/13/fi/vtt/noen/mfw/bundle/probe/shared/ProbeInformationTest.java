package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        String targetType = "Sensor";
        ProbeInformation probeInformation = new ProbeInformation("Probe1", targetType, "BM1", "Parameter1", "Description1", "ProbeDescription1", 2, "http://localhost:8080/xmlrpc");
        assertEquals(targetType, probeInformation.getTargetType());
    }

    @Test
    public void testGetBmName() {
        String bmName = "Parameter1";
        ProbeInformation probeInformation = new ProbeInformation("Probe1", "Sensor", "BM1", bmName, "Description1", "ProbeDescription1", 2, "http://localhost:8080/xmlrpc");
        assertEquals(bmName, probeInformation.getBmName());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullTargetName() {
        String targetType = "Sensor";
        String bmClass = "BM1";
        String bmName = "Parameter1";
        String bmDescription = "Description1";
        String probeDescription = "ProbeDescription1";
        int precision = 2;
        String xmlRpcUrl = "http://localhost:8080/xmlrpc";
        ProbeInformation probeInformation = new ProbeInformation(null, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    // Add test cases for other methods, edge cases, and exceptions here

    @Test
    public void testToString() {
        String expected = "ProbeInformation{targetName='Probe1', targetType='Sensor', bmClass='BM1', bmName='Parameter1', bmDescription='Description1', probeDescription='ProbeDescription1', precision=2}";
        ProbeInformation probeInformation = new ProbeInformation("Probe1", "Sensor", "BM1", "Parameter1", "Description1", "ProbeDescription1", 2, "http://localhost:8080/xmlrpc");
        assertEquals(expected, probeInformation.toString());
    }

    @Test
    public void testEquals() {
        ProbeInformation probeInformation1 = new ProbeInformation("Probe1", "Sensor", "BM1", "Parameter1", "Description1", "ProbeDescription1", 2, "http://localhost:8080/xmlrpc");
        ProbeInformation probeInformation2 = new ProbeInformation("Probe1", "Sensor", "BM1", "Parameter1", "Description1", "ProbeDescription1", 2, "http://localhost:8080/xmlrpc");
        assertTrue(probeInformation1.equals(probeInformation2));

        ProbeInformation probeInformation3 = new ProbeInformation("Probe1", "Sensor", "BM1", "Parameter1", "Description1", "ProbeDescription1", 3, "http://localhost:8080/xmlrpc");
        assertFalse(probeInformation1.equals(probeInformation3));

        ProbeInformation probeInformation4 = null;
        assertFalse(probeInformation1.equals(probeInformation4));
    }

    @Test
    public void testHashCode() {
        String targetName1 = "Probe1";
        String targetType1 = "Sensor";
        String bmClass1 = "BM1";
        String bmName1 = "Parameter1";
        String bmDescription1 = "Description1";
        String probeDescription1 = "ProbeDescription1";
        int precision1 = 2;
        String xmlRpcUrl1 = "http://localhost:8080/xmlrpc";
        ProbeInformation probeInformation1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);

        String targetName2 = "Probe2";
        String targetType2 = "Sensor";
        String bmClass2 = "BM1";
        String bmName2 = "Parameter1";
        String bmDescription2 = "Description1";
        String probeDescription2 = "ProbeDescription1";
        int precision2 = 2;
        String xmlRpcUrl2 = "http://localhost:8080/xmlrpc";
        ProbeInformation probeInformation2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeDescription2, precision2, xmlRpcUrl2);

        int hashCode1 = probeInformation1.hashCode();
        int hashCode2 = probeInformation2.hashCode();

        assertEquals(hashCode1, hashCode2);

        String targetName3 = "Probe1";
        String targetType3 = "Actuator";
        String bmClass3 = "BM1";
        String bmName3 = "Parameter1";
        String bmDescription3 = "Description1";
        String probeDescription3 = "ProbeDescription1";
        int precision3 = 2;
        String xmlRpcUrl3 = "http://localhost:8080/xmlrpc";
        ProbeInformation probeInformation3 = new ProbeInformation(targetName3, targetType3, bmClass3, bmName3, bmDescription3, probeDescription3, precision3, xmlRpcUrl3);

        int hashCode3 = probeInformation1.hashCode();
        int hashCode4 = probeInformation3.hashCode();

        assertNotEquals(hashCode1, hashCode3);
    }
}