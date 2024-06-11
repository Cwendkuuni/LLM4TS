
package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("Target1", "Type1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "xmlRpcUrl1");
        String expectedType = "Type1";
        String actualType = probeInfo.getTargetType();
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInfo = new ProbeInformation("Target1", "Type1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "xmlRpcUrl1");
        String expectedName = "BMName1";
        String actualName = probeInfo.getBmName();
        assertEquals(expectedName, actualName);

        ProbeInformation probeInfoNullBMName = new ProbeInformation("Target1", "Type1", "BMClass1", null, "BMDescription1", "ProbeDescription1", 1, "xmlRpcUrl1");
        String actualNullBMName = probeInfoNullBMName.getBmName();
        assertNull(actualNullBMName);

        ProbeInformation probeInfoEmptyBMName = new ProbeInformation("Target1", "Type1", "BMClass1", "", "BMDescription1", "ProbeDescription1", 1, "xmlRpcUrl1");
        String actualEmptyBMName = probeInfoEmptyBMName.getBmName();
        assertEquals("", actualEmptyBMName);
    }

    @Test
    public void testToString() {
        ProbeInformation probeInfo = new ProbeInformation("Target1", "Type1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "xmlRpcUrl1");
        String expectedString = "ProbeInformation{targetName='Target1', targetType='Type1', bmClass='BMClass1', bmName='BMName1', bmDescription='BMDescription1', probeDescription='ProbeDescription1', precision=1}";
        String actualString = probeInfo.toString();
        assertEquals(expectedString, actualString);
    }

    @Test(expected = NullPointerException.class)
    public void testGetBmDescriptionWithNullValue() {
        ProbeInformation probeInfo = new ProbeInformation("Target1", "Type1", "BMClass1", "BMName1", null, "ProbeDescription1", 1, "xmlRpcUrl1");
        String actualBmDescription = probeInfo.getBmDescription();
        fail("Expected NullPointerException but received " + actualBmDescription);
    }

    @Test
    public void testGetMeasureURIWithEmptyBMClass() {
        ProbeInformation probeInfo = new ProbeInformation("Target1", "Type1", "", "BMName1", "BMDescription1", "ProbeDescription1", 1, "xmlRpcUrl1");
        String expectedMeasureURI = "";
        String actualMeasureURI = probeInfo.getMeasureURI();
        assertEquals(expectedMeasureURI, actualMeasureURI);
    }

    // Add more test cases, including edge cases and error scenarios

}
