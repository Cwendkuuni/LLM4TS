package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.*;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

 private String targetName;
 private String targetType;
 private String bmClass;
 private String bmName;
 private String bmDescription;
 private String probeName;
 private int precision;
 private String xmlRpcUrl;

 @Test
 public void testConstructor() {
 ProbeInformation probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 assertEquals("targetName1", probeInformation.getTargetName());
 assertEquals("targetType1", probeInformation.getTargetType());
 assertEquals("bmClass1", probeInformation.getBmClass());
 assertEquals("bmName1", probeInformation.getBmName());
 assertEquals("bmDescription1", probeInformation.getBmDescription());
 assertEquals("probeName1", probeInformation.getProbeName());
 assertEquals(1, probeInformation.getPrecision());
 assertEquals("xmlRpcUrl1", probeInformation.getXmlRpcUrl());
 }

 @Test
 public void testGetTargetType() {
 ProbeInformation probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 assertEquals("targetType1", probeInformation.getTargetType());
 }

 @Test
 public void testGetBmName() {
 ProbeInformation probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 assertEquals("bmName1", probeInformation.getBmName());
 }

 @Test
 public void testGetProbeName() {
 ProbeInformation probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 assertEquals("probeName1", probeInformation.getProbeName());
 }

 @Test
 public void testGetTargetName() {
 ProbeInformation probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 assertEquals("targetName1", probeInformation.getTargetName());
 }

 @Test
 public void testGetBmClass() {
 ProbeInformation probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 assertEquals("bmClass1", probeInformation.getBmClass());
 }

 @Test
 public void testGetBmDescription() {
 ProbeInformation probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 assertEquals("bmDescription1", probeInformation.getBmDescription());
 }

 @Test
 public void testGetPrecision() {
 ProbeInformation probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 assertEquals(1, probeInformation.getPrecision());
 }

 @Test
 public void testGetMeasureURI() {
 ProbeInformation probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 assertEquals("measureURI1", probeInformation.getMeasureURI());
 }

 @Test
 public void testGetXmlRpcUrl() {
 ProbeInformation probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 assertEquals("xmlRpcUrl1", probeInformation.getXmlRpcUrl());
 }

 @Test
 public void testToString() {
 ProbeInformation probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 String expected = "ProbeInformation{targetName='targetName1', targetType='targetType1', bmClass='bmClass1', bmName='bmName1', bmDescription='bmDescription1', probeDescription='probeName1', precision=1}";
 assertEquals(expected, probeInformation.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullTargetName() {
 ProbeInformation probeInformation = new ProbeInformation(null, "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullTargetType() {
 ProbeInformation probeInformation = new ProbeInformation("targetName1", null, "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullBmClass() {
 ProbeInformation probeInformation = new ProbeInformation("targetName1", "targetType1", null, "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullBmName() {
 ProbeInformation probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", null, "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullBmDescription() {
 ProbeInformation probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", null, "probeName1", 1, "xmlRpcUrl1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullProbeName() {
 ProbeInformation probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", null, 1, "xmlRpcUrl1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNegativePrecision() {
 ProbeInformation probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", -1, "xmlRpcUrl1");
 }

 @Test
 public void testEqualsSameObject() {
 ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 ProbeInformation probeInformation2 = probeInformation1;
 assertTrue(probeInformation1.equals(probeInformation2));
 }

 @Test
 public void testEqualsNullObject() {
 ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 assertFalse(probeInformation1.equals(null));
 }

 @Test
 public void testEqualsDifferentClass() {
 ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 Object otherObject = new Object();
 assertFalse(probeInformation1.equals(otherObject));
 }

 @Test
 public void testEqualsDifferentTargetName() {
 ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 ProbeInformation probeInformation2 = new ProbeInformation("targetName2", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 assertFalse(probeInformation1.equals(probeInformation2));
 }

 @Test
 public void testEqualsDifferentTargetType() {
 ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 ProbeInformation probeInformation2 = new ProbeInformation("targetName1", "targetType2", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 assertFalse(probeInformation1.equals(probeInformation2));
 }

 @Test
 public void testEqualsDifferentBmClass() {
 ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 ProbeInformation probeInformation2 = new ProbeInformation("targetName1", "targetType1", "bmClass2", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 assertFalse(probeInformation1.equals(probeInformation2));
 }

 @Test
 public void testEqualsDifferentBmName() {
 ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 ProbeInformation probeInformation2 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName2", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 assertFalse(probeInformation1.equals(probeInformation2));
 }

 @Test
 public void testEqualsDifferentBmDescription() {
 ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 ProbeInformation probeInformation2 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription2", "probeName1", 1, "xmlRpcUrl1");
 assertFalse(probeInformation1.equals(probeInformation2));
 }

 @Test
 public void testEqualsDifferentProbeName() {
 ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 ProbeInformation probeInformation2 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName2", 1, "xmlRpcUrl1");
 assertFalse(probeInformation1.equals(probeInformation2));
 }

 @Test
 public void testEqualsSameValues() {
 ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 ProbeInformation probeInformation2 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 assertTrue(probeInformation1.equals(probeInformation2));
 }

 @Test
 public void testHashCodeSameObject() {
 ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 ProbeInformation probeInformation2 = probeInformation1;
 assertEquals(probeInformation1.hashCode(), probeInformation2.hashCode());
 }

 @Test
 public void testHashCodeDifferentObjects() {
 ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 ProbeInformation probeInformation2 = new ProbeInformation("targetName2", "targetType2", "bmClass2", "bmName2", "bmDescription2", "probeName2", 2, "xmlRpcUrl2");
 assertNotEquals(probeInformation1.hashCode(), probeInformation2.hashCode());
 }

 @Test
 public void testHashCodeSameValues() {
 ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 ProbeInformation probeInformation2 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 assertEquals(probeInformation1.hashCode(), probeInformation2.hashCode());
 }

}