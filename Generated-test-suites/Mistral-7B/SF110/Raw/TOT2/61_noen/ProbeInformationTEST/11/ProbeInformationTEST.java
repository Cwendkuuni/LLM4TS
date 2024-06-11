package fi.vtt.noen.mfw.bundle.probe.shared.test;

import org.junit.Test;
import static org.junit.Assert.*;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInfo = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        String expected = "targetType1";
        String actual = probeInfo.getTargetType();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetBmNameWithEmptyString() {
        ProbeInformation probeInfo = new ProbeInformation("targetName1", "targetType1", "bmClass1", "", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
        String expected = "";
        String actual = probeInfo.getBmName();
        assertEquals(expected, actual);
    }

    // Test for other methods: getProbeName(), getTargetName(), getBmClass(), getBmDescription(), getPrecision(), getMeasureURI(), getXmlRpcUrl(), toString(), equals(), hashCode()

    @Test(expected = NullPointerException.class)
    public void testNullBmName() {
        ProbeInformation probeInfo = new ProbeInformation("targetName1", "targetType1", "bmClass1", null, "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
    }

    // Test for other methods: getTargetType(), getBmName(), getProbeName(), getTargetName(), getBmClass(), getBmDescription(), getPrecision(), getXmlRpcUrl(), toString(), equals(), hashCode()

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPrecision() {
        ProbeInformation probeInfo = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", -1, "xmlRpcUrl1");
    }

    // Test for other methods: getTargetType(), getBmName(), getProbeName(), getTargetName(), getBmClass(), getBmDescription(), getXmlRpcUrl(), toString(), equals(), hashCode()

    // Test additional edge cases, boundary values and other error scenarios
}