import org.junit.Test;
import static org.junit.Assert.*;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        String targetType = "Sensor";
        ProbeInformation probeInformation = new ProbeInformation("Probe1", targetType, "SensorClass", "ProbeBMName", "Probe BM Description", "Probe Description", 2, "http://localhost:8080/RPC");
        String actualTargetType = probeInformation.getTargetType();
        assertEquals(targetType, actualTargetType);
    }

    @Test(expected = NullPointerException.class)
    public void testGetTargetTypeWithNullTargetType() {
        ProbeInformation probeInformation = new ProbeInformation(null, null, "SensorClass", "ProbeBMName", "Probe BM Description", "Probe Description", 2, "http://localhost:8080/RPC");
        probeInformation.getTargetType();
    }

    @Test
    public void testGetTargetTypeWithEmptyTargetType() {
        String targetType = "";
        ProbeInformation probeInformation = new ProbeInformation("Probe1", targetType, "SensorClass", "ProbeBMName", "Probe BM Description", "Probe Description", 2, "http://localhost:8080/RPC");
        String actualTargetType = probeInformation.getTargetType();
        assertEquals("", actualTargetType);
    }

    @Test
    public void testGetBmName() {
        String bmName = "ProbeBMName";
        ProbeInformation probeInformation = new ProbeInformation("Probe1", null, "SensorClass", bmName, "Probe BM Description", "Probe Description", 2, "http://localhost:8080/RPC");
        String actualBmName = probeInformation.getBmName();
        assertEquals(bmName, actualBmName);
    }

    // Add test cases for other methods in the ProbeInformation class

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullTargetName() {
        String targetType = "Sensor";
        String bmClass = "SensorClass";
        String bmName = null;
        String bmDescription = "Probe BM Description";
        String probeDescription = "Probe Description";
        int precision = 2;
        String xmlRpcUrl = "http://localhost:8080/RPC";
        ProbeInformation probeInformation = new ProbeInformation(null, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    // Add test cases for other potential edge cases and exception handling scenarios

    @Test
    public void testToString() {
        String targetName = "Probe1";
        String targetType = "Sensor";
        String bmClass = "SensorClass";
        String bmName = "ProbeBMName";
        String bmDescription = "Probe BM Description";
        String probeDescription = "Probe Description";
        int precision = 2;
        String xmlRpcUrl = "http://localhost:8080/RPC";
        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
        String expectedToString = "ProbeInformation{targetName='Probe1', targetType='Sensor', bmClass='SensorClass', bmName='ProbeBMName', bmDescription='Probe BM Description', probeDescription='Probe Description', precision=2}";
        String actualToString = probeInformation.toString();
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testEquals() {
        ProbeInformation probeInformation1 = new ProbeInformation("Probe1", "Sensor", "SensorClass", "ProbeBMName", "Probe BM Description", "Probe Description", 2, "http://localhost:8080/RPC");
        ProbeInformation probeInformation2 = new ProbeInformation("Probe1", "Sensor", "SensorClass", "ProbeBMName", "Probe BM Description", "Probe Description", 2, "http://localhost:8080/RPC");
        ProbeInformation probeInformation3 = new ProbeInformation("Probe2", "Sensor", "SensorClass", "ProbeBMName", "Probe BM Description", "Probe Description", 2, "http://localhost:8080/RPC");
        assertTrue(probeInformation1.equals(probeInformation2));
        assertFalse(probeInformation1.equals(probeInformation3));
    }

    @Test
    public void testHashCode() {
        ProbeInformation probeInformation1 = new ProbeInformation("Probe1", "Sensor", "SensorClass", "ProbeBMName", "Probe BM Description", "Probe Description", 2, "http://localhost:8080/RPC");
        ProbeInformation probeInformation2 = new ProbeInformation("Probe1", "Sensor", "SensorClass", "ProbeBMName", "Probe BM Description", "Probe Description", 2, "http://localhost:8080/RPC");
        ProbeInformation probeInformation3 = new ProbeInformation("Probe2", "Sensor", "SensorClass", "ProbeBMName", "Probe BM Description", "Probe Description", 2, "http://localhost:8080/RPC");
        int hashcode1 = probeInformation1.hashCode();
        int hashcode2 = probeInformation2.hashCode();
        int hashcode3 = probeInformation3.hashCode();
        assertEquals(hashcode1, hashcode2);
        assertNotEquals(hashcode1, hashcode3);
    }
}