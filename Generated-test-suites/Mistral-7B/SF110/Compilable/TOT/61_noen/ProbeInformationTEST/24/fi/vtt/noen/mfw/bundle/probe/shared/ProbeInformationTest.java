package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ProbeInformationTest {
    @Test
    public void testGetTargetType() {
        String targetType = "Sensor";
        ProbeInformation probeInformation = new ProbeInformation("MyTarget", targetType, "BMClassName", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRPCUrl");
        assertEquals(targetType, probeInformation.getTargetType());
    }
}