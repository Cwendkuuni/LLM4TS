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
        String xmlRpcUrl = "http://localhost:8080/xmlRpcUrl1";

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
    public void testGetTargetType() {
        String targetType = "type1";
        ProbeInformation probeInfo = new ProbeInformation("target1", targetType, "BMClass1", "BMName1", "BMDescription1", "ProbeName1", 1, "http://localhost:8080/xmlRpcUrl1");

        assertEquals(targetType, probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        String bmName = "BMName1";
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "BMClass1", bmName, "BMDescription1", "ProbeName1", 1, "http://localhost:8080/xmlRpcUrl1");

        assertEquals(bmName, probeInfo.getBmName());
    }

    @Test
    public void testGetProbeName() {
        String probeName = "ProbeName1";
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "BMClass1", "BMName1", "BMDescription1", probeName, 1, "http://localhost:8080/xmlRpcUrl1");

        assertEquals(probeName, probeInfo.getProbeName());
    }

    @Test
    public void testGetTargetName() {
        String targetName = "target1";
        ProbeInformation probeInfo = new ProbeInformation(targetName, "type1", "BMClass1", "BMName1", "BMDescription1", "ProbeName1", 1, "http://localhost:8080/xmlRpcUrl1");

        assertEquals(targetName, probeInfo.getTargetName());
    }

    @Test
    public void testGetBmClass() {
        String bmClass = "BMClass1";
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", bmClass, "BMName1", "BMDescription1", "ProbeName1", 1, "http://localhost:8080/xmlRpcUrl1");

        assertEquals(bmClass, probeInfo.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        String bmDescription = "BMDescription1";
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "BMClass1", "BMName1", bmDescription, "ProbeName1", 1, "http://localhost:8080/xmlRpcUrl1");

        assertEquals(bmDescription, probeInfo.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        int precision = 1;
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "BMClass1", "BMName1", "BMDescription1", "ProbeName1", precision, "http://localhost:8080/xmlRpcUrl1");

        assertEquals(precision, probeInfo.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        String targetType = "type1";
        String targetName = "target1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, "BMDescription1", "ProbeName1", 1, "http://localhost:8080/xmlRpcUrl1");

        assertEquals(Const.createMeasureURI(targetType, targetName, bmClass, bmName), probeInfo.getMeasureURI());
    }

    @Test
    public void testGetXmlRpcUrl() {
        String xmlRpcUrl = "http://localhost:8080/xmlRpcUrl1";
        ProbeInformation probeInfo = new ProbeInformation("target1", "type1", "BMClass1", "BMName1", "BMDescription1", "ProbeName1", 1, xmlRpcUrl);

        assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());
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
        String xmlRpcUrl = "http://localhost:8080/xmlRpcUrl1";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        String expected = "ProbeInformation{targetName='" + targetName + "', targetType='" + targetType + "', bmClass='" + bmClass + "', bmName='" + bmName + "', bmDescription='" + bmDescription + "', probeDescription='" + probeName + "', precision=1}";

        assertEquals(expected, probeInfo.toString());
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
        String xmlRpcUrl1 = "http://localhost:8080/xmlRpcUrl1";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        String targetName2 = "target2";
        String targetType2 = "type1";
        String bmClass2 = "BMClass1";
        String bmName2 = bmName1;
        String bmDescription2 = "BMDescription2";
        String probeName2 = probeName1;
        int precision2 = precision1;
        String xmlRpcUrl2 = xmlRpcUrl1;

        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);

        ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        ProbeInformation probeInfo4 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, null);

        ProbeInformation probeInfo5 = new ProbeInformation("target1", "type2", bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        ProbeInformation probeInfo6 = null;

        assertEquals(probeInfo1, probeInfo1);
        assertEquals(probeInfo1, probeInfo3);
        assertNotEquals(probeInfo1, probeInfo2);
        assertNotEquals(probeInfo1, probeInfo4);
        assertNotEquals(probeInfo1, probeInfo5);
        assertNotEquals(probeInfo1, probeInfo6);
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

        int hashCode1 = probeInfoHashCode("target1", "type1", "BMClass1", "BMName1", "BMDescription1", "ProbeName1", 1);
        ProbeInformation probeInfo1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, null);

        int hashCode2 = probeInfoHashCode("target2", "type1", "BMClass1", "BMName1", "BMDescription2", "ProbeName1", 1);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName, targetType, bmClass, bmName, "BMDescription2", probeName, precision, null);

        int hashCode3 = probeInfoHashCode("target1", "type1", "BMClass2", "BMName2", "BMDescription1", "ProbeName2", 2);
        ProbeInformation probeInfo3 = new ProbeInformation(targetName, targetType, "BMClass2", "BMName2", bmDescription, "ProbeName2", precision, null);

        assertEquals(hashCode1, probeInfo1.hashCode());
        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    private int probeInfoHashCode(String targetName, String targetType, String bmClass, String bmName, String bmDescription, String probeName, int precision) {
        int result = (targetName != null) ? targetName.hashCode() : 0;
        result = 31 * result + ((targetType != null) ? targetType.hashCode() : 0);
        result = 31 * result + ((bmClass != null) ? bmClass.hashCode() : 0);
        result = 31 * result + ((bmName != null) ? bmName.hashCode() : 0);
        result = 31 * result + ((bmDescription != null) ? bmDescription.hashCode() : 0);
        result = 31 * result + probeName.hashCode();
        result = 31 * result + precision;
        return result;
    }

    @Test
    public void testNullObject() {
        ProbeInformation probeInfo = null;
        assertNull(probeInfo);
    }
}