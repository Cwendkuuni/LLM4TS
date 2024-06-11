import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ProbeInformationTest {
    @Test
    public void testGetTargetType() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "xmlRpcUrl1");
        assertEquals("TargetType1", probeInformation.getTargetType());
    }
}