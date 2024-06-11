package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;

import org.junit.Test;

import fi.vtt.noen.mfw.bundle.common.Const;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @Test
    public void testProbeInformationConstructor() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(targetName, probeInfo.getTargetName());
        assertEquals(targetType, probeInfo.getTargetType());
        assertEquals(bmClass, probeInfo.getBmClass());
        assertEquals(bmName, probeInfo.getBmName());
        assertEquals(bmDescription, probeInfo.getBmDescription());
        assertEquals(probeName, probeInfo.getProbeName());
        assertEquals(precision, probeInfo.getPrecision());
        assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());
        assertEquals(probeInfo.toString(), "ProbeInformation{targetName='" + targetName + "', targetType='" + targetType + "', bmClass='" + bmClass + "', bmName='" + bmName + "', bmDescription='" + bmDescription + "', probeDescription='" + probeName + "', precision=" + precision + "}");
    }

    @Test
    public void testGetMethods() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(targetName, probeInfo.getTargetName());
        assertEquals(targetType, probeInfo.getTargetType());
        assertEquals(bmClass, probeInfo.getBmClass());
        assertEquals(bmName, probeInfo.getBmName());
        assertEquals(bmDescription, probeInfo.getBmDescription());
        assertEquals(probeName, probeInfo.getProbeName());
        assertEquals(precision, probeInfo.getPrecision());
        assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());

        assertEquals(Const.createMeasureURI(targetType, targetName, bmClass, bmName), probeInfo.getMeasureURI());
    }

    @Test
    public void testEquals() {
        String targetName1 = "testTargetName1";
        String targetName2 = "testTargetName2";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeName1 = "testProbeName1";
        String probeName2 = "testProbeName2";
        int precision = 1;
        String xmlRpcUrl1 = "testXmlRpcUrl1";
        String xmlRpcUrl2 = "testXmlRpcUrl2";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeName1, precision, xmlRpcUrl1);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType, bmClass, bmName, bmDescription, probeName2, precision, xmlRpcUrl2);

        assertFalse(probeInfo1.equals(null));
        assertFalse(probeInfo1.equals(""));
        assertFalse(probeInfo1.equals(new Object()));

        ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeName1, precision, xmlRpcUrl1);
        assertTrue(probeInfo1.equals(probeInfo3));

        ProbeInformation probeInfo4 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeName2, precision, xmlRpcUrl1);
        assertFalse(probeInfo1.equals(probeInfo4));

        ProbeInformation probeInfo5 = new ProbeInformation(targetName2, targetType, bmClass, bmName, bmDescription, probeName1, precision, xmlRpcUrl2);
        assertFalse(probeInfo1.equals(probeInfo5));

        ProbeInformation probeInfo6 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeName1, precision, xmlRpcUrl1);
        assertTrue(probeInfo1.equals(probeInfo6));
    }

    @Test
    public void testHashCode() {
        String targetName = "testTargetName";
        String targetType = "testTargetType";
        String bmClass = "testBmClass";
        String bmName = "testBmName";
        String bmDescription = "testBmDescription";
        String probeName = "testProbeName";
        int precision = 1;
        String xmlRpcUrl = "testXmlRpcUrl";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        int hashCode = probeInfo.hashCode();

        String newTargetName = "newTargetName";
        String newTargetType = "newTargetType";
        String newBmClass = "newBmClass";
        String newBmName = "newBmName";
        String newBmDescription = "newBmDescription";
        String newProbeName = "newProbeName";
        int newPrecision = 2;
        String newXmlRpcUrl = "newXmlRpcUrl";

        ProbeInformation newProbeInfo = new ProbeInformation(newTargetName, newTargetType, newBmClass, newBmName, newBmDescription, newProbeName, newPrecision, newXmlRpcUrl);

        int newHashCode = newProbeInfo.hashCode();

        assertEquals(hashCode, probeInfo.hashCode());
        assertNotSame(hashCode, newHashCode);
    }
}