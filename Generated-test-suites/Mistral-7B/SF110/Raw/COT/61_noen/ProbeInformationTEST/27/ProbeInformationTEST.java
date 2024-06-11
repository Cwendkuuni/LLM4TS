package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import fi.vtt.noen.mfw.bundle.common.Const;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @Test
    public void testProbeInformationConstructor() {
        String targetName = "TestTargetName";
        String targetType = "TestTargetType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeDescription = "TestProbeDescription";
        int precision = 4;
        String xmlRpcUrl = "TestXmlRpcUrl";

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
    public void testProbeInformationConstructorNullTargetName() {
        String targetType = "TestTargetType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeDescription = "TestProbeDescription";
        int precision = 4;
        String xmlRpcUrl = "TestXmlRpcUrl";

        new ProbeInformation(null, targetType, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test(expected = NullPointerException.class)
    public void testProbeInformationConstructorNullTargetType() {
        String targetName = "TestTargetName";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeDescription = "TestProbeDescription";
        int precision = 4;
        String xmlRpcUrl = "TestXmlRpcUrl";

        new ProbeInformation(targetName, null, bmClass, bmName, bmDescription, probeDescription, precision, xmlRpcUrl);
    }

    @Test
    public void testGetMeasureURI() {
        String targetType = "TestTargetType";
        String targetName = "TestTargetName";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";

        ProbeInformation probeInfo = new ProbeInformation("TestTargetName", targetType, bmClass, bmName, null, null, 4, null);

        assertEquals(Const.createMeasureURI(targetType, targetName, bmClass, bmName), probeInfo.getMeasureURI());
    }

    @Test
    public void testEquals() {
        String targetName1 = "TestTargetName1";
        String targetName2 = "TestTargetName2";
        String targetType = "TestTargetType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeName1 = "TestProbeName1";
        String probeName2 = "TestProbeName2";
        int precision = 4;
        String xmlRpcUrl = "TestXmlRpcUrl";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeName1, precision, xmlRpcUrl);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType, bmClass, bmName, bmDescription, probeName2, precision, xmlRpcUrl);

        assertTrue(probeInfo1.equals(probeInfo1));
        assertTrue(probeInfo1.equals(probeInfo2));

        ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeName1, precision, null);
        assertFalse(probeInfo1.equals(probeInfo3));

        ProbeInformation probeInfo4 = new ProbeInformation("TestTargetName1", targetType, bmClass, bmName, bmDescription, probeName1, precision, xmlRpcUrl);
        assertFalse(probeInfo1.equals(probeInfo4));

        ProbeInformation probeInfo5 = new ProbeInformation(targetName1, targetType, bmClass, bmName, null, probeName1, precision, xmlRpcUrl);
        assertFalse(probeInfo1.equals(probeInfo5));

        ProbeInformation probeInfo6 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeName1, precision, xmlRpcUrl);
        probeInfo6.setPrecision(5);
        assertFalse(probeInfo1.equals(probeInfo6));
    }

    @Test
    public void testHashCode() {
        String targetName = "TestTargetName";
        String targetType = "TestTargetType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeName = "TestProbeName";
        int precision = 4;

        ProbeInformation probeInfo1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, null);
        int hashCode1 = probeInfo1.hashCode();

        ProbeInformation probeInfo2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, null);
        int hashCode2 = probeInfo2.hashCode();

        assertEquals(hashCode1, hashCode2);

        ProbeInformation probeInfo3 = new ProbeInformation(targetName, targetType, bmClass, bmName, null, probeName, precision, null);
        int hashCode3 = probeInfo3.hashCode();

        assertNotEquals(hashCode1, hashCode3);

        ProbeInformation probeInfo4 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, "TestXmlRpcUrl");
        int hashCode4 = probeInfo4.hashCode();

        assertNotEquals(hashCode1, hashCode4);

        ProbeInformation probeInfo5 = new ProbeInformation("TestTargetName", targetType, bmClass, bmName, bmDescription, probeName, precision, null);
        int hashCode5 = probeInfo5.hashCode();

        assertNotEquals(hashCode1, hashCode5);

        ProbeInformation probeInfo6 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision + 1, null);
        int hashCode6 = probeInfo6.hashCode();

        assertNotEquals(hashCode1, hashCode6);
    }

    @Test
    public void testToString() {
        String targetName = "TestTargetName";
        String targetType = "TestTargetType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeName = "TestProbeName";
        int precision = 4;

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, null);

        String expectedResult = "ProbeInformation{targetName='TestTargetName', targetType='TestTargetType', bmClass='TestBMClass', bmName='TestBMName', bmDescription='TestBMDescription', probeDescription='TestProbeName', precision=4}";
        assertEquals(expectedResult, probeInfo.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToStringNullTargetName() {
        String targetType = "TestTargetType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeName = "TestProbeName";
        int precision = 4;

        ProbeInformation probeInfo = new ProbeInformation(null, targetType, bmClass, bmName, bmDescription, probeName, precision, null);

        probeInfo.toString();
    }
}