package fi.vtt.noen.mfw.bundle.probe.shared.test;

import org.junit.Test;
import static org.junit.Assert.*;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {
    @Test
    public void testGetTargetType() {
        String targetType = "TestTargetType";
        ProbeInformation probeInfo = new ProbeInformation("TestTargetName", targetType, "TestBMClass", "TestBMName", "TestBMDescription", "TestProbeDescription", 1, "TestXmlRpcUrl");
        assertEquals(targetType, probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        String bmName = "TestBMName";
        ProbeInformation probeInfo = new ProbeInformation("TestTargetName", "TestTargetType", "TestBMClass", bmName, "TestBMDescription", "TestProbeDescription", 1, "TestXmlRpcUrl");
        assertEquals(bmName, probeInfo.getBmName());
    }

    @Test
    public void testConstructor() {
        String targetName = "TestTargetName";
        String targetType = "TestTargetType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeName = "TestProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "TestXmlRpcUrl";
        
        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
        
        assertEquals(targetName, probeInfo.getTargetName());
        assertEquals(targetType, probeInfo.getTargetType());
        assertEquals(bmClass, probeInfo.getBmClass());
        assertEquals(bmName, probeInfo.getBmName());
        assertEquals(bmDescription, probeInfo.getBmDescription());
        assertEquals(probeName, probeInfo.getProbeName());
        assertEquals(precision, probeInfo.getPrecision());
        assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());

        ProbeInformation probeInfoNull = new ProbeInformation(null, null, null, null, null, null, 0, null);
        assertNull(probeInfoNull.getTargetName());
        assertNull(probeInfoNull.getTargetType());
        assertNull(probeInfoNull.getBmClass());
        assertNull(probeInfoNull.getBmName());
        assertNull(probeInfoNull.getBmDescription());
        assertNull(probeInfoNull.getProbeName());
        assertEquals(0, probeInfoNull.getPrecision());
        assertNull(probeInfoNull.getXmlRpcUrl());
    }
}