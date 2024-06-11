package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testConstructor() {
        String targetName = "target1";
        String targetType = "type1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeName = "ProbeName1";
        int precision = 1;
        String xmlRpcUrl = "http://test.com/url";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(targetName, probeInfo.getTargetName());
        assertEquals(targetType, probeInfo.getTargetType());
        assertEquals(bmClass, probeInfo.getBmClass());
        assertEquals(bmName, probeInfo.getBmName());
        assertEquals(bmDescription, probeInfo.getBmDescription());
        assertEquals(probeName, probeInfo.getProbeName());
        assertEquals(precision, probeInfo.getPrecision());
        assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());
    }

    @Test
    public void testGetters() {
        String targetName = "target1";
        String targetType = "type1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeName = "ProbeName1";
        int precision = 1;
        String xmlRpcUrl = "http://test.com/url";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(targetName, probeInfo.getTargetName());
        assertEquals(targetType, probeInfo.getTargetType());
        assertEquals(bmClass, probeInfo.getBmClass());
        assertEquals(bmName, probeInfo.getBmName());
        assertEquals(bmDescription, probeInfo.getBmDescription());
        assertEquals(probeName, probeInfo.getProbeName());
        assertEquals(precision, probeInfo.getPrecision());
        assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());
    }

    @Test
    public void testGetMeasureURI() {
        String targetType = "type1";
        String targetName = "target1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";

        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "BMClass1", "BMName1", null, null, 1, null);

        String measureURI = probeInfo.getMeasureURI();

        assertEquals(Const.createMeasureURI(targetType, targetName, bmClass, bmName), measureURI);
    }

    @Test
    public void testEquals() {
        String targetName1 = "target1";
        String targetName2 = "target2";
        String targetType1 = "type1";
        String targetType2 = "type2";
        String bmClass1 = "BMClass1";
        String bmClass2 = "BMClass2";
        String bmName1 = "BMName1";
        String bmName2 = "BMName2";
        String bmDescription1 = "BMDescription1";
        String bmDescription2 = "BMDescription2";
        String probeName1 = "ProbeName1";
        String probeName2 = "ProbeName2";
        int precision1 = 1;
        int precision2 = 2;
        String xmlRpcUrl1 = "http://test.com/url1";
        String xmlRpcUrl2 = "http://test.com/url2";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);

        ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        // Test equality with same object
        assertTrue(probeInfo1.equals(probeInfo1));

        // Test equality with same values
        ProbeInformation probeInfo4 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        assertTrue(probeInfo1.equals(probeInfo4));

        // Test inequality with different values
        assertNotEquals(probeInfo1, probeInfo2);
        assertNotEquals(probeInfo1, probeInfo3);
        assertNotEquals(probeInfo1, new Object());

        // Test hashCode
        int hashCode1 = probeInfo1.hashCode();
        int hashCode2 = probeInfo2.hashCode();
        int hashCode3 = probeInfo3.hashCode();

        // Test that equal objects have the same hash code
        assertEquals(hashCode1, hashCode3);
        assertEquals(hashCode1, probeInfo1.hashCode());

        // Test that different objects have different hash codes
        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode2, hashCode3);
    }

    @Test
    public void testToString() {
        String targetName = "target1";
        String targetType = "type1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeName = "ProbeName1";
        int precision = 1;
        String xmlRpcUrl = "http://test.com/url";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        String expected = "ProbeInformation{targetName='target1', targetType='type1', bmClass='BMClass1', bmName='BMName1', bmDescription='BMDescription1', probeDescription='ProbeName1', precision=1}";

        assertEquals(expected, probeInfo.toString());
    }

    @Test
    public void testNullTargetName() {
        String targetType = "type1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeName = "ProbeName1";
        int precision = 1;
        String xmlRpcUrl = "http://test.com/url";

        ProbeInformation probeInfo = new ProbeInformation(null, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertNull(probeInfo.getTargetName());
    }

    @Test
    public void testNullTargetType() {
        String targetName = "target1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeName = "ProbeName1";
        int precision = 1;
        String xmlRpcUrl = "http://test.com/url";

        ProbeInformation probeInfo = new ProbeInformation(targetName, null, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertNull(probeInfo.getTargetType());
    }

    @Test
    public void testNullBmClass() {
        String targetName = "target1";
        String targetType = "type1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeName = "ProbeName1";
        int precision = 1;
        String xmlRpcUrl = "http://test.com/url";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, null, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertNull(probeInfo.getBmClass());
    }

    @Test
    public void testNullBmName() {
        String targetName = "target1";
        String targetType = "type1";
        String bmClass = "BMClass1";
        String bmDescription = "BMDescription1";
        String probeName = "ProbeName1";
        int precision = 1;
        String xmlRpcUrl = "http://test.com/url";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, null, bmDescription, probeName, precision, xmlRpcUrl);

        assertNull(probeInfo.getBmName());
    }

    @Test
    public void testNullBmDescription() {
        String targetName = "target1";
        String targetType = "type1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String probeName = "ProbeName1";
        int precision = 1;
        String xmlRpcUrl = "http://test.com/url";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, null, probeName, precision, xmlRpcUrl);

        assertNull(probeInfo.getBmDescription());
    }

    @Test
    public void testNullProbeName() {
        String targetName = "target1";
        String targetType = "type1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        int precision = 1;
        String xmlRpcUrl = "http://test.com/url";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, null, precision, xmlRpcUrl);

        assertNull(probeInfo.getProbeName());
    }
}