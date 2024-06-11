package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ProbeInformationTest {

    @Test
    public void testProbeInformationCreation() {
        String targetName = "testTarget";
        String targetType = "testType";
        String bmClass = "testBMClass";
        String bmName = "testBMName";
        String bmDescription = "testBMDescription";
        String probeName = "testProbeName";
        int precision = 2;
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
        String targetName = "testTarget";
        String targetType = "testType";
        String bmClass = "testBMClass";
        String bmName = "testBMName";
        String bmDescription = "testBMDescription";
        String probeName = "testProbeName";
        int precision = 2;
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

        // Test toString method
        assertEquals("ProbeInformation{targetName='" + targetName + "', targetType='" + targetType + "', bmClass='" + bmClass + "', bmName='" + bmName + "', bmDescription='" + bmDescription + "', probeDescription='" + probeName + "', precision=" + precision + "}", probeInformation.toString());
    }

    @Test
    public void testEqualsAndHashcode() {
        String targetName1 = "testTarget1";
        String targetName2 = "testTarget2";
        String targetType1 = "testType1";
        String targetType2 = "testType2";
        String bmClass1 = "testBMClass1";
        String bmClass2 = "testBMClass2";
        String bmName1 = "testBMName1";
        String bmName2 = "testBMName2";
        String bmDescription1 = "testBMDescription1";
        String bmDescription2 = "testBMDescription2";
        String probeName1 = "testProbeName1";
        String probeName2 = "testProbeName2";
        int precision1 = 2;
        int precision2 = 3;
        String xmlRpcUrl1 = "testXmlRpcUrl1";
        String xmlRpcUrl2 = "testXmlRpcUrl2";

        ProbeInformation probeInformation1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        ProbeInformation probeInformation2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);

        // Test equals method
        assertNotEquals(probeInformation1, probeInformation2);

        ProbeInformation probeInformation3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        assertEquals(probeInformation1, probeInformation3);

        // Test hashcode method
        int hashcode1 = probeInformation1.hashCode();
        int hashcode2 = probeInformation2.hashCode();
        int hashcode3 = probeInformation3.hashCode();

        // Test same object
        ProbeInformation probeInformation4 = probeInformation1;
        assertEquals(probeInformation1, probeInformation4);
        assertEquals(hashcode1, hashcode4);

        // Test null object
        assertNull(probeInformation1.equals(null));

        // Test different class
        assertNotEquals(probeInformation1, new Object());
    }

    @Test
    public void testMeasureURI() {
        String targetType = "testTargetType";
        String targetName = "testTargetName";
        String bmClass = "testBMClass";
        String bmName = "testBMName";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, null, null, 0, null);

        String measureURI = probeInformation.getMeasureURI();
        String expectedMeasureURI = Const.createMeasureURI(targetType, targetName, bmClass, bmName);

        assertEquals(expectedMeasureURI, measureURI);
    }
}