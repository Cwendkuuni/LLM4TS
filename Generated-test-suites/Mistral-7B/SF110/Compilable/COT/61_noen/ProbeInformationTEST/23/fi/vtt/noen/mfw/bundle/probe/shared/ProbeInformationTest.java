package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;

import fi.vtt.noen.mfw.bundle.common.Const;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @org.junit.Test
    public void testConstructor() {
        String targetName = "TargetName";
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";

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

    @org.junit.Test
    public void testGetTargetType() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        String expected = "TargetType";
        String actual = probeInformation.getTargetType();

        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testGetBmName() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        String expected = "BMName";
        String actual = probeInformation.getBmName();

        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testGetProbeName() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        String expected = "ProbeDescription";
        String actual = probeInformation.getProbeName();

        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testGetTargetName() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        String expected = "TargetName";
        String actual = probeInformation.getTargetName();

        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testGetBmClass() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        String expected = "BMClass";
        String actual = probeInformation.getBmClass();

        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testGetBmDescription() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        String expected = "BMDescription";
        String actual = probeInformation.getBmDescription();

        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testGetPrecision() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        int expected = 1;
        int actual = probeInformation.getPrecision();

        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testGetMeasureURI() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        String expected = Const.createMeasureURI("TargetType", "TargetName", "BMClass", "BMName");
        String actual = probeInformation.getMeasureURI();

        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testGetXmlRpcUrl() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        String expected = "XMLRpcUrl";
        String actual = probeInformation.getXmlRpcUrl();

        assertEquals(expected, actual);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullTargetName() {
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation(null, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullTargetType() {
        String targetName = "TargetName";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation(targetName, null, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullBMClass() {
        String targetName = "TargetName";
        String targetType = "TargetType";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, null, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullBMName() {
        String targetName = "TargetName";
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, null, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullBMDescription() {
        String targetName = "TargetName";
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, null, probeDescription, precision, xmlRpcUrl);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullProbeDescription() {
        String targetName = "TargetName";
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";
        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, null, precision, xmlRpcUrl);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testEqualsWithNullObject() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        ProbeInformation other = null;

        assertFalse(probeInformation.equals(other));
    }

    @org.junit.Test
    public void testEqualsWithSameObject() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName", "TargetType", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XMLRpcUrl");
        ProbeInformation other = probeInformation;

        assertTrue(probeInformation.equals(other));
    }

    @org.junit.Test
    public void testEqualsWithDifferentObjects() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XMLRpcUrl1");
        ProbeInformation other = new ProbeInformation("TargetName2", "TargetType2", "BMClass2", "BMName2", "BMDescription2", "ProbeDescription2", 1, "XMLRpcUrl2");

        assertFalse(probeInformation.equals(other));
    }

    @org.junit.Test
    public void testEqualsWithDifferentPrecision() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XMLRpcUrl1");
        ProbeInformation other = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 2, "XMLRpcUrl1");

        assertFalse(probeInformation.equals(other));
    }

    @org.junit.Test
    public void testEqualsWithDifferentBMClass() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XMLRpcUrl1");
        ProbeInformation other = new ProbeInformation("TargetName1", "TargetType1", "BMClass2", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XMLRpcUrl1");

        assertFalse(probeInformation.equals(other));
    }

    @org.junit.Test
    public void testEqualsWithDifferentBMName() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XMLRpcUrl1");
        ProbeInformation other = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName2", "BMDescription1", "ProbeDescription1", 1, "XMLRpcUrl1");

        assertFalse(probeInformation.equals(other));
    }

    @org.junit.Test
    public void testEqualsWithDifferentBMDescription() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XMLRpcUrl1");
        ProbeInformation other = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription2", "ProbeDescription1", 1, "XMLRpcUrl1");

        assertFalse(probeInformation.equals(other));
    }

    @org.junit.Test
    public void testEqualsWithDifferentTargetName() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XMLRpcUrl1");
        ProbeInformation other = new ProbeInformation("TargetName2", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XMLRpcUrl1");

        assertFalse(probeInformation.equals(other));
    }

    @org.junit.Test
    public void testEqualsWithDifferentTargetType() {
        ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XMLRpcUrl1");
        ProbeInformation other = new ProbeInformation("TargetName1", "TargetType2", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XMLRpcUrl1");

        assertFalse(probeInformation.equals(other));
    }

    @org.junit.Test
    public void testHashCodeWithSameObjects() {
        ProbeInformation probeInformation1 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XMLRpcUrl1");
        ProbeInformation probeInformation2 = probeInformation1;

        int hashCode1 = probeInformation1.hashCode();
        int hashCode2 = probeInformation2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @org.junit.Test
    public void testHashCodeWithDifferentObjects() {
        ProbeInformation probeInformation1 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XMLRpcUrl1");
        ProbeInformation probeInformation2 = new ProbeInformation("TargetName2", "TargetType2", "BMClass2", "BMName2", "BMDescription2", "ProbeDescription2", 2, "XMLRpcUrl2");

        int hashCode1 = probeInformation1.hashCode();
        int hashCode2 = probeInformation2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }
}