
package fi.vtt.noen.mfw.bundle.probe.shared;

import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {
    @Test
    public void testGetTargetType() {
        String targetType = "TestTargetType";
        ProbeInformation probeInfo = new ProbeInformation("TestTargetName", targetType, "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRPCUrl");
        assertEquals(targetType, probeInfo.getTargetType());
    }

    @Test
    public void testGetBmName() {
        String bmName = "TestBMName";
        ProbeInformation probeInfo = new ProbeInformation("TestTargetName", "TestTargetType", "BMClass", bmName, "BMDescription", "ProbeDescription", 1, "XMLRPCUrl");
        assertEquals(bmName, probeInfo.getBmName());
    }

    @Test
    public void testToString() {
        String expected = "ProbeInformation{targetName='TestTargetName', targetType='TestTargetType', bmClass='BMClass', bmName='TestBMName', bmDescription='BMDescription', probeDescription='ProbeDescription', precision=1}";
        ProbeInformation probeInfo = new ProbeInformation("TestTargetName", "TestTargetType", "BMClass", "TestBMName", "BMDescription", "ProbeDescription", 1, "XMLRPCUrl");
        assertEquals(expected, probeInfo.toString());
    }

    @Test
    public void testGetBmClass() {
        String bmClass = "TestBMClass";
        ProbeInformation probeInfo = new ProbeInformation("TestTargetName", "TestTargetType", bmClass, "TestBMName", "BMDescription", "ProbeDescription", 1, "XMLRPCUrl");
        assertEquals(bmClass, probeInfo.getBmClass());
    }

    @Test
    public void testGetBmDescription() {
        String bmDescription = "TestBMDescription";
        ProbeInformation probeInfo = new ProbeInformation("TestTargetName", "TestTargetType", "BMClass", "TestBMName", bmDescription, "ProbeDescription", 1, "XMLRPCUrl");
        assertEquals(bmDescription, probeInfo.getBmDescription());
    }

    @Test
    public void testGetPrecision() {
        int precision = 1;
        ProbeInformation probeInfo = new ProbeInformation("TestTargetName", "TestTargetType", "BMClass", "TestBMName", "BMDescription", "ProbeDescription", precision, "XMLRPCUrl");
        assertEquals(precision, probeInfo.getPrecision());
    }

    @Test
    public void testGetMeasureURI() {
        String targetType = "TestTargetType";
        String targetName = "TestTargetName";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, "BMDescription", "ProbeDescription", 1, "XMLRPCUrl");
        String measureURI = probeInfo.getMeasureURI();
        String expected = Const.createMeasureURI(targetType, targetName, bmClass, bmName);
        assertEquals(expected, measureURI);
    }

    @Test
    public void testGetXmlRpcUrl() {
        String xmlRpcUrl = "TestXMLRPCUrl";
        ProbeInformation probeInfo = new ProbeInformation("TestTargetName", "TestTargetType", "BMClass", "TestBMName", "BMDescription", "ProbeDescription", 1, xmlRpcUrl);
        assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());
    }

    @Test
    public void testEquals() {
        String targetName1 = "TestTargetName1";
        String targetType1 = "TestTargetType1";
        String bmClass1 = "BMClass1";
        String bmName1 = "BMName1";
        String bmDescription1 = "BMDescription1";
        String probeName1 = "ProbeName1";
        int precision1 = 1;
        String xmlRpcUrl1 = "XMLRPCUrl1";

        String targetName2 = "TestTargetName2";
        String targetType2 = "TestTargetType1";
        String bmClass2 = "BMClass1";
        String bmName2 = "BMName1";
        String bmDescription2 = "BMDescription1";
        String probeName2 = "ProbeName1";
        int precision2 = 1;
        String xmlRpcUrl2 = "XMLRPCUrl1";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);

        assertTrue(probeInfo1.equals(probeInfo1));
        assertTrue(probeInfo1.equals(probeInfo2));

        ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        ProbeInformation probeInfo4 = new ProbeInformation(targetName1, targetType2, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        assertFalse(probeInfo3.equals(probeInfo4));

        ProbeInformation probeInfo5 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        ProbeInformation probeInfo6 = new ProbeInformation(targetName2, targetType1, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);

        assertFalse(probeInfo5.equals(probeInfo6));
    }

    @Test
    public void testHashCode() {
        String targetName = "TestTargetName";
        String targetType = "TestTargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeName = "ProbeName";
        int precision = 1;

        int hashCode1 = probeInfo("TestTargetName", "TestTargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 1).hashCode();
        int hashCode2 = probeInfo("TestTargetName", "TestTargetType", "BMClass", "BMName", "BMDescription", "ProbeName", 1).hashCode();

        assertEquals(hashCode1, hashCode2);

        int hashCode3 = probeInfo("TestTargetName1", "TestTargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeName1", 1).hashCode();
        int hashCode4 = probeInfo("TestTargetName1", "TestTargetType2", "BMClass1", "BMName1", "BMDescription1", "ProbeName1", 1).hashCode();

        assertNotEquals(hashCode3, hashCode4);
    }

    private ProbeInformation probeInfo(String targetName, String targetType, String bmClass, String bmName, String bmDescription, String probeName, int precision) {
        return new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, "");
    }
}