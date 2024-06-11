import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {
    @Test
    public void testGetTargetType() {
        String targetType = "testTargetType";
        ProbeInformation probeInformation = new ProbeInformation("targetName", targetType, "bmClass", "bmName", "bmDescription", "probeName", 1, "xmlRpcUrl");
        assertEquals(targetType, probeInformation.getTargetType());
    }
}