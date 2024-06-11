package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        String targetType = "Sensor";
        ProbeInformation probeInformation = new ProbeInformation("Sensor1", targetType, "BM1", "BM1_Name", "BM1 Description", "Probe1", 5, "http://localhost:8080/xmlrpc");
        assertEquals(targetType, probeInformation.getTargetType());
    }
}