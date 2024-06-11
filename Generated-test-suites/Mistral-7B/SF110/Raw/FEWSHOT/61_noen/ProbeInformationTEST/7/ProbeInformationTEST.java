package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testConstructor() {
        String targetName1 = "target1";
        String targetType1 = "type1";
        String bmClass1 = "BMClass1";
        String bmName1 = "BMName1";
        String bmDescription1 = "BMDescription1";
        String probeName1 = "ProbeName1";
        int precision1 = 1;
        String xmlRpcUrl1 = "http://localhost:8080/xmlrpc";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        assertEquals(targetName1, probeInfo1.getTargetName());
        assertEquals(targetType1, probeInfo1.getTargetType());
        assertEquals(bmClass1, probeInfo1.getBmClass());
        assertEquals(bmName1, probeInfo1.getBmName());
        assertEquals(bmDescription1, probeInfo1.getBmDescription());
        assertEquals(probeName1, probeInfo1.getProbeName());
        assertEquals(precision1, probeInfo1.getPrecision());
        assertEquals(xmlRpcUrl1, probeInfo1.getXmlRpcUrl());

        String targetName2 = "target2";
        String targetType2 = "type2";
        String bmClass2 = "BMClass2";
        String bmName2 = "BMName2";
        String bmDescription2 = "BMDescription2";
        String probeName2 = "ProbeName2";
        int precision2 = 2;
        String xmlRpcUrl2 = "http://localhost:8080/xmlrpc";

        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);

        assertNotEquals(probeInfo1, probeInfo2);

        ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        assertEquals(probeInfo1, probeInfo3);

        ProbeInformation probeInfo4 = new ProbeInformation();

        assertNull(probeInfo4.getTargetName());
        assertNull(probeInfo4.getTargetType());
        assertNull(probeInfo4.getBmClass());
        assertNull(probeInfo4.getBmName());
        assertNull(probeInfo4.getBmDescription());
        assertNull(probeInfo4.getProbeName());
        assertEquals(0, probeInfo4.getPrecision());
        assertNull(probeInfo4.getXmlRpcUrl());
    }

    @Test
    public void testGetters() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeName = "ProbeName";
        int precision = 1;
        String xmlRpcUrl = "http://localhost:8080/xmlrpc";

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
        String targetType = "targetType";
        String targetName = "targetName";
        String bmClass = "BMClass";
        String bmName = "BMName";

        ProbeInformation probeInfo = new ProbeInformation("targetName", targetType, bmClass, bmName, "", "", 1, "");

        assertEquals(Const.createMeasureURI(targetType, targetName, bmClass, bmName), probeInfo.getMeasureURI());
    }

    @Test
    public void testToString() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeName = "ProbeName";
        int precision = 1;
        String xmlRpcUrl = "http://localhost:8080/xmlrpc";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals("ProbeInformation{targetName='" + targetName + "'"
                + ", targetType='" + targetType + "'"
                + ", bmClass='" + bmClass + "'"
                + ", bmName='" + bmName + "'"
                + ", bmDescription='" + bmDescription + "'"
                + ", probeDescription='" + probeName + "'"
                + ", precision=" + precision + '}'
                , probeInfo.toString());
    }

    @Test
    public void testEquals() {
        String targetName1 = "target1";
        String targetType1 = "type1";
        String bmClass1 = "BMClass1";
        String bmName1 = "BMName1";
        String bmDescription1 = "BMDescription1";
        String probeName1 = "ProbeName1";
        int precision1 = 1;
        String xmlRpcUrl1 = "http://localhost:8080/xmlrpc";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        String targetName2 = "target2";
        String targetType2 = "type1";
        String bmClass2 = "BMClass1";
        String bmName2 = "BMName2";
        String bmDescription2 = "BMDescription2";
        String probeName2 = "ProbeName1";
        int precision2 = 1;
        String xmlRpcUrl2 = "http://localhost:8080/xmlrpc";

        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);

        ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        ProbeInformation probeInfo4 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        ProbeInformation probeInfo5 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName2, precision1, xmlRpcUrl1);

        assertEquals(probeInfo1, probeInfo3);
        assertEquals(probeInfo1, probeInfo4);
        assertNotEquals(probeInfo1, probeInfo2);
        assertNotEquals(probeInfo1, probeInfo5);
    }

    @Test
    public void testHashCode() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeName = "ProbeName";
        int precision = 1;

        int hashCode1 = probeInfoHashCode(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision);

        String targetName2 = "targetName2";
        String targetType2 = "targetType2";
        String bmClass2 = "BMClass2";
        String bmName2 = "BMName2";
        String bmDescription2 = "BMDescription2";
        String probeName2 = "ProbeName2";
        int precision2 = 2;

        int hashCode2 = probeInfoHashCode(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2);

        assertEquals(hashCode1, probeInfo1.hashCode());
        assertEquals(hashCode2, probeInfo2.hashCode());
        assertNotEquals(hashCode1, hashCode2);
    }

    private int probeInfoHashCode(String targetName, String targetType, String bmClass, String bmName, String bmDescription, String probeName, int precision) {
        int result = (targetName != null) ? targetName.hashCode() : 0;
        result = 31 * result + (targetType != null ? targetType.hashCode() : 0);
        result = 31 * result + (bmClass != null ? bmClass.hashCode() : 0);
        result = 31 * result + (bmName != null ? bmName.hashCode() : 0);
        result = 31 * result + (bmDescription != null ? bmDescription.hashCode() : 0);
        result = 31 * result + probeName.hashCode();
        result = 31 * result + precision;
        return result;
    }
}