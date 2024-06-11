import org.junit.Assert;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInformation = new ProbeInformation("testTargetName", "testTargetType", "testBMClass", "testBMName", "testBMDescription", "testProbeName", 1, "testXmlRpcUrl");
        String expectedTargetType = "testTargetType";
        String actualTargetType = probeInformation.getTargetType();
        Assert.assertEquals(expectedTargetType, actualTargetType);
    }

    // ... other test cases for other methods go here
}