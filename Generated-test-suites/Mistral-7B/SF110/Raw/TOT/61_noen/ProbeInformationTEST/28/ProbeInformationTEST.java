import static org.junit.Assert.assertEquals;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {
    // 
    @Test
    public void testGetTargetType() {
        String targetType = "TestTargetType";
        ProbeInformation probeInformation = new ProbeInformation("TestTargetName", targetType, "TestBMClass", "TestBMName", "TestBMDescription", "TestProbeDescription", 1, "TestXmlRpcUrl");
        assertEquals(targetType, probeInformation.getTargetType());
    }
    // 
}