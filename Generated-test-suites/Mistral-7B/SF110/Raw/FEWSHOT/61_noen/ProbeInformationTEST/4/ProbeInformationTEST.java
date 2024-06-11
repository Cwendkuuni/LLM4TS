package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testConstructor() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeDescription = "testProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        assertEquals(targetName, probeInformation.getTargetName());
        assertEquals(targetType, probeInformation.getTargetType());
        assertEquals(bmClass, probeInformation.getBmClass());
        assertEquals(bmName, probeInformation.getBmName());
        assertEquals(bmDescription, probeInformation.getBmDescription());
        assertEquals(probeDescription, probeInformation.getProbeName());
        assertEquals(precision, probeInformation.getPrecision());
        assertEquals(xmlRpcUrl, probeInformation.getXmlRpcUrl());
    }

    @Test
    public void testGetters() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeDescription = "testProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        assertEquals(targetName, probeInformation.getTargetName());
        assertEquals(targetType, probeInformation.getTargetType());
        assertEquals(bmClass, probeInformation.getBmClass());
        assertEquals(bmName, probeInformation.getBmName());
        assertEquals(bmDescription, probeInformation.getBmDescription());
        assertEquals(probeDescription, probeInformation.getProbeName());
        assertEquals(precision, probeInformation.getPrecision());
        assertEquals(xmlRpcUrl, probeInformation.getXmlRpcUrl());
    }

    @Test
    public void testMeasureURI() {
        String targetType = "testTargetType";
        String targetName = "testTargetName";
        String bmClass = "testBmClass";
        String bmName = "testBmName";

        ProbeInformation probeInformation = new ProbeInformation("", targetType, bmClass, bmName, "", "", 0, "");
        assertEquals(Const.createMeasureURI(targetType, targetName, bmClass, bmName), probeInformation.getMeasureURI());
    }

    @Test
    public void testEquals() {
        String targetName1 = "testTargetName1";
        String targetName2 = "testTargetName2";
        String targetType1 = "testTargetType1";
        String targetType2 = "testTargetType2";
        String bmClass1 = "testBmClass1";
        String bmClass2 = "testBmClass2";
        String bmName1 = "testBmName1";
        String bmName2 = "testBmName2";
        String bmDescription1 = "testBmDescription1";
        String bmDescription2 = "testBmDescription2";
        String probeDescription1 = "testProbeDescription1";
        String probeDescription2 = "testProbeDescription2";
        int precision1 = 1;
        int precision2 = 2;
        String xmlRpcUrl1 = "testXmlRpcUrl1";
        String xmlRpcUrl2 = "testXmlRpcUrl2";

        ProbeInformation probeInformation1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        ProbeInformation probeInformation2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeDescription2, precision2, xmlRpcUrl2);

        assertNotSame(probeInformation1, probeInformation2);

        ProbeInformation probeInformation3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        assertSame(probeInformation1, probeInformation3);

        ProbeInformation probeInformation4 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        assertSame(probeInformation1, probeInformation4);

        ProbeInformation probeInformation5 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        probeInformation5.setPrecision(precision2);
        assertNotSame(probeInformation1, probeInformation5);

        ProbeInformation probeInformation6 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        probeInformation6.setBmClass(bmClass2);
        assertNotSame(probeInformation1, probeInformation6);

        ProbeInformation probeInformation7 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        probeInformation7.setBmName(bmName2);
        assertNotSame(probeInformation1, probeInformation7);

        ProbeInformation probeInformation8 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        probeInformation8.setBmDescription(bmDescription2);
        assertNotSame(probeInformation1, probeInformation8);

        ProbeInformation probeInformation9 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        probeInformation9.setProbeDescription(probeDescription2);
        assertNotSame(probeInformation1, probeInformation9);

        ProbeInformation probeInformation10 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        probeInformation10.setTargetName(targetName2);
        assertNotSame(probeInformation1, probeInformation10);

        ProbeInformation probeInformation11 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeDescription1, precision1, xmlRpcUrl1);
        probeInformation11.setTargetType(targetType2);
        assertNotSame(probeInformation1, probeInformation11);
    }

    @Test
    public void testHashCode() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeDescription = "testProbeDescription";
        int precision = 1;

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, "");

        int hashCode = probeInformation.hashCode();

        assertEquals(hashCode, probeInformation.hashCode());

        probeInformation.setTargetName("testTargetName2");
        assertEquals(hashCode, probeInformation.hashCode());

        probeInformation.setTargetType("testTargetType2");
        assertEquals(hashCode, probeInformation.hashCode());

        probeInformation.setBmClass("testBmClass2");
        assertNotEquals(hashCode, probeInformation.hashCode());

        probeInformation.setBmName("testBmName2");
        assertNotEquals(hashCode, probeInformation.hashCode());

        probeInformation.setBmDescription("testBmDescription2");
        assertNotEquals(hashCode, probeInformation.hashCode());

        probeInformation.setProbeDescription("testProbeDescription2");
        assertNotEquals(hashCode, probeInformation.hashCode());

        probeInformation.setPrecision(2);
        assertNotEquals(hashCode, probeInformation.hashCode());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullArguments() {
        new ProbeInformation(null, null, null, null, null, null, 0, null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullTargetName() {
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeDescription = "testProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";

        new ProbeInformation(null, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullTargetType() {
        String targetName = "testTargetName";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeDescription = "testProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";

        new ProbeInformation(targetName, null, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullBmClass() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeDescription = "testProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";

        new ProbeInformation(targetName, targetType, null, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullBmName() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmDescription = "testBmDescription";
        String probeDescription = "testProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";

        new ProbeInformation(targetName, targetType, bmClass, null, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullBmDescription() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String probeDescription = "testProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";

        new ProbeInformation(targetName, targetType, bmClass, bmName, null, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullProbeDescription() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";

        new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, null, precision, xmlRpcUrl);
    }

    @Test
    public void testToString() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeDescription = "testProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        String expected = "ProbeInformation{targetName='" + targetName + "', targetType='" + targetType + "', bmClass='" + bmClass + "', bmName='" + bmName + "', bmDescription='" + bmDescription + "', probeDescription='" + probeDescription + "', precision=" + precision + "}";

        assertEquals(expected, probeInformation.toString());
    }
}