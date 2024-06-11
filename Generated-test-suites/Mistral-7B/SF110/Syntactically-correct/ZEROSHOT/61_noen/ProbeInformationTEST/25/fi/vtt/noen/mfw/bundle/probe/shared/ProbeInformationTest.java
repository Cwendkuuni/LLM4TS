package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import fi.vtt.noen.mfw.bundle.common.Const;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @Test
    public void constructorTest() {
        String targetName = "TargetName1";
        String targetType = "TargetType1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeDescription = "ProbeDescription1";
        int precision = 1;
        String xmlRpcUrl = "XMLRPCUrl1";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);

        assertEquals(targetName, probeInfo.getTargetName());
        assertEquals(targetType, probeInfo.getTargetType());
        assertEquals(bmClass, probeInfo.getBmClass());
        assertEquals(bmName, probeInfo.getBmName());
        assertEquals(bmDescription, probeInfo.getBmDescription());
        assertEquals(probeDescription, probeInfo.getProbeName());
        assertEquals(precision, probeInfo.getPrecision());
        assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());

        String measureURI = Const.createMeasureURI(targetType, targetName, bmClass, bmName);
        assertEquals(measureURI, probeInfo.getMeasureURI());
    }

    @Test
    public void gettersTest() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XMLRPCUrl1");

        assertEquals("TargetType1", probeInfo.getTargetType());
        assertEquals("BMName1", probeInfo.getBmName());
        assertEquals("ProbeDescription1", probeInfo.getProbeName());
        assertEquals("TargetName1", probeInfo.getTargetName());
        assertEquals("BMClass1", probeInfo.getBmClass());
        assertEquals("BMDescription1", probeInfo.getBmDescription());
        assertEquals(1, probeInfo.getPrecision());
        assertNull(probeInfo.getXmlRpcUrl());

        probeInfo = new ProbeInformation("TargetName2", null, "BMClass2", null, null, null, 2, "XMLRPCUrl2");

        assertEquals(null, probeInfo.getTargetType());
        assertNull(probeInfo.getBmName());
        assertNull(probeInfo.getProbeName());
        assertEquals("TargetName2", probeInfo.getTargetName());
        assertEquals("BMClass2", probeInfo.getBmClass());
        assertNull(probeInfo.getBmDescription());
        assertEquals(2, probeInfo.getPrecision());
        assertEquals("XMLRPCUrl2", probeInfo.getXmlRpcUrl());
    }

    @Test
    public void equalsTest() {
        ProbeInformation probeInfo1 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XMLRPCUrl1");
        ProbeInformation probeInfo2 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XMLRPCUrl1");
        ProbeInformation probeInfo3 = new ProbeInformation("TargetName1", "TargetType1", "BMClass2", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XMLRPCUrl1");
        ProbeInformation probeInfo4 = new ProbeInformation("TargetName1", "TargetType2", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XMLRPCUrl1");

        assertEquals(probeInfo1, probeInfo1);
        assertEquals(probeInfo1, probeInfo2);
        assertNotSame(probeInfo1, probeInfo3);
        assertFalse(probeInfo1.equals(probeInfo4));

        // Test hashCode
        int hashCode1 = probeInfo1.hashCode();
        int hashCode2 = probeInfo2.hashCode();
        int hashCode3 = probeInfo3.hashCode();
        int hashCode4 = probeInfo4.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
    }

    @Test
    public void toStringTest() {
        ProbeInformation probeInfo = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XMLRPCUrl1");

        String expectedToString = "ProbeInformation{targetName='TargetName1', targetType='TargetType1', bmClass='BMClass1', bmName='BMName1', bmDescription='BMDescription1', probeDescription='ProbeDescription1', precision=1}";

        assertEquals(expectedToString, probeInfo.toString());
    }

    @Test(expected = NullPointerException.class)
    public void constructorNullBMDescriptionTest() {
        String targetName = "TargetName1";
        String targetType = "TargetType1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = null;
        String probeDescription = "ProbeDescription1";
        int precision = 1;
        String xmlRpcUrl = "XMLRPCUrl1";

        new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void constructorNullBmNameTest() {
        String targetName = "TargetName1";
        String targetType = "TargetType1";
        String bmClass = "BMClass1";
        String bmName = null;
        String bmDescription = "BMDescription1";
        String probeDescription = "ProbeDescription1";
        int precision = 1;
        String xmlRpcUrl = "XMLRPCUrl1";

        new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void constructorNullBmClassTest() {
        String targetName = "TargetName1";
        String targetType = "TargetType1";
        String bmClass = null;
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeDescription = "ProbeDescription1";
        int precision = 1;
        String xmlRpcUrl = "XMLRPCUrl1";

        new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void constructorNullTargetNameTest() {
        String targetName = null;
        String targetType = "TargetType1";
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeDescription = "ProbeDescription1";
        int precision = 1;
        String xmlRpcUrl = "XMLRPCUrl1";

        new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void constructorNullTargetTypeTest() {
        String targetName = "TargetName1";
        String targetType = null;
        String bmClass = "BMClass1";
        String bmName = "BMName1";
        String bmDescription = "BMDescription1";
        String probeDescription = "ProbeDescription1";
        int precision = 1;
        String xmlRpcUrl = "XMLRPCUrl1";

        new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }
}