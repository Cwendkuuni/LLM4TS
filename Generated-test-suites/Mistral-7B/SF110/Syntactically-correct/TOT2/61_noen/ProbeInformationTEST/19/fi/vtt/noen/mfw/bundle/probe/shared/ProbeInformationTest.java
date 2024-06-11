import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {
    @Test
    public void testGetTargetType() {
        String targetType = "Sensor";
        ProbeInformation probeInformation = new ProbeInformation("ProbeName", targetType, "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        assertEquals(targetType, probeInformation.getTargetType());
    }
}