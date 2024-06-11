package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @Test
    public void testProbeInformationConstructor() {
        String targetName = "TargetName1";
        String targetType = "TargetType1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeDescription = "ProbeDescription1";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl1";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        assertEquals(targetName, probeInfo.getTargetName());
        assertEquals(targetType, probeInfo.getTargetType());
        assertEquals(bmClass, probeInfo.getBmClass());
        assertEquals(bmName, probeInfo.getBmName());
        assertEquals(bmDescription, probeInfo.getBmDescription());
        assertEquals(probeDescription, probeInfo.getProbeName());
        assertEquals(precision, probeInfo.getPrecision());
        assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());
    }

    @Test(expected = NullPointerException.class)
    public void testProbeInformationConstructorWithNullTargetName() {
        String targetType = "TargetType1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeDescription = "ProbeDescription1";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl1";

        new ProbeInformation(null, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testProbeInformationConstructorWithNullTargetType() {
        String targetName = "TargetName1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeDescription = "ProbeDescription1";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl1";

        new ProbeInformation(targetName, null, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testProbeInformationConstructorWithNullBmClass() {
        String targetName = "TargetName1";
        String targetType = "TargetType1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeDescription = "ProbeDescription1";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl1";

        new ProbeInformation(targetName, targetType, null, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testProbeInformationConstructorWithNullBmName() {
        String targetName = "TargetName1";
        String targetType = "TargetType1";
        String bmClass = "BMClass1";
        String bmDescription = "BMDescription1";
        String probeDescription = "ProbeDescription1";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl1";

        new ProbeInformation(targetName, targetType, bmClass, null, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testProbeInformationConstructorWithNullBmDescription() {
        String targetName = "TargetName1";
        String targetType = "TargetType1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String probeDescription = "ProbeDescription1";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl1";

        new ProbeInformation(targetName, targetType, bmClass, bmName, null, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testProbeInformationConstructorWithNullProbeDescription() {
        String targetName = "TargetName1";
        String targetType = "TargetType1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl1";

        new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, null, precision, xmlRpcUrl);
    }

    @Test
    public void testGetMeasureURI() {
        String targetType = "TargetType1";
        String targetName = "TargetName1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";

        ProbeInformation probeInfo = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XMLRpcUrl1");

        assertEquals("http://localhost:8080/measure?namespace=http%3A%2F%2Ffi.vtt.noen.mfw.bundle.probe.shared&name=" + targetType + "%2F" + targetName + "%2F" + bmClass + "%2F" + bmName, probeInfo.getMeasureURI());
    }

    @Test
    public void testEquals() {
        String targetName1 = "TargetName1";
        String targetName2 = "TargetName2";
        String targetType1 = "TargetType1";
        String targetType2 = "TargetType2";
        String bmClass1 = "BMClass1";
        String bmClass2 = "BMClass2";
        String bmName1 = "BMName1";
        String bmName2 = "BMName2";
        String bmDescription1 = "BMDescription1";
        String bmDescription2 = "BMDescription2";
        String probeDescription1 = "ProbeDescription1";
        String probeDescription2 = "ProbeDescription2";
        int precision1 = 1;
        int precision2 = 2;
        String xmlRpcUrl1 = "XMLRpcUrl1";
        String xmlRpcUrl2 = "XMLRpcUrl2";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeDescription2, precision2, xmlRpcUrl2);

        assertNotEquals(probeInfo1, probeInfo2);

        ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);

        assertEquals(probeInfo1, probeInfo3);

        ProbeInformation probeInfo4 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);

        assertEquals(probeInfo1, probeInfo4);

        probeInfo1.setPrecision(2);

        assertNotEquals(probeInfo1, probeInfo4);

        probeInfo4.setPrecision(2);

        assertEquals(probeInfo1, probeInfo4);

        probeInfo1.setBmClass("BMClass1_1");

        assertNotEquals(probeInfo1, probeInfo4);

        probeInfo4.setBmClass("BMClass1_1");

        assertEquals(probeInfo1, probeInfo4);

        probeInfo1.setBmDescription("BMDescription1_1");

        assertNotEquals(probeInfo1, probeInfo4);

        probeInfo4.setBmDescription("BMDescription1_1");

        assertEquals(probeInfo1, probeInfo4);

        probeInfo1.setTargetName("TargetName1_1");

        assertNotEquals(probeInfo1, probeInfo4);

        probeInfo4.setTargetName("TargetName1_1");

        assertEquals(probeInfo1, probeInfo4);

        probeInfo1.setTargetType("TargetType1_1");

        assertNotEquals(probeInfo1, probeInfo4);

        probeInfo4.setTargetType("TargetType1_1");

        assertEquals(probeInfo1, probeInfo4);

        probeInfo1.setBmName("BMName1_1");

        assertNotEquals(probeInfo1, probeInfo4);

        probeInfo4.setBmName("BMName1_1");

        assertEquals(probeInfo1, probeInfo4);

        probeInfo1.setProbeDescription("ProbeDescription1_1");

        assertNotEquals(probeInfo1, probeInfo4);

        probeInfo4.setProbeDescription("ProbeDescription1_1");

        assertEquals(probeInfo1, probeInfo4);

        probeInfo1.setXmlRpcUrl("XMLRpcUrl1_1");

        assertNotEquals(probeInfo1, probeInfo4);

        probeInfo4.setXmlRpcUrl("XMLRpcUrl1_1");

        assertEquals(probeInfo1, probeInfo4);
    }

    @Test
    public void testHashCode() {
        String targetName = "TargetName1";
        String targetType = "TargetType1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeDescription = "ProbeDescription1";
        int precision = 1;

        ProbeInformation probeInfo1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, null);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, "XMLRpcUrl2");

        int hashCode1 = probeInfo1.hashCode();
        int hashCode2 = probeInfo2.hashCode();

        assertNotEquals(hashCode1, hashCode2);

        probeInfo1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, null);
        probeInfo2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, null);

        assertEquals(hashCode1, hashCode2);

        probeInfo1.setTargetName("TargetName1_1");

        hashCode1 = probeInfo1.hashCode();
        hashCode2 = probeInfo2.hashCode();

        assertNotEquals(hashCode1, hashCode2);

        probeInfo2.setTargetName("TargetName1_1");

        assertEquals(hashCode1, hashCode2);

        probeInfo1.setTargetType("TargetType1_1");

        hashCode1 = probeInfo1.hashCode();
        hashCode2 = probeInfo2.hashCode();

        assertNotEquals(hashCode1, hashCode2);

        probeInfo2.setTargetType("TargetType1_1");

        assertEquals(hashCode1, hashCode2);

        probeInfo1.setBmClass("BMClass1_1");

        hashCode1 = probeInfo1.hashCode();
        hashCode2 = probeInfo2.hashCode();

        assertNotEquals(hashCode1, hashCode2);

        probeInfo2.setBmClass("BMClass1_1");

        assertEquals(hashCode1, hashCode2);

        probeInfo1.setBmName("BMName1_1");

        hashCode1 = probeInfo1.hashCode();
        hashCode2 = probeInfo2.hashCode();

        assertNotEquals(hashCode1, hashCode2);

        probeInfo2.setBmName("BMName1_1");

        assertEquals(hashCode1, hashCode2);

        probeInfo1.setBmDescription("BMDescription1_1");

        hashCode1 = probeInfo1.hashCode();
        hashCode2 = probeInfo2.hashCode();

        assertNotEquals(hashCode1, hashCode2);

        probeInfo2.setBmDescription("BMDescription1_1");

        assertEquals(hashCode1, hashCode2);

        probeInfo1.setProbeDescription("ProbeDescription1_1");

        hashCode1 = probeInfo1.hashCode();
        hashCode2 = probeInfo2.hashCode();

        assertNotEquals(hashCode1, hashCode2);

        probeInfo2.setProbeDescription("ProbeDescription1_1");

        assertEquals(hashCode1, hashCode2);

        probeInfo1.setPrecision(2);

        hashCode1 = probeInfo1.hashCode();
        hashCode2 = probeInfo2.hashCode();

        assertNotEquals(hashCode1, hashCode2);

        probeInfo2.setPrecision(2);

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        String targetName = "TargetName1";
        String targetType = "TargetType1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeDescription = "ProbeDescription1";
        int precision = 1;

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, null);

        assertEquals("ProbeInformation{targetName='TargetName1', targetType='TargetType1', bmClass='BMClass1', bmName='BMName1', bmDescription='BMDescription1', probeDescription='ProbeDescription1', precision=1}", probeInfo.toString());
    }
}