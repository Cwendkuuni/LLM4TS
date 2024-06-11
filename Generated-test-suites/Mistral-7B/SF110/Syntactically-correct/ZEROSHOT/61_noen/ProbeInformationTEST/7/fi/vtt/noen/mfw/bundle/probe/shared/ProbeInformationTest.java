package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;

import org.junit.Test;

import fi.vtt.noen.mfw.bundle.common.Const;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

    @Test
    public void testProbeInformationConstructor() {
        String targetName = "TestTarget";
        String targetType = "TestTargetType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeName = "TestProbeName";
        int precision = 3;
        String xmlRpcUrl = "http://test.com/xmlrpc";

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
        String targetName = "TestTarget";
        String targetType = "TestTargetType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeName = "TestProbeName";
        int precision = 3;
        String xmlRpcUrl = "http://test.com/xmlrpc";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        assertEquals(targetName, probeInformation.getTargetName());
        assertEquals(targetType, probeInformation.getTargetType());
        assertEquals(bmClass, probeInformation.getBmClass());
        assertEquals(bmName, probeInformation.getBmName());
        assertEquals(bmDescription, probeInformation.getBmDescription());
        assertEquals(probeName, probeInformation.getProbeName());
        assertEquals(precision, probeInformation.getPrecision());
        assertEquals(xmlRpcUrl, probeInformation.getXmlRpcUrl());

        assertEquals(Const.createMeasureURI(targetType, targetName, bmClass, bmName), probeInformation.getMeasureURI());
    }

    @Test
    public void testEqualsAndHashCode() {
        String targetName1 = "TestTarget1";
        String targetName2 = "TestTarget2";
        String targetType = "TestTargetType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeName1 = "TestProbeName1";
        String probeName2 = "TestProbeName2";
        int precision = 3;
        String xmlRpcUrl = "http://test.com/xmlrpc";

        ProbeInformation probeInformation1 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeName1, precision, xmlRpcUrl);
        ProbeInformation probeInformation2 = new ProbeInformation(targetName2, targetType, bmClass, bmName, bmDescription, probeName2, precision, xmlRpcUrl);

        ProbeInformation probeInformation3 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeName1, precision, xmlRpcUrl);

        ProbeInformation probeInformation4 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeName2, precision, xmlRpcUrl);

        ProbeInformation probeInformation5 = new ProbeInformation(targetName1, targetType, bmClass, bmName, bmDescription, probeName1, precision, "http://test2.com/xmlrpc");

        ProbeInformation probeInformation6 = new ProbeInformation(targetName2, targetType, bmClass, bmName, bmDescription, probeName1, precision, xmlRpcUrl);

        ProbeInformation probeInformation7 = new ProbeInformation("TestTarget1", targetType, bmClass, bmName, bmDescription, probeName1, precision, xmlRpcUrl);

        assertEquals(probeInformation1, probeInformation3);
        assertEquals(probeInformation1.hashCode(), probeInformation3.hashCode());

        assertFalse(probeInformation1.equals(probeInformation2));
        assertFalse(probeInformation1.hashCode(), probeInformation2.hashCode());

        assertFalse(probeInformation1.equals(probeInformation4));
        assertFalse(probeInformation1.hashCode(), probeInformation4.hashCode());

        assertFalse(probeInformation1.equals(probeInformation5));
        assertFalse(probeInformation1.hashCode(), probeInformation5.hashCode());

        assertFalse(probeInformation1.equals(probeInformation6));
        assertFalse(probeInformation1.hashCode(), probeInformation6.hashCode());

        assertFalse(probeInformation1.equals(probeInformation7));
        assertFalse(probeInformation1.hashCode(), probeInformation7.hashCode());
    }

    @Test
    public void testToString() {
        String targetName = "TestTarget";
        String targetType = "TestTargetType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";
        String bmDescription = "TestBMDescription";
        String probeName = "TestProbeName";
        int precision = 3;
        String xmlRpcUrl = "http://test.com/xmlrpc";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);

        String expectedToString = "ProbeInformation{targetName='TestTarget', targetType='TestTargetType', bmClass='TestBMClass', bmName='TestBMName', bmDescription='TestBMDescription', probeDescription='TestProbeName', precision=3}";

        assertEquals(expectedToString, probeInformation.toString());
    }

    @Test
    public void testGetMeasureURI() {
        String targetName = "TestTarget";
        String targetType = "TestTargetType";
        String bmClass = "TestBMClass";
        String bmName = "TestBMName";

        ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, null, null, 3, null);

        String expectedMeasureURI = Const.createMeasureURI(targetType, targetName, bmClass, bmName);

        assertEquals(expectedMeasureURI, probeInformation.getMeasureURI());
    }
}