package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testProbeInformationCreation() {
        String targetName = "targetName1";
        String targetType = "targetType1";
        String bmClass = "bmClass1";
        String bmName = "bmName1";
        String bmDescription = "bmDescription1";
        String probeName = "probeName1";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl1";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(targetName, probeInfo1.getTargetName());
        assertEquals(targetType, probeInfo1.getTargetType());
        assertEquals(bmClass, probeInfo1.getBmClass());
        assertEquals(bmName, probeInfo1.getBmName());
        assertEquals(bmDescription, probeInfo1.getBmDescription());
        assertEquals(probeName, probeInfo1.getProbeName());
        assertEquals(precision, probeInfo1.getPrecision());
        assertEquals(xmlRpcUrl, probeInfo1.getXmlRpcUrl());
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
        String targetType = "targetType1";
        String targetName = "targetName1";
        String bmClass = "bmClass1";
        String bmName = "bmName1";

        ProbeInformation probeInfo = new ProbeInformation("targetName1", targetType, bmClass, bmName, null, null, 1, null);

        String measureURI = probeInfo.getMeasureURI();
        assertEquals(Const.createMeasureURI(targetType, targetName, bmClass, bmName), measureURI);
    }

    @Test
    public void testEquals() {
        String targetName1 = "targetName1";
        String targetName2 = "targetName2";
        String targetType1 = "targetType1";
        String targetType2 = "targetType2";
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

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);

        assertNotSame(probeInfo1, probeInfo2);

        ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        assertEquals(probeInfo1, probeInfo3);

        ProbeInformation probeInfo4 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        assertEquals(probeInfo1, probeInfo4);

        ProbeInformation probeInfo5 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        assertEquals(probeInfo1, probeInfo5);

        probeInfo1.setPrecision(precision2);
        assertNotSame(probeInfo1, probeInfo3);

        probeInfo1.setBmClass(bmClass2);
        assertNotSame(probeInfo1, probeInfo3);

        probeInfo1.setBmName(bmName2);
        assertNotSame(probeInfo1, probeInfo3);

        probeInfo1.setBmDescription(bmDescription2);
        assertNotSame(probeInfo1, probeInfo3);

        probeInfo1.setProbeName(probeName2);
        assertNotSame(probeInfo1, probeInfo3);

        probeInfo1.setTargetName(targetName2);
        assertNotSame(probeInfo1, probeInfo3);

        probeInfo1.setTargetType(targetType2);
        assertNotSame(probeInfo1, probeInfo3);

        probeInfo1.setXmlRpcUrl(xmlRpcUrl2);
        assertNotSame(probeInfo1, probeInfo3);
    }

    @Test
    public void testHashCode() {
        String targetName = "targetName1";
        String targetType = "targetType1";
        String bmClass = "bmClass1";
        String bmName = "bmName1";
        String bmDescription = "bmDescription1";
        String probeName = "probeName1";
        int precision = 1;

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, null);

        int hashCode = probeInfo.hashCode();

        assertEquals(hashCode, probeInfo.hashCode());

        ProbeInformation probeInfo2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, null);
        assertEquals(hashCode, probeInfo2.hashCode());

        String targetName2 = "targetName2";
        ProbeInformation probeInfo3 = new ProbeInformation(targetName2, targetType, bmClass, bmName, bmDescription, probeName, precision, null);
        assertNotSame(hashCode, probeInfo3.hashCode());

        String targetType2 = "targetType2";
        ProbeInformation probeInfo4 = new ProbeInformation(targetName, targetType2, bmClass, bmName, bmDescription, probeName, precision, null);
        assertNotSame(hashCode, probeInfo4.hashCode());

        String bmClass2 = "bmClass2";
        ProbeInformation probeInfo5 = new ProbeInformation(targetName, targetType, bmClass2, bmName, bmDescription, probeName, precision, null);
        assertNotSame(hashCode, probeInfo5.hashCode());

        String bmName2 = "bmName2";
        ProbeInformation probeInfo6 = new ProbeInformation(targetName, targetType, bmClass, bmName2, bmDescription, probeName, precision, null);
        assertNotSame(hashCode, probeInfo6.hashCode());

        String bmDescription2 = "bmDescription2";
        ProbeInformation probeInfo7 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription2, probeName, precision, null);
        assertNotSame(hashCode, probeInfo7.hashCode());

        String probeName2 = "probeName2";
        ProbeInformation probeInfo8 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName2, precision, null);
        assertNotSame(hashCode, probeInfo8.hashCode());

        int precision2 = 2;
        ProbeInformation probeInfo9 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision2, null);
        assertNotSame(hashCode, probeInfo9.hashCode());

        ProbeInformation probeInfo10 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, "xmlRpcUrl1");
        assertNotSame(hashCode, probeInfo10.hashCode());
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

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, null);

        String toString = probeInfo.toString();
        assertEquals("ProbeInformation{targetName='" + targetName + "', targetType='" + targetType + "', bmClass='" + bmClass + "', bmName='" + bmName + "', bmDescription='" + bmDescription + "', probeDescription='" + probeName + "', precision=" + precision + "}", toString);
    }

    @Test
    public void testNullValues() {
        ProbeInformation probeInfo = new ProbeInformation(null, null, null, null, null, null, 0, null);
        assertNull(probeInfo.getTargetName());
        assertNull(probeInfo.getTargetType());
        assertNull(probeInfo.getBmClass());
        assertNull(probeInfo.getBmName());
        assertNull(probeInfo.getBmDescription());
        assertNull(probeInfo.getProbeName());
        assertEquals(0, probeInfo.getPrecision());
        assertNull(probeInfo.getXmlRpcUrl());
    }
}