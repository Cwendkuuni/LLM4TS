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
 assertNotNull(probeInformation);
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
 ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 1, "xmlRpcUrl");
 assertEquals("targetType", probeInformation.getTargetType());
 }

 @Test
 public void testGetBmName() {
 ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 1, "xmlRpcUrl");
 assertEquals("bmName", probeInformation.getBmName());
 }

 @Test
 public void testGetProbeName() {
 ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 1, "xmlRpcUrl");
 assertEquals("probeName", probeInformation.getProbeName());
 }

 @Test
 public void testGetTargetName() {
 ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 1, "xmlRpcUrl");
 assertEquals("targetName", probeInformation.getTargetName());
 }

 @Test
 public void testGetBmClass() {
 ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 1, "xmlRpcUrl");
 assertEquals("bmClass", probeInformation.getBmClass());
 }

 @Test
 public void testGetBmDescription() {
 ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 1, "xmlRpcUrl");
 assertEquals("bmDescription", probeInformation.getBmDescription());
 }

 @Test
 public void testGetPrecision() {
 ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 1, "xmlRpcUrl");
 assertEquals(1, probeInformation.getPrecision());
 }

 @Test
 public void testGetMeasureURI() {
 ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 1, "xmlRpcUrl");
 assertEquals(Const.createMeasureURI("targetType", "targetName", "bmClass", "bmName"), probeInformation.getMeasureURI());
 }

 @Test
 public void testGetXmlRpcUrl() {
 ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 1, "xmlRpcUrl");
 assertEquals("xmlRpcUrl", probeInformation.getXmlRpcUrl());
 }

 @Test(expected = NullPointerException.class)
 public void testNullConstructor() {
 new ProbeInformation(null, null, null, null, null, null, 0, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullTargetName() {
 ProbeInformation probeInformation = new ProbeInformation(null, "targetType", "bmClass", "bmName", "bmDescription", "probeName", 1, "xmlRpcUrl");
 assertNull(probeInformation.getTargetName());
 }

 @Test(expected = NullPointerException.class)
 public void testNullTargetType() {
 ProbeInformation probeInformation = new ProbeInformation("targetName", null, "bmClass", "bmName", "bmDescription", "probeName", 1, "xmlRpcUrl");
 assertNull(probeInformation.getTargetType());
 }

 @Test(expected = NullPointerException.class)
 public void testNullBmClass() {
 ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", null, "bmName", "bmDescription", "probeName", 1, "xmlRpcUrl");
 assertNull(probeInformation.getBmClass());
 }

 @Test(expected = NullPointerException.class)
 public void testNullBmName() {
 ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", null, "bmDescription", "probeName", 1, "xmlRpcUrl");
 assertNull(probeInformation.getBmName());
 }

 @Test(expected = NullPointerException.class)
 public void testNullBmDescription() {
 ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", null, "probeName", 1, "xmlRpcUrl");
 assertNull(probeInformation.getBmDescription());
 }

 @Test(expected = NullPointerException.class)
 public void testNullProbeName() {
 ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", null, 1, "xmlRpcUrl");
 assertNull(probeInformation.getProbeName());
 }

 @Test(expected = NullPointerException.class)
 public void testNullPrecision() {
 ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 0, "xmlRpcUrl");
 assertNull(probeInformation.getPrecision());
 }

 @Test(expected = NullPointerException.class)
 public void testNullXmlRpcUrl() {
 ProbeInformation probeInformation = new ProbeInformation("targetName", "targetType", "bmClass", "bmName", "bmDescription", "probeName", 1, null);
 assertNull(probeInformation.getXmlRpcUrl());
 }

 @Test
 public void testEquals() {
 ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 ProbeInformation probeInformation2 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 assertTrue(probeInformation1.equals(probeInformation2));
 ProbeInformation probeInformation3 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName2", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 assertFalse(probeInformation1.equals(probeInformation3));
 }

 @Test
 public void testHashCode() {
 ProbeInformation probeInformation1 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 ProbeInformation probeInformation2 = new ProbeInformation("targetName1", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 assertEquals(probeInformation1.hashCode(), probeInformation2.hashCode());
 ProbeInformation probeInformation3 = new ProbeInformation("targetName2", "targetType1", "bmClass1", "bmName1", "bmDescription1", "probeName1", 1, "xmlRpcUrl1");
 assertNotEquals(probeInformation1.hashCode(), probeInformation3.hashCode());
 }
}