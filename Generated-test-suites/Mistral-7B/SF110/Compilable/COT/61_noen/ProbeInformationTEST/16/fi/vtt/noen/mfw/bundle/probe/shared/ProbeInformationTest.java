package fi.vtt.noen.mfw.bundle.probe.shared;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProbeInformationTest {

 private ProbeInformation probeInformation;

 @Test
 public void testConstructor() {
 probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
 assertEquals("ProbeInformation{targetName='targetName1', targetType='targetType1', bmClass='bmClass1', bmName='bmName1', bmDescription='bmDescription1', probeDescription='probeDescription1', precision=1}", probeInformation.toString());
 }

 @Test
 public void testGetTargetType() {
 probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
 assertEquals("targetType1", probeInformation.getTargetType());
 }

 @Test
 public void testGetBmName() {
 probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
 assertEquals("bmName1", probeInformation.getBmName());
 }

 @Test
 public void testGetProbeName() {
 probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
 assertEquals("probeDescription1", probeInformation.getProbeName());
 }

 @Test
 public void testGetTargetName() {
 probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
 assertEquals("targetName1", probeInformation.getTargetName());
 }

 @Test
 public void testGetBmClass() {
 probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
 assertEquals("bmClass1", probeInformation.getBmClass());
 }

 @Test
 public void testGetBmDescription() {
 probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
 assertEquals("bmDescription1", probeInformation.getBmDescription());
 }

 @Test
 public void testGetPrecision() {
 probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
 assertEquals(1, probeInformation.getPrecision());
 }

 @Test
 public void testGetMeasureURI() {
 probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
 assertEquals("targetType1:targetName1:bmClass1:bmName1", probeInformation.getMeasureURI());
 }

 @Test
 public void testGetXmlRpcUrl() {
 probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
 assertEquals("xmlRpcUrl1", probeInformation.getXmlRpcUrl());
 }

 // Edge cases and exception handling tests

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullTargetName() {
 probeInformation = new ProbeInformation(null, "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullTargetType() {
 probeInformation = new ProbeInformation("targetName1", null, "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullBmClass() {
 probeInformation = new ProbeInformation("targetName1", "targetType1", null, "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullBmName() {
 probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", null, "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullBmDescription() {
 probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", null, "probeDescription1", 1, "xmlRpcUrl1");
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullProbeDescription() {
 probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", null, 1, "xmlRpcUrl1");
 }

 @Test(expected = NumberFormatException.class)
 public void testConstructorWithInvalidPrecision() {
 probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", -1, "xmlRpcUrl1");
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullXmlRpcUrl() {
 probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, null);
 }

 @Test
 public void testEqualsWithNullObject() {
 probeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
 assertFalse(probeInformation.equals(null));
 }

 @Test
 public void testEqualsWithDifferentClassProbeInformation() {
 ProbeInformation anotherProbeInformation = new ProbeInformation("targetName2", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
 assertFalse(probeInformation.equals(anotherProbeInformation));
 }

 @Test
 public void testEqualsWithSameInstance() {
 ProbeInformation sameInstance = probeInformation;
 assertTrue(probeInformation.equals(sameInstance));
 }

 @Test
 public void testEqualsWithSameValuesButDifferentInstance() {
 ProbeInformation anotherProbeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
 assertTrue(probeInformation.equals(anotherProbeInformation));
 }

 @Test
 public void testHashCode() {
 int firstHashCode = probeInformation.hashCode();
 ProbeInformation anotherProbeInformation = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeDescription1", 1, "xmlRpcUrl1");
 int secondHashCode = anotherProbeInformation.hashCode();
 assertEquals(firstHashCode, secondHashCode);
 }
}