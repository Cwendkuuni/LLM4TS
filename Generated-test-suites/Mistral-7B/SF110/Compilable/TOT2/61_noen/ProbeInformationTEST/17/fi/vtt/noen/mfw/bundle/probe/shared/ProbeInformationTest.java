package fi.vtt.noen.mfw.bundle.probe.shared.test;

import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {
    @Test
    public void testGetTargetType() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XmlRpcUrl");
        String expectedTargetType = "TargetType";
        String actualTargetType = probeInformation.getTargetType();
        assertEquals(expectedTargetType, actualTargetType);
    }

    @Test
    public void testGetTargetType_EmptyTargetType() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XmlRpcUrl");
        String expectedTargetType = "";
        String actualTargetType = probeInformation.getTargetType();
        assertEquals(expectedTargetType, actualTargetType);
    }

    @Test(expected = NullPointerException.class)
    public void testGetTargetType_NullTargetType() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", null, "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XmlRpcUrl");
        String actualTargetType = probeInformation.getTargetType();
        fail("Expected NullPointerException but got: " + actualTargetType);
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XmlRpcUrl");
        String expectedBmName = "BMName";
        String actualBmName = probeInformation.getBmName();
        assertEquals(expectedBmName, actualBmName);
    }

    // Add test cases for other methods here...

    @Test
    public void testToString() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XmlRpcUrl");
        String expectedToString = "ProbeInformation{targetName='TargetName', targetType='TargetType', bmClass='BMClass', bmName='BMName', bmDescription='BMDescription', probeDescription='ProbeDescription', precision=1}";
        String actualToString = probeInformation.toString();
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testEquals() {
        ProbeInformation probeInformation1 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        boolean expectedEquals = true;
        boolean actualEquals = probeInformation1.equals(probeInformation2);
        assertEquals(expectedEquals, actualEquals);
    }

    @Test
    public void testHashCode() {
        ProbeInformation probeInformation1 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("TargetName2", "TargetType2", "BMClass2", "BMName2", "BMDescription2", "ProbeDescription2", 2, "XmlRpcUrl2");
        int expectedHashCode1 = 12345; // Arbitrary hashcode value for test
        int expectedHashCode2 = 54321; // Arbitrary hashcode value for test
        int actualHashCode1 = probeInformation1.hashCode();
        int actualHashCode2 = probeInformation2.hashCode();
        assertEquals(expectedHashCode1, actualHashCode1);
        assertEquals(expectedHashCode2, actualHashCode2);
    }
}