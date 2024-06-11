package fi.vtt.noen.mfw.bundle.probe.shared;

import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {
    @Test
    public void testGetTargetType() {
        String targetType = "Type1";
        ProbeInformation probe = new ProbeInformation("TargetName1", targetType, "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XMLRpcUrl1");
        assertEquals(targetType, probe.getTargetType());
    }

    @Test
    public void testGetBmName() {
        String bmName = "BMName2";
        ProbeInformation probe = new ProbeInformation("TargetName2", "TargetType2", "BMClass2", bmName, "BMDescription2", "ProbeDescription2", 2, "XMLRpcUrl2");
        assertEquals(bmName, probe.getBmName());
    }

    @Test
    public void testEquals() {
        String targetName1 = "TargetName1";
        String targetName2 = "TargetName2";
        String targetType = "Type1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeName = "ProbeDescription1";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl1";

        ProbeInformation probe1 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
        ProbeInformation probe2 = new ProbeInformation(targetName2, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertTrue(probe1.equals(probe2));
    }

    @Test
    public void testToString() {
        String targetName = "TargetName3";
        String targetType = "Type3";
        String bmClass = "BMClass3";
        String bmName = "BMName3";
        String bmDescription = "BMDescription3";
        String probeName = "ProbeDescription3";
        int precision = 3;
        String xmlRpcUrl = "XMLRpcUrl3";

        ProbeInformation probe = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        String expected = "ProbeInformation{targetName='" + targetName + "', targetType='" + targetType + "', bmClass='" + bmClass + "', bmName='" + bmName + "', bmDescription='" + bmDescription + "', probeDescription='" + probeName + "', precision=3}";

        assertEquals(expected, probe.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidProbeInformation() {
        String targetName = null;
        String targetType = null;
        String bmClass = null;
        String bmName = "BMName4";
        String bmDescription = null;
        String probeName = null;
        int precision = 4;
        String xmlRpcUrl = "XMLRpcUrl4";

        try {
            new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
        } catch (NullPointerException e) {
            fail("Expected IllegalStateException, but got NullPointerException");
        }

        ProbeInformation probe = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
    }
}