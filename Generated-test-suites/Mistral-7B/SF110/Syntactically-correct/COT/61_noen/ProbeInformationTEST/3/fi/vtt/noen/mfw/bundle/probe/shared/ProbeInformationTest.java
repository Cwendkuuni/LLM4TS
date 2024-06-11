package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertFalse;

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

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullTargetName() {
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";

        new ProbeInformation(null, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullTargetType() {
        String targetName = "TargetName";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";

        new ProbeInformation(targetName, null, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullBmClass() {
        String targetName = "TargetName";
        String targetType = "TargetType";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";

        new ProbeInformation(targetName, targetType, null, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullBmName() {
        String targetName = "TargetName";
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";

        new ProbeInformation(targetName, targetType, bmClass, null, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullBmDescription() {
        String targetName = "TargetName";
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";

        new ProbeInformation(targetName, targetType, bmClass, bmName, null, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullProbeDescription() {
        String targetName = "TargetName";
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";

        new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, null, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullXmlRpcUrl() {
        String targetName = "TargetName";
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;

        new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, null);
    }

    @Test
    public void testGetMethods() {
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

        String measureURI = probeInformation.getMeasureURI();
        assertEquals(Const.createMeasureURI(targetType, targetName, bmClass, bmName), measureURI);
    }

    @Test
    public void testToString() {
        String targetName = "TargetName";
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        String expectedString = "ProbeInformation{targetName='" + targetName + "', targetType='" + targetType + "', bmClass='" + bmClass + "', bmName='" + bmName + "', bmDescription='" + bmDescription + "', probeDescription='" + probeDescription + "', precision=" + precision + "}";
        assertEquals(expectedString, probeInformation.toString());
    }

    @Test
    public void testEquals() {
        String targetName1 = "TargetName1";
        String targetName2 = "TargetName2";
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;
        String xmlRpcUrl = "XMLRpcUrl";

        ProbeInformation probeInformation1 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
        ProbeInformation probeInformation2 = new ProbeInformation(targetName2, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        ProbeInformation probeInformation3 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
        ProbeInformation probeInformation4 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeDescription, precision, null);
        ProbeInformation probeInformation5 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeDescription, precision + 1, xmlRpcUrl);

        assertNotSame(probeInformation1, probeInformation2);
        assertSame(probeInformation1, probeInformation3);
        assertFalse(probeInformation1.equals(probeInformation4));
        assertFalse(probeInformation1.equals(probeInformation5));
    }

    @Test
    public void testHashCode() {
        String targetName = "TargetName";
        String targetType = "TargetType";
        String bmClass = "BMClass";
        String bmName = "BMName";
        String bmDescription = "BMDescription";
        String probeDescription = "ProbeDescription";
        int precision = 1;

        int hashCode1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision).hashCode();
        int hashCode2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision).hashCode();

        assertEquals(hashCode1, hashCode2);
    }
}