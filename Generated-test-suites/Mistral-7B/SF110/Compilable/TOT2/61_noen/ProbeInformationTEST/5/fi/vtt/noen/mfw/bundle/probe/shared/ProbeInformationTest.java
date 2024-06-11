package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        String targetType = "TestType";
        ProbeInformation probeInfo = new ProbeInformation("TestTargetName", targetType, "TestBMClass", "TestBMName", "TestBMDescription", "TestProbeName", 1, "TestXMLRPCUrl");
        assertEquals(targetType, probeInfo.getTargetType());
    }
}