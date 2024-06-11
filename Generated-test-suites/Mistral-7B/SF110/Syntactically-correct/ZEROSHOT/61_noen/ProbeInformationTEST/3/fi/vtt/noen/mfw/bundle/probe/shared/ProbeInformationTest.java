package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import fi.vtt.noen.mfw.bundle.common.Const;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest
{
    @Test
    public void testProbeInformationConstruction()
    {
        String targetName = "TestTarget";
        String targetType = "TestType";
        String bmClass = "TestBM";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeName = "TestProbeName";
        int precision = 1;
        String xmlRpcUrl = "TestXMLRPCUrl";

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
    public void testGetMeasureURI()
    {
        String targetType = "TestType";
        String targetName = "TestTarget";
        String bmClass = "TestBM";
        String bmName = "TestBMName";

        ProbeInformation probeInfo = new ProbeInformation("TestTarget", "TestType", "TestBM", "TestBMName", "TestBMDescription", "TestProbeName", 1, "TestXMLRPCUrl");

        assertEquals(Const.createMeasureURI(targetType, targetName, bmClass, bmName), probeInfo.getMeasureURI());
    }

    @Test
    public void testEquals()
    {
        String targetName1 = "Target1";
        String targetName2 = "Target2";
        String targetType1 = "Type1";
        String targetType2 = "Type2";
        String bmClass1 = "BM1";
        String bmClass2 = "BM2";
        String bmName1 = "BMName1";
        String bmName2 = "BMName2";
        String bmDescription1 = "BMDescription1";
        String bmDescription2 = "BMDescription2";
        String probeName1 = "ProbeName1";
        String probeName2 = "ProbeName2";
        int precision1 = 1;
        int precision2 = 2;
        String xmlRpcUrl1 = "XMLRPCUrl1";
        String xmlRpcUrl2 = "XMLRPCUrl2";

        ProbeInformation probeInfo1 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);
        ProbeInformation probeInfo2 = new ProbeInformation(targetName2, targetType2, bmClass2, bmName2, bmDescription2, probeName2, precision2, xmlRpcUrl2);
        ProbeInformation probeInfo3 = new ProbeInformation(targetName1, targetType1, bmClass1, bmName1, bmDescription1, probeName1, precision1, xmlRpcUrl1);

        assertSame(probeInfo1, probeInfo3);
        assertFalse(probeInfo1.equals(probeInfo2));
    }

    @Test
    public void testHashCode()
    {
        String targetName = "TestTarget";
        String targetType = "TestType";
        String bmClass = "TestBM";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeName = "TestProbeName";
        int precision = 1;
        String xmlRpcUrl = "TestXMLRPCUrl";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        int hashCode = probeInfo.hashCode();

        ProbeInformation probeInfoNew = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(hashCode, probeInfoNew.hashCode());
    }

    @Test
    public void testToString()
    {
        String targetName = "TestTarget";
        String targetType = "TestType";
        String bmClass = "TestBM";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeName = "TestProbeName";
        int precision = 1;
        String xmlRpcUrl = "TestXMLRPCUrl";

        ProbeInformation probeInfo = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        String result = probeInfo.toString();

        String expected = String.format("ProbeInformation{targetName='%s', targetType='%s', bmClass='%s', bmName='%s', bmDescription='%s', probeDescription='%s', precision=%d}", targetName, targetType, bmClass, bmName, bmDescription, probeName, precision);

        assertEquals(expected, result);
    }

    @Test
    public void testEmptyConstructor()
    {
        ProbeInformation probeInfo = new ProbeInformation();
        assertNull(probeInfo.getTargetName());
        assertNull(probeInfo.getTargetType());
        assertNull(probeInfo.getBmClass());
        assertNull(probeInfo.getBmName());
        assertNull(probeInfo.getBmDescription());
        assertNull(probeInfo.getProbeName());
        assertEquals(0, probeInfo.getPrecision());
        assertNull(probeInfo.getXmlRpcUrl());
    }
}