package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testProbeInformationConstructor() {
        String targetName = "target1";
        String targetType = "type1";
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
        String targetName = "target1";
        String targetType = "type1";
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
    public void testMeasureURI() {
        String targetType = "type1";
        String targetName = "target1";
        String bmClass = "bmClass1";
        String bmName = "bmName1";

        ProbeInformation probeInformation = new ProbeInformation("", targetType, bmClass, bmName, "", "", 1, "");
        assertEquals(Const.createMeasureURI(targetType, targetName, bmClass, bmName), probeInformation.getMeasureURI());
    }

    @Test
    public void testEquals() {
        String targetName1 = "target1";
        String targetName2 = "target2";
        String targetType1 = "type1";
        String targetType2 = "type2";
        String bmClass1 = "bmClass1";
        String bmClass2 = "bmClass2";
        String bmName1 = "bmName1";
        String bmName2 = "bmName2";
        String bmDescription1 = "bmDescription1";
        String bmDescription2 = "bmDescription2";
        String probeName1 = "probeName1";
        String probeName2 = "probeName2";
        int precision1 = 1;
        int precision2 = 2;
        String xmlRpcUrl1 = "xmlRpcUrl1";
        String xmlRpcUrl2 = "xmlRpcUrl2";

        ProbeInformation probeInformation1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        ProbeInformation probeInformation2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);

        assertFalse(probeInformation1.equals(probeInformation2));

        ProbeInformation probeInformation3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        assertTrue(probeInformation1.equals(probeInformation3));

        ProbeInformation probeInformation4 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        ProbeInformation probeInformation5 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        assertTrue(probeInformation4.equals(probeInformation5));

        ProbeInformation probeInformation6 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        probeInformation6.setPrecision(2);
        assertFalse(probeInformation1.equals(probeInformation6));

        ProbeInformation probeInformation7 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        probeInformation7.setBmClass("bmClass3");
        assertFalse(probeInformation1.equals(probeInformation7));

        ProbeInformation probeInformation8 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        probeInformation8.setBmName("bmName3");
        assertFalse(probeInformation1.equals(probeInformation8));

        ProbeInformation probeInformation9 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        probeInformation9.setBmDescription("bmDescription3");
        assertFalse(probeInformation1.equals(probeInformation9));

        ProbeInformation probeInformation10 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        probeInformation10.setProbeName("probeName3");
        assertFalse(probeInformation1.equals(probeInformation10));

        ProbeInformation probeInformation11 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        probeInformation11.setTargetName("target3");
        assertFalse(probeInformation1.equals(probeInformation11));

        ProbeInformation probeInformation12 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        probeInformation12.setTargetType("type3");
        assertFalse(probeInformation1.equals(probeInformation12));
    }

    @Test
    public void testHashCode() {
        String targetName = "target1";
        String targetType = "type1";
        String bmClass = "bmClass1";
        String bmName = "bmName1";
        String bmDescription = "bmDescription1";
        String probeName = "probeName1";
        int precision = 1;

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, "");

        int hashCode = probeInformation.hashCode();

        ProbeInformation probeInformation2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, "");

        assertEquals(hashCode, probeInformation2.hashCode());
    }

    @Test
    public void testToString() {
        String targetName = "target1";
        String targetType = "type1";
        String bmClass = "bmClass1";
        String bmName = "bmName1";
        String bmDescription = "bmDescription1";
        String probeName = "probeName1";
        int precision = 1;

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, "");

        assertEquals("ProbeInformation{targetName='" + targetName + "', targetType='" + targetType + "', bmClass='" + bmClass + "', bmName='" + bmName + "', bmDescription='" + bmDescription + "', probeDescription='" + probeName + "', precision=1}", probeInformation.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testToStringWithNullTargetName() {
        String targetType = "type1";
        String bmClass = "bmClass1";
        String bmName = "bmName1";
        String bmDescription = "bmDescription1";
        String probeName = "probeName1";
        int precision = 1;

        ProbeInformation probeInformation = new ProbeInformation(null, targetType, bmClass, bmName, bmDescription, probeName, precision, "");
    }

    @Test(expected = NullPointerException.class)
    public void testToStringWithNullTargetType() {
        String targetName = "target1";
        String bmClass = "bmClass1";
        String bmName = "bmName1";
        String bmDescription = "bmDescription1";
        String probeName = "probeName1";
        int precision = 1;

        ProbeInformation probeInformation = new ProbeInformation(targetName, null, bmClass, bmName, bmDescription, probeName, precision, "");
    }

    @Test(expected = NullPointerException.class)
    public void testToStringWithNullBmClass() {
        String targetName = "target1";
        String targetType = "type1";
        String bmName = "bmName1";
        String bmDescription = "bmDescription1";
        String probeName = "probeName1";
        int precision = 1;

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, null, bmName, bmDescription, probeName, precision, "");
    }

    @Test(expected = NullPointerException.class)
    public void testToStringWithNullBmName() {
        String targetName = "target1";
        String targetType = "type1";
        String bmClass = "bmClass1";
        String bmDescription = "bmDescription1";
        String probeName = "probeName1";
        int precision = 1;

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, null, bmDescription, probeName, precision, "");
    }

    @Test(expected = NullPointerException.class)
    public void testToStringWithNullBmDescription() {
        String targetName = "target1";
        String targetType = "type1";
        String bmClass = "bmClass1";
        String bmName = "bmName1";
        String probeName = "probeName1";
        int precision = 1;

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, null, probeName, precision, "");
    }

    @Test(expected = NullPointerException.class)
    public void testToStringWithNullProbeName() {
        String targetName = "target1";
        String targetType = "type1";
        String bmClass = "bmClass1";
        String bmName = "bmName1";
        String bmDescription = "bmDescription1";
        int precision = 1;

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, null, precision, "");
    }

    @Test
    public void testNullObject() {
        assertNull(new ProbeInformation(null, null, null, null, null, null, 0, null));
    }
}