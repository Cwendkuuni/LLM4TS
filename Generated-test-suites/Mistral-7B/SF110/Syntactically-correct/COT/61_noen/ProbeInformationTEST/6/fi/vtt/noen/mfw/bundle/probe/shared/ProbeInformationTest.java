package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;

import org.junit.Test;

import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        String targetType = "Sensor";
        ProbeInformation probeInformation = new ProbeInformation("Target1", targetType, "BM1", "BMName1", "BMDescription1", "Probe1", 1, "XMLRPCUrl1");
        assertEquals(targetType, probeInformation.getTargetType());
    }

    @Test
    public void testGetBmName() {
        String bmName = "BMName1";
        ProbeInformation probeInformation = new ProbeInformation("Target1", "Sensor", "BM1", bmName, "BMDescription1", "Probe1", 1, "XMLRPCUrl1");
        assertEquals(bmName, probeInformation.getBmName());
    }

    @Test
    public void testGetProbeName() {
        String probeName = "Probe1";
        ProbeInformation probeInformation = new ProbeInformation("Target1", "Sensor", "BM1", "BMName1", "BMDescription1", probeName, 1, "XMLRPCUrl1");
        assertEquals(probeName, probeInformation.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        String targetName = "Target1";
        ProbeInformation probeInformation = new ProbeInformation(targetName, "Sensor", "BM1", "BMName1", "BMDescription1", "Probe1", 1, "XMLRPCUrl1");
        assertEquals(targetName, probeInformation.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        String bmClass = "BM1";
        ProbeInformation probeInformation = new ProbeInformation("Target1", "Sensor", bmClass, "BMName1", "BMDescription1", "Probe1", 1, "XMLRPCUrl1");
        assertEquals(bmClass, probeInformation.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        String bmDescription = "BMDescription1";
        ProbeInformation probeInformation = new ProbeInformation("Target1", "Sensor", "BM1", "BMName1", bmDescription, "Probe1", 1, "XMLRPCUrl1");
        assertEquals(bmDescription, probeInformation.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        int precision = 1;
        ProbeInformation probeInformation = new ProbeInformation("Target1", "Sensor", "BM1", "BMName1", "BMDescription1", "Probe1", precision, "XMLRPCUrl1");
        assertEquals(precision, probeInformation.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        String targetType = "Sensor";
        String targetName = "Target1";
        String bmClass = "BM1";
        String bmName = "BMName1";
        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, "BMDescription1", "Probe1", 1, "XMLRPCUrl1");
        String expectedMeasureURI = Const.createMeasureURI(targetType, targetName, bmClass, bmName);
        assertEquals(expectedMeasureURI, probeInformation.getMeasureURI());
    }

    @Test(expected = NullPointerException.class)
    public void testGetTargetTypeNull() {
        ProbeInformation probeInformation = new ProbeInformation("Target1", null, "BM1", "BMName1", "BMDescription1", "Probe1", 1, "XMLRPCUrl1");
    }

    @Test(expected = NullPointerException.class)
    public void testGetBmNameNull() {
        ProbeInformation probeInformation = new ProbeInformation("Target1", "Sensor", null, null, "BMDescription1", "Probe1", 1, "XMLRPCUrl1");
    }

    @Test(expected = NullPointerException.class)
    public void testGetProbeNameNull() {
        ProbeInformation probeInformation = new ProbeInformation("Target1", "Sensor", "BM1", null, "BMDescription1", null, 1, "XMLRPCUrl1");
    }

    @Test(expected = NullPointerException.class)
    public void testGetBmDescriptionNull() {
        ProbeInformation probeInformation = new ProbeInformation("Target1", "Sensor", "BM1", "BMName1", null, "Probe1", 1, "XMLRPCUrl1");
    }

    @Test
    public void testEquals() {
        ProbeInformation probeInformation1 = new ProbeInformation("Target1", "Sensor", "BM1", "BMName1", "BMDescription1", "Probe1", 1, "XMLRPCUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("Target1", "Sensor", "BM1", "BMName1", "BMDescription1", "Probe1", 1, "XMLRPCUrl1");
        assertTrue(probeInformation1.equals(probeInformation2));

        ProbeInformation probeInformation3 = new ProbeInformation("Target1", "Sensor", "BM1", "BMName1", "BMDescription1", "Probe2", 1, "XMLRPCUrl1");
        assertFalse(probeInformation1.equals(probeInformation3));

        ProbeInformation probeInformation4 = new ProbeInformation("Target2", "Sensor", "BM1", "BMName1", "BMDescription1", "Probe1", 1, "XMLRPCUrl1");
        assertFalse(probeInformation1.equals(probeInformation4));

        ProbeInformation probeInformation5 = new ProbeInformation("Target1", "Sensor", "BM2", "BMName1", "BMDescription1", "Probe1", 1, "XMLRPCUrl1");
        assertFalse(probeInformation1.equals(probeInformation5));

        ProbeInformation probeInformation6 = new ProbeInformation("Target1", "Sensor", "BM1", "BMName2", "BMDescription1", "Probe1", 1, "XMLRPCUrl1");
        assertFalse(probeInformation1.equals(probeInformation6));

        ProbeInformation probeInformation7 = new ProbeInformation("Target1", "Sensor", "BM1", "BMName1", "BMDescription2", "Probe1", 1, "XMLRPCUrl1");
        assertFalse(probeInformation1.equals(probeInformation7));

        ProbeInformation probeInformation8 = new ProbeInformation("Target1", "Sensor", "BM1", "BMName1", "BMDescription1", "Probe1", 2, "XMLRPCUrl1");
        assertFalse(probeInformation1.equals(probeInformation8));
    }

    @Test
    public void testHashCode() {
        ProbeInformation probeInformation1 = new ProbeInformation("Target1", "Sensor", "BM1", "BMName1", "BMDescription1", "Probe1", 1, "XMLRPCUrl1");
        String targetName1 = probeInformation1.getTargetName();
        String targetType1 = probeInformation1.getTargetType();
        String bmClass1 = probeInformation1.getBmClass();
        String bmName1 = probeInformation1.getBmName();
        String bmDescription1 = probeInformation1.getBmDescription();
        String probeName1 = probeInformation1.getProbeName();
        int precision1 = probeInformation1.getPrecision();
        int hashCode1 = probeInformation1.hashCode();

        ProbeInformation probeInformation2 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, "XMLRPCUrl2");
        assertEquals(hashCode1, probeInformation2.hashCode());

        ProbeInformation probeInformation3 = new ProbeInformation("Target2", "Sensor", "BM1", "BMName1", "BMDescription1", "Probe1", 1, "XMLRPCUrl1");
        int hashCode2 = probeInformation3.hashCode();
        assertNotEquals(hashCode1, hashCode2);

        ProbeInformation probeInformation4 = new ProbeInformation("Target1", "Sensor", "BM2", "BMName1", "BMDescription1", "Probe1", 1, "XMLRPCUrl1");
        int hashCode3 = probeInformation4.hashCode();
        assertNotEquals(hashCode1, hashCode3);

        ProbeInformation probeInformation5 = new ProbeInformation("Target1", "Sensor", "BM1", "BMName2", "BMDescription1", "Probe1", 1, "XMLRPCUrl1");
        int hashCode4 = probeInformation5.hashCode();
        assertNotEquals(hashCode1, hashCode4);

        ProbeInformation probeInformation6 = new ProbeInformation("Target1", "Sensor", "BM1", "BMName1", "BMDescription2", "Probe1", 1, "XMLRPCUrl1");
        int hashCode5 = probeInformation6.hashCode();
        assertNotEquals(hashCode1, hashCode5);

        ProbeInformation probeInformation7 = new ProbeInformation("Target1", "Sensor", "BM1", "BMName1", "BMDescription1", "Probe1", 2, "XMLRPCUrl1");
        int hashCode6 = probeInformation7.hashCode();
        assertNotEquals(hashCode1, hashCode6);
    }
}