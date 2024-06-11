import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        String expectedTargetType = "WebService";
        ProbeInformation probeInformation = new ProbeInformation("TestTarget", expectedTargetType, "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        String actualTargetType = probeInformation.getTargetType();
        assertEquals(expectedTargetType, actualTargetType);
    }
}