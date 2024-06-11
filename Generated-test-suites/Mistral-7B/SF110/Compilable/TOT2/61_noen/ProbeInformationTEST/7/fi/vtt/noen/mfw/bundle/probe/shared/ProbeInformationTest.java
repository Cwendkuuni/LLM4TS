package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        String targetType = "Sensor";
        ProbeInformation probeInfo = new ProbeInformation("Probe1", targetType, "Class1", "Name1", "Description1", "ProbeDescription1", 1, "http://localhost:8080/rpc");
        assertEquals(targetType, probeInfo.getTargetType());
    }

    // Repeat the test case for remaining methods with appropriate test data.
}