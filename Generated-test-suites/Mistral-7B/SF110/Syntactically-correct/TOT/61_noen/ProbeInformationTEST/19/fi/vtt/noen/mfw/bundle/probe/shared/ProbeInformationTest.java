package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;
import org.junit.Test;

import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        String targetType = "webService";
        ProbeInformation probeInformation = new ProbeInformation("TestTarget", targetType, " BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "http://test.com/xmlRpcUrl");
        assertEquals(targetType, probeInformation.getTargetType());
    }

    @Test
    public void testGetBmName() {
        String bmName = "BMNameTest";
        ProbeInformation probeInformation = new ProbeInformation("TestTarget", "webService", " BMClass", bmName, "BMDescription", "ProbeDescription", 1, "http://test.com/xmlRpcUrl");
        assertEquals(bmName, probeInformation.getBmName());
    }

    @Test
    public void testGetProbeName() {
        String probeName = "ProbeNameTest";
        ProbeInformation probeInformation = new ProbeInformation("TestTarget", "webService", " BMClass", "BMName", probeName, "BMDescription", 1, "http://test.com/xmlRpcUrl");
        assertEquals(probeName, probeInformation.getProbeName());
    }
}