package fi.vtt.noen.mfw.bundle.probe.shared.test;

import static org.junit.Assert.*;

import fi.vtt.noen.mfw.bundle.common.*;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

 private ProbeInformation probeInformation;

 @org.junit.Before
 public void setUp() {
 probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 }

 // Test methods for getter methods

 @org.junit.Test
 public void testGetTargetType() {
 String expected = "TargetType1";
 String actual = probeInformation.getTargetType();
 assertEquals(expected, actual);
 }

 @org.junit.Test
 public void testGetBmName() {
 String expected = "BMName1";
 String actual = probeInformation.getBmName();
 assertEquals(expected, actual);
 }

 @org.junit.Test
 public void testGetProbeName() {
 String expected = "ProbeDescription1";
 String actual = probeInformation.getProbeName();
 assertEquals(expected, actual);
 }

 @org.junit.Test
 public void testGetTargetName() {
 String expected = "TargetName1";
 String actual = probeInformation.getTargetName();
 assertEquals(expected, actual);
 }

 @org.junit.Test
 public void testGetBmClass() {
 String expected = "BMClass1";
 String actual = probeInformation.getBmClass();
 assertEquals(expected, actual);
 }

 @org.junit.Test
 public void testGetBmDescription() {
 String expected = "BMDescription1";
 String actual = probeInformation.getBmDescription();
 assertEquals(expected, actual);
 }

 @org.junit.Test
 public void testGetPrecision() {
 int expected = 1;
 int actual = probeInformation.getPrecision();
 assertEquals(expected, actual);
 }

 @org.junit.Test
 public void testGetMeasureURI() {
 String expected = Const.createMeasureURI("TargetType1", "TargetName1", "BMClass1", "BMName1");
 String actual = probeInformation.getMeasureURI();
 assertEquals(expected, actual);
 }

 @org.junit.Test
 public void testGetXmlRpcUrl() {
 String expected = "XmlRpcUrl1";
 String actual = probeInformation.getXmlRpcUrl();
 assertEquals(expected, actual);
 }

 // Test methods for edge cases and exception handling

 @org.junit.Test(expected = NullPointerException.class)
 public void testConstructorWithNullTargetName() {
 new ProbeInformation(null, "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 }

 @org.junit.Test(expected = NullPointerException.class)
 public void testConstructorWithNullTargetType() {
 new ProbeInformation("TargetName1", null, "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 }

 @org.junit.Test(expected = NullPointerException.class)
 public void testConstructorWithNullBmClass() {
 new ProbeInformation("TargetName1", "TargetType1", null, "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 }

 @org.junit.Test(expected = NullPointerException.class)
 public void testConstructorWithNullBmName() {
 new ProbeInformation("TargetName1", "TargetType1", "BMClass1", null, "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 }

 @org.junit.Test(expected = NullPointerException.class)
 public void testConstructorWithNullBmDescription() {
 new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", null, "ProbeDescription1", 1, "XmlRpcUrl1");
 }

 @org.junit.Test(expected = NullPointerException.class)
 public void testConstructorWithNullProbeDescription() {
 new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", null, 1, "XmlRpcUrl1");
 }

 @org.junit.Test(expected = NumberFormatException.class)
 public void testConstructorWithInvalidPrecision() {
 new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", -1, "XmlRpcUrl1");
 }

 @org.junit.Test(expected = NullPointerException.class)
 public void testConstructorWithNullXmlRpcUrl() {
 new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, null);
 }

 @org.junit.Test
 public void testEqualsWithSameObject() {
 ProbeInformation probeInformation1 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 ProbeInformation probeInformation2 = probeInformation1;
 assertTrue(probeInformation1.equals(probeInformation2));
 }

 @org.junit.Test
 public void testEqualsWithDifferentObject() {
 ProbeInformation probeInformation1 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 ProbeInformation probeInformation2 = new ProbeInformation("TargetName2", "TargetType2", "BMClass2", "BMName2", "BMDescription2", "ProbeDescription2", 2, "XmlRpcUrl2");
 assertFalse(probeInformation1.equals(probeInformation2));
 }

 @org.junit.Test
 public void testEqualsWithDifferentBMClass() {
 ProbeInformation probeInformation1 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 ProbeInformation probeInformation2 = new ProbeInformation("TargetName1", "TargetType1", "BMClass2", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 assertFalse(probeInformation1.equals(probeInformation2));
 }

 @org.junit.Test
 public void testEqualsWithDifferentBmDescription() {
 ProbeInformation probeInformation1 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 ProbeInformation probeInformation2 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription2", "ProbeDescription1", 1, "XmlRpcUrl1");
 assertFalse(probeInformation1.equals(probeInformation2));
 }

 @org.junit.Test
 public void testEqualsWithDifferentBmName() {
 ProbeInformation probeInformation1 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 ProbeInformation probeInformation2 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName2", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 assertFalse(probeInformation1.equals(probeInformation2));
 }

 @org.junit.Test
 public void testEqualsWithDifferentProbeName() {
 ProbeInformation probeInformation1 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 ProbeInformation probeInformation2 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription2", 1, "XmlRpcUrl1");
 assertFalse(probeInformation1.equals(probeInformation2));
 }

 @org.junit.Test
 public void testEqualsWithDifferentTargetName() {
 ProbeInformation probeInformation1 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 ProbeInformation probeInformation2 = new ProbeInformation("TargetName2", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 assertFalse(probeInformation1.equals(probeInformation2));
 }

 @org.junit.Test
 public void testEqualsWithDifferentTargetType() {
 ProbeInformation probeInformation1 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 ProbeInformation probeInformation2 = new ProbeInformation("TargetName1", "TargetType2", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 assertFalse(probeInformation1.equals(probeInformation2));
 }

 @org.junit.Test
 public void testHashCodeWithSameObjects() {
 ProbeInformation probeInformation1 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 ProbeInformation probeInformation2 = probeInformation1;
 int expected = probeInformation1.hashCode();
 int actual = probeInformation2.hashCode();
 assertEquals(expected, actual);
 }

 @org.junit.Test
 public void testHashCodeWithDifferentObjects() {
 ProbeInformation probeInformation1 = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 ProbeInformation probeInformation2 = new ProbeInformation("TargetName2", "TargetType2", "BMClass2", "BMName2", "BMDescription2", "ProbeDescription2", 2, "XmlRpcUrl2");
 int expected = probeInformation1.hashCode();
 int actual = probeInformation2.hashCode();
 assertNotSame(expected, actual);
 }

}