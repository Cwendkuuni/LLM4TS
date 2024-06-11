package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;

import org.junit.Test;

import fi.vtt.noen.mfw.bundle.common.Const;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @Test
    public void testProbeInformationConstruction() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = 4;
        String xmlRpcUrl = "testXmlRpcUrl";

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
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = 4;
        String xmlRpcUrl = "testXmlRpcUrl";

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
    public void testToString() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = 4;
        String xmlRpcUrl = "testXmlRpcUrl";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        String expected = "ProbeInformation{targetName='" + targetName + "', targetType='" + targetType + "', bmClass='" + bmClass + "', bmName='" + bmName + "', bmDescription='" + bmDescription + "', probeDescription='" + probeName + "', precision=4}";
        assertEquals(expected, probeInformation.toString());
    }

    @Test
    public void testEquals() {
        String targetName1 = "testTargetName1";
        String targetName2 = "testTargetName2";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = 4;
        String xmlRpcUrl = "testXmlRpcUrl";

        ProbeInformation probeInformation1 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
        ProbeInformation probeInformation2 = new ProbeInformation(targetName2, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        ProbeInformation probeInformation3 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeName, precision, null);

        assertTrue(probeInformation1.equals(probeInformation1));
        assertTrue(probeInformation1.equals(probeInformation2));
        assertFalse(probeInformation1.equals(probeInformation3));
        assertFalse(probeInformation1.equals(null));
    }

    @Test
    public void testHashCode() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = 4;
        String xmlRpcUrl = "testXmlRpcUrl";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        int hashCode = probeInformation.hashCode();
        String targetName2 = "testTargetName2";
        String targetType2 = "testTargetType2";
        String bmClass2 = "testBmClass2";
        String bmName2 = "testBmName2";
        String bmDescription2 = "testBmDescription2";
        String probeName2 = "testProbeName2";
        int precision2 = 5;
        String xmlRpcUrl2 = "testXmlRpcUrl2";

        ProbeInformation probeInformation2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);

        ProbeInformation probeInformation3 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, null);

        int expectedHashCode = targetName.hashCode() * 31 + targetType.hashCode() * 31 + bmClass.hashCode() * 31 + bmName.hashCode() * 31 + bmDescription.hashCode() * 31 + probeName.hashCode() * 31 + precision;
        assertEquals(expectedHashCode, probeInformation.hashCode());
        assertEquals(probeInformation.hashCode(), probeInformation2.hashCode());
        assertNotEquals(probeInformation.hashCode(), probeInformation3.hashCode());
    }

    @Test
    public void testCreateMeasureURI() {
        String targetType = "testTargetType";
        String targetName = "testTargetName";
        String bmClass = "testBmClass";
        String bmName = "testBmName";

        String measureURI = Const.createMeasureURI(targetType, targetName, bmClass, bmName);

        String expected = "http://localhost:8080/measure?type=" + targetType + "&name=" + targetName + "&class=" + bmClass + "&name=" + bmName;
        assertEquals(expected, measureURI);
    }

}