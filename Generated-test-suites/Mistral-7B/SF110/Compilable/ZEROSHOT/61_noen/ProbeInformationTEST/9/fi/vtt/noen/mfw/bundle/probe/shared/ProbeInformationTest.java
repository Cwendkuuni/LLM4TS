package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;

import org.junit.Test;

import fi.vtt.noen.mfw.bundle.common.Const;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

 private String targetName = "TestTarget";
 private String targetType = "TestType";
 private String bmClass = "TestBMClass";
 private String bmName = "TestBMName";
 private String bmDescription = "TestBMDescription";
 private String probeName = "TestProbeName";
 private int precision = 1;
 private String xmlRpcUrl = "http://test.com";

 @Test
 public void testProbeInformationConstructor() {
 ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 assertNotNull(probeInformation);
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
 ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 assertEquals(targetType, probeInformation.getTargetType());
 assertEquals(bmName, probeInformation.getBmName());
 assertEquals(probeName, probeInformation.getProbeName());
 assertEquals(targetName, probeInformation.getTargetName());
 assertEquals(bmClass, probeInformation.getBmClass());
 assertEquals(bmDescription, probeInformation.getBmDescription());
 assertEquals(precision, probeInformation.getPrecision());
 }

 @Test
 public void testToString() {
 ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 assertEquals("ProbeInformation{targetName='TestTarget', targetType='TestType', bmClass='TestBMClass', bmName='TestBMName', bmDescription='TestBMDescription', probeDescription='TestProbeName', precision=1}", probeInformation.toString());
 }

 @Test
 public void testEquals() {
 ProbeInformation probeInformation1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 ProbeInformation probeInformation2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 assertTrue(probeInformation1.equals(probeInformation2));

 ProbeInformation probeInformation3 = new ProbeInformation("TestTarget2", targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 assertFalse(probeInformation1.equals(probeInformation3));

 ProbeInformation probeInformation4 = null;
 assertFalse(probeInformation1.equals(probeInformation4));
 }

 @Test
 public void testHashCode() {
 ProbeInformation probeInformation1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 ProbeInformation probeInformation2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 assertEquals(probeInformation1.hashCode(), probeInformation2.hashCode());

 ProbeInformation probeInformation3 = new ProbeInformation("TestTarget2", targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 assertNotEquals(probeInformation1.hashCode(), probeInformation3.hashCode());

 ProbeInformation probeInformation4 = null;
 assertNotEquals(probeInformation1.hashCode(), probeInformation4.hashCode());
 }

 @Test
 public void testMeasureURI() {
 ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 assertEquals(Const.createMeasureURI(targetType, targetName, bmClass, bmName), probeInformation.getMeasureURI());
 }

 @Test
 public void testGetXmlRpcUrl() {
 ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 assertEquals(xmlRpcUrl, probeInformation.getXmlRpcUrl());
 }
}