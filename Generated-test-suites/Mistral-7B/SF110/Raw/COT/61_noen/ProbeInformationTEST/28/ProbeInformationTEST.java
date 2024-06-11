import org.junit.Test;
import org.junit.Assert.*;
import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

 private String targetName;
 private String targetType;
 private String bmClass;
 private String bmName;
 private String bmDescription;
 private String probeDescription;
 private int precision;
 private String xmlRpcUrl;

 @Test
 public void testConstructor() {
 ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 this.targetName = "TargetName1";
 this.targetType = "TargetType1";
 this.bmClass = "BMClass1";
 this.bmName = "BMName1";
 this.bmDescription = "BMDescription1";
 this.probeDescription = "ProbeDescription1";
 this.precision = 1;
 this.xmlRpcUrl = "XmlRpcUrl1";

 ProbeInformation probeInformation1 = new ProbeInformation("TargetName2", "TargetType2", "BMClass2", "BMName2", "BMDescription2", "ProbeDescription2", 2, "XmlRpcUrl2");

 assertEquals(this.targetName, probeInformation.getTargetName());
 assertEquals(this.targetType, probeInformation.getTargetType());
 assertEquals(this.bmClass, probeInformation.getBmClass());
 assertEquals(this.bmName, probeInformation.getBmName());
 assertEquals(this.bmDescription, probeInformation.getBmDescription());
 assertEquals(this.probeDescription, probeInformation.getProbeName());
 assertEquals(this.precision, probeInformation.getPrecision());
 assertEquals(this.xmlRpcUrl, probeInformation.getXmlRpcUrl());

 assertNotSame(this.targetName, probeInformation1.getTargetName());
 assertNotSame(this.targetType, probeInformation1.getTargetType());
 assertNotSame(this.bmClass, probeInformation1.getBmClass());
 assertNotSame(this.bmName, probeInformation1.getBmName());
 assertNotSame(this.bmDescription, probeInformation1.getBmDescription());
 assertNotSame(this.probeDescription, probeInformation1.getProbeName());
 assertNotSame(this.precision, probeInformation1.getPrecision());
 assertNotSame(this.xmlRpcUrl, probeInformation1.getXmlRpcUrl());
 }

 @Test
 public void testGetTargetType() {
 ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 assertEquals("TargetType1", probeInformation.getTargetType());
 }

 @Test
 public void testGetBmName() {
 ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 assertEquals("BMName1", probeInformation.getBmName());
 }

 @Test
 public void testGetProbeName() {
 ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 assertEquals("ProbeDescription1", probeInformation.getProbeName());
 }

 @Test
 public void testGetTargetName() {
 ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 assertEquals("TargetName1", probeInformation.getTargetName());
 }

 @Test
 public void testGetBmClass() {
 ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 assertEquals("BMClass1", probeInformation.getBmClass());
 }

 @Test
 public void testGetBmDescription() {
 ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 assertEquals("BMDescription1", probeInformation.getBmDescription());
 }

 @Test
 public void testGetPrecision() {
 ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 assertEquals(1, probeInformation.getPrecision());
 }

 @Test
 public void testGetMeasureURI() {
 ProbeInformation probeInformation = new ProbeInformation("TargetType", "TargetName", "BMClass", "BMName", "BMDescription", "ProbeDescription", 1, "XmlRpcUrl");
 String measureURI = probeInformation.getMeasureURI();
 String expectedMeasureURI = Const.createMeasureURI(probeInformation.getTargetType(), probeInformation.getTargetName(), probeInformation.getBmClass(), probeInformation.getBmName());
 assertEquals(expectedMeasureURI, measureURI);
 }

 @Test
 public void testGetXmlRpcUrl() {
 ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 assertEquals("XmlRpcUrl1", probeInformation.getXmlRpcUrl());
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullTargetName() {
 ProbeInformation probeInformation = new ProbeInformation(null, "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullTargetType() {
 ProbeInformation probeInformation = new ProbeInformation("TargetName1", null, "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullBmClass() {
 ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", null, "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullBmName() {
 ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", null, "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullBmDescription() {
 ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", null, "ProbeDescription1", 1, "XmlRpcUrl1");
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullProbeDescription() {
 ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", null, 1, "XmlRpcUrl1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithEmptyTargetName() {
 ProbeInformation probeInformation = new ProbeInformation("", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithEmptyTargetType() {
 ProbeInformation probeInformation = new ProbeInformation("TargetName1", "", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithEmptyBmClass() {
 ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "", "BMName1", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithEmptyBmName() {
 ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "", "BMDescription1", "ProbeDescription1", 1, "XmlRpcUrl1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithEmptyBmDescription() {
 ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "", "ProbeDescription1", 1, "XmlRpcUrl1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNegativePrecision() {
 ProbeInformation probeInformation = new ProbeInformation("TargetName1", "TargetType1", "BMClass1", "BMName1", "BMDescription1", "ProbeDescription1", -1, "XmlRpcUrl1");
 }
}