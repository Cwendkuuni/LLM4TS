package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @Test
    public void testConstructor() {
        String targetName = "TargetName";
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeName = "ProbeName";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";

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

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullTargetName() {
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeName = "ProbeName";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";

        new ProbeInformation(null, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullTargetType() {
        String targetName = "TargetName";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeName = "ProbeName";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";

        new ProbeInformation(targetName, null, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullBmClass() {
        String targetName = "TargetName";
        String targetType = "TargetType";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeName = "ProbeName";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";

        new ProbeInformation(targetName, targetType, null, bmName, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullBmName() {
        String targetName = "TargetName";
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmDescription = "BMDescription";
        String probeName = "ProbeName";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";

        new ProbeInformation(targetName, targetType, bmClass, null, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullBmDescription() {
        String targetName = "TargetName";
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String probeName = "ProbeName";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";

        new ProbeInformation(targetName, targetType, bmClass, bmName, null, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullProbeName() {
        String targetName = "TargetName";
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";

        new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, null, precision, xmlRpcUrl);
    }

    @Test
    public void testGetters() {
        String targetName = "TargetName";
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeName = "ProbeName";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";

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
    public void testEquals() {
        String targetName1 = "TargetName1";
        String targetType1 = "TargetType1";
        String bmClass1 = "BMClass1";
        String bmName1 = "BMName1";
        String bmDescription1 = "BMDescription1";
        String probeName1 = "ProbeName1";
        int precision1 = 1;
        String xmlRpcUrl1 = "XMLRpcUrl1";

        String targetName2 = "TargetName2";
        String targetType2 = "TargetType2";
        String bmClass2 = "BMClass2";
        String bmName2 = "BMName2";
        String bmDescription2 = "BMDescription2";
        String probeName2 = "ProbeName2";
        int precision2 = 1;
        String xmlRpcUrl2 = "XMLRpcUrl2";

        ProbeInformation probeInformation1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        ProbeInformation probeInformation2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);

        ProbeInformation probeInformation3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        assertTrue(probeInformation1.equals(probeInformation3));

        assertFalse(probeInformation1.equals(probeInformation2));

        assertFalse(probeInformation1.equals(null));

        assertFalse(probeInformation1.equals("Test"));
    }

    @Test
    public void testHashCode() {
        String targetName = "TargetName";
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeName = "ProbeName";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        int hashCode = probeInformation.hashCode();

        String targetName2 = "TargetName2";
        String targetType2 = "TargetType2";
        String bmClass2 = "BMClass2";
        String bmName2 = "BMName2";
        String bmDescription2 = "BMDescription2";
        String probeName2 = "ProbeName2";
        int precision2 = 2;
        String xmlRpcUrl2 = "XMLRpcUrl2";

        ProbeInformation probeInformation2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);

        int hashCode2 = probeInformation2.hashCode();

        assertFalse(hashCode == hashCode2);
    }

    @Test
    public void testToString() {
        String targetName = "TargetName";
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeName = "ProbeName";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        String expectedResult = "ProbeInformation{targetName='TargetName', targetType='TargetType', bmClass='BMClass', bmName='BMName', bmDescription='BMDescription', probeDescription='ProbeName', precision=1}";

        assertEquals(expectedResult, probeInformation.toString());
    }

    @Test
    public void testGetMeasureURI() {
        String targetType = "TargetType";
        String targetName = "TargetName";
        String bmClass = "BMClass";
        String bmName = "BMName";

        ProbeInformation probeInformation = new ProbeInformation("TargetName", targetType, bmClass, bmName, null, null, 1, null);

        String expectedResult = Const.createMeasureURI(targetType, targetName, bmClass, bmName);

        assertEquals(expectedResult, probeInformation.getMeasureURI());
    }
}