package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertEquals("targetType1", probeInformation.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        assertEquals("bmName1", probeInformation.getBmName());
    }

    @Test
    public void testGetProbeName() {
        // Add test case for getProbeName() method here
    }

    @Test
    public void testGetTargetName() {
        // Add test case for getTargetName() method here
    }

    @Test
    public void testGetBmClass() {
        // Add test case for getBmClass() method here
    }

    @Test
    public void testGetBmDescription() {
        // Add test case for getBmDescription() method here
    }

    @Test
    public void testGetPrecision() {
        // Add test case for getPrecision() method here
    }

    @Test
    public void testGetMeasureURI() {
        // Add test case for getMeasureURI() method here
    }

    @Test
    public void testGetXmlRpcUrl() {
        // Add test case for getXmlRpcUrl() method here
    }

    @Test
    public void testToString() {
        ProbeInformation probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        String expected = "ProbeInformation{targetName='targetName1', targetType='targetType1', bmClass='bmClass1', bmName='bmName1', bmDescription='bmDescription1', probeDescription='probeDescription1', precision=1}";
        assertEquals(expected, probeInformation.toString());
    }

    @Test
    public void testEquals() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInformation3 = new ProbeInformation("targetName1", "targetType2", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInformation4 = new ProbeInformation("targetName2", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");

        assertEquals(probeInformation1, probeInformation1);
        assertEquals(probeInformation1, probeInformation2);
        assertNotEquals(probeInformation1, probeInformation3);
        assertNotEquals(probeInformation1, probeInformation4);
    }

    @Test
    public void testHashCode() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInformation3 = new ProbeInformation("targetName1", "targetType2", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        ProbeInformation probeInformation4 = new ProbeInformation("targetName2", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");

        int hashCode1 = probeInformation1.hashCode();
        int hashCode2 = probeInformation2.hashCode();
        int hashCode3 = probeInformation3.hashCode();
        int hashCode4 = probeInformation4.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
    }
}