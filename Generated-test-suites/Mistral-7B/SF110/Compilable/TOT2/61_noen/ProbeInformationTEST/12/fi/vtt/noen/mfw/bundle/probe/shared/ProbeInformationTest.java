package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {
    @Test
    public void testGetTargetType() {
        String expectedTargetType = "testType";
        ProbeInformation probeInfo = new ProbeInformation("testTargetName", expectedTargetType, "testBMClass", "testBMName", "testBMDescription", "testProbeDescription", 1, "testXmlRpcUrl");
        String actualTargetType = probeInfo.getTargetType();
        assertEquals(expectedTargetType, actualTargetType);
    }

    // Edge cases and exceptional scenarios testing will be added in the next steps.
}