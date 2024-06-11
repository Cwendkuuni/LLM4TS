import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {
    // ...
    
    @Test
    public void testGetTargetType() {
        String targetType = "testType";
        ProbeInformation probeInformation = new ProbeInformation("testTargetName", targetType, "testBMClass", "testBMName", "testBMDescription", "testProbeName", 1, "testXmlRpcUrl");
        assertEquals(targetType, probeInformation.getTargetType());
    }
}