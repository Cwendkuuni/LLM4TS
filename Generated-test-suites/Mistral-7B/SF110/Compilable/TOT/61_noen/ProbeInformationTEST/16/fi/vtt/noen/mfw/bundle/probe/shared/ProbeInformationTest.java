package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {
    // ... other imports and setup code ...

    @Test
    public void testGetTargetType() {
        String targetType = "Sensor";
        ProbeInformation probeInfo = new ProbeInformation("TargetName1", targetType, "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XMLRPCUrl1");
        assertEquals(targetType, probeInfo.getTargetType());
    }
}