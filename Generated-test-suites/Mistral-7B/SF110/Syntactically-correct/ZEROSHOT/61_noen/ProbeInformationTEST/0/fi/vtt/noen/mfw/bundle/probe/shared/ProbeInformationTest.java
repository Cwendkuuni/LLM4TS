package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;

import org.junit.Test;

import fi.vtt.noen.mfw.bundle.common.Const;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @Test
    public void testProbeInformationConstruction() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeName = "probeName";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(targetName, probeInfo.getTargetName());
        assertEquals(targetType, probeInfo.getTargetType());
        assertEquals(bmClass, probeInfo.getBmClass());
        assertEquals(bmName, probeInfo.getBmName());
        assertEquals(bmDescription, probeInfo.getBmDescription());
        assertEquals(probeName, probeInfo.getProbeName());
        assertEquals(precision, probeInfo.getPrecision());
        assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());

        String measureURI = probeInfo.getMeasureURI();
        String expectedMeasureURI = Const.createMeasureURI(targetType, targetName, bmClass, bmName);
        assertEquals(expectedMeasureURI, measureURI);
    }

    @Test
    public void testEqualsAndHashCode() {
        String targetName1 = "targetName1";
        String targetName2 = "targetName2";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeName1 = "probeName1";
        String probeName2 = "probeName2";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeName1, precision, xmlRpcUrl);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType, bmClass, bmName, bmDescription, probeName2, precision, xmlRpcUrl);

        ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeName1, precision, xmlRpcUrl);

        ProbeInformation probeInfo4 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeName2, precision, xmlRpcUrl);

        ProbeInformation probeInfo5 = new ProbeInformation(targetName2, targetType, bmClass, bmName, bmDescription, probeName1, precision, xmlRpcUrl);

        ProbeInformation probeInfo6 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeName1, precision + 1, xmlRpcUrl);

        ProbeInformation probeInfo7 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeName1, precision, "xmlRpcUrl2");

        assertEquals(probeInfo1, probeInfo3);
        assertEquals(probeInfo1.hashCode(), probeInfo3.hashCode());

        assertNotEquals(probeInfo1, probeInfo2);
        assertNotEquals(probeInfo1.hashCode(), probeInfo2.hashCode());

        assertNotEquals(probeInfo1, probeInfo4);
        assertNotEquals(probeInfo1.hashCode(), probeInfo4.hashCode());

        assertNotEquals(probeInfo1, probeInfo5);
        assertNotEquals(probeInfo1.hashCode(), probeInfo5.hashCode());

        assertNotEquals(probeInfo1, probeInfo6);
        assertNotEquals(probeInfo1.hashCode(), probeInfo6.hashCode());

        assertNotEquals(probeInfo1, probeInfo7);
        assertNotEquals(probeInfo1.hashCode(), probeInfo7.hashCode());
    }

    @Test
    public void testToString() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeName = "probeName";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        String expectedToString = "ProbeInformation{targetName='" + targetName + "', targetType='" + targetType + "', bmClass='" + bmClass + "', bmName='" + bmName + "', bmDescription='" + bmDescription + "', probeDescription='" + probeName + "', precision=1}";
        assertEquals(expectedToString, probeInfo.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullBMClass() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeName = "probeName";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl";

        new ProbeInformation(targetName, targetType, null, bmName, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullBMName() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmDescription = "bmDescription";
        String probeName = "probeName";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl";

        new ProbeInformation(targetName, targetType, bmClass, null, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullBMDescription() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String probeName = "probeName";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl";

        new ProbeInformation(targetName, targetType, bmClass, bmName, null, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullProbeName() {
        String targetName = "targetName";
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl";

        new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, null, precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullTargetName() {
        String targetType = "targetType";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeName = "probeName";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl";

        new ProbeInformation(null, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullTargetType() {
        String targetName = "targetName";
        String bmClass = "bmClass";
        String bmName = "bmName";
        String bmDescription = "bmDescription";
        String probeName = "probeName";
        int precision = 1;
        String xmlRpcUrl = "xmlRpcUrl";

        new ProbeInformation(targetName, null, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
    }
}