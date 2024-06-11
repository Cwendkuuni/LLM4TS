package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        String targetType = "Sensor";
        ProbeInformation probeInformation = new ProbeInformation("Probe1", targetType, "BM1", "Measure1", "Description1", "Probe1Description", 2, "http://localhost/rpc");
        assertEquals(targetType, probeInformation.getTargetType());
    }
}