import org.junit.Assert;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {
    // ... other test cases ...

    @Test
    public void testGetTargetType() {
        String targetType = "sensor";
        ProbeInformation probeInfo = new ProbeInformation("Probe 1", targetType, "BM1", "ProbeDescription1", "BM Description1", "5", "http://example.com/xmlrpc");
        String actualTargetType = probeInfo.getTargetType();
        Assert.assertEquals(targetType, actualTargetType);
    }
}