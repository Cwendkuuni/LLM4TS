package fi.vtt.noen.mfw.bundle.probe.shared.test;

import org.junit.Test;
import static org.junit.Assert.*;
import static fi.vtt.noen.mfw.bundle.common.Const.createMeasureURI;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        String targetType = "TestType";
        ProbeInformation probeInfo = new ProbeInformation("TestTarget", targetType, "TestBMClass", "TestBMName", "TestBMDescription", "TestProbeName", 1, "TestXMLRPCUrl");
        assertEquals(targetType, probeInfo.getTargetType());

        // Edge cases and exception handling
        String emptyTargetType = "";
        ProbeInformation emptyTargetTypeProbeInfo = new ProbeInformation("TestTarget", emptyTargetType, "TestBMClass", "TestBMName", "TestBMDescription", "TestProbeName", 1, "TestXMLRPCUrl");
        assertNotEquals(targetType, emptyTargetTypeProbeInfo.getTargetType());

        ProbeInformation nullTargetTypeProbeInfo = new ProbeInformation("TestTarget", null, "TestBMClass", "TestBMName", "TestBMDescription", "TestProbeName", 1, "TestXMLRPCUrl");
        assertNull(nullTargetTypeProbeInfo.getTargetType());

        ProbeInformation negativePrecisionProbeInfo = new ProbeInformation("TestTarget", targetType, "TestBMClass", "TestBMName", "TestBMDescription", "TestProbeName", -1, "TestXMLRPCUrl");
        assertNotNull(negativePrecisionProbeInfo.getTargetType());
        assertEquals(targetType, negativePrecisionProbeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        String bmName = "TestBMName";
        ProbeInformation probeInfo = new ProbeInformation("TestTarget", "TestTargetType", "TestBMClass", bmName, "TestBMDescription", "TestProbeName", 1, "TestXMLRPCUrl");
        assertEquals(bmName, probeInfo.getBmName());

        // Edge cases and exception handling
        String emptyBMName = "";
        ProbeInformation emptyBMNameProbeInfo = new ProbeInformation("TestTarget", "TestTargetType", "TestBMClass", emptyBMName, "TestBMDescription", "TestProbeName", 1, "TestXMLRPCUrl");
        assertNotEquals(bmName, emptyBMNameProbeInfo.getBmName());

        ProbeInformation nullBMNameProbeInfo = new ProbeInformation("TestTarget", "TestTargetType", "TestBMClass", null, "TestBMDescription", "TestProbeName", 1, "TestXMLRPCUrl");
        assertNull(nullBMNameProbeInfo.getBmName());

        ProbeInformation negativePrecisionProbeInfo = new ProbeInformation("TestTarget", "TestTargetType", "TestBMClass", bmName, "TestBMDescription", "TestProbeName", -1, "TestXMLRPCUrl");
        assertNotNull(negativePrecisionProbeInfo.getBmName());
        assertEquals(bmName, negativePrecisionProbeInfo.getBmName());
    }

    @Test
    public void testGetMeasureURI() {
        String targetType = "TestType";
        String targetName = "TestTarget";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, "TestBMDescription", "TestProbeName", 1, "TestXMLRPCUrl");
        String expected = createMeasureURI(targetType, targetName, bmClass, bmName);
        assertEquals(expected, probeInfo.getMeasureURI());

        // Edge cases and exception handling
        ProbeInformation nullTargetTypeProbeInfo = new ProbeInformation(null, targetType, bmClass, bmName, "TestBMDescription", "TestProbeName", 1, "TestXMLRPCUrl");
        assertNull(nullTargetTypeProbeInfo.getMeasureURI());

        ProbeInformation emptyTargetNameProbeInfo = new ProbeInformation(targetName, targetType, bmClass, "", "TestBMDescription", "TestProbeName", 1, "TestXMLRPCUrl");
        assertNotNull(emptyTargetNameProbeInfo.getMeasureURI());
        assertNotEquals(expected, emptyTargetNameProbeInfo.getMeasureURI());

        ProbeInformation emptyBMClassProbeInfo = new ProbeInformation(targetName, targetType, "", bmName, "TestBMDescription", "TestProbeName", 1, "TestXMLRPCUrl");
        assertNotNull(emptyBMClassProbeInfo.getMeasureURI());
        assertNotEquals(expected, emptyBMClassProbeInfo.getMeasureURI());

        ProbeInformation emptyBMNameProbeInfo = new ProbeInformation(targetName, targetType, bmClass, "", "TestBMDescription", "TestProbeName", 1, "TestXMLRPCUrl");
        assertNotNull(emptyBMNameProbeInfo.getMeasureURI());
        assertNotEquals(expected, emptyBMNameProbeInfo.getMeasureURI());

        ProbeInformation negativePrecisionProbeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, "TestBMDescription", "TestProbeName", -1, "TestXMLRPCUrl");
        assertNotNull(negativePrecisionProbeInfo.getMeasureURI());
        assertEquals(expected, negativePrecisionProbeInfo.getMeasureURI());
    }
}