import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 5, "XmlRpcUrl");
        String expectedTargetType = "TargetType";
        String actualTargetType = probeInformation.getTargetType();
        assertEquals(expectedTargetType, actualTargetType);
    }
}