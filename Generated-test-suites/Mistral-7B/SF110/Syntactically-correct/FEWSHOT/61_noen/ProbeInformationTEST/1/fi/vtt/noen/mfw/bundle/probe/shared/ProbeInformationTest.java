package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testProbeInformationConstructor() {
        String targetName = "targetName1";
        String targetType = "targetType1";
        String bmClass = "bmClass1";
        String bmName = "bmName1";
        String bmDescription = "bmDescription1";
        String probeName = "probeName1";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl1";

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
        String targetName = "targetName1";
        String targetType = "targetType1";
        String bmClass = "bmClass1";
        String bmName = "bmName1";
        String bmDescription = "bmDescription1";
        String probeName = "probeName1";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl1";

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
    public void testGetMeasureURI() {
        String targetType = "targetType1";
        String targetName = "targetName1";
        String bmClass = "bmClass1";
        String bmName = "bmName1";

        ProbeInformation probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", null, null, 1, null);

        assertEquals(Const.createMeasureURI(targetType, targetName, bmClass, bmName), probeInformation.getMeasureURI());
    }

    @Test
    public void testEquals() {
        String targetName1 = "targetName1";
        String targetType1 = "targetType1";
        String bmClass1 = "bmClass1";
        String bmName1 = "bmName1";
        String bmDescription1 = "bmDescription1";
        String probeName1 = "probeName1";
        int precision1 = 1;
        String xmlRpcUrl1 = "xmlRpcUrl1";

        ProbeInformation probeInformation1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        String targetName2 = "targetName2";
        String targetType2 = "targetType1";
        String bmClass2 = "bmClass2";
        String bmName2 = "bmName2";
        String bmDescription2 = "bmDescription2";
        String probeName2 = "probeName2";
        int precision2 = 1;
        String xmlRpcUrl2 = "xmlRpcUrl2";

        ProbeInformation probeInformation2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);

        ProbeInformation probeInformation3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        ProbeInformation probeInformation4 = new ProbeInformation(targetName2, targetType2, bmClass1, bmName2, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        ProbeInformation probeInformation5 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, null, probeName1, precision1, xmlRpcUrl1);

        ProbeInformation probeInformation6 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1,probeName2, precision1, xmlRpcUrl1);

        assertTrue(probeInformation1.equals(probeInformation3));
        assertNotEquals(probeInformation1, probeInformation2);
        assertNotEquals(probeInformation1, probeInformation4);
        assertNotEquals(probeInformation1, probeInformation5);
        assertNotEquals(probeInformation1, probeInformation6);
    }

    @Test
    public void testHashCode() {
        String targetName1 = "targetName1";
        String targetType1 = "targetType1";
        String bmClass1 = "bmClass1";
        String bmName1 = "bmName1";
        String bmDescription1 = "bmDescription1";
        String probeName1 = "probeName1";
        int precision1 = 1;
        String xmlRpcUrl1 = "xmlRpcUrl1";

        ProbeInformation probeInformation1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        String targetName2 = "targetName2";
        String targetType2 = "targetType2";
        String bmClass2 = "bmClass2";
        String bmName2 = "bmName2";
        String bmDescription2 = "bmDescription2";
        String probeName2 = "probeName2";
        int precision2 = 2;
        String xmlRpcUrl2 = "xmlRpcUrl2";

        ProbeInformation probeInformation2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);

        int hashCode1 = probeInformation1.hashCode();
        int hashCode2 = probeInformation2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        String targetName = "targetName1";
        String targetType = "targetType1";
        String bmClass = "bmClass1";
        String bmName = "bmName1";
        String bmDescription = "bmDescription1";
        String probeName = "probeName1";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl1";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals("ProbeInformation{targetName='" + targetName + "\', targetType='" + targetType + "\', bmClass='" + bmClass + "\', bmName='" + bmName + "\', bmDescription='" + bmDescription + "\', probeDescription='" + probeName + "\', precision=" + precision + "}", probeInformation.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testToStringWithNullTargetName() {
        String targetType = "targetType1";
        String bmClass = "bmClass1";
        String bmName = "bmName1";
        String bmDescription = "bmDescription1";
        String probeName = "probeName1";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl1";

        ProbeInformation probeInformation = new ProbeInformation(null, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        probeInformation.toString();
    }

    @Test(expected = NullPointerException.class)
    public void testToStringWithNullTargetType() {
        String targetName = "targetName1";
        String bmClass = "bmClass1";
        String bmName = "bmName1";
        String bmDescription = "bmDescription1";
        String probeName = "probeName1";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl1";

        ProbeInformation probeInformation = new ProbeInformation(targetName, null, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        probeInformation.toString();
    }

    @Test(expected = NullPointerException.class)
    public void testToStringWithNullBmClass() {
        String targetName = "targetName1";
        String targetType = "targetType1";
        String bmName = "bmName1";
        String bmDescription = "bmDescription1";
        String probeName = "probeName1";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl1";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, null, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        probeInformation.toString();
    }

    @Test(expected = NullPointerException.class)
    public void testToStringWithNullBmName() {
        String targetName = "targetName1";
        String targetType = "targetType1";
        String bmClass = "bmClass1";
        String bmDescription = "bmDescription1";
        String probeName = "probeName1";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl1";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, null, bmDescription, probeName, precision, xmlRpcUrl);

        probeInformation.toString();
    }

    @Test(expected = NullPointerException.class)
    public void testToStringWithNullBmDescription() {
        String targetName = "targetName1";
        String targetType = "targetType1";
        String bmClass = "bmClass1";
        String bmName = "bmName1";
        String probeName = "probeName1";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl1";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, null, probeName, precision, xmlRpcUrl);

        probeInformation.toString();
    }

    @Test(expected = NullPointerException.class)
    public void testToStringWithNullProbeName() {
        String targetName = "targetName1";
        String targetType = "targetType1";
        String bmClass = "bmClass1";
        String bmName = "bmName1";
        String bmDescription = "bmDescription1";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl1";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, null, precision, xmlRpcUrl);

        probeInformation.toString();
    }
}