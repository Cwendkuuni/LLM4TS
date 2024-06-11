package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {
    // ... other imports and class setup

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeName1", 1, "XMLRpcUrl1");
        String expectedTargetType = "TargetType1";
        String actualTargetType = probeInfo.getTargetType();
        assertEquals(expectedTargetType, actualTargetType);
    }
}