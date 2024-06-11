package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ProbeInformationTest {
    // ...

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertEquals("targetType1", probeInformation.getTargetType());
    }
}