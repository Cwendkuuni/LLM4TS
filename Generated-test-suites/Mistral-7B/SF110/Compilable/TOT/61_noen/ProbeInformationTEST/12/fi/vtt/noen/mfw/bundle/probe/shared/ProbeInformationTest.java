package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ProbeInformationTest {
    // ... other imports and test setup

    @Test
    public void testGetTargetType() {
        String expectedTargetType = "expectedTargetType";
        ProbeInformation probeInformation = new ProbeInformation("targetName", expectedTargetType, "bmClass", "bmName", "bmDescription", "probeDescription", 1, "xmlRpcUrl");
        String actualTargetType = probeInformation.getTargetType();

        assertEquals(expectedTargetType, actualTargetType);
    }
}