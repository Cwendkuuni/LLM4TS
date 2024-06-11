package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testProbeInformationConstructor() {
        String targetName = "target1";
        String targetType = "type1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeName = "ProbeName1";
        int precision = 1;
        String xmlRpcUrl = "http://test.com/xmlrpc";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(targetName, probeInfo.getTargetName());
        assertEquals(targetType, probeInfo.getTargetType());
        assertEquals(bmClass, probeInfo.getBmClass());
        assertEquals(bmName, probeInfo.getBmName());
        assertEquals(bmDescription, probeInfo.getBmDescription());
        assertEquals(probeName, probeInfo.getProbeName());
        assertEquals(precision, probeInfo.getPrecision());
        assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());
        assertEquals(probeInfo.toString(), "ProbeInformation{targetName='" + targetName + "', targetType='" + targetType + "', bmClass='" + bmClass + "', bmName='" + bmName + "', bmDescription='" + bmDescription + "', probeDescription='" + probeName + "', precision=1}");
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
        String xmlRpcUrl = "http://test.com/xmlrpc";

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
        String targetName = "target1";
        String targetType = "type1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, null, null, 1, null);

        assertEquals(Const.createMeasureURI(targetType, targetName, bmClass, bmName), probeInfo.getMeasureURI());
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
        String xmlRpcUrl1 = "http://test1.com/xmlrpc";
        String xmlRpcUrl2 = "http://test2.com/xmlrpc";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);
        ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        // Same object
        assertTrue(probeInfo1.equals(probeInfo1));

        // Same values
        assertTrue(probeInfo1.equals(probeInfo3));

        // Different targetName
        assertFalse(probeInfo1.equals(new ProbeInformation(targetName2, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1)));

        // Different targetType
        assertFalse(probeInfo1.equals(new ProbeInformation(targetName1, targetType2, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1)));

        // Different bmClass
        assertFalse(probeInfo1.equals(new ProbeInformation(targetName1, targetType1, bmClass2, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1)));

        // Different bmName
        assertFalse(probeInfo1.equals(new ProbeInformation(targetName1, targetType1, bmClass1, bmName2, bmDescription1, probeName1, precision1, xmlRpcUrl1)));

        // Different bmDescription
        assertFalse(probeInfo1.equals(new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, "BMDescription2", probeName1, precision1, xmlRpcUrl1)));

        // Different probeName
        assertFalse(probeInfo1.equals(new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, "ProbeName2", precision1, xmlRpcUrl1)));

        // Different precision
        assertFalse(probeInfo1.equals(new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, 2, xmlRpcUrl1)));

        // Different xmlRpcUrl
        assertFalse(probeInfo1.equals(new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, "http://test3.com/xmlrpc")));
    }

    @Test
    public void testHashCode() {
        String targetName = "target1";
        String targetType = "type1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeName = "ProbeName1";
        int precision = 1;

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, null);

        int hashCode = probeInfo.hashCode();

        assertEquals(hashCode, probeInfo.hashCode());

        ProbeInformation probeInfo2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, null);

        assertEquals(hashCode, probeInfo2.hashCode());

        String differentTargetName = "target2";

        ProbeInformation probeInfo3 = new ProbeInformation(differentTargetName, targetType, bmClass, bmName, bmDescription, probeName, precision, null);

        assertNotEquals(hashCode, probeInfo3.hashCode());

        String differentTargetType = "type2";

        ProbeInformation probeInfo4 = new ProbeInformation(targetName, differentTargetType, bmClass, bmName, bmDescription, probeName, precision, null);

        assertNotEquals(hashCode, probeInfo4.hashCode());

        String differentBmClass = "BMClass2";

        ProbeInformation probeInfo5 = new ProbeInformation(targetName, targetType, differentBmClass, bmName, bmDescription, probeName, precision, null);

        assertNotEquals(hashCode, probeInfo5.hashCode());

        String differentBmName = "BMName2";

        ProbeInformation probeInfo6 = new ProbeInformation(targetName, targetType, bmClass, differentBmName, bmDescription, probeName, precision, null);

        assertNotEquals(hashCode, probeInfo6.hashCode());

        String differentBmDescription = "BMDescription2";

        ProbeInformation probeInfo7 = new ProbeInformation(targetName, targetType, bmClass, bmName, differentBmDescription, probeName, precision, null);

        assertNotEquals(hashCode, probeInfo7.hashCode());

        String differentProbeName = "ProbeName2";

        ProbeInformation probeInfo8 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, differentProbeName, precision, null);

        assertNotEquals(hashCode, probeInfo8.hashCode());

        int differentPrecision = 2;

        ProbeInformation probeInfo9 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, differentPrecision, null);

        assertNotEquals(hashCode, probeInfo9.hashCode());
    }
}