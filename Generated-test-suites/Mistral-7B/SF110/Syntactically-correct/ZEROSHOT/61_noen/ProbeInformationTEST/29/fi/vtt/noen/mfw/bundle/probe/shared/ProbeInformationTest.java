package fi.vtt.noen.mfw.bundle.probe.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class ProbeInformationTest
{
    @Test
    public void testProbeInformationConstructor()
    {
        String targetName = "TestTargetName";
        String targetType = "TestTargetType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeName = "TestProbeName";
        int precision = 1;
        String xmlRpcUrl = "TestXmlRpcUrl";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(targetName, probeInfo.getTargetName());
        assertEquals(targetType, probeInfo.getTargetType());
        assertEquals(bmClass, probeInfo.getBmClass());
        assertEquals(bmName, probeInfo.getBmName());
        assertEquals(bmDescription, probeInfo.getBmDescription());
        assertEquals(probeName, probeInfo.getProbeName());
        assertEquals(precision, probeInfo.getPrecision());
        assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());
    }

    @Test
    public void testGetters()
    {
        String targetName = "TestTargetName";
        String targetType = "TestTargetType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeName = "TestProbeName";
        int precision = 1;
        String xmlRpcUrl = "TestXmlRpcUrl";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(targetName, probeInfo.getTargetName());
        assertEquals(targetType, probeInfo.getTargetType());
        assertEquals(bmClass, probeInfo.getBmClass());
        assertEquals(bmName, probeInfo.getBmName());
        assertEquals(bmDescription, probeInfo.getBmDescription());
        assertEquals(probeName, probeInfo.getProbeName());
        assertEquals(precision, probeInfo.getPrecision());
        assertEquals(xmlRpcUrl, probeInfo.getXmlRpcUrl());

        assertNull(probeInfo.getTargetName(null));
        assertNull(probeInfo.getTargetType(null));
        assertNull(probeInfo.getBmClass(null));
        assertNull(probeInfo.getBmName(null));
        assertNull(probeInfo.getBmDescription(null));
        assertNull(probeInfo.getProbeName(null));
        assertNull(probeInfo.getPrecision(null));
        assertNull(probeInfo.getXmlRpcUrl(null));

        String emptyTargetName = "";
        String emptyTargetType = "";
        String emptyBMClass = "";
        String emptyBMName = "";
        String emptyBMDescription = "";
        String emptyProbeName = "";

        ProbeInformation emptyProbeInfo = new ProbeInformation(emptyTargetName, emptyTargetType, emptyBMClass, emptyBMName, emptyBMDescription, emptyProbeName, precision, xmlRpcUrl);

        assertSame(emptyProbeInfo.getTargetName(), emptyTargetName);
        assertSame(emptyProbeInfo.getTargetType(), emptyTargetType);
        assertSame(emptyProbeInfo.getBmClass(), emptyBMClass);
        assertSame(emptyProbeInfo.getBmName(), emptyBMName);
        assertSame(emptyProbeInfo.getBmDescription(), emptyBMDescription);
        assertSame(emptyProbeInfo.getProbeName(), emptyProbeName);

        ProbeInformation differentProbeInfo1 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeName1", 1, "XmlRpcUrl1");
        ProbeInformation differentProbeInfo2 = new ProbeInformation("TargetName2", "TargetType2", "BMClass1", "BMName1", "BMDescription1", "ProbeName2", 2, "XmlRpcUrl2");

        assertNotSame(probeInfo, differentProbeInfo1);
        assertNotSame(probeInfo, differentProbeInfo2);
    }

    @Test
    public void testToString()
    {
        String targetName = "TestTargetName";
        String targetType = "TestTargetType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeName = "TestProbeName";
        int precision = 1;
        String xmlRpcUrl = "TestXmlRpcUrl";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals("ProbeInformation{targetName='TestTargetName', targetType='TestTargetType', bmClass='TestBMClass', bmName='TestBMName', bmDescription='TestBMDescription', probeDescription='TestProbeName', precision=1}", probeInfo.toString());
    }

    @Test
    public void testEquals()
    {
        String targetName1 = "TestTargetName1";
        String targetName2 = "TestTargetName2";
        String targetType1 = "TestTargetType1";
        String targetType2 = "TestTargetType2";
        String bmClass1 = "TestBMClass1";
        String bmClass2 = "TestBMClass2";
        String bmName1 = "TestBMName1";
        String bmName2 = "TestBMName2";
        String bmDescription1 = "TestBMDescription1";
        String bmDescription2 = "TestBMDescription2";
        String probeName1 = "TestProbeName1";
        String probeName2 = "TestProbeName2";
        int precision1 = 1;
        int precision2 = 2;
        String xmlRpcUrl1 = "TestXmlRpcUrl1";
        String xmlRpcUrl2 = "TestXmlRpcUrl2";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);
        ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        assertTrue(probeInfo1.equals(probeInfo3));
        assertFalse(probeInfo1.equals(probeInfo2));

        ProbeInformation probeInfo4 = new ProbeInformation();
        assertFalse(probeInfo1.equals(probeInfo4));

        ProbeInformation probeInfo5 = new ProbeInformation(targetName1, targetType1, bmClass1, null, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        assertFalse(probeInfo1.equals(probeInfo5));

        ProbeInformation probeInfo6 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, null, probeName1, precision1, xmlRpcUrl1);
        assertFalse(probeInfo1.equals(probeInfo6));

        ProbeInformation probeInfo7 = new ProbeInformation(targetName1, targetType1, null, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        assertFalse(probeInfo1.equals(probeInfo7));

        ProbeInformation probeInfo8 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName2, precision1, xmlRpcUrl1);
        assertFalse(probeInfo1.equals(probeInfo8));

        ProbeInformation probeInfo9 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision2, xmlRpcUrl1);
        assertFalse(probeInfo1.equals(probeInfo9));

        ProbeInformation probeInfo10 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, null);
        assertFalse(probeInfo1.equals(probeInfo10));

        ProbeInformation probeInfo11 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl2);
        assertFalse(probeInfo1.equals(probeInfo11));
    }

    @Test
    public void testHashCode()
    {
        String targetName = "TestTargetName";
        String targetType = "TestTargetType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeName = "TestProbeName";
        int precision = 1;
        String xmlRpcUrl = "TestXmlRpcUrl";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        int hashCode1 = probeInfo.hashCode();

        String differentTargetName = "DifferentTargetName";
        String differentTargetType = "DifferentTargetType";
        String differentBMClass = "DifferentBMClass";
        String differentBMName = "DifferentBMName";
        String differentBMDescription = "DifferentBMDescription";
        String differentProbeName = "DifferentProbeName";

        ProbeInformation differentProbeInfo = new ProbeInformation(differentTargetName, differentTargetType, differentBMClass, differentBMName, differentBMDescription, differentProbeName, precision, xmlRpcUrl);

        int hashCode2 = differentProbeInfo.hashCode();

        assertEquals(hashCode1, hashCode1);
        assertEquals(hashCode2, hashCode2);
        assertNotEqual(hashCode1, hashCode2);

        ProbeInformation probeInfo2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(hashCode1, hashCode1);
        assertEquals(hashCode1, hashCode2);
        assertNotEqual(hashCode1, hashCode2);

        ProbeInformation probeInfo3 = new ProbeInformation();

        assertNotEqual(hashCode1, hashCode3);

        ProbeInformation probeInfo4 = new ProbeInformation(targetName, targetType, bmClass, null, bmDescription, probeName, precision, xmlRpcUrl);

        assertNotEqual(hashCode1, hashCode4);

        ProbeInformation probeInfo5 = new ProbeInformation(targetName, targetType, bmClass, bmName, null, probeName, precision, xmlRpcUrl);

        assertNotEqual(hashCode1, hashCode5);

        ProbeInformation probeInfo6 = new ProbeInformation(targetName, targetType, null, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertNotEqual(hashCode1, hashCode6);

        ProbeInformation probeInfo7 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, null);

        assertNotEqual(hashCode1, hashCode7);

        ProbeInformation probeInfo8 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(hashCode1, hashCode8);

        ProbeInformation probeInfo9 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(hashCode1, hashCode9);

        ProbeInformation probeInfo10 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(hashCode1, hashCode10);

        ProbeInformation probeInfo11 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(hashCode1, hashCode11);
    }
}