package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {
    @Test
    public void testGetTargetType() {
        String targetType = "Sensor";
        ProbeInformation probeInformation = new ProbeInformation("Probe1", targetType, "SensorClass", "SensorName", "SensorDescription", "ProbeDescription", 2, "http://localhost:8080/probeData");
        assertEquals(targetType, probeInformation.getTargetType());
    }
    
    @Test
    public void testGetBmName() {
        String bmName = "Temperature";
        ProbeInformation probeInformation = new ProbeInformation("Probe1", "Sensor", "SensorClass", bmName, "SensorDescription", "ProbeDescription", 2, "http://localhost:8080/probeData");
        assertEquals(bmName, probeInformation.getBmName());
    }
    
    @Test
    public void testToString() {
        String expected = "ProbeInformation{targetName='Probe1', targetType='Sensor', bmClass='SensorClass', bmName='Temperature', bmDescription='SensorDescription', probeDescription='ProbeDescription', precision=2, xmlRpcUrl='http://localhost:8080/probeData'}";
        ProbeInformation probeInformation = new ProbeInformation("Probe1", "Sensor", "SensorClass", "Temperature", "SensorDescription", "ProbeDescription", 2, "http://localhost:8080/probeData");
        assertEquals(expected, probeInformation.toString());
    }
    
    // Additional test cases for edge cases and exception handling scenarios
}