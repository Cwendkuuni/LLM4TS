package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @Test
    public void testConstructor() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeName = "probeName";
        int precision = 5;
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

    @Test(expected = NullPointerException.class)
    public void testConstructorNullTargetName() {
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeName = "probeName";
        int precision = 5;
        String xmlRpcUrl = "xmlRpcUrl";

        new ProbeInformation(null, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test
    public void testGetTargetType() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        assertEquals("targetType", probeInformation.getTargetType());
    }

    @Test
    public void testGetBmName() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        assertEquals("bmName", probeInformation.getBmName());
    }

    @Test
    public void testGetProbeName() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        assertEquals("probeName", probeInformation.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        assertEquals("targetName", probeInformation.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        assertEquals("bmClass", probeInformation.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        assertEquals("bmDescription", probeInformation.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        assertEquals(5, probeInformation.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        String targetType = "targetType";
        String targetName = "targetName";
        String bmClass = "bmClass";
        String bmName = "bmName";
        ProbeInformation probeInformation = new ProbeInformation("targetName", targetType, bmClass, bmName, null, null, 5, null);
        assertEquals(Const.createMeasureURI(targetType, targetName, bmClass, bmName), probeInformation.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        assertEquals("xmlRpcUrl", probeInformation.getXmlRpcUrl());
    }

    @Test
    public void testToString() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        String expected = "ProbeInformation{targetName='targetName', targetType='targetType', bmClass='bmClass', bmName='bmName', bmDescription='bmDescription', probeDescription='probeName', precision=5}";
        assertEquals(expected, probeInformation.toString());
    }

    @Test
    public void testEqualsNull() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        assertNotEquals(null, probeInformation);
    }

    @Test
    public void testEqualsSameInstance() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        ProbeInformation probeInformation2 = probeInformation1;
        assertEquals(probeInformation1, probeInformation2);
    }

    @Test
    public void testEqualsDifferentInstances() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 5, "xmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName2", "targetType2", "bmClass2", "bmName2", "bmDescription2", "probeName2", 5, "xmlRpcUrl2");
        assertNotEquals(probeInformation1, probeInformation2);
    }

    @Test
    public void testEqualsSameValues() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        assertEquals(probeInformation1, probeInformation2);
    }

    @Test
    public void testEqualsDifferentValues() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 5, "xmlRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName2", "targetType2", "bmClass2", "bmName2", "bmDescription2", "probeName2", 5, "xmlRpcUrl2");
        assertNotEquals(probeInformation1, probeInformation2);
    }

    @Test
    public void testEqualsDifferentPrecision() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 6, "xmlRpcUrl");
        assertNotEquals(probeInformation1, probeInformation2);
    }

    @Test
    public void testEqualsDifferentBmClass() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass1", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass2", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        assertNotEquals(probeInformation1, probeInformation2);
    }

    @Test
    public void testEqualsDifferentBmName() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName1", "bmDescription", "probeName", 5, "xmlRpcUrl");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName2", "bmDescription", "probeName", 5, "xmlRpcUrl");
        assertNotEquals(probeInformation1, probeInformation2);
    }

    @Test
    public void testEqualsDifferentBmDescription() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription1", "probeName", 5, "xmlRpcUrl");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription2", "probeName", 5, "xmlRpcUrl");
        assertNotEquals(probeInformation1, probeInformation2);
    }

    @Test
    public void testEqualsDifferentProbeName() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName1", 5, "xmlRpcUrl");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName2", 5, "xmlRpcUrl");
        assertNotEquals(probeInformation1, probeInformation2);
    }

    @Test
    public void testEqualsDifferentTargetName() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName2", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        assertNotEquals(probeInformation1, probeInformation2);
    }

    @Test
    public void testEqualsDifferentTargetType() {
        ProbeInformation probeInformation1 = new ProbeInformation("targetName", "targetType1", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        ProbeInformation probeInformation2 = new ProbeInformation("targetName", "targetType2", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        assertNotEquals(probeInformation1, probeInformation2);
    }

    @Test
    public void testHashCode() {
        ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 5, "xmlRpcUrl");
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeName = "probeName";
        int precision = 5;
        int result = probeInformation.hashCode();
        int expected = targetName.hashCode() * 31 + targetType.hashCode() * 31 + bmClass.hashCode() * 31 + bmName.hashCode() * 31 + bmDescription.hashCode() * 31 + probeName.hashCode() * 31 + precision;
        assertEquals(expected, result);
    }
}