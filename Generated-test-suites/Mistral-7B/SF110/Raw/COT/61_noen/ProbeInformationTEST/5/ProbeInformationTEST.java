import org.junit.Assert;import org.junit.Test;import fi.vtt.noen.mfw.bundle.probe.shared.ProbeInformation;

public class ProbeInformationTest {

 private String targetName = "TestTargetName";
 private String targetType = "TestTargetType";
 private String bmClass = "TestBMClass";
 private String bmName = "TestBMName";
 private String bmDescription = "TestBMDescription";
 private String probeName = "TestProbeName";
 private int precision = 1;
 private String xmlRpcUrl = "TestXmlRpcUrl";

 @Test
 public void testGetTargetType() {
 ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 String actualTargetType = probeInformation.getTargetType();
 Assert.assertEquals("Unexpected targetType", targetType, actualTargetType);
 }

 @Test
 public void testGetBmName() {
 ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 String actualBmName = probeInformation.getBmName();
 Assert.assertEquals("Unexpected bmName", bmName, actualBmName);
 }

 @Test
 public void testGetProbeName() {
 ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 String actualProbeName = probeInformation.getProbeName();
 Assert.assertEquals("Unexpected probeName", probeName, actualProbeName);
 }

 @Test
 public void testGetTargetName() {
 ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 String actualTargetName = probeInformation.getTargetName();
 Assert.assertEquals("Unexpected targetName", targetName, actualTargetName);
 }

 @Test
 public void testGetBmClass() {
 ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 String actualBmClass = probeInformation.getBmClass();
 Assert.assertEquals("Unexpected bmClass", bmClass, actualBmClass);
 }

 @Test
 public void testGetBmDescription() {
 ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 String actualBmDescription = probeInformation.getBmDescription();
 Assert.assertEquals("Unexpected bmDescription", bmDescription, actualBmDescription);
 }

 @Test
 public void testGetPrecision() {
 ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 int actualPrecision = probeInformation.getPrecision();
 Assert.assertEquals("Unexpected precision", precision, actualPrecision);
 }

 @Test
 public void testGetMeasureURI() {
 ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 String actualMeasureURI = probeInformation.getMeasureURI();
 String expectedMeasureURI = Const.createMeasureURI(targetType, targetName, bmClass, bmName);
 Assert.assertEquals("Unexpected measureURI", expectedMeasureURI, actualMeasureURI);
 }

 @Test
 public void testGetXmlRpcUrl() {
 ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 String actualXmlRpcUrl = probeInformation.getXmlRpcUrl();
 Assert.assertEquals("Unexpected xmlRpcUrl", xmlRpcUrl, actualXmlRpcUrl);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorNullTargetName() {
 ProbeInformation probeInformation = new ProbeInformation(null, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorNullTargetType() {
 ProbeInformation probeInformation = new ProbeInformation(targetName, null, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorNullBmClass() {
 ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, null, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorNullBmName() {
 ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, null, bmDescription, probeName, precision, xmlRpcUrl);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorNullBmDescription() {
 ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, null, probeName, precision, xmlRpcUrl);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorNullProbeName() {
 ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, null, precision, xmlRpcUrl);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorEmptyTargetName() {
 ProbeInformation probeInformation = new ProbeInformation("", targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorEmptyTargetType() {
 ProbeInformation probeInformation = new ProbeInformation(targetName, "", bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorEmptyBmClass() {
 ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, "", bmName, bmDescription, probeName, precision, xmlRpcUrl);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorEmptyBmName() {
 ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, "", bmDescription, probeName, precision, xmlRpcUrl);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorEmptyBmDescription() {
 ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, "", probeName, precision, xmlRpcUrl);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorEmptyProbeName() {
 ProbeInformation probeInformation = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, "", precision, xmlRpcUrl);
 }

 @Test
 public void testEqualsSameObject() {
 ProbeInformation probeInformation1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 ProbeInformation probeInformation2 = probeInformation1;
 boolean actualSame = probeInformation1.equals(probeInformation2);
 Assert.assertTrue("Unexpected equals result", actualSame);
 }

 @Test
 public void testEqualsNullObject() {
 ProbeInformation probeInformation1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 boolean actualNull = probeInformation1.equals(null);
 Assert.assertFalse("Unexpected equals result", actualNull);
 }

 @Test
 public void testEqualsDifferentClass() {
 ProbeInformation probeInformation1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 Object differentObject = new Object();
 boolean actualDifferentClass = probeInformation1.equals(differentObject);
 Assert.assertFalse("Unexpected equals result", actualDifferentClass);
 }

 @Test
 public void testEqualsDifferentTargetName() {
 ProbeInformation probeInformation1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 ProbeInformation probeInformation2 = new ProbeInformation("DifferentTargetName", targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 boolean actualDifferentTargetName = probeInformation1.equals(probeInformation2);
 Assert.assertFalse("Unexpected equals result", actualDifferentTargetName);
 }

 @Test
 public void testEqualsDifferentTargetType() {
 ProbeInformation probeInformation1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 ProbeInformation probeInformation2 = new ProbeInformation(targetName, "DifferentTargetType", bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 boolean actualDifferentTargetType = probeInformation1.equals(probeInformation2);
 Assert.assertFalse("Unexpected equals result", actualDifferentTargetType);
 }

 @Test
 public void testEqualsDifferentBmClass() {
 ProbeInformation probeInformation1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 ProbeInformation probeInformation2 = new ProbeInformation(targetName, targetType, "DifferentBmClass", bmName, bmDescription, probeName, precision, xmlRpcUrl);
 boolean actualDifferentBmClass = probeInformation1.equals(probeInformation2);
 Assert.assertFalse("Unexpected equals result", actualDifferentBmClass);
 }

 @Test
 public void testEqualsDifferentBmName() {
 ProbeInformation probeInformation1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 ProbeInformation probeInformation2 = new ProbeInformation(targetName, targetType, bmClass, "DifferentBmName", bmDescription, probeName, precision, xmlRpcUrl);
 boolean actualDifferentBmName = probeInformation1.equals(probeInformation2);
 Assert.assertFalse("Unexpected equals result", actualDifferentBmName);
 }

 @Test
 public void testEqualsDifferentBmDescription() {
 ProbeInformation probeInformation1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 ProbeInformation probeInformation2 = new ProbeInformation(targetName, targetType, bmClass, bmName, "DifferentBmDescription", probeName, precision, xmlRpcUrl);
 boolean actualDifferentBmDescription = probeInformation1.equals(probeInformation2);
 Assert.assertFalse("Unexpected equals result", actualDifferentBmDescription);
 }

 @Test
 public void testEqualsDifferentProbeName() {
 ProbeInformation probeInformation1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 ProbeInformation probeInformation2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, "DifferentProbeName", precision, xmlRpcUrl);
 boolean actualDifferentProbeName = probeInformation1.equals(probeInformation2);
 Assert.assertFalse("Unexpected equals result", actualDifferentProbeName);
 }

 @Test
 public void testEqualsSameValues() {
 ProbeInformation probeInformation1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 ProbeInformation probeInformation2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 boolean actualSameValues = probeInformation1.equals(probeInformation2);
 Assert.assertTrue("Unexpected equals result", actualSameValues);
 }

 @Test
 public void testHashCode() {
 ProbeInformation probeInformation1 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 int actualHashCode1 = probeInformation1.hashCode();
 ProbeInformation probeInformation2 = new ProbeInformation(targetName, targetType, bmClass, bmName, bmDescription, probeName, precision, xmlRpcUrl);
 int actualHashCode2 = probeInformation2.hashCode();
 Assert.assertEquals("Unexpected hashCode result", actualHashCode1, actualHashCode2);
 }

}