package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 1, "XmlRpcUrl");
        String expected = "TargetType";
        String actual = probeInfo.getTargetType();
        assertEquals(expected, actual);
    }
}