package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {
    // ... other imports and class setup

    @Test
    public void testGetTargetType() {
        String targetType = "Sensor"; // Any valid value
        ProbeInformation probeInfo = new ProbeInformation("Precipitation", targetType, "Temperature", "AverageRainfall", "Average precipitation", "RainfallProbe", 2, "http://example.com/xmlrpc");
        assertEquals("Sensor", probeInfo.getTargetType());
    }
}