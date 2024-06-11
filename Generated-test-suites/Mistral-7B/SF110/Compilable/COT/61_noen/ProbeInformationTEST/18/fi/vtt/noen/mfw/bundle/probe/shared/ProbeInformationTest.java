package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;

import fi.vtt.noen.mfw.bundle.common.Const;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testConstructor() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeName = "probeName";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(targetName, probeInformation.getTargetName());
        assertEquals(targetType, probeInformation.getTargetType());
        assertEquals(bmClass, probeInformation.getBmClass());
        assertEquals(bmName, probeInformation.getBmName());
        assertEquals(bmDescription, probeInformation.getBmDescription());
        assertEquals(probeName, probeInformation.getProbeName());
        assertEquals(precision, probeInformation.getPrecision());
        assertEquals(xmlRpcUrl, probeInformation.getXmlRpcUrl());
    }

    @Test
    public void testGetters() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeName = "probeName";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(targetName, probeInformation.getTargetName());
        assertEquals(targetType, probeInformation.getTargetType());
        assertEquals(bmClass, probeInformation.getBmClass());
        assertEquals(bmName, probeInformation.getBmName());
        assertEquals(bmDescription, probeInformation.getBmDescription());
        assertEquals(probeName, probeInformation.getProbeName());
        assertEquals(precision, probeInformation.getPrecision());
        assertEquals(xmlRpcUrl, probeInformation.getXmlRpcUrl());
    }

    @Test
    public void testToString() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeName = "probeName";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        String expected = "ProbeInformation{targetName='" + targetName + "', targetType='" + targetType + "', bmClass='" + bmClass + "', bmName='" + bmName + "', bmDescription='" + bmDescription + "', probeDescription='" + probeName + "', precision=1}";

        assertEquals(expected, probeInformation.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqualsNull() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 1, "xmlRpcUrl");

        assertNotEquals(null, probeInformation);
    }

    @Test
    public void testEqualsSameInstance() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 1, "xmlRpcUrl");

        assertEquals(probeInformation, probeInformation);
    }

    @Test
    public void testEqualsDifferentInstance() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName2", "targetType2", "bmClass2", "bmName2", "bmDescription2", "probeName2", 1, "xmlRpcUrl2");

        assertNotEquals(probeInformation1, probeInformation2);
    }

    @Test
    public void testEqualsSameValues() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 1, "xmlRpcUrl");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 1, "xmlRpcUrl");

        assertEquals(probeInformation1, probeInformation2);
    }

    @Test
    public void testHashCodeSameValues() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 1, "xmlRpcUrl");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 1, "xmlRpcUrl");

        assertEquals(probeInformation1.hashCode(), probeInformation2.hashCode());
    }

    @Test
    public void testHashCodeDifferentValues() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName2", "targetType2", "bmClass2", "bmName2", "bmDescription2", "probeName2", 1, "xmlRpcUrl2");

        assertNotEquals(probeInformation1.hashCode(), probeInformation2.hashCode());
    }

    @Test
    public void testGetMeasureURINullBmClass() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", null, "bmName", "bmDescription", "probeName", 1, "xmlRpcUrl");

        assertNull(probeInformation.getMeasureURI());
    }

    @Test
    public void testGetMeasureURIEmptyBmClass() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "", "bmName", "bmDescription", "probeName", 1, "xmlRpcUrl");

        assertEquals("", probeInformation.getMeasureURI());
    }

    @Test
    public void testGetMeasureURIValid() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 1, "xmlRpcUrl");

        String expected = Const.createMeasureURI("targetType", "targetName", "bmClass", "bmName");

        assertEquals(expected, probeInformation.getMeasureURI());
    }
}