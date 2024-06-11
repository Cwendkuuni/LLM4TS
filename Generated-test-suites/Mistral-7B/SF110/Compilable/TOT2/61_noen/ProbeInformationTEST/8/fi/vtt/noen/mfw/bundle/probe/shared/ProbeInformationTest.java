package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("TestTargetName", "TestTargetType", "TestBMClass", "TestBMName", "TestBMDescription", "TestProbeName", 1, "TestXmlRpcUrl");
        assertEquals("TestTargetType", probeInfo.getTargetType());
    }
}