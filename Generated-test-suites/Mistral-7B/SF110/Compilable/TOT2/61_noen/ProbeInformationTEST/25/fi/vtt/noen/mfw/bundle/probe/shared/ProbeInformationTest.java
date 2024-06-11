package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        String targetType = "Sensor";
        ProbeInformation probeInfo = new ProbeInformation("Probe1", targetType, "ProbeClass1", "ProbeName1", "ProbeDescription1", "ProbeDescription1", 1, "http://localhost:8080/api");
        assertEquals(targetType, probeInfo.getTargetType());
    }

    // Edge case 1: Testing with null targetType
    @Test(expected = NullPointerException.class)
    public void testGetTargetTypeNullTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("Probe1", null, "ProbeClass1", "ProbeName1", "ProbeDescription1", "ProbeDescription1", 1, "http://localhost:8080/api");
    }
}