import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRPCUrl");
        String expectedTargetType = "TargetType";
        String actualTargetType = probeInformation.getTargetType();
        Assertions.assertEquals(expectedTargetType, actualTargetType);
    }

    // Repeat the above test case for each public method
}