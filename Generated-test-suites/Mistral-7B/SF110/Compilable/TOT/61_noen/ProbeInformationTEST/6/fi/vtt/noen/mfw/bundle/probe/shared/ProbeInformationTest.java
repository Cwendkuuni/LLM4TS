package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {
    @Test
    public void testGetTargetType() {
        String targetType = "Sensor";
        ProbeInformation probeInformation = new ProbeInformation("TestTarget", targetType, "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "http://test.com/xmlrpc");
        assertEquals("Sensor", probeInformation.getTargetType());
    }
}