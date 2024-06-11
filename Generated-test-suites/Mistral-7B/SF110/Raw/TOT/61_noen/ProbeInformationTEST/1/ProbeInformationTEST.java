
package fi.vtt.noen.mfw.bundle.probe.shared.test;

import org.junit.Test;
import static org.junit.Assert.*;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        String targetType = "Sensor";
        ProbeInformation probeInformation = new ProbeInformation("TestTarget", targetType, "BM1", "BM1Name", "BM1Description", "Probe1", 1, "http://localhost:8080/xmlrpc");
        assertEquals(targetType, probeInformation.getTargetType());
    }

    @Test
    public void testGetBmName() {
        String bmName = "BM1Name";
        ProbeInformation probeInformation = new ProbeInformation("TestTarget", "Sensor", "BM1", bmName, "BM1Description", "Probe1", 1, "http://localhost:8080/xmlrpc");
        assertEquals(bmName, probeInformation.getBmName());
    }

    @Test
    public void testToString() {
        String expectedString = "ProbeInformation{targetName='TestTarget', targetType='Sensor', bmClass='BM1', bmName='BM1Name', bmDescription='BM1Description', probeDescription='Probe1', precision=1}";
        ProbeInformation probeInformation = new ProbeInformation("TestTarget", "Sensor", "BM1", "BM1Name", "BM1Description", "Probe1", 1, "http://localhost:8080/xmlrpc");
        assertEquals(expectedString, probeInformation.toString());
    }
}
